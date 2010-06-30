/*
    Copyright (c) 2010, NullNoname
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright
          notice, this list of conditions and the following disclaimer.
        * Redistributions in binary form must reproduce the above copyright
          notice, this list of conditions and the following disclaimer in the
          documentation and/or other materials provided with the distribution.
        * Neither the name of NullNoname nor the names of its
          contributors may be used to endorse or promote products derived from
          this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.
*/
package org.game_host.hebo.nullpomino.game.subsystem.ai;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;
import org.game_host.hebo.nullpomino.game.component.Controller;
import org.game_host.hebo.nullpomino.game.component.Field;
import org.game_host.hebo.nullpomino.game.component.Piece;
import org.game_host.hebo.nullpomino.game.component.WallkickResult;
import org.game_host.hebo.nullpomino.game.play.GameEngine;
import org.game_host.hebo.nullpomino.game.play.GameManager;
import org.game_host.hebo.nullpomino.tool.airanksgenerator.Ranks;

public class RanksAI extends DummyAI implements Runnable {

	static Logger log = Logger.getLogger(RanksAI.class);

	
	public boolean bestHold;

	
	public int bestX;

	public int bestY;

	
	public int bestRt;

	
	public int bestXSub;

	
	public int bestYSub;

	
	public int bestRtSub;


	public int bestPts;

	
	public boolean forceHold;

	
	public int delay;

	
	public GameEngine gEngine;

	
	public GameManager gManager;

	
	public boolean thinkRequest;

	
	public boolean thinking;

	
	public int thinkDelay;

	
	public int thinkCurrentPieceNo;

	
	public int thinkLastPieceNo;

	
	public volatile boolean threadRunning;

	
	public Thread thread;

	private Ranks ranks;
	@Override
	public String getName() {
		return "RANKS";
	}


	@Override
	public void init(GameEngine engine, int playerID) {
		delay = 0;
		gEngine = engine;
		gManager = engine.owner;
		thinkRequest = false;
		thinking = false;
		threadRunning = false;

		if( ((thread == null) || !thread.isAlive()) && (engine.aiUseThread) ) {
			thread = new Thread(this, "AI_" + playerID);
			thread.setDaemon(true);
			thread.start();
			thinkDelay = engine.aiThinkDelay;
			thinkCurrentPieceNo = 0;
			thinkLastPieceNo = 0;
		}
		String inputFile="ranks.bin";
		FileInputStream fis = null;
		ObjectInputStream in = null;
		if (inputFile.trim().isEmpty())
			ranks=new Ranks(4,9);
		else {
			  try {
				fis = new FileInputStream(inputFile);
				   in = new ObjectInputStream(fis);
				   ranks = (Ranks)in.readObject();
				   in.close();
				   
			} catch (FileNotFoundException e) {
				ranks=new Ranks(4,9);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	@Override
	public void shutdown(GameEngine engine, int playerID) {
		ranks=null;
		if((thread != null) && (thread.isAlive())) {
			thread.interrupt();
			threadRunning = false;
			thread = null;
		}
	}

	
	@Override
	public void newPiece(GameEngine engine, int playerID) {
		if(!engine.aiUseThread) {
			thinkBestPosition(engine, playerID);
		} else {
			thinkRequest = true;
			thinkCurrentPieceNo++;
		}
	}

	
	@Override
	public void onFirst(GameEngine engine, int playerID) {
	}

	
	@Override
	public void onLast(GameEngine engine, int playerID) {
	}

	
	@Override
	public void setControl(GameEngine engine, int playerID, Controller ctrl) {
		if( (engine.nowPieceObject != null) && (engine.stat == GameEngine.STAT_MOVE) && (delay >= engine.aiMoveDelay) && (engine.statc[0] > 0) &&
		    (!engine.aiUseThread || (threadRunning && !thinking && (thinkCurrentPieceNo <= thinkLastPieceNo))) )
		{
			int input = 0;	
			Piece pieceNow = engine.nowPieceObject;
			int nowX = engine.nowPieceX;
			int nowY = engine.nowPieceY;
			int rt = pieceNow.direction;
			Field fld = engine.field;
			boolean pieceTouchGround = pieceNow.checkCollision(nowX, nowY + 1, fld);

			if((bestHold || forceHold) && engine.isHoldOK()) {
				
				input |= Controller.BUTTON_BIT_D;
			} else {
				
				if(rt != bestRt) {
					int lrot = engine.getRotateDirection(-1);
					int rrot = engine.getRotateDirection(1);

					if((Math.abs(rt - bestRt) == 2) && (engine.ruleopt.rotateButtonAllowDouble) && !ctrl.isPress(Controller.BUTTON_E)) {
						input |= Controller.BUTTON_BIT_E;
					} else if(!ctrl.isPress(Controller.BUTTON_B) && engine.ruleopt.rotateButtonAllowReverse &&
							  !engine.isRotateButtonDefaultRight() && (bestRt == rrot)) {
						input |= Controller.BUTTON_BIT_B;
					} else if(!ctrl.isPress(Controller.BUTTON_B) && engine.ruleopt.rotateButtonAllowReverse &&
							  engine.isRotateButtonDefaultRight() && (bestRt == lrot)) {
						input |= Controller.BUTTON_BIT_B;
					} else if(!ctrl.isPress(Controller.BUTTON_A)) {
						input |= Controller.BUTTON_BIT_A;
					}
				}

				
				int minX = pieceNow.getMostMovableLeft(nowX, nowY, rt, fld);
				int maxX = pieceNow.getMostMovableRight(nowX, nowY, rt, fld);

				if( ((bestX < minX - 1) || (bestX > maxX + 1) || (bestY < nowY)) && (rt == bestRt) ) {
					
					thinkRequest = true;
					} else {
					
					if((nowX == bestX) && (pieceTouchGround) && (rt == bestRt)) {
						
						if(bestRtSub != -1) {
							bestRt = bestRtSub;
							bestRtSub = -1;
						}
						
						if(bestX != bestXSub) {
							bestX = bestXSub;
							bestY = bestYSub;
						}
					}

					if(nowX > bestX) {
						
						if(!ctrl.isPress(Controller.BUTTON_LEFT) || (engine.aiMoveDelay >= 0))
							input |= Controller.BUTTON_BIT_LEFT;
					} else if(nowX < bestX) {
						
						if(!ctrl.isPress(Controller.BUTTON_RIGHT) || (engine.aiMoveDelay >= 0))
							input |= Controller.BUTTON_BIT_RIGHT;
					} else if((nowX == bestX) && (rt == bestRt)) {
						
						if((bestRtSub == -1) && (bestX == bestXSub)) {
							if(engine.ruleopt.harddropEnable && !ctrl.isPress(Controller.BUTTON_UP))
								input |= Controller.BUTTON_BIT_UP;
							else if(engine.ruleopt.softdropEnable || engine.ruleopt.softdropLock)
								input |= Controller.BUTTON_BIT_DOWN;
						} else {
							if(engine.ruleopt.harddropEnable && !engine.ruleopt.harddropLock && !ctrl.isPress(Controller.BUTTON_UP))
								input |= Controller.BUTTON_BIT_UP;
							else if(engine.ruleopt.softdropEnable && !engine.ruleopt.softdropLock)
								input |= Controller.BUTTON_BIT_DOWN;
						}
					}
				}
			}

			delay = 0;
			ctrl.setButtonBit(input);
		} else {
			delay++;
			ctrl.setButtonBit(0);
		}
	}

	/**
	 * 
	 * @param engine 
	 * @param playerID 
	 */
	public void thinkBestPosition(GameEngine engine, int playerID) {
		bestHold = false;
		bestX = 0;
		bestY = 0;
		bestRt = 0;
		bestXSub = 0;
		bestYSub = 0;
		bestRtSub = -1;
		bestPts = 0;
		forceHold = false;

		Piece pieceNow = engine.nowPieceObject;
		int nowX = engine.nowPieceX;
		int nowY = engine.nowPieceY;
		boolean holdOK = engine.isHoldOK();
		boolean holdEmpty = false;
		Piece pieceHold = engine.holdPieceObject;
		Piece pieceNext = engine.getNextObject(engine.nextPieceCount);
		if(pieceHold == null) {
			holdEmpty = true;
		}
		Field fld = new Field(engine.field);
		  if ((pieceNow.id==Piece.PIECE_I) && (engine.field.getHeight()-engine.field.getHighestBlockY()>6)&&(engine.field.getHeight()-engine.field.getHighestBlockY(9)==0)){
			  bestHold = false;
			  bestRt = 1;
				bestX =  pieceNow.getMostMovableRight(nowX, 2, bestRt, engine.field);;
				bestY = pieceNow.getBottom(bestX, nowY, bestRt, fld);;
				
				bestXSub = bestX;
				bestYSub = bestY;
				bestRtSub = -1;
				bestPts = Integer.MAX_VALUE;
		  }
		 else{
		for(int depth = 0; depth < getMaxThinkDepth(); depth++) {
			for(int rt = 0; rt < Piece.DIRECTION_COUNT; rt++) {
				nowY=2;
			
				int minX = pieceNow.getMostMovableLeft(nowX, nowY, rt, engine.field);
				int maxX = pieceNow.getMostMovableRight(nowX, nowY, rt, engine.field)-1;

				for(int x = minX; x <= maxX; x++) {
					fld.copy(engine.field);
					int y = pieceNow.getBottom(x, nowY, rt, fld);

					//if(!pieceNow.checkCollision(x, y, rt, fld)) {
						
						int pts = thinkMain(engine, x, y, rt, -1, fld, pieceNow, pieceNext, pieceHold, depth);

						if(pts >= bestPts) {
							bestHold = false;
							bestX = x;
							bestY = y;
							bestRt = rt;
							bestXSub = x;
							bestYSub = y;
							bestRtSub = -1;
							bestPts = pts;
						}

						
						

							
					
				}
				

			}
		  }
			
		}

		thinkLastPieceNo++;

		//System.out.println("X:" + bestX + " Y:" + bestY + " R:" + bestRt + " H:" + bestHold + " Pts:" + bestPts);
	}

	/**
	 * �?考ルー�?ン
	 * @param engine GameEngine
	 * @param x X座標
	 * @param y Y座標
	 * @param rt 方�?�
	 * @param rtOld 回転�?�?�方�?�（-1：�?��?�）
	 * @param fld フィールド（�?�ん�?��?�弄�?��?�も�?題�?��?�）
	 * @param piece ピース
	 * @param nextpiece NEXTピース
	 * @param holdpiece HOLDピース(null�?�場�?��?�り)
	 * @param depth 妥�?�レベル（0�?�らgetMaxThinkDepth()-1�?��?�）
	 * @return 評価得点
	 */
	public int thinkMain(GameEngine engine, int x, int y, int rt, int rtOld, Field fld, Piece piece, Piece nextpiece, Piece holdpiece, int depth) {
		
		int pts = 0;
		int beforeHoles=fld.getHowManyHoles();
		if(!piece.placeToField(x, y, rt, fld)) {
			return 0;
		}
		if (fld.getHowManyHoles()-beforeHoles>0) 
			return 0;
         int heights[]=new int [fld.getWidth()-1];
         for (int i=0;i<fld.getWidth()-1;i++){
        	 heights[i]=fld.getHeight()-fld.getHighestBlockY(i);
         }
         int surface[]=new int [fld.getWidth()-2];
         int maxJump=ranks.getMaxJump();
         for (int i=0;i<fld.getWidth()-2;i++){
        	 int diff=heights[i+1]-heights[i];
        	 if (diff>maxJump)
        		 diff=maxJump;
        	 if (diff<-maxJump)
        		 diff=-maxJump;
        	 surface[i]=diff;
         }
		
		pts=ranks.getRankValue(ranks.encode(surface));
		System.out.println("depth = "+depth+"piece= " +piece.id+" posx = "+x+" rotation = "+rt+" points = "+pts);
		return pts;
	}

	/**
	 * 最大妥�?�レベルを�?�得
	 * @return 最大妥�?�レベル
	 */
	public int getMaxThinkDepth() {
		return 1;
	}

	/*
	 * スレッド�?�処�?�
	 */
	public void run() {
		log.info("RanksAI: Thread start");
		threadRunning = true;

		while(threadRunning) {
			if(thinkRequest) {
				thinkRequest = false;
				thinking = true;
				try {
					thinkBestPosition(gEngine, gEngine.playerID);
				} catch (Throwable e) {
					log.debug("RanksAI: thinkBestPosition Failed", e);
				}
				thinking = false;
			}

			if(thinkDelay > 0) {
				try {
					Thread.sleep(thinkDelay);
				} catch (InterruptedException e) {
					break;
				}
			}
		}

		threadRunning = false;
		ranks=null;
		log.info("RanksAI: Thread end");
	}
}
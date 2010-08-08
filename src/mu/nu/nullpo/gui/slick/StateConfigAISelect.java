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
package mu.nu.nullpo.gui.slick;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import mu.nu.nullpo.game.subsystem.ai.AIPlayer;
import mu.nu.nullpo.util.GeneralUtil;

import org.apache.log4j.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * AI選択画面のステート
 */
public class StateConfigAISelect extends BasicGameState {
	/** このステートのID */
	public static final int ID = 8;

	/** 1画面に表示する最大AI数 */
	public static final int MAX_AI_IN_ONE_PAGE = 20;

	/** Log */
	static Logger log = Logger.getLogger(StateConfigAISelect.class);

	/** プレイヤーID */
	public int player = 0;

	/** AIのクラス一覧 */
	protected String[] aiPathList;

	/** AIの名前一覧 */
	protected String[] aiNameList;

	/** Current AIのクラス */
	protected String currentAI;

	/** AIのID */
	protected int aiID = 0;

	/** AIの移動間隔 */
	protected int aiMoveDelay = 0;

	/** AIの思考の待ち時間 */
	protected int aiThinkDelay = 0;

	/** AIでスレッドを使う */
	protected boolean aiUseThread = false;

	/** カーソル位置 */
	protected int cursor = 0;

	/** スクリーンショット撮影フラグ */
	protected boolean ssflag = false;

	/*
	 * このステートのIDを取得
	 */
	@Override
	public int getID() {
		return ID;
	}

	/*
	 * ステートのInitialization
	 */
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		try {
			BufferedReader in = new BufferedReader(new FileReader("config/list/ai.lst"));
			aiPathList = loadAIList(in);
			aiNameList = loadAINames(aiPathList);
			in.close();
		} catch (IOException e) {
			log.error("Failed to load AI list", e);
		}
	}

	/*
	 * このステートに入ったときの処理
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		currentAI = NullpoMinoSlick.propGlobal.getProperty(player + ".ai", "");
		aiMoveDelay = NullpoMinoSlick.propGlobal.getProperty(player + ".aiMoveDelay", 0);
		aiThinkDelay = NullpoMinoSlick.propGlobal.getProperty(player + ".aiThinkDelay", 0);
		aiUseThread = NullpoMinoSlick.propGlobal.getProperty(player + ".aiUseThread", true);

		aiID = -1;
		for(int i = 0; i < aiPathList.length; i++) {
			if(currentAI.equals(aiPathList[i])) aiID = i;
		}
	}

	/**
	 * AI一覧を読み込み
	 * @param bf 読み込み元のテキストファイル
	 * @return AI一覧
	 */
	public String[] loadAIList(BufferedReader bf) {
		ArrayList<String> aiArrayList = new ArrayList<String>();

		while(true) {
			String name = null;
			try {
				name = bf.readLine();
			} catch (Exception e) {
				break;
			}
			if(name == null) break;
			if(name.length() == 0) break;

			if(!name.startsWith("#"))
				aiArrayList.add(name);
		}

		String[] aiStringList = new String[aiArrayList.size()];
		for(int i = 0; i < aiArrayList.size(); i++) aiStringList[i] = aiArrayList.get(i);

		return aiStringList;
	}

	/**
	 * AIの名前一覧を作成
	 * @param aiPath AIのクラスのリスト
	 * @return AIの名前一覧
	 */
	public String[] loadAINames(String[] aiPath) {
		String[] aiName = new String[aiPath.length];

		for(int i = 0; i < aiPath.length; i++) {
			Class<AIPlayer> aiClass;
			AIPlayer aiObj;
			aiName[i] = "(INVALID)";

			try {
				aiClass = (Class<AIPlayer>) Class.forName(aiPath[i]);
				aiObj = aiClass.newInstance();
				aiName[i] = aiObj.getName();
			} catch(ClassNotFoundException e) {
				log.error("AI class " + aiPath[i] + " not found", e);
			} catch(Throwable e) {
				log.error("AI class " + aiPath[i] + " load failed", e);
			}
		}

		return aiName;
	}

	/*
	 * 画面描画
	 */
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// 背景
		g.drawImage(ResourceHolder.imgMenu, 0, 0);

		// Menu
		NormalFont.printFontGrid(1, 1, (player + 1) + "P AI SETTING", NormalFont.COLOR_ORANGE);

		NormalFont.printFontGrid(1, 3 + cursor, "b", NormalFont.COLOR_RED);

		String aiName = "";
		if(aiID < 0) aiName = "(DISABLE)";
		else aiName = aiNameList[aiID].toUpperCase();
		NormalFont.printFontGrid(2, 3, "AI TYPE:" + aiName, (cursor == 0));
		NormalFont.printFontGrid(2, 4, "AI MOVE DELAY:" + aiMoveDelay, (cursor == 1));
		NormalFont.printFontGrid(2, 5, "AI THINK DELAY:" + aiThinkDelay, (cursor == 2));
		NormalFont.printFontGrid(2, 6, "AI USE THREAD:" + GeneralUtil.getONorOFF(aiUseThread), (cursor == 3));

		NormalFont.printFontGrid(1, 28, "A:OK B:CANCEL", NormalFont.COLOR_GREEN);

		// FPS
		NullpoMinoSlick.drawFPS(container);
		// オブザーバー
		NullpoMinoSlick.drawObserverClient();
		// スクリーンショット
		if(ssflag) {
			NullpoMinoSlick.saveScreenShot(container, g);
			ssflag = false;
		}

		if(!NullpoMinoSlick.alternateFPSTiming) NullpoMinoSlick.alternateFPSSleep();
	}

	/*
	 * ゲーム状態の更新
	 */
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(!container.hasFocus()) {
			if(NullpoMinoSlick.alternateFPSTiming) NullpoMinoSlick.alternateFPSSleep();
			return;
		}

		// キー入力状態を更新
		GameKey.gamekey[0].update(container.getInput());

		// カーソル移動
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_UP)) {
			cursor--;
			if(cursor < 0) cursor = 3;
			ResourceHolder.soundManager.play("cursor");
		}
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_DOWN)) {
			cursor++;
			if(cursor > 3) cursor = 0;
			ResourceHolder.soundManager.play("cursor");
		}

		// Configuration changes
		int change = 0;
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_LEFT)) change = -1;
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_RIGHT)) change = 1;

		if(change != 0) {
			ResourceHolder.soundManager.play("change");

			switch(cursor) {
			case 0:
				aiID += change;
				if(aiID < -1) aiID = aiNameList.length - 1;
				if(aiID > aiNameList.length - 1) aiID = -1;
				break;
			case 1:
				aiMoveDelay += change;
				if(aiMoveDelay < -1) aiMoveDelay = 99;
				if(aiMoveDelay > 99) aiMoveDelay = -1;
				break;
			case 2:
				aiThinkDelay += change * 10;
				if(aiThinkDelay < 0) aiThinkDelay = 1000;
				if(aiThinkDelay > 1000) aiThinkDelay = 0;
				break;
			case 3:
				aiUseThread = !aiUseThread;
				break;
			}
		}

		// 決定ボタン
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_A)) {
			ResourceHolder.soundManager.play("decide");

			if(aiID >= 0) NullpoMinoSlick.propGlobal.setProperty(player + ".ai", aiPathList[aiID]);
			else NullpoMinoSlick.propGlobal.setProperty(player + ".ai", "");
			NullpoMinoSlick.propGlobal.setProperty(player + ".aiMoveDelay", aiMoveDelay);
			NullpoMinoSlick.propGlobal.setProperty(player + ".aiThinkDelay", aiThinkDelay);
			NullpoMinoSlick.propGlobal.setProperty(player + ".aiUseThread", aiUseThread);
			NullpoMinoSlick.saveConfig();

			game.enterState(StateConfigMainMenu.ID);
			return;
		}

		// Cancelボタン
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_B)) {
			game.enterState(StateConfigMainMenu.ID);
			return;
		}

		// スクリーンショットボタン
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_SCREENSHOT)) ssflag = true;

		// 終了ボタン
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_QUIT)) container.exit();

		if(NullpoMinoSlick.alternateFPSTiming) NullpoMinoSlick.alternateFPSSleep();
	}
}
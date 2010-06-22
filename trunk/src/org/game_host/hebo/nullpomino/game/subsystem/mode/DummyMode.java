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
package org.game_host.hebo.nullpomino.game.subsystem.mode;

import org.game_host.hebo.nullpomino.game.component.Block;
import org.game_host.hebo.nullpomino.game.play.GameEngine;
import org.game_host.hebo.nullpomino.game.play.GameManager;
import org.game_host.hebo.nullpomino.util.CustomProperties;

/**
 * ダミーのゲームモード
 */
public class DummyMode implements GameMode {
	public void pieceLocked(GameEngine engine, int playerID, int lines) {
	}

	public boolean lineClearEnd(GameEngine engine, int playerID) {
		return false;
	}

	public void afterHardDropFall(GameEngine engine, int playerID, int fall) {
	}

	public void afterSoftDropFall(GameEngine engine, int playerID, int fall) {
	}

	public void blockBreak(GameEngine engine, int playerID, int x, int y, Block blk) {
	}

	public void calcScore(GameEngine engine, int playerID, int lines) {
	}

	public void fieldEditExit(GameEngine engine, int playerID) {}

	public String getName() {
		return "DUMMY";
	}

	public int getPlayers() {
		return 1;
	}

	public void loadReplay(GameEngine engine, int playerID, CustomProperties prop) {
	}

	public void modeInit(GameManager manager) {
	}

	public boolean onARE(GameEngine engine, int playerID) {
		return false;
	}

	public boolean onCustom(GameEngine engine, int playerID) {
		return false;
	}

	public boolean onEndingStart(GameEngine engine, int playerID) {
		return false;
	}

	public boolean onExcellent(GameEngine engine, int playerID) {
		return false;
	}

	public void onFirst(GameEngine engine, int playerID) {
	}

	public boolean onGameOver(GameEngine engine, int playerID) {
		return false;
	}

	public void onLast(GameEngine engine, int playerID) {
	}

	public boolean onLineClear(GameEngine engine, int playerID) {
		return false;
	}

	public boolean onLockFlash(GameEngine engine, int playerID) {
		return false;
	}

	public boolean onMove(GameEngine engine, int playerID) {
		return false;
	}

	public boolean onReady(GameEngine engine, int playerID) {
		return false;
	}

	public boolean onResult(GameEngine engine, int playerID) {
		return false;
	}

	public boolean onSetting(GameEngine engine, int playerID) {
		return false;
	}

	public boolean onFieldEdit(GameEngine engine, int playerID) {
		return false;
	}

	public void playerInit(GameEngine engine, int playerID) {
	}

	public void renderARE(GameEngine engine, int playerID) {
	}

	public void renderCustom(GameEngine engine, int playerID) {
	}

	public void renderEndingStart(GameEngine engine, int playerID) {
	}

	public void renderExcellent(GameEngine engine, int playerID) {
	}

	public void renderFirst(GameEngine engine, int playerID) {
	}

	public void renderGameOver(GameEngine engine, int playerID) {
	}

	public void renderLast(GameEngine engine, int playerID) {
	}

	public void renderLineClear(GameEngine engine, int playerID) {
	}

	public void renderLockFlash(GameEngine engine, int playerID) {
	}

	public void renderMove(GameEngine engine, int playerID) {
	}

	public void renderReady(GameEngine engine, int playerID) {
	}

	public void renderResult(GameEngine engine, int playerID) {
	}

	public void renderSetting(GameEngine engine, int playerID) {
	}

	public void renderFieldEdit(GameEngine engine, int playerID) {
	}

	public void saveReplay(GameEngine engine, int playerID, CustomProperties prop) {
	}

	public void startGame(GameEngine engine, int playerID) {
	}

	public boolean isNetplayMode() {
		return false;
	}

	public void netplayInit(Object obj) {
	}
}

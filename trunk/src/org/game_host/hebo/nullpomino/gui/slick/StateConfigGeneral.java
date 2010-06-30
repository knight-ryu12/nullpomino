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
package org.game_host.hebo.nullpomino.gui.slick;

import org.game_host.hebo.nullpomino.util.CustomProperties;
import org.game_host.hebo.nullpomino.util.GeneralUtil;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 全般の設定画面のステート
 */
public class StateConfigGeneral extends BasicGameState {
	/** このステートのID */
	public static final int ID = 6;

	/** ジョイスティック入力検出法の表示名 */
	protected static final String[] JOYSTICK_METHOD_STRINGS = {"NONE", "SLICK DEFAULT", "SLICK ALTERNATE", "LWJGL"};

	/** スクリーンショット撮影フラグ */
	protected boolean ssflag = false;

	/** カーソル位置 */
	protected int cursor = 0;

	/** フルスクリーンフラグ */
	protected boolean fullscreen;

	/** 効果音ON/OFF */
	protected boolean se;

	/** BGMのON/OFF */
	protected boolean bgm;

	/** BGMの事前読み込み */
	protected boolean bgmpreload;

	/** BGMストリーミングのON/OFF */
	protected boolean bgmstreaming;

	/** 背景表示 */
	protected boolean showbg;

	/** FPS表示 */
	protected boolean showfps;

	/** フレームステップ有効 */
	protected boolean enableframestep;

	/** 最大FPS */
	protected int maxfps;

	/** ライン消去エフェクト表示 */
	protected boolean showlineeffect;

	/** 重い演出を使う */
	protected boolean heavyeffect;

	/** フィールド背景の明るさ */
	protected int fieldbgbright;

	/** NEXT欄を暗くする */
	protected boolean darknextarea;

	/** 効果音ボリューム */
	protected int sevolume;

	/** BGMボリューム */
	protected int bgmvolume;

	/** ジョイスティック入力の検出法 */
	protected int joyMethod;

	/** フィールド右側にメーターを表示 */
	protected boolean showmeter;

	/** 垂直同期を待つ */
	protected boolean vsync;

	/** 別のFPS算出法を使う */
	protected boolean smoothdeltas;

	/** 独自のFPS維持法を使う */
	protected boolean useAlternateFPSSleep;

	/** ゴーストピースの上にNEXT表示 */
	protected boolean nextshadow;

	/** 枠線型ゴーストピース */
	protected boolean outlineghost;

	/*
	 * このステートのIDを取得
	 */
	@Override
	public int getID() {
		return ID;
	}

	/*
	 * ステートの初期化
	 */
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		loadConfig(NullpoMinoSlick.propConfig);
	}

	/**
	 * 設定読み込み
	 * @param prop 読み込み元のプロパティファイル
	 */
	protected void loadConfig(CustomProperties prop) {
		fullscreen = prop.getProperty("option.fullscreen", false);
		se = prop.getProperty("option.se", true);
		bgm = prop.getProperty("option.bgm", false);
		bgmpreload = prop.getProperty("option.bgmpreload", false);
		bgmstreaming = prop.getProperty("option.bgmstreaming", true);
		showbg = prop.getProperty("option.showbg", true);
		showfps = prop.getProperty("option.showfps", true);
		enableframestep = prop.getProperty("option.enableframestep", false);
		maxfps = prop.getProperty("option.maxfps", 60);
		showlineeffect = prop.getProperty("option.showlineeffect", true);
		heavyeffect = prop.getProperty("option.heavyeffect", false);
		fieldbgbright = prop.getProperty("option.fieldbgbright", 64);
		darknextarea = prop.getProperty("option.darknextarea", true);
		sevolume = prop.getProperty("option.sevolume", 128);
		bgmvolume = prop.getProperty("option.bgmvolume", 128);
		joyMethod = prop.getProperty("option.joymethod", ControllerManager.CONTROLLER_METHOD_SLICK_DEFAULT);
		showmeter = prop.getProperty("option.showmeter", true);
		vsync = prop.getProperty("option.vsync", false);
		smoothdeltas = prop.getProperty("option.smoothdeltas", false);
		useAlternateFPSSleep = prop.getProperty("option.useAlternateFPSSleep", false);
		nextshadow = prop.getProperty("option.nextshadow", false);
		outlineghost = prop.getProperty("option.outlineghost", false);
	}

	/**
	 * 設定保存
	 * @param prop 保存先のプロパティファイル
	 */
	protected void saveConfig(CustomProperties prop) {
		prop.setProperty("option.fullscreen", fullscreen);
		prop.setProperty("option.se", se);
		prop.setProperty("option.bgm", bgm);
		prop.setProperty("option.bgmpreload", bgmpreload);
		prop.setProperty("option.bgmstreaming", bgmstreaming);
		prop.setProperty("option.showbg", showbg);
		prop.setProperty("option.showfps", showfps);
		prop.setProperty("option.enableframestep", enableframestep);
		prop.setProperty("option.maxfps", maxfps);
		prop.setProperty("option.showlineeffect", showlineeffect);
		prop.setProperty("option.heavyeffect", heavyeffect);
		prop.setProperty("option.fieldbgbright", fieldbgbright);
		prop.setProperty("option.darknextarea", darknextarea);
		prop.setProperty("option.sevolume", sevolume);
		prop.setProperty("option.bgmvolume", bgmvolume);
		prop.setProperty("option.joymethod", joyMethod);
		prop.setProperty("option.showmeter", showmeter);
		prop.setProperty("option.vsync", vsync);
		prop.setProperty("option.smoothdeltas", smoothdeltas);
		prop.setProperty("option.useAlternateFPSSleep", useAlternateFPSSleep);
		prop.setProperty("option.nextshadow", nextshadow);
		prop.setProperty("option.outlineghost", outlineghost);
	}

	/*
	 * 画面描画
	 */
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// 背景
		g.drawImage(ResourceHolder.imgMenu, 0, 0);

		// メニュー
		NormalFont.printFontGrid(1, 1, "GENERAL OPTIONS", NormalFont.COLOR_ORANGE);

		NormalFont.printFontGrid(1, 3 + cursor, "b", NormalFont.COLOR_RED);

		NormalFont.printFontGrid(2,  3, "FULLSCREEN:" + GeneralUtil.getOorX(fullscreen), (cursor == 0));
		NormalFont.printFontGrid(2,  4, "SE:" + GeneralUtil.getOorX(se), (cursor == 1));
		NormalFont.printFontGrid(2,  5, "BGM:" + GeneralUtil.getOorX(bgm), (cursor == 2));
		NormalFont.printFontGrid(2,  6, "BGM PRELOAD:" + GeneralUtil.getOorX(bgmpreload), (cursor == 3));
		NormalFont.printFontGrid(2,  7, "BGM STREAMING:" + GeneralUtil.getOorX(bgmstreaming), (cursor == 4));
		NormalFont.printFontGrid(2,  8, "SHOW BACKGROUND:" + GeneralUtil.getOorX(showbg), (cursor == 5));
		NormalFont.printFontGrid(2,  9, "SHOW FPS:" + GeneralUtil.getOorX(showfps), (cursor == 6));
		NormalFont.printFontGrid(2, 10, "FRAME STEP:" + GeneralUtil.getOorX(enableframestep), (cursor == 7));
		NormalFont.printFontGrid(2, 11, "MAX FPS:" + maxfps, (cursor == 8));
		NormalFont.printFontGrid(2, 12, "SHOW LINE EFFECT:" + GeneralUtil.getOorX(showlineeffect), (cursor == 9));
		NormalFont.printFontGrid(2, 13, "USE BACKGROUND FADE:" + GeneralUtil.getOorX(heavyeffect), (cursor == 10));
		NormalFont.printFontGrid(2, 14, "FIELD BG BRIGHT:" + fieldbgbright, (cursor == 11));
		NormalFont.printFontGrid(2, 15, "DARK NEXT AREA:" + GeneralUtil.getOorX(darknextarea), (cursor == 12));
		NormalFont.printFontGrid(2, 16, "SE VOLUME:" + sevolume, (cursor == 13));
		NormalFont.printFontGrid(2, 17, "BGM VOLUME:" + bgmvolume, (cursor == 14));
		NormalFont.printFontGrid(2, 18, "JOYSTICK METHOD:" + JOYSTICK_METHOD_STRINGS[joyMethod], (cursor == 15));
		NormalFont.printFontGrid(2, 19, "SHOW METER:" + GeneralUtil.getOorX(showmeter), (cursor == 16));
		NormalFont.printFontGrid(2, 20, "VSYNC:" + GeneralUtil.getOorX(vsync), (cursor == 17));
		NormalFont.printFontGrid(2, 21, "SMOOTH DELTAS:" + GeneralUtil.getOorX(smoothdeltas), (cursor == 18));
		NormalFont.printFontGrid(2, 22, "FRAMERATE MODE:" + (useAlternateFPSSleep ? "CUSTOM (SDL&SWING)" : "SLICK DEFAULT"), (cursor == 19));
		NormalFont.printFontGrid(2, 23, "SHOW NEXT ABOVE SHADOW:" + GeneralUtil.getOorX(nextshadow), (cursor == 20));
		NormalFont.printFontGrid(2, 24, "OUTLINE GHOST PIECE:" + GeneralUtil.getOorX(outlineghost), (cursor == 21));

		if(cursor == 0) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_Fullscreen"));
		if(cursor == 1) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_SE"));
		if(cursor == 2) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_BGM"));
		if(cursor == 3) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_BGMPreload"));
		if(cursor == 4) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_BGMStreaming"));
		if(cursor == 5) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_Background"));
		if(cursor == 6) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_ShowFPS"));
		if(cursor == 7) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_FrameStep"));
		if(cursor == 8) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_MaxFPS"));
		if(cursor == 9) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_ShowLineEffect"));
		if(cursor == 10) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_UseBackgroundFade"));
		if(cursor == 11) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_FieldBGBright"));
		if(cursor == 12) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_DarkNextArea"));
		if(cursor == 13) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_SEVolume"));
		if(cursor == 14) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_BGMVolume"));
		if(cursor == 15) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_JoyMethod"));
		if(cursor == 16) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_ShowMeter"));
		if(cursor == 17) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_VSync"));
		if(cursor == 18) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_SmoothDeltas"));
		if(cursor == 19) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_UseAlternateFPSSleep"));
		if(cursor == 20) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_NextShadow"));
		if(cursor == 21) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText("ConfigGeneral_OutlineGhost"));

		// FPS
		NullpoMinoSlick.drawFPS(container);
		// オブザーバー
		NullpoMinoSlick.drawObserverClient();
		// スクリーンショット
		if(ssflag) {
			NullpoMinoSlick.saveScreenShot(container, g);
			ssflag = false;
		}

		NullpoMinoSlick.alternateFPSSleep();
	}

	/*
	 * ゲーム状態の更新
	 */
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// TTFフォント描画
		if(ResourceHolder.ttfFont != null) ResourceHolder.ttfFont.loadGlyphs();

		// キー入力状態を更新
		GameKey.gamekey[0].update(container.getInput());

		// カーソル移動
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_UP)) {
			cursor--;
			if(cursor < 0) cursor = 21;
			ResourceHolder.soundManager.play("cursor");
		}
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_DOWN)) {
			cursor++;
			if(cursor > 21) cursor = 0;
			ResourceHolder.soundManager.play("cursor");
		}

		// 設定変更
		int change = 0;
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_LEFT)) change = -1;
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_RIGHT)) change = 1;

		if(change != 0) {
			ResourceHolder.soundManager.play("change");

			switch(cursor) {
				case 0:
					fullscreen = !fullscreen;
					break;
				case 1:
					se = !se;
					break;
				case 2:
					bgm = !bgm;
					break;
				case 3:
					bgmpreload = !bgmpreload;
					break;
				case 4:
					bgmstreaming = !bgmstreaming;
					break;
				case 5:
					showbg = !showbg;
					break;
				case 6:
					showfps = !showfps;
					break;
				case 7:
					enableframestep = !enableframestep;
					break;
				case 8:
					maxfps += change;
					if(maxfps < 0) maxfps = 99;
					if(maxfps > 99) maxfps = 0;
					break;
				case 9:
					showlineeffect = !showlineeffect;
					break;
				case 10:
					heavyeffect = !heavyeffect;
					break;
				case 11:
					fieldbgbright += change;
					if(fieldbgbright < 0) fieldbgbright = 128;
					if(fieldbgbright > 128) fieldbgbright = 0;
					break;
				case 12:
					darknextarea = !darknextarea;
					break;
				case 13:
					sevolume += change;
					if(sevolume < 0) sevolume = 128;
					if(sevolume > 128) sevolume = 0;
					break;
				case 14:
					bgmvolume += change;
					if(bgmvolume < 0) bgmvolume = 128;
					if(bgmvolume > 128) bgmvolume = 0;
					break;
				case 15:
					joyMethod += change;
					if(joyMethod < 0) joyMethod = ControllerManager.CONTROLLER_METHOD_MAX - 1;
					if(joyMethod > ControllerManager.CONTROLLER_METHOD_MAX - 1) joyMethod = 0;
					break;
				case 16:
					showmeter = !showmeter;
					break;
				case 17:
					vsync = !vsync;
					break;
				case 18:
					smoothdeltas = !smoothdeltas;
					break;
				case 19:
					useAlternateFPSSleep = !useAlternateFPSSleep;
					break;
				case 20:
					nextshadow = !nextshadow;
					break;
				case 21:
					outlineghost = !outlineghost;
					break;
			}
		}

		// 決定ボタン
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_A)) {
			ResourceHolder.soundManager.play("decide");
			saveConfig(NullpoMinoSlick.propConfig);
			NullpoMinoSlick.saveConfig();
			NullpoMinoSlick.setGeneralConfig();
			game.enterState(StateConfigMainMenu.ID);
		}

		// キャンセルボタン
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_B)) {
			loadConfig(NullpoMinoSlick.propConfig);
			game.enterState(StateConfigMainMenu.ID);
		}

		// スクリーンショットボタン
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_SCREENSHOT)) ssflag = true;

		// 終了ボタン
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_QUIT)) container.exit();
	}
}
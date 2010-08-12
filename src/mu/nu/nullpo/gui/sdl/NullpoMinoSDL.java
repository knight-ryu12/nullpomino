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
package mu.nu.nullpo.gui.sdl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import mu.nu.nullpo.game.net.NetObserverClient;
import mu.nu.nullpo.util.CustomProperties;
import mu.nu.nullpo.util.ModeManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sdljava.SDLException;
import sdljava.SDLMain;
import sdljava.SDLVersion;
import sdljava.event.SDLEvent;
import sdljava.event.SDLKeyboardEvent;
import sdljava.event.SDLQuitEvent;
import sdljava.joystick.HatState;
import sdljava.joystick.SDLJoystick;
import sdljava.mixer.SDLMixer;
import sdljava.ttf.SDLTTF;
import sdljava.video.SDLRect;
import sdljava.video.SDLSurface;
import sdljava.video.SDLVideo;

/**
 * NullpoMino SDLVersion
 */
public class NullpoMinoSDL {
	/** Log */
	static Logger log = Logger.getLogger(NullpoMinoSDL.class);

	/** ゲームステートのID */
	public static final int STATE_TITLE = 0,
							STATE_CONFIG_MAINMENU = 1,
							STATE_CONFIG_RULESELECT = 2,
							STATE_CONFIG_GENERAL = 3,
							STATE_CONFIG_KEYBOARD = 4,
							STATE_CONFIG_JOYSTICK_BUTTON = 5,
							STATE_SELECTMODE = 6,
							STATE_INGAME = 7,
							STATE_REPLAYSELECT = 8,
							STATE_CONFIG_AISELECT = 9,
							STATE_NETGAME = 10,
							STATE_CONFIG_JOYSTICK_MAIN = 11,
							STATE_CONFIG_JOYSTICK_TEST = 12,
							STATE_CONFIG_GAMETUNING = 13;

	/** ゲームステートのcount */
	public static final int STATE_MAX = 14;

	/** 認識するキーの最大値 */
	public static final int SDL_KEY_MAX = 322;

	/** プログラムに渡されたコマンドLines引count */
	public static String[] programArgs;

	/** 設定保存用Property file */
	public static CustomProperties propConfig;

	/** 設定保存用Property file（全Version共通） */
	public static CustomProperties propGlobal;

	/** 音楽リストProperty file */
	public static CustomProperties propMusic;

	/** オブザーバー機能用Property file */
	public static CustomProperties propObserver;

	/** Default language file */
	public static CustomProperties propLangDefault;

	/** 言語ファイル */
	public static CustomProperties propLang;

	/** Mode 管理 */
	public static ModeManager modeManager;

	/** 終了 flag */
	public static boolean quit = false;

	/** FPS表示 */
	public static boolean showfps = true;

	/** FPS計算用 */
	protected static long calcInterval = 0;

	/** FPS計算用 */
	protected static long prevCalcTime = 0;

	/**  frame count */
	protected static long frameCount = 0;

	/** 実際のFPS */
	public static double actualFPS = 0.0;

	/** FPS表示用DecimalFormat */
	public static DecimalFormat df = new DecimalFormat("0.0");

	/** キーを押しているならtrue */
	public static boolean[] keyPressedState;

	/** 使用するジョイスティックの number */
	public static int[] joyUseNumber;

	/** ジョイスティックのアナログスティック無視 */
	public static boolean[] joyIgnoreAxis;

	/** ジョイスティックのハットスイッチ無視 */
	public static boolean[] joyIgnorePOV;

	/** ジョイスティックのcount */
	public static int joystickMax;

	/** ジョイスティック */
	public static SDLJoystick[] joystick;

	/** ジョイスティックのDirectionキー状態 */
	public static int[] joyAxisX, joyAxisY;

	/** ジョイスティックのハットスイッチのcount */
	public static int[] joyMaxHat;

	/** ジョイスティックのハットスイッチの状態 */
	public static HatState[] joyHatState;

	/** ジョイスティックの buttonのcount */
	public static int[] joyMaxButton;

	/** ジョイスティックの buttonを押しているならtrue */
	public static boolean[][] joyPressedState;

	/** ゲームステート */
	public static BaseStateSDL[] gameStates;

	/** Current ステート */
	public static int currentState;

	/** 終了 buttonやスクリーンショット buttonの使用許可 */
	public static boolean enableSpecialKeys;

	/** 終了 button使用許可 */
	public static boolean allowQuit;

	/** 最大FPS */
	public static int maxFPS;

	/** オブザーバークライアント */
	public static NetObserverClient netObserverClient;

	/**
	 * メイン関count
	 * @param args プログラムに渡された引count
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure("config/etc/log_sdl.cfg");
		log.info("NullpoMinoSDL Start");

		programArgs = args;
		propConfig = new CustomProperties();
		propGlobal = new CustomProperties();
		propMusic = new CustomProperties();

		// 設定ファイル読み込み
		try {
			FileInputStream in = new FileInputStream("config/setting/sdl.cfg");
			propConfig.load(in);
			in.close();
		} catch(IOException e) {}
		try {
			FileInputStream in = new FileInputStream("config/setting/global.cfg");
			propGlobal.load(in);
			in.close();
		} catch(IOException e) {}
		try {
			FileInputStream in = new FileInputStream("config/setting/music.cfg");
			propMusic.load(in);
			in.close();
		} catch(IOException e) {}

		// 言語ファイル読み込み
		propLangDefault = new CustomProperties();
		try {
			FileInputStream in = new FileInputStream("config/lang/sdl_default.properties");
			propLangDefault.load(in);
			in.close();
		} catch (IOException e) {
			log.error("Failed to load default UI language file", e);
		}

		propLang = new CustomProperties();
		try {
			FileInputStream in = new FileInputStream("config/lang/sdl_" + Locale.getDefault().getCountry() + ".properties");
			propLang.load(in);
			in.close();
		} catch(IOException e) {}

		// Mode読み込み
		modeManager = new ModeManager();
		try {
			BufferedReader txtMode = new BufferedReader(new FileReader("config/list/mode.lst"));
			modeManager.loadGameModes(txtMode);
			txtMode.close();
		} catch (IOException e) {
			log.error("Failed to load game mode list", e);
		}

		// キー入力のInitialization
		keyPressedState = new boolean[SDL_KEY_MAX];
		GameKeySDL.initGlobalGameKeySDL();
		GameKeySDL.gamekey[0].loadConfig(propConfig);
		GameKeySDL.gamekey[1].loadConfig(propConfig);
		MouseInputSDL.initalizeMouseInput();

		log.debug("Finished key initialization.");

		// ステートのInitialization
		try {
		currentState = -1;
		gameStates = new BaseStateSDL[STATE_MAX];
		gameStates[STATE_TITLE] = new StateTitleSDL();
		gameStates[STATE_CONFIG_MAINMENU] = new StateConfigMainMenuSDL();
		log.debug("Checkpoint 1");
		gameStates[STATE_CONFIG_RULESELECT] = new StateConfigRuleSelectSDL();
		log.debug("Checkpoint 2");
		gameStates[STATE_CONFIG_GENERAL] = new StateConfigGeneralSDL();
		log.debug("Checkpoint 3");
		gameStates[STATE_CONFIG_KEYBOARD] = new StateConfigKeyboardSDL();
		log.debug("Checkpoint 4");
		gameStates[STATE_CONFIG_JOYSTICK_BUTTON] = new StateConfigJoystickButtonSDL();
		log.debug("Checkpoint 5");
		gameStates[STATE_SELECTMODE] = new StateSelectModeSDL();
		gameStates[STATE_INGAME] = new StateInGameSDL();
		gameStates[STATE_REPLAYSELECT] = new StateReplaySelectSDL();
		gameStates[STATE_CONFIG_AISELECT] = new StateConfigAISelectSDL();
		log.debug("Checkpoint 6");
		gameStates[STATE_NETGAME] = new StateNetGameSDL();
		gameStates[STATE_CONFIG_JOYSTICK_MAIN] = new StateConfigJoystickMainSDL();
		gameStates[STATE_CONFIG_JOYSTICK_TEST] = new StateConfigJoystickTestSDL();
		gameStates[STATE_CONFIG_GAMETUNING] = new StateConfigGameTuningSDL();

		} catch (Throwable e) {
			log.fatal("Uncaught Exception", e);
		}
		log.debug("Finished state initialization.");

		// SDLのInitializationと開始
		try {
			init();
			log.debug("Finished SDL initialization.");
			run();
		} catch (Throwable e) {
			log.fatal("Uncaught Exception", e);
		} finally {
			shutdown();
		}

		System.exit(0);
	}

	/**
	 * SDLのInitialization
	 * @throws SDLException SDLのエラーが発生した場合
	 */
	public static void init() throws SDLException {
		log.info("Now initializing SDL...");

		SDLMain.init(SDLMain.SDL_INIT_VIDEO | SDLMain.SDL_INIT_AUDIO | SDLMain.SDL_INIT_JOYSTICK);

		SDLVersion ver = SDLMain.getSDLVersion();
		log.info("SDL Version:" + ver.getMajor() + "." + ver.getMinor() + "." + ver.getPatch());

		SDLVideo.wmSetCaption("NullpoMino (Now Loading...)", null);

		long flags = SDLVideo.SDL_ANYFORMAT | SDLVideo.SDL_DOUBLEBUF | SDLVideo.SDL_HWSURFACE;
		if(propConfig.getProperty("option.fullscreen", false) == true) flags |= SDLVideo.SDL_FULLSCREEN;
		SDLVideo.setVideoMode(640, 480, 0, flags);

		SDLTTF.init();
		SDLVersion ttfver = SDLTTF.getTTFVersion();
		log.info("TTF Version:" + ttfver.getMajor() + "." + ttfver.getMinor() + "." + ttfver.getPatch());

		SDLMixer.openAudio(44100, SDLMixer.AUDIO_S16SYS, 2, propConfig.getProperty("option.soundbuffer", 1024));

		joyUseNumber = new int[2];
		joyUseNumber[0] = propConfig.getProperty("joyUseNumber.p0", -1);
		joyUseNumber[1] = propConfig.getProperty("joyUseNumber.p1", -1);
		joyIgnoreAxis = new boolean[2];
		joyIgnoreAxis[0] = propConfig.getProperty("joyIgnoreAxis.p0", false);
		joyIgnoreAxis[1] = propConfig.getProperty("joyIgnoreAxis.p1", false);
		joyIgnorePOV = new boolean[2];
		joyIgnorePOV[0] = propConfig.getProperty("joyIgnorePOV.p0", false);
		joyIgnorePOV[1] = propConfig.getProperty("joyIgnorePOV.p1", false);

		joystickMax = SDLJoystick.numJoysticks();
		log.info("Number of Joysticks:" + joystickMax);

		if(joystickMax > 0) {
			joystick = new SDLJoystick[joystickMax];
			joyAxisX = new int[joystickMax];
			joyAxisY = new int[joystickMax];
			joyMaxHat = new int[joystickMax];
			joyMaxButton = new int[joystickMax];
			joyHatState = new HatState[joystickMax];

			int max = 0;

			for(int i = 0; i < joystickMax; i++) {
				try {
					joystick[i] = SDLJoystick.joystickOpen(i);

					joyMaxButton[i] = joystick[i].joystickNumButtons();
					if(joyMaxButton[i] > max) max = joyMaxButton[i];

					joyMaxHat[i] = joystick[i].joystickNumHats();
					joyHatState[i] = null;
				} catch (Throwable e) {
					log.warn("Failed to open Joystick #" + i, e);
				}
			}

			joyPressedState = new boolean[joystickMax][max];
		}
	}

	/**
	 * メインループ
	 * @throws SDLException SDLのエラーが発生した場合
	 */
	public static void run() throws SDLException {
		maxFPS = propConfig.getProperty("option.maxfps", 60);

		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;

		showfps = propConfig.getProperty("option.showfps", true);

		beforeTime = System.nanoTime();
		prevCalcTime = beforeTime;

		quit = false;
		enableSpecialKeys = true;
		allowQuit = true;

		SDLSurface surface = SDLVideo.getVideoSurface();

		// 画像などの読み込み
		ResourceHolderSDL.load();
		NormalFontSDL.dest = surface;

		if(propConfig.getProperty("option.firstSetupMode", true) == true) {
			// 初期設定
			enterState(STATE_CONFIG_KEYBOARD);
		} else {
			// タイトルステートに入る
			enterState(STATE_TITLE);
		}

		// メインループ
		while(quit == false) {
			// イベント処理
			processEvent();

			// ジョイスティックの更新
			if(joystickMax > 0) joyUpdate();

			// キー入力状態を更新
			for(int i = 0; i < 2; i++) {
				int joynum = joyUseNumber[i];

				if((joystickMax > 0) && (joynum >= 0) && (joynum < joystickMax)) {
					GameKeySDL.gamekey[i].update(keyPressedState, joyPressedState[joynum], joyAxisX[joynum], joyAxisY[joynum], joyHatState[joynum]);
				} else {
					GameKeySDL.gamekey[i].update(keyPressedState);
				}
			}

			// 各ステートの処理を実行
			gameStates[currentState].update();
			gameStates[currentState].render(surface);

			// FPS描画
			if(showfps) NormalFontSDL.printFont(0, 480 - 16, NullpoMinoSDL.df.format(NullpoMinoSDL.actualFPS), NormalFontSDL.COLOR_BLUE, 1.0f);

			// オブザーバークライアント
			if((netObserverClient != null) && netObserverClient.isConnected()) {
				int fontcolor = NormalFontSDL.COLOR_BLUE;
				if(netObserverClient.getObserverCount() > 1) fontcolor = NormalFontSDL.COLOR_GREEN;
				if(netObserverClient.getObserverCount() > 0 && netObserverClient.getPlayerCount() > 0) fontcolor = NormalFontSDL.COLOR_RED;
				String strObserverInfo = String.format("%d/%d", netObserverClient.getObserverCount(), netObserverClient.getPlayerCount());
				String strObserverString = String.format("%40s", strObserverInfo);
				NormalFontSDL.printFont(0, 480 - 16, strObserverString, fontcolor);
			}

			// 特殊キー
			if(enableSpecialKeys) {
				// スクリーンショット
				if(GameKeySDL.gamekey[0].isPushKey(GameKeySDL.BUTTON_SCREENSHOT) || GameKeySDL.gamekey[1].isPushKey(GameKeySDL.BUTTON_SCREENSHOT))
					saveScreenShot();

				// 終了 button
				if(allowQuit) {
					if(GameKeySDL.gamekey[0].isPushKey(GameKeySDL.BUTTON_QUIT) || GameKeySDL.gamekey[1].isPushKey(GameKeySDL.BUTTON_QUIT))
						enterState(-1);
				}
			}

			// 画面に表示
			surface.flip();

			// 休止・FPS計算処理
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			// 前回のフレームの休止 time誤差も引いておく
			long period = (long) (1.0 / maxFPS * 1000000000);
			sleepTime = (period - timeDiff) - overSleepTime;

			if(sleepTime > 0) {
				// 休止 timeがとれる場合
				if(maxFPS > 0) {
					try {
						Thread.sleep(sleepTime / 1000000L);
					} catch(InterruptedException e) {}
				}
				// sleep()の誤差
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else {
				// 状態更新・レンダリングで timeを使い切ってしまい
				// 休止 timeがとれない場合
				overSleepTime = 0L;
				// 休止なしが16回以上続いたら
				if(++noDelays >= 16) {
					Thread.yield(); // 他のスレッドを強制実行
					noDelays = 0;
				}
			}

			beforeTime = System.nanoTime();

			// FPSを計算
			calcFPS(period);
		}
	}

	/**
	 * SDLの終了処理
	 */
	public static void shutdown() {
		log.info("NullpoMinoSDL shutdown()");

		try {
			stopObserverClient();
			for(int i = 0; i < joystickMax; i++) {
				joystick[i].joystickClose();
			}
			SDLMixer.close();
			SDLMain.quit();
		} catch (Throwable e) {}
	}

	/**
	 * ステート切り替え
	 * @param id 切り替え先ステートID（-1で終了）
	 * @throws SDLException SDLのエラーが発生した場合
	 */
	public static void enterState(int id) throws SDLException {
		if((currentState >= 0) && (currentState < STATE_MAX) && (gameStates[currentState] != null)) {
			gameStates[currentState].leave();
		}
		if((id >= 0) && (id < STATE_MAX) && (gameStates[id] != null)) {
			currentState = id;
			gameStates[currentState].enter();
		} else if(id < 0) {
			quit = true;
		} else {
			throw new NullPointerException("Game state #" + id + " is null");
		}
	}

	/**
	 * 設定ファイルを保存
	 */
	public static void saveConfig() {
		try {
			FileOutputStream out = new FileOutputStream("config/setting/sdl.cfg");
			propConfig.store(out, "NullpoMino SDL-frontend Config");
			out.close();
			log.debug("Saved SDL-frontend config");
		} catch(IOException e) {
			log.error("Failed to save SDL-specific config", e);
		}

		try {
			FileOutputStream out = new FileOutputStream("config/setting/global.cfg");
			propGlobal.store(out, "NullpoMino Global Config");
			out.close();
			log.debug("Saved global config");
		} catch(IOException e) {
			log.error("Failed to save global config", e);
		}
	}

	/**
	 * スクリーンショットを保存
	 * @throws SDLException 保存に失敗した場合
	 */
	public static void saveScreenShot() throws SDLException {
		// ファイル名を決める
		String dir = NullpoMinoSDL.propGlobal.getProperty("custom.screenshot.directory", "ss");
		GregorianCalendar currentTime = new GregorianCalendar();
		int month = currentTime.get(Calendar.MONTH) + 1;
		String filename = String.format(
				dir + "/%04d_%02d_%02d_%02d_%02d_%02d.bmp",
				currentTime.get(Calendar.YEAR), month, currentTime.get(Calendar.DATE), currentTime.get(Calendar.HOUR_OF_DAY),
				currentTime.get(Calendar.MINUTE), currentTime.get(Calendar.SECOND)
		);
		log.info("Saving screenshot to " + filename);
		
		File ssfolder = new File(dir);
		if (!ssfolder.exists()) {
			if (ssfolder.mkdir()) {
				log.info("Created replay folder: " + dir);
			} else {
				log.info("Couldn't create replay folder at "+ dir);
			}
		}

		// ファイルに保存
		SDLVideo.getVideoSurface().saveBMP(filename);
	}

	/**
	 * 画面外にはみ出す画像をちゃんと描画できるようにSDLRectを修正する
	 * @param rectSrc 修正するSDLRect（描画元）
	 * @param rectDst 修正するSDLRect（描画先）
	 */
	public static void fixRect(SDLRect rectSrc, SDLRect rectDst) {
		if(rectSrc == null) return;
		if(rectDst == null) return;

		if(rectDst.x < 0) {
			int prevX = rectDst.x;
			rectDst.width += prevX;
			rectDst.x = 0;
			rectSrc.width += prevX;
			rectSrc.x -= prevX;
		}

		if(rectDst.y < 0) {
			int prevY = rectDst.y;
			rectDst.height += prevY;
			rectDst.y = 0;
			rectSrc.height += prevY;
			rectSrc.y -= prevY;
		}
	}

	/**
	 * 翻訳後のUIの文字列を取得
	 * @param str 文字列
	 * @return 翻訳後のUIの文字列（無いならそのままstrを返す）
	 */
	public static String getUIText(String str) {
		String result = propLang.getProperty(str);
		if(result == null) {
			result = propLangDefault.getProperty(str, str);
		}
		return result;
	}

	/**
	 * イベント処理
	 * @throws SDLException SDLのエラーが発生した場合
	 */
	protected static void processEvent() throws SDLException {
		while(true) {
			SDLEvent event = SDLEvent.pollEvent();
			if(event == null) break;

			if(event instanceof SDLQuitEvent) {
				// 終了 button
				enterState(-1);
			} else if(event instanceof SDLKeyboardEvent) {
				// キー入力
				SDLKeyboardEvent keyevent = (SDLKeyboardEvent)event;

				int keysym = keyevent.getSym();

				if(keyevent.getType() == SDLKeyboardEvent.SDL_KEYDOWN) {
					if(keysym < keyPressedState.length) keyPressedState[keysym] = true;
				} else if(keyevent.getType() == SDLKeyboardEvent.SDL_KEYUP) {
					if(keysym < keyPressedState.length) keyPressedState[keysym] = false;
				}
			}
		}
	}

	/**
	 * ジョイスティックの状態の更新
	 */
	protected static void joyUpdate() {
		try {
			SDLJoystick.joystickUpdate();

			for(int i = 0; i < joystickMax; i++) {
				if(joyIgnoreAxis[i] == false) {
					joyAxisX[i] = joystick[i].joystickGetAxis(0);
					joyAxisY[i] = joystick[i].joystickGetAxis(1);
				} else {
					joyAxisX[i] = 0;
					joyAxisY[i] = 0;
				}

				for(int j = 0; j < joyMaxButton[i]; j++) {
					joyPressedState[i][j] = joystick[i].joystickGetButton(j);
				}

				if((joyMaxHat[i] > 0) && (joyIgnorePOV[i] == false)) {
					joyHatState[i] = joystick[i].joystickGetHat(0);
				} else {
					joyHatState[i] = null;
				}
			}
		} catch (Throwable e) {
			log.warn("Joystick state update failed", e);
		}
	}

	/**
	 * FPSの計算
	 * @param period FPSを計算する間隔
	 */
	protected static void calcFPS(long period) {
		frameCount++;
		calcInterval += period;

		// 1秒おきにFPSを再計算する
		if(calcInterval >= 1000000000L) {
			long timeNow = System.nanoTime();

			// 実際の経過 timeを測定
			long realElapsedTime = timeNow - prevCalcTime; // 単位: ns

			// FPSを計算
			// realElapsedTimeの単位はnsなのでsに変換する
			actualFPS = ((double) frameCount / realElapsedTime) * 1000000000L;

			frameCount = 0L;
			calcInterval = 0L;
			prevCalcTime = timeNow;
		}
	}

	/**
	 * オブザーバークライアントを開始
	 */
	public static void startObserverClient() {
		log.debug("startObserverClient called");

		if(propObserver == null) {
			propObserver = new CustomProperties();
			try {
				FileInputStream in = new FileInputStream("config/setting/netobserver.cfg");
				propObserver.load(in);
				in.close();
			} catch (IOException e) {}
		}

		if(propObserver.getProperty("observer.enable", false) == false) return;
		if((netObserverClient != null) && netObserverClient.isConnected()) return;

		String host = propObserver.getProperty("observer.host", "");
		int port = propObserver.getProperty("observer.port", NetObserverClient.DEFAULT_PORT);

		if((host.length() > 0) && (port > 0)) {
			netObserverClient = new NetObserverClient(host, port);
			netObserverClient.start();
		}
	}

	/**
	 * オブザーバークライアントを停止
	 */
	public static void stopObserverClient() {
		log.debug("stopObserverClient called");

		if(netObserverClient != null) {
			if(netObserverClient.isConnected()) {
				netObserverClient.send("disconnect\n");
			}
			netObserverClient.threadRunning = false;
			netObserverClient.connectedFlag = false;
			netObserverClient = null;
		}
		propObserver = null;
	}
}

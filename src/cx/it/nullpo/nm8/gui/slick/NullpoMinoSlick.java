package cx.it.nullpo.nm8.gui.slick;

import cx.it.nullpo.nm8.gui.game.NullpoMino;
import cx.it.nullpo.nm8.gui.slick.framework.SlickNFSystem;
import cx.it.nullpo.nm8.util.CustomProperties;
import cx.it.nullpo.nm8.util.NGlobalConfig;

/**
 * Start NullpoMino with Slick framework
 */
public class NullpoMinoSlick {
	public static void main(String[] args) {
		try {
			NGlobalConfig.load();

			CustomProperties propGlobal = NGlobalConfig.getConfig();
			int screenWidth = propGlobal.getProperty("sys.resolution.width", 640);
			int screenHeight = propGlobal.getProperty("sys.resolution.height", 480);
			boolean fullscreen = propGlobal.getProperty("sys.fullscreen", false);

			SlickNFSystem sys = new SlickNFSystem(new NullpoMino(), fullscreen, screenWidth, screenHeight, 640, 480, true, args);
			NGlobalConfig.applyNFSystem(sys);
			if(!fullscreen) sys.setUseAWTKeyReceiver(propGlobal.getProperty("slick.awtkey", false));

			sys.init();
			sys.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
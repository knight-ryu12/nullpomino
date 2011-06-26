package cx.it.nullpo.nm8.neuro.test;

import cx.it.nullpo.nm8.gui.framework.NFGraphics;
import cx.it.nullpo.nm8.neuro.core.NEUROCore;
import cx.it.nullpo.nm8.neuro.error.PluginInitializationException;
import cx.it.nullpo.nm8.neuro.plugin.Nullterm;

/**
 * A type of NEURO which is used to verify that events are being passed around correctly.
 * @author Zircean
 *
 */
public class TestingNEURO extends NEUROCore {
	
	/**
	 * Constructs a TestingNEURO.
	 */
	public TestingNEURO() {
		super();
		try {
			new Nullterm().init(this);
		} catch (PluginInitializationException e) {
			System.err.println("Shit is all wrong, cap'n...");
		}
	}

	public String getName() {
		return "Test NEURO";
	}

	public float getVersion() {
		return 0.0F;
	}

	public void draw(NFGraphics g) { }

}

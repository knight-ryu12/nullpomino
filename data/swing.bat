@echo off
set path=%path%;%systemroot%\SysWOW64
cd ..\
java -cp bin;NullpoMino.jar;data/lib/log4j-1.2.16.jar;data/lib/jdom.jar;data/lib/lwjgl.jar;data/lib/lwjgl_util.jar;data/lib/eventbus-1.4.jar;data/lib/nifty-1.3.jar;data/lib/xpp3_min-1.1.4c.jar;data/lib/commons-logging-1.1.1.jar;data/lib/nifty-default-controls-1.3.jar;data/lib/nifty-style-black-1.3.jar -Djava.library.path=data/lib -Dlog4j.configuration=file:data/properties/log.cfg -Djava.util.logging.config.file=data/properties/jdklogger.cfg cx.it.nullpo.nm8.gui.swing.NullpoMinoSwing

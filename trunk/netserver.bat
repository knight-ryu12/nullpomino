@echo off
set path=%path%;%systemroot%\SysWOW64
start java -cp bin;NullpoMino.jar;lib\log4j-1.2.15.jar org.game_host.hebo.nullpomino.game.net.NetServer

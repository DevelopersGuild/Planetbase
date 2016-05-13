package io.developersguild.rebelinvader.desktop;

import java.io.File;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import io.developersguild.Debug;
import io.developersguild.StrategyGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Planetbase";
        config.width = 1000;
        config.height = 700;
        new LwjglApplication(new StrategyGame(), config);
    }
}

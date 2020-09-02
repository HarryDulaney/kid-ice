package com.hgdiv.kidice.game;

import com.hgdiv.kidice.engine.GameEngine;
import com.hgdiv.kidice.engine.IGameLogic;

/**
 * {@code Main} is the application entry point.
 */
public class Main {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new TestGame();
            GameEngine gameEngine = new GameEngine("GAME", 600, 400, vSync, gameLogic);
            gameEngine.run();

        }catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

    }

}


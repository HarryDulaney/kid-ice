package com.hgdiv.kidice.game;

import com.hgdiv.kidice.engine.GameEngine;
import com.hgdiv.kidice.engine.IGameLogic;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = false;
            IGameLogic gameLogic = new TestGame();
            GameEngine gameEngine = new GameEngine("GAME", 600, 400, vSync, gameLogic);
            gameEngine.run();

        }catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

    }

}


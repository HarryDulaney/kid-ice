package com.hgdiv.kidice.engine;

import com.hgdiv.kidice.game.TestGame;

/**
 * {@code IGameLogic} is an interface for defining the
 * games lifecycle. To be implemented by a Game class.
 * {@link TestGame} for an example.
 */
public interface IGameLogic {

    void init(Window window) throws Exception;

    void input(Window window, MouseInput mouseInput);

    void update(float interval, MouseInput mouseInput);

    void render(Window window);

    void cleanup();

}

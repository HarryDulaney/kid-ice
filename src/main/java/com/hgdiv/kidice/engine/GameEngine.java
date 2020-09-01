package com.hgdiv.kidice.engine;

/**
 * The type Game engine.
 */
public class GameEngine implements Runnable {

    /**
     * The constant TARGET_FPS.
     */
    public static final int TARGET_FPS = 75;
    /**
     * The constant TARGET_UPS.
     */
    public static final int TARGET_UPS = 30;

    private final IGameLogic gameLogic;
    private final Window window;
    private final Timer timer;



    /**
     * Instantiates a new Game engine.
     *
     * @param windowTitle the window title
     * @param width       the width
     * @param height      the height
     * @param vsSync      the vs sync
     * @param gameLogic   the game logic
     * @throws Exception the exception
     */
    public GameEngine(String windowTitle, int width, int height, boolean vsSync, IGameLogic gameLogic) throws Exception {
        window = new Window(windowTitle, width, height, vsSync);
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }

    }

    /**
     * Init.
     *
     * @throws Exception the exception
     */
    protected void init() throws Exception {
        window.init();
        timer.init();
        gameLogic.init();

    }


    /**
     * Game loop.
     */
    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;


            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }


    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ire) {
                ire.printStackTrace();

            }
        }

    }

    /**
     * Input.
     */
    protected void input() {
        gameLogic.input(window);
    }

    /**
     * Update.
     *
     * @param interval the interval
     */
    protected void update(float interval) {
        gameLogic.update(interval);

    }
    protected void cleanup() {
        gameLogic.cleanup();
    }

    /**
     * Render.
     */
    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}

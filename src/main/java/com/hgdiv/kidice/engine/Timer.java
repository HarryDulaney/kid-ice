package com.hgdiv.kidice.engine;

/**
 * The type Timer.
 */
public class Timer {

    private double lastLoopTime;


    /**
     * Init.
     */
    public void init() {
        lastLoopTime = getTime();
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public double getTime() {
        return System.nanoTime() / 1_000_000_000.0;
    }

    /**
     * Gets elapsed time.
     *
     * @return the elapsed time
     */
    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;

    }

    /**
     * Gets last loop time.
     *
     * @return the last loop time
     */
    public double getLastLoopTime() {
        return lastLoopTime;
    }
}

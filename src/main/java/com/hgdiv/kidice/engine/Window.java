package com.hgdiv.kidice.engine;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


/**
 * The type Window.
 */
public class Window {

    private final String title;
    private int height;
    private int width;
    private boolean vSync;

    public long getWindowHandle() {
        return windowHandle;
    }

    private long windowHandle;
    private boolean resized;

    /**
     * Instantiates a new Window.
     *
     * @param title  the title
     * @param width  the width
     * @param height the height
     * @param vSync  the v sync
     */
    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
    }

    /**
     * Init.
     */
    public void init() {
        // Error callback for standard out
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // Create window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failure to create GFLW window");
        }

        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        // Monitor Resolution
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                windowHandle,
                (vidMode.width() - width) / 2,
                (vidMode.height() - height) / 2
        );

        glfwMakeContextCurrent(windowHandle);

        if (isvSync()) {
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_DEPTH_TEST);
    }

    /**
     * Sets clear color.
     *
     * @param r     the r
     * @param g     the g
     * @param b     the b
     * @param alpha the alpha
     */
    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    /**
     * Is key pressed boolean.
     *
     * @param keyCode the key code
     * @return the boolean
     */
    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    /**
     * Window should close boolean.
     *
     * @return the boolean
     */
    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Isv sync boolean.
     *
     * @return the boolean
     */
    public boolean isvSync() {
        return vSync;
    }

    /**
     * Sets sync.
     *
     * @param vSync the v sync
     */
    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Is resized boolean.
     *
     * @return the boolean
     */
    public boolean isResized() {
        return resized;
    }

    /**
     * Sets resized.
     *
     * @param resized the resized
     */
    public void setResized(boolean resized) {
        this.resized = resized;
    }

    /**
     * Update.
     */
    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }
}

package com.hgdiv.kidice.game;

import com.hgdiv.kidice.engine.EngineUtils;
import com.hgdiv.kidice.engine.GameItem;
import com.hgdiv.kidice.engine.Window;
import com.hgdiv.kidice.engine.graph.ShaderProgram;
import com.hgdiv.kidice.engine.graph.Transformation;
import org.joml.Matrix4f;

import java.io.File;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;


/**
 * The type Renderer.
 */
public class Renderer {

    private ShaderProgram shaderProgram;
    private Transformation transformation;

    /**
     * Field of View (Radians)
     */
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float ZNear = 0.01f;
    private static final float ZFar = 1000.f;

    private Matrix4f projectionMatrix;

    /**
     * Instantiates a new Renderer.
     */
    public Renderer() {
        transformation = new Transformation();
    }

    /**
     * {@code Renderer.init(window)} performs the initialization steps to setup
     * graphics buffers with the GPU via LWJGL
     *
     * <p> - Load shaders </p>
     * <p> - Load shaders </p>
     * <p> - Instantiate ShaderProgram </p>
     * <p> - Load Vertex Shader /  Load Fragment Shader </p>
     * <p> - link() ShaderProgram </p>
     * <p> - Instantiate the projectionMatrix, account for current window's aspect ratio </p>
     * <p> - Create uniform </p>
     *
     * @throws Exception the exception IOException, FileNotFoundException
     */
    public void init(Window window) throws Exception {

        File vertFile = new File("D:\\Develop\\Projects\\kid-ice\\src\\main\\resources\\shaders\\vertex.vs");
        File fragFile = new File("D:\\Develop\\Projects\\kid-ice\\src\\main\\resources\\shaders\\fragment.fs");

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(EngineUtils.loadResource(vertFile));
        shaderProgram.createFragmentShader(EngineUtils.loadResource(fragFile));
        shaderProgram.link();

        //Projection Matrix create
        float aspectRatio = (float) window.getWidth() / window.getHeight();
        projectionMatrix = new Matrix4f().perspective(Renderer.FOV, aspectRatio,
                Renderer.ZNear, Renderer.ZFar);
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }

    /**
     * {@code Renderer.clear()}
     */
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * {@code Renderer.render }
     *
     * @param gameItems an array of GameItem's to render
     * @param window    the window object to render to
     */
    public void render(Window window, GameItem[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), ZNear, ZFar);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Render each gameItem
        for (GameItem gameItem : gameItems) {
            // Set world matrix for this item
            Matrix4f worldMatrix =
                    transformation.getWorldMatrix(
                            gameItem.getPosition(),
                            gameItem.getRotation(),
                            gameItem.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);

            // Render the mes for this game item
            gameItem.getMesh().render();
        }
        shaderProgram.unbind();
    }

    /**
     * {@code Renderer.cleanup()} frees up acquired resources
     */
    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}

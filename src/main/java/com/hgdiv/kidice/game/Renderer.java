package com.hgdiv.kidice.game;

import com.hgdiv.kidice.engine.EngineUtils;
import com.hgdiv.kidice.engine.GameItem;
import com.hgdiv.kidice.engine.Window;
import com.hgdiv.kidice.engine.graph.Camera;
import com.hgdiv.kidice.engine.graph.Mesh;
import com.hgdiv.kidice.engine.graph.ShaderProgram;
import com.hgdiv.kidice.engine.graph.Transformation;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;


/**
 * The type Renderer.
 */
public class Renderer {

    /**
     * Field of View (Radians)
     */
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float ZNear = 0.01f;
    private static final float ZFar = 1000.f;

    private ShaderProgram shaderProgram;
    private final Transformation transformation;


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
     *
     * @throws Exception the exception IOException, FileNotFoundException
     */
    public void init(Window window) throws Exception {

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(EngineUtils.loadResource("src/main/resources/shaders/vertex.vs"));
        shaderProgram.createFragmentShader(EngineUtils.loadResource("src/main/resources/shaders/fragment.fs"));
        shaderProgram.link();

        //Projection Matrix create
        float aspectRatio = (float) window.getWidth() / window.getHeight();
        Matrix4f projectionMatrix = new Matrix4f().perspective(Renderer.FOV, aspectRatio,
                Renderer.ZNear, Renderer.ZFar);
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("color");


        shaderProgram.createUniform("useColor");
        shaderProgram.createUniform("texture_sampler");


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
    public void render(Window window, Camera camera, GameItem[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), ZNear, ZFar);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("texture_sampler", 0);
        // Render each gameItem
        for (GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            shaderProgram.setUniform("color", mesh.getColor());
            shaderProgram.setUniform("useColor", mesh.isTextured() ? 0 : 1);
            mesh.render();
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

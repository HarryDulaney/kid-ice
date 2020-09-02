package com.hgdiv.kidice.game;

import com.hgdiv.kidice.engine.EngineUtils;
import com.hgdiv.kidice.engine.Window;
import com.hgdiv.kidice.engine.graph.Mesh;
import com.hgdiv.kidice.engine.graph.ShaderProgram;
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

    }

    /**
     * {@code Renderer.init()} performs the initialization steps to setup
     * graphics buffers with the GPU via LWJGL
     *
     * @throws Exception the exception
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
     * @param mesh   the instance of Mesh to use in rendering
     * @param window the window object to render to
     */
    public void render(Window window, Mesh mesh) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Bind to the VAO
        glBindVertexArray(mesh.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the vertices
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);


        // Restore state
        glBindVertexArray(0);

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

package com.hgdiv.kidice.game;

import com.hgdiv.kidice.engine.EngineUtils;
import com.hgdiv.kidice.engine.Window;
import com.hgdiv.kidice.engine.graph.Mesh;
import com.hgdiv.kidice.engine.graph.ShaderProgram;

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
    public void init() throws Exception {
        File vertFile = new File("D:\\Develop\\Projects\\kid-ice\\src\\main\\resources\\shaders\\vertex.vs");
        File fragFile = new File("D:\\Develop\\Projects\\kid-ice\\src\\main\\resources\\shaders\\fragment.fs");

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(EngineUtils.loadResource(vertFile));
        shaderProgram.createFragmentShader(EngineUtils.loadResource(fragFile));
        shaderProgram.link();

    }

    /**
     * {@code Renderer.clear()}
     */
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * {@code Renderer.render(Mesh mesh) }
     *
     * @param mesh
     */
    public void render(Window window, Mesh mesh) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

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

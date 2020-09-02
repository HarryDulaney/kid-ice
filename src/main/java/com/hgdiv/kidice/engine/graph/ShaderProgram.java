package com.hgdiv.kidice.engine.graph;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * The type Shader program.
 */
public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    private final Map<String, Integer> uniforms;

    /**
     * Instantiates a new Shader program.
     *
     * @throws Exception the exception
     */
    public ShaderProgram() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Shader failed to start");
        }
        uniforms = new HashMap<>();

    }

    /**
     * Create vertex shader.
     *
     * @param shaderCode the shader code
     * @throws Exception the exception
     */
    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    /**
     * Create fragment shader.
     *
     * @param shaderCode the shader code
     * @throws Exception the exception
     */
    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    /**
     * Create shader int.
     *
     * @param shaderCode the shader code
     * @param shaderType the shader type
     * @return the int
     * @throws Exception the exception
     */
    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    /**
     * {@code link} is called after shaders have been attached to
     * verify attachment and link the bindings. Allows for detaching shaders.
     *
     * @throws Exception the exception when GL_LINK_STATUS equals zero.
     */
    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);//TODO: Remove for production build
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

    }

    /**
     * Create uniform.
     *
     * @param uniformName the uniform name
     * @throws Exception the exception
     */
    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programId,
                uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" +
                    uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer floatBuffer = stack.mallocFloat(16);
            value.get(floatBuffer);
            glUniformMatrix4fv(uniforms.get(uniformName), false, floatBuffer);
        }

    }

    /**
     * Bind the programId with glUseProgram()
     */
    public void bind() {
        glUseProgram(programId);
    }

    /**
     * Unbind the programId by binding a 0 with the GLFW
     * glUseProgram(0) method.
     */
    public void unbind() {
        glUseProgram(0);
    }

    /**
     * Cleanup calls {@code ShaderProgram.unbind()} method. Then calls glDeleteProgram() on the
     * programId to erase the association totally.
     */
    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

}


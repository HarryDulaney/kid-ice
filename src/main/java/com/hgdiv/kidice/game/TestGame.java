package com.hgdiv.kidice.game;

import com.hgdiv.kidice.engine.GameItem;
import com.hgdiv.kidice.engine.IGameLogic;
import com.hgdiv.kidice.engine.MouseInput;
import com.hgdiv.kidice.engine.Window;
import com.hgdiv.kidice.engine.graph.Camera;
import com.hgdiv.kidice.engine.graph.Mesh;
import com.hgdiv.kidice.engine.graph.OBJLoader;
import com.hgdiv.kidice.engine.graph.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Test implementation of the game engine
 */
public class TestGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] gameItems;

    private static final float CAMERA_POS_STEP = 0.05f;


    /**
     * Instantiates a new Test game.
     */
    public TestGame() throws Exception {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
//        Mesh mesh = OBJLoader.loadMesh("src/main/resources/models/bunny.obj");
        Mesh mesh = OBJLoader.loadMesh("src/main/resources/models/cube.obj");
//        Mesh mesh = OBJLoader.loadMesh("src/main/resources/models/cartoonGuy.obj");
        Texture texture = new Texture("src/main/resources/textures/grassblock.png");
        mesh.setTexture(texture);
        GameItem gameItem1 = new GameItem(mesh);
        gameItem1.setScale(0.5f);
        gameItem1.setPosition(0, 0, -2);
        GameItem gameItem2 = new GameItem(mesh);
        gameItem2.setScale(0.5f);
        gameItem2.setPosition(0, 0, 2);
        GameItem gameItem3 = new GameItem(mesh);
        gameItem3.setScale(0.5f);
        gameItem3.setPosition(-2, 0, 0);
        GameItem gameItem4 = new GameItem(mesh);
        gameItem4.setScale(0.5f);
        gameItem4.setPosition(3, -3, 4);
        GameItem gameItem5 = new GameItem(mesh);
        gameItem5.setScale(0.5f);
        gameItem5.setPosition(2, 2, 0);
        GameItem gameItem6 = new GameItem(mesh);
        gameItem6.setScale(0.5f);
        gameItem6.setPosition(4, 5, 8);
        gameItems = new GameItem[]{gameItem1, gameItem2, gameItem3, gameItem4, gameItem5, gameItem6};
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_UP)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_PAGE_UP)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_PAGE_DOWN)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }

}

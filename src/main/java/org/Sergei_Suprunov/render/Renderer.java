package org.Sergei_Suprunov.render;

import org.Sergei_Suprunov.models.*;
import org.Sergei_Suprunov.utils.GLCamera;
import org.joml.Matrix4f;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private int width = 800;
    private int height = 600;

    private GLCamera camera;
    private Skybox skybox;
    private List<Model> models;
    private SnowSystem snowSystem;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private boolean isMousePressed = false;
    private double lastX = -1.0;
    private double lastY = -1.0;
    private double lastFrameTime;

    public Renderer() {

    }

    public void init() {

        glEnable(GL_DEPTH_TEST);

        camera = new GLCamera();
        skybox = new Skybox();
        snowSystem = new SnowSystem();

        models = new ArrayList<>();
        models.add(new Ground());
        models.add(new House());

        lastFrameTime = glfwGetTime();
    }

    public void display() {
        double now = glfwGetTime();
        float dt = (float) (now - lastFrameTime);
        lastFrameTime = now;

        handleContinuousInput(dt);

        glViewport(0, 0, width, height);
        glClearColor(0.5f, 0.7f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        try (MemoryStack stack = MemoryStack.stackPush()) {

            Matrix4f projection = new Matrix4f().perspective(
                    (float) Math.toRadians(45.0f),
                    (float) width / height,
                    0.1f,
                    100.0f
            );
            glMatrixMode(GL_PROJECTION);
            glLoadMatrixf(projection.get(stack.mallocFloat(16)));

            Matrix4f viewRotationOnly = new Matrix4f(camera.getViewMatrix());
            viewRotationOnly.setTranslation(0, 0, 0);

            glMatrixMode(GL_MODELVIEW);
            glLoadMatrixf(viewRotationOnly.get(stack.mallocFloat(16)));

            glDepthMask(false);
            skybox.draw();
            glDepthMask(true);

            Matrix4f view = camera.getViewMatrix();
            glLoadMatrixf(view.get(stack.mallocFloat(16)));
        }

        for (Model model : models) {
            if (model instanceof House) {
                ((House) model).setRoofSnowGrid(snowSystem.getRoofSnowGrid());
            }
            model.draw();
        }
        if (snowSystem != null) {
            snowSystem.update(dt);
            snowSystem.draw();
        }
    }

    private void handleContinuousInput(float dt) {
        float speed = 5.0f * dt;
        if (pressedKeys.contains(GLFW_KEY_W)) camera.forward(speed);
        if (pressedKeys.contains(GLFW_KEY_S)) camera.backward(speed);
        if (pressedKeys.contains(GLFW_KEY_A)) camera.left(speed);
        if (pressedKeys.contains(GLFW_KEY_D)) camera.right(speed);
        if (pressedKeys.contains(GLFW_KEY_SPACE)) camera.up(speed);
        if (pressedKeys.contains(GLFW_KEY_LEFT_SHIFT)) camera.down(speed);
        float cloudSpeed = 8.0f * dt;
        if (snowSystem != null) {
            if (pressedKeys.contains(GLFW_KEY_UP)) snowSystem.moveCloud(0, -cloudSpeed);
            if (pressedKeys.contains(GLFW_KEY_DOWN)) snowSystem.moveCloud(0, cloudSpeed);
            if (pressedKeys.contains(GLFW_KEY_LEFT)) snowSystem.moveCloud(-cloudSpeed, 0);
            if (pressedKeys.contains(GLFW_KEY_RIGHT)) snowSystem.moveCloud(cloudSpeed, 0);
        }
    }

    public void dispose() {
    }

    public GLFWKeyCallback getGlfwKeyCallback() {
        return new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true);
                if (action == GLFW_PRESS) pressedKeys.add(key);
                if (action == GLFW_RELEASE) pressedKeys.remove(key);
            }
        };
    }

    public GLFWMouseButtonCallback getGlfwMouseButtonCallback() {
        return new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button == GLFW_MOUSE_BUTTON_1) {
                    isMousePressed = (action == GLFW_PRESS);
                    if (isMousePressed) { lastX = -1.0; lastY = -1.0; }
                }
            }
        };
    }

    public GLFWCursorPosCallback getGlfwCursorPosCallback() {
        return new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                if (isMousePressed) {
                    if (lastX == -1.0 || lastY == -1.0) { lastX = xpos; lastY = ypos; return; }
                    double deltaX = xpos - lastX;
                    double deltaY = ypos - lastY;
                    double smoothingFactor = 0.005;

                    camera.addAzimuth((float) (deltaX * smoothingFactor));
                    camera.addZenith((float) (-deltaY * smoothingFactor));

                    lastX = xpos;
                    lastY = ypos;
                }
            }
        };
    }

    public GLFWWindowSizeCallback getGlfwWindowSizeCallback() {
        return new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                if (w > 0 && h > 0) {
                    width = w;
                    height = h;
                }
            }
        };
    }

}
package org.Sergei_Suprunov.view;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.Sergei_Suprunov.render.Renderer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class LwjglWindow {
    private long window;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private Renderer renderer;

    public void start() {
        init();
        loop();

        renderer.dispose();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Nepodařilo se inicializovat GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(WIDTH, HEIGHT, "3D Scéna", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Nepodařilo se vytvořit okno GLFW");


        try (MemoryStack stack = MemoryStack.stackPush()) {
            var pWidth = stack.mallocInt(1);
            var pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        renderer = new Renderer();

        glfwSetKeyCallback(window, renderer.getGlfwKeyCallback());
        glfwSetMouseButtonCallback(window, renderer.getGlfwMouseButtonCallback());
        glfwSetCursorPosCallback(window, renderer.getGlfwCursorPosCallback());
        glfwSetWindowSizeCallback(window, renderer.getGlfwWindowSizeCallback());
    }

    private void loop() {
        GL.createCapabilities();
        renderer.init();

        while (!glfwWindowShouldClose(window)) {
            renderer.display();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}
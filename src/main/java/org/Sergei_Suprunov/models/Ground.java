package org.Sergei_Suprunov.models;

import org.Sergei_Suprunov.lwjglutils.OGLTexture2D;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Ground implements Model {
    private OGLTexture2D texture;

    public Ground() {
        try {
            texture = new OGLTexture2D("textures/ground.jpg");
        } catch (IOException e) {
            System.err.println("Ошибка загрузки текстуры земли!");
        }
    }

    @Override
    public void draw() {
        if (texture != null) {
            glEnable(GL_TEXTURE_2D);
            texture.bind();
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        }

        glColor3f(1.0f, 1.0f, 1.0f);
        float size = 20f;
        float repeat = 10.0f;

        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(-size, 0, -size);

        glTexCoord2f(repeat, 0.0f);
        glVertex3f(size, 0, -size);

        glTexCoord2f(repeat, repeat);
        glVertex3f(size, 0, size);

        glTexCoord2f(0.0f, repeat);
        glVertex3f(-size, 0, size);
        glEnd();

        if (texture != null) {
            glDisable(GL_TEXTURE_2D);
        }
    }
}
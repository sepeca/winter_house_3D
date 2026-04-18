package org.Sergei_Suprunov.models;

import org.Sergei_Suprunov.lwjglutils.OGLTexture2D;
import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;

public class Skybox implements Model {

    private OGLTexture2D texture;
    private final int slices = 32;
    private final int stacks = 32;
    private final float radius = 50.0f;

    public Skybox() {
        try {
            texture = new OGLTexture2D("textures/skybox.jpg");
        } catch (IOException e) {
            System.err.println("Chyba: nebyla nalezená textura");
        }
    }

    @Override
    public void draw() {
        if (texture != null) {
            glEnable(GL_TEXTURE_2D);
            texture.bind();
        }

        glColor3f(1.0f, 1.0f, 1.0f);

        for (int i = 0; i < stacks; i++) {
            float phi1 = (float) (Math.PI * (float) i / stacks);
            float phi2 = (float) (Math.PI * (float) (i + 1) / stacks);

            glBegin(GL_TRIANGLE_STRIP);
            for (int j = 0; j <= slices; j++) {
                float theta = (float) (2.0 * Math.PI * (float) j / slices);

                float u = 1.0f - (float) j / slices;
                float v = (float) i / stacks;
                float v2 = (float) (i + 1) / stacks;

                glTexCoord2f(u, v);
                drawSphereVertex(phi1, theta);

                glTexCoord2f(u, v2);
                drawSphereVertex(phi2, theta);
            }
            glEnd();
        }

        if (texture != null) {
            glDisable(GL_TEXTURE_2D);
        }
    }

    private void drawSphereVertex(float phi, float theta) {
        float x = (float) (radius * Math.sin(phi) * Math.cos(theta));
        float y = (float) (radius * Math.cos(phi));
        float z = (float) (radius * Math.sin(phi) * Math.sin(theta));
        glVertex3f(x, y, z);
    }
}
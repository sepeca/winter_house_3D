package org.Sergei_Suprunov.models;

import org.Sergei_Suprunov.lwjglutils.OGLTexture2D;
import static org.lwjgl.opengl.GL11.*;

public class House implements Model {

    private OGLTexture2D textureWalls;
    private OGLTexture2D textureRoofSlopes;
    private OGLTexture2D textureRoofFronton;
    private OGLTexture2D textureDoor;
    private float[][] roofSnowGrid;
    private OGLTexture2D textureSnowPatch;

    public void setRoofSnowGrid(float[][] grid) {
        this.roofSnowGrid = grid;
    }

    private float getLocalRoofY(float localX) {
        return 1.8f - (Math.abs(localX) / 1.2f) * (1.8f - 0.84f);
    }

    public House() {
        try {
            textureWalls = new OGLTexture2D("textures/wall.jpg");
            textureRoofSlopes = new OGLTexture2D("textures/roof.jpg");
            textureRoofFronton = new OGLTexture2D("textures/roof_fronton.jpg");
            textureDoor = new OGLTexture2D("textures/door.jpg");
            textureSnowPatch = new OGLTexture2D("textures/snow.png");
        } catch (Exception e) {
            System.err.println("Textury domu nebyly nalezeny, budou pouzity zakladni barvy");
        }
    }

    @Override
    public void draw() {
        glPushMatrix();
        glTranslatef(0.0f, 0.0f, -5.0f);
        glScalef(4.0f, 4.0f, 4.0f);

        drawWalls();
        drawDoor();
        drawRoofStructure();
        drawRoofing();

        glPopMatrix();
    }

    private void drawRoofStructure() {
        if (textureRoofFronton != null) {
            glEnable(GL_TEXTURE_2D);
            textureRoofFronton.bind();
            glColor3f(1.0f, 1.0f, 1.0f);
        } else {
            glColor3f(0.5f, 0.3f, 0.1f);
        }

        glBegin(GL_TRIANGLES);
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-1.0f, 1.0f, 1.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex3f( 1.0f, 1.0f, 1.0f);
        glTexCoord2f(0.5f, 1.0f); glVertex3f( 0.0f, 1.8f, 1.0f);

        glTexCoord2f(0.0f, 0.0f); glVertex3f( 1.0f, 1.0f, -1.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex3f(-1.0f, 1.0f, -1.0f);
        glTexCoord2f(0.5f, 1.0f); glVertex3f( 0.0f, 1.8f, -1.0f);
        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

    private void drawRoofing() {
        if (textureRoofSlopes != null) {
            glEnable(GL_TEXTURE_2D);
            textureRoofSlopes.bind();
            glColor3f(1.0f, 1.0f, 1.0f);
        } else {
            glColor3f(1.0f, 1.0f, 1.0f);
        }

        glBegin(GL_QUADS);

        glTexCoord2f(0, 0); glVertex3f(-1.2f, 0.84f, -1.2f);
        glTexCoord2f(1, 0); glVertex3f(-1.2f, 0.84f,  1.2f);
        glTexCoord2f(1, 1); glVertex3f( 0.0f, 1.8f,  1.2f);
        glTexCoord2f(0, 1); glVertex3f( 0.0f, 1.8f, -1.2f);

        glTexCoord2f(0, 0); glVertex3f( 1.2f, 0.84f,  1.2f);
        glTexCoord2f(1, 0); glVertex3f( 1.2f, 0.84f, -1.2f);
        glTexCoord2f(1, 1); glVertex3f( 0.0f, 1.8f, -1.2f);
        glTexCoord2f(0, 1); glVertex3f( 0.0f, 1.8f,  1.2f);
        glEnd();

        if (textureRoofSlopes != null) glDisable(GL_TEXTURE_2D);

        if (roofSnowGrid != null) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            glEnable(GL_TEXTURE_2D);
            textureSnowPatch.bind();

            glPushMatrix();
            glTranslatef(0.0f, 0.01f, 0.0f);

            int GRID_SIZE = roofSnowGrid.length;
            float cellW = 2.4f / GRID_SIZE;
            float cellD = 2.4f / GRID_SIZE;

            glBegin(GL_QUADS);
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    float opacity = roofSnowGrid[row][col];
                    if (opacity > 0.0f) {
                        glColor4f(1.0f, 1.0f, 1.0f, opacity);

                        float cx = -1.2f + col * cellW + (cellW / 2.0f);
                        float cz = -1.2f + row * cellD + (cellD / 2.0f);
                        float extW = cellW * 0.75f;
                        float extD = cellD * 0.75f;

                        float x1 = cx - extW;
                        float x2 = cx + extW;
                        float z1 = cz - extD;
                        float z2 = cz + extD;

                        int rot = (row * 31 + col * 17) % 4;
                        float u1=0, v1=0, u2=1, v2=0, u3=1, v3=1, u4=0, v4=1;
                        if (rot == 1) { u1=0; v1=1; u2=0; v2=0; u3=1; v3=0; u4=1; v4=1; }
                        else if (rot == 2) { u1=1; v1=1; u2=0; v2=1; u3=0; v3=0; u4=1; v4=0; }
                        else if (rot == 3) { u1=1; v1=0; u2=1; v2=1; u3=0; v3=1; u4=0; v4=0; }

                        glTexCoord2f(u1, v1); glVertex3f(x1, getLocalRoofY(x1), z1);
                        glTexCoord2f(u2, v2); glVertex3f(x1, getLocalRoofY(x1), z2);
                        glTexCoord2f(u3, v3); glVertex3f(x2, getLocalRoofY(x2), z2);
                        glTexCoord2f(u4, v4); glVertex3f(x2, getLocalRoofY(x2), z1);
                    }
                }
            }
            glEnd();

            glPopMatrix();
            glDisable(GL_BLEND);
            glDisable(GL_TEXTURE_2D);
        }
    }

    private void drawWalls() {
        if (textureWalls != null) {
            glEnable(GL_TEXTURE_2D);
            textureWalls.bind();
            glColor3f(1.0f, 1.0f, 1.0f);
        } else {
            glColor3f(0.6f, 0.4f, 0.2f);
        }

        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-1.0f, 0.0f,  1.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex3f( 1.0f, 0.0f,  1.0f);
        glTexCoord2f(1.0f, 1.0f); glVertex3f( 1.0f, 1.0f,  1.0f);
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-1.0f, 1.0f,  1.0f);

        glTexCoord2f(0.0f, 0.0f); glVertex3f( 1.0f, 0.0f, -1.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex3f(-1.0f, 0.0f, -1.0f);
        glTexCoord2f(1.0f, 1.0f); glVertex3f(-1.0f, 1.0f, -1.0f);
        glTexCoord2f(0.0f, 1.0f); glVertex3f( 1.0f, 1.0f, -1.0f);

        glTexCoord2f(0.0f, 0.0f); glVertex3f(-1.0f, 0.0f, -1.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex3f(-1.0f, 0.0f,  1.0f);
        glTexCoord2f(1.0f, 1.0f); glVertex3f(-1.0f, 1.0f,  1.0f);
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-1.0f, 1.0f, -1.0f);

        glTexCoord2f(0.0f, 0.0f); glVertex3f( 1.0f, 0.0f,  1.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex3f( 1.0f, 0.0f, -1.0f);
        glTexCoord2f(1.0f, 1.0f); glVertex3f( 1.0f, 1.0f, -1.0f);
        glTexCoord2f(0.0f, 1.0f); glVertex3f( 1.0f, 1.0f,  1.0f);
        glEnd();

        if (textureWalls != null) glDisable(GL_TEXTURE_2D);
    }

    private void drawDoor() {
        if (textureDoor != null) {
            glEnable(GL_TEXTURE_2D);
            textureDoor.bind();
            glColor3f(1.0f, 1.0f, 1.0f);
        } else {
            glColor3f(0.3f, 0.1f, 0.0f);
        }

        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-0.3f, 0.0f, 1.01f);
        glTexCoord2f(1.0f, 0.0f); glVertex3f( 0.3f, 0.0f, 1.01f);
        glTexCoord2f(1.0f, 1.0f); glVertex3f( 0.3f, 0.8f, 1.01f);
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-0.3f, 0.8f, 1.01f);
        glEnd();

        if (textureDoor != null) glDisable(GL_TEXTURE_2D);
    }
}
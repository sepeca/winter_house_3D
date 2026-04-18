package org.Sergei_Suprunov.models;

import static org.lwjgl.opengl.GL11.*;
import java.util.Random;

public class SnowSystem implements Model {

    private final int MAX_PARTICLES = 3000;
    private final float[] x = new float[MAX_PARTICLES];
    private final float[] y = new float[MAX_PARTICLES];
    private final float[] z = new float[MAX_PARTICLES];
    private final float[] speed = new float[MAX_PARTICLES];

    private final Random random = new Random();

    private final int GRID_SIZE = 80;
    private final float WORLD_SIZE = 20.0f;
    private final float[][] snowGrid = new float[GRID_SIZE][GRID_SIZE];
    private float cloudX = 0.0f;
    private float cloudZ = 0.0f;
    private final float cloudSize = 6.0f;
    private final float cloudHeight = 15.0f;

    public SnowSystem() {
        for (int i = 0; i < MAX_PARTICLES; i++) {
            resetParticle(i, true);
        }
    }

    public void moveCloud(float dx, float dz) {
        cloudX += dx;
        cloudZ += dz;
    }

    private void resetParticle(int i, boolean randomHeight) {
        float angle = random.nextFloat() * 2.0f * (float) Math.PI;
        float r = cloudSize * (float) Math.sqrt(random.nextFloat());

        x[i] = cloudX + r * (float) Math.cos(angle);
        z[i] = cloudZ + r * (float) Math.sin(angle);

        y[i] = randomHeight ? random.nextFloat() * cloudHeight : cloudHeight;
        speed[i] = 2.0f + random.nextFloat() * 2.0f;
    }

    public void update(float dt) {
        for (int i = 0; i < MAX_PARTICLES; i++) {
            y[i] -= speed[i] * dt;
            x[i] += (float) Math.sin(y[i]) * dt * 0.5f;

            if (y[i] <= 0) {
                int col = (int) (((x[i] + WORLD_SIZE) / (WORLD_SIZE * 2)) * GRID_SIZE);
                int row = (int) (((z[i] + WORLD_SIZE) / (WORLD_SIZE * 2)) * GRID_SIZE);

                if (col >= 0 && col < GRID_SIZE && row >= 0 && row < GRID_SIZE) {
                    if (snowGrid[row][col] < 1.0f) {
                        snowGrid[row][col] += 0.05f;
                    }
                }

                resetParticle(i, false);
            }
        }
    }

    @Override
    public void draw() {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        float cellSize = (WORLD_SIZE * 2) / GRID_SIZE;
        float h = 0.01f;

        glBegin(GL_QUADS);
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                float opacity = snowGrid[row][col];

                if (opacity > 0.0f) {
                    glColor4f(1.0f, 1.0f, 1.0f, opacity);

                    float cellX = -WORLD_SIZE + col * cellSize;
                    float cellZ = -WORLD_SIZE + row * cellSize;

                    glVertex3f(cellX, h, cellZ);
                    glVertex3f(cellX + cellSize, h, cellZ);
                    glVertex3f(cellX + cellSize, h, cellZ + cellSize);
                    glVertex3f(cellX, h, cellZ + cellSize);
                }
            }
        }
        glEnd();
        glDisable(GL_BLEND);

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glLineWidth(3.0f);
        glColor4f(0.8f, 0.9f, 1.0f, 0.7f);

        glBegin(GL_LINE_LOOP);
        int segments = 36;
        for (int j = 0; j < segments; j++) {
            float angle = 2.0f * (float) Math.PI * j / segments;
            float cx = cloudX + cloudSize * (float) Math.cos(angle);
            float cz = cloudZ + cloudSize * (float) Math.sin(angle);
            glVertex3f(cx, cloudHeight, cz);
        }
        glEnd();

        glLineWidth(1.0f);
        glDisable(GL_BLEND);

        glDisable(GL_TEXTURE_2D);
        glPointSize(3.0f);
        glColor3f(1.0f, 1.0f, 1.0f);

        glBegin(GL_POINTS);
        for (int i = 0; i < MAX_PARTICLES; i++) {
            glVertex3f(x[i], y[i], z[i]);
        }
        glEnd();
    }
}
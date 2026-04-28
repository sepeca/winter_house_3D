package org.Sergei_Suprunov.models;

import static org.lwjgl.opengl.GL11.*;
import java.util.Random;
import static org.lwjgl.opengl.GL20.GL_POINT_SPRITE;
import static org.lwjgl.opengl.GL20.GL_COORD_REPLACE;
import org.Sergei_Suprunov.lwjglutils.OGLTexture2D;

public class SnowSystem implements Model {
    private OGLTexture2D groundSnowTexture;
    private int MAX_PARTICLES = 3000;
    private float[] x = new float[MAX_PARTICLES];
    private float[] y = new float[MAX_PARTICLES];
    private float[] z = new float[MAX_PARTICLES];
    private float[] speed = new float[MAX_PARTICLES];

    private final Random random = new Random();

    private final int GRID_SIZE = 600;
    private final float WORLD_SIZE = 20.0f;
    private final float[][] snowGrid = new float[GRID_SIZE][GRID_SIZE];
    private float cloudX = 0.0f;
    private float cloudZ = 0.0f;

    public int getMAX_PARTICLES() {
        return MAX_PARTICLES;
    }

    public float getCloudSize() {
        return cloudSize;
    }

    public void setCloudSize(float cloudSize) {
        this.cloudSize = cloudSize;
    }

    private float cloudSize = 6.0f;
    private final float cloudHeight = 15.0f;

    private final int ROOF_GRID_SIZE = 48;
    private float[][] roofSnowGrid = new float[ROOF_GRID_SIZE][ROOF_GRID_SIZE];
    private OGLTexture2D snowflakeTexture;

    public float[][] getRoofSnowGrid() {
        return roofSnowGrid;
    }

    public SnowSystem() {
        try {
            snowflakeTexture = new OGLTexture2D("textures/snowflake.png");
            groundSnowTexture = new OGLTexture2D("textures/snow.png");
        } catch (Exception e) {
            System.err.println("Pozor, textura nebyla nalezena, budou prezentovany jako bile tecky");
        }
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

            float surfaceY = getSurfaceHeight(x[i], z[i]);

            if (y[i] <= surfaceY) {

                if (surfaceY == 0.0f) {
                    int col = (int) (((x[i] + WORLD_SIZE) / (WORLD_SIZE * 2)) * GRID_SIZE);
                    int row = (int) (((z[i] + WORLD_SIZE) / (WORLD_SIZE * 2)) * GRID_SIZE);

                    if (col >= 0 && col < GRID_SIZE && row >= 0 && row < GRID_SIZE) {
                        if (snowGrid[row][col] < 1.0f) {
                            snowGrid[row][col] += 0.05f;
                        }
                    }
                } else {
                    int col = (int) (((x[i] + 4.8f) / 9.6f) * ROOF_GRID_SIZE);
                    int row = (int) (((z[i] + 9.8f) / 9.6f) * ROOF_GRID_SIZE);

                    if (col >= 0 && col < ROOF_GRID_SIZE && row >= 0 && row < ROOF_GRID_SIZE) {
                        if (roofSnowGrid[row][col] < 1.0f) {
                            roofSnowGrid[row][col] += 0.08f;
                        }
                    }
                }

                resetParticle(i, true);
            }
        }
    }
    private float getSurfaceHeight(float px, float pz) {

        if (px > -4.8f && px < 4.8f && pz > -9.8f && pz < -0.2f) {

            return 7.2f - (Math.abs(px) / 4.8f) * (7.2f - 3.36f);
        }
        return 0.0f;
    }

    @Override
    public void draw() {
        glEnable(GL_TEXTURE_2D);
        groundSnowTexture.bind();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        float cellSize = (WORLD_SIZE * 2) / GRID_SIZE;
        float drawSize = cellSize * 1.5f;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                float opacity = snowGrid[row][col];
                if (opacity > 0.0f) {
                    float cellX = -WORLD_SIZE + col * cellSize;
                    float cellZ = -WORLD_SIZE + row * cellSize;

                    glPushMatrix();
                    glTranslatef(cellX + cellSize/2, 0.01f, cellZ + cellSize/2);

                    glRotatef((row * col * 123) % 360, 0, 1, 0);

                    glColor4f(1.0f, 1.0f, 1.0f, opacity);

                    glBegin(GL_QUADS);

                    glTexCoord2f(0, 0); glVertex3f(-drawSize/2, 0, -drawSize/2);
                    glTexCoord2f(1, 0); glVertex3f( drawSize/2, 0, -drawSize/2);
                    glTexCoord2f(1, 1); glVertex3f( drawSize/2, 0,  drawSize/2);
                    glTexCoord2f(0, 1); glVertex3f(-drawSize/2, 0,  drawSize/2);
                    glEnd();
                    glPopMatrix();
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

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if (snowflakeTexture != null) {
            glEnable(GL_TEXTURE_2D);
            snowflakeTexture.bind();

            glEnable(GL_POINT_SPRITE);
            glTexEnvi(GL_POINT_SPRITE, GL_COORD_REPLACE, GL_TRUE);

            glPointSize(12.0f);
        } else {
            glDisable(GL_TEXTURE_2D);
            glPointSize(3.0f);
        }

        glColor3f(1.0f, 1.0f, 1.0f);

        glBegin(GL_POINTS);
        for (int i = 0; i < MAX_PARTICLES; i++) {
            glVertex3f(x[i], y[i], z[i]);
        }
        glEnd();

        if (snowflakeTexture != null) {
            glDisable(GL_POINT_SPRITE);
            glDisable(GL_TEXTURE_2D);
        }
        glDisable(GL_BLEND);
    }
    public void setMAX_PARTICLES(int newMax) {
        float[] newX = new float[newMax];
        float[] newY = new float[newMax];
        float[] newZ = new float[newMax];
        float[] newSpeed = new float[newMax];

        int countToCopy = Math.min(this.MAX_PARTICLES, newMax);
        System.arraycopy(x, 0, newX, 0, countToCopy);
        System.arraycopy(y, 0, newY, 0, countToCopy);
        System.arraycopy(z, 0, newZ, 0, countToCopy);
        System.arraycopy(speed, 0, newSpeed, 0, countToCopy);

        this.x = newX;
        this.y = newY;
        this.z = newZ;
        this.speed = newSpeed;

        if (newMax > this.MAX_PARTICLES) {
            for (int i = this.MAX_PARTICLES; i < newMax; i++) {
                resetParticle(i, true);
            }
        }

        this.MAX_PARTICLES = newMax;
    }
}
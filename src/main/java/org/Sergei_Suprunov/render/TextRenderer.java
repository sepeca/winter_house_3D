package org.Sergei_Suprunov.render;
import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;

public class TextRenderer {

    public static void drawText(String text, float x, float y, float scale, float r, float g, float b) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer charBuffer = stack.malloc(text.length() * 270);

            int quads = stb_easy_font_print(0, 0, text, null, charBuffer);

            glPushAttrib(GL_ALL_ATTRIB_BITS);
            glPushMatrix();

            glDisable(GL_DEPTH_TEST);
            glDisable(GL_LIGHTING);
            glDisable(GL_TEXTURE_2D);

            glTranslatef(x, y, 0);
            glScalef(scale, scale, 1f);

            glColor3f(r, g, b);

            glEnableClientState(GL_VERTEX_ARRAY);

            glVertexPointer(2, GL_FLOAT, 16, charBuffer);

            glDrawArrays(GL_QUADS, 0, quads * 4);

            glDisableClientState(GL_VERTEX_ARRAY);

            glPopMatrix();
            glPopAttrib();
        }
    }

}
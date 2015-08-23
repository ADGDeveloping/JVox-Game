package com.regrowthStudios.JVoxTest;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.regrowthStudios.JVox.audio.SoundManager;
import com.regrowthStudios.JVox.graphics.SpriteBatch;
import com.regrowthStudios.JVox.graphics.Window;
import com.regrowthStudios.JVox.math.Vector;
import com.regrowthStudios.JVox.math.Vector4;
import com.regrowthStudios.JVox.systems.particle.ParticleEmitter;
import com.regrowthStudios.JVox.ui.Button;
import com.regrowthStudios.JVox.utils.EventUtils.MouseEvents;

public class Start {
    private static SoundManager soundManager = null;

    public static void main(String[] args) {
        Window window = new Window();
        window.init("Test", new Vector(800, 600));
        window.create();

        {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, window.getDimensions().x, window.getDimensions().y, 0, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }

        SpriteBatch batch = new SpriteBatch();
        batch.init();
        Texture tex;
        try {
            tex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("data/textures/test.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Button testContainer = new Button();
        testContainer.init(new Vector4(0, 0, 200, 40));

        try {
            testContainer.upTexture = TextureLoader.getTexture("PNG",
                    ResourceLoader.getResourceAsStream("data/textures/up.png"));
            testContainer.downTexture = TextureLoader.getTexture("PNG",
                    ResourceLoader.getResourceAsStream("data/textures/down.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*{
            soundManager = new SoundManager();
            soundManager.init();

            Sound3D testSound = new Sound3D();
            testSound.setSoundSystem(soundManager.getSoundSystem());
            testSound.init(SoundFlags.NONE, "testsound", "data/audio/test.ogg", "test.ogg", new Vector(0, 0, 0));
            testSound.create();

            SoundPool testPool = new SoundPool();
            testPool.addSound("testsound", testSound);

            soundManager.addSoundPool("testpool", testPool);
            soundManager.play("testpool", "testsound");
        }*/

        ParticleEmitter testEmitter = new ParticleEmitter();
        testEmitter.init();
        
        byte color[] = {(byte)255,(byte) 255, (byte)255, (byte)255};

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            batch.begin();
            
            batch.draw(tex.getTextureID(), 0.0f, 0.0f, 2.0f, 2.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, color);

            batch.end();
            batch.render();
            testContainer.draw(batch);
            testContainer.update(MouseEvents.getMove(), MouseEvents.getPositionInvY());

            testEmitter.render(batch);
            testEmitter.update();

            Display.update();
        }

        //soundManager.stop("testpool", "testsound");
        //soundManager.destroy("testpool", "testsound");
        //soundManager.getSoundSystem().cleanup();
    }
}

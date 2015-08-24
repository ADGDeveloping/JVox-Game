package com.regrowthStudios.JVoxTest;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import com.regrowthStudios.JVox.audio.SoundManager;
import com.regrowthStudios.JVox.entity.Entity;
import com.regrowthStudios.JVox.graphics.Camera;
import com.regrowthStudios.JVox.graphics.SpriteBatch;
import com.regrowthStudios.JVox.graphics.Window;
import com.regrowthStudios.JVox.math.Vector;
import com.regrowthStudios.JVox.math.Vector4;
import com.regrowthStudios.JVox.systems.content.ResourceSystem;
import com.regrowthStudios.JVox.systems.particle.ParticleEmitter;
import com.regrowthStudios.JVox.ui.CheckBox;
import com.regrowthStudios.JVox.utils.EventUtils.MouseEvents;

public class Start {
    public static SoundManager soundManager = null;
    private static Camera camera = new Camera();
    private static ArrayList<ParticleEmitter> particleEmitters = new ArrayList<ParticleEmitter>();

    public static void main(String[] args) {
        Window window = new Window();
        window.init("Test", new Vector(800, 600));
        window.create();

        {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }

        {
            ResourceSystem.loadResource("textures.background.space", "data/textures/test.png", 0);
            ResourceSystem.loadResource("textures.button.up", "data/textures/up.png", 0);
            ResourceSystem.loadResource("textures.button.down", "data/textures/down.png", 0);
            ResourceSystem.loadResource("textures.particle.spark", "data/textures/spark.png", 0);
            
            ResourceSystem.loadResource("shaders.basic.texture", "./data/shaders/BasicTexture.vert, ./data/shaders/BasicTexture.frag", 1);
        }

        SpriteBatch batch = new SpriteBatch();
        batch.init();

        CheckBox testContainer = new CheckBox();
        testContainer.init(new Vector4(0, 0, 40, 40));

        Entity testEntity = new Entity(0, new Vector4(100, 100, 50, 50));

        camera.init((int) window.getDimensions().x, (int) window.getDimensions().y);

        byte color[] = { (byte) 255, (byte) 255, (byte) 255, (byte) 255 };
        Texture tex = (Texture) ResourceSystem.getResource("textures.background.space");

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            camera.update();

            Vector mp = MouseEvents.getPosition();
            mp = camera.convertScreenToWorld((float) mp.x, (float) mp.y);

            batch.begin();
            batch.draw(tex.getTextureID(), 0.0f, 0.0f, (float) window.getDimensions().x, (float) window.getDimensions().y,
                    0.0f, 0.0f, tex.getWidth(), tex.getHeight(), 0.0f, color);

            if (MouseEvents.buttonDown(0)) {
                ParticleEmitter emitter = new ParticleEmitter();
                emitter.init((Texture) ResourceSystem.getResource("textures.particle.spark"), (float) mp.x, (float) mp.y, 0.0f,
                        1.0f);
                emitter.spawnAngle = (float) Math.PI / 32.0f;
                emitter.particlesPerSpawn = 1;
                emitter.framesUntilSpawn = 1;
                emitter.setColor((byte) new Random().nextInt(255), (byte) new Random().nextInt(255), (byte) new Random().nextInt(255), (byte) 255);
                particleEmitters.add(emitter);
            }

            for (ParticleEmitter e : particleEmitters) {
                e.render(batch);
                e.update();
            }

            testContainer.draw(batch);
            testContainer.update(MouseEvents.getMove(), mp);

            testEntity.render(batch);
            testEntity.update();

            batch.end();
            batch.render(camera);

            Display.update();
        }
    }
}

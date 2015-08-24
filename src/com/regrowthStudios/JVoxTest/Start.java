package com.regrowthStudios.JVoxTest;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import com.regrowthStudios.JVox.audio.SoundManager;
import com.regrowthStudios.JVox.entity.Entity;
import com.regrowthStudios.JVox.graphics.Camera2D;
import com.regrowthStudios.JVox.graphics.SpriteBatch;
import com.regrowthStudios.JVox.graphics.Window;
import com.regrowthStudios.JVox.math.Vector;
import com.regrowthStudios.JVox.math.Vector4;
import com.regrowthStudios.JVox.physics.CollisionManager;
import com.regrowthStudios.JVox.systems.content.ResourceSystem;
import com.regrowthStudios.JVox.systems.particle.ParticleEmitter;
import com.regrowthStudios.JVox.ui.CheckBox;
import com.regrowthStudios.JVox.utils.EventUtils.KeyboardEvents;
import com.regrowthStudios.JVox.utils.EventUtils.MouseEvents;
import com.regrowthStudios.JVox.world.ChunkManager2D;

public class Start {
    public static SoundManager soundManager = null;
    private static Camera2D camera = new Camera2D();
    private static ArrayList<ParticleEmitter> particleEmitters = new ArrayList<ParticleEmitter>();
    private static CollisionManager cManager = new CollisionManager();
    
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
            ResourceSystem.loadResource("textures.misc.empty", "data/textures/empty.png", 0);

            ResourceSystem.loadResource("shaders.basic.texture",
                    "./data/shaders/BasicTexture.vert, ./data/shaders/BasicTexture.frag", 1);
        }

        SpriteBatch batch = new SpriteBatch();
        batch.init();

        CheckBox testContainer = new CheckBox();
        testContainer.init(new Vector4(0, 0, 40, 40));

        camera.init((int) window.getDimensions().x, (int) window.getDimensions().y);

        byte color[] = { (byte) 255, (byte) 255, (byte) 255, (byte) 255 };
        Texture tex = (Texture) ResourceSystem.getResource("textures.background.space");

        ChunkManager2D testManager = new ChunkManager2D(camera);
        testManager.init();

        float speed = 5.0F;

        Entity e1 = new Entity(0, new Vector4(250, -250, 50, 50));
        Entity player = new Entity(1, new Vector4(0, 0, 50, 50));
        
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            camera.update();
            Vector mp = MouseEvents.getPosition();
            mp = camera.convertScreenToWorld((float) mp.x, (float) mp.y);

            batch.begin();
            batch.draw(tex.getTextureID(), 0.0f, 0.0f, (float) window.getDimensions().x, (float) window.getDimensions().y,
                    0.0f, 0.0f, tex.getWidth(), tex.getHeight(), 0.0f, color);

            testManager.update();
            testManager.render(batch);

            {
                if (KeyboardEvents.keyDown(Keyboard.KEY_A)) {
                    camera.setPosition((float) camera.getPosition().x - speed, (float) camera.getPosition().y);
                } else if (KeyboardEvents.keyDown(Keyboard.KEY_D)) {
                    camera.setPosition((float) camera.getPosition().x + speed, (float) camera.getPosition().y);
                }

                if (KeyboardEvents.keyDown(Keyboard.KEY_S)) {
                    camera.setPosition((float) camera.getPosition().x, (float) camera.getPosition().y - speed);
                } else if (KeyboardEvents.keyDown(Keyboard.KEY_W)) {
                    camera.setPosition((float) camera.getPosition().x, (float) camera.getPosition().y + speed);
                }
            }

            if (MouseEvents.buttonDown(0)) {
                ParticleEmitter emitter = new ParticleEmitter();
                emitter.init((Texture) ResourceSystem.getResource("textures.particle.spark"), (float) mp.x, (float) mp.y, 0.0f,
                        1.0f);
                emitter.spawnAngle = (float) Math.PI / 128.0f;
                emitter.particlesPerSpawn = 1;
                emitter.framesUntilSpawn = 1;
                emitter.setColor((byte) new Random().nextInt(255), (byte) new Random().nextInt(255),
                        (byte) new Random().nextInt(255), (byte) 255);
                particleEmitters.add(emitter);
            }

            for (ParticleEmitter e : particleEmitters) {
                e.render(batch);
                e.update();
            }

            testContainer.draw(batch);
            testContainer.update(MouseEvents.getMove(), mp);
            
            {
                player.aabb.x = camera.getPosition().x;
                player.aabb.y = camera.getPosition().y;
                

                e1.update();
                player.update();
                ArrayList<Entity> e = new ArrayList<Entity>();
                e.add(player);
                e.add(e1);
                
                cManager.collide(e);
                
                e1.render(batch);

                player.render(batch);
             
            }

            batch.end();
            batch.render(camera);

            Display.update();
        }
    }
}

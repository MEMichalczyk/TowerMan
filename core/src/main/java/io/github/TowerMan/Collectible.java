package io.github.TowerMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Collectible extends Sprite {
    private boolean collected = false;
    @SuppressWarnings("FieldMayBeFinal")
    private Sound collectSound;

    public Collectible(Texture texture, float x, float y, String soundFile) {
        super(texture);
        setPosition(x, y);
        collectSound = Gdx.audio.newSound(Gdx.files.internal(soundFile));
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
        collectSound.play(0.2f);
    }
    
    public Rectangle getHitbox(){
        return new Rectangle(
        getX(), 
        getY(),
        getWidth(),
        getHeight());
    }
}

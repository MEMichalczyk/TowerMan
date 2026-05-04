package io.github.TowerMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Key extends Sprite {
    @SuppressWarnings("FieldMayBeFinal")
    private boolean collected = false;

    @SuppressWarnings("FieldMayBeFinal")
    private Sound keyCollect;

    public Key(Texture texture, float x, float y) {
        super(texture);
        setPosition(x, y);

        keyCollect = Gdx.audio.newSound(Gdx.files.internal("keyCollect.mp3"));
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
        keyCollect.play(0.5f);
    }
}

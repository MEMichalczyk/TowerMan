package io.github.TowerMan;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Key extends Collectible {
    public Key(Texture texture, float x, float y) {
        super(texture, x, y, "keyCollect.mp3");
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(
            getX() + 1,
            getY() + 2,
            getWidth() - 2,
            getHeight() - 4
        );
    }
}
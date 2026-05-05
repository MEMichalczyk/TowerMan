package io.github.TowerMan;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Coin extends Collectible {
    public Coin(Texture texture, float x, float y) {
        super(texture, x, y, "coinCollect.mp3");
        setSize(11, 11);
    }
    
    @Override
    public Rectangle getHitbox() {
        return new Rectangle(
            getX() + 3,
            getY() + 2,
            getWidth() - 6,
            getHeight() - 4
        );
    }
}

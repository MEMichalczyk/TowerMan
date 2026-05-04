package io.github.TowerMan;

import com.badlogic.gdx.graphics.Texture;

public class Coin extends Collectible {
    public Coin(Texture texture, float x, float y) {
        super(texture, x, y, "coinCollect.mp3");
    }
    
}

package io.github.TowerMan;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Slime extends Sprite{
    private float velocityX;
    private float velocityY;

    @SuppressWarnings("FieldMayBeFinal")
    private float gravity;

    @SuppressWarnings("FieldMayBeFinal")
    private float speed;

    @SuppressWarnings("FieldMayBeFinal")
    private int direction;

    public Slime(Texture texture, float posX, float posY) {
        super(texture);
        setPosition(posX, posY);
        setSize(14, 16);

        speed = 75f;
        gravity = -700f;

        direction = -1;

        velocityX = speed * direction;
        velocityY = 0;
    }

    public void applyGravity(float deltaTime) {
        velocityY += gravity * deltaTime;
        setY(getY() + velocityY * deltaTime);
    }
}

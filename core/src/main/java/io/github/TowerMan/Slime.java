package io.github.TowerMan;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Slime extends Sprite{
    @SuppressWarnings("FieldMayBeFinal")
    private float velocityX;

    @SuppressWarnings("FieldMayBeFinal")
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
        setSize(16, 16);

        speed = 35f;
        gravity = -700f;

        direction = -1;

        velocityX = speed * direction;
        velocityY = 0;
    }

    public void move(float deltaTime) {
        setX(getX() + velocityX * deltaTime);
    }

    public void applyGravity(float deltaTime) {
        velocityY += gravity * deltaTime;
        setY(getY() + velocityY * deltaTime);
    }

    public void reverseDirection() {
        direction *= -1;
        velocityX = speed * direction;
        setFlip(true, false);
    }   

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }
}

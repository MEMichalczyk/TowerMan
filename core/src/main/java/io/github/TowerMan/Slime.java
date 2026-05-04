package io.github.TowerMan;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

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

    // Constructor to initialize the slime with a texture and position
    public Slime(Texture texture, float posX, float posY) {
        super(texture);
        setPosition(posX, posY);
        setSize(16, 16);

        //speed = 30f;

        // Randomly assign a speed of either 30 or 40 to the slime
        speed = Math.random() < 0.5 ? 30f : 40f;
        
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

    // Reverse the slime's direction and update its velocity
    public void reverseDirection() {
        direction *= -1;
        velocityX = speed * direction;
        flip(true, false);
    }

    // Get the hitbox for collision detection
    // I did ask ChatGPT for help with this. I could have figured this out I am sure but I was stumped.
    public Rectangle getHitbox(){
        return new Rectangle(
        getX() + 2, 
        getY(),
        getWidth() - 8,
        getHeight() - 6);
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

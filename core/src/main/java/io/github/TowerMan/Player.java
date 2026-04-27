package io.github.TowerMan;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Sprite {
    // Player-specific variables
    private float velocityY;
    
    // Gravity and jump velocity can be adjusted as needed
    @SuppressWarnings("FieldMayBeFinal")
    private float gravity;

    @SuppressWarnings("FieldMayBeFinal")
    private float jumpVelocity;

    // Flag to check if the player is on the ground
    private boolean onGround;

    // 1 for right, -1 for left facing direction
    private int facingDirection = 1;

    // Getters and setters for player variables
    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    // Getters and setters for gravity
    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    // Getters and setters for jump velocity
    public float getJumpVelocity() {
        return jumpVelocity;
    }

    public void setJumpVelocity(float jumpVelocity) {
        this.jumpVelocity = jumpVelocity;
    }

    // Getters and setters for onGround flag
    public boolean isOnGround() {
        return onGround;
    }
    
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    // Getters and setters for facing direction
    public int getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(int facingDirection) {
        this.facingDirection = facingDirection;
    }

    // Constructor to initialize the player with a texture and set initial position and size
    public Player(Texture texture) {
        // Call the Sprite constructor to initialize the sprite with the given texture
        super(texture);
        
        // Set initial position and size of the player
        setPosition(100, 100);
        setSize(32, 32);

        // Initialize player-specific variables
        velocityY = 0f;
        gravity = -15f;
        jumpVelocity = 5f;
        onGround = true;
    }
    
    public void move() {
        // Implement player movement logic here, such as applying gravity and handling jumps
        
    }

    @Override
    public String toString() {
        return "Player{" +
                "velocityY=" + velocityY +
                ", gravity=" + gravity +
                ", jumpVelocity=" + jumpVelocity +
                ", onGround=" + onGround +
                ", facingDirection=" + facingDirection +
                '}';
    }
}

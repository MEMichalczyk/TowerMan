package io.github.TowerMan;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Sprite {
    // Player-specific variables
    private float velocityY = 0f;
    
    // Gravity and jump velocity can be adjusted as needed
    @SuppressWarnings("FieldMayBeFinal")
    private float gravity = -15f;

    @SuppressWarnings("FieldMayBeFinal")
    private float jumpVelocity = 5f;

    // Flag to check if the player is on the ground
    private boolean isOnGround = true;

    // 1 for right, -1 for left facing direction
    private int facingDirection = 1;

    // Constructor to initialize the player with a texture and set initial position and size
    public Player() {
        setPosition(1, 0);
        setSize(1, 1);
    }
    
}

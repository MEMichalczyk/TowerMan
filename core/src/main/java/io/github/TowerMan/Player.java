package io.github.TowerMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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

    // Sound for player jump
    private Sound playerJump;

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
        setPosition(20, 20); //This will change with map.
        setSize(46, 46); // Adjust size as needed

        // Initialize player-specific variables
        velocityY = 0f;
        gravity = -900f;
        jumpVelocity = 275f;
        onGround = false;

        // Load the jump sound effect
        playerJump = Gdx.audio.newSound(Gdx.files.internal("playerJump.mp3"));
    }
    
    public void move() {
        // Implement player movement logic here, such as applying gravity and handling jumps
        float speed = 120f; // Example horizontal speed
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Move left with the left arrow key or A key
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.translateX(-speed * deltaTime);

            // Flip the sprite to face left
            this.setFlip(true, false);
            facingDirection = -1;
        }

        // Move right with the right arrow key or D key
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.translateX(speed * deltaTime);

            // Flip the sprite to face right
            this.setFlip(false, false);
            facingDirection = 1;
        }

        // LOOK INTO GETTING A BETTER JUMP! HOLD TO JUMP HIGHER, ETC. ----------------------------------------------------
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
            velocityY = jumpVelocity;
            onGround = false;

            
            // Play the jump sound effect
            playerJump .play(0.2f); // Adjust volume as needed
        }

        //Bounds? Check later ----------------------------------------------------
    }

    // Gravity
    public void applyGravity(float deltaTime) {
        if (!onGround) {
            velocityY += gravity * deltaTime;
            this.translateY(velocityY * deltaTime);

            //JUMP HOLD CHECK WILL PROBABLY GO HERE LATER ----------------------------------------------------
            
            // Check if the player has landed on the ground. Replace with collision detection with the map later.
            //I WILL NEED TO CHANGE THIS ONCE I APPLY THE MAP FROM TILED! -------------------------------
            //if (this.getY() <= 16) {
                //this.setY(16);
                //onGround = true;
                //velocityY = 0;
            //}
        }
    }

    //toString method sends information to terminal.
    @Override
    public String toString() {
        return "Player{" +
                "velocityY= " + velocityY +
                ", gravity= " + gravity +
                ", jumpVelocity= " + jumpVelocity +
                ", onGround= " + onGround +
                ", facingDirection= " + facingDirection +
                '}';
    }
}

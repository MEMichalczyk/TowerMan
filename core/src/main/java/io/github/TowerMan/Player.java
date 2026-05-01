package io.github.TowerMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Sprite {

    //Requesting a jump
    private boolean jumpRequest = false;

    // Player-specific variables
    private float velocityX;

    private float speed;

    private float velocityY;

    // Hold the players previous Y - checking for platforms
    private float previousY;
    
    // Gravity and jump velocity can be adjusted as needed
    @SuppressWarnings("FieldMayBeFinal")
    private float gravity;

    @SuppressWarnings("FieldMayBeFinal")
    private float jumpVelocity;

    // Flag to check if the player is on the ground
    private boolean onGround;

    // Flag to check if player is on a ladder
    private boolean onLadder = false;

    // 1 for right, -1 for left facing direction
    private int facingDirection = 1;

    // Sound for player jump
    @SuppressWarnings("FieldMayBeFinal")
    private Sound playerJump;

    @SuppressWarnings("FieldMayBeFinal")
    private Sound playerDeath;

    // Getters and setters for player variables
    public boolean isOnLadder(){
        return onLadder;
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

    //------------------------------------------------------------------
    // Constructor to initialize the player with a texture and set initial position and size
    public Player(Texture texture) {
        // Call the Sprite constructor to initialize the sprite with the given texture
        super(texture);
        
        // Set initial position and size of the player
        setPosition(2 * 16, 2 * 16); //This will change with map.
        setSize(18, 20); // Adjust size as needed

        // Initialize player-specific variables
        velocityY = 0f;
        gravity = -700f;
        jumpVelocity = 175f;
        onGround = false;

        playerJump = Gdx.audio.newSound(Gdx.files.internal("playerJump.mp3"));
        playerDeath = Gdx.audio.newSound(Gdx.files.internal("deathSound.mp3"));
    }
    
    //------------------------------------------------------------------
    public void move() {
        // Implement player movement logic here, such as applying gravity and handling jumps
        previousY = getY();

        speed = 100f; // Example horizontal speed

        velocityX = 0;

        // Move left with the left arrow key or A key
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocityX = -speed;

            // Flip the sprite to face left
            setFlip(true, false);
            facingDirection = -1;
        }

        //------------------------------------------------------------------
        // Move right with the right arrow key or D key
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocityX = speed;

            // Flip the sprite to face right
            setFlip(false, false);
            facingDirection = 1;
        }

        //------------------------------------------------------------------
        // LOOK INTO GETTING A BETTER JUMP! HOLD TO JUMP HIGHER, ETC. ----------------------------------------------------
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            jumpRequest = true;
        }

        //------------------------------------------------------------------
        // Ladder Movement
        if (onLadder){
            velocityY = 0f; // So we don't fall
            
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                velocityY = 80f;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                velocityY = -80f;
            }

        }
    }

    // For player being on a ladder set to onLadder
    public void setOnLadder(boolean onLadder) {
        this.onLadder = onLadder;
    }

    //------------------------------------------------------------------
    // Gravity
    public void applyGravity(float deltaTime) {
        if (!onGround) {
            velocityY += gravity * deltaTime;
        }
    }

    //------------------------------------------------------------------
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

    //------------------------------------------------------------------
    // Getters/Setters
    public float getPreviousY() {
        return previousY;
    }

    public void setPreviousY(float previousY) {
        this.previousY = previousY;
    }

    public boolean isJumpRequested(){
        return jumpRequest;
    }

    public void clearJumpRequest() {
        jumpRequest = false;
    }

    public void playJumpSound() {
        playerJump.play(0.2f);
    }

    public void playDeathSound() {
        playerDeath.play(0.2f);
    }
}

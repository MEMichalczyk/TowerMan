package io.github.TowerMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    @SuppressWarnings("FieldMayBeFinal")
    private float WORLD_WIDTH = 320;
    @SuppressWarnings("FieldMayBeFinal")
    private float WORLD_HEIGHT = 384;
    @SuppressWarnings("FieldMayBeFinal")
    private int SCALE = 2;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private OrthographicCamera camera;

    private FitViewport viewport;

    private SpriteBatch batch;

    private Player player;
    private Texture playerTexture;

    private Music backgroundMusic;

    private Array<Rectangle> platform;
    private Array<Rectangle> ladder;
    private Array<Rectangle> spike;
    private Array<Rectangle> win;

    private int deaths = 0;
    private BitmapFont deathFont;
    private BitmapFont winFont;

    private Array<Slime> slimes;
    private Texture slimeTexture;

    private boolean hasWon = false;
    private Sound winSound;

    //-----------------------------------------------------------------------------------------------------------------
    //SHOW
    @Override
    public void show() {
        // Camera and Viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
        
        Gdx.graphics.setWindowedMode(320 * SCALE , 384 * SCALE); // Set the window size. Adjust as needed.

        deathFont = new BitmapFont();

        winFont = new BitmapFont();
        winFont.getData().setScale(2); // Make the win message larger. Adjust as needed.
        winFont.setColor(1, 1, 0, 1); // Set the win message color to yellow. Adjust as needed.


        // Map
        map = new TmxMapLoader().load("TowerMan3.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1f); // Adjust unit scale as needed

        //--------------------------------------------------------------
        // The Array of platforms.
        platform = loadRectangles("Platforms");
        
        // The Array of Ladders
        ladder = loadRectangles("Ladders");

        // The Array of Spikes
        spike = loadRectangles("Spikes");

        // The Array of Win zones
        win = loadRectangles("Win");
        //--------------------------------------------------------------

        // Initialize the player and its texture
        batch = new SpriteBatch();
        playerTexture = new Texture("Player.png");
        player = new Player(playerTexture);

        // Initialize the slimes and its textures
        slimeTexture = new Texture("Slime.png");
        
        // Create and add slimes to the array. Adjust positions as needed. Organized right now bottom to top.
        slimes = new Array<>();
        slimes.add(new Slime(slimeTexture, 220, 79));
        slimes.add(new Slime(slimeTexture, 100, 97));
        slimes.add(new Slime(slimeTexture, 222, 225));
        slimes.add(new Slime(slimeTexture, 195, 287));
        slimes.add(new Slime(slimeTexture, 70, 337));

        //--------------------------------------------------------------
        // Load and play background music
        winSound = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("BGM.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.3f); // Adjust volume as needed
        backgroundMusic.play();
    }

    //------------------------------------------------------------------------------------------------------------------
    //RENDER
    @Override
    public void render(float delta) {
        // Update player position and apply gravity
        if(!hasWon) {
            player.move();
            player.applyGravity(delta);
            
            for (Slime slime : slimes){
                slime.move(delta);
                slimeCollisionX(slime);
                checkSlimeLedge(slime);
                slime.applyGravity(delta);
                slimeCollisionY(slime);
            }
            
            //Collision and Jump
            player.setX(player.getX() + player.getVelocityX() * delta);
            playerCollisionX();
            
            player.setY(player.getY() + player.getVelocityY() * delta);
            playerCollisionY();
            
            // Ladder/Damage checks
            checkLadder();
            checkSpikes();
            checkSlimeTouch();
            
            checkWin();
            // Handle player jump input
            if (player.isOnGround() && player.isJumpRequested()) {
                player.setVelocityY(player.getJumpVelocity());
                player.setOnGround(false);
                player.clearJumpRequest();
                player.playJumpSound();
            }
        }
        
        camera.update();

        //--------------------------------------------------------------
        // Wipe the screen so that we don't see anything from a previous frame.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        //--------------------------------------------------------------
        // Render the map
        mapRenderer.setView(camera);
        mapRenderer.render();

        //--------------------------------------------------------------
        // Render the player and the slimes
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        deathFont.draw(batch, "Deaths: " + deaths, 7, 14);
        player.draw(batch);

        // Render all slimes in the array
        for (Slime slime : slimes) {
            slime.draw(batch);
        }

        // If the player has won, display a win message
        if (hasWon) {
            winFont.draw(batch, "YOU WIN!", 90, 220);
            deathFont.draw(batch, "ESC TO EXIT", 115, 190);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        batch.end();
    }

    //--------------------------------------------------------------------------------------------------
    //METHODS
    
    // Stops horizontal movement on platforms
    private void playerCollisionX(){
        Rectangle bounds = player.getHitbox();

        for (Rectangle rect : platform){
            if (bounds.overlaps(rect)){
                if (player.getVelocityX() > 0){
                    player.setX(rect.x - player.getWidth());
                }

                if (player.getVelocityX() < 0) {
                    player.setX(rect.x + rect.width);
                }

                player.setVelocityX(0);

                bounds = player.getHitbox();
            }
        }
    }

    // Stops horizontal movement on platforms for slimes and reverses their direction
    private void slimeCollisionX(Slime slime){
        Rectangle bounds = slime.getHitbox();

        for (Rectangle rect : platform){
            if (bounds.overlaps(rect)){
                if (slime.getVelocityX() > 0){
                    slime.setX(rect.x - slime.getWidth());
                }

                if (slime.getVelocityX() < 0) {
                    slime.setX(rect.x + rect.width);
                }

                slime.reverseDirection();
                break;
            }
        }
    }

    //------------------------------------------------------------------
    // Stops vertical movement on platforms.
    private void playerCollisionY(){
        Rectangle bounds = player.getHitbox();

        player.setOnGround(false);

        for (Rectangle rect : platform){
            if (bounds.overlaps(rect)){
                if (player.getVelocityY() <= 0){
                    player.setY(rect.y + rect.height);
                    player.setVelocityY(0);
                    player.setOnGround(true);
                }

                if (player.getVelocityY() > 0) {
                    player.setY(rect.y - player.getHeight());
                    player.setVelocityY(0);
                }

                bounds = player.getHitbox();

            }
        }
    }

    // Stops vertical movement on platforms for slimes
    private void slimeCollisionY(Slime slime){
        Rectangle bounds = slime.getHitbox();

        for (Rectangle rect : platform){
            if (bounds.overlaps(rect)){
                if (slime.getVelocityY() <= 0){
                    slime.setY(rect.y + rect.height);
                    slime.setVelocityY(0);
                }

                if (slime.getVelocityY() > 0) {
                    slime.setY(rect.y - slime.getHeight());
                    slime.setVelocityY(0);
                }

                bounds = slime.getHitbox();
            }
        }
    }

    // Check if the player has reached a win zone
    private void checkWin(){
        if (hasWon) return;
        
        Rectangle bounds = player.getHitbox();
        
        for (Rectangle rect : win) {
            if (bounds.overlaps(rect)){
                hasWon = true;
                winSound.play(0.2f);
                break;
            }
        }
    }

    // Check if the player is on spikes
    private void checkSpikes(){
        Rectangle bounds = player.getHitbox();

        for (Rectangle rect : spike) {
            if (bounds.overlaps(rect)){
                resetPlayer();
                player.playDeathSound();
                break;
            }
        }
    }

    // Check if the player is touching a slime
    private void checkSlimeTouch(){
        for (Slime slime : slimes) {
            if (player.getHitbox().overlaps(slime.getHitbox())) {
                resetPlayer();
                player.playDeathSound();
                break;
            }
        }
    }

    // Check if the player is on a ladder and allow climbing
    private void checkLadder(){
        Rectangle bounds = player.getHitbox();

        player.setOnLadder(false);

        for (Rectangle rect : ladder) {
            if (bounds.overlaps(rect)){
                if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
                    player.setOnLadder(true);
                    break;
                }
            }
        }
    }

    // Reset the player to the start of the level
    private void resetPlayer() {
        player.setPosition(2 * 16, 2 * 16);
        player.setVelocityX(0);
        player.setVelocityY(0);
        player.setOnGround(false);
        deaths++;
    }

    // Slime checks for ledges and turns around when it hits one. It also turns around when it hits a wall.
    private void checkSlimeLedge(Slime slime){
        float checkX;

        // Check the direction of the slime to determine which side to check for a ledge
        if (slime.getVelocityX() > 0) {
            checkX = slime.getX() + slime.getWidth() + 1;
        } else {
            checkX = slime.getX() - 1;
        }

        // We check slightly below the slime's current position to see if there is ground ahead
        float checkY = slime.getY() - 1;
        boolean groundAhead = false;

        // Check if there is ground ahead of the slime by checking if the point (checkX, checkY) is within any platform rectangle
        for (Rectangle rect : platform) {
            if (rect.contains(checkX, checkY)) {
                groundAhead = true;
                break;
            }
        }

        // If there is no ground ahead, reverse the slime's direction
        if (!groundAhead) {
            slime.reverseDirection();
        }
    }

    //------------------------------------------------------------------
    // Method for getting rectangle objects from map!
    private Array<Rectangle> loadRectangles(String layerName) {
        Array<Rectangle> rectangles = new Array<>();

        MapObjects objects = map.getLayers().get(layerName).getObjects();

        // Go through the objects and store them to the list
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject r) {
                Rectangle rect = r.getRectangle();
                rectangles.add(rect);
            }
        }

        return rectangles;
    }

    //------------------------------------------------------------------
    @Override
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height);
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    // Dispose of assets when the screen is destroyed.
    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        map.dispose();
        mapRenderer.dispose();
        backgroundMusic.dispose();
        slimeTexture.dispose();
        deathFont.dispose();
        winFont.dispose();
        winSound.dispose();
    }
}
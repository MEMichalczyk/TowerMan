package io.github.TowerMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

    private int deaths = 0;
    private BitmapFont font;

    private Slime slimes;
    private Texture slimeTexture;

    //------------------------------------------------------------------
    @Override
    public void show() {
        // Camera and Viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
        
        Gdx.graphics.setWindowedMode(320 * SCALE , 384 * SCALE); // Set the window size. Adjust as needed.

        font = new BitmapFont();
        //--------------------------------------------------------------
        // Map
        map = new TmxMapLoader().load("TowerMan3.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1f); // Adjust unit scale as needed

        //--------------------------------------------------------------
        // The Array of platforms.
        platform = loadRectangles("Platforms");
        
        /* OG turned into a method for all rectangles (HOPEFULLY....)
        platform = new Array<>();
        
        // Pull the Platforms layer from the Tiled Map
        MapObjects objects = map.getLayers().get("Platforms").getObjects();
        System.out.println("Number of platform objects: " + objects.getCount());

        // Go through the objects and store them to the list
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject r) {
                Rectangle rect = r.getRectangle();
                platform.add(rect);
            }
        }
        
        for (Rectangle rect : platform) {
            System.out.println("Platform: " + rect);
        }
        */
    
        //--------------------------------------------------------------
        // The Array of Ladders
        ladder = loadRectangles("Ladders");
        //--------------------------------------------------------------

        //--------------------------------------------------------------
        // The Array of Spikes
        spike = loadRectangles("Spikes");
        //--------------------------------------------------------------

        // Initialize the player and its texture
        batch = new SpriteBatch();
        playerTexture = new Texture("Player.png");
        player = new Player(playerTexture);

        // Initialize the slime and its texture
        slimeTexture = new Texture("Slime.png");
        slimes = new Slime(slimeTexture, 200, 100);

        System.out.println("Player starts at: X=" + player.getX() + ", Y=" + player.getY());

        //--------------------------------------------------------------
        // Load and play background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("BGM.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.3f); // Adjust volume as needed
        backgroundMusic.play();
    }

    //------------------------------------------------------------------
    //RENDER
    @Override
    public void render(float delta) {
        // Update player position and apply gravity
        player.move();
        player.applyGravity(delta);

        slimes.move(delta);
        slimeCollisionX();
        checkSlimeLedge();
        slimes.applyGravity(delta);
        slimeCollisionY();

        //Collision and Jump
        player.setX(player.getX() + player.getVelocityX() * delta);
        collisionX();
        
        player.setY(player.getY() + player.getVelocityY() * delta);
        collisionY();

        checkLadder();
        checkSpikes();

        // Handle player jump input
        if (player.isOnGround() && player.isJumpRequested()) {
            player.setVelocityY(player.getJumpVelocity());
            player.setOnGround(false);
            player.clearJumpRequest();
            player.playJumpSound();
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
        // Render the player and slimes
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "Deaths: " + deaths, 3, 14);
        player.draw(batch);
        slimes.draw(batch);
        batch.end();
    }

    //------------------------------------------------------------------
    // Stops horizontal movement on platforms
    private void collisionX(){
        Rectangle bounds = player.getBoundingRectangle();

        for (Rectangle rect : platform){
            if (bounds.overlaps(rect)){
                if (player.getVelocityX() > 0){
                    player.setX(rect.x - player.getWidth());
                }

                if (player.getVelocityX() < 0) {
                    player.setX(rect.x + rect.width);
                }

                player.setVelocityX(0);

                bounds = player.getBoundingRectangle();
            }
        }
    }

    private void slimeCollisionX(){
        Rectangle bounds = slimes.getBoundingRectangle();

        for (Rectangle rect : platform){
            if (bounds.overlaps(rect)){
                if (slimes.getVelocityX() > 0){
                    slimes.setX(rect.x - slimes.getWidth());
                }

                if (slimes.getVelocityX() < 0) {
                    slimes.setX(rect.x + rect.width);
                }

                slimes.reverseDirection();

                //bounds = slimes.getBoundingRectangle();
                break;
            }
        }
    }

    //------------------------------------------------------------------
    // Stops vertical movement on platforms.
    private void collisionY(){
        Rectangle bounds = player.getBoundingRectangle();

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

                bounds = player.getBoundingRectangle();

            }
        }
    }

    private void slimeCollisionY(){
        Rectangle bounds = slimes.getBoundingRectangle();

        for (Rectangle rect : platform){
            if (bounds.overlaps(rect)){
                if (slimes.getVelocityY() <= 0){
                    slimes.setY(rect.y + rect.height);
                    slimes.setVelocityY(0);
                }

                if (slimes.getVelocityY() > 0) {
                    slimes.setY(rect.y - slimes.getHeight());
                    slimes.setVelocityY(0);
                }

                bounds = slimes.getBoundingRectangle();

            }
        }
    }

    // Check if the player is on spikes
    private void checkSpikes(){
        Rectangle bounds = player.getBoundingRectangle();

        for (Rectangle rect : spike) {
            if (bounds.overlaps(rect)){
            resetPlayer();
            player.playDeathSound();
            break;
            }
        }
    }

    private void checkLadder(){
        Rectangle bounds = player.getBoundingRectangle();

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
    private void checkSlimeLedge() {
        float checkX;

        if (slimes.getVelocityX() > 0) {
            checkX = slimes.getX() + slimes.getWidth() + 1;
        } else {
            checkX = slimes.getX() - 1;
        }

        float checkY = slimes.getY() - 1;

        boolean groundAhead = false;

        for (Rectangle rect : platform) {
            if (rect.contains(checkX, checkY)) {
                groundAhead = true;
                break;
            }
        }

        if (!groundAhead) {
            slimes.reverseDirection();
        }
    }

    //------------------------------------------------------------------
    // Method for getting rectangle objects from map!
    private Array<Rectangle> loadRectangles(String layerName) {
        Array<Rectangle> rectangles = new Array<>();

        MapObjects objects = map.getLayers().get(layerName).getObjects();
        
        //Shows objects
        System.out.println("Number of objects: " + layerName + " " + objects.getCount());

        // Go through the objects and store them to the list
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject r) {
                Rectangle rect = r.getRectangle();
                rectangles.add(rect);
            }
        }
        
        //Check for platform data
        for (Rectangle rect : rectangles) {
            System.out.println("Platform: " + rect);
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
    }
}
package io.github.TowerMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private Player player;
    private Texture playerTexture;

    @Override
    public void show() {
        // Load the Tiled map and set up the renderer and camera
        map = new TmxMapLoader().load("TowerMan.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();

        // Set the camera to show map.
        camera.setToOrtho(false, 224, 256);

        // Initialize the player and its texture
        batch = new SpriteBatch();
        playerTexture = new Texture("Player.png");
        player = new Player(playerTexture);
    }

    @Override
    public void render(float delta) {
        // Wipe the screen so that we don't see anything from a previous frame.

        player.move();
        player.applyGravity(delta);

        System.out.println(player);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        batch.begin();
        // Render the map
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Render the player
        player.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

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

    @Override
    public void dispose() {
        // Dispose of assets when no longer needed to free up resources.
    }
}
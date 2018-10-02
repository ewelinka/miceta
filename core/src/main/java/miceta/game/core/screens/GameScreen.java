package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import miceta.game.core.Assets;
import miceta.game.core.controllers.CvWorldController;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.Constants;

import java.util.ArrayList;


/**
 * Created by ewe on 8/10/17.
 */
public class GameScreen extends AbstractGameScreen {
    private static final String TAG = GameScreen.class.getName();
    protected ShapeRenderer shapeRenderer;
    private int shiftX =50;
    private int shiftY =50;
    private BitmapFont font = new BitmapFont();
    private SpriteBatch spriteBatch  = new SpriteBatch();
    private FeedbackDrawManager fd;
    private boolean first_time = true;
    protected boolean showMenu;


    public GameScreen(miCeta game){
        super(game);
    }

    @Override
    public void show() {
        Gdx.app.log(TAG," we start the SHOW! "+Gdx.graphics.getWidth());
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        worldController = new CvWorldController(game,stage);
        shapeRenderer = new ShapeRenderer();
        showMenu = false;
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0,0);
        //Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Do not update game world when paused.
        if (!paused) {
            worldController.update(deltaTime);
            stage.act(deltaTime);
        }
        stage.draw();
        spriteBatch.begin();
        spriteBatch.draw(Assets.instance.background.generic,0,0);
        spriteBatch.end();

        if(!showMenu) { // if we do not draw menu, we draw blocks as feedback
            ArrayList<Integer> cValues = worldController.getCurrentBlocksValuesFromManager();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (int i=0;i<cValues.size();i++) {
                setColorFromValue(cValues.get(i));
                if (first_time) {
                    fd = new FeedbackDrawManager();
                    first_time = false;
                }
                // TODO considerar caso mas de 10 bloques en area de deteccion
                fd.drawBlockByValue(shapeRenderer, cValues.get(i), shiftX + i * 10 + Constants.BASE * i, shiftY);
            }
            shapeRenderer.end();
        }

        if(showMenu) {
            spriteBatch.begin();
            font.draw(spriteBatch, "" + worldController.getRandomNumber(), 200, 680);
            font.draw(spriteBatch, "jugar", 550, 1000);
            font.draw(spriteBatch, "feedback", 10, 1000);
            font.draw(spriteBatch, "Reset", 300, 1000);

            font.draw(spriteBatch, "Mi IP: " + game.getMyIp(), 10, 160);

            font.draw(spriteBatch, "Velocidad entre golpes:", 10, 140);
            font.draw(spriteBatch, "más lento", 10, 120);
            font.draw(spriteBatch, "Delay extra: " + String.format("%.2g%n", worldController.getExtraDelayBetweenFeedback()), 250, 120);
            font.draw(spriteBatch, "más rápido", 400, 120);

            font.draw(spriteBatch, "Tiempo de espera entre series:", 10, 80);
            font.draw(spriteBatch, "más tiempo", 10, 60);
            font.draw(spriteBatch, "Tiempo: " + String.format("%.2g%n", worldController.getWaitAfterKnock()), 250, 60);
            font.draw(spriteBatch, "menos tiempo", 400, 60);

            spriteBatch.end();
        }

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        Gdx.app.log(TAG," we start the HIDE of the screen ! " +Gdx.graphics.getWidth()+" h "+Gdx.graphics.getHeight());
        //   Gdx.input.setCatchBackKey(false);
        dispose();

    }

    @Override
    public void pause() {
        Gdx.app.log(TAG," we start the PAUSE of the screen ! " +Gdx.graphics.getWidth()+" h "+Gdx.graphics.getHeight());
        paused =true;

    }

    @Override
    public void resume () {
        Gdx.app.log(TAG," we start the RESUME of the screen ! " +Gdx.graphics.getWidth()+" h "+Gdx.graphics.getHeight());
        super.resume();
        // Only called on Android!
        paused = false;
    }

    @Override
    public void dispose(){
        Gdx.app.log(TAG," we start the DISPOSE of the screen ! " +Gdx.graphics.getWidth()+" h "+Gdx.graphics.getHeight());
        stage.dispose();

    }

    @Override
    public InputProcessor getInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(worldController);
        return multiplexer;
    }

    private float radianToStage(double r){
        float d = (float) Math.toDegrees(r*-1 - Math.PI/2);
        if(d < 0)
            d = 360 + d;
        return d;
    }

    private void setColorFromValue(int value){
        switch (value){
            case 1:
                shapeRenderer.setColor(1, 1, 0, 1); // yellow
                break;
            case 2:
                shapeRenderer.setColor(0, 1, 0, 1); // green
                break;
            case 3:
                shapeRenderer.setColor(0, 0, 1, 1); // blue
                break;
            case 4:
                shapeRenderer.setColor(1, 159/255.0f, 0, 1); // orange
                break;
            case 5:
                shapeRenderer.setColor(1, 0, 0, 1); //red
                break;
        }
    }






}

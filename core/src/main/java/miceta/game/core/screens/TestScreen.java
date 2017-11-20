package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import edu.ceta.vision.core.blocks.Block;
import miceta.game.core.controllers.CvWorldController;
import miceta.game.core.miCeta;
import miceta.game.core.util.Constants;

import java.util.Set;


/**
 * Created by ewe on 8/10/17.
 */
public class TestScreen extends AbstractGameScreen {
    private static final String TAG = TestScreen.class.getName();
    protected ShapeRenderer shapeRenderer;
    private int shiftX =70;
    private int shiftY =100;
    private BitmapFont font = new BitmapFont();
    private SpriteBatch spriteBatch  = new SpriteBatch();

    public TestScreen(miCeta game){
        super(game);
    }

    @Override
    public void show() {
        Gdx.app.log(TAG," we start the SHOW! "+Gdx.graphics.getWidth());
        stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH , Constants.VIEWPORT_HEIGHT));
        worldController = new CvWorldController(game,stage);
        shapeRenderer = new ShapeRenderer();

        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);

    }

    @Override
    public void render(float deltaTime) {
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed / 255.0f, 0xff / 255.0f);

        //Gdx.app.log(TAG,"famerate " + Gdx.graphics.getFramesPerSecond());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Do not update game world when paused.
        if (!paused) {
            worldController.update(deltaTime);
            stage.act(deltaTime);
        }
        stage.draw();

        Set<Block> cBlocks = worldController.getCurrentBlocksFromManager();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(shiftX, shiftY, 480, 480);

        if(cBlocks!=null) {
            for (Block block : cBlocks) {
                setColorFromValue(block.getValue());
                shapeRenderer.rect((float) block.getCenter().y+shiftX, (float) block.getCenter().x+shiftY,
                        Constants.BASE * block.getValue(), Constants.BASE,
                        Constants.BASE * block.getValue() / 2, Constants.BASE / 2,
                        radianToStage(block.getOrientation()));
            }
        }
        shapeRenderer.end();

        spriteBatch.begin();
        font.draw(spriteBatch,""+worldController.getRandomNumber(),200,680);
        font.draw(spriteBatch,"jugar",550,1000);
        font.draw(spriteBatch,"feedback",10,1000);
        spriteBatch.end();

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

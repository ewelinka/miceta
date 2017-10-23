package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.controllers.CvWorldController;
import miceta.game.core.miCeta;
import miceta.game.core.util.Constants;


/**
 * Created by ewe on 8/10/17.
 */
public class TestScreen extends AbstractGameScreen {
    private static final String TAG = TestScreen.class.getName();

    public TestScreen(miCeta game){
        super(game);
    }

    @Override
    public void show() {
        Gdx.app.log(TAG," we start the SHOW! "+Gdx.graphics.getWidth());
        stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH , Constants.VIEWPORT_HEIGHT));
        worldController = new CvWorldController(game,stage);

        // android back key
        Gdx.input.setCatchBackKey(true);

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

}

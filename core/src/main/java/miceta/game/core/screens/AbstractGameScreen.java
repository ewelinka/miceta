package miceta.game.core.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.controllers.CvWorldController;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;
import miceta.game.core.util.GameScreen;

/**
 * Created by ewe on 8/10/17.
 */
public abstract class AbstractGameScreen  extends InputAdapter implements Screen {
    public static final String TAG = AbstractGameScreen.class.getName();
    protected miCeta game;
    protected Stage stage;
    protected CvWorldController worldController;
    protected boolean paused;
    protected int viewportWidth, viewportHeight;
    protected GameScreen gameScreen;
    protected boolean upLevel;


    public AbstractGameScreen (miCeta game, boolean upLevel){
        this.game = game;
        this.upLevel = upLevel;
        gameScreen = game.updateGameScreen();
        paused = false;

        if(Gdx.app.getType() == Application.ApplicationType.Android){
            viewportWidth = Constants.ANDROID_WIDTH;
            viewportHeight = Constants.ANDROID_HEIGHT;
        }else{
            viewportWidth = Constants.DESKTOP_WIDTH;
            viewportHeight = Constants.DESKTOP_HEIGHT;
        }
    }

    public abstract void render (float deltaTime);

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        dispose();

    }

    @Override
    public void pause() {
        paused =true;

    }

    @Override
    public void resume () {
        // Only called on Android!
        paused = false;
    }

    @Override
    public void dispose(){
        stage.dispose();
    }

    public InputProcessor getInputProcessor(){

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);

        return multiplexer;
    }


    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        Gdx.app.log(TAG, " TOUCHED  down" + screenX + " " + screenY);
        if (button == Input.Buttons.RIGHT){
            stopCurrentSound();
            game.setScreen(new IntroScreen(game));
        }
        else {
            if(worldController != null) {
                Gdx.app.log(TAG, " TOUCHED " + screenX + " " + screenY);
                if (Gdx.app.getType() == Application.ApplicationType.Android) {
                    worldController.touchDownAndroid(screenX, screenY, button);
                } else {
                    worldController.touchDownDesktop(screenX, screenY, button);
                }
            }
        }
        return true;
    }
    @Override
    public boolean keyDown(int keycode){
        switch (keycode)
        {
            case Input.Keys.ESCAPE:
                stopCurrentSound();
                game.setScreen(new MenuScreen(game));
                break;
            case Input.Keys.F1:
                stopCurrentSound();
                LevelsManager.instance.forceLevelParams(1);
                game.setScreen(new ConcreteTutorial(game,0, 0,false));
                break;
            case Input.Keys.LEFT:
                AudioManager.instance.downFeedbackVolSound();
                break;
            case Input.Keys.RIGHT:
                AudioManager.instance.upFeedbackVolSound();
                break;
            case Input.Keys.UP:
                AudioManager.instance.upKnockNoteVol();
                break;
            case Input.Keys.DOWN:
                AudioManager.instance.downKnockNoteVol();
                break;
            case Input.Keys.Q:
                if(worldController!= null){ //in concrete tutorial there is no worldController
                    stopCurrentSound();
                    worldController.forceScreenFinish();
                }
                break;

        }
        return true;
    }


    public void stopCurrentSound(){

        AudioManager.instance.stop_sounds(game.getGameScreen().screenName);

    }
}



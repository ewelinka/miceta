package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.controllers.CvWorldControllerTutorial;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.miCeta;

/**
 * Created by ewe on 11/16/17.
 */
public class TutorialScreen extends BaseScreen {
    private static final String TAG = TutorialScreen.class.getName();
    private boolean _upLevel = false;

    public TutorialScreen(miCeta game, boolean upLevel) {

        super(game);
        _upLevel = upLevel;
    }

    @Override
    public void show() {


        Gdx.app.log(TAG," we start the SHOW! "+Gdx.graphics.getWidth());
        stage = new Stage(new FitViewport(viewportWidth , viewportHeight));
        worldController = new CvWorldControllerTutorial(game,stage, _upLevel);

        shapeRenderer = new ShapeRenderer();
        fd = new FeedbackDrawManager();

        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);

    }


    public boolean touchDown (int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.RIGHT){
            game.setScreen(new IntroScreen(game));
        }

        return true;
    }

}


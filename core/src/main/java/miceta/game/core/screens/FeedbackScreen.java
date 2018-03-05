package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.controllers.CvWorldControllerFeedback;
import miceta.game.core.miCeta;

/**
 * Created by ewe on 11/16/17.
 */
public class FeedbackScreen extends BaseScreen {
    private static final String TAG = FeedbackScreen.class.getName();

    public FeedbackScreen(miCeta game) {
        super(game, false);
    }

    @Override
    public void show() {
        Gdx.app.log(TAG," we start the SHOW! "+Gdx.graphics.getWidth());
        stage = new Stage(new FitViewport(viewportWidth , viewportHeight));
        worldController = new CvWorldControllerFeedback(game,stage);
        shapeRenderer = new ShapeRenderer();

        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);

    }
}

package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.controllers.CvWithIntroController;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.GameScreen;

/**
 * Created by ewe on 1/29/18.
 */
public class BaseScreenWithIntro extends BaseScreen {
    private static final String TAG = BaseScreenWithIntro.class.getName();

    public BaseScreenWithIntro(miCeta game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
       // Sound[] nowSounds = game.getSoundsForCurrent();
        worldController = new CvWithIntroController(game,stage,
                gameScreen.feedbackSoundType,
                gameScreen.intro,
                gameScreen.positive,
                gameScreen.tooFew,
                gameScreen.tooMuch,
                gameScreen.finalSound);
        shapeRenderer = new ShapeRenderer();
        fd = new FeedbackDrawManager();
        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);
    }



}

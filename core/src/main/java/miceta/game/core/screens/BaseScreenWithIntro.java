package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.controllers.CvWithIntroController;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.miCeta;

/**
 * Created by ewe on 1/29/18.
 */
public class BaseScreenWithIntro extends BaseScreen {
    private static final String TAG = BaseScreenWithIntro.class.getName();
    private  boolean isInOgranicHelpScreen;

    public BaseScreenWithIntro(miCeta game) {
        this(game, false, false);
    }

    public BaseScreenWithIntro(miCeta game, boolean upLevel, boolean shouldRepeatTutorial) {
        this(game, upLevel,shouldRepeatTutorial, false);
    }

    public BaseScreenWithIntro(miCeta game, boolean upLevel, boolean shouldRepeatTutorial, boolean isInOgranicHelpScreen) {
        super(game, upLevel,shouldRepeatTutorial);
        this.isInOgranicHelpScreen = isInOgranicHelpScreen;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        worldController = new CvWithIntroController(game,stage,
                game.gameScreen.feedbackSoundType,
                game.gameScreen.intro,
                game.gameScreen.positives,
                game.gameScreen.tooFew,
                game.gameScreen.tooMuch,
                game.gameScreen.finalSound,
                upLevel,
                shouldRepeatTutorial,
                isInOgranicHelpScreen);
        shapeRenderer = new ShapeRenderer();
        fd = new FeedbackDrawManager();
        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);
    }



}

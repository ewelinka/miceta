package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.controllers.CvWithIntroControllerStepsTwo;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.ScreenName;

/**
 * Created by ewe on 3/23/18.
 */
public class BaseScreenWithIntroSteps extends BaseScreenWithIntro {
    public BaseScreenWithIntroSteps(miCeta game, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, upLevel, shouldRepeatTutorial, true);

    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        // if we come from the future, we need to adapt some params
        setGameScreenTo(ScreenName.GAME_STEPS); // we force organic tutorial audios
        LevelsManager.instance.forceLevelParams(2); // we force the operations from organic! // 0=headers, 1 = concrete, 2 = steps // TODO check if still needed!
        worldController = new CvWithIntroControllerStepsTwo(game,stage,
                gameScreen.feedbackSoundType,
                gameScreen.intro,
                gameScreen.positives,
                gameScreen.tooFew,
                gameScreen.tooMuch,
                gameScreen.finalSound,
                upLevel,
                shouldRepeatTutorial);
        shapeRenderer = new ShapeRenderer();
        fd = new FeedbackDrawManager();
        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);
    }


}

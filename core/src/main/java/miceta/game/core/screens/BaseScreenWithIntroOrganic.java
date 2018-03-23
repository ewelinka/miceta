package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.controllers.CvWithIntroController;
import miceta.game.core.controllers.CvWithIntroControllerOrganicPart2;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.ScreenName;

/**
 * Created by ewe on 3/23/18.
 */
public class BaseScreenWithIntroOrganic extends BaseScreenWithIntro {
    public BaseScreenWithIntroOrganic(miCeta game, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, upLevel, shouldRepeatTutorial, true);

    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        // if we come from the future, we need to adapt some params
        setGameScreenTo(ScreenName.ORGANIC_TUTORIAL1); // we force organic tutorial audios
        LevelsManager.instance.forceLevelParams(2); // we force the operations from organic! // 0=headers, 1 = concrete, 2 = organic
        worldController = new CvWithIntroControllerOrganicPart2(game,stage,
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
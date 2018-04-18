package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.controllers.CvWithIntroControllerReadNumber;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.ScreenName;

/**
 * Created by ewe on 4/3/18.
 */
public class BaseScreenWithIntroReadNumber extends BaseScreenWithIntro{
    ScreenName forceScreenName;
    public BaseScreenWithIntroReadNumber(miCeta game, boolean upLevel, boolean shouldRepeatTutorial, ScreenName forceScreenName) {
        super(game, upLevel, shouldRepeatTutorial, true);
        this.forceScreenName = forceScreenName;

    }

    @Override
    public void show() {
        initRenderRelatedStuff();
        // if we come from the future, we need to adapt some params
        setGameScreenTo(this.forceScreenName); // we force organic tutorial audios
        LevelsManager.instance.forceLevelParams(2); // we force the operations from organic! // 0=headers, 1 = concrete, 2 = steps
        worldController = new CvWithIntroControllerReadNumber(game,stage,
                game.gameScreen.screenName,
                game.gameScreen.intro,
                game.gameScreen.positives,
                game.gameScreen.tooFew,
                game.gameScreen.tooMuch,
                game.gameScreen.finalSound,
                upLevel,
                shouldRepeatTutorial);

    }


}

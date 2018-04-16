package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.Assets;
import miceta.game.core.controllers.CvWithIntroControllerStepsOne;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.ScreenName;

/**
 * Created by ewe on 2/2/18.
 */
public class StepsOneScreen extends BaseScreenWithIntro {
    private static final String TAG = StepsOneScreen.class.getName();

    public StepsOneScreen(miCeta game) {
        this(game, false, false);
    }

    public StepsOneScreen(miCeta game, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, upLevel, shouldRepeatTutorial);
    }

    @Override
    public void show() {
        initRenderRelatedStuff();
        worldController = new CvWithIntroControllerStepsOne(game,stage,
                FeedbackSoundType.STEP,
                Assets.instance.sounds.stepIntro,
                Assets.instance.sounds.positivesFeedbacks,
                Assets.instance.sounds.stepTooFew_1,
                Assets.instance.sounds.stepTooMuch_1,
                Assets.instance.sounds.tada,
                upLevel,
                shouldRepeatTutorial
        );
    }
}

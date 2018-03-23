package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.Assets;
import miceta.game.core.controllers.CvWithIntroControllerTutorial;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.ScreenName;

/**
 * Created by ewe on 2/2/18.
 */
public class OrganicOneScreen  extends BaseScreenWithIntro {
    private static final String TAG = OrganicOneScreen.class.getName();

    public OrganicOneScreen(miCeta game) {
        this(game, false, false);
    }

    public OrganicOneScreen(miCeta game, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, upLevel, shouldRepeatTutorial);
    }

    @Override
    public void show() {
        Gdx.app.log(TAG," we start the SHOW! "+Gdx.graphics.getWidth());
        stage = new Stage(new FitViewport(viewportWidth , viewportHeight));
        setGameScreenTo(ScreenName.ORGANIC_TUTORIAL1); // we force organic tutorial to execute stop_sounds() in correct way
        worldController = new CvWithIntroControllerTutorial(game,stage,
                FeedbackSoundType.KNOCK,
                Assets.instance.sounds.tmm1_intro,
                Assets.instance.sounds.positivesFeedbacks,
                Assets.instance.sounds.tmm1_tooFewOne,
                Assets.instance.sounds.tmm1_tooMuch_1,
                Assets.instance.sounds.tada,
                upLevel,
                shouldRepeatTutorial
        );
        shapeRenderer = new ShapeRenderer();
        fd = new FeedbackDrawManager();

        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);

    }
}

package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.Assets;
import miceta.game.core.controllers.CvWithIntroControllerConcrete;
import miceta.game.core.controllers.CvWithIntroControllerOrganicHelpOne;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.ScreenName;

/**
 * Created by ewe on 4/10/18.
 */
public class ConcreteTutorial extends BaseScreenWithIntro {
    private static final String TAG = ConcreteTutorial.class.getName();


    public ConcreteTutorial(miCeta game) {
        super(game, true, false);
    }

    public ConcreteTutorial(miCeta game, boolean upLevel, boolean shouldRepeat) {
        super(game, upLevel, shouldRepeat);
    }

    @Override
    public void show() {
        initRenderRelatedStuff();

        setGameScreenTo(ScreenName.CONCRETE_TUTORIAL); // we force organic tutorial to execute stop_sounds() in correct way
        worldController = new CvWithIntroControllerConcrete(game,stage,
                FeedbackSoundType.CLAP,
                Assets.instance.sounds.concreteIntro,
                Assets.instance.sounds.positivesOrganicHelp,
                Assets.instance.sounds.concreteTooFew,
                Assets.instance.sounds.concreteTooMuch,
                Assets.instance.sounds.concreteFinal,
                upLevel,
                shouldRepeatTutorial
        );
    }
}

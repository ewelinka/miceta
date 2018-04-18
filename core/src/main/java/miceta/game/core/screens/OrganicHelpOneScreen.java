package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.Assets;
import miceta.game.core.controllers.CvWithIntroControllerOrganicHelpOne;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.ScreenName;

/**
 * Created by ewe on 4/3/18.
 */
public class OrganicHelpOneScreen extends BaseScreenWithIntro {
    private static final String TAG = OrganicHelpOneScreen.class.getName();

    public OrganicHelpOneScreen(miCeta game) {
        this(game, false, false);
    }

    public OrganicHelpOneScreen(miCeta game, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, upLevel, shouldRepeatTutorial);
    }

    @Override
    public void show() {
        initRenderRelatedStuff();
        Gdx.app.log(TAG," we start the SHOW in ORGANIC! "+Gdx.graphics.getWidth());
        setGameScreenTo(ScreenName.ORGANIC_HELP); // we force organic tutorial to execute stop_sounds() in correct way
        worldController = new CvWithIntroControllerOrganicHelpOne(game,stage,
                ScreenName.ORGANIC_HELP,
                Assets.instance.sounds.organicHelpIntro,
                Assets.instance.sounds.positivesTada,
                Assets.instance.sounds.organicHelpTooFew_1,
                Assets.instance.sounds.organicHelpTooMuch_1,
                Assets.instance.sounds.organicHelpFinal,
                upLevel,
                shouldRepeatTutorial
        );
    }
}

package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.controllers.CvWithIntroController;
import miceta.game.core.controllers.CvWorldController;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.FeedbackSoundType;

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
        int representationInCurrentLevel = LevelsManager.instance.getRepresentation();
        Sound[] nowSounds = game.getRepresentationToScreenMapper().getSoundsFromRepresentation(representationInCurrentLevel);
        FeedbackSoundType nowFeedback = game.getRepresentationToScreenMapper().getFeedbackTypeFromRepresentation(representationInCurrentLevel);
        worldController = new CvWithIntroController(game,stage,nowFeedback, nowSounds[0],nowSounds[1],nowSounds[2], nowSounds[3],nowSounds[4]);
        shapeRenderer = new ShapeRenderer();
        fd = new FeedbackDrawManager();
        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);
    }



}

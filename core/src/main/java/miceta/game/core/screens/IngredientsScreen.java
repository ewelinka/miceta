package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.Assets;
import miceta.game.core.controllers.CvIngredientsController;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.FeedbackSoundType;

/**
 * Created by ewe on 1/18/18.
 */
public class IngredientsScreen extends BaseScreen {
    private static final String TAG = IngredientsScreen.class.getName();


    public IngredientsScreen(miCeta game, boolean upLevel) {
        super(game, upLevel);
    }

    @Override
    public void show() {

        Gdx.app.log(TAG," we start the SHOW! "+Gdx.graphics.getWidth());
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        worldController = new CvIngredientsController(game,stage, FeedbackSoundType.INGREDIENT,Assets.instance.sounds.ingredientsMore,Assets.instance.sounds.ingredientsLess,Assets.instance.sounds.ingredientsFinal, upLevel);
        shapeRenderer = new ShapeRenderer();
        fd = new FeedbackDrawManager();
        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);
    }
}

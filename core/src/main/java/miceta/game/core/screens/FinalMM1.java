package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.controllers.FinalController;
import miceta.game.core.miCeta;


/**
 * Created by ewe on 4/16/18.
 */
public class FinalMM1 extends AbstractGameScreen {
    FinalController finalController;

    public FinalMM1(miCeta game, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, upLevel, shouldRepeatTutorial);
    }

    @Override
    public void render(float deltaTime) {
        // if (!paused) {
        finalController.update(deltaTime);
        stage.act(deltaTime);
        // }

    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        finalController = new FinalController(game,stage,game.gameScreen.intro);

        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);

    }
}

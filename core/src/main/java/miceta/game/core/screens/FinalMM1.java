package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
        super(game, upLevel, shouldRepeatTutorial,false);
    }

    @Override
    public void render(float deltaTime) {
        // if (!paused) {
        finalController.update(deltaTime);
        stage.act(deltaTime);
        spriteBatch.begin();
        spriteBatch.draw(game.gameScreen.imgBackground,0,0);
        spriteBatch.end();
        // }

    }

    @Override
    public void show() {
        initRenderRelatedStuff();
        finalController = new FinalController(game,stage,game.gameScreen.intro);
    }
}

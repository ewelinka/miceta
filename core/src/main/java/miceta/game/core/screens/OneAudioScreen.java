package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import miceta.game.core.controllers.OneAudioController;
import miceta.game.core.miCeta;


/**
 * Created by ewe on 4/16/18.
 */
public class OneAudioScreen extends AbstractGameScreen {
    OneAudioController oneAudioController;

    public OneAudioScreen(miCeta game, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, upLevel, shouldRepeatTutorial,false);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // if (!paused) {
        oneAudioController.update(deltaTime);

        spriteBatch.begin();
        spriteBatch.draw(game.gameScreen.imgBackground,0,0);
        spriteBatch.end();

        stage.act(deltaTime);
        stage.draw();
        // }

    }

    @Override
    public void show() {
        initRenderRelatedStuff();
        oneAudioController = new OneAudioController(game,stage,game.gameScreen.intro);
    }
}

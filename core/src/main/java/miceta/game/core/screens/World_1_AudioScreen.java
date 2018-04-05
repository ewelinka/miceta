package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;

/**
 * Created by ewe on 1/9/18.
 */
public class World_1_AudioScreen extends AbstractGameScreen {
    private float timePassed;
    private float tutorialDuration;

    public World_1_AudioScreen(miCeta game) {
        this(game, false, false);
    }
    public World_1_AudioScreen(miCeta game, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, upLevel, shouldRepeatTutorial);
    }

    @Override
    public void render(float deltaTime) {

        timePassed+=deltaTime;
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();

        if(timePassed > tutorialDuration){
            game.setScreen(new World_1_Game(game, upLevel, shouldRepeatTutorial));
        }
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        Gdx.input.setCatchBackKey(false);
        timePassed = 0;
        AudioManager.instance.setStage(stage);
        if(shouldRepeatTutorial)
            tutorialDuration = AudioManager.instance.reproduce_Game_1(0, 1);
        else
            tutorialDuration = AudioManager.instance.reproduce_Game_1(1, 1);


    }

}
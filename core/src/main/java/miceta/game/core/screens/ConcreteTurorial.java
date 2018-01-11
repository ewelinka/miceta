package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;

import java.util.ArrayList;

/**
 * Created by ewe on 1/9/18.
 */
public class ConcreteTurorial extends AbstractGameScreen {
    private float timePassed;
    private float tutorialDuration;
    private int part = 0;
    private int t_part = 0;
    private int t_aux_number =0;

    public ConcreteTurorial(miCeta game, int part, int aux_number) {

        super(game);
        t_part = part;
        t_aux_number =  aux_number;

    }

    @Override
    public void render(float deltaTime) {
        timePassed+=deltaTime;
        Gdx.gl.glClearColor(1, 1, 1, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();
        if(timePassed > tutorialDuration){
           // game.setScreen(new FeedbackScreen(game));
            game.setScreen(new TutorialScreen(game));
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth , viewportHeight));
        Gdx.input.setCatchBackKey(false);
        timePassed = 0;
        AudioManager.instance.setStage(stage);

        if (t_part == 0){
            tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(0,5);
        }
        else if (t_part == 1){

          ArrayList<Integer>  nowDetected = new ArrayList<>();
          nowDetected.add((t_aux_number));
          AudioManager.instance.readBlocks(nowDetected, 0);
            tutorialDuration = 3; //calcular bien esto

        }

    }

    @Override
    public void hide() {
        stage.dispose();

    }

    @Override
    public void pause() {

    }

    public void up_part() {
        part = part+1;
    }


    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }
}
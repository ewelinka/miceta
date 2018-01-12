package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ewe on 1/9/18.
 */
public class ConcreteTurorial extends AbstractGameScreen {
    private float timePassed;
    private float tutorialDuration;
    private int part = 0;
    private int t_part = 0;
    private int t_aux_number =0;
    private int knock_counter =0;

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

        if((t_part==0) && (timePassed > tutorialDuration)){
           // game.setScreen(new FeedbackScreen(game));
            game.setScreen(new TutorialScreen(game));
        }
        else  if((t_part==1) && (timePassed > tutorialDuration)){

            timePassed =0;
            tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(7,7);
            t_part = 3;
        }
        else if((t_part==3) && (timePassed > tutorialDuration)){

            ArrayList<Integer>  nowDetected = new ArrayList<>();
            nowDetected.add((t_aux_number));
            AudioManager.instance.readBlocks(nowDetected, 0);

            timePassed = 0;
            tutorialDuration =0;
            for (int i = 0; i < nowDetected.size(); i++) {
                tutorialDuration = tutorialDuration + nowDetected.get(i);
            }
            t_part = 4;
        }
        else if((t_part==4) && (timePassed > tutorialDuration)){

            timePassed = 0;
            tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(8,8);
            t_part = 5;
        }
        else if((t_part==5) && (timePassed > tutorialDuration)){

            ArrayList<Integer>  nowDetected = new ArrayList<>();
            nowDetected.add((t_aux_number));
            nowDetected.add((4));
            AudioManager.instance.readBlocks(nowDetected, 0);

            timePassed = 0;
            tutorialDuration =0;
            for (int i = 0; i < nowDetected.size(); i++) {
                tutorialDuration = tutorialDuration + nowDetected.get(i);
            }
            t_part = 6;
        }

        else if((t_part==6) && (timePassed > tutorialDuration)){
            timePassed = 0;
            tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(9,9);
            t_part = 7;
        }
        else if((t_part==7) && (timePassed > tutorialDuration && ( knock_counter < t_aux_number))){

            game.setScreen(new TestScreen(game));

            // game.setScreen(new TutorialScreen(game));
           // knock_counter ++;
            //timePassed = 0;
            //tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(10,10);
            //tutorialDuration = 0.5f;

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
            tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(0,4);
        }

        else if (t_part == 1){

          ArrayList<Integer>  nowDetected = new ArrayList<>();
          nowDetected.add((t_aux_number));
          AudioManager.instance.readBlocks(nowDetected, 0);

          tutorialDuration =0;
          for (int i = 0; i < nowDetected.size(); i++) {
              tutorialDuration = tutorialDuration + nowDetected.get(i);
          }


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
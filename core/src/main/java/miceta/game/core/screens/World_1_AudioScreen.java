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
public class World_1_AudioScreen extends AbstractGameScreen {
    private float timePassed;
    private float tutorialDuration;
    private int part = 0;
    private int t_part = 0;
    private int t_aux_number =0;
    private int knock_counter =0;
    private int game_number =0;
    private ArrayList<Integer>  nowDetected = new ArrayList<>();


    public World_1_AudioScreen(miCeta game, int part, int aux_number, int g_number) {
        super(game);
        t_part = part;
        t_aux_number =  aux_number;
        game_number = g_number;
    }

    @Override
    public void render(float deltaTime) {
        timePassed+=deltaTime;
        Gdx.gl.glClearColor(1, 1, 1, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();

        if((game_number ==1) && (t_part==0) && (timePassed > tutorialDuration)){
            game.setScreen(new World_1_Game(game,1));
        }
        else
        if((game_number ==5) && (t_part==0) && (timePassed > tutorialDuration)){
            game.setScreen(new World_1_Game(game,5));
        }
    }


    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth , viewportHeight));
        Gdx.input.setCatchBackKey(false);
        timePassed = 0;
        AudioManager.instance.setStage(stage);

        if ((t_part == 0) && (game_number == 1)){
            tutorialDuration = AudioManager.instance.reproduce_Game_1(0,0);
        }
        else
        if ((t_part == 0) && (game_number == 5)){
            tutorialDuration = AudioManager.instance.reproduce_Game_5(0,0);
        }
    }


    public void up_part() {
        part = part+1;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }
}
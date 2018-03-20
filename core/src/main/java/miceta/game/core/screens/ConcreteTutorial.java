package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;
import miceta.game.core.util.FeedbackSoundType;

import java.util.ArrayList;

/**
 * Created by ewe on 1/9/18.
 */
public class ConcreteTutorial extends AbstractGameScreen {
    private float _timePassed;
    private float _tutorialDuration;
    private int _tutorialPart = 0;
    private int _tutorialAuxNumber =0;
    private int _loopCounter = 0;


    public ConcreteTutorial(miCeta game, int part, int aux_number, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, upLevel, shouldRepeatTutorial);
        _tutorialPart = part;
        _tutorialAuxNumber =  aux_number;
    }

    @Override
    public void render(float deltaTime) {
        _timePassed += deltaTime;
        Gdx.gl.glClearColor(1, 1, 1, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();

        int _knockCounter = 0;
        if ((_tutorialPart == 0) && (_timePassed > _tutorialDuration)) {
            // game.setScreen(new FeedbackScreen(game));
            game.setScreen(new TutorialScreen(game, upLevel, false));
        } else if ((_tutorialPart == 1) && (_timePassed > _tutorialDuration)) {

            _timePassed = 0;
            _tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(7, 7);
            _tutorialPart = 3;
        } else if ((_tutorialPart == 3) && (_timePassed > _tutorialDuration)) {

            //busco que se repita.
            _loopCounter ++;
            if (_loopCounter == 3){
                _tutorialPart =4;
                _loopCounter =0;
            }
            reproduceBlocks(false, true);
           // _tutorialPart = 4;
        } else if ((_tutorialPart == 4) && (_timePassed > _tutorialDuration)) {


            _timePassed = 0;
            _tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(8, 8);
            _tutorialPart = 5;
        } else if ((_tutorialPart == 5) && (_timePassed > _tutorialDuration)) {

            _loopCounter ++;
            if (_loopCounter == 2){
                _tutorialPart =6;
                _loopCounter =0;
            }
            reproduceBlocks(true, true);
            //_tutorialPart = 6;
        } else if ((_tutorialPart == 6) && (_timePassed > _tutorialDuration)) {

            _timePassed = 0;
            _tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(9, 9);
            _tutorialPart = 7;
        } else if ((_tutorialPart == 7) && (_timePassed > _tutorialDuration && (_knockCounter < _tutorialAuxNumber))) {
            //game.goToNextScreen();
            reproduceBlocks(false, true);
            AudioManager.instance.readNumberWithFeedback(_tutorialAuxNumber, 0.3f);
            _tutorialPart =8;

            //tutorialDuration =t_aux_number* Constants.READ_ONE_UNIT_DURATION + Constants.WAIT_AFTER_KNOCK + t_aux_number * 0.3f;

        } else if ((_tutorialPart == 8) && (_timePassed > _tutorialDuration && (_knockCounter <_tutorialAuxNumber))) {

            _timePassed = 0;
            _tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(10, 10);
            _tutorialPart =9;
        }
        else if ((_tutorialPart == 9) && (_timePassed > _tutorialDuration && (_knockCounter < _tutorialAuxNumber))) {

            AudioManager.instance.setFeedbackSoundType(FeedbackSoundType.BELL);
            reproduceBlocks(false, true);
            AudioManager.instance.readNumberWithFeedback(_tutorialAuxNumber, 0.3f);
            _tutorialDuration =_tutorialAuxNumber* Constants.READ_ONE_UNIT_DURATION + Constants.WAIT_AFTER_KNOCK + _tutorialAuxNumber * 0.3f;
            _timePassed = 0;
            _tutorialPart =10;
        }
        else if ((_tutorialPart == 10) && (_timePassed > _tutorialDuration)){

            if (upLevel){
                game.goToNextScreen();
            }
            else{
                game.goToLastScreen(false);
            }
        }
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth , viewportHeight));
        Gdx.input.setCatchBackKey(false);
        _timePassed = 0;
        AudioManager.instance.setStage(stage);

        if (_tutorialPart == 0){
            _tutorialDuration = AudioManager.instance.reproduce_concrete_tutorial(0,4);
        }

        else if (_tutorialPart == 1){
            reproduceBlocks(false, false);
        }
    }


    private void reproduceBlocks(boolean add_adicional, boolean isFeedBackWithNumber){

        ArrayList<Integer>  nowDetected = new ArrayList<>();
        nowDetected.add((_tutorialAuxNumber));

        if(add_adicional){
            nowDetected.add((2));
        }

        if (isFeedBackWithNumber) {
            AudioManager.instance.readNumberWithMagicFeedback(nowDetected, 0.3f);
        }
        else{
            AudioManager.instance.readBlocks(nowDetected, 0);
        }
//-
        _timePassed = 0;
        _tutorialDuration =0;
        for (Integer aNowDetected : nowDetected) {
            _tutorialDuration = _tutorialDuration + aNowDetected;
        }

    }


}
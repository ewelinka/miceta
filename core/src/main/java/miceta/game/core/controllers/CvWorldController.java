package miceta.game.core.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;

import edu.ceta.vision.core.blocks.Block;

import edu.ceta.vision.core.topcode.TopCodeDetectorDesktop;
import miceta.game.core.Assets;
import miceta.game.core.managers.CvBlocksManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.Constants;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;


import java.util.ArrayList;
import java.util.Set;

/**
 * Created by ewe on 8/10/17.
 */
public class CvWorldController extends InputAdapter {
    private static final String TAG = CvWorldController.class.getName();
    protected Stage stage;
    public miCeta game;
    private int randomNumber,previousRandomNumber;
    private boolean lastAnswerRight;
    private int error_min = 0;
    private int error_max = 0;
    private int counter =0;
    private int feedback_delay=0;
    private float timeToWait, timePassed;


    protected CvBlocksManager cvBlocksManager;

    public CvWorldController(miCeta game, Stage stage){
        this.game = game;
        this.stage = stage;
        cvBlocksManager = new CvBlocksManager(game,stage);
        init();

    }

    private void init(){
        Gdx.app.log(TAG,"init in the cv blocks manager");
        timePassed = 0;
        randomNumber = getNewNumber();

        AudioManager.instance.setStage(stage); // we set current Stage in AudioManager, if not "reader" actor doesn't work
        AudioManager.instance.readFeedback(randomNumber); //first we read the random number
        timeToWait = randomNumber + Constants.WAIT_AFTER_KNOCK; // time we should wait before next loop starts
        lastAnswerRight = false;
    }

    private void updateAndroid(){
        if (game.hasNewFrame()) {
            cvBlocksManager.updateDetected(); // to get detected blocks
        }
        if (cvBlocksManager.isDetectionReady()) {
            cvBlocksManager.analyseDetected(); // to analyse the blocks (=get blocks values)
        }
    }

    private void updatePC(){
        if(!((TopCodeDetectorDesktop)cvBlocksManager.getTopCodeDetector()).isBusy()){ //ask before in order to not accumulate new threads.
            cvBlocksManager.updateDetected();

            if(cvBlocksManager.isDetectionReady()){
                cvBlocksManager.analyseDetected();
            }
        }
    }


    public void update(float deltaTime) {
        timePassed+=deltaTime; // variable used to check in isTimeToStartNewLoop() to decide if new feedback loop should be started
        if((Gdx.app.getType() == Application.ApplicationType.Android)){
            updateAndroid();
        }else{
            updatePC();
        }

        if(isTimeToStartNewLoop()){
            Gdx.app.log(TAG,"new loop! with random number "+randomNumber);
            if(lastAnswerRight){ // if las answer was correct, we get new random number
                previousRandomNumber = randomNumber;
                randomNumber = getNewNumber();
                timeToWait = Constants.READ_NUMBER_DURATION + randomNumber*Constants.READ_ONE_UNIT_DURATION + Constants.WAIT_AFTER_KNOCK; // one extra second to read number,feedback and yuju
                timePassed = 0; // start to count the time
                AudioManager.instance.readFeedback(randomNumber);
                lastAnswerRight = false;

            }else { // if last answer was wrong we check the detected values and read feedback and read blocks detected

                counter ++;
                ArrayList<Integer> nowDetected = cvBlocksManager.getNewDetectedVals(); // to know the blocks on the table
                int sum = 0;
                for (int i = 0; i < nowDetected.size(); i++)
                    sum += nowDetected.get(i); // we need to know the sum to decide if response is correct

                if (sum > randomNumber) { //check how long to wait (biggest number between sum of blocks and random number)
                    error_max++;
                    error_min =0;
                    counter =0;

                    //timeToWait += Constants.FEEDBACK_DELAY;
                    feedback_delay= Constants.FEEDBACK_DELAY;

                    timeToWait = Constants.READ_NUMBER_DURATION + sum*Constants.READ_ONE_UNIT_DURATION + feedback_delay ;
                    feedback_delay =0;
                } else {

                    if ((sum>0)&&(sum < randomNumber)) { //check how long to wait (biggest number between sum of blocks and random number)
                        error_min++;
                        error_max =0;
                        counter =0;
                        feedback_delay= Constants.FEEDBACK_DELAY;
                    }

                    timeToWait = Constants.READ_NUMBER_DURATION + randomNumber*Constants.READ_ONE_UNIT_DURATION +feedback_delay;
                    feedback_delay =0;


                }//cuidado con este else

                if(sum == randomNumber){ // correct answer! in next loop we will celebrate
                    lastAnswerRight = true;
                    timeToWait += Constants.DELAY_FOR_YUJU;
                    error_min=0;
                    error_max=0;
                    counter =0;
                }else{

                    timeToWait += Constants.WAIT_AFTER_KNOCK; // we add extra time to wait after feedback reading

                    if ((error_max >= Constants.ERRORS_FOT_HINT) ) {



                        AudioManager.instance.playQuitOrAddBlock(0);
                        Gdx.app.log(TAG,"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%ERROR MAX ");
                        error_max =Constants.ERRORS_FOT_HINT-1;
                        error_min =Constants.ERRORS_FOT_HINT-1;
                       // timeToWait = timeToWait +2;

                       //if (error_max >= Constants.ERRORS_FOT_HINT){
                            counter =0;
                            //timeToWait = timeToWait +2;

//                        }
                    }
                    if ((error_min >= Constants.ERRORS_FOT_HINT)||(counter >= Constants.INACTIVITY_LIMIT))
                        {

                        AudioManager.instance.playQuitOrAddBlock(1);

                        Gdx.app.log(TAG,"########################################ERROR MIN ");
                        error_min =Constants.ERRORS_FOT_HINT-1;
                        error_max=Constants.ERRORS_FOT_HINT-1;

                            if (error_min >= Constants.ERRORS_FOT_HINT){
                            counter =0;


                            }
                            //timeToWait = timeToWait +2;

                        }
                }
                timePassed = 0;

                if(lastAnswerRight)
                    Gdx.app.log(TAG," RIGHT "+sum+ " "+randomNumber+" "+timeToWait);
                AudioManager.instance.readAllFeedbacks(nowDetected, randomNumber, lastAnswerRight);
                //delay(1);

                //AudioManager.instance.playQuitOrAddBlock(1);
            }

        }
    }


    private int getNewNumber(){
        int candidate = MathUtils.random(1,5);
        if(candidate == previousRandomNumber)
            candidate = (candidate+1)%6;
        if(candidate == 0) candidate = 1;

        return candidate;
    }

    private boolean isTimeToStartNewLoop(){
        //Gdx.app.log(TAG,"NEW LOOOOOOOP "+timePassed+" "+timeToWait+" "+(timePassed > timeToWait));
        return (timePassed > timeToWait );
    }

    public Set<Block> getCurrentBlocksFromManager(){
        return cvBlocksManager.getCurrentBlocks();
    }

    public int getRandomNumber(){
        return randomNumber;
    }


}

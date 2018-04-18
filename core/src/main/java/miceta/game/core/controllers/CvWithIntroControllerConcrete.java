package miceta.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.FeedbackSoundType;
import miceta.game.core.util.ScreenName;

import java.util.ArrayList;

/**
 * Created by ewe on 4/10/18.
 */
public class CvWithIntroControllerConcrete extends CvWorldController {
    private static final String TAG = CvWithIntroControllerConcrete.class.getName();
    private boolean firstLoop;
    public CvWithIntroControllerConcrete(miCeta game, Stage stage, ScreenName screenNameNow, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, stage, screenNameNow, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial);
    }


    @Override
    protected void init(){
        numberToPlay = 0;
        setDelayForPositiveFeedback();
        timeToWait = AudioManager.instance.reproduceIntro();
        answerRight = false;
        firstLoop = true;
    }

    @Override
    public void update(float deltaTime) {
        timePassed+=deltaTime; // variable used to check in isTimeToStartNewLoop() to decide if new feedback loop should be started
        inactivityTime+=deltaTime;
        updateCV();


        if(isTimeToStartNewLoop()){
            if(firstLoop) {
                inactivityTime = 0;
                firstLoop = false;
            }
            Gdx.app.log(TAG,"isTimeToStartNewLoop and willGoToNextPart "+willGoToNextPart);
            if(!willGoToNextPart) {
                timePassed = 0; // start to count the time
                ArrayList<Integer> nowDetected = cvBlocksManager.getNewDetectedVals(); // to know the blocks on the table
                if(nowDetected.size() > 0){
                    Gdx.app.log(TAG,"we have block! ");
                    timeToWait = AudioManager.instance.reproduceFinal()+ 5;
                    willGoToNextPart = true;
                }
                else {
                    if(inactivityTime > 8){
                        this.tooFewErrorSound.play(1.0f);
                        timeToWait = 1.5f;
                        inactivityTime = 0;
                    }
                    else
                        timeToWait = 1.0f;
                }
            }else{
                goToNextLevel();

            }
        }
    }

}

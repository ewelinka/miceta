package miceta.game.core.controllers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.Assets;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.FeedbackSoundType;

import static miceta.game.core.util.CommonFeedbacks.INTRO;

/**
 * Created by ewe on 4/16/18.
 */
public class OneAudioController {
    miCeta game;
    float timeToWait;
    float timePassed;

    public OneAudioController(miCeta game, Stage stage, Sound introSound){
        this.game  = game;
        timePassed = 0;
        AudioManager.instance.setStage(stage);
        AudioManager.instance.setCustomSound(introSound, INTRO);
        timeToWait = AudioManager.instance.reproduceIntro();

    }

    public void update(float deltaTime) {
        timePassed += deltaTime;
        if(timePassed > timeToWait ){
            game.goToNextScreen();
        }
    }
}

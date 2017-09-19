package miceta.game.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import miceta.game.core.Assets;


import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

/**
 * Created by ewe on 8/10/17.
 */
public class AudioManager {
    public static final String TAG = AudioManager.class.getName();

    public static final AudioManager instance = new AudioManager();
    private Music playingMusic;
    private Sound currentSound;
    private SequenceAction readFeedback, readBlocks, readCorrectSolution;
    private float defaultVolSound = 1.0f;
    private Actor reader;
    private Stage stage;
    private float readBlockDuration = 0.5f;

    private AudioManager () { }

    public void setStage(Stage stage){
        Gdx.app.log(TAG,"set stage in AM");
        this.stage = stage;
        reader = new Actor(); // ractor that reads everything
        stage.addActor(reader);
        readFeedback = new SequenceAction(); // for feedback reading
        readBlocks = new SequenceAction(); // for detected blocks reading
        readCorrectSolution = new SequenceAction(); // for correct number and yuju
    }

    public void play (Sound sound) {
        play(sound, 1);
    }
    public void play (Sound sound, float volume) {
        play(sound, volume, 1);
    }
    public void play (Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    }
    public void play (Sound sound, float volume, float pitch, float pan) {
        currentSound = sound;
        currentSound.play(defaultVolSound * volume, pitch, pan);
    }

    public void play (Music music) {
        stopMusic();
        playingMusic = music;

        music.setLooping(true);
        music.setVolume(0.05f);
        music.play();

    }
    public void stopMusic () {
        if (playingMusic != null) playingMusic.stop();
    }

    public void playWithoutInterruption(Sound sound) {
        sound.play(defaultVolSound , 1, 1);
    }

    public void playNumber (int nr) {
        final Sound whichSound;
        switch(nr) {
            case 1:
                whichSound = Assets.instance.sounds.f1;
                break;
            case 2:
                whichSound = Assets.instance.sounds.f2;
                break;
            case 3:
                whichSound = Assets.instance.sounds.f3;
                break;
            case 4:
                whichSound = Assets.instance.sounds.f4;
                break;
            case 5:
                whichSound = Assets.instance.sounds.f5;
                break;
            case 6:
                whichSound = Assets.instance.sounds.f6;
                break;
            case 7:
                whichSound = Assets.instance.sounds.f7;
                break;
            case 8:
                whichSound = Assets.instance.sounds.f8;
                break;
            case 9:
                whichSound = Assets.instance.sounds.f9;
                break;
            case 10:
                whichSound = Assets.instance.sounds.f10;
                break;
            case 11:
                whichSound = Assets.instance.sounds.f11;
                break;
            case 12:
                whichSound = Assets.instance.sounds.f12;
                break;
            case 13:
                whichSound = Assets.instance.sounds.f13;
                break;
            case 14:
                whichSound = Assets.instance.sounds.f14;
                break;
            case 15:
                whichSound = Assets.instance.sounds.f15;
                break;
            default:
                whichSound = Assets.instance.sounds.f15; // should never be used because we count to 15 max
                break;
        }
        //playWithoutInterruption(whichSound);
        readCorrectSolution.reset();
        readCorrectSolution.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(whichSound);
            }
        }));
        readCorrectSolution.addAction(delay(readBlockDuration));
        readCorrectSolution.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(Assets.instance.sounds.yuju);
            }
        }));
        reader.addAction(readCorrectSolution);

    }

    public void addToReadBlock (int nr) {
        final Sound whichSound;
        switch (nr) {
            case 1:
                whichSound = Assets.instance.sounds.oneDo;
                break;
            case 2:
                whichSound = Assets.instance.sounds.oneRe;
                break;
            case 3:
                whichSound = Assets.instance.sounds.oneMi;
                break;
            case 4:
                whichSound = Assets.instance.sounds.oneFa;
                break;
            case 5:
                whichSound = Assets.instance.sounds.oneSol;
                break;
            default:
                whichSound = Assets.instance.sounds.oneSol; // TODO change the default to hmmm nothing?
                break;

        }
        readBlocks.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(whichSound);
            }
        }));
        readBlocks.addAction(delay(readBlockDuration)); // we wait 0.5s because sound files with "do", "re" and "mi" have 0.5s
    }

    public void readSingleKnock(int whichKnock){
        final Sound whichSound;
        switch(whichKnock) {
            case 1:
                whichSound = Assets.instance.sounds.d1;
                break;
            case 2:
                whichSound = Assets.instance.sounds.d2;
                break;
            case 3:
                whichSound = Assets.instance.sounds.d3;
                break;
            case 4:
                whichSound = Assets.instance.sounds.d4;
                break;
            case 5:
                whichSound = Assets.instance.sounds.d5;
                break;
            case 6:
                whichSound = Assets.instance.sounds.d6;
                break;
            case 7:
                whichSound = Assets.instance.sounds.d7;
                break;
            case 8:
                whichSound = Assets.instance.sounds.d8;
                break;
            case 9:
                whichSound = Assets.instance.sounds.d9;
                break;
            case 10:
                whichSound = Assets.instance.sounds.d10;
                break;
            default:
                whichSound = Assets.instance.sounds.d1;
                break;

        }
        readFeedback.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(whichSound);
            }
        }));
        readFeedback.addAction(delay(readBlockDuration));

    }

    public void addToReadFeedback (int nr) {
        for(int i = 0; i<nr;i++){ // if the number is 5 we have to knock 5 times
            readFeedback.addAction(run(new Runnable() {
                public void run() {
                    playWithoutInterruption(Assets.instance.sounds.puck);
                }
            }));
            readFeedback.addAction(delay(readBlockDuration));
        }
    }

    public void addToReadFeedbackInSpace (int nr) {
        for(int i = 0; i<nr;i++){ // if the number is 5 we have to knock 5 times
            readSingleKnock(i+1); // we start with 1
        }
    }

    public void readFeedbackAndBlocks(ArrayList<Integer> toReadNums, int numToBuild){
        readBlocks.reset();
        readBlocks.addAction(delay(0.2f));
        for(int i = 0; i<toReadNums.size();i++) { // if we have detected block 3 and block 2, we have to read 3 times "mi" and 2 time "re"
            int val = toReadNums.get(i); // val will be 3 and than 2
            for(int j = 0; j<val;j++) {
                addToReadBlock(val); // one single lecture
            }
        }

        readFeedback.reset();
        readFeedback.addAction(delay(0.2f));
        addToReadFeedbackInSpace(numToBuild); // to generate feedback


        reader.addAction(parallel(readBlocks,readFeedback)); // we read feedback and the blocks in parallel

    }

    public void readFeedback( int numToBuild, int delayForNumberAndYuju){ // we use this action at the beginning of new screen, we read feedback without blocks
        Gdx.app.log(TAG,"readFeedback "+numToBuild);
        readFeedback.reset();
        readFeedback.addAction(delay(delayForNumberAndYuju));
        addToReadFeedbackInSpace(numToBuild);
        reader.addAction(readFeedback);
    }
}

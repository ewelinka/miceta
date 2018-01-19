package miceta.game.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.ArrayMap;

import miceta.game.core.Assets;
import miceta.game.core.controllers.CvWorldController;
import miceta.game.core.managers.CvBlocksManager;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by ewe on 8/10/17.
 */
public class AudioManager {
    public static final String TAG = AudioManager.class.getName();

    public static final AudioManager instance = new AudioManager();
    private Music playingMusic;
    private Sound currentSound;
    private SequenceAction readFeedbackAction, readBlocksAction, readCorrectSolutionAction, readTutorialAction;
    private float defaultVolSound = 0.5f;
    private float firstNoteVol = 1.0f;
    private Actor reader;
    private Stage stage;
    private float readBlockDuration = Constants.READ_ONE_UNIT_DURATION;
    private int feedback_delay=0;
    private boolean delay_quit = false;
    private boolean delay_add = false;
    private boolean newblock_loop = false;
    private Sound   nb_sound = Assets.instance.sounds.newblock;
    private Music  nb_sound_loop = Assets.instance.music.new_block_loop;
    private FeedbackSoundType feedbackSoundType;
    private Sound tooMuchErrorSound, tooFewErrorSound;

//    public static enum FeedbackType {
//        KNOCK, DROP
//    }

    private AudioManager () { }

    public void setStage(Stage stage){
        this.stage = stage;
        reader = new Actor(); // ractor that reads everything
        stage.addActor(reader);
        readFeedbackAction = new SequenceAction(); // for feedback reading
        readBlocksAction = new SequenceAction(); // for detected blocks reading
        readCorrectSolutionAction = new SequenceAction(); // for correct number and yuju
        readTutorialAction = new SequenceAction();
    }

    public void play (Sound sound) {
        play(sound, 1);
    }
    public void play (Sound sound, float volume) {
        play(sound, volume, 1);
    } //pitch 1
    public void play (Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    } // pan 0
    public void play (Sound sound, float volume, float pitch, float pan) {
        currentSound = sound;
        currentSound.play(defaultVolSound * volume, pitch, pan);
    }

    public void play (Music music) {
        stopMusic();
        playingMusic = music;
        music.setLooping(true);
        music.setVolume(0.01f);
        music.play();


       //


     /*   if (newblock_loop) {
            Sound nb_sound;
            nb_sound = Assets.instance.sounds.newblock;
            nb_sound.play(defaultVolSound);
            nb_sound.loop();
        }*/
    }


    public void stopMusic () {
        if (playingMusic != null) playingMusic.stop();
    }

    public void playWithoutInterruption(Sound sound, boolean firstNote) {
        if(firstNote)
            sound.play(firstNoteVol);// be default vol = 1
        else
            sound.play(defaultVolSound);
    }

    public void playWithoutInterruption(Sound sound){
        playWithoutInterruption(sound, false);
    }

    public void playNewBlockSong_loop(){

        if (newblock_loop){
            nb_sound_loop.setLooping(true);
            nb_sound_loop.setVolume(0.1f);
            nb_sound_loop.play();
        }
        else{
            nb_sound_loop.pause();
            nb_sound_loop.stop();
        }
    }



    public void playNewBlockSong()  {


        nb_sound.play(defaultVolSound);
    }

    public void playQuitOrAddBlock(int i){
        final Sound soundToPlay;
        feedback_delay=Constants.FEEDBACK_DELAY;
        if (i == 0) {
            soundToPlay = tooMuchErrorSound;
        }
        else {
            soundToPlay = tooFewErrorSound;

        }
        soundToPlay.play(defaultVolSound);

    }

    public void setDelay_quit(Sound tooFewErrorSound){
        delay_quit= true;
    }

    public void setDelay_add(Sound tooMuchErrorSound){
        delay_add= true;
    }

    public void setCustomSound(Sound nowSound, TooMuchTooFew soundType){
        switch (soundType){
            case TOO_MUCH:
                this.tooMuchErrorSound = nowSound;
                break;
            case TOO_FEW:
                this.tooFewErrorSound = nowSound;
                break;

        }
    }

    public void setFeedbackSoundType(FeedbackSoundType soundName){
        this.feedbackSoundType = soundName;
    }


    public SequenceAction playNumber (int nr, SequenceAction readCorrectSolution) {
        final Sound whichSound;
        switch(nr) {
            case 1:
                whichSound = Assets.instance.sounds.number1;
                break;
            case 2:
                whichSound = Assets.instance.sounds.number2;
                break;
            case 3:
                whichSound = Assets.instance.sounds.number3;
                break;
            case 4:
                whichSound = Assets.instance.sounds.number4;
                break;
            case 5:
                whichSound = Assets.instance.sounds.number5;
                break;
            case 6:
                whichSound = Assets.instance.sounds.number6;
                break;
            case 7:
                whichSound = Assets.instance.sounds.number7;
                break;
            case 8:
                whichSound = Assets.instance.sounds.number8;
                break;
            case 9:
                whichSound = Assets.instance.sounds.number9;
                break;
            case 10:
                whichSound = Assets.instance.sounds.number10;
                break;
            case 11:
                whichSound = Assets.instance.sounds.number11;
                break;
            case 12:
                whichSound = Assets.instance.sounds.number12;
                break;
            case 13:
                whichSound = Assets.instance.sounds.number13;
                break;
            case 14:
                whichSound = Assets.instance.sounds.number14;
                break;
            case 15:
                whichSound = Assets.instance.sounds.number15;
                break;
            default:
                whichSound = Assets.instance.sounds.number15; // should never be used because we count to 15 max
                break;
        }

        readCorrectSolution.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(whichSound);
            }
        }));
        return readCorrectSolution;

    }

    public void addToReadBlock (int nr, SequenceAction readBlocks, final boolean firstNote, float extraDelayBetweenFeedback) {
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
                playWithoutInterruption(whichSound, firstNote);
            }
        }));


        readBlocks.addAction(delay(readBlockDuration + extraDelayBetweenFeedback )); // we wait Xs because sound files with "do", "re" and "mi" have X duration
    }



    private void readSingleFeedbackSound(int whichNr, SequenceAction readFeedback, float extraDelayBetweenFeedback){
        switch(this.feedbackSoundType){
            case KNOCK:
                readSingleKnock(whichNr, readFeedback,extraDelayBetweenFeedback);
                break;
            case DROP:
                readSingleDrop(whichNr, readFeedback,extraDelayBetweenFeedback);
                break;
            default:
                readSingleKnock(whichNr, readFeedback,extraDelayBetweenFeedback);
                break;
        }
    }
    private void readSingleDrop(int whichKnock, SequenceAction readFeedback, float extraDelayBetweenFeedback) {
        readFeedback.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(Assets.instance.sounds.puck); // TODO change when we have drop sound
            }
        }));

        readFeedback.addAction(delay(readBlockDuration + extraDelayBetweenFeedback));


    }
    private void readSingleKnock(int whichKnock, SequenceAction readFeedback, float extraDelayBetweenFeedback){
        readFeedback.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(Assets.instance.sounds.knock); // TODO change when we have drop sound
            }
        }));

        readFeedback.addAction(delay(readBlockDuration + extraDelayBetweenFeedback)); // we wait Xs because sound files with "knock" have X duration

    }



    public SequenceAction addToReadFeedbackInSpace (int nr, SequenceAction readFeedback, float extraDelayBetweenFeedback) {
        for(int i = 0; i<nr;i++){ // if the number is 5 we have to knock 5 times
            readSingleFeedbackSound(nr, readFeedback, extraDelayBetweenFeedback); // we start with 1
        }

        return readFeedback;
    }

    public void readAllFeedbacks(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){
            //readFeedbackAndBlocksAndYuju(toReadNums,numToBuild);
        readFeedbackAndBlocks(toReadNums, numToBuild, extraDelayBetweenFeedback);

    }

    public void readAllFeedbacksAndPositive(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){
        readFeedbackAndBlocksAndTadaAndYuju(toReadNums,numToBuild, extraDelayBetweenFeedback);
    }

    public void readAllFeedbacksAndPositiveWithNewIngredient(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback, final int ingredientIndex) {
        reader.clearActions();
        /////// blocks
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback);
        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);

        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(Assets.instance.sounds.ingredientsPositive); //after correct answer comes "yuju"
            }
        }));
        final Sound ingredientSound = getIngredientFromIndex(ingredientIndex);
        readFeedbackAction.addAction(delay(Assets.instance.getSoundDuration(Assets.instance.sounds.ingredientsPositive)));
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(ingredientSound); //after correct answer comes "yuju"
            }
        }));

        reader.addAction(parallel(readBlocksAction,readFeedbackAction)); // we read feedback and the blocks in parallel
    }

    public void readAllFeedbacksAndFinalIngredientsScreen(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback, final int ingredientIndex) {
        reader.clearActions();
        /////// blocks
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback);
        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);


        final Sound ingredientSound = getIngredientFromIndex(ingredientIndex);
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(ingredientSound); //after correct answer comes "yuju"
            }
        }));

        reader.addAction(parallel(readBlocksAction,readFeedbackAction)); // we read feedback and the blocks in parallel
    }

        private void readFeedbackAndBlocks(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){
        reader.clearActions();
        /////// blocks
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback);

        if(delay_add){
            readBlocksAction.addAction(run(new Runnable() {
                public void run() {
                    playQuitOrAddBlock(0);
                }
            }));
            delay_quit = false;
            delay_add = false;
        }

        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);
        if(delay_quit){
            readFeedbackAction.addAction(run(new Runnable() {
                public void run() {
                    playQuitOrAddBlock(1);
                }


            }));
            delay_quit = false;
            delay_add = false;
         }

        reader.addAction(parallel(readBlocksAction,readFeedbackAction)); // we read feedback and the blocks in parallel
    }

    private void readFeedbackAndBlocksAndYuju(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){ //
        reader.clearActions();
        /////// blocks
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback);
        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);

        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(Assets.instance.sounds.yuju); //after correct answer comes "yuju"
            }
        }));

        reader.addAction(parallel(readBlocksAction,readFeedbackAction)); // we read feedback and the blocks in parallel
    }

    private void readFeedbackAndBlocksAndTadaAndYuju(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){ //
        reader.clearActions();
        /////// blocks
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback);
        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);

        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(Assets.instance.sounds.tada); //after correct answer comes "yuju"
            }
        }));
        readFeedbackAction.addAction(delay(Constants.DELAY_FOR_TADA));
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(Assets.instance.sounds.yuju); //after correct answer comes "yuju"
            }
        }));

        reader.addAction(parallel(readBlocksAction,readFeedbackAction)); // we read feedback and the blocks in parallel
    }

    public void readFeedback( int numToBuild, float extraDelayBetweenFeedback){ // we use this action at the beginning of new screen, we read feedback without blocks
        reader.clearActions();
        readFeedbackAction.reset();
        // read knocks
        readFeedbackAction.addAction(delay(Constants.READ_ONE_UNIT_DURATION)); // wait a little bit at the beggining
        readFeedbackAction = addToReadFeedbackInSpace(numToBuild, readFeedbackAction,extraDelayBetweenFeedback);
        // first read with small delay at the beginning
        reader.addAction(readFeedbackAction);
    }

    public void readNumberAndFeedback( int numToBuild, float extraDelayBetweenFeedback){ // we use this action at the beginning of new screen, we read feedback without blocks
        reader.clearActions();
        readFeedbackAction.reset();
        // first read number then knocks
        readFeedbackAction.addAction(delay(Constants.READ_ONE_UNIT_DURATION)); // wait before start read feedback
        readFeedbackAction = playNumber(numToBuild,readFeedbackAction);
        readFeedbackAction.addAction(delay(Constants.READ_NUMBER_DURATION)); // wait to finish read the number
        readFeedbackAction = addToReadFeedbackInSpace(numToBuild, readFeedbackAction, extraDelayBetweenFeedback);
        // first read with small delay at the beginning
        reader.addAction(readFeedbackAction);
    }



    public void readBlocks(ArrayList<Integer> toReadNums, float extraDelayBetweenFeedback){
        reader.clearActions();
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback);
        reader.addAction(readBlocksAction);
    }

    private SequenceAction createReadBlocksAction(SequenceAction readBlocksAction, ArrayList<Integer> toReadNums, float extraDelayBetweenFeedback){
        readBlocksAction.reset();
        boolean firstNote;
        for(int i = 0; i<toReadNums.size();i++) { // if we have detected block 3 and block 2, we have to read 3 times "mi" and 2 time "re"
            int val = toReadNums.get(i); // val will be 3 and than 2
            firstNote = true; // first note should be lauder
            for(int j = 0; j<val;j++) {
                addToReadBlock(val, readBlocksAction, firstNote, extraDelayBetweenFeedback); // one single lecture
                firstNote = false;
            }
        }
        return readBlocksAction;
    }

    private SequenceAction createReadFeedbackAction(SequenceAction readFeedbackAction, int numToBuild, float extraDelayBetweenFeedback){
        readFeedbackAction.reset();
        readFeedbackAction = addToReadFeedbackInSpace(numToBuild, readFeedbackAction, extraDelayBetweenFeedback); // to generate feedback
        return readFeedbackAction;
    }

    public void setNewblock_loop(boolean aux){
        newblock_loop = aux;
    }

    public boolean getNewblock_loop(){
        return newblock_loop;

    }

    private float reproduceSoundsWithIndex(ArrayList<Sound> soundsToReproduce, int start, int end){
        float duration_total =  0;

        readTutorialAction.reset();
     //   for (int i = 0; i < SoundsToReproduce.size(); i++){
        for (int i = start; i <= end; i++){
            float soundDuration = Assets.instance.getSoundDuration(soundsToReproduce.get(i));
            duration_total = duration_total + soundDuration;
            final Sound singleSound = soundsToReproduce.get(i);

            readTutorialAction.addAction(run(new Runnable() {
                public void run() {
                    playWithoutInterruption(singleSound,true); // knocks with which volume??
                }
            }));
            readTutorialAction.addAction((delay(soundDuration)));
        }

        Gdx.app.log(TAG, "============================= " + duration_total);
        reader.addAction(readTutorialAction);
        return duration_total;
    }

    private float reproduceSounds(ArrayList<Sound> soundsToReproduce){
        float duration_total =  0;

        readTutorialAction.reset();
        for (int i = 0; i < soundsToReproduce.size(); i++){
            float duration_aux = Assets.instance.getSoundDuration(soundsToReproduce.get(i));
            duration_total = duration_total + duration_aux;
            final Sound singleSound = soundsToReproduce.get(i);

            readTutorialAction.addAction(run(new Runnable() {
                public void run() {
                    playWithoutInterruption(singleSound,true);
                }
            }));
            readTutorialAction.addAction((delay(duration_aux)));
        }

        Gdx.app.log(TAG, "============================= " + duration_total);
        reader.addAction(readTutorialAction);

        return duration_total;
    }

    public float reproduce_concrete_tutorial(int start, int end) {
        ArrayList<Sound> soundsToReproduce = new ArrayList<Sound>();

        soundsToReproduce.add(Assets.instance.sounds.ct_1);
        soundsToReproduce.add(Assets.instance.sounds.ct_2);
        soundsToReproduce.add(Assets.instance.sounds.newblock);
        soundsToReproduce.add(Assets.instance.sounds.ct_3);
        soundsToReproduce.add(Assets.instance.sounds.ct_4);
        soundsToReproduce.add(Assets.instance.sounds.ct_5);
        soundsToReproduce.add(Assets.instance.sounds.ct_6);
        soundsToReproduce.add(Assets.instance.sounds.ct_7);
        soundsToReproduce.add(Assets.instance.sounds.ct_8);
        soundsToReproduce.add(Assets.instance.sounds.ct_9);
        soundsToReproduce.add(Assets.instance.sounds.knock);
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, start, end);
    }


    public float reproduce_organic_tutorial1(int tutorialPart){
        ArrayList<Sound> soundsToReproduce = new ArrayList<Sound>();
        switch(tutorialPart){
            case 0:
                // we should repeat the intro because of errors
                // "Volvimos al pasado! Empecemos una vez más!"
                soundsToReproduce.add(Assets.instance.sounds.newblock); // TODO change this when we have audio file
                break;
            case 1:
                soundsToReproduce.add(Assets.instance.sounds.tmm1_1);
                soundsToReproduce.add(Assets.instance.sounds.tmm1_2);
                break;
            case 3:
                soundsToReproduce.add(Assets.instance.sounds.tada); // we finished!!
                break;

        }
        return AudioManager.instance.reproduceSounds(soundsToReproduce);
    }

    private Sound getIngredientFromIndex(int ingredientIndex){
        switch(ingredientIndex){
            case 1:
                return Assets.instance.sounds.ingredientsCat;
            case 2:
                return Assets.instance.sounds.ingredientsCow;
            case 3:
                return Assets.instance.sounds.ingredientsCrocodile;
            case 4:
                return Assets.instance.sounds.ingredientsFrog;
            case 5:
                return Assets.instance.sounds.ingredientsLama;
            case 6:
                return Assets.instance.sounds.ingredientsVinegar;
            case 7:
                return Assets.instance.sounds.ingredientsFinal;
            default:
                return Assets.instance.sounds.puck;
        }

    }

    public float reproduce_ingredients_intro(){
        ArrayList<Sound> soundsToReproduce = new ArrayList<Sound>();
        soundsToReproduce.add(Assets.instance.sounds.ingredientsIntro);
        soundsToReproduce.add(Assets.instance.sounds.ingredientsAnt);
        return AudioManager.instance.reproduceSounds(soundsToReproduce);
    }







}

package miceta.game.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import miceta.game.core.Assets;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by ewe on 8/10/17.
 */
public class AudioManager {
    private static final String TAG = AudioManager.class.getName();
    public static final AudioManager instance = new AudioManager();
    private Music playingMusic;
    private Sound currentSound;
    private SequenceAction readFeedbackAction, readBlocksAction, readTutorialAction;
    private final float defaultVolSound = 0.5f;
    private float feedbackVolSound = 0.5f;
    private float knockNoteVol = 0.5f;
    private Actor reader;
    private float readBlockDuration;
    private boolean delay_quit = false;
    private boolean delay_add = false;
    private boolean newblock_loop = false;
    private final Sound   nb_sound = Assets.instance.sounds.newblock;
    private final Music  nb_sound_loop = Assets.instance.music.new_block_loop;
    private FeedbackSoundType feedbackSoundType;
    private Sound tooMuchErrorSound, tooFewErrorSound,finalFeedback, introSound;
    private ArrayList<Sound> positiveFeedback;
    private int currentPositiveIndex;
    private Sound currentClue;
    private int lastClueIndex;


    private AudioManager () { }

    public void setStage(Stage stage){
        reader = new Actor(); // actor that reads everything
        stage.addActor(reader);
        readFeedbackAction = new SequenceAction(); // for feedback reading
        readBlocksAction = new SequenceAction(); // for detected blocks reading
        readTutorialAction = new SequenceAction();
        readBlockDuration = Constants.READ_ONE_UNIT_DURATION;
    }


    public void play (Sound sound) {
        play(sound, 1);
    }
    private void play(Sound sound, float volume) {
        play(sound, volume, 1);
    } //pitch 1
    private void play(Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    } // pan 0
    private void play(Sound sound, float volume, float pitch, float pan) {
        //currentSound = sound;
        sound.play(defaultVolSound * volume, pitch, pan);
    }

    public void play (Music music) {
        stopMusic();
        playingMusic = music;
        music.setLooping(true);
        music.setVolume(0.01f);
        music.play();

     /*   if (newblock_loop) {
            Sound nb_sound;
            nb_sound = Assets.instance.sounds.newblock;
            nb_sound.play(defaultVolSound);
            nb_sound.loop();
        }*/
    }


    private void stopMusic() {
        if (playingMusic != null) playingMusic.stop();
    }

    private void playWithoutInterruption(Sound sound, boolean firstNote, float volume) {
        if(firstNote)
            sound.play(volume+0.2f); // first note louder!
        else{
            sound.play(volume);
        }
    }


    public void upFeedbackVolSound(){
        if (feedbackVolSound < 1.0f)
            feedbackVolSound +=  0.1f;
        feedbackVolSound = (float)roundMe(feedbackVolSound);
    }

    public void downFeedbackVolSound(){
        if (feedbackVolSound > 0.2f)
            feedbackVolSound -= 0.1f;
        feedbackVolSound = (float)roundMe(feedbackVolSound);
    }


    public void upKnockNoteVol(){
        if (knockNoteVol < 1.0f)
            knockNoteVol += 0.1f;
        knockNoteVol = (float)roundMe(knockNoteVol);
    }

    public void downKnockNoteVol(){
        if (knockNoteVol > 0.2f)
            knockNoteVol -= 0.1f;
        knockNoteVol = (float)roundMe(knockNoteVol);
    }

    private double roundMe(float toRound){
        return Math.round(toRound*100.0)/100.0;
    }

    public void playWithoutInterruption(Sound sound){

        playWithoutInterruption(sound, false, defaultVolSound);
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

    private void playQuitOrAddBlock(int i){
        final Sound soundToPlay;
        if (i == 0) {
            soundToPlay = tooMuchErrorSound;
        }
        else {
            soundToPlay = tooFewErrorSound;

        }
        soundToPlay.play(defaultVolSound);

    }

    public void setDelay_quit(){
        delay_quit= true;
    }

    public void setDelay_add(){
        delay_add= true;
    }

    public void setCustomSound(Sound nowSound, CommonFeedbacks soundType){
        switch (soundType){
            case TOO_MUCH:
                this.tooMuchErrorSound = nowSound;
                break;
            case TOO_FEW:
                this.tooFewErrorSound = nowSound;
                break;
            case FINAL:
                this.finalFeedback = nowSound;
            case INTRO:
                this.introSound = nowSound;
                break;
        }
    }

    public void setCustomSoundArray(CommonFeedbacks soundType,  ArrayList<Sound> sounds){
        switch (soundType){
            case POSITIVE:
                this.positiveFeedback = sounds;
                currentPositiveIndex = 0;
                break;
        }
    }

    public void setFeedbackSoundTypeAndLastClueIndex(FeedbackSoundType soundName){
        lastClueIndex = -1;
        this.feedbackSoundType = soundName;
    }


    private SequenceAction playNumber(int nr, SequenceAction readCorrectSolution) {
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

    private void addToReadBlock(int magicSoundLevel, SequenceAction readBlocks, final boolean firstNote, float extraDelayBetweenFeedback) {
        final Sound whichSound;
        Gdx.app.log(TAG,magicSoundLevel+" -----");
        switch (magicSoundLevel) {
            case 1: // pronounced strong
                whichSound = Assets.instance.sounds.magicStrong;
                break;
            case 2: // pronounced middle
                whichSound = Assets.instance.sounds.magicMiddle;
                break;
            case 3: // normal
                whichSound = Assets.instance.sounds.magicNomal;
                break;
            default:
                whichSound = Assets.instance.sounds.magicNomal;
                break;

        }
        readBlocks.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(whichSound, firstNote, feedbackVolSound);
            }
        }));


        readBlocks.addAction(delay(readBlockDuration + extraDelayBetweenFeedback )); // we wait Xs because sound files with "do", "re" and "mi" have X duration
    }

    private void readSingleFeedbackSound(SequenceAction readFeedback, float extraDelayBetweenFeedback, final Sound clue){
        readFeedback.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(clue); // TODO change when we have drop sound
            }
        }));

        readFeedback.addAction(delay(readBlockDuration + extraDelayBetweenFeedback));
    }

    private Sound getRandomClue(){
        Sound clueSound = Assets.instance.sounds.puck;
        switch(this.feedbackSoundType){
            case KNOCK:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesKnock);
                break;
            case INGREDIENT:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesIngredients);
                break;
            case MIXING:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesMixing);
                break;
            case MUSIC:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesMusic);
                break;
            case GREETING:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesGreeting);
                break;
            case STEP:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesSteps);
                break;
            case CLAP:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesOrganicHelp);
                break;
        }
        return  clueSound;

    }

    private Sound getRandomClueFromAll(ArrayList<Sound> clues){
        Gdx.app.log(TAG," last clue idx "+lastClueIndex);
        int idx = MathUtils.random(0,clues.size()-1);
        if(clues.size()>1){
            while(lastClueIndex == idx){
                idx = MathUtils.random(0,clues.size()-1);
            }
            lastClueIndex = idx;
        }
        Gdx.app.log(TAG," idx "+idx);
        return clues.get(idx);
    }

    private void readSingleKnockWithNumber(final int number, final SequenceAction readFeedback, final float extraDelayBetweenFeedback){
        readFeedback.addAction(run(new Runnable() {
            public void run() {

                switch(number) {
                    case 1:
                        playWithoutInterruption(Assets.instance.sounds.number1);
                        break;
                    case 2:
                        playWithoutInterruption(Assets.instance.sounds.number2);
                        break;
                    case 3:
                        playWithoutInterruption(Assets.instance.sounds.number3);
                        break;
                    case 4:
                        playWithoutInterruption(Assets.instance.sounds.number4);
                        break;
                    case 5:
                        playWithoutInterruption(Assets.instance.sounds.number5);
                        break;
                    case 6:
                        playWithoutInterruption(Assets.instance.sounds.number6);
                        break;
                    case 7:
                        playWithoutInterruption(Assets.instance.sounds.number7);
                        break;
                    case 8:
                        playWithoutInterruption(Assets.instance.sounds.number8);
                        break;
                    case 9:
                        playWithoutInterruption(Assets.instance.sounds.number9);
                        break;
                    case 10:
                        playWithoutInterruption(Assets.instance.sounds.number10);
                        break;
                    case 11:
                        playWithoutInterruption(Assets.instance.sounds.number11);
                        break;
                    case 12:
                        playWithoutInterruption(Assets.instance.sounds.number12);
                        break;
                    case 13:
                        playWithoutInterruption(Assets.instance.sounds.number13);
                        break;
                    case 14:
                        playWithoutInterruption(Assets.instance.sounds.number14);
                        break;
                    case 15:
                        playWithoutInterruption(Assets.instance.sounds.number15);
                        break;
                }

               // Sound whichSound = Assets.instance.sounds.oneDo;

                playWithoutInterruption(Assets.instance.sounds.organicClap); // TODO change when we have drop sound

            }
        }));

        readFeedback.addAction(delay(readBlockDuration + extraDelayBetweenFeedback)); // we wait Xs because sound files with "knock" have X duration
    }



    private void readSingleFeedbackWithNumber(final int number, final SequenceAction readFeedback, final float extraDelayBetweenFeedback, final Sound feedbackSound){
        readFeedback.addAction(run(new Runnable() {
            public void run() {

                switch(number) {
                    case 1:
                        playWithoutInterruption(Assets.instance.sounds.number1);
                        break;
                    case 2:
                        playWithoutInterruption(Assets.instance.sounds.number2);
                        break;
                    case 3:
                        playWithoutInterruption(Assets.instance.sounds.number3);
                        break;
                    case 4:
                        playWithoutInterruption(Assets.instance.sounds.number4);
                        break;
                    case 5:
                        playWithoutInterruption(Assets.instance.sounds.number5);
                        break;
                    case 6:
                        playWithoutInterruption(Assets.instance.sounds.number6);
                        break;
                    case 7:
                        playWithoutInterruption(Assets.instance.sounds.number7);
                        break;
                    case 8:
                        playWithoutInterruption(Assets.instance.sounds.number8);
                        break;
                    case 9:
                        playWithoutInterruption(Assets.instance.sounds.number9);
                        break;
                    case 10:
                        playWithoutInterruption(Assets.instance.sounds.number10);
                        break;
                    case 11:
                        playWithoutInterruption(Assets.instance.sounds.number11);
                        break;
                    case 12:
                        playWithoutInterruption(Assets.instance.sounds.number12);
                        break;
                    case 13:
                        playWithoutInterruption(Assets.instance.sounds.number13);
                        break;
                    case 14:
                        playWithoutInterruption(Assets.instance.sounds.number14);
                        break;
                    case 15:
                        playWithoutInterruption(Assets.instance.sounds.number15);
                        break;
                }


                playWithoutInterruption(feedbackSound); // TODO change when we have drop sound

            }
        }));

        readFeedback.addAction(delay(readBlockDuration + extraDelayBetweenFeedback)); // we wait Xs because sound files with "knock" have X duration
    }




    private SequenceAction addToReadFeedbackInSpace(int nr, SequenceAction readFeedback, float extraDelayBetweenFeedback) {
        for(int i = 0; i<nr;i++){ // if the number is 5 we have to knock 5 times
            readSingleFeedbackSound(readFeedback, extraDelayBetweenFeedback, currentClue);
        }
        return readFeedback;
    }

    public void readAllFeedbacks(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){
        //readFeedbackAndBlocksAndYuju(toReadNums,numToBuild);
        readFeedbackAndBlocks(toReadNums, numToBuild, extraDelayBetweenFeedback);

    }

    public void readNumberAndAllFeedbacks(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){
        readNumberFeedbackAndBlocks(toReadNums, numToBuild, extraDelayBetweenFeedback);
    }

    public void readAllFeedbacksAndPositive(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){
        readFeedbackAndBlocksAndPositive(toReadNums,numToBuild, extraDelayBetweenFeedback);
    }



    private Sound getPositiveSound(ArrayList<Sound> positives){
        if(positives.size()>1){
            int rand = MathUtils.random(0, positives.size() -1);
            while (rand == currentPositiveIndex){
                rand = MathUtils.random(0, positives.size() -1);

            }
            currentPositiveIndex = rand;
        }
        return  positives.get(currentPositiveIndex);
    }

    public void readAllFeedbacksAndPositiveWithNewIngredient(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback, final int ingredientIndex) {
        reader.clearActions();
        /////// blocks
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback);
        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);


        final Sound positive = getPositiveSound(Assets.instance.sounds.positivesIngredients);

        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(positive); //after correct answer comes "yuju"
            }
        }));
        final Sound ingredientSound = getIngredientFromIndex(ingredientIndex);
        readFeedbackAction.addAction(delay(Assets.instance.getSoundDuration(positive)));
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(ingredientSound); //after correct answer comes "yuju"
            }
        }));

        reader.addAction(parallel(readBlocksAction,readFeedbackAction)); // we read feedback and the blocks in parallel
    }

    public void readNumberAllFeedbacksAndFinal(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback) {
        reader.clearActions();
        /////// blocks
        readBlocksAction = createReadNumberAndBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback);
        /////////// feedback
        readFeedbackAction = createReadNumberFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);


        final Sound finalSound = this.finalFeedback;
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(finalSound); //after correct answer comes "yuju"
            }
        }));

        reader.addAction(parallel(readBlocksAction,readFeedbackAction)); // we read feedback and the blocks in parallel
    }


    public void readAllFeedbacksAndFinal(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback) {
        reader.clearActions();
        /////// blocks
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback);
        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);


        final Sound finalSound = this.finalFeedback;
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(finalSound); //after correct answer comes "yuju"
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

    private void readNumberFeedbackAndBlocks(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){
        reader.clearActions();
        /////// blocks
        readBlocksAction = createReadNumberAndBlocksAction (readBlocksAction, toReadNums, extraDelayBetweenFeedback);


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
        readFeedbackAction = createReadNumberFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);

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

    private void readFeedbackAndBlocksAndPositive(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){
        reader.clearActions();
        /////// blocks
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback);
        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);

        final Sound positiveNow = getPositiveSound(this.positiveFeedback);

        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(positiveNow); //after correct answer comes positive feedback
            }
        }));
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                currentClue = getRandomClue(); //after correct answer comes positive feedback
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


    public void readNumberWithFeedback( int numToBuild, float extraDelayBetweenFeedback){ // we use this action at the beginning of new screen, we read feedback without blocks
        reader.clearActions();
        readFeedbackAction.reset();
        // first read number then knocks
        readFeedbackAction.addAction(delay(Constants.READ_ONE_UNIT_DURATION)); // wait before start read feedback
        //readFeedbackAction = playNumber(1,readFeedbackAction);
        //readFeedbackAction.addAction(delay(Constants.READ_NUMBER_DURATION)); // wait to finish read the number

        for(int i = 1; i<=numToBuild;i++){ // if the number is 5 we have to knock 5 times
            readSingleKnockWithNumber(i, readFeedbackAction, extraDelayBetweenFeedback); // we start with 1
        }

        // readFeedbackAction = addToReadFeedbackInSpace(numToBuild, readFeedbackAction, extraDelayBetweenFeedback);
        // first read with small delay at the beginning
        reader.addAction(readFeedbackAction);
    }

    private SequenceAction createReadNumberAndBlocksAction(SequenceAction readBlocksAction, ArrayList<Integer> toReadNums, float extraDelayBetweenFeedback){ // we use this action at the beginning of new screen, we read feedback without blocks
        readBlocksAction.reset();
        readBlocksAction.addAction(delay(Constants.READ_NUMBER_DURATION));
        boolean firstNote;

        for (Integer toReadNum : toReadNums) { // if we have detected block 3 and block 2, we have to read 3 times "mi" and 2 time "re"
            int val = toReadNum; // val will be 3 and than 2
            firstNote = true; // first note should be lauder
            for (int j = 0; j < val; j++) {
                if(j == 0) // first note!
                    addToReadBlock(1, readBlocksAction, firstNote, extraDelayBetweenFeedback); // one single lecture
                else if((val == 5 && j == 3) || (val == 4 && j == 2)) // nr 4 in 5 and nr 3 in 4
                    addToReadBlock(2, readBlocksAction, firstNote, extraDelayBetweenFeedback); // one single lecture
                else
                    addToReadBlock(3, readBlocksAction, firstNote, extraDelayBetweenFeedback); // one single lecture

                firstNote = false;
            }
        }
        return readBlocksAction;
    }

    public void readNumberAllFeedbacksAndPositive(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){
        reader.clearActions();
        /////// blocks
        readBlocksAction = createReadNumberAndBlocksAction (readBlocksAction, toReadNums, extraDelayBetweenFeedback);
        /////////// feedback
        readFeedbackAction = createReadNumberFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);

        final Sound positiveNow = getPositiveSound(this.positiveFeedback);

        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(positiveNow); //after correct answer comes positive feedback
            }
        }));
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                currentClue = getRandomClue(); //after correct answer comes positive feedback
            }
        }));

        reader.addAction(parallel(readBlocksAction,readFeedbackAction)); // we read feedback and the blocks in parallel
    }



    private SequenceAction createReadNumberFeedbackAction(SequenceAction readFeedbackAction, int numToBuild, float extraDelayBetweenFeedback){ // we use this action at the beginning of new screen, we read feedback without blocks
        readFeedbackAction.reset();
        readFeedbackAction = playNumber(numToBuild,readFeedbackAction);
        readFeedbackAction.addAction(delay(Constants.READ_NUMBER_DURATION)); // wait to finish read the number
        readFeedbackAction = addToReadFeedbackInSpace(numToBuild, readFeedbackAction, extraDelayBetweenFeedback);
        return readFeedbackAction;
    }

    private Sound getFeedbackSound(int number){

        Sound feedbackSound;
        switch (number) {
            case 1:
                feedbackSound = Assets.instance.sounds.oneDo;
                break;
            case 2:
                feedbackSound = Assets.instance.sounds.oneRe;
                break;
            case 3:
                feedbackSound = Assets.instance.sounds.oneMi;
                break;
            case 4:
                feedbackSound = Assets.instance.sounds.oneFa;
                break;
            case 5:
                feedbackSound = Assets.instance.sounds.oneSol;
                break;
            default:
                feedbackSound = Assets.instance.sounds.oneSol; // TODO change the default to hmmm nothing?
                break;
        }
        return feedbackSound;
    }


    public void readNumberWithMagicFeedback( ArrayList<Integer>  nowDetected, float extraDelayBetweenFeedback)
    { // we use this action at the beginning of new screen, we read feedback without blocks
        reader.clearActions();
        readFeedbackAction.reset();
        readFeedbackAction.addAction(delay(Constants.READ_ONE_UNIT_DURATION)); // wait before start read feedback

        int numberToBuild =0;

        for (Integer aNowDetected : nowDetected) {
            numberToBuild = numberToBuild + aNowDetected;
        }

        /*
        int counter = 0;
        for(int i = 1; i< numberToBuild; i++)
        {
            counter++
            Sound feedbackSound = getFeedbackSound(nowDetected.get(0));
            readSingleFeedbackWithNumber(counter , readFeedbackAction, extraDelayBetweenFeedback, feedbackSound); // we start with 1
        }
        */
        int counter = 0;
        for (Integer aNowDetected : nowDetected) {
            Sound feedbackSound = getFeedbackSound(aNowDetected);

            for (int j = 0; j < aNowDetected; j++) {
                counter++;
                readSingleFeedbackWithNumber(counter, readFeedbackAction, extraDelayBetweenFeedback, feedbackSound); // we start with 1
            }
        }

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

        for (Integer toReadNum : toReadNums) { // if we have detected block 3 and block 2, we have to read 3 times "mi" and 2 time "re"
            int val = toReadNum; // val will be 3 and than 2
            firstNote = true; // first note should be lauder
            for (int j = 0; j < val; j++) {
                if(j == 0) // first note!
                    addToReadBlock(1, readBlocksAction, firstNote, extraDelayBetweenFeedback); // one single lecture
                else if((val == 5 && j == 3) || (val == 4 && j == 2)) // nr 4 in 5 and nr 3 in 4
                    addToReadBlock(2, readBlocksAction, firstNote, extraDelayBetweenFeedback); // one single lecture
                else
                    addToReadBlock(3, readBlocksAction, firstNote, extraDelayBetweenFeedback); // one single lecture
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
                    playWithoutInterruption(singleSound,true,defaultVolSound); // knocks with which volume??
                }
            }));
            readTutorialAction.addAction((delay(soundDuration)));
        }

        reader.addAction(readTutorialAction);
        return duration_total;
    }

    private float reproduceSounds(ArrayList<Sound> soundsToReproduce){
        float duration_total =  0;

        readTutorialAction.reset();
        for (Sound aSoundsToReproduce : soundsToReproduce) {
            float duration_aux = Assets.instance.getSoundDuration(aSoundsToReproduce);
            duration_total = duration_total + duration_aux;
            final Sound singleSound = aSoundsToReproduce;

            readTutorialAction.addAction(run(new Runnable() {
                public void run() {
                    playWithoutInterruption(singleSound, true, defaultVolSound);
                }
            }));
            readTutorialAction.addAction((delay(duration_aux)));
        }
        reader.addAction(readTutorialAction);

        return duration_total;
    }

    public float reproduce_concrete_tutorial(int start, int end) {
        ArrayList<Sound> soundsToReproduce = new ArrayList<>();

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
        soundsToReproduce.add(Assets.instance.sounds.ct_10);
        soundsToReproduce.add(Assets.instance.sounds.ct_11);
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, start, end);
    }

    private Sound getIngredientFromIndex(int ingredientIndex){
//        ingredientIndex = ingredientIndex%6 +1;
        switch(ingredientIndex){
            case 1:
                return Assets.instance.sounds.i2;
            case 2:
                return Assets.instance.sounds.i3;
            case 3:
                return Assets.instance.sounds.i4;
            case 4:
                return Assets.instance.sounds.i5;
            case 5:
                return Assets.instance.sounds.i6;
            case 6:
                return Assets.instance.sounds.i7;
            case 7:
                return Assets.instance.sounds.i8;
            case 8:
                return Assets.instance.sounds.i9;
            case 9:
                return Assets.instance.sounds.i10;
            case 10:
                return Assets.instance.sounds.i11;
            case 11:
                return Assets.instance.sounds.i12;
            case 12:
                return Assets.instance.sounds.i13;
            case 13:
                return Assets.instance.sounds.i14;
            case 14:
                return Assets.instance.sounds.i15;
            case 15:
                return Assets.instance.sounds.i16;
            case 16:
                return Assets.instance.sounds.i17;
            case 17:
                return Assets.instance.sounds.i1;
            case 18:
                return Assets.instance.sounds.i2;
            case 19:
                return Assets.instance.sounds.i3;
            default:
                return Assets.instance.sounds.i1;
        }
    }

    public void setCurrentClue(){
        currentClue = getRandomClue();
    }

    public float reproduceIntro(){
        Gdx.app.log(TAG,"INTRO reproduce it now! ");
        ArrayList<Sound> soundsToReproduce = new ArrayList<>();
        soundsToReproduce.add(this.introSound);
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,0);
    }

    public float reproduce_ingredients_intro() {
        ArrayList<Sound> soundsToReproduce = new ArrayList<>();
        soundsToReproduce.add(Assets.instance.sounds.ingredientsIntro);
        soundsToReproduce.add(Assets.instance.sounds.i1);
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0, 1);
    }


    public void stop_sounds(ScreenName whichScreen) {
        switch (whichScreen){
            case CONCRETE_TUTORIAL:
                Assets.instance.sounds.ct_1.stop();
                Assets.instance.sounds.ct_2.stop();
                Assets.instance.sounds.ct_3.stop();
                Assets.instance.sounds.ct_4.stop();
                Assets.instance.sounds.ct_5.stop();
                Assets.instance.sounds.ct_6.stop();
                Assets.instance.sounds.ct_7.stop();
                Assets.instance.sounds.ct_8.stop();
                Assets.instance.sounds.ct_9.stop();
                Assets.instance.sounds.ct_10.stop();
                Assets.instance.sounds.ct_11.stop();
                break;
            case ORGANIC_HELP: //TODO update me with correct sounds!!
                Assets.instance.sounds.organicHelpIntro.stop();
                Assets.instance.sounds.organicHelpIntro2.stop();
                Assets.instance.sounds.organicHelpTooFew_1.stop();
                Assets.instance.sounds.organicHelpTooFew_2.stop();
                Assets.instance.sounds.organicHelpTooMuch_1.stop();
                Assets.instance.sounds.organicHelpTooMuch_2.stop();
                Assets.instance.sounds.organicHelpFinal.stop();
                Assets.instance.sounds.organicHelpFinal2.stop();
                break;
            case GAME_STEPS:
                Assets.instance.sounds.stepIntro.stop();
                Assets.instance.sounds.stepIntro2.stop();
                Assets.instance.sounds.stepTooFew_1.stop();
                Assets.instance.sounds.stepTooFew_2.stop();
                Assets.instance.sounds.stepTooMuch_1.stop();
                Assets.instance.sounds.stepTooMuch_2.stop();
                Assets.instance.sounds.stepFinal.stop();
                Assets.instance.sounds.stepFinal2.stop();
                break;
            case GAME_KNOCK:
                Assets.instance.sounds.knockIntro.stop();
                Assets.instance.sounds.knockTooFew.stop();
                Assets.instance.sounds.knockTooMuch.stop();
                Assets.instance.sounds.knockFinal.stop();
                break;
            case GAME_INGREDIENTS:
                Assets.instance.sounds.ingredientsIntro.stop();
                Assets.instance.sounds.ingredientsTooFew.stop();
                Assets.instance.sounds.ingredientsTooMuch.stop();
                Assets.instance.sounds.ingredientsPositive_1.stop();
                Assets.instance.sounds.ingredientsFinal.stop();
                break;
            case GAME_MIXING:
                Assets.instance.sounds.mixingIntro.stop();
                Assets.instance.sounds.mixingTooFew.stop();
                Assets.instance.sounds.mixingTooMuch.stop();
               // Assets.instance.sounds.mixingPositive_1.stop();
                Assets.instance.sounds.mixingFinal.stop();
                break;
            case GAME_MUSIC:
                Assets.instance.sounds.musicIntro.stop();
                Assets.instance.sounds.musicTooFew.stop();
                Assets.instance.sounds.musicTooMuch.stop();
                Assets.instance.sounds.musicFinal.stop();
                break;
            case GAME_GREETING:
                Assets.instance.sounds.greetingIntro.stop();
                Assets.instance.sounds.greetingTooFew.stop();
                Assets.instance.sounds.greetingTooMuch.stop();
                Assets.instance.sounds.greetingFinal.stop();
                break;
        }
    }


}
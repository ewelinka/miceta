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
import java.util.Random;
import java.util.TooManyListenersException;

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
    private final float defaultVolSound = 1.0f;
    private float feedbackVolSound = 0.5f;
    private float knockNoteVol = 0.5f;
    private Actor reader;
    private float readBlockDuration;
    private boolean delay_quit = false;
    private boolean delay_add = false;
    private boolean delay_hint_add = false;
    private boolean delay_hint_quit = false;
    
    private boolean newblock_loop = false;
    
    private Sound nextHint;
    //smarichal
    private boolean successErrorBlocksFeedbackMode = false;
    


	private final Sound   nb_sound = Assets.instance.sounds.newblock;
    private final Music  nb_sound_loop = Assets.instance.music.new_block_loop;
   // private FeedbackSoundType feedbackSoundType;
    private ScreenName currentGameScreenName;
    private Sound tooMuchErrorSound, tooFewErrorSound,finalFeedback, introSound;
    private ArrayList<Sound> positiveFeedback;
    private ArrayList<Sound> concreteTutorialSteps;

    private int currentPositiveIndex;
    private Sound currentClue;
    private ArrayList<Sound> currentGreetingClues;
    private int lastClueIndex;
	private boolean explicit_help;
	private Sound nextTooMany;
	private Sound nextTooFew;
	private boolean addHasHechoUn = true;


    private AudioManager () { }

    public void setStage(Stage stage){
        reader = new Actor(); // actor that reads everything
        stage.addActor(reader);
        readFeedbackAction = new SequenceAction(); // for feedback reading
        readBlocksAction = new SequenceAction(); // for detected blocks reading
        readTutorialAction = new SequenceAction();
        readBlockDuration = Constants.READ_ONE_UNIT_DURATION;
    	nextHint = Assets.instance.sounds.hints.get(new Random().nextInt(3));
    	explicit_help = false;
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
        Gdx.app.log(TAG,"up volume "+feedbackVolSound);
    }

    public void downFeedbackVolSound(){

        if (feedbackVolSound > 0.2f)
            feedbackVolSound -= 0.1f;
        feedbackVolSound = (float)roundMe(feedbackVolSound);
        Gdx.app.log(TAG,"down volume "+feedbackVolSound);
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
    
    private void playHint(){
    	nextHint.play(defaultVolSound);
    }

    private void nextTooManySound(){
    	if(new Random().nextInt(2)==0){ //random between 0-1
    		nextTooMany =  tooMuchErrorSound;
    	}else{
    		nextTooMany = Assets.instance.sounds.hints_menosPiezasGenerales.get(new Random().nextInt(2));
    	}
    }
    
    private void nextTooFewSound(){
    	if(new Random().nextInt(2)==0){ //random between 0-1
    		nextTooFew =  tooFewErrorSound;
    	}else{
    		nextTooFew = Assets.instance.sounds.hints_masPiezasGenerales.get(new Random().nextInt(3));
    	}
    }

    
    private void playQuitOrAddBlock(int i){
        final Sound soundToPlay;
        if (i == 0) {
            soundToPlay = nextTooMany;
        }
        else {
            soundToPlay = nextTooFew;

        }
        soundToPlay.play(defaultVolSound);

    }
    
    private void playExplicitHelp(int n){
        Assets.instance.sounds.hints_todaviaNo.get(n-1).play(defaultVolSound);
    }

    public float setDelay_quit(){
        delay_quit= true;
        nextTooFewSound();
        return Assets.instance.getSoundDuration(nextTooFew);
    }

    public float setDelay_add(){
        delay_add= true;
        nextTooManySound();
        return Assets.instance.getSoundDuration(nextTooMany);
    }
    
    public float setDelay_hint_add(){
        delay_hint_add= true;
        nextHint = Assets.instance.sounds.hints.get(new Random().nextInt(3));
        return Assets.instance.getSoundDuration(nextHint);
    }
    
    public float setDelay_hint_quit(){
        delay_hint_quit= true;
        nextHint = Assets.instance.sounds.hints.get(new Random().nextInt(3));
        return Assets.instance.getSoundDuration(nextHint);
    }
    
    public float setExplicitHelp(boolean explicit, int n){
    	explicit_help = explicit;
    	return Assets.instance.getSoundDuration(Assets.instance.sounds.hints_todaviaNo.get(n-1));
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

    public void setConcreteTutorialSoundArray( ArrayList<Sound> sounds){
        concreteTutorialSteps = sounds;
    }


    
    public void setScreenNameAndLastClueIndex(ScreenName gameScreenName){
        lastClueIndex = -1;
        this.currentGameScreenName = gameScreenName;
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

    
    private void addToReadBlock(BlockFeedbackSoundType soundType, int magicSoundLevel, SequenceAction readBlocks, final boolean firstNote, float extraDelayBetweenFeedback) {
        final Sound whichSound;
        switch(soundType){	//TODO smarichal: reemplazar los sonidos por exito, error y neutral
	        case SUCCESS:
                	whichSound = Assets.instance.sounds.magicStrong;
	        	break;
	        case ERROR:
                whichSound = Assets.instance.sounds.magicStrong;
	        	break;
	        case NEUTRAL:
                whichSound = Assets.instance.sounds.magicStrong;
	        	break;
	        default:
                whichSound = Assets.instance.sounds.magicStrong;
	        	break;
        }
//        switch (magicSoundLevel) {
//            case 1: // pronounced strong
//                whichSound = Assets.instance.sounds.magicStrong;
//                break;
//            case 2: // pronounced middle
//                whichSound = Assets.instance.sounds.magicMiddle;
//                break;
//            case 3: // normal
//                whichSound = Assets.instance.sounds.magicNomal;
//                break;
//            default:
//                whichSound = Assets.instance.sounds.magicNomal;
//                break;
//
//        }
        readBlocks.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(whichSound, firstNote, feedbackVolSound);
            }
        }));


        readBlocks.addAction(delay(readBlockDuration + extraDelayBetweenFeedback )); // we wait Xs because sound files with "do", "re" and "mi" have X duration
    }
    
    
    private void addToReadBlock(int magicSoundLevel, SequenceAction readBlocks, final boolean firstNote, float extraDelayBetweenFeedback) {
        final Sound whichSound;
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

    private void readSingleGreetingSound(SequenceAction readFeedback, float extraDelayBetweenFeedback, final ArrayList<Sound> greetings, final int index){
        readFeedback.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(greetings.get(index));
            }
        }));

        readFeedback.addAction(delay(readBlockDuration + extraDelayBetweenFeedback));
    }

    private void readSingleFeedbackSound(SequenceAction readFeedback, float extraDelayBetweenFeedback, final Sound clue){
        readFeedback.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(clue);
            }
        }));

        readFeedback.addAction(delay(readBlockDuration + extraDelayBetweenFeedback));
    }

    private ArrayList<Sound> getRandomGreetings(){
        ArrayList<Sound> greetings = new ArrayList<Sound>();
        // we prepare 20 greetings just in case (max value en csv is 15)
        for(int i=0;i<20;i++){
            int idx = MathUtils.random(0,Assets.instance.sounds.cluesGreeting.size()-1);
            while(lastClueIndex == idx){
                idx = MathUtils.random(0,Assets.instance.sounds.cluesGreeting.size()-1);
            }
            lastClueIndex = idx;
            greetings.add(Assets.instance.sounds.cluesGreeting.get(idx));
//            Gdx.app.log(TAG,"GREETING ---> idx "+idx);

        }
        return greetings;
    }

    private Sound getRandomClue(){
        Sound clueSound = Assets.instance.sounds.cluesKnock.get(0);
        switch(currentGameScreenName){
            case GAME_KNOCK:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesKnock);
                break;
            case GAME_INGREDIENTS:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesIngredients);
                break;
            case GAME_MIXING:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesMixing);
                break;
            case GAME_MUSIC:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesMusic);
                break;
            case GAME_GREETING:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesGreeting);
                break;
            case GAME_STEPS:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesSteps);
                break;
            case ORGANIC_HELP:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesOrganicHelp);
                break;
            case GAME_HOLES:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesHoles);
                break;
            case GAME_WINGS:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesIngredients);
                break;
            case GAME_BIRD:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesBird);
                break;
            case GAME_NUMERUS:
                clueSound = getRandomClueFromAll(Assets.instance.sounds.cluesNumerus);
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


    private SequenceAction addToReadFeedbackInSpace(int nr, SequenceAction readFeedback, float extraDelayBetweenFeedback) {
        for(int i = 0; i<nr;i++){ // if the number is 5 we have to knock 5 times
            if(this.currentGameScreenName == ScreenName.GAME_GREETING)
                readSingleGreetingSound(readFeedback, extraDelayBetweenFeedback, currentGreetingClues, i);
            else
                readSingleFeedbackSound(readFeedback, extraDelayBetweenFeedback, currentClue);
        }
        return readFeedback;
    }

    public void readAllFeedbacks(ArrayList<Integer> toReadNums, int numToBuild, float extraDelayBetweenFeedback){
        //readFeedbackAndBlocksAndYuju(toReadNums,numToBuild);
        readFeedbackAndBlocks(toReadNums, numToBuild, extraDelayBetweenFeedback);

    }
    
    public void readFeedbackWithoutBlocks(int numToBuild, float extraDelayBetweenFeedback){
    	readOnlyFeedback(numToBuild,extraDelayBetweenFeedback);
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
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback,numToBuild);
        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);


        final Sound positive = getPositiveSound(Assets.instance.sounds.pre_positivesIngredients);

        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(positive); //after correct answer comes "yuju"
            }
        }));
       
        readFeedbackAction.addAction(delay(Assets.instance.getSoundDuration(positive)));
 
        if(addHasHechoUn ){
	      //  readFeedbackAction.addAction(delay(Assets.instance.getSoundDuration(positive)+ extraDelayBetweenFeedback));
	        final Sound has_hecho_un = getHasHechoUn(numToBuild);
	        readFeedbackAction.addAction(run(new Runnable() {
	            public void run() {
	                playWithoutInterruption(has_hecho_un); //after correct answer comes positive feedback
	            }
	        }));
	        readFeedbackAction.addAction(delay(Assets.instance.getSoundDuration(has_hecho_un)));
        }
        
        
        final Sound post_positive = getPositiveSound(Assets.instance.sounds.post_positivesIngredients);
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(post_positive); 
            }
        }));
       
        readFeedbackAction.addAction(delay(Assets.instance.getSoundDuration(post_positive)));
                
        final Sound ingredientSound = getIngredientFromIndex(ingredientIndex); 
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(ingredientSound);
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
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback, numToBuild);
        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);

        if(addHasHechoUn ){
	        final Sound has_hecho_un = getHasHechoUn(numToBuild);
	        readFeedbackAction.addAction(run(new Runnable() {
	            public void run() {
	                playWithoutInterruption(has_hecho_un); //after correct answer comes positive feedback
	            }
	        }));
	        readFeedbackAction.addAction(delay(Assets.instance.getSoundDuration(has_hecho_un)+ extraDelayBetweenFeedback));
        }
        
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
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback, numToBuild);
        final int n = numToBuild;
        if(explicit_help ){	//Tienes que formar un X
        	readFeedbackAction.addAction(run(new Runnable() {
                public void run() {
                   playExplicitHelp(n);
                }

            }));
            delay_quit = false;
            delay_add = false;
            delay_hint_add = false;
            delay_hint_quit = false;
            explicit_help = false;
        }
        
        if(delay_add){
            readBlocksAction.addAction(run(new Runnable() {
                public void run() {
                    playQuitOrAddBlock(0);
                }
            }));
            
            delay_quit = false;
            delay_add = false;
            delay_hint_add = false;
            delay_hint_quit = false;
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
            delay_hint_add = false;
            delay_hint_quit = false;
        }
        
        if(delay_hint_add){
        	readBlocksAction.addAction(run(new Runnable() {
                public void run() {
                	playHint();
                }
            }));
            
            delay_quit = false;
            delay_add = false;
            delay_hint_add = false;
            delay_hint_quit = false;
        }
               
        if(delay_hint_quit){
        	readFeedbackAction.addAction(run(new Runnable() {
                public void run() {
                	playHint();
                }
            }));
            
            delay_quit = false;
            delay_add = false;
            delay_hint_add = false;
            delay_hint_quit = false;
        }

        reader.addAction(parallel(readBlocksAction,readFeedbackAction)); // we read feedback and the blocks in parallel
    }
    
    
    
    
    
    private void readOnlyFeedback(int numToBuild, float extraDelayBetweenFeedback){
        reader.clearActions();
        /////// blocks

        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);
        final int n = numToBuild;
        if(explicit_help){	//Tienes que formar un X
        	readFeedbackAction.addAction(run(new Runnable() {
                public void run() {
                   playExplicitHelp(n);
                }

            }));
            delay_quit = false;
            delay_add = false;
            delay_hint_add = false;
            delay_hint_quit = false;
            explicit_help = false;
        }
        
        final int option = delay_quit?1:0;
        if(delay_quit || delay_add){
            readFeedbackAction.addAction(run(new Runnable() {
                public void run() {
                    playQuitOrAddBlock(option);
                }


            }));
            delay_quit = false;
            delay_add = false;
            delay_hint_add = false;
            delay_hint_quit = false;
        }
        
        if(delay_hint_add || delay_hint_quit){
        	readFeedbackAction.addAction(run(new Runnable() {
                public void run() {
                	playHint();
                }
            }));
            
            delay_quit = false;
            delay_add = false;
            delay_hint_add = false;
            delay_hint_quit = false;
        	nextHint = Assets.instance.sounds.hints.get(new Random().nextInt(3));
        }
        reader.addAction(readFeedbackAction); // we read feedback and the blocks in parallel
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
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback, numToBuild);
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
        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback, numToBuild);
        /////////// feedback
        readFeedbackAction = createReadFeedbackAction(readFeedbackAction, numToBuild, extraDelayBetweenFeedback);

        final Sound positiveNow = getPositiveSound(this.positiveFeedback);
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                playWithoutInterruption(positiveNow); //after correct answer comes positive feedback
            }
        }));
        
        if(addHasHechoUn ){
	        readFeedbackAction.addAction(delay(Assets.instance.getSoundDuration(positiveNow)+ extraDelayBetweenFeedback));
	        final Sound has_hecho_un = getHasHechoUn(numToBuild);
	        readFeedbackAction.addAction(run(new Runnable() {
	            public void run() {
	                playWithoutInterruption(has_hecho_un); //after correct answer comes positive feedback
	            }
	        }));
        }
        
        final ScreenName nowGame = this.currentGameScreenName;
        readFeedbackAction.addAction(run(new Runnable() {
            public void run() {
                if(nowGame == ScreenName.GAME_GREETING){
                    currentGreetingClues = getRandomGreetings();
                }else
                    currentClue = getRandomClue();
            }
        }));

        reader.addAction(parallel(readBlocksAction,readFeedbackAction)); // we read feedback and the blocks in parallel
    }

    private Sound getHasHechoUn(int n) {
		return Assets.instance.sounds.has_hecho_un.get(n-1);
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

    // this function is used just in organic help screen
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
        
        if(addHasHechoUn ){
	        readFeedbackAction.addAction(delay(Assets.instance.getSoundDuration(positiveNow)+ extraDelayBetweenFeedback));
	        final Sound has_hecho_un = getHasHechoUn(numToBuild);
	        readFeedbackAction.addAction(run(new Runnable() {
	            public void run() {
	                playWithoutInterruption(has_hecho_un); //after correct answer comes positive feedback
	            }
	        }));
        }
        
        
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

//smarichal: this function wasn't being used anywhere
//    public void readBlocks(ArrayList<Integer> toReadNums, float extraDelayBetweenFeedback){
//        reader.clearActions();
//        readBlocksAction = createReadBlocksAction(readBlocksAction, toReadNums, extraDelayBetweenFeedback);
//        reader.addAction(readBlocksAction);
//    }

    
    private SequenceAction createReadBlocksActionWithSuccessOrError(SequenceAction readBlocksAction, ArrayList<Integer> toReadNums, float extraDelayBetweenFeedback, int numToBuild){
        readBlocksAction.reset();
        boolean firstNote;
        int sum = 0;
//TODO smarichal: here we can distinguish between success, error and neutral sounds
        for (Integer toReadNum : toReadNums) { // if we have detected block 3 and block 2, we have to read 3 times "mi" and 2 time "re"
            int val = toReadNum; // val will be 3 and than 2
            firstNote = true; // first note should be lauder
            for (int j = 0; j < val; j++) {
            	sum++;
            	BlockFeedbackSoundType blockFeedbackType;
           		blockFeedbackType = sum<=numToBuild?BlockFeedbackSoundType.SUCCESS:BlockFeedbackSoundType.ERROR;
                if(j == 0) // first note!
                    addToReadBlock(blockFeedbackType, 1, readBlocksAction, firstNote, extraDelayBetweenFeedback); // one single lecture
                else if((val == 5 && j == 3) || (val == 4 && j == 2)) // nr 4 in 5 and nr 3 in 4
                    addToReadBlock(blockFeedbackType, 2, readBlocksAction, firstNote, extraDelayBetweenFeedback); // one single lecture
                else
                    addToReadBlock(blockFeedbackType, 3, readBlocksAction, firstNote, extraDelayBetweenFeedback); // one single lecture
                firstNote = false;
            }
        }
        return readBlocksAction;
    }
    
    
    
    private SequenceAction createReadBlocksAction(SequenceAction readBlocksAction, ArrayList<Integer> toReadNums, float extraDelayBetweenFeedback, int numToBuild){
        readBlocksAction.reset();
        boolean firstNote;
        Gdx.app.log(TAG,"Leyendo feedback bloques");
        for (Integer toReadNum : toReadNums) { // if we have detected block 3 and block 2, we have to read 3 times "mi" and 2 time "re"
            int val = toReadNum; // val will be 3 and than 2
            Gdx.app.log(TAG,"val= "+val);
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
    
    
    public float reproduceFinalStep1(){
	    Gdx.app.log(TAG,"FINAL STEP 1 reproduce it now! ");
        ArrayList<Sound> soundsToReproduce = new ArrayList<>();
        soundsToReproduce.add(Assets.instance.sounds.muyBien);
        soundsToReproduce.add(this.finalFeedback);
    	soundsToReproduce.add(this.concreteTutorialSteps.get(0));
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,2);
    }
    
    public float reproduceMuyBienEscuchaComoSuena(){
	    Gdx.app.log(TAG,"FINAL STEP 2 reproduce it now! ");
    	ArrayList<Sound> soundsToReproduce = new ArrayList<>();
        //soundsToReproduce.add(Assets.instance.sounds.muyBien);
    	soundsToReproduce.add(Assets.instance.sounds.tada);
    	soundsToReproduce.add(this.concreteTutorialSteps.get(1));
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,1);
    }
    
    public float reproducePonUnaPieza(){
	    Gdx.app.log(TAG,"pon una pieza reproduce it now! ");
    	ArrayList<Sound> soundsToReproduce = new ArrayList<>();
    	soundsToReproduce.add(Assets.instance.sounds.ponUnaPieza);
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,0);
    }
    
    public float reproduceTodaviaNo(int n){
	    Gdx.app.log(TAG,"Playing todavia no n= " + n);
    	ArrayList<Sound> soundsToReproduce = new ArrayList<>();
    	soundsToReproduce.add(Assets.instance.sounds.hints_todaviaNo.get(n-1));
    	return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,0);
    }
    
    public float reproduceCuandoJuguemosJuntos(){
     	ArrayList<Sound> soundsToReproduce = new ArrayList<>();
     	soundsToReproduce.add(Assets.instance.sounds.cuando_juguemos);
     	return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,0);
    }
    
    public float reproduceAhiVaDeNuevo(){
	    Gdx.app.log(TAG,"va de nuevo reproduce it now! ");
    	ArrayList<Sound> soundsToReproduce = new ArrayList<>();
    	soundsToReproduce.add(this.concreteTutorialSteps.get(2));
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,0); 
    }
    
    public float reproduceStep4(){
	    Gdx.app.log(TAG,"STEP 4 reproduce it now! ");
    	ArrayList<Sound> soundsToReproduce = new ArrayList<>();
    	soundsToReproduce.add(this.concreteTutorialSteps.get(3));
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,0);
    }
    
    public float reproduceStep5(){
	    Gdx.app.log(TAG,"STEP 5 reproduce it now! ");
    	ArrayList<Sound> soundsToReproduce = new ArrayList<>();
    	soundsToReproduce.add(this.concreteTutorialSteps.get(4));
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,0);
    }
    
    public float reproduceStep6(){
	    Gdx.app.log(TAG,"STEP 6 reproduce it now! ");
    	ArrayList<Sound> soundsToReproduce = new ArrayList<>();
    	soundsToReproduce.add(this.concreteTutorialSteps.get(5));
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,0);
    }
    
    public float reproduceStep7(){
	    Gdx.app.log(TAG,"STEP 7 reproduce it now! ");
    	ArrayList<Sound> soundsToReproduce = new ArrayList<>();
    	soundsToReproduce.add(Assets.instance.sounds.tada);
    	soundsToReproduce.add(this.concreteTutorialSteps.get(6));
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,1);
    }
    
    public float reproduceStep8(){
	    Gdx.app.log(TAG,"STEP 7 reproduce it now! ");
    	ArrayList<Sound> soundsToReproduce = new ArrayList<>();
    	soundsToReproduce.add(this.concreteTutorialSteps.get(7));
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,0);
    }
    
    
    public float reproduceLimpiarRecta(){
    	ArrayList<Sound> soundsToReproduce = new ArrayList<>();
        soundsToReproduce.add(Assets.instance.sounds.limpiarRecta);
        return AudioManager.instance.reproduceSoundsWithIndex(soundsToReproduce, 0,0);
    }
    
    public float reproduceFinal(){
        float soundDuration = Assets.instance.getSoundDuration(this.finalFeedback);
        playWithoutInterruption(this.finalFeedback); //after correct answer comes "yuju"
        
        return soundDuration;
    }
    
//    
//    public float reproduceConcreteTutorialStep(int step){
//    	float soundDuration = Assets.instance.getSoundDuration(this.concreteTutorialSteps.get(step-1));
//        playWithoutInterruption(this.concreteTutorialSteps.get(step-1));
//
//        return soundDuration;
//    }

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

        if(this.currentGameScreenName == ScreenName.GAME_GREETING){
            currentGreetingClues = getRandomGreetings();
        }else
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
        if(reader!=null)
            reader.clearActions();
        switch (whichScreen){
            case CONCRETE_TUTORIAL:
                Assets.instance.sounds.concreteIntro.stop();
                break;
            case ORGANIC_HELP:
                Assets.instance.sounds.organicHelpIntro.stop();
                Assets.instance.sounds.organicHelpIntro2.stop();
                Assets.instance.sounds.organicHelpFinal.stop();
                Assets.instance.sounds.organicHelpFinal2.stop();
                break;
            case GAME_STEPS:
                Assets.instance.sounds.stepIntro.stop();
                Assets.instance.sounds.stepIntro2.stop();
                Assets.instance.sounds.stepFinal.stop();
                Assets.instance.sounds.stepFinal2.stop();
                break;
            case GAME_KNOCK:
                Assets.instance.sounds.knockIntro.stop();
                break;
            case GAME_INGREDIENTS:
                Assets.instance.sounds.ingredientsIntro.stop();
                break;
            case GAME_MIXING:
                Assets.instance.sounds.mixingIntro.stop();
                break;
            case GAME_MUSIC:
                Assets.instance.sounds.musicIntro.stop();
                break;
            case GAME_GREETING:
                Assets.instance.sounds.greetingIntro.stop();
                break;
            case FINAL_MM1:
                Assets.instance.sounds.finalMM1.stop();
                break;
            case INTRO_MM2:
                Assets.instance.sounds.introMM2.stop();
                break;
            case GAME_HOLES:
                Assets.instance.sounds.holesIntro.stop();
                break;
            case GAME_WINGS:
                Assets.instance.sounds.wingsIntro.stop();
                break;
            case GAME_BIRD:
                Assets.instance.sounds.birdIntro.stop();
                break;
            case GAME_NUMERUS:
                Assets.instance.sounds.numerusIntro.stop();
                break;
            case FINAL_MM2:
                Assets.instance.sounds.finalMM2.stop();
                break;
        }
    }


    public float getNextHintDelay() {
    	return Assets.instance.getSoundDuration(this.nextHint);
	}

	public boolean isSuccessErrorBlocksFeedbackMode() {
		return successErrorBlocksFeedbackMode;
	}

	public void setSuccessErrorBlocksFeedbackMode(
			boolean successErrorBlocksFeedbackMode) {
		this.successErrorBlocksFeedbackMode = successErrorBlocksFeedbackMode;
	}
}
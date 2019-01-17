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
    
    private int step;
	private boolean waitingForBlockFeedback; 
    
    public CvWithIntroControllerConcrete(miCeta game, Stage stage, ScreenName screenNameNow, Sound introSound, ArrayList<Sound> positiveFeedback, Sound tooFewErrorSound, Sound tooMuchErrorSound, Sound finalFeedback, ArrayList<Sound> concreteTurorialSteps,boolean upLevel, boolean shouldRepeatTutorial) {
        super(game, stage, screenNameNow, introSound, positiveFeedback, tooFewErrorSound, tooMuchErrorSound, finalFeedback, upLevel, shouldRepeatTutorial);
        AudioManager.instance.setConcreteTutorialSoundArray(concreteTurorialSteps);
    }

    @Override
    protected void init(){
        numberToPlay = 0;
        step = 1;
        setDelayForPositiveFeedback();
        timeToWait = AudioManager.instance.reproduceIntro();
        answerRight = false;
        firstLoop = true;
        waitingForBlockFeedback = false;
    }

    
    protected void reproduceAllFeedbacks(ArrayList<Integer> nowDetected, int numberToPlay){
        AudioManager.instance.readAllFeedbacks(nowDetected, numberToPlay, extraDelayBetweenFeedback);
    }
    
    private static int MAX_INACTIVITY_TIME = 6;
    private static int MAX_WAIT_FOR_ACTION_TIME = 6;
    
    @Override
    public void update(float deltaTime) {
        timePassed+=deltaTime; // variable used to check in isTimeToStartNewLoop() to decide if new feedback loop should be started
        inactivityTime+=deltaTime;
        //updateCV();
        updateTangible();
    	
        if(isTimeToStartNewLoop()){
            if(firstLoop) {
                inactivityTime = 0;
                firstLoop = false;
            }
            Gdx.app.log(TAG,"isTimeToStartNewLoop and willGoToNextPart "+willGoToNextPart);
            if(!willGoToNextPart) {
                timePassed = 0; // start to count the time
                ArrayList<Integer> nowDetected = blocksManager.getDetectedIds();
                ArrayList<Integer> nowDetectedValues = blocksManager.getCurrentBlocksValues();
                currentSum = 0;
                for (Integer aNowDetected : nowDetectedValues)
                    currentSum += aNowDetected; // current sum
                switch(step){
	                case 1:
	                	if(nowDetected.size() > 0){ //waiting for any block
		                    Gdx.app.log(TAG,"we have block! ");
		                    timeToWait = AudioManager.instance.reproduceFinalStep1()+ 4;
		                    Gdx.app.log(TAG,"after play 1, time to wait = " + timeToWait );
		                    step = 2;
		                }
		                else {
		                    if(inactivityTime > MAX_INACTIVITY_TIME +4){
		                        timeToWait = AudioManager.instance.reproducePonUnaPieza();
		                        Gdx.app.log(TAG,"inactivity: time to wait = " + timeToWait );
		                        inactivityTime = 0;
		                    }
		                    else{
		                        timeToWait = 1.0f;
		                        Gdx.app.log(TAG,"else time to wait = " + timeToWait );
		                    }
		                }
	            	break;
	                case 2: //waiting for block 2
	                	timeToWait = 2;
	                	if(nowDetected.size() > 0){
	                		if(nowDetected.size()>1){
	                			if(inactivityTime > MAX_INACTIVITY_TIME + 4){
		                			this.tooMuchErrorSound.play(1.0f);
			                        timeToWait = 1.5f; //check this time
			                        inactivityTime = 0;
	                			}
	                		}else{	//hay una sola
	                			if(nowDetectedValues.get(0)==2){ 
	                				timeToWait = AudioManager.instance.reproduceMuyBienEscuchaComoSuena();//+ 5;
	    		                    Gdx.app.log(TAG,"after play final step 2");
	    		                    step = 3;
	                			}else{
	                				if(inactivityTime > MAX_INACTIVITY_TIME +4){
		    		                    Gdx.app.log(TAG,"Playing todavia no");
		                				timeToWait = AudioManager.instance.reproduceTodaviaNo(2);
		                				inactivityTime=0;
	                				}
	                			}
	                		}
	                	}else{
	                		if(inactivityTime > MAX_INACTIVITY_TIME +4){
		                        timeToWait = AudioManager.instance.reproducePonUnaPieza();
		                        inactivityTime = 0;
		                    }else
		                        timeToWait = 1.0f;
	                	}
	                	
                	break;
	                case 3: //read feedback of the blocks
	                	timeToWait = calculateTimeToWait(currentSum, currentSum, false);
	                    Gdx.app.log(TAG,"currentsum = "+ currentSum + ", timeToWait= " + timeToWait);
	                	reproduceAllFeedbacks(nowDetectedValues, 0);
	                	step = 4;
                	break;
	                case 4:
	                	timeToWait=AudioManager.instance.reproduceAhiVaDeNuevo();
	                	step = 5;
                	break;
	                case 5:
	                	timeToWait = calculateTimeToWait(currentSum, currentSum, false);
	                	Gdx.app.log(TAG,"currentsum = "+ currentSum + ", timeToWait= " + timeToWait);
	                	reproduceAllFeedbacks(nowDetectedValues, 0);
	                	inactivityTime=MAX_INACTIVITY_TIME;
	                	step = 6;
                	break;
	                case 6://limpiar recta
	                	if( (nowDetected.size() > 0)){
	                		if(inactivityTime>=MAX_INACTIVITY_TIME){
		                		timeToWait=AudioManager.instance.reproduceLimpiarRecta();
		                		inactivityTime = 0;
	                		}
	                	}else{
	                		step=7;
	                	}
                	break;
	                case 7: //recta ya está vacia
	                	timeToWait=AudioManager.instance.reproduceStep4() +4;
                        inactivityTime = 0;
	                	step=8;
                	break;
	                case 8:
	                	if(nowDetected.size() > 0){
	                		if(nowDetected.size()>1){
	                			if(inactivityTime > MAX_INACTIVITY_TIME +4){
		                			this.tooMuchErrorSound.play(1.0f);
			                        timeToWait = 1.5f; //check this time
			                        inactivityTime = 0;
	                			}
	                		}else{	//hay una sola
	                			if(nowDetectedValues.get(0)==3){ 
	                				timeToWait = AudioManager.instance.reproduceMuyBienEscuchaComoSuena();//+ 5;
	    		                    Gdx.app.log(TAG,"after play final step 2");
	    		                    step = 9;
	                			}else{
	                				if(inactivityTime > MAX_INACTIVITY_TIME +4){
		    		                    Gdx.app.log(TAG,"Playing todavia no");
		                				timeToWait = AudioManager.instance.reproduceTodaviaNo(3);
		                				inactivityTime=0;
	                				}
	                			}
	                		}
	                	}else{
		                	//esperando que ponga la pieza numero 3
		                	if(inactivityTime > MAX_INACTIVITY_TIME){
			                	timeToWait=AudioManager.instance.reproduceStep4(); //repite consigna pon la pieza 3
		                        inactivityTime = 0;
		                    }
		                    else
		                        timeToWait = 1.0f;
	                	}
                	break;
	                case 9:
	                	timeToWait = calculateTimeToWait(currentSum, currentSum,false);
	                	Gdx.app.log(TAG,"currentsum = "+ currentSum + ", timeToWait= " + timeToWait);
	                	reproduceAllFeedbacks(nowDetectedValues, 0);
	                	step = 10;
                	break;
	                case 10:
	                	timeToWait=AudioManager.instance.reproduceAhiVaDeNuevo();
	                	step = 11;
                	break;
	                case 11:
	                	timeToWait = calculateTimeToWait(currentSum, currentSum,false);
	                	reproduceAllFeedbacks(nowDetectedValues, 0);
	                	timeToWait+=1.5;
	                	step = 12;
                	break;
	                case 12://limpiar recta
	                	if( (nowDetected.size() > 0)){
	                		if(inactivityTime>=MAX_INACTIVITY_TIME){
		                		timeToWait=AudioManager.instance.reproduceLimpiarRecta();
		                		inactivityTime = 0;
	                		}
	                	}else{
	                		step=13;
	                	}
                	break;
	                case 13: //recta ya está vacia
	                	timeToWait=AudioManager.instance.reproduceStep5();
                        inactivityTime = 0;
	                	step=14;
                	break;
	                case 14:
	                	if(nowDetected.size() > 0){
	                		if(nowDetected.size()>1){
	                			if(inactivityTime > MAX_INACTIVITY_TIME +4){
		                			this.tooMuchErrorSound.play(1.0f);
			                        timeToWait = 1.5f; //check this time
			                        inactivityTime = 0;
	                			}
	                		}else{	//hay una sola
	                			if(nowDetectedValues.get(0)==1){ 
	                				timeToWait = AudioManager.instance.reproduceMuyBienEscuchaComoSuena();//+ 5;
	    		                    Gdx.app.log(TAG,"after play final step 2");
	    		                    step = 15;
	                			}else{
	                				if(inactivityTime > MAX_INACTIVITY_TIME +4){
		                				timeToWait = AudioManager.instance.reproduceTodaviaNo(1);
		                				inactivityTime=0;
	                				}
	                			}
	                		}
	                	}else{
		                	//esperando que ponga la pieza numero 1
		                	if(inactivityTime > MAX_INACTIVITY_TIME){
			                	timeToWait=AudioManager.instance.reproduceStep5(); //repite consigna pon la pieza 3
		                        inactivityTime = 0;
		                    }
		                    else
		                        timeToWait = 1.0f;
	                	}
                	break;
	                case 15:
	                	timeToWait = calculateTimeToWait(currentSum, currentSum, false);
	                	Gdx.app.log(TAG,"currentsum = "+ currentSum + ", timeToWait= " + timeToWait);
	                	reproduceAllFeedbacks(nowDetectedValues, 0);
	                	step = 16;
                	break;
                	case 16:
	                	timeToWait=AudioManager.instance.reproduceAhiVaDeNuevo();
	                	step = 17;
                	break;
	                case 17:
	                	timeToWait = calculateTimeToWait(currentSum, currentSum, false);
	                	reproduceAllFeedbacks(nowDetectedValues, 0);
	                	step = 18;
                	break;
	                case 18://limpiar recta
	                	if( (nowDetected.size() > 0)){
	                		if(inactivityTime>=MAX_INACTIVITY_TIME){
		                		timeToWait=AudioManager.instance.reproduceLimpiarRecta();
		                		inactivityTime = 0;
	                		}
	                	}else{
	                		step=19;
	                	}
                	break;
                	case 19: //recta ya está vacia
	                	timeToWait=AudioManager.instance.reproduceStep6();
	                	inactivityTime =0;
	                	step=20;
                	break;
	                case 20:
	                	if(nowDetected.size() > 0){
	                		if(nowDetected.size()>2){
	                			if(inactivityTime>=MAX_INACTIVITY_TIME +4){
		                			this.tooMuchErrorSound.play(1.0f);
			                        timeToWait = 1.5f; //check this time
			                        inactivityTime = 0;
	                			}
	                		}else if(nowDetected.size()==1){
	                			if(inactivityTime>=MAX_INACTIVITY_TIME +4){
	                				this.tooFewErrorSound.play(1.0f);
			                        timeToWait = 1.5f; //check this time
			                        inactivityTime = 0;
	                			}
	                			//hay que poner una mas
	                		}else{	//hay 2 piezas
	                			if( (nowDetectedValues.get(0)==2 && nowDetectedValues.get(1)==3) || 
	                					(nowDetectedValues.get(0)==3 && nowDetectedValues.get(1)==2) ){ 
	                				timeToWait = AudioManager.instance.reproduceStep7() +1;
	    		                    Gdx.app.log(TAG,"after play final step 2");
	    		                    step = 21;
	                			}else{
	                				if(inactivityTime>=MAX_INACTIVITY_TIME +4){
		                				timeToWait = AudioManager.instance.reproduceTodaviaNo(5);
		                				inactivityTime=0;
	                				}
	                			}
	                		}
	                	}else{
		                	//esperando que ponga la pieza numero ...
		                	if(inactivityTime > MAX_INACTIVITY_TIME + 5){
			                	timeToWait=AudioManager.instance.reproduceStep6(); //repite consigna pon la pieza ...
		                        inactivityTime = 0;
		                    }
		                    else
		                        timeToWait = 1.0f;
	                	}
                	break;
	                case 21:
	                	timeToWait = calculateTimeToWait(currentSum, currentSum, false);
	                	Gdx.app.log(TAG,"currentsum = "+ currentSum + ", timeToWait= " + timeToWait);
	                	reproduceAllFeedbacks(nowDetectedValues, 0);
	                	step = 22;
                	break;
                	case 22:
	                	timeToWait=AudioManager.instance.reproduceAhiVaDeNuevo();
	                	step = 23;
                	break;
	                case 23:
	                	timeToWait = calculateTimeToWait(currentSum, currentSum, false);
	                	reproduceAllFeedbacks(nowDetectedValues, 0);
	                	step = 24;
                	break;
	                case 24:
	                	timeToWait = AudioManager.instance.reproduceCuandoJuguemosJuntos() +3;
	                    Gdx.app.log(TAG,"after play final step 2");
	                    step = 25;
                	break;
	                case 25:
	                	timeToWait = AudioManager.instance.reproduceStep8() +3;
	                    Gdx.app.log(TAG,"after play final step 2");
	                    willGoToNextPart=true;
                	break;

                }
            }else{
                goToNextLevel();

            }
        }
    }

}

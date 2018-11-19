package miceta.game.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import miceta.game.core.receiver.Block;
import miceta.game.core.util.GamePreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import static edu.ceta.vision.core.utils.BlocksMarkersMap.belongsToBlockClass;

/**
 * Created by ewe on 4/10/18.
 */
public class ResultsManager {
    public static final String TAG = ResultsManager.class.getName();
    private Date intentStartDate;
    private Date intentStartDateLock;
    private Date intentEndDate;
    private int intentsNr;
    private int priceNumber; // first price? second?
    private int lastResponse;
    private float feedbackDuration;
    private ArrayList<Integer> responseBlocks;
    private int levelNumber;
    private FileHandle intentsFile;
    private SimpleDateFormat sdf;
    private  SimpleDateFormat justTime;
    private long collectionTimeMillis;
    private String toSave;
    private byte successfulIntent; // 1=yes, 0=no
    private String randomId;
    private ArrayList<Integer> previousIds;

    public ResultsManager() {
        sdf = new SimpleDateFormat("dd-MM-yyyy,HH:mm:ss.SSS");
        justTime = new SimpleDateFormat("HH:mm:ss.SSS");
        randomId = GamePreferences.instance.randomId;
        intentsFile = Gdx.files.external("./ceta-results/"+randomId+"-results.csv");
        Gdx.app.log(TAG, " AAAAAAA "+ Gdx.files.getLocalStoragePath()+ " available: "+Gdx.files.isLocalStorageAvailable());
        previousIds = new ArrayList<>(); // TODO check if should be done here or we reset in every new screen
    }

    public void newPriceAppeared(int priceNr, int levelNr){
        intentStartDate = new Date();
        intentStartDateLock = intentStartDate;
        intentsNr = 0;
        priceNumber = priceNr;
        levelNumber = levelNr;
        Gdx.app.log(TAG,"NEW PRICE "+justTime.format(intentStartDate));
    }

    public void setFeedbackDuration(float feedbackDurationRegistered){
        feedbackDuration = feedbackDurationRegistered;
        intentStartDate = new Date(intentStartDate.getTime()+(int)(feedbackDuration*1000));
        intentStartDateLock = intentStartDate;
    }

    public void addIntentFromActionSubmit(boolean wasSuccessful, int kidResponse, int priceVal, ArrayList<Integer> toReadVals){
        addIntent(wasSuccessful,kidResponse,priceVal,toReadVals, new ArrayList<Integer>(),true);
    }

    public synchronized void addIntent(boolean wasSuccessful, int kidResponse, int priceVal, ArrayList<Integer> responseBlocks, ArrayList<Integer> detectedIds, boolean cameFromActionSubmit){
        Date toUseStartDate = lastResponse == kidResponse? intentStartDateLock : intentStartDate;
        if(!(lastResponse == kidResponse)){ // its the same response, with the same begging
            intentsNr+=1;
            intentStartDateLock = intentStartDate;
        }
        intentEndDate = new Date();
        Gdx.app.log(TAG," addIntent "+justTime.format(intentEndDate)+ " feedbackDuration: "+feedbackDuration);
        collectionTimeMillis = intentEndDate.getTime() - toUseStartDate.getTime() ;

        if(wasSuccessful)
            successfulIntent = 1;
        else
            successfulIntent = 0;

        // level, price Nr, price val, lastResponse, responseBlocks, lastResponse blocks number, intents nr, intent result (1 or 0),
        // intent start, intent end, difference in millis
        // id
        toSave = levelNumber+","+priceNumber+","+priceVal+","
                + kidResponse +","+formatResponseBlocks(responseBlocks)+","+responseBlocks.size()+","+formatResponseBlocks(detectedIds)+", "
                +intentsNr+","+successfulIntent+","
                +sdf.format(toUseStartDate)+","+justTime.format(intentEndDate)+","+collectionTimeMillis+","
                +(cameFromActionSubmit? 1 : 0)+","
                +randomId+"\n";
        Gdx.app.log(TAG,"save data "+toSave);
        intentsFile.writeString(toSave,true);

        if(!(lastResponse == kidResponse)){
            intentStartDate = intentEndDate; // if its the same response the start of the next will be the end of the first registered
        }
        lastResponse = kidResponse;
        feedbackDuration = 0;

    }

    private String formatResponseBlocks(ArrayList<Integer> responseBlocks){
        String responseAsString = "";
        for (int i=0;i<responseBlocks.size();i++){
            responseAsString+=responseBlocks.get(i)+" ";
        }
        return responseAsString;
    }

    public boolean analyseDetectedIds(ArrayList<Integer> nowDetectedIds, Set<Block> set, int numberToPlay){
        boolean somethingChanged = false;
        int detectedBlocksSum = 0;
        ArrayList<Integer> nowDetectedValues = new ArrayList<>();
        ArrayList<Integer> previousIdsCopy = new ArrayList<>(previousIds);

        for(int i = nowDetectedIds.size()-1;i>=0;i--){ // we start from the end to avoid ids problems
            int nowId = nowDetectedIds.get(i);
            int val = getBlockValeFromSet(nowId,set);
            detectedBlocksSum+=val;
            nowDetectedValues.add(val);
            if(!previousIds.contains(nowId)) { // we know this id?
                Gdx.app.log(TAG, String.format("new id %s", nowId));
                somethingChanged = true;
            }
        }
        previousIdsCopy.removeAll(nowDetectedIds);
        if(previousIdsCopy.size() > 0) {
            Gdx.app.log(TAG, "missing ids!");
            somethingChanged = true;
        }

        if(somethingChanged) {
            addIntent(numberToPlay == detectedBlocksSum, detectedBlocksSum, numberToPlay, nowDetectedValues, nowDetectedIds, false);
            previousIds = new  ArrayList<>(nowDetectedIds);
        }
        return somethingChanged;
    }
    
    private int getBlockValeFromSet(int blockId, Set<Block> set){
    	for(Iterator<Block> iter = set.iterator();iter.hasNext();){
    		Block b = iter.next();
    		if(b.getId()==blockId){
    			return b.getValue();
    		}
    	}
    	return 0;
    }

    private int getBlockValue(int blockId){
        for(int i=1;i<=5;i++){
            if(belongsToBlockClass(i,blockId)){
                return i;
            }
        }
        return 0;
    }

}

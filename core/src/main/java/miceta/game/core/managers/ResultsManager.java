package miceta.game.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import miceta.game.core.util.GamePreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ewe on 4/10/18.
 */
public class ResultsManager {
    public static final String TAG = ResultsManager.class.getName();
    private Date intentStartDate;
    private Date intentEndDate;
    private int intentsNr;
    private int priceNumber; // first price? second?
    private int priceValue; // which number?
    private int response;
    private ArrayList<Integer> responseBlocks;
    private int levelNumber;
    private FileHandle intentsFile;
    private SimpleDateFormat sdf;
    private  SimpleDateFormat justTime;
    private long collectionTimeMillis;
    private String toSave;
    private byte successfulIntent; // 1=yes, 0=no


    private int lastSolutionNr;
    private int lastFinalSolutionNr;
    private String randomId;

    public ResultsManager() {
        sdf = new SimpleDateFormat("dd-MM-yyyy,HH:mm:ss.SSS");
        justTime = new SimpleDateFormat("HH:mm:ss.SSS");
        randomId = GamePreferences.instance.randomId;
        intentsFile = Gdx.files.external("./ceta-results/"+randomId+"-results.csv");
        Gdx.app.log(TAG, " AAAAAAA "+ Gdx.files.getLocalStoragePath()+ " available: "+Gdx.files.isLocalStorageAvailable());
        lastFinalSolutionNr = 0;
        lastSolutionNr = 0;
    }

    public void newPriceAppeared(int priceNr, int levelNr){
        intentStartDate = new Date();
        intentsNr = 0;
        priceNumber = priceNr;
        levelNumber = levelNr;
        Gdx.app.log(TAG,"NEW PRICE "+intentStartDate);
    }

    public synchronized void addIntent(boolean wasSuccessful, int kidResponse, int priceVal, ArrayList<Integer> toReadVals){
        Gdx.app.log(TAG," addIntent +1 to "+intentsNr);
        responseBlocks = toReadVals;

        intentsNr+=1;
        intentEndDate = new Date();
        collectionTimeMillis = intentEndDate.getTime() - intentStartDate.getTime() ;

        if(wasSuccessful)
            successfulIntent = 1;
        else
            successfulIntent = 0;

        response = kidResponse;
        priceValue = priceVal;

        // level, price Nr, price val, response, responseBlocks, response blocks number, intents nr, intent result (1 or 0),
        // intent start, intent end, difference in millis
        // id
        toSave = levelNumber+","+priceNumber+","+priceValue+","
                +response+","+formatResponseBlocks(responseBlocks)+","+responseBlocks.size()+","
                +intentsNr+","+successfulIntent+","
                +sdf.format(intentStartDate)+","+justTime.format(intentEndDate)+","+collectionTimeMillis+","
                +randomId+"\n";
        Gdx.app.log(TAG,"save data "+toSave);
        intentsFile.writeString(toSave,true);

        // now we reset the start
        intentStartDate = intentEndDate;
        lastSolutionNr = kidResponse;

    }

    private String formatResponseBlocks(ArrayList<Integer> responseBlocks){
        String responseAsString = "";
        for (int i=0;i<responseBlocks.size();i++){
            responseAsString+=responseBlocks.get(i)+" ";
        }
        return responseAsString;
    }

}

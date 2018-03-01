package miceta.game.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import miceta.game.core.screens.AbstractGameScreen;
import miceta.game.core.util.GamePreferences;
import miceta.game.core.util.LevelParams;
import miceta.game.core.util.RepresentationMapper;
import miceta.game.core.util.ScreenName;

import java.util.ArrayList;
import java.util.Scanner;
 //
public class LevelsManager {
     public static final String TAG = LevelsManager.class.getName();
     public static LevelsManager instance = new LevelsManager(); //Singleton
     private String[] csvLines;
     private LevelParams currentLevelParams;
     private int level;
     private int operation_index;
     private int level_tope;

    //Singleton
    public static LevelsManager getInstance() {
        return instance;
    }

     public void init(){
         level = GamePreferences.instance.getLast_level();
         System.out.println("LEVEL " + level);
         //operation_index = GamePreferences.instance.getOperation_index();
         operation_index =0; // for now we start from the beginning
         load_csv(); // load all levels
         currentLevelParams = loadLevelParams(level);

     }

    private void load_csv(){
        FileHandle levelsFile = Gdx.files.internal("levels/world_1.csv");
        String csvContentString = levelsFile.readString();
        csvLines = csvContentString.split("\n");
        level_tope = csvLines.length-1; // -1 because of the headers
        Gdx.app.log(TAG,"csvContentString "+csvContentString + " tope "+level_tope);
//        for (int i=0;i<csvLines.length;i++){
//            Gdx.app.log(TAG, "line "+i+" -- "+csvLines[i]);
//        }
    }

    private LevelParams loadLevelParams(int levelNr){

        LevelParams levelParams = new LevelParams();
        String line = csvLines[levelNr];//0 for titles
        String [] splittedLine = line.split(",");

        levelParams.screenName = RepresentationMapper.getScreenNameFromRepresentation(Integer.parseInt(splittedLine[4]));
        levelParams.numberMin = Integer.parseInt(splittedLine[0]);
        levelParams.numberMax = Integer.parseInt(splittedLine[1]);
        levelParams.operationsToFinishLevel = Integer.parseInt(splittedLine[2]);

        int [] operations;
        if(splittedLine[3].length() > 0){
            String operationsStr = splittedLine[3];
            String[] operationsStrSplit = operationsStr.split(" ");
            levelParams.operationsToFinishLevel = operationsStrSplit.length; // if we have array, we check how long it is!
            operations = new int[operationsStrSplit.length];
            for(int i=0;i<operationsStrSplit.length;i++){
                if(operationsStrSplit[i]!="")
                    operations[i]= Integer.parseInt(operationsStrSplit[i]);
            }
        }else{
            operations = new int[levelParams.operationsToFinishLevel];
            int previous = 0;
            int now = MathUtils.random(levelParams.numberMin,levelParams.numberMax);
            for(int i =0; i< levelParams.operationsToFinishLevel;i++){
                while(previous == now) {
                    now = MathUtils.random(levelParams.numberMin, levelParams.numberMax);
                }
                operations[i] = now;
                previous = now;
            }
        }

        levelParams.operations = operations;
        operation_index = 0;

        return levelParams;
    }

     public void forceLevelParams(int forcedLevelNr){
         Gdx.app.log(TAG," -- forceLevelParams ---> "+forcedLevelNr);
         //level = forcedLevelNr;
         currentLevelParams = loadLevelParams(forcedLevelNr);
     }

    public void upLevelAndLoadParams(){
        if (level < level_tope) {
            operation_index =0;
            level++;
        }
        else{
            operation_index =0;
            level = 1; //reinicio los niveles
        }
        GamePreferences.instance.setLast_level(level);
        GamePreferences.instance.setOperation_index(operation_index);
        GamePreferences.instance.save();
        currentLevelParams = loadLevelParams(level);
    }


     public void LevelAndLoadParams(){

         GamePreferences.instance.setLast_level(level);
         GamePreferences.instance.setOperation_index(operation_index);
         GamePreferences.instance.save();
         currentLevelParams = loadLevelParams(level);
     }




    public int get_number_to_play(){
        return currentLevelParams.operations[operation_index];
    }

    public boolean up_operation_index(){
        operation_index ++;
        GamePreferences.instance.setLast_level(level);
        GamePreferences.instance.setOperation_index(operation_index);
        GamePreferences.instance.save();

        if (operation_index >= currentLevelParams.operationsToFinishLevel){ //Ojo con Esto! puede que sea mejor el ==
//            upLevelAndLoadParams();
            return true;
        }
        return false;
    }



     public int get_level_size(){
        return level_tope;
    }

     public int get_level(){
        return level;
    }

     public void forceLevel(int forcedLevel){level = forcedLevel;}

     public int get_operation_index(){
        return operation_index;
    }

     public ScreenName getScreenName(){return currentLevelParams.screenName;}

     public int[] getOperations(){return currentLevelParams.operations;}
     public int getOperationsNumberToFinishLevel(){ return currentLevelParams.operationsToFinishLevel;}


}
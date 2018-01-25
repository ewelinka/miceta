package miceta.game.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import miceta.game.core.util.GamePreferences;

import java.util.ArrayList;
import java.util.Scanner;
//
public class LevelsManager {
    public static final String TAG = LevelsManager.class.getName();

    private int level;
    private int operation_index =0;
    private int level_tope=0;
    private String[][] subLines;
    private int index =0;
    private ArrayList<Integer> list_to_play = new ArrayList<Integer>();
    private Integer quantity_operation =0;
    public static LevelsManager instance = null; //Singleton



    //Singleton
    public static LevelsManager getInstance() {
        if (instance == null){
            instance = new LevelsManager();
        }
        return instance;
    }

    private LevelsManager(){

        // level = GamePreferences.instance.getLast_level();
        level = 11;
        System.out.println("LEVEL " + level);
        //operation_index = GamePreferences.instance.getOperation_index();
        operation_index =0;
        load_csv();
        get_List_of_numbers(level);
    }

    private void load_csv(){

        //FileHandle handle = Gdx.files.internal("levels/ejemploNiveles.csv");

        switch(level) {
            case 0:
                FileHandle handle = Gdx.files.internal("levels/world_1.csv");
                break;

        }

        FileHandle handle = Gdx.files.internal("levels/world_1.csv");

        String s_level = handle.readString();
        String[] lines;
        lines = s_level.split("\n");

        index = 0;
        level_tope = lines.length -1;

        if (level > level_tope){
            level =0;
        }

        Scanner scanner = new Scanner(lines[0]);

        while ((scanner.hasNext())) {
            index = index +1;
            scanner.next();
        }

        subLines = new String[level_tope][index];
        index =0;

        for (int i = 0; i < level_tope; i++) {
            scanner = new Scanner(lines[i+1]);
            scanner.useDelimiter("\\r");

            while ((scanner.hasNext())) {
                String text_aux = scanner.next();
                subLines[i][index] = text_aux;
                System.out.println("[ i -> " + i + "- index -> " + index + " sub[i][index]-> " + subLines[i][index] + "-]");
            }
            scanner.close();
        }

        for (int j = 0; j < level_tope;j++){
            index = 0;
            Scanner scanner_2 = new Scanner(subLines[j][index]);
            scanner_2.useDelimiter(";");
            while ((scanner_2.hasNext())&&(index<5)) {
                String text =  scanner_2.next();
                subLines[j][index] = text;
                // System.out.println("[j-> " + j + "- index -> " + index + " sub[j][index]->" + subLines[j][index] + "-]");
                index++;
            }
            scanner_2.close();
        }
    }


    public void get_List_of_numbers(int level){
        quantity_operation = (new Integer (subLines[level][3]).intValue());
        list_to_play.clear();
        Integer number  = 0;

        if (level == level_tope) {
            level = 0; //reinicio los niveles
        }
        try {
            Scanner scanner = new Scanner(subLines[level][4]);
            while ((scanner.hasNext())) {

                number = (new Integer(scanner.next()).intValue());
                Gdx.app.log(TAG,"Number to play :" + number+" level "+level);
                list_to_play.add(number);
                System.out.println(number.toString());
            }

        }
        catch (java.lang.NullPointerException e) {
            Integer tope = (new Integer(subLines[level][3]).intValue());
            Gdx.app.log(TAG,"=== Numero tope es " + tope+" level "+level);
            int rand =0;
            int previous_rand = 0;

            for (int i=0; i < tope; i++){

                Integer min = (new Integer(subLines[level][1]).intValue());
                Integer max = (new Integer(subLines[level][2]).intValue());

                if (min == 0){ //esto es es caso que el excel tenga valor 0
                    min =1;
                }

                while ((rand == previous_rand)) {
                    rand = MathUtils.random(min, max);
                }

                previous_rand = rand;
                list_to_play.add(rand);
                Gdx.app.log(TAG,"LEVEL" + level + " index "+ i+" RAND " + rand);

            }
            operation_index =0;
        }

    }

    public void up_level(){
        if (level < level_tope -1) {
            operation_index =0;
            level++;
        }
        else{
            operation_index =0;
            level = 0; //reinicio los niveles
        }
        get_List_of_numbers(level);
    }

    public int get_number_to_play(){
        return list_to_play.get(operation_index);
    }

    public void up_operation_index(){
        operation_index ++;

        if (operation_index >= quantity_operation){ //Ojo con Esto! puede que sea mejor el ==
            Gdx.app.log(TAG, "all operations ready! "+operation_index);
            up_level();

        }
    }
    public int get_quantity_operation(){
        return quantity_operation;
    }

    public int get_level_size(){
        return level_tope;
    }

    public int get_level(){
        return level;
    }

    public int get_operation_index(){
        return operation_index;
    }





}
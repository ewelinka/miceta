package miceta.game.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import miceta.game.core.util.GamePreferences;

import java.util.ArrayList;
import java.util.Scanner;
 //
public class LevelsManager {

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

        level = GamePreferences.instance.getLast_level();

        System.out.println("LEVEL " + level);
        operation_index = GamePreferences.instance.getOperation_index();
        load_csv();
    }

    private void load_csv(){

        FileHandle handle = Gdx.files.internal("levels/ejemploNiveles.csv");
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
                System.out.println("[" + i + "-" + index + " " + subLines[i][index] + "-]");
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
                System.out.println("[" + j + "-" + index + " " + subLines[j][index] + "-]");
                index++;
            }

            scanner_2.close();
        }
        get_List_of_numbers(level);

    }






    public void get_List_of_numbers(int level){

        Integer number  = 0;

        if (level == level_tope) {
            level = 0; //reinicio los niveles
        }
        try {
            Scanner scanner = new Scanner(subLines[level][4]);
            list_to_play.clear();

            while ((scanner.hasNext())) {

                System.out.println("Number es :" + number);

                number = (new Integer(scanner.next()).intValue());
                list_to_play.add(number);
                System.out.println(number.toString());
            }

        }

        catch (java.lang.NullPointerException e) {

            Integer tope = (new Integer(subLines[level][3]).intValue());
            System.out.println("Tope es" + tope);

            int rand =0;
            int previus_rand = 0;

            for (int i=0; i < tope; i++){

                Integer min = (new Integer(subLines[level][1]).intValue());
                Integer max = (new Integer(subLines[level][2]).intValue());

                if (min == 0){ //esto es es caso que el excel tenga valor 0
                    min =1;
                }

                while ((rand == previus_rand)) {
                    rand = MathUtils.random(min, max);
                }

                previus_rand = rand;
                // list_to_play.clear();
                list_to_play.add(rand);
                System.out.println("LEVEL" + level + "RAND " + rand);
                System.out.flush();
            }
            operation_index =0;
        }
    }

    public void up_level(){

        System.out.println("level tope esss " + level_tope);

        if (level < level_tope -1) {
            list_to_play.clear();
            operation_index =0;
            level++;
        }
        else{
            operation_index =0;
            level = 0; //reinicio los niveles
        }
    }

    public int get_number_to_play(){

        System.out.println("LISTA " + list_to_play.size());
        return list_to_play.get(operation_index);
    }

    public void up_operation_index(){ //REVISAR ESTA FUNCION

        operation_index ++;
        System.out.println("========================================== Level: " + level);

        System.out.println("========================================== Operation Index: " + operation_index);

        quantity_operation = (new Integer (subLines[level][3]).intValue());

        System.out.println("========================================== quantity_operation " + quantity_operation);

        if (operation_index == quantity_operation){ //Ojo con Esto! puede que sea mejor el ==

            System.out.println("operation_index");
            up_level();
            operation_index = 0;
            get_List_of_numbers(level);
        }

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
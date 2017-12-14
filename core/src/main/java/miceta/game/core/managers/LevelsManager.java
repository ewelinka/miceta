package miceta.game.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelsManager {


    private int level;
    private int operation_index =0;
    private int level_tope=0;
    private String[] Lines;
    private String[][] subLines;
    private int index =0;
    private ArrayList<Integer> list_to_play = new ArrayList<Integer>();
    private Integer quantity_operation =0;


    public LevelsManager(){

        level = 1;
        operation_index = 0;
        FileHandle handle = Gdx.files.internal("levels/ejemploNiveles.csv");
        String s_level = handle.readString();
        Lines = s_level.split("\n");
        subLines = new String[10][6];
        boolean aux = true;
        index = 0;
        level_tope = Lines.length;

        for (int i = 1; i < Lines.length ; i++) {

            Scanner scanner = new Scanner(Lines[i]);
            scanner.useDelimiter("\\r");

            while ((scanner.hasNext())) {

                    String text_aux = scanner.next();
                    subLines[i][index] = text_aux;
                    System.out.println("[" + i + "-" + index + " " + subLines[i][index] + "-]");
                }

            scanner.close();
        }

        for (int j = 1; j < Lines.length ;j++){

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

            Integer a  = 0;

            if (level == level_tope) {
                level = 1; //reinicio los niveles
            }
            try {
                Scanner scanner = new Scanner(subLines[level][4]);
                list_to_play.clear();

                while ((scanner.hasNext())) {

                    a = (new Integer(scanner.next()).intValue());
                    list_to_play.add(a);
                    System.out.println(a.toString());
                }

            }

                catch (java.lang.NullPointerException e) {
                Integer tope = (new Integer(subLines[level][3]).intValue());

                for (int i=0; i < tope; i++){

                    Integer min = (new Integer(subLines[level][1]).intValue());
                    Integer max = (new Integer(subLines[level][2]).intValue());

                    if (min == 0){ //esto es es caso que el excel tenga valor 0
                        min =1;
                    }

                    int rand = MathUtils.random(min,max);
                    list_to_play.add(rand);
                    System.out.flush();
                }
            }
    }

    public void up_level(){

        if (level < level_tope) {
        level++;
        }
        else{
            level = 1; //reinicio los niveles
        }
    }

    public int get_number_to_play(){

        return list_to_play.get(operation_index);
    }

    public void up_operation_index(){

        operation_index ++;

        System.out.println("Operation Index: " + operation_index);

        quantity_operation = (new Integer (subLines[level][3]).intValue());

        if (operation_index == quantity_operation){

            System.out.println("operation_index");
            up_level();
            operation_index = 0;
            get_List_of_numbers(level);
        }

    }

   public int get_level_size(){

        return level_tope;
    }



}

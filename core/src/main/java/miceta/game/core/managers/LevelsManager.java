package miceta.game.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


public class LevelsManager {

    public static final String TAG = "prueba";
    private int actual_level;
    private int price;
    private int operation_index;
    private int price_min;
    private int price_max;


    public LevelsManager(){

        actual_level = 1;
        operation_index =1;
        Gdx.app.log(TAG,"====================================================================");
    }

    public void up_level(){

        actual_level = actual_level +1;
    }



    public int get_number_to_play(){

        return 1;
    }






}

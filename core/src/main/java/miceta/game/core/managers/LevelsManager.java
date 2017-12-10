package miceta.game.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import static miceta.game.core.managers.CvBlocksManager.TAG;

public class LevelsManager {


    private int actual_level;
    private int price;
    private int operation_index;
    private int price_min;
    private int price_max;


    public LevelsManager(){

        actual_level = 1;
        operation_index =1;
        FileHandle handle = Gdx.files.internal("src/ejemploNiveles.csv");

        Gdx.app.log(TAG,"LOAD MANAGER IS OK: " + handle.toString()
        );


    }

    public void up_level(){

        actual_level = actual_level +1;
    }



    public int get_number_to_play(){

        return 1;
    }






}

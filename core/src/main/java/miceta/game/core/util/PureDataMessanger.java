package miceta.game.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import net.mgsx.pd.Pd;
import net.mgsx.pd.PdConfiguration;
import net.mgsx.pd.patch.PdPatch;

import java.util.Arrays;

/**
 * Created by ewe on 10/1/18.
 */
public class PureDataMessanger {

    private static final String TAG = PureDataMessanger.class.getName();
    public static final PureDataMessanger instance = new PureDataMessanger(); //Singleton
    PdPatch patch;
    public static PdConfiguration config;

    public void init(){
      //  patch = Pd.audio.open(Gdx.files.internal("pd/SendList.pd"));
//        Gdx.app.log(TAG, " --------- qoqoqo ");
//        FileHandle[] files = Gdx.files.internal("pd/").list();
//        for(FileHandle file: files) {
//            Gdx.app.log(TAG, "qoqoqo "+file);
//            // do something interesting here
//        }
       // patch = Pd.audio.open(Gdx.files.internal("pd/SendList.pd"));
        //patch = Pd.audio.open(Gdx.files.absolute("/Users/ewe/AndroidStudioProjects/Logarin-pd/assets/pd/SendList.pd"));
        config = new PdConfiguration();
        Pd.audio.create(config);

        //patch = Pd.audio.open(Gdx.files.absolute("C:\\Users\\Alumno Ceibal\\Desktop\\Logarin\\pd\\SendList.pd"));
        patch = Pd.audio.open(Gdx.files.absolute("/Users/ewe/AndroidStudioProjects/Logarin-pd/assets/pd/SendList.pd"));



    }
    public void testSendList(String whichFeedback, int numberToBuild, int numberOfBlocks, int delayBetweenFeedbacksInMillis, final Object... blocksValues){
        Pd.audio.sendList(whichFeedback, blocksValues);
    }

    public void sendMessage(String whichFeedback, Object... blocksValues){

        //Object[] toSend = new Object[]  { 1,3,2,300,1,3 };
        Pd.audio.sendList("recv_list", blocksValues);
    }

    public void sendMessageMatias(String whichFeedback, Object... blocksValues){


       // Object[] toSend = new Object[]  { 300, 3, 4, 1, 3 };
        Pd.audio.sendList("timbre", blocksValues);
    }

    public void dispose(){
        Pd.audio.dispose();
    }

}

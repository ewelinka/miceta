package miceta.game.core.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import miceta.game.core.Assets;
import miceta.game.core.controllers.CvWorldController;
import miceta.game.core.miCeta;
import miceta.game.core.util.Constants;

/**
 * Created by ewe on 8/10/17.
 */
public abstract class AbstractGameScreen  implements Screen {
    public static final String TAG = AbstractGameScreen.class.getName();
    protected miCeta game;
    protected Stage stage;
    protected CvWorldController worldController;
    protected int levelJson;
    protected boolean paused;
    protected int viewportWidth, viewportHeight;


    public AbstractGameScreen (miCeta game){
        this(game,0);
    }

    public AbstractGameScreen (miCeta game, int levelJson){
        this.game = game;
        this.levelJson = levelJson;
        paused = false;

        if(Gdx.app.getType() == Application.ApplicationType.Android){
            viewportWidth = Constants.ANDROID_WIDTH;
            viewportHeight = Constants.ANDROID_HEIGHT;
        }else{
            viewportWidth = Constants.DESKTOP_WIDTH;
            viewportHeight = Constants.DESKTOP_HEIGHT;
        }
    }


    public abstract void render (float deltaTime);
    public abstract void resize (int width, int height);
    public abstract void show ();
    public abstract void hide ();
    public abstract void pause ();

    public void resume () {
        Gdx.app.log(TAG,"== resume assets instance");
        //Assets.instance.init(new AssetManager());
    }
    public void dispose () {
        Gdx.app.log(TAG,"== dispose assets instance");
        Assets.instance.dispose();
    }
    public abstract InputProcessor getInputProcessor ();



}



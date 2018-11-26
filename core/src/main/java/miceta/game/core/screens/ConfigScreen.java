package miceta.game.core.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import miceta.game.core.Assets;
import miceta.game.core.controllers.CvWithIntroController;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.transitions.ScreenTransition;
import miceta.game.core.transitions.ScreenTransitionFade;
import miceta.game.core.util.AudioManager;
import miceta.game.core.util.GamePreferences;
import miceta.game.core.util.ScreenName;

/**
 * Created by ewe on 1/5/18.
 */
public class ConfigScreen extends AbstractGameScreen {
    private static final String TAG = ConfigScreen.class.getName();
    private ScreenName currentSelectedScreen;
    private ScreenName [] allScreens;
    private ImageButton btnPlay, btnExit, btnNewStart, btnHelp;
    private final BitmapFont font = new BitmapFont();
	private int silentFeedbackMode, mixedFeedbackMode,errorOrSuccessBlocksFeedbackMode;

    public ConfigScreen(miCeta game) {
        super(game,true);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0,0,0,0);
        //Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();
        GamePreferences.instance.load();
        silentFeedbackMode = GamePreferences.instance.getSilentFeedbackMode();
        mixedFeedbackMode = GamePreferences.instance.getMixedFeedbackMode();
        errorOrSuccessBlocksFeedbackMode = GamePreferences.instance.getErrorOrSuccessBlocksFeedbackMode();
        drawSilentOption();
        drawMixedFeedbackOption();
        drawErrorOrSuccessBlocksFeedbackMode();
    }
    
    private void drawSilentOption(){
    	spriteBatch.begin();
        font.setScale(2, 2);
        font.draw(spriteBatch,"Silent Feedback: ",100,700);
        font.draw(spriteBatch,silentFeedbackMode==1?"YES":"NO",350,700);
        spriteBatch.end();
    }
    
    private void drawMixedFeedbackOption(){
    	spriteBatch.begin();
        font.setScale(2, 2);
        font.draw(spriteBatch,"Mixed Feedback: ",100,500);
        font.draw(spriteBatch,mixedFeedbackMode==1?"YES":"NO",350,500);
        spriteBatch.end();
    }
    
    
    private void drawErrorOrSuccessBlocksFeedbackMode(){
    	spriteBatch.begin();
        font.setScale(2, 2);
        font.draw(spriteBatch,"Err/Succ Feedback: ",100,300);
        font.draw(spriteBatch,errorOrSuccessBlocksFeedbackMode==1?"YES":"NO",350,300);
        spriteBatch.end();
    }
    
    

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        spriteBatch  = new SpriteBatch();
        super.addButtons(true);
        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);
    }

    
    
    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        Gdx.app.log(TAG, " CONFIG TOUCHED  down" + screenX + " " + screenY);
        if ((screenX > 352 && screenX < 392)&& (screenY > 66 && screenY < 87)) {
            toogleSilentMode();
        }else if((screenX > 352 && screenX < 392)&& (screenY > 265 && screenY < 292)) {
        	toogleMixedFeedbackMode();
        }else if((screenX > 352 && screenX < 392)&& (screenY > 465 && screenY < 492)) {
        	toogleErrorOrSuccessFeedbackMode();
        }
        if(worldController != null) {
            Gdx.app.log(TAG, " TOUCHED " + screenX + " " + screenY);
            if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                worldController.touchDownDesktop(screenX, screenY, button);
            }
        }
        return true;
    }
    
    private void toogleSilentMode(){
        silentFeedbackMode = (silentFeedbackMode+1)%2;
        game.getOscManager().setSilentFeedbackMode(silentFeedbackMode==1);
        game.getOscManager().send_silent_mode_feedback();
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        GamePreferences.instance.setSilentFeedbackMode(silentFeedbackMode);
        prefs.save();
    }
    
    private void toogleMixedFeedbackMode(){
        mixedFeedbackMode = (mixedFeedbackMode+1)%2;
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        GamePreferences.instance.setMixedFeedbackMode(mixedFeedbackMode);
        prefs.save();
    }
    
    private void toogleErrorOrSuccessFeedbackMode(){
    	errorOrSuccessBlocksFeedbackMode = (errorOrSuccessBlocksFeedbackMode+1)%2;
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        GamePreferences.instance.setErrorOrSuccessBlocksFeedbackMode(errorOrSuccessBlocksFeedbackMode);
        prefs.save();
    }
    
        
    @Override
    public boolean keyDown(int keycode){
        switch (keycode)
        {
            case Input.Keys.S:
                stopCurrentSound();
                game.setScreen(new MenuScreen(game));
                break;
        }
        return true;
    }
    
    /*
     * @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        Gdx.app.log(TAG, " TOUCHED  down" + screenX + " " + screenY);
        if (button == Input.Buttons.RIGHT){
            stopCurrentSound();
            game.setScreen(new IntroScreen(game));
        }
        else {
            if(worldController != null) {
                Gdx.app.log(TAG, " TOUCHED " + screenX + " " + screenY);
                if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                    worldController.touchDownDesktop(screenX, screenY, button);
                }
            }
        }
        return true;
    }
     * */

}

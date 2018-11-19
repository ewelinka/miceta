package miceta.game.core.screens;

import com.badlogic.gdx.*;
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
import miceta.game.core.controllers.CvWorldController;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.transitions.ScreenTransition;
import miceta.game.core.transitions.ScreenTransitionFade;
import miceta.game.core.util.*;

/**
 * Created by ewe on 8/10/17.
 */
public abstract class AbstractGameScreen  extends InputAdapter implements Screen {
    private static final String TAG = AbstractGameScreen.class.getName();
    final miCeta game;
    Stage stage;
    CvWorldController worldController;
    SpriteBatch spriteBatch;
    ShapeRenderer shapeRenderer;
    FeedbackDrawManager fd;
    private ScreenName currentSelectedScreen;
    boolean paused;
    final int viewportWidth;
    final int viewportHeight;

    final boolean upLevel;
    final boolean shouldRepeatTutorial;
    final boolean inIntro;

    AbstractGameScreen(miCeta game){
        this(game,false,false, false);
    }
    AbstractGameScreen(miCeta game, boolean inIntro){
        this(game,false,false, inIntro);
    }

    AbstractGameScreen(miCeta game, boolean upLevel, boolean shouldRepeatTutorial, boolean inIntro){
        this.game = game;
        this.upLevel = upLevel;
        this.shouldRepeatTutorial = shouldRepeatTutorial;
        this.inIntro = inIntro;
        this.game.gameScreen = game.updateGameScreen();
        paused = false;

        if(Gdx.app.getType() == Application.ApplicationType.Android){
            viewportWidth = Constants.ANDROID_WIDTH;
            viewportHeight = Constants.ANDROID_HEIGHT;
        }else{
            viewportWidth = Constants.DESKTOP_WIDTH;
            viewportHeight = Constants.DESKTOP_HEIGHT;
        }
        currentSelectedScreen = ScreenName.NONE;
    }

    public abstract void render (float deltaTime);


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        dispose();

    }

    @Override
    public void pause() {
        paused =true;

    }

    @Override
    public void resume () {
        // Only called on Android!
        paused = false;
    }

    @Override
    public void dispose(){
        stage.dispose();
    }

    public InputProcessor getInputProcessor(){

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);

        return multiplexer;
    }

    protected void initRenderRelatedStuff(){
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        fd = new FeedbackDrawManager();
        addButtons(false);
        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);
    }


    @Override
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
    @Override
    public boolean keyDown(int keycode){
        switch (keycode)
        {
            case Input.Keys.ESCAPE:
                stopCurrentSound();
                game.setScreen(new MenuScreen(game));
                break;
            case Input.Keys.F1:
                stopCurrentSound();
                LevelsManager.instance.forceLevelParams(1);
                game.setScreen(new ConcreteTutorial(game, false, false));
                break;
            case Input.Keys.LEFT:
                AudioManager.instance.downFeedbackVolSound(); // blocks volume
                break;
            case Input.Keys.RIGHT:
                AudioManager.instance.upFeedbackVolSound(); // blocks volume
                break;
            case Input.Keys.UP:
                //AudioManager.instance.upKnockNoteVol();
                goToPreviouseMenuItem();
                break;
            case Input.Keys.DOWN:
               // AudioManager.instance.downKnockNoteVol();
                goToNextMenuItem();
                break;
            case Input.Keys.ENTER:
                enterPressed();
                break;
            case Input.Keys.Q:
                if(worldController!= null){ //in concrete tutorial there is no worldController
                    stopCurrentSound();
                    worldController.forceScreenFinish();
                }
                break;

        }
        return true;
    }

    protected void setGameScreenTo(ScreenName name){
        this.game.gameScreen = RepresentationMapper.getGameScreenFromScreenName(name);
    }


    void stopCurrentSound(){

        AudioManager.instance.stop_sounds(this.game.getGameScreen().screenName);
    }

    protected void addButtons(boolean showPlay){
        int x = 1100;
        int shift = 20+100;
        int initialShift = 160;
        addBtnExit(x, initialShift ); // last btn
        addBtnHelp(x, initialShift + shift*1 );
        addBtnNewStart(x , initialShift +shift*2);
        if(showPlay){
            addBtnPlay(x, initialShift +shift*3 ); // top button
            addBtnConfig(x, initialShift +shift*4 );
        }else{
        	addBtnConfig(x, initialShift +shift*3 );
        }
    }

    private void addBtnPlay(int x, int y){
        ImageButton btnPlay = new ImageButton(Assets.instance.buttons.playButtonStyle);
        btnPlay.setPosition(x,y);
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked(ScreenName.LAST_SCREEN);
            }
        });
        btnPlay.addListener(new InputListener(){
            @Override
            public  void    enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                currentSelectedScreen = ScreenName.LAST_SCREEN;
                if(pointer == -1) { // if not, on btn click the audio file is played again (without any need)
                    AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.jugar);
                }
            }
        });
        stage.addActor(btnPlay);
    }

    private void addBtnConfig(int x, int y){
        ImageButton btnConfig = new ImageButton(Assets.instance.buttons.configButtonStyle);
        btnConfig.setPosition(x,y);
        btnConfig.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked(ScreenName.CONFIG);
            }
        });
        btnConfig.addListener(new InputListener(){
            @Override
            public  void    enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                currentSelectedScreen = ScreenName.CONFIG;
                if(pointer == -1) { // if not, on btn click the audio file is played again (without any need)
                    //AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.jugar);
                }
            }
        });
        stage.addActor(btnConfig);
    }
    
    
    private void addBtnNewStart(int x, int y){
        ImageButton btnNewStart = new ImageButton(Assets.instance.buttons.newStartButtonStyle);
        btnNewStart.setPosition(x,y);
        btnNewStart.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked(ScreenName.RESTART);
            }
        });
        btnNewStart.addListener(new InputListener(){
            @Override
            public  void    enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                currentSelectedScreen = ScreenName.RESTART;
                if(pointer == -1) { // if not, on btn click the audio file is played again (without any need)
                    AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.denuevo); // TODO change to "JUGAR" audio whan available
                }
            }
        });
        stage.addActor(btnNewStart);
    }
    private void addBtnHelp(int x, int y){
        ImageButton btnHelp = new ImageButton(Assets.instance.buttons.helpButtonStyle);
        btnHelp.setPosition(x,y);
        btnHelp.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked(ScreenName.CONCRETE_TUTORIAL);
            }
        });
        btnHelp.addListener(new InputListener(){
            @Override
            public  void    enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                currentSelectedScreen = ScreenName.CONCRETE_TUTORIAL;
                if(pointer == -1) { // if not, on btn click the audio file is played again (without any need)
                    AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.ayuda); // TODO change to "JUGAR" audio whan available
                }
            }
        });
        stage.addActor(btnHelp);
    }
    private void addBtnExit(int x, int y){
        ImageButton btnExit = new ImageButton(Assets.instance.buttons.exitButtonStyle);
        btnExit.setPosition(x,y);
        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked(ScreenName.EXIT);
            }
        });

        btnExit.addListener(new InputListener(){
            @Override
            public  void    enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                currentSelectedScreen = ScreenName.EXIT;
                if(pointer == -1) { // if not, on btn click the audio file is played again (without any need)
                    AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.salir); // TODO change to "JUGAR" audio whan available
                }
            }
        });
        stage.addActor(btnExit);
    }
    private void onBtnClicked(ScreenName screenName) {
        Gdx.app.log(TAG,"onBtnClicked ----> "+screenName);
        stopCurrentSound();
        ScreenTransition transition = ScreenTransitionFade.init(1);
        switch(screenName){
            case LAST_SCREEN:
                LevelsManager.instance.loadLevelParams();
                AbstractGameScreen nowScreen = game.getRepresentationMapper().getScreenFromScreenName(LevelsManager.instance.getScreenName(), false);
                game.setScreen(nowScreen);
                break;
            case EXIT:
                Gdx.app.exit();
                break;
            case RESTART:
                GamePreferences.instance.setLast_level(1);
                LevelsManager.instance.forceLevelAndLevelParams(1);
                game.setScreen(new ConcreteTutorial(game, true, false),transition);// restart should do upLevel!
                break;
            case CONCRETE_TUTORIAL:
                LevelsManager.instance.forceLevelParams(1);
                game.setScreen(new ConcreteTutorial(game, false, false),transition);
                break;
            case CONFIG:
                //LevelsManager.instance.forceLevelParams(1);
                game.setScreen(new ConfigScreen(game),transition);
                break;
        }
    }


    private void goToPreviouseMenuItem(){
        switch(currentSelectedScreen) {
            case NONE:
                if(inIntro){
                    setPlayToCurrent();
                }else{
                    setRestartToCurrent();
                }
                break;
            case LAST_SCREEN:
                setExitToCurrent();
                break;
            case CONCRETE_TUTORIAL:
                setRestartToCurrent();
                break;
            case EXIT:
                setHelpToCurrent();
                break;
            case RESTART:
                if(inIntro){
                    setPlayToCurrent();
                }else{
                    setExitToCurrent();
                }
                break;
            case CONFIG:
                setConfigToCurrent();
                break;
        }

    }
    private void goToNextMenuItem(){
        switch(currentSelectedScreen){
            case NONE:
                if(inIntro){
                    setPlayToCurrent();
                }else{
                    setRestartToCurrent();
                }
                break;
            case LAST_SCREEN:
                setRestartToCurrent();
                break;
            case RESTART:
                setHelpToCurrent();
                break;
            case CONCRETE_TUTORIAL:
                setExitToCurrent();
                break;
            case EXIT:
                if(inIntro){
                    setPlayToCurrent();
                }else{
                    setRestartToCurrent();
                }
                break;

        }

    }

    private void setExitToCurrent(){
        currentSelectedScreen = ScreenName.EXIT;
        AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.salir);
    }
    private void setRestartToCurrent(){
        currentSelectedScreen = ScreenName.RESTART;
        AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.denuevo);

    }
    private void setHelpToCurrent(){
        currentSelectedScreen = ScreenName.CONCRETE_TUTORIAL;
        AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.ayuda);
    }
    private void setPlayToCurrent(){
        currentSelectedScreen = ScreenName.LAST_SCREEN;
        AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.jugar);
    }
    private void setConfigToCurrent(){
        currentSelectedScreen = ScreenName.CONFIG;
        //AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.jugar);
    }


    private void enterPressed(){
        onBtnClicked(currentSelectedScreen);
    }
}



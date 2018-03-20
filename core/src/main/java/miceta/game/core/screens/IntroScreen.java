package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.Assets;
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
public class IntroScreen extends AbstractGameScreen {
    private static final String TAG = IntroScreen.class.getName();
    private ScreenName currentSelectedScreen;
    private ScreenName [] allScreens;
    private ImageButton btnPlay, btnExit, btnNewStart, btnHelp;

    public IntroScreen(miCeta game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(1, 1, 1, 0.5f);
        //Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();
    }


    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth , viewportHeight));
        Gdx.input.setCatchBackKey(false);
        // btn 300 x 150, screen 1366 x 768
        addBtnExit(viewportWidth/2 - 300/2, 30 ); // last btn
        addBtnHelp(viewportWidth/2 - 300/2, 30 + (30 + 150)*1 );
        addBtnNewStart(viewportWidth/2 - 300/2, 30 +(30 + 150)*2);
        addBtnPlay(viewportWidth/2 - 300/2, 30 +(30 + 150)*3 ); // top button
        currentSelectedScreen = ScreenName.LAST_SCREEN;
        allScreens = new ScreenName[]{ScreenName.LAST_SCREEN, ScreenName.RESTART, ScreenName.CONCRETE_TUTORIAL, ScreenName.EXIT};
    }


    private void addBtnPlay(int x, int y){
        btnPlay = new ImageButton(Assets.instance.buttons.playButtonStyle);
        btnPlay.setPosition(0,y);
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
                    AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.jugar); // TODO change to "JUGAR" audio whan available
                }
            }
        });
        btnPlay.setWidth(1366);
        btnPlay.setHeight(180);
        stage.addActor(btnPlay);
    }
    private void addBtnExit(int x, int y){
        btnExit = new ImageButton(Assets.instance.buttons.exitButtonStyle);
        btnExit.setPosition(0,y);
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

        btnExit.setWidth(1366);
        btnExit.setHeight(180);
        stage.addActor(btnExit);
    }
    private void addBtnNewStart(int x, int y){
        btnNewStart = new ImageButton(Assets.instance.buttons.newStartButtonStyle);
        btnNewStart.setPosition(0,y);
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
        btnNewStart.setWidth(1366);
        btnNewStart.setHeight(180);
        stage.addActor(btnNewStart);
    }
    private void addBtnHelp(int x, int y){
        btnHelp = new ImageButton(Assets.instance.buttons.helpButtonStyle);
        btnHelp.setPosition(0,y);
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

        btnHelp.setWidth(1366);
        btnHelp.setHeight(180);
        stage.addActor(btnHelp);
    }


    private void onBtnClicked(ScreenName screenName) {
        Gdx.app.log(TAG,"onBtnClicked ----> "+screenName);
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
                LevelsManager.instance.forceToFirstLevel();
                LevelsManager.instance.forceLevelParams(1);
                game.setScreen(new ConcreteTutorial(game,0,0, true, false),transition);// restart should do upLevel!
                break;
            case CONCRETE_TUTORIAL:
                LevelsManager.instance.forceLevelParams(1);
                game.setScreen(new ConcreteTutorial(game,0, 0,false, false));
                break;
        }
    }

    @Override
    public boolean keyDown(int keycode){
        switch (keycode){
            case Input.Keys.F1:
                stopCurrentSound();
                LevelsManager.instance.forceLevelParams(1);
                game.setScreen(new ConcreteTutorial(game,0, 0,false, false));
                break;
            case Input.Keys.UP:
                goToPreviouseMenuItem();
                break;
            case Input.Keys.DOWN:
                goToNextMenuItem();
                break;
            case Input.Keys.ENTER:
                enterPressed();
                break;
        }
        return true;
    }

    private void goToPreviouseMenuItem(){
        switch(currentSelectedScreen) {
            case LAST_SCREEN:
                currentSelectedScreen = ScreenName.EXIT;
                AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.salir);
                break;
            case CONCRETE_TUTORIAL:
                currentSelectedScreen = ScreenName.RESTART;
                AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.denuevo);
                break;
            case EXIT:
                currentSelectedScreen = ScreenName.CONCRETE_TUTORIAL;
                AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.ayuda);
                break;
            case RESTART:
                currentSelectedScreen = ScreenName.LAST_SCREEN;
                AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.jugar);
                break;
        }

    }
    private void goToNextMenuItem(){
        switch(currentSelectedScreen){
            case LAST_SCREEN:
                currentSelectedScreen = ScreenName.RESTART;
                AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.denuevo);
                break;
            case RESTART:
                currentSelectedScreen = ScreenName.CONCRETE_TUTORIAL;
                AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.ayuda);
                break;
            case CONCRETE_TUTORIAL:
                currentSelectedScreen = ScreenName.EXIT;
                AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.salir);
                break;
            case EXIT:
                currentSelectedScreen = ScreenName.LAST_SCREEN;
                AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.jugar);
                break;

        }

    }

    private void enterPressed(){
        onBtnClicked(currentSelectedScreen);
    }

}

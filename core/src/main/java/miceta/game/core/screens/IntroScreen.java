package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
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

/**
 * Created by ewe on 1/5/18.
 */
public class IntroScreen extends AbstractGameScreen {
    public static final String TAG = IntroScreen.class.getName();
    private ImageButton btnPlay, btnExit, btnHelp, btnNewStart, btnOrganicTutorial, btnIngredients;

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

    }

//
//    @Override
//    public InputProcessor getInputProcessor() {
//        return stage;
//    }

    private void addBtnPlay(int x, int y){
        btnPlay = new ImageButton(Assets.instance.buttons.playButtonStyle);
        btnPlay.setPosition(0,y);
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked("play");
            }
        });
        btnPlay.addListener(new InputListener(){
            @Override
            public  void    enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
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
                onBtnClicked("exit");
            }
        });

        btnExit.addListener(new InputListener(){
            @Override
            public  void    enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
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
                onBtnClicked("restart");
            }
        });
        btnNewStart.addListener(new InputListener(){
            @Override
            public  void    enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
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
                onBtnClicked("help");
            }
        });
        btnHelp.addListener(new InputListener(){
            @Override
            public  void    enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1) { // if not, on btn click the audio file is played again (without any need)
                    AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.ayuda); // TODO change to "JUGAR" audio whan available
                }
            }
        });

        btnHelp.setWidth(1366);
        btnHelp.setHeight(180);
        stage.addActor(btnHelp);
    }


    private void onBtnClicked(String btnType) {
        ScreenTransition transition = ScreenTransitionFade.init(1);
        switch(btnType){
            case "play":
                LevelsManager.instance.forceLevelParams(LevelsManager.instance.get_level());
                AbstractGameScreen nowScreen = game.getRepresentationMapper().getScreenFromScreenName(LevelsManager.instance.getScreenName());
                game.setScreen(nowScreen);
                break;
            case "exit":
                Gdx.app.exit();
                break;
            case "restart":
                GamePreferences.instance.setLast_level(1);
                LevelsManager.instance.forceLevel(1);
                LevelsManager.instance.forceLevelParams(1);
                game.setScreen(new ConcreteTutorial(game,0,0),transition);
                break;
            case "help":
                LevelsManager.instance.forceLevel(1);
                LevelsManager.instance.forceLevelParams(1);
                game.setScreen(new ConcreteTutorial(game,0, 0));
                break;
        }

    }

}

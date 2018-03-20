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
 * Created by ewe on 2/5/18.
 */
public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = IntroScreen.class.getName();


    public MenuScreen(miCeta game) {
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
        int x = 20;
        int shift = 20+150;
        addBtnExit(x, 30 ); // last btn
        addBtnHelp(x, 30 + shift*1 );
        addBtnNewStart(x , 30 +shift*2);
        addBtnPlay(x, 30 +shift*3 ); // top button

        x = 340; //second column
        ImageButton btnOrganic = addGameBtn(x,30+ shift*3 );
        btnOrganic.setStyle(Assets.instance.buttons.helpButtonStyle);
        btnOrganic.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked(ScreenName.ORGANIC_TUTORIAL1);
            }
        });

        ImageButton btnKnock = addGameBtn(x,30 );
        btnKnock.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked(ScreenName.GAME_KNOCK);
            }
        });
        x = 660;
        ImageButton btnIngredients = addGameBtn(x,30+ shift*3 );
        btnIngredients.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked(ScreenName.GAME_INGREDIENTS);
            }
        });

        ImageButton btnMixing = addGameBtn(x,30+ shift*2 );
        btnMixing.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked(ScreenName.GAME_MIXING);
            }
        });
        ImageButton btnMusic = addGameBtn(x,30+ shift );
        btnMusic.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked(ScreenName.GAME_MUSIC);
            }
        });
        ImageButton btnBell = addGameBtn(x,30 );
        btnBell.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked(ScreenName.GAME_BELL);
            }
        });

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
                if(pointer == -1) { // if not, on btn click the audio file is played again (without any need)
                    AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.jugar); // TODO change to "JUGAR" audio whan available
                }
            }
        });
        stage.addActor(btnPlay);
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
                if(pointer == -1) { // if not, on btn click the audio file is played again (without any need)
                    AudioManager.instance.playWithoutInterruption(Assets.instance.sounds.salir); // TODO change to "JUGAR" audio whan available
                }
            }
        });
        stage.addActor(btnExit);
    }


    private ImageButton addGameBtn(int x, int y){
        ImageButton btnGame = new ImageButton(Assets.instance.buttons.playButtonStyle);
        btnGame.setPosition(x,y);
        stage.addActor(btnGame);
        return btnGame;
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
                game.setScreen(new ConcreteTutorial(game, 0, 0, true, false),transition);// restart should do upLevel!
                break;
            case CONCRETE_TUTORIAL:
                LevelsManager.instance.forceLevelParams(1);
                game.setScreen(new ConcreteTutorial(game, 0, 0, false, false));
                break;
            case ORGANIC_TUTORIAL1:
                LevelsManager.instance.forceLevelParams(2);
                game.setScreen(new OrganicOneScreen(game),transition);
                break;
            case GAME_KNOCK:
                LevelsManager.instance.forceLevelParams(3);
                game.setScreen(new World_1_AudioScreen(game),transition);
                break;
            case GAME_INGREDIENTS:
                LevelsManager.instance.forceLevelParams(4);
                game.setScreen(new IngredientsScreen(game),transition);
                break;
            case GAME_MIXING:
                LevelsManager.instance.forceLevelParams(5);
                game.setScreen(new BaseScreenWithIntro(game),transition);
                break;
            case GAME_MUSIC:
                //LevelsManager.instance.forceToFirstLevel(6);
                LevelsManager.instance.forceLevelParams(6);
                game.setScreen(new BaseScreenWithIntro(game),transition);
                break;
            case GAME_BELL:
                //LevelsManager.instance.forceToFirstLevel(7);
                LevelsManager.instance.forceLevelParams(7);
                game.setScreen(new BaseScreenWithIntro(game, false, false),transition);
                break;
        }
    }

}

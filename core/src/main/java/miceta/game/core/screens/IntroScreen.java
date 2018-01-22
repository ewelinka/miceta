package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.Assets;
import miceta.game.core.miCeta;
import miceta.game.core.transitions.ScreenTransition;
import miceta.game.core.transitions.ScreenTransitionFade;
import miceta.game.core.util.AudioManager;


import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.*;
import java.util.ArrayList;

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
        addBtnOrganicTutorial(30, 30 ); // TODO just for testing!
        addBtnIngredients(30,30 + (30 + 150)*1); // TODO just for testing!
        addBtnExit(viewportWidth/2 - 300/2, 30 ); // last btn
        addBtnHelp(viewportWidth/2 - 300/2, 30 + (30 + 150)*1 );
        addBtnNewStart(viewportWidth/2 - 300/2, 30 +(30 + 150)*2);
        addBtnPlay(viewportWidth/2 - 300/2, 30 +(30 + 150)*3 ); // top button

    }


    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    private void addBtnPlay(int x, int y){
        btnPlay = new ImageButton(Assets.instance.buttons.playButtonStyle);
        btnPlay.setPosition(x,y);
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
        stage.addActor(btnPlay);
    }
    private void addBtnExit(int x, int y){
        btnExit = new ImageButton(Assets.instance.buttons.exitButtonStyle);
        btnExit.setPosition(x,y);
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



        stage.addActor(btnExit);
    }
    private void addBtnNewStart(int x, int y){
        btnNewStart = new ImageButton(Assets.instance.buttons.newStartButtonStyle);
        btnNewStart.setPosition(x,y);
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

        stage.addActor(btnNewStart);
    }
    private void addBtnHelp(int x, int y){
        btnHelp = new ImageButton(Assets.instance.buttons.helpButtonStyle);
        btnHelp.setPosition(x,y);
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

        stage.addActor(btnHelp);
    }
    private void addBtnOrganicTutorial(int x, int y){
        btnOrganicTutorial = new ImageButton(Assets.instance.buttons.helpButtonStyle);
        btnOrganicTutorial.setPosition(x,y);
        btnOrganicTutorial.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked("organic");
            }
        });
        stage.addActor(btnOrganicTutorial);
    }

    private void addBtnIngredients(int x, int y){
        btnIngredients = new ImageButton(Assets.instance.buttons.helpButtonStyle);
        btnIngredients.setPosition(x,y);
        btnIngredients.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBtnClicked("ingredients");
            }
        });
        stage.addActor(btnIngredients);

    }

    private void onBtnClicked(String btnType) {
        ScreenTransition transition = ScreenTransitionFade.init(1);
        switch(btnType){
            case "play":
                //game.setScreen(new TestScreen(game),transition);
                game.setScreen(new World_1_AudioScreen(game,0, 0),transition);

                break;
            case "exit":
                Gdx.app.exit();
                break;
            case "restart":
                // TODO implement game reset
                game.setScreen(new FeedbackScreen(game),transition);
                break;
            case "help":
                // TODO implement tutorial and then go from here to this tutorial
                game.setScreen(new ConcreteTurorial(game,0, 0),transition);
                break;
            case "organic":
                // TODO should not be in menu, we put it now for testing
                //game.setScreen(new ConcreteTutorial(game),transition);
                game.setScreen(new OrganicTutorial1AudioScreen(game,1,3),transition);
                break;
            case "ingredients":
                // TODO should not be in menu, we put it now for testing
                //game.setScreen(new ConcreteTutorial(game),transition);
                game.setScreen(new IngredientsScreen(game),transition);
                break;
        }


    }


}

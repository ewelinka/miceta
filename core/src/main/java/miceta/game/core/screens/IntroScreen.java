package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
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
public class IntroScreen extends AbstractGameScreen {
    private static final String TAG = IntroScreen.class.getName();
    private ScreenName currentSelectedScreen;
    private ScreenName [] allScreens;
    private ImageButton btnPlay, btnExit, btnNewStart, btnHelp;

    public IntroScreen(miCeta game) {
        super(game,true);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0,0,0,0);
        //Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();

        spriteBatch.begin();
        spriteBatch.draw(Assets.instance.backgrounds.generic,0,0);
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




}

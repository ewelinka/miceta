package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.Assets;
import miceta.game.core.controllers.CvWorldControllerFeedback;
import miceta.game.core.miCeta;
import miceta.game.core.util.AudioManager;

/**
 * Created by ewe on 1/12/18.
 */
public class OrganicTutorial1AudioScreen extends OrganicTutorial1Screen {
    private static final String TAG = OrganicTutorial1AudioScreen.class.getName();
    private float timePassed;
    private float tutorialDuration;

    public OrganicTutorial1AudioScreen(miCeta game, int tutorialPart, int totalTutorialParts) {
        super(game, tutorialPart, totalTutorialParts);
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth , viewportHeight));
        Gdx.input.setCatchBackKey(false);
        timePassed = 0;
        AudioManager.instance.setStage(stage);
        tutorialDuration = AudioManager.instance.reproduce_organic_tutorial1(tutorialPart);
    }


    @Override
    public void render(float deltaTime) {
        timePassed+=deltaTime;
        Gdx.gl.glClearColor(1, 1, 1, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();
        if(timePassed > tutorialDuration){
            if(tutorialPart==totalTutorialParts) {
                game.setScreen(new IntroScreen(game)); // TODO when micro mundo 1 ready, go to it!
            } else
                game.setScreen(new OrganicTutorial1InteractiveScreen(game,tutorialPart+1,totalTutorialParts));
        }

    }

    @Override
    public void resize(int width, int height) {

    }



    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }
}

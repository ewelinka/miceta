package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import miceta.game.core.controllers.CvOrganicTutorialController;
import miceta.game.core.miCeta;

/**
 * Created by ewe on 1/12/18.
 */
public class OrganicTutorial1InteractiveScreen extends OrganicTutorial1Screen {

    public OrganicTutorial1InteractiveScreen(miCeta game, int tutorialPart, int totalTutorialParts) {
        super(game, tutorialPart, totalTutorialParts);
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        worldController = new CvOrganicTutorialController(game,stage,getFeedbackSoundNameFromTutorialPart(tutorialPart));
        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);

    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(255.0f, 0x95 / 255.0f,0xed / 255.0f, 0xff / 255.0f);
        //Gdx.app.log(TAG,"famerate " + Gdx.graphics.getFramesPerSecond());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Do not update game world when paused.
        if (!paused) {
            worldController.update(deltaTime);
            stage.act(deltaTime);
        }
    }



    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }

    private String getFeedbackSoundNameFromTutorialPart(int tutorialPartNr){
        switch(tutorialPartNr){
            case 2:
                return "drop";
            default:
                return "knock";
        }
    }
}

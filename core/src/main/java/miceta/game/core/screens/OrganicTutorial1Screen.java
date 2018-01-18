package miceta.game.core.screens;

import com.badlogic.gdx.InputProcessor;
import miceta.game.core.miCeta;

/**
 * Created by ewe on 1/12/18.
 */
public class OrganicTutorial1Screen extends AbstractGameScreen {
    // organic tutorial = audioIntro + interactive part + audio tada

    protected int tutorialPart;
    protected int totalTutorialParts;
    public OrganicTutorial1Screen(miCeta game, int tutorialPart, int totalTutorialParts) {
        super(game);
        this.tutorialPart  = tutorialPart;
        this.totalTutorialParts = totalTutorialParts;
    }

    @Override
    public void show() {

    }
    @Override
    public void render(float deltaTime) {

    }



    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }
}

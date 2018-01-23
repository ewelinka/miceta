package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;
import miceta.game.core.util.GamePreferences;

/**
 * Created by ewe on 1/22/18.
 */
public class AutoInitScreen extends AbstractGameScreen {
    public AutoInitScreen(miCeta game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {

    }

    @Override
    public void show() {
        AbstractGameScreen nowScreen = game.getRepresentationToScreenMapper().getScreenFromRepresentation(LevelsManager.instance.getRepresentation());
        game.setScreen(nowScreen);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }
}

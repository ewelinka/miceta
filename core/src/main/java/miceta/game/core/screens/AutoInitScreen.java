package miceta.game.core.screens;

import miceta.game.core.managers.LevelsManager;
import miceta.game.core.miCeta;

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
    public void stopCurrentSound() {
        // nothing to stop!
    }

    @Override
    public void show() {
        AbstractGameScreen nowScreen = game.getRepresentationMapper().getScreenFromScreenName(LevelsManager.instance.getScreenName());
        game.setScreen(nowScreen);
    }

}

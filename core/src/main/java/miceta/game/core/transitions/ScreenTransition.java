package miceta.game.core.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by ewe on 8/10/17.
 */
public interface ScreenTransition {
    float getDuration();
    void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha);
}


package miceta.game.core.util;

import com.badlogic.gdx.Gdx;
import miceta.game.core.miCeta;
import miceta.game.core.screens.*;

/**
 * Created by ewe on 1/22/18.
 */
public class RepresentationToScreenMapper {
    public static final String TAG = RepresentationToScreenMapper.class.getName();
    private miCeta game;

    public RepresentationToScreenMapper(miCeta game) {
        this.game = game;
    }

    public AbstractGameScreen getScreenFromRepresentation( int representation){
        Gdx.app.log(TAG,"represent! "+representation);
        switch (representation){
            case -1:
                Gdx.app.log(TAG,"concrete!");
                return new ConcreteTutorial(game,0, 0);
            case 0:
                Gdx.app.log(TAG,"organic!");
                return new OrganicTutorial1AudioScreen(game,1,3);
            case 1:
                Gdx.app.log(TAG,"w1!");
                return new World_1_AudioScreen(game,0,0);
            case 2:
                Gdx.app.log(TAG,"ingredients!");
                return new IngredientsScreen(game);
            default:
                Gdx.app.log(TAG,"def!");
                return new OrganicTutorial1AudioScreen(game,1,3);
        }
    }
}

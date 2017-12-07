package miceta.game.core.managers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import miceta.game.core.receiver.Block;
import miceta.game.core.util.Constants;

public class FeedbackDrawManager {



    public void setShapeRenderer(ShapeRenderer shapeRenderer, Block block, float shiftX, float shiftY ){

        shapeRenderer.rect(0, 10, Constants.BASE, Constants.BASE * block.getValue());
    }


    private float radianToStage(double r){
        float d = (float) Math.toDegrees(r - Math.PI/2);
        if(d < 0)
            d = 360 - d;
        return d;
    }

}

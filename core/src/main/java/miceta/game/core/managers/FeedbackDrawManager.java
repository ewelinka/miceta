package miceta.game.core.managers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import edu.ceta.vision.core.blocks.Block;
import miceta.game.core.util.Constants;

public class FeedbackDrawManager {



    public void setShapeRenderer(ShapeRenderer shapeRenderer, Block block, float shiftX, float shiftY ){


        if((Gdx.app.getType() == Application.ApplicationType.Android)) {

                     shapeRenderer.rect((float) block.getCenter().y+shiftX, (float) block.getCenter().x+shiftY,
                    Constants.BASE * block.getValue(), Constants.BASE,
                    Constants.BASE * block.getValue() / 2, Constants.BASE / 2,
                    radianToStage(block.getOrientation()));
        }
        else{
            shapeRenderer.rect((float) (block.getCenter().x)  , 480 - (float) block.getCenter().y,
                    Constants.BASE, Constants.BASE * block.getValue(),
                    Constants.BASE  * block.getValue() / 2,Constants.BASE/2,
                    radianToStage(block.getOrientation()));

        }
    }


    private float radianToStage(double r){
        float d = (float) Math.toDegrees(r - Math.PI/2);
        if(d < 0)
            d = 360 - d;
        return d;
    }

}

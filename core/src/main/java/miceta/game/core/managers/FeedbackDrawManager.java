package miceta.game.core.managers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import miceta.game.core.receiver.Block;
import miceta.game.core.util.Constants;

public class FeedbackDrawManager {

    public void drawBlockByValue(ShapeRenderer shapeRenderer, int blockValue, float shiftX, float shiftY ){
        setColorFromValue(blockValue, shapeRenderer);
        shapeRenderer.rect(shiftX, shiftY, Constants.BASE, Constants.BASE * blockValue);
    }

    private void setColorFromValue(int value, ShapeRenderer shapeRenderer){
        switch (value){
            case 1:
                shapeRenderer.setColor(1, 1, 0, 1); // yellow
                break;
            case 2:
                shapeRenderer.setColor(0, 1, 0, 1); // green
                break;
            case 3:
                shapeRenderer.setColor(0, 0, 1, 1); // blue
                break;
            case 4:
                shapeRenderer.setColor(1, 159/255.0f, 0, 1); // orange
                break;
            case 5:
                shapeRenderer.setColor(1, 0, 0, 1); //red
                break;
        }
    }

}

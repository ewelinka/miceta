package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import edu.ceta.vision.core.blocks.Block;
import miceta.game.core.controllers.CvWorldController;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.miCeta;

import java.util.Set;


/**
 * Created by ewe on 8/10/17.
 */
public class BaseScreen extends AbstractGameScreen {
    private static final String TAG = BaseScreen.class.getName();
    ShapeRenderer shapeRenderer;
    private final BitmapFont font = new BitmapFont();
    private final SpriteBatch spriteBatch  = new SpriteBatch();
    FeedbackDrawManager fd;


    public BaseScreen(miCeta game, boolean upLevel, boolean shouldRepeatTutorial){
        super(game, upLevel, shouldRepeatTutorial);
    }

    @Override
    public void show() {

        Gdx.app.log(TAG," we start the SHOW! "+Gdx.graphics.getWidth());
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        worldController = new CvWorldController(game,stage, upLevel, shouldRepeatTutorial);
        shapeRenderer = new ShapeRenderer();
        fd = new FeedbackDrawManager();
        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Do not update game world when paused.
       // if (!paused) {
            worldController.update(deltaTime);
            stage.act(deltaTime);
       // }
        stage.draw();

        Set<Block> cBlocks = worldController.getCurrentBlocksFromManager();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);

        // in desktop:
        int shiftY = 200;
        int shiftX = 70;
        shapeRenderer.rect(shiftX, shiftY, 480, 640);



        if(cBlocks!=null) {

            for (Block block : cBlocks) {
                setColorFromValue(block.getValue());

                fd.setShapeRenderer(shapeRenderer, block, shiftX, shiftY);

            }

        }

        shapeRenderer.end();
        spriteBatch.begin();
        font.draw(spriteBatch,""+worldController.getRandomNumber(),200,680);
        font.draw(spriteBatch,"jugar",550,1000);
        font.draw(spriteBatch,"feedback",10,1000);

        font.draw(spriteBatch,"Velocidad entre golpes:",10,140);
        font.draw(spriteBatch,"más lento",10,120);
        font.draw(spriteBatch,"Delay extra: " + String.format("%.2g%n", worldController.getExtraDelayBetweenFeedback()),250,120);
        font.draw(spriteBatch,"más rápido",400,120);

        font.draw(spriteBatch,"Tiempo de espera entre series:",10,80);
        font.draw(spriteBatch,"más tiempo",10,60);
        font.draw(spriteBatch,"Tiempo: " + String.format("%.2g%n", worldController.getWaitAfterKnock()),250,60);
        font.draw(spriteBatch,"menos tiempo",400,60);

        spriteBatch.end();

    }

    private float radianToStage(double r){
        float d = (float) Math.toDegrees(r*-1 - Math.PI/2);
        if(d < 0)
            d = 360 + d;
        return d;
    }

    private void setColorFromValue(int value){
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

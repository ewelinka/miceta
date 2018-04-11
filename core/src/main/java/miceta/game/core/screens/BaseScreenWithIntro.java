package miceta.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import edu.ceta.vision.core.blocks.Block;
import miceta.game.core.controllers.CvWithIntroController;
import miceta.game.core.managers.FeedbackDrawManager;
import miceta.game.core.miCeta;

import java.util.Set;

/**
 * Created by ewe on 1/29/18.
 */
public class BaseScreenWithIntro  extends AbstractGameScreen {
    private static final String TAG = BaseScreenWithIntro.class.getName();
    ShapeRenderer shapeRenderer;
    private final BitmapFont font = new BitmapFont();
    private final SpriteBatch spriteBatch  = new SpriteBatch();
    FeedbackDrawManager fd;
    private  boolean isInOgranicHelpScreen;

    public BaseScreenWithIntro(miCeta game) {
        this(game, false, false);
    }

    public BaseScreenWithIntro(miCeta game, boolean upLevel, boolean shouldRepeatTutorial) {
        this(game, upLevel,shouldRepeatTutorial, false);
    }

    public BaseScreenWithIntro(miCeta game, boolean upLevel, boolean shouldRepeatTutorial, boolean isInOgranicHelpScreen) {
        super(game, upLevel,shouldRepeatTutorial);
        this.isInOgranicHelpScreen = isInOgranicHelpScreen;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(viewportWidth, viewportHeight));
        worldController = new CvWithIntroController(game,stage,
                game.gameScreen.feedbackSoundType,
                game.gameScreen.intro,
                game.gameScreen.positives,
                game.gameScreen.tooFew,
                game.gameScreen.tooMuch,
                game.gameScreen.finalSound,
                upLevel,
                shouldRepeatTutorial,
                isInOgranicHelpScreen);
        shapeRenderer = new ShapeRenderer();
        fd = new FeedbackDrawManager();
        // android back key used to exit, we should not catch
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Do not update game world when paused.
        // if (!paused) {
        worldController.update(deltaTime);
        stage.act(deltaTime);
        // }
        stage.draw();


        Set<Block> cBlocks = worldController.getCurrentBlocksFromManager();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        spriteBatch.begin();
        spriteBatch.draw(game.gameScreen.imgBackground,0,0);
        spriteBatch.end();

        int shiftY = 0;
        int shiftX = 0;



        if(cBlocks!=null) {

            for (Block block : cBlocks) {
                setColorFromValue(block.getValue());

                fd.setShapeRenderer(shapeRenderer, block, shiftX, shiftY);

            }

        }

        shapeRenderer.end();
        spriteBatch.begin();
        font.setScale(2, 2);
        font.draw(spriteBatch,""+worldController.getRandomNumber(),200,680);
        font.setScale(1, 1);
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

package miceta.game.core.receiver;

import java.util.Collection;
import java.util.Date;

/**
 * Created by ewe on 12/6/17.
 */
public class Block {
    private int id;
    private int value;
    private BlockState state;
    private Collection<Integer> neighbours;
    private Date startTouching;
    private boolean isBeingTouched;


    public Block(int id, int value) {
        super();
        this.id = id;
        this.value = value;
        this.isBeingTouched = false;
        this.startTouching = new Date();

        state = new BlockState();

    }

    public void addNeighbour(Integer blockId){
        this.neighbours.add(blockId);
    }

    public void removeNeighbour(Integer blockId){
        this.neighbours.remove(blockId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public BlockState getState() {
        return state;
    }

    public void setState(BlockState state) {
        this.state = state;
    }
    public void startTouching(){
        this.startTouching = new Date();
        this.isBeingTouched = true;
    }

    public void stopTouching(){
        this.isBeingTouched = false;
    }

    public boolean isBeingTouched(){
        return this.isBeingTouched;
    }
    public Date getStartTouching(){
        return startTouching;
    }

    public Collection<Integer> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Collection<Integer> neighbours) {
        this.neighbours = neighbours;
    }
}

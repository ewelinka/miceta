package miceta.game.core.receiver;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.illposed.osc.OSCPortOut;

/**
 * Created by ewe on 12/6/17.
 */
public class Block {
    private int id;
    private int value;
    private BlockState state;
    private Set<Integer> neighbours;
    private Date startTouching;
    private boolean isBeingTouched;


    /*OSC Data*/
	private OSCPortOut portOut;	

	
    public Block(int id, int value, OSCPortOut out) {
        super();
        this.id = id;
        this.value = value;
        this.isBeingTouched = false;
        this.startTouching = new Date();
        this.portOut = out;
        this.neighbours = new HashSet<Integer>();
        state = new BlockState();

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

    public void setNeighbours(Set<Integer> neighbours) {
        this.neighbours = neighbours;
    }
    
    public void addNeighbour(int neighbour_id){
    	this.neighbours.add(neighbour_id);
    }
      
    public boolean isNeighbour(int blockId){
    	return this.neighbours.contains(blockId);
    }
    
    public void removeNeighbour(int neighbour_id){
    	this.neighbours.remove(neighbour_id); 
    }
    
    public OSCPortOut getPortOut(){
    	return this.portOut;
    }
}

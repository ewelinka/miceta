package miceta.game.core.util;

import java.util.HashSet;
import java.util.Set;

public class CompositionData {
	public String oscIDs;
	public Set<Integer> blocksIds;
	public int composed_n;
	public boolean hasNeighbourTouched;
	public long sentTime;
	
	public CompositionData() {
		super();
		oscIDs="";
		composed_n = 0;
		hasNeighbourTouched=false;
		sentTime=-1;
		blocksIds = new HashSet<Integer>();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null || !(obj instanceof CompositionData))
			return false;
		else {
			CompositionData comp = (CompositionData)obj;
			return blocksIds.equals(comp.blocksIds) && composed_n==comp.composed_n;
		}
	}
	
	
}

package mmt.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.time.LocalTime;

public class Station implements java.io.Serializable{
	private String _name;

	private List<TrainStop> _trainStops = new ArrayList<TrainStop>();

	protected Station(String name){
		_name = name;
	}
	
	protected String getName(){
		return _name;
	}

	protected void addTrainStop(TrainStop t){
		_trainStops.add(t);
	}

	protected Collection<TrainStop> getTrainStopsTime(LocalTime time){
		List<TrainStop> _newList = new ArrayList<TrainStop>();
		for(TrainStop t: _trainStops){
			if(! (time.isAfter(t.getTime()))){
				_newList.add(t);
			}
		}
		return _newList;
	}

	protected Collection<TrainStop> getTrainStops(){
    	return Collections.unmodifiableCollection(_trainStops);
  	}

	
}
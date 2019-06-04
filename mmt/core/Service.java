package mmt.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.time.Duration;
import java.time.LocalTime;

public class Service implements java.io.Serializable{

	private int _id;
	private double _cost;
	private int _size;

	private List<TrainStop> _trainStops = new ArrayList<TrainStop>();

	protected Service(int id, double cost){
		_id = id;
		_cost = cost;
	}
	
	protected int getId(){
		return _id;
	}

	protected double getCost(){
		return _cost;
	}

	protected void addTrainStop(TrainStop t){
		_trainStops.add(t);
	}

	protected Duration serviceDuration(){
		return Duration.between(getDepartureTrainStop().getTime(),getArrivalTrainStop().getTime());
	}

	protected TrainStop getDepartureTrainStop(){
		return _trainStops.get(0);
	}

	protected TrainStop getArrivalTrainStop(){
		return _trainStops.get(_trainStops.size()-1);
	}

	protected Collection<TrainStop> getTrainStopsBetween(TrainStop start, TrainStop finish){
		ArrayList<TrainStop> _list = new ArrayList<TrainStop> ();
		int i =0;
		while(!_trainStops.get(i).getStationName().equals(start.getStationName()))
			i++;
		while(!_trainStops.get(i).getStationName().equals(finish.getStationName())){
			_list.add(_trainStops.get(i));
			i++;
		}
		_list.add(_trainStops.get(i));
		return Collections.unmodifiableCollection(_list);

	}


	protected Collection<TrainStop> getTrainStops(){
    	return Collections.unmodifiableCollection(_trainStops);
  	}
	public String toString(){
		String _string = "Serviço #" + getId() + " @ " + String.format("%.2f",getCost()) + "\n"; 
		for(TrainStop t: _trainStops)
			_string += t.getTime() + " " + t.getStationName() + "\n";
		return _string;
	}
}
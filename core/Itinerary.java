package mmt.core;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

public class Itinerary implements java.io.Serializable{

	private LocalDate _date;
	private int _passengerId;
	private double _cost;
	private TrainCompany _trainCompany;
	private long _duration;
	private int _itNumber;

	private List<Segment> _segments = new ArrayList<Segment>();

	protected Itinerary(LocalDate date, TrainCompany trainCompany){
		_date = date;
		_trainCompany = trainCompany;
	}

	protected void addSegment(Segment s){
		_segments.add(s);
	}

	protected LocalDate getDate(){
		return _date;
	}

	protected void setCost(double cost){
		_cost += cost;
	}

	protected LocalTime departureTime(){
		return _segments.get(0).getDepartureStop().getTime();
	}

	protected LocalTime arrivalTime(){
		return  _segments.get(_segments.size()-1).getArrivalStop().getTime();
	}


	protected long itDuration(){
		return _duration = Duration.between(_segments.get(0).getDepartureStop().getTime(), _segments.get(_segments.size()-1).getArrivalStop().getTime()).toMinutes();
	}

	protected double getCost(){
		for(Segment seg: _segments){
			double sCost = seg.getService().getCost();
			long sTime = seg.getService().serviceDuration().toMinutes();
			long time = Duration.between(seg.getDepartureStop().getTime(), seg.getArrivalStop().getTime()).toMinutes();
			double cost = (time*sCost)/sTime;
			setCost(cost);
		}
		return _cost;
	}

	protected double cost(){
		return _cost;
	}

	protected int itNumber(){
		return _itNumber;
	}

	protected void setItNumber(int i){
		_itNumber = i;
	}

	public String toString(){
		
		double sCost;
		long sTime;
		long time;
		double cost;
		String _string = "\nItinerário " + itNumber() + " para " + getDate() + " @ " + String.format("%.2f",cost()) + "\n";
		for (Segment s: _segments){
			sCost = s.getService().getCost();
			sTime = s.getService().serviceDuration().toMinutes();
			time = Duration.between(s.getDepartureStop().getTime(), s.getArrivalStop().getTime()).toMinutes();
			cost = (time*sCost)/sTime;
			_string += "Serviço #" + s.getService().getId() +" @ " + String.format("%.2f",cost) + "\n";
			ArrayList<TrainStop> _list = new ArrayList<TrainStop>(s.getService().getTrainStopsBetween(s.getDepartureStop(),s.getArrivalStop()));
			for(TrainStop t: _list)
			_string += t.getTime() + " " + t.getStationName() + "\n";
		}
		return _string;
	}	
}
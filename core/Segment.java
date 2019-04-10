package mmt.core;

public class Segment implements java.io.Serializable{
	private TrainStop _departureStop;
	private TrainStop _arrivalStop;
	private Service _service;
	private double _cost;

	protected Segment(Service service, TrainStop departure, TrainStop arrival){
		_departureStop = departure;
		_arrivalStop = arrival;
		_service = service;
	}

	protected Service getService(){
		return _service;
	}

	protected TrainStop getDepartureStop(){
		return _departureStop;
	}

	protected TrainStop getArrivalStop(){
		return _arrivalStop;
	}
}
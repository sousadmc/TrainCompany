package mmt.core;

import java.time.LocalTime;

public class TrainStop implements java.io.Serializable{
	private LocalTime _time;
	private Station _station;
	private Service _service;

	protected TrainStop(LocalTime time, Station station, Service service){
		_time = time;
		_station = station;
		_service = service;
	}

	protected LocalTime getTime(){
		return _time;
	}

	protected String getStationName(){
		return _station.getName();
	}

	protected Service getService(){
		return _service;
	}
	
}
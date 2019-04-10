package mmt.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NonUniquePassengerNameException;


public class Passenger implements java.io.Serializable{
	private Category _category;
	private int _id;
	private String _name;
	private double _totalCost;
	private int _numItinerary;
	private long _travelTime;
	private TrainCompany _trainC;
	private double[] _trips = new double[10];
	private int _pos = 0;
	
	private List<Itinerary> _itineraries = new ArrayList<Itinerary>();

	protected Passenger(TrainCompany trainC, String name) throws InvalidPassengerNameException, NonUniquePassengerNameException {
		_name = name;
		_category = new Normal();
		_trainC = trainC;
		if( _name == null)
    		throw new InvalidPassengerNameException(name);
		_id = _trainC.registerPassenger(this);
		_travelTime = 0;
	}

	protected int getId(){
		return _id;
	}

	protected String getName(){
		return _name;
	}

	protected void setName(String name){
		_name = name;
	}

	protected void setCategory (Category category){
		this._category = category;
	}

	protected void getLastCost(){
		double _soma = 0;
		for(int i = 0; i < 10; i++)
			_soma += _trips[i];
		if(_soma > 250 && _soma <= 2500)
			setCategory(new Frequent());
		else if(_soma > 2500)
			setCategory(new Special());
		else
			setCategory(new Normal());
	}

	protected void addItinerary(Itinerary it){
		LocalDate date = it.getDate();
		_itineraries.add(it);
		_numItinerary++;
		getLastCost();
      	double itCost = it.cost();
      	double cost = itCost * _category.getDiscount();

      	setCost(cost);
      	int i = _pos % 10;
      	
      	_trips[i] = itCost;
      	_pos++;
      	_travelTime += it.itDuration();
 		getLastCost();
 		
      	itinerarySort();
	}

	protected void itinerarySort(){
		Comparator<Itinerary> comparator = new Comparator<Itinerary>() {
          public int compare(Itinerary i1, Itinerary i2) {
            return i1.getDate().compareTo(i2.getDate());
          }
        };
    Collections.sort(_itineraries, comparator);
    for(int i = 0; i < _itineraries.size(); i++)
      _itineraries.get(i).setItNumber(i+1);
  	}

  	protected String itString(){
  		return "== Passageiro " + this.getId() + ": " + this.getName() + " ==\n";
  	}


	protected Collection<Itinerary> getItineraries(){
    	return Collections.unmodifiableCollection(_itineraries);
	}

	protected void resetItinerary(){
		_itineraries.clear();
	}

	protected String getCategory(){
		return _category.toString();
	}

	protected void setCost(double cost){
		_totalCost += cost;
	}

	protected double getTotalCost(){
		return _totalCost;
	}

	protected int getNumItinerary(){
		return _numItinerary;
	}

	protected long getTravelTime(){
		return  _travelTime;
	}

	public String toString(){
		return  "" + getId() + "|" + getName() + "|" + getCategory() + "|" + getNumItinerary() + "|" + String.format("%.2f",getTotalCost()) + "|" + String.format("%02d:%02d",getTravelTime()/60, (getTravelTime()%60));
	}


	
}
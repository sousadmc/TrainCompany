package mmt.core;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadEntryException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NoSuchDepartureException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import mmt.core.exceptions.NonUniquePassengerNameException;
import mmt.core.exceptions.ImportFileException;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.Comparator;
import java.time.format.DateTimeParseException;


/**
 * A train company has schedules (services) for its trains and passengers that
 * acquire itineraries based on those schedules.
 * @author  Diogo Sousa 
 * @author  Francisco Henriques
 * @version 1.0
 */
public class TrainCompany implements java.io.Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;
  /** Map for Passengers sorted by Passenger Id.*/
  private Map<Integer, Passenger> _passengersMap = new TreeMap<Integer, Passenger>();
  /** Map for Services sorted by Service Id.*/
  private Map<Integer, Service> _servicesMap = new TreeMap<Integer, Service>();
  /**List of Passengers organized by registration order.*/
  private List<Passenger> _passengers = new ArrayList<Passenger>();
  /**List of Available Stations.*/
  private List<Station> _stations = new ArrayList<Station>();
  /** Map for Stations sorted by Name.*/
  private Map<String, Station> _stationsMap = new TreeMap<String, Station>();
  /**List of available Services organized by registration order.*/
  private List<Service> _services = new ArrayList<Service>();
  /**Id Counter for passenger registration*/
  private int _nextId;


  /**
  * Parse of a file
  * @param name of the parsing file
  * @return a Train Company
  */
  protected TrainCompany importFile(String filename) throws ImportFileException{
      NewParser _new = new NewParser();
     return _new.parseFile(filename);
  }


  /**
  * Register a passenger on Train Company and add it to Map and List of passengers
  * @param passenger to register
  * @return id for the registered passenger
  */
  protected int registerPassenger(Passenger p) throws NonUniquePassengerNameException{
    int id = _nextId++;
    for(Passenger pass : _passengers){
      String s = pass.getName();
      if(s.equals(p.getName()))
        throw new NonUniquePassengerNameException(p.getName());
    }
    _passengersMap.put(id, p);
    _passengers.add(p);
    return id;
  }

  protected int getNumPassengers(){
    return _passengers.size();
  }

  protected void removePassenger(int id) throws NoSuchPassengerIdException{
    _passengersMap.remove(id);
    for(int i = 0; i < _passengers.size(); i++){
      if(id == _passengers.get(i).getId())
        _passengers.remove(i);
    }
  }


  /**
  * Get a passenger from the Train Company by Id
  * @param id of the passenger to get
  * @return Passenger with the passed Id
  */
  protected Passenger getPassenger(int id) throws NoSuchPassengerIdException {
    if (!_passengersMap.containsKey(id))
      throw new NoSuchPassengerIdException(id);
    return _passengersMap.get(id);
  }

  
  /**
  * Change name of a TrainCompany's Passenger
  * @param Id of the Passenger and new name
  */
  protected void changePassengerName(int id, String name) throws NoSuchPassengerIdException, InvalidPassengerNameException, NonUniquePassengerNameException{
    if(!_passengers.contains(getPassenger(id)))
      throw new NoSuchPassengerIdException(id);
    Passenger _newnamed = getPassenger(id);
    if( name == null)
      throw new InvalidPassengerNameException(name);
    for(Passenger p : _passengers){
      String s = p.getName();
      if(s.equals(name))
        throw new NonUniquePassengerNameException(name);
    }
    _newnamed.setName(name);
  }


  /**
  * Add a station to the list of all Stations
  * @param Station to add
  */
  protected void addStation(Station station){
    if(!_stationsMap.containsKey(station.getName())){
      _stations.add(station);
      _stationsMap.put(station.getName(),station);
    }
  }


  /**
  * Add a trainStop to the Station list of trainStops
  * @param Station and trainstop to add
  */
  protected void addTrainStop(Station station, TrainStop t){
    _stationsMap.get(station.getName()).addTrainStop(t);
  }


  /**
  * Add a Service to the list of all Services
  * @param Service to add
  */
  protected void addService(Service service){
    _services.add(service);
    _servicesMap.put(service.getId(),service);
  }


  /**
  * Get a Service from the list by Id
  * @param Id of the Service to get
  * @return Service with the passed Id
  */
  protected Service getService(int serviceId) throws NoSuchServiceIdException{
    if(_servicesMap.containsKey(serviceId))
    	return _servicesMap.get(serviceId);
    else
	    throw new NoSuchServiceIdException(serviceId);
  }

  /**
  * Get a TrainStop from the Service by name
  * @param station name to trainStop and serviceId
  * @return TrainStop from passed service
  */
  protected TrainStop getTrainStop(String stationname, int serviceId) throws NoSuchServiceIdException{
    Service s = getService(serviceId);
    List<TrainStop> _t = new ArrayList<TrainStop> (s.getTrainStops());
    for(TrainStop t: _t)
      if(t.getStationName().equals(stationname))
        return t;
    return null;
  }


  /**
  * Sort the List of Services that Start in the same Station by Departure Time
  * @param List to sort and int to define comparator
  */
  protected void compareTime(ArrayList<Service> sort, int st){
  	if(st == 1){
  		Comparator<Service> comparator = new Comparator<Service>() {
      		public int compare(Service s1, Service s2) {
        		TrainStop t1 = s1.getDepartureTrainStop();
        		TrainStop t2 = s2.getDepartureTrainStop();
        		return t1.getTime().compareTo(t2.getTime());
        	}
      	};
      	Collections.sort(sort, comparator);
  	}
  	else{
    	Comparator<Service> comparator = new Comparator<Service>() {
      		public int compare(Service s1, Service s2) {
        		TrainStop t1 = s1.getArrivalTrainStop();
        		TrainStop t2 = s2.getArrivalTrainStop();
        		return t1.getTime().compareTo(t2.getTime());
        	}
      	};
      	Collections.sort(sort, comparator);
  	}
  	
  }


  /**
  * Get a service cost by Id
  * @param id of the service to get cost
  * @return cost of service if the service exists
  */
  protected double getServiceCost (int serviceId) throws NoSuchServiceIdException{
    if(_servicesMap.containsKey(serviceId))
    	return _servicesMap.get(serviceId).getCost();
    else
	    throw new NoSuchServiceIdException(serviceId);
  }


  /**
  * Eliminate all Passengers from TrainCompany List and reset passengersId
  */
  protected void resetPassenger(){
    _nextId = 0;
    _passengers.clear();
    _passengersMap.clear();
  }


  /**
  * Eliminate all Itineraries from TrainCompany List
  */
  protected void resetItinerary(){
    for(Passenger p: _passengers){
      p.resetItinerary();
    }
  }

  /**
  * Commit a Itinerary to a Passenger
  * @param Passenger that bought itinerary and the itinerary to commit
  */
  protected void commitItinerary(int passengerId, Itinerary it) throws NoSuchPassengerIdException{
    if (passengerId>(_nextId-1) || passengerId<0)
      throw new NoSuchPassengerIdException(passengerId);
    getPassenger(passengerId).addItinerary(it);

  }

  /**
  * Search itineraries from departureStation to arrivalStation on departureDate and departureTime
  * @param name of departure Station, name of arrival Station, date of the trip, time of the trip
  * @return Collection of Itineraries that corresponds to preferences passed
  */
  protected Collection<Itinerary> searchItinerary(String departureStation, String arrivalStation, String departureDate,
                                              String departureTime, int passengerId) throws NoSuchPassengerIdException, NoSuchStationNameException,
                                              BadDateSpecificationException,BadTimeSpecificationException{
    if (passengerId>(_nextId-1) || passengerId<0)
      throw new NoSuchPassengerIdException(passengerId);

    List<Itinerary> _list = new ArrayList<Itinerary>();

    Station st = null;
    Passenger p = null;
    LocalTime time;
    LocalDate date;
    
    Service s;
    
    

    if(!_stationsMap.containsKey(departureStation))
  		throw new NoSuchStationNameException(departureStation);

    if(!_stationsMap.containsKey(arrivalStation))
  		throw new NoSuchStationNameException(arrivalStation);
    try{
      date = LocalDate.parse(departureDate);
    }catch (DateTimeParseException e){
      throw new BadDateSpecificationException(departureDate);
    }

    try{
      time = LocalTime.parse(departureTime);
    }catch (DateTimeParseException e){
      throw new BadTimeSpecificationException(departureTime);
    }
    

    for(Station station : _stations)
      if(station.getName().equals(departureStation)){
        st = station;
      }


    int i = 0;
  	
    ArrayList<TrainStop> _tStop = new ArrayList<TrainStop> (st.getTrainStopsTime(time));
    ArrayList<TrainStop> _t2;

    for(TrainStop t: _tStop){
      s = t.getService();
      _t2 = new ArrayList<TrainStop>(s.getTrainStops());
      i++;
      for(TrainStop tr: _t2){
        if(tr.getStationName().equals(arrivalStation)  && tr.getTime().isAfter(t.getTime())){
          Itinerary it = new Itinerary(date, this);
          Segment seg = new Segment(s,t,tr);
          it.addSegment(seg);
          it.getCost();
          _list.add(it);
        }
      }
    }
    compareItinerary(_list);
    


    return Collections.unmodifiableCollection(_list);
  }

  

  protected void compareItinerary(List<Itinerary> list){
  	Comparator<Itinerary> comparator = new Comparator<Itinerary>() {
          public int compare(Itinerary i1, Itinerary i2) {
            if (i1.arrivalTime().equals(i2.arrivalTime()))
              return i1.departureTime().compareTo(i2.departureTime());
            else
              return i1.arrivalTime().compareTo(i2.arrivalTime());

          }
        };
    Collections.sort(list, comparator);
    for(int j = 0; j < list.size(); j++)
      list.get(j).setItNumber(j+1);
  }


  /**
  * Get all Passengers of the Train Company in a Collection
  * @return collection of all passengers
  */
  protected Collection<Passenger> getPassengers() {
    List<Passenger> list = new ArrayList<Passenger>();
    for(Map.Entry<Integer,Passenger> entry : _passengersMap.entrySet()) {
      Passenger serv = entry.getValue();
      list.add(serv);
    }
    return Collections.unmodifiableCollection(list);
  }

  /**
  * Get all the itiniraries commited to one passenger
  * @param Id of the Passenger to get
  * @return Collection of bought itineraries
  */
  protected Collection<Itinerary> getPassengeritinerary(int passengerId) throws NoSuchPassengerIdException{
    return Collections.unmodifiableCollection(getPassenger(passengerId).getItineraries());
  }


  /**
  * Get all Itineraries of the Train Company in a Collection
  * @return collection of all Itineraries
  */
  protected Collection<Itinerary> getItineraries(){
    List<Itinerary> list = null;
    List<Itinerary> _it = new ArrayList<Itinerary>();
    for(Passenger p : _passengers){
      if(p.getNumItinerary() > 0){
        list = new ArrayList<Itinerary>(p.getItineraries());
        for(Itinerary it: list)
          _it.add(it);
      }
    }
    return Collections.unmodifiableCollection(_it);
  }


  /**
  * Get all Services of the Train Company in a Collection
  * @return collection of all Services
  */
  protected Collection<Service> getServices(){
    List<Service> list = new ArrayList<Service>();
    for(Map.Entry<Integer,Service> entry : _servicesMap.entrySet()) {
      Service serv = entry.getValue();
      list.add(serv);
    }
    return Collections.unmodifiableCollection(list);
  }

  /**
  * Get all Services that starts on the same Station of the Train Company in a Collection
  * @return collection of all Services
  */
  protected Collection<Service> getServicesStationDeparture(String stationname) throws NoSuchStationNameException{
  	ArrayList<Service> list = new ArrayList<Service>();
  	for(Service s : _services){
      String station = s.getDepartureTrainStop().getStationName();
      if (station.equals(stationname)){
        list.add(s);
      }
    compareTime(list,1);  
    }
    if(!_stationsMap.containsKey(stationname))
      throw new NoSuchStationNameException(stationname);

    return Collections.unmodifiableCollection(list);
  }

  /**
  * Get all Services that arrives on the same Station of the Train Company in a Collection
  * @return collection of all Services
  */
  protected Collection<Service> getServicesStationArrival(String stationname) throws NoSuchStationNameException{
  	ArrayList<Service> list = new ArrayList<Service>();
  	for(Service s : _services){
      String station = s.getArrivalTrainStop().getStationName();
      if (station.equals(stationname)){
        list.add(s);
      }
    compareTime(list,2);  
    }
    if(!_stationsMap.containsKey(stationname))
      throw new NoSuchStationNameException(stationname);

    return Collections.unmodifiableCollection(list);
  }

}
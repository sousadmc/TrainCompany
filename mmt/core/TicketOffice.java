package mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.MissingFileAssociationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import mmt.core.exceptions.NonUniquePassengerNameException;
import mmt.core.exceptions.InvalidPassengerNameException;


/**
 * Façade for handling persistence and other functions.
 */
public class TicketOffice implements Serializable {

  private TrainCompany _trainCompany = new TrainCompany();
  private String _name = null;

  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name = name;
  }

  public void reset() {
    _trainCompany.resetPassenger();
    _trainCompany.resetItinerary();
  }


  public void save(String filename) throws IOException {
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
      out.writeObject(_trainCompany);
      out.close(); 
  }

  public void load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
    ObjectInputStream inob = new ObjectInputStream(new FileInputStream(filename));
    _trainCompany = (TrainCompany)inob.readObject();
    inob.close();
  }

  public void importFile(String datafile) throws ImportFileException{
   _trainCompany =  _trainCompany.importFile(datafile);

  }

  public TrainCompany getTrainCompany(){
    return _trainCompany;
  }

  public int getNumPassenger(){
    return _trainCompany.getNumPassengers();
  }

  public void removePassenger(int id) throws NoSuchPassengerIdException{
    _trainCompany.removePassenger(id);
  }

  public Collection<Itinerary> searchItineraries(String departureStation, String arrivalStation, String departureDate,
                                              String departureTime, int passengerId) throws NoSuchPassengerIdException, NoSuchStationNameException, BadTimeSpecificationException,
                                              BadDateSpecificationException,
                                              NoSuchPassengerIdException{
    return Collections.unmodifiableCollection(_trainCompany.searchItinerary(departureStation, arrivalStation, departureDate, departureTime, passengerId));
  }

  public void commitItinerary(int passengerId, Object it) throws NoSuchPassengerIdException{
    _trainCompany.commitItinerary(passengerId, (Itinerary) it);
  }

  public void registerPassenger(String name) throws InvalidPassengerNameException, NonUniquePassengerNameException{
    Passenger p = new Passenger(_trainCompany,name);
  }

  public void changePassengerName(int id, String name) throws NoSuchPassengerIdException, InvalidPassengerNameException, NonUniquePassengerNameException{
    _trainCompany.changePassengerName(id,name);
  }

  public Passenger getPassenger(int id) throws NoSuchPassengerIdException{
    Passenger p = _trainCompany.getPassenger(id);
    return p;
  }
  public String getPassengerName(int id) throws NoSuchPassengerIdException{
    return _trainCompany.getPassenger(id).getName();
  }

  public Service getService(int id) throws NoSuchServiceIdException{
    Service s = _trainCompany.getService(id);
    return s;
  }

  public Collection<Passenger> getPassengers() {
    return Collections.unmodifiableCollection(_trainCompany.getPassengers());
  }

  public Collection<Itinerary> getPassengerItineraries(int id) throws NoSuchPassengerIdException{
    Passenger p = _trainCompany.getPassenger(id);
    return Collections.unmodifiableCollection(p.getItineraries());
  }

  public Collection<String> getItineraries(){
    List<Passenger> list = new ArrayList<Passenger>(_trainCompany.getPassengers());
    List<String> string = new ArrayList<String> ();
    List<Itinerary> its;
    for(Passenger p : list)
      if(p.getNumItinerary() > 0){
        string.add(p.itString());
        its = new ArrayList<Itinerary> (p.getItineraries());
        for (Itinerary it : its) {
          string.add(it.toString());
          
        }
      }


    return Collections.unmodifiableCollection(string);
  }

  public Collection<Service> getServices(){
    return Collections.unmodifiableCollection(_trainCompany.getServices());
  }

  public Collection<Service> getServicesStationDep(String stationName) throws NoSuchStationNameException{
    return Collections.unmodifiableCollection(_trainCompany.getServicesStationDeparture(stationName));
  }

  public Collection<Service> getServicesStationArr(String stationName) throws NoSuchStationNameException{
    return Collections.unmodifiableCollection(_trainCompany.getServicesStationArrival(stationName));
  }

}

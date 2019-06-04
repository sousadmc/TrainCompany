package mmt.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.time.LocalTime;
import java.time.LocalDate;


import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.NonUniquePassengerNameException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchPassengerIdException;

public class NewParser {

  private TrainCompany _trainCompany = new TrainCompany();

  public TrainCompany parseFile(String fileName) throws ImportFileException {

    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;

      while ((line = reader.readLine()) != null) {
        parseLine(line);
      }
    } catch (IOException ioe) {
      throw new ImportFileException(ioe);
    }

    return _trainCompany;
  }

  private void parseLine(String line) throws ImportFileException{
    String[] components = line.split("\\|");

    switch (components[0]) {
      case "PASSENGER":
        parsePassenger(components);
        break;

      case "SERVICE":
        parseService(components);
        break;

      case "ITINERARY":
        parseItinerary(components);
        break;

     default:
       throw new ImportFileException("invalid type of line: " + components[0]);
    }
  }

  private void parsePassenger(String[] components) throws ImportFileException{
    if (components.length != 2)
      throw new ImportFileException("invalid number of arguments in passenger line: " + components.length);
    String passengerName = components[1];
    try{
      Passenger p = new Passenger(_trainCompany,passengerName);
    }catch (InvalidPassengerNameException p) {
      System.out.println("Invalid Passenger Name");
    }catch (NonUniquePassengerNameException p){
      System.out.println("Duplicated Name");
    }

  }

  private void parseService(String[] components) {
    double cost = Double.parseDouble(components[2]);
    int serviceId = Integer.parseInt(components[1]);

    Service s = new Service(serviceId, cost);
    
    for (int i = 3; i < components.length; i += 2) {
      String time = components[i];
      String stationName = components[i + 1];
      LocalTime ltime = LocalTime.parse(time);

      Station station = new Station(stationName);
      TrainStop t = new TrainStop(ltime, station, s);
      s.addTrainStop(t);
      
      _trainCompany.addStation(station);
      _trainCompany.addTrainStop(station,t);
    }
    
    _trainCompany.addService(s);
  }

  private void parseItinerary(String[] components) throws ImportFileException {
    if (components.length < 4)
      throw new ImportFileException("Invalid number of elements in itinerary line: " + components.length);
    try{
    int passengerId = Integer.parseInt(components[1]);
    LocalDate date = LocalDate.parse(components[2]);

    Itinerary it = new Itinerary(date, _trainCompany);

    for (int i = 3; i < components.length; i++) {
      String segmentDescription[] = components[i].split("/");

      int serviceId = Integer.parseInt(segmentDescription[0]);
      String departureTrainStop = segmentDescription[1];
      String arrivalTrainStop = segmentDescription[2];

      Segment s = new Segment(_trainCompany.getService(serviceId), _trainCompany.getTrainStop(departureTrainStop,serviceId), _trainCompany.getTrainStop(arrivalTrainStop, serviceId));
      it.addSegment(s);

    }
    it.getCost();
    _trainCompany.commitItinerary(passengerId, it);
    }catch(NoSuchServiceIdException p){
      System.out.println("Invalid Service Id");
    }catch(NoSuchPassengerIdException p) {
      System.out.println("Invalid Passenger Name");
    }
  }
}
package mmt.app.itineraries;

import mmt.core.TicketOffice;
import mmt.app.exceptions.BadDateException;
import mmt.app.exceptions.BadTimeException;
import mmt.app.exceptions.NoSuchItineraryException;
import mmt.app.exceptions.NoSuchPassengerException;
import mmt.app.exceptions.NoSuchStationException;
import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.Form;
import pt.tecnico.po.ui.Display;
import java.util.List;
import java.util.ArrayList;

//FIXME import other classes if necessary

/**
 * §3.4.3. Add new itinerary.
 */
public class DoRegisterItinerary extends Command<TicketOffice> {

  private Input<Integer> _passengerId;
  private Input<String> _departure;
  private Input<String> _arrival;
  private Input<String> _date;
  private Input<String> _time;
  private Input<Integer> _choice;
  private Form _secondForm = new Form();
  /**
   * @param receiver
   */
  public DoRegisterItinerary(TicketOffice receiver) {
    super(Label.REGISTER_ITINERARY, receiver);
    _passengerId = _form.addIntegerInput(Message.requestPassengerId());
    _departure = _form.addStringInput(Message.requestDepartureStationName());
    _arrival = _form.addStringInput(Message.requestArrivalStationName());
    _date = _form.addStringInput(Message.requestDepartureDate());
    _time = _form.addStringInput(Message.requestDepartureTime());
    _choice = _secondForm.addIntegerInput(Message.requestItineraryChoice());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {

   try {
      _form.parse();
      List<Object> _its = new ArrayList<>(_receiver.searchItineraries(_departure.value(),_arrival.value(),_date.value(),_time.value(), _passengerId.value()));
      for(Object it: _its)
        _display.addLine(it.toString());
      _display.display();
      if(_its.size() > 0){
        _secondForm.parse();
        if(_choice.value() < 0 || _choice.value() > _its.size())
          throw new NoSuchItineraryException(_passengerId.value(), _choice.value());
        else if(_choice.value() > 0 && _choice.value() <= _its.size())
        _receiver.commitItinerary(_passengerId.value(), _its.get(_choice.value()-1));
      }
      
    } catch (NoSuchPassengerIdException e) {
      throw new NoSuchPassengerException(e.getId());
    } catch (NoSuchStationNameException e) {
      throw new NoSuchStationException(e.getName());
    } catch (BadDateSpecificationException e) {
      throw new BadDateException(e.getDate());
    } catch (BadTimeSpecificationException e) {
      throw new BadTimeException(e.getTime());
    }
  } 
}

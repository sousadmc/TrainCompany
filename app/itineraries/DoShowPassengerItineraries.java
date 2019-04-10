package mmt.app.itineraries;

import mmt.core.TicketOffice;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.app.exceptions.NoSuchPassengerException;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.Display;
import java.util.List;
import java.util.ArrayList;

/**
 * §3.4.2. Show all itineraries (for a specific passenger).
 */
public class DoShowPassengerItineraries extends Command<TicketOffice> {

  private Input<Integer> _passengerId;

  /**
   * @param receiver
   */
  public DoShowPassengerItineraries(TicketOffice receiver) {
    super(Label.SHOW_PASSENGER_ITINERARIES, receiver);
    _passengerId = _form.addIntegerInput(Message.requestPassengerId());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try{
      _form.parse();

      List<Object> _itineraries = new ArrayList<>(_receiver.getPassengerItineraries(_passengerId.value()));
      if(_itineraries.size() == 0){
        _display.addLine(Message.noItineraries(_passengerId.value()));
      }
      else{
        _display.addLine("== Passageiro " + _passengerId.value() + ": " + _receiver.getPassengerName(_passengerId.value()) + " ==\n");
        for(Object i: _itineraries)
          _display.addLine(i.toString());
      }
      _display.display();
    }catch(NoSuchPassengerIdException p){
      throw new NoSuchPassengerException (_passengerId.value());
    }
  }

}

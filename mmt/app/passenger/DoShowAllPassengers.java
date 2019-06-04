package mmt.app.passenger;

import mmt.core.TicketOffice;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;

import java.util.ArrayList;

/**
 * §3.3.1. Show all passengers.
 */
public class DoShowAllPassengers extends Command<TicketOffice> {

  /**
   * @param receiver
   */
  public DoShowAllPassengers(TicketOffice receiver) {
    super(Label.SHOW_ALL_PASSENGERS, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    ArrayList<Object> _passengers = new ArrayList<>(_receiver.getPassengers());
    
    for(Object p : _passengers){
      _display.addLine(p.toString());
    }
    _display.display();
  }

}

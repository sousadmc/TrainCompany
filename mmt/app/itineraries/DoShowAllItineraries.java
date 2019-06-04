package mmt.app.itineraries;

import mmt.core.TicketOffice;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;


import java.util.List;
import java.util.ArrayList;

/**
 * §3.4.1. Show all itineraries (for all passengers).
 */
public class DoShowAllItineraries extends Command<TicketOffice> {

  /**
   * @param receiver
   */
  public DoShowAllItineraries(TicketOffice receiver) {
    super(Label.SHOW_ALL_ITINERARIES, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    List<String> _its = new ArrayList<String>(_receiver.getItineraries());
    for(String it : _its){
      _display.addLine(it);
    }

    _display.display();
  }

}

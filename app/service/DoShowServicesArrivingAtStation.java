package mmt.app.service;

import mmt.core.TicketOffice;
import java.util.ArrayList;

import mmt.core.exceptions.NoSuchStationNameException;
import mmt.app.exceptions.NoSuchStationException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.Display;


//FIXME import other classes if necessary

/**
 * 3.2.4 Show services arriving at station.
 */
public class DoShowServicesArrivingAtStation extends Command<TicketOffice> {

  private Input<String> _stationName;

  /**
   * @param receiver
   */
  public DoShowServicesArrivingAtStation(TicketOffice receiver) {
    super(Label.SHOW_SERVICES_ARRIVING_AT_STATION, receiver);
    _stationName = _form.addStringInput(Message.requestStationName());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try{
      _form.parse();
      ArrayList<Object> services = new ArrayList<>(_receiver.getServicesStationArr(_stationName.value()));
      
      for(Object s : services)
        _display.addLine(s.toString());
      _display.display();

    }catch (NoSuchStationNameException s){
      throw new NoSuchStationException (_stationName.value());
    }
  }
}

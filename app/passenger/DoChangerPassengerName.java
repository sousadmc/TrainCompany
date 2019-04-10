package mmt.app.passenger;

import mmt.core.TicketOffice;
import mmt.core.TrainCompany;
import mmt.core.Passenger;
import mmt.app.exceptions.BadPassengerNameException;
import mmt.app.exceptions.DuplicatePassengerNameException;
import mmt.app.exceptions.NoSuchPassengerException;
import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NonUniquePassengerNameException;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

//FIXME import other classes if necessary

/**
 * §3.3.4. Change passenger name.
 */
public class DoChangerPassengerName extends Command<TicketOffice> {

  private Input<String> _newname;
  private Input<Integer> _id;

  /**
   * @param receiver
   */
  public DoChangerPassengerName(TicketOffice receiver) {
    super(Label.CHANGE_PASSENGER_NAME, receiver);
    _id = _form.addIntegerInput(Message.requestPassengerId());
    _newname = _form.addStringInput(Message.requestPassengerName());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try{
      _form.parse();
      _receiver.changePassengerName(_id.value(),_newname.value());

    }catch(NoSuchPassengerIdException p){
      throw new NoSuchPassengerException(_id.value());

    }catch(InvalidPassengerNameException p){
      throw new BadPassengerNameException (_newname.value());

    }catch(NonUniquePassengerNameException p){
      throw new DuplicatePassengerNameException (_newname.value());
    }

  }
}

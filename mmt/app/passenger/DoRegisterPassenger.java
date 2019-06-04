package mmt.app.passenger;

import mmt.core.TicketOffice;

import mmt.app.exceptions.BadPassengerNameException;
import mmt.app.exceptions.DuplicatePassengerNameException;
import mmt.core.exceptions.InvalidPassengerNameException;
import mmt.core.exceptions.NonUniquePassengerNameException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

/**
 * §3.3.3. Register passenger.
 */
public class DoRegisterPassenger extends Command<TicketOffice> {

  private Input<String> _passengerName;

  /**
   * @param receiver
   */
  public DoRegisterPassenger(TicketOffice receiver) {
    super(Label.REGISTER_PASSENGER, receiver);
    _passengerName = _form.addStringInput(Message.requestPassengerName());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try{
      _form.parse();
      _receiver.registerPassenger(_passengerName.value());

    }catch (InvalidPassengerNameException p){
      throw new BadPassengerNameException(_passengerName.value());

    }catch (NonUniquePassengerNameException p){
      throw new DuplicatePassengerNameException(_passengerName.value());
    }
  }
}

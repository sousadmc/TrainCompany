package mmt.app;

import static pt.tecnico.po.ui.Dialog.IO;

import mmt.core.TicketOffice;
import mmt.core.exceptions.ImportFileException;

import mmt.app.main.MainMenu;
import pt.tecnico.po.ui.Menu;
import java.util.Locale;

/**
 * Main driver for the travel management application.
 */
public class App {
  /**
   * @param args
   */
  public static void main(String[] args) {
    TicketOffice office = new TicketOffice();
    Locale.setDefault(new Locale("en", "US"));

    String datafile = System.getProperty("import");
    if (datafile != null) {
      try {
        office.importFile(datafile);
      } catch (ImportFileException e) {
          System.out.println(e.getMessage());
          e.printStackTrace();
      }
    }

    Menu menu = new MainMenu(office);
    menu.open();

    IO.close();
  }

}

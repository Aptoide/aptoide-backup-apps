package pt.aptoide.backupapps;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 21-08-2013
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class StopParseEvent {
  private boolean error;

  public StopParseEvent(boolean error) {

    this.error = error;
  }

  public boolean isError() {
    return error;
  }
}

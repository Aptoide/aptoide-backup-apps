package pt.aptoide.backupapps.download.event;

/**
 * Created with IntelliJ IDEA.
 * User: brutus
 * Date: 29-11-2013
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class AskRepoLoginDataEvent {

  private boolean afterWrongCredentials;

  public AskRepoLoginDataEvent(boolean afterWrongCredentials) {
    this.afterWrongCredentials = afterWrongCredentials;
  }

  public boolean isAfterWrongCredentials() {
    return afterWrongCredentials;
  }
}

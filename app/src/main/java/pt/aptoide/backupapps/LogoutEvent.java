package pt.aptoide.backupapps;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 20-08-2013
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public class LogoutEvent {

  private boolean fromAccountManager;

  public boolean isFromAccountManager() {
    return fromAccountManager;
  }

  public LogoutEvent setFromAccountManager(boolean fromAccountManager) {
    this.fromAccountManager = fromAccountManager;
    return this;
  }
}

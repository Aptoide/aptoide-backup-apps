package pt.aptoide.backupapps;

import java.util.ArrayList;
import pt.aptoide.backupapps.model.InstalledApk;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 20-08-2013
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
public class RefreshInstalledAppsEvent {
  private final ArrayList<InstalledApk> installedApks;
  private final boolean systemApps;

  public RefreshInstalledAppsEvent(ArrayList<InstalledApk> installedApks, boolean systemApps) {
    this.installedApks = installedApks;
    this.systemApps = systemApps;
  }

  public ArrayList<InstalledApk> getInstalledApks() {
    return installedApks;
  }

  public boolean isSystemApps() {
    return systemApps;
  }
}

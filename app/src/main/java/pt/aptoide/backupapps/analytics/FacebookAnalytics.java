package pt.aptoide.backupapps.analytics;

import android.os.Bundle;
import com.facebook.AppEventsLogger;

/**
 * Created by pedroribeiro on 21/08/17.
 */

public class FacebookAnalytics {

  private static final String BACKUP_APPS_STATUS = "Backup_App_Status";
  private static final String UNINSTALL_APPS = "Uninstall_Apps";
  private static final String DRAWER_INTERACT = "Drawer_Interact";
  private final AppEventsLogger facebook;
  private static final String BACKUP_APPS_PRESS = "Backup_Apps_Press";

  public FacebookAnalytics(AppEventsLogger facebook) {
    this.facebook = facebook;
  }

  public void sendBackupAppsClickEvent(int numberOfBackedUpApps) {
    facebook.logEvent(BACKUP_APPS_PRESS, createBackupAppsBundle(numberOfBackedUpApps));
  }

  public void sendBackupAppStatusEvent(String status) {
    facebook.logEvent(BACKUP_APPS_STATUS, createBackupAppStatusBundle(status));
  }

  public void sendUninstallAppsEvent(int numberofUninstallApps) {
    facebook.logEvent(UNINSTALL_APPS, createUninstallAppsBundle(numberofUninstallApps));
  }

  public void sendInstalledTabOpenEvent() {
    facebook.logEvent("Installed_Tab_Open");
  }

  public void sendAvailableTabOpenEvent() {
    facebook.logEvent("Available_Tab_Open");
  }

  public void sendSortAppsEvent(String sortType) {
    sortType = formatString(sortType);
    facebook.logEvent("Sort", createSortEventBundle(sortType));
  }

  public void sendShowSystemApplicationsEvent(boolean checked) {
    facebook.logEvent("Show_System_Applications", createShowSystemApplicationsBundle(checked));
  }

  public void sendDrawerInteract(String overflowOption) {
    overflowOption = formatString(overflowOption);
    facebook.logEvent(DRAWER_INTERACT, createDrawerInteractBundle(overflowOption));
  }

  public void sendSettingsInteractEvent(String action, String automaticBackup, String backupOnWifi,
      String backupOnData, String backupOnEthernet, String backupOnWimax) {
    facebook.logEvent("Setting_Interact",
        createSettingInteractBundle(action, automaticBackup, backupOnWifi, backupOnData,
            backupOnEthernet, backupOnWimax));
  }

  private Bundle createSettingInteractBundle(String action, String automaticBackup,
      String backupOnWifi, String backupOnData, String backupOnEthernet, String backupOnWimax) {
    Bundle bundle = new Bundle();
    bundle.putString("action", action);
    bundle.putString("has_automatic_backup", automaticBackup);
    bundle.putString("allow_backup_on_Wifi", backupOnWifi);
    bundle.putString("allow_backup_on_Data", backupOnData);
    bundle.putString("allow_backup_on_Ethernet", backupOnEthernet);
    bundle.putString("allow_backup_on_WiMax", backupOnWimax);
    return bundle;
  }

  public void sendManagerInteractEvent() {
    facebook.logEvent("Manager_Interact", createManagerInteractBundle());
  }

  private String formatString(String unformattedString) {
    return unformattedString.replace("_", " ")
        .toLowerCase();
  }

  private Bundle createUninstallAppsBundle(int numberofUninstallApps) {
    Bundle bundle = new Bundle();
    bundle.putInt("number_of_selected_apps", numberofUninstallApps);
    return bundle;
  }

  private Bundle createBackupAppStatusBundle(String status) {
    Bundle bundle = new Bundle();
    bundle.putString("status", status);
    return bundle;
  }

  private Bundle createBackupAppsBundle(int numberOfBackedUpApps) {
    Bundle bundle = new Bundle();
    bundle.putInt("number_of_selected_apps", numberOfBackedUpApps);
    return bundle;
  }

  private Bundle createSortEventBundle(String sortType) {
    Bundle bundle = new Bundle();
    bundle.putString("sort_type", sortType);
    return bundle;
  }

  private Bundle createShowSystemApplicationsBundle(boolean checked) {
    Bundle bundle = new Bundle();
    bundle.putString("is_turned_on", String.valueOf(checked));
    return bundle;
  }

  private Bundle createDrawerInteractBundle(String overflowOption) {
    Bundle bundle = new Bundle();
    bundle.putString("action", overflowOption);
    return bundle;
  }

  private Bundle createManagerInteractBundle() {
    Bundle bundle = new Bundle();
    bundle.putString("action", "clear completed");
    return bundle;
  }

  public enum SORT_TYPE {
    ALPHABETICALLY, BY_SIZE, MOST_RECENT_FIRST, BY_STATE
  }

  public enum OVERFLOW {
    SETTINGS, MANAGER
  }

  public enum SETTINGS {
    AUTOMATIC_BACKUP, LOGIN, LOGOUT, BACKUP_UPLOAD_PERMISSION5
  }
}

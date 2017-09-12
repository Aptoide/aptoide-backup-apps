package pt.aptoide.backupapps;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.AppEventsLogger;
import pt.aptoide.backupapps.analytics.FacebookAnalytics;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 30-07-2013
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
public class Settings extends BaseSherlockPreferenceActivity {

  private FacebookAnalytics facebookAnalytics;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    facebookAnalytics = new FacebookAnalytics(AppEventsLogger.newLogger(getApplicationContext()));
    addPreferencesFromResource(R.xml.preferences);

    if (Build.VERSION.SDK_INT >= 11) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);

    PackageInfo pInfo = null;
    String version = "";
    try {
      pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
      version = "v" + pInfo.versionName + " (" + pInfo.versionCode + ")";
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    ((Preference) findPreference("version_id")).setTitle(version);

    if (sPref.contains(Constants.LOGIN_USER_LOGIN)) {
      Preference preference = new Preference(this);
      preference.setOrder(-1);
      preference.setSelectable(false);
      preference.setTitle(getString(R.string.settings_title_logged_as, sPref.getString(Constants.LOGIN_USER_LOGIN, "")));
      ((PreferenceCategory) findPreference("login_cat")).addPreference(preference);

      findPreference("set_server_login").setOnPreferenceClickListener(
          new Preference.OnPreferenceClickListener() {
            @Override public boolean onPreferenceClick(Preference preference) {
              new Logout(Settings.this).execute();
              return false;
            }
          });
    } else {
      findPreference("set_server_login").setTitle(R.string.settings_login_text);
      findPreference("set_server_login").setSummary(R.string.settings_login_description_text);
      findPreference("set_server_login").setOnPreferenceClickListener(
          new Preference.OnPreferenceClickListener() {
            @Override public boolean onPreferenceClick(Preference preference) {
              BusProvider.getInstance()
                  .post(new LoginMoveEvent());
              finish();
              return false;
            }
          });
    }
    findPreference("automatic_install").setOnPreferenceClickListener(
        new Preference.OnPreferenceClickListener() {
          @Override public boolean onPreferenceClick(Preference preference) {
            facebookAnalytics.sendSettingsInteractEvent(
                String.valueOf(FacebookAnalytics.SETTINGS.AUTOMATIC_BACKUP),
                String.valueOf(!getAutomaticInstallValue()), String.valueOf(!getBackupOnWifi()),
                String.valueOf(!getBackupOnData()), String.valueOf(!getBackupOnEthernet()),
                String.valueOf(!getBackupOnWimax()));
            return false;
          }
        });
    findPreference("icon_download_permissions").setOnPreferenceClickListener(
        new Preference.OnPreferenceClickListener() {
          @Override public boolean onPreferenceClick(Preference preference) {
            facebookAnalytics.sendSettingsInteractEvent(
                String.valueOf(FacebookAnalytics.SETTINGS.BACKUP_UPLOAD_PERMISSION5),
                String.valueOf(getAutomaticInstallValue()), String.valueOf(getBackupOnWifi()),
                String.valueOf(getBackupOnData()), String.valueOf(getBackupOnEthernet()),
                String.valueOf(getBackupOnWimax()));
            return false;
          }
        }); findPreference("set_server_login").setOnPreferenceClickListener(
        new Preference.OnPreferenceClickListener() {
          @Override public boolean onPreferenceClick(Preference preference) {
            String prefTitle = findPreference("set_server_login").getTitle()
                .toString();
            facebookAnalytics.sendSettingsInteractEvent(
                prefTitle.equals(getString(R.string.settings_button_logout)) ? String.valueOf(
                    FacebookAnalytics.SETTINGS.LOGOUT)
                    : String.valueOf(FacebookAnalytics.SETTINGS.LOGIN),
                String.valueOf(getAutomaticInstallValue()), String.valueOf(getBackupOnWifi()),
                String.valueOf(getBackupOnData()), String.valueOf(getBackupOnEthernet()),
                String.valueOf(getBackupOnWimax()));
            return false;
          }
        });
  }

  @Override public boolean onMenuItemSelected(int featureId, MenuItem item) {

    switch (item.getItemId()) {

      case R.id.abs__home:
        finish();
        break;
      case android.R.id.home:
        finish();
        break;
    }

    return super.onMenuItemSelected(featureId, item);
  }

  private boolean getAutomaticInstallValue() {
    return ((CheckBoxPreference) findPreference("automatic_install")).isChecked();
  }

  private boolean getBackupOnWifi() {
    return ((CheckBoxPreference) findPreference("prefer_wifi")).isChecked();
  }

  private boolean getBackupOnData() {
    return ((CheckBoxPreference) findPreference("prefer_mobile_data")).isChecked();
  }

  private boolean getBackupOnEthernet() {
    return ((CheckBoxPreference) findPreference("prefer_ethernet")).isChecked();
  }

  private boolean getBackupOnWimax() {
    return ((CheckBoxPreference) findPreference("prefer_wimax")).isChecked();
  }
}

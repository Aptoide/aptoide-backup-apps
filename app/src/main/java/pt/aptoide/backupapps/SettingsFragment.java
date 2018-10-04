package pt.aptoide.backupapps;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import com.facebook.AppEventsLogger;
import pt.aptoide.backupapps.analytics.FacebookAnalytics;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.util.Constants;

public class SettingsFragment extends PreferenceFragment {
  private FacebookAnalytics facebookAnalytics;
  private SharedPreferences sharedPreferences;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);

    facebookAnalytics =
        new FacebookAnalytics(AppEventsLogger.newLogger(getActivity().getApplicationContext()));
    sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

    PackageInfo pInfo = null;
    String version = "";
    try {
      pInfo = getActivity().getPackageManager()
          .getPackageInfo(getActivity().getPackageName(), 0);
      version = "v" + pInfo.versionName + " (" + pInfo.versionCode + ")";
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    ((Preference) findPreference("version_id")).setTitle(version);

    if (sharedPreferences.contains(Constants.LOGIN_USER_LOGIN)) {
      Preference preference = new Preference(getActivity().getApplicationContext());
      preference.setOrder(-1);
      preference.setSelectable(false);
      String string = getString(R.string.settings_title_logged_as,
          sharedPreferences.getString(Constants.LOGIN_USER_LOGIN, ""));
      SpannableString styledString = new SpannableString(string);
      styledString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, styledString.length(), 0);
      preference.setTitle(styledString);
      ((PreferenceCategory) findPreference("login_cat")).addPreference(preference);
    } else {
      findPreference("set_server_login").setTitle(R.string.settings_login_text);
      findPreference("set_server_login").setSummary(R.string.settings_login_description_text);
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
        });
    findPreference("set_server_login").setOnPreferenceClickListener(
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

  @Override
  public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
    if (preference.getKey()
        .equals("set_server_login")) {
      if (sharedPreferences.contains(Constants.LOGIN_USER_LOGIN)) {
        new Logout(getActivity()).execute();
      } else {
        BusProvider.getInstance()
            .post(new LoginMoveEvent());
        getActivity().finish();
      }
    }

    return super.onPreferenceTreeClick(preferenceScreen, preference);
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

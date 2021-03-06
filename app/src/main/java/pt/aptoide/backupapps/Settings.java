package pt.aptoide.backupapps;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import com.actionbarsherlock.view.MenuItem;
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

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);

    if (Build.VERSION.SDK_INT >= 11) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);

    if (sPref.contains(Constants.LOGIN_USER_LOGIN)) {
      Preference preference = new Preference(this);
      preference.setOrder(-1);
      preference.setSelectable(false);
      preference.setTitle(
          getString(R.string.settings_logged_text) + sPref.getString(Constants.LOGIN_USER_LOGIN,
              ""));
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
    try {
      findPreference("version_name").setTitle(
          String.format(getString(R.string.settings_version_text),
              getPackageManager().getPackageInfo(getPackageName(), 0).versionName)
              + " "
              + "("
              + getPackageManager().getPackageInfo(getPackageName(), 0).versionCode
              + ")");
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
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
}

package pt.aptoide.backupapps;

import android.preference.PreferenceActivity;
import pt.aptoide.backupapps.analytics.Analytics;

/**
 * Created by neuro on 09-02-2016.
 */
public class BaseSherlockPreferenceActivity extends PreferenceActivity {

  @Override protected void onResume() {
    super.onResume();
    Analytics.Lifecycle.Activity.onResume(this);
  }

  @Override protected void onPause() {
    Analytics.Lifecycle.Activity.onPause(this);
    super.onPause();
  }
}

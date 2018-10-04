package pt.aptoide.backupapps;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import pt.aptoide.backupapps.analytics.Analytics;

/**
 * Created by neuro on 09-02-2016.
 */
public class BaseSherlockPreferenceActivity extends SherlockPreferenceActivity {

  @Override protected void onResume() {
    super.onResume();
    Analytics.Lifecycle.Activity.onResume(this);
  }

  @Override protected void onPause() {
    Analytics.Lifecycle.Activity.onPause(this);
    super.onPause();
  }
}

package pt.aptoide.backupapps;

import android.support.v7.app.AppCompatActivity;
import pt.aptoide.backupapps.analytics.Analytics;

/**
 * Created by neuro on 09-02-2016.
 */
public class BaseSherlockFragmentActivity extends AppCompatActivity {

  @Override protected void onPause() {
    Analytics.Lifecycle.Activity.onPause(this);
    super.onPause();
  }

  @Override protected void onResume() {
    super.onResume();
    Analytics.Lifecycle.Activity.onResume(this);
  }
}

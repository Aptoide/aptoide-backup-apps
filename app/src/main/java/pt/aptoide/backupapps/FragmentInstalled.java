package pt.aptoide.backupapps;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.actionbarsherlock.app.SherlockListFragment;
import com.facebook.AppEventsLogger;
import com.manuelpeinado.multichoiceadapter.CheckableRelativeLayout;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Iterator;
import pt.aptoide.backupapps.analytics.FacebookAnalytics;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.model.InstalledApk;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 20-08-2013
 * Time: 15:37
 * To change this template use File | Settings | File Templates.
 */
public class FragmentInstalled extends SherlockListFragment {

  private ArrayList<InstalledApk> installedApks = new ArrayList<InstalledApk>(50);
  private ArrayList<Integer> backedUpApps = new ArrayList<Integer>();
  private FacebookAnalytics facebookAnalytics;

  @Override public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    BusProvider.getInstance()
        .register(this);
    facebookAnalytics =
        new FacebookAnalytics(AppEventsLogger.newLogger(getActivity().getApplicationContext()));
  }

  @Override public void onDestroy() {
    super.onDestroy();
    BusProvider.getInstance()
        .unregister(this);
  }

  private InstalledAdapter installedAdapter;

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    installedAdapter =
        new InstalledAdapter(savedInstanceState, getSherlockActivity(), 0, installedApks,
            backedUpApps, facebookAnalytics);
    setListAdapter(installedAdapter);
    installedAdapter.setAdapterView(getListView());
    installedAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((CheckableRelativeLayout) view).setChecked(!((CheckableRelativeLayout) view).isChecked());
      }
    });
    getListView().setFastScrollEnabled(true);
    setListShown(false);
  }

  @Subscribe public void onRefreshBackedUpApps(RefreshInstalledBackedUpAppsEvent event) {
    backedUpApps.clear();
    backedUpApps.addAll(event.getInstalledBackedUpApps());
    installedAdapter.notifyDataSetChanged();
  }

  @Subscribe public void onRefreshInstalledApps(RefreshInstalledAppsEvent event) {
    installedApks.clear();
    if (!event.isSystemApps()) {
      for (Iterator<InstalledApk> iterator = event.getInstalledApks()
          .iterator(); iterator.hasNext(); ) {
        InstalledApk apk = iterator.next();
        if (!apk.isSystemApp()) {
          installedApks.add(apk);
        }
      }
    } else {
      installedApks.addAll(event.getInstalledApks());
    }
    installedAdapter.notifyDataSetChanged();
    if (getListView() != null) {
      setListShown(true);
    }
  }

  @Subscribe public void onLoginEvent(LoginEvent event) {
    Log.d("TAG", "fragment installed onLogin " + installedAdapter.getLocalCheckedItems());
    if (!installedAdapter.getLocalCheckedItems()
        .isEmpty()) {
      Log.d("TAG", "fragment installing checked items");
      installedAdapter.installCheckedItems();
    }
  }

  @Subscribe public void onActionModeFinished(ActionModeFinishEvent event) {
    installedAdapter.finishActionMode();
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    getListView().setCacheColorHint(0);
  }
}

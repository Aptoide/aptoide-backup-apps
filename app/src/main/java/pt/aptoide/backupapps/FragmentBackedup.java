package pt.aptoide.backupapps;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.actionbarsherlock.app.SherlockListFragment;
import com.manuelpeinado.multichoiceadapter.CheckableRelativeLayout;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.model.InstalledApk;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 02-08-2013
 * Time: 15:55
 * To change this template use File | Settings | File Templates.
 */
public class FragmentBackedup extends SherlockListFragment
    implements LoaderManager.LoaderCallbacks<Cursor> {

  BackedUpCursorAdapter backedUpAdapter;
  private Context context;
  private ArrayList<Integer> backedUpApps = new ArrayList<Integer>();

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return super.onCreateView(inflater, container,
        savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    backedUpAdapter =
        new BackedUpCursorAdapter(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
            backedUpApps);//To change body of overridden methods use File | Settings | File Templates.
    backedUpAdapter.setAdapterView(getListView());

    Bundle bundle = new Bundle();
    bundle.putInt("order", MainActivity.currentSort.ordinal());
    getLoaderManager().restartLoader(0, bundle, this);
    backedUpAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((CheckableRelativeLayout) view).setChecked(!((CheckableRelativeLayout) view).isChecked());
      }
    });
    setListAdapter(backedUpAdapter);
    getListView().setFastScrollEnabled(true);
    //setListShown(false);

    setEmptyText(getSherlockActivity().getString(R.string.backed_up_short_no_apps));
    getListView().setCacheColorHint(0);
  }

  @Override public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    BusProvider.getInstance()
        .register(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    BusProvider.getInstance()
        .unregister(this);
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    context = activity;
  }

  @Override public void onDetach() {
    setListAdapter(null);
    super.onDetach();    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Subscribe public void onRefresh(BackedUpRefreshEvent event) {

    Bundle bundle = new Bundle();
    bundle.putInt("order", event.getSort()
        .ordinal());
    getLoaderManager().restartLoader(0, bundle, this);
  }

  @Subscribe public void onRefreshBackedUpApps(final RefreshInstalledAppsEvent event) {
    new Thread(new Runnable() {
      @Override public void run() {
        backedUpApps.clear();

        ArrayList<Integer> backedAppsHashes = new ArrayList<Integer>(50);

        for (InstalledApk apk : event.getInstalledApks()) {
          backedAppsHashes.add((apk.getPackageName() + apk.getVersionCode()).hashCode());
        }

        backedUpApps.addAll(backedAppsHashes);
        getSherlockActivity().runOnUiThread(new Runnable() {
          @Override public void run() {
            backedUpAdapter.notifyDataSetChanged();
          }
        });
      }
    }).start();
  }

  @Subscribe public void onActionModeFinish(ActionModeFinishEvent event) {
    backedUpAdapter.finishActionMode();
  }

  @Override
  public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, final Bundle bundle) {
    return new SimpleCursorLoader(getSherlockActivity()) {
      @Override public Cursor loadInBackground() {
        return Database.getInstance()
            .getAvailable(bundle.getInt(
                "order"));  //To change body of implemented methods use File | Settings | File Templates.
      }
    };
  }

  @Override
  public void onLoadFinished(android.support.v4.content.Loader<Cursor> objectLoader, Cursor o) {
    backedUpAdapter.swapCursor(o);
    //if(!backedUpAdapter.isEmpty()){
    //  setListShown(true);
    //}
  }

  @Override public void onLoaderReset(android.support.v4.content.Loader<Cursor> objectLoader) {
    backedUpAdapter.swapCursor(null);
  }

  @Subscribe public void onStartParse(StartParseEvent event) {
    if (backedUpAdapter.isEmpty() && getListView() != null) {
      Log.d("TAG", "StartParseEvent");
      setListShown(false);
    }
  }

  @Subscribe public void onStopParse(StopParseEvent event) {
    Log.d("TAG", "StopParseEvent");

    if (getView() != null) {
      setListShown(true);
      if (event.isError()) {
        setEmptyText(getString(R.string.backed_up_apps_message_no_network_connection));
      }
    }
  }
}

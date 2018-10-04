package pt.aptoide.backupapps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.facebook.AppEventsLogger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import pt.aptoide.backupapps.analytics.FacebookAnalytics;
import pt.aptoide.backupapps.download.DownloadInfo;
import pt.aptoide.backupapps.download.DownloadManager;
import pt.aptoide.backupapps.download.EnumDownloadFailReason;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.download.event.DownloadStatusEvent;
import pt.aptoide.backupapps.download.event.UploadStatusEvent;
import pt.aptoide.backupapps.download.state.EnumUploadFailReason;
import pt.aptoide.backupapps.download.state.ErrorState;
import pt.aptoide.backupapps.download.state.UploadErrorState;
import pt.aptoide.backupapps.model.InstalledApk;
import pt.aptoide.backupapps.model.RepoApk;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 01-08-2013
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class Manager extends BaseSherlockActivity {

  ArrayAdapter<DownloadInfo> adapter;
  private ListView onGoingListView;
  private ArrayAdapter<DownloadInfo> adapter2;
  private ListView notOngoingListView;
  private ArrayList<DownloadInfo> activeList;
  private ArrayList<DownloadInfo> inactiveList;
  private TextView onGoingTextView;
  private TextView notOngoingTextView;
  private TextView noDownloads;
  private FacebookAnalytics facebookAnalytics;

  @Subscribe public void onDownloadInfo(DownloadInfo info) {

    Log.d("TAG", DownloadManager.INSTANCE.mActiveList.size() + " active");
    Log.d("TAG", DownloadManager.INSTANCE.mInactiveList.size() + " inactive");
    Log.d("TAG", DownloadManager.INSTANCE.mErrorList.size() + " error");
    Log.d("TAG", DownloadManager.INSTANCE.mPendingList.size() + " pending");
    Log.d("TAG", DownloadManager.INSTANCE.mCompletedList.size() + " completed");
    Log.d("TAG", DownloadManager.INSTANCE.mOngoingList.size() + " ongoing");
    Log.d("TAG", DownloadManager.INSTANCE.mNotOngoingList.size() + " notongoing");

    adapter.notifyDataSetChanged();
    adapter2.notifyDataSetChanged();

    refreshLists();
  }

  private void refreshLists() {
    if (DownloadManager.INSTANCE.mNotOngoingList.isEmpty()) {
      notOngoingTextView.setVisibility(View.GONE);
      notOngoingListView.setVisibility(View.GONE);
    } else {
      notOngoingTextView.setVisibility(View.VISIBLE);
      notOngoingListView.setVisibility(View.VISIBLE);
    }

    if (DownloadManager.INSTANCE.mOngoingList.isEmpty()) {
      onGoingTextView.setVisibility(View.GONE);
      onGoingListView.setVisibility(View.GONE);
    } else {
      onGoingTextView.setVisibility(View.VISIBLE);
      onGoingListView.setVisibility(View.VISIBLE);
    }

    if (DownloadManager.INSTANCE.mOngoingList.isEmpty()
        && DownloadManager.INSTANCE.mNotOngoingList.isEmpty()) {
      noDownloads.setVisibility(View.VISIBLE);
    } else {
      noDownloads.setVisibility(View.GONE);
    }
  }

  @Subscribe public void onDownloadEvent(DownloadStatusEvent event) {

    inactiveList.clear();
    inactiveList.addAll(DownloadManager.INSTANCE.mNotOngoingList);

    activeList.clear();
    activeList.addAll(DownloadManager.INSTANCE.mOngoingList);

    adapter.notifyDataSetChanged();
    adapter2.notifyDataSetChanged();
    refreshLists();
  }

  @Subscribe public void onUploadEvent(UploadStatusEvent event) {
    inactiveList.clear();
    inactiveList.addAll(DownloadManager.INSTANCE.mNotOngoingList);

    activeList.clear();
    activeList.addAll(DownloadManager.INSTANCE.mOngoingList);

    adapter2.notifyDataSetChanged();
    adapter.notifyDataSetChanged();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.manager, menu);

    return super.onCreateOptionsMenu(
        menu);    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.clear_all:
        DownloadManager.INSTANCE.mNotOngoingList.clear();
        onDownloadEvent(null);
        facebookAnalytics.sendManagerInteractEvent();
        break;
      //case R.id.abs__home:
      case android.R.id.home:
        finish();
        break;
    }
    return super.onOptionsItemSelected(
        item);    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.manager);
    this.onGoingListView = (ListView) findViewById(R.id.active);
    this.notOngoingListView = (ListView) findViewById(R.id.inactive);
    BusProvider.getInstance()
        .register(this);
    activeList = new ArrayList<DownloadInfo>(DownloadManager.INSTANCE.mOngoingList);
    //getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    facebookAnalytics = new FacebookAnalytics(AppEventsLogger.newLogger(getApplicationContext()));
    onGoingListView = (ListView) findViewById(R.id.active);
    notOngoingListView = (ListView) findViewById(R.id.inactive);

    onGoingTextView = (TextView) findViewById(R.id.downloading_intro);
    notOngoingTextView = (TextView) findViewById(R.id.downloaded_intro);

    noDownloads = (TextView) findViewById(R.id.no_downloads);

    adapter = new ArrayAdapter<DownloadInfo>(this, 0, activeList) {

      @Override public View getView(int position, View convertView, ViewGroup parent) {

        View v;

        if (convertView == null) {
          v = getLayoutInflater().inflate(R.layout.row_app_uploading, null);
        } else {
          v = convertView;
        }

        if (getItem(position).getApk() instanceof RepoApk) {
          ImageLoader.getInstance()
              .displayImage(((RepoApk) getItem(position).getApk()).getIconPath(),
                  (ImageView) v.findViewById(R.id.uploading_icon));
        } else if (getItem(position).getApk() instanceof InstalledApk) {
          ((ImageView) v.findViewById(R.id.uploading_icon)).setImageDrawable(
              ((InstalledApk) getItem(position).getApk()).getIcon());
        }
        ((TextView) v.findViewById(R.id.uploading_name)).setText(getItem(position).getApk()
            .getName());
        if (getItem(position).getPercentDownloaded() == 0) {
          ((ProgressBar) v.findViewById(R.id.uploading_progress)).setIndeterminate(true);
        } else {
          ((ProgressBar) v.findViewById(R.id.uploading_progress)).setIndeterminate(false);
        }
        ((ProgressBar) v.findViewById(R.id.uploading_progress)).setProgress(
            getItem(position).getPercentDownloaded());

        return v;    //To change body of overridden methods use File | Settings | File Templates.
      }
    };
    this.onGoingListView.setAdapter(adapter);

    inactiveList = new ArrayList<DownloadInfo>(DownloadManager.INSTANCE.mNotOngoingList);
    adapter2 = new ArrayAdapter<DownloadInfo>(this, 0, inactiveList) {

      @Override public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("MYLOG", getItem(position).getStatusState()
            .getClass()
            .getCanonicalName());
        View v;

        if (convertView == null) {
          v = getLayoutInflater().inflate(R.layout.row_app_uploaded, null);
        } else {
          v = convertView;
        }

        if (getItem(position).getApk() instanceof RepoApk) {
          ImageLoader.getInstance()
              .displayImage(((RepoApk) getItem(position).getApk()).getIconPath(),
                  (ImageView) v.findViewById(R.id.uploaded_icon));
        } else if (getItem(position).getApk() instanceof InstalledApk) {
          ((ImageView) v.findViewById(R.id.uploaded_icon)).setImageDrawable(
              ((InstalledApk) getItem(position).getApk()).getIcon());
        }
        ((TextView) v.findViewById(R.id.uploaded_name)).setText(getItem(position).getApk()
            .getName());

        if (getItem(position).getStatusState() instanceof UploadErrorState) {
          ArrayList<EnumUploadFailReason> failReasons =
              ((UploadErrorState) getItem(position).getStatusState()).getErrorMessage();
          Log.d("MYLOG", failReasons.toString() + " getted now!");
          String errors = failReasons.get(0)
              .toString(getContext());

          for (int i = 1; i != failReasons.size(); i++) {
            errors = errors + ", " + failReasons.get(i)
                .toString(getContext());
          }
          Log.d("MYLOG", failReasons.toString() + " getted now! " + errors);

          ((TextView) v.findViewById(R.id.failed_status)).setText("Error: " + errors);
        } else if (getItem(position).getStatusState() instanceof ErrorState) {
          EnumDownloadFailReason error =
              ((ErrorState) getItem(position).getStatusState()).getErrorMessage();

          ((TextView) v.findViewById(R.id.failed_status)).setText(
              "Error: " + error.toString(getContext()));
        } else {
          ((TextView) v.findViewById(R.id.failed_status)).setText("");
        }

        return v;    //To change body of overridden methods use File | Settings | File Templates.
      }
    };
    this.notOngoingListView.setAdapter(adapter2);
    this.notOngoingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DownloadInfo info = adapter2.getItem(position);
        if (info.getStatusState() instanceof UploadErrorState) {

          if (((UploadErrorState) info.getStatusState()).getErrorMessage()
              .contains(EnumUploadFailReason.MISSING_DESCRIPTION)
              || ((UploadErrorState) info.getStatusState()).getErrorMessage()
              .contains(EnumUploadFailReason.MISSING_APK_NAME)
              || ((UploadErrorState) info.getStatusState()).getErrorMessage()
              .contains(EnumUploadFailReason.MISSING_RATING)
              || ((UploadErrorState) info.getStatusState()).getErrorMessage()
              .contains(EnumUploadFailReason.MISSING_CATEGORY)
              || ((UploadErrorState) info.getStatusState()).getErrorMessage()
              .contains(EnumUploadFailReason.BAD_EMAIL)
              || ((UploadErrorState) info.getStatusState()).getErrorMessage()
              .contains(EnumUploadFailReason.BAD_WEBSITE)
              || ((UploadErrorState) info.getStatusState()).getErrorMessage()
              .contains(EnumUploadFailReason.APK_NOT_FOUND)) {

            Intent t = new Intent(Manager.this, SubmitForm.class);
            t.putExtra("id", position);

            startActivityForResult(t, 0);
          }
        }
      }
    });

    onDownloadInfo(null);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    BusProvider.getInstance()
        .unregister(this);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    onDownloadEvent(null);
  }
}

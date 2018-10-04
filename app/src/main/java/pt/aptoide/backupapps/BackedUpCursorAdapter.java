package pt.aptoide.backupapps;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.manuelpeinado.multichoiceadapter.ItemClickInActionModePolicy;
import com.manuelpeinado.multichoiceadapter.MultiChoiceAdapter;
import com.manuelpeinado.multichoiceadapter.MultiChoiceAdapterHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.database.Schema;
import pt.aptoide.backupapps.download.DownloadManager;
import pt.aptoide.backupapps.download.Utils;
import pt.aptoide.backupapps.model.RepoApk;
import pt.aptoide.backupapps.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 29-07-2013
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public class BackedUpCursorAdapter extends CursorAdapter
    implements MultiChoiceAdapter, ActionMode.Callback {

  public MultiChoiceAdapterHelper helper = new MultiChoiceAdapterHelper(this);
  ArrayList<Integer> backedUpApps;
  SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
  private String iconPath;

  public BackedUpCursorAdapter(Context context, Cursor c, int flags,
      ArrayList<Integer> backedUpApps) {
    super(context, c, flags);
    this.backedUpApps = backedUpApps;
    iconPath = Database.getInstance()
        .getIconsPath();
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {

    View viewWithoutSelection = super.getView(position, convertView, parent);

    return helper.getView(position, viewWithoutSelection);
  }

  @Override public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
    return LayoutInflater.from(context)
        .inflate(R.layout.row_app_backup, null);
  }

  @Override public void bindView(View view, Context context, Cursor cursor) {

    ViewHolder viewHolder = (ViewHolder) view.getTag();

    if (viewHolder == null) {
      viewHolder = new ViewHolder();
      viewHolder.status = (TextView) view.findViewById(R.id.status);
      viewHolder.timestamp = (TextView) view.findViewById(R.id.timestamp);
      viewHolder.appName = (TextView) view.findViewById(R.id.app_name);
      viewHolder.versionName = (TextView) view.findViewById(R.id.version_name);
      viewHolder.size = (TextView) view.findViewById(R.id.size);
      viewHolder.appIcon = (ImageView) view.findViewById(R.id.app_icon);
      view.setTag(viewHolder);
    }

    int hashCode = (cursor.getString(cursor.getColumnIndex(Schema.PACKAGE_NAME)) + cursor.getString(
        cursor.getColumnIndex(Schema.VERSION_CODE))).hashCode();

    if (backedUpApps.contains(hashCode)) {
      viewHolder.status.setText("Installed");
    } else {
      viewHolder.status.setText("");
    }

    viewHolder.timestamp.setText(
        df.format(new Date(cursor.getLong(cursor.getColumnIndex(Schema.DATE)))));
    viewHolder.versionName.setText(cursor.getString(cursor.getColumnIndex(Schema.VERSION_NAME)));
    viewHolder.appName.setText(cursor.getString(cursor.getColumnIndex(Schema.NAME)));
    //viewHolder.versionName.setText(cursor.getString(cursor.getColumnIndex(Schema.VERSION_NAME)));

    int size = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Schema.SIZE)));
    viewHolder.size.setText(Utils.formatBytes(size * 1024));

    ImageLoader.getInstance()
        .displayImage(iconPath + cursor.getString(cursor.getColumnIndex(Schema.ICON_PATH)),
            viewHolder.appIcon);
  }

  public void setAdapterView(AdapterView<? super BaseAdapter> adapterView) {
    helper.setAdapterView(adapterView);
  }

  /**
   * Register a callback to be invoked when an item in the associated AdapterView has been clicked
   *
   * @param listener The callback that will be invoked
   */
  public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
    helper.setOnItemClickListener(listener);
  }

  /**
   * Always call this method from your activity's onSaveInstanceState method. This is necessary for
   * the adapter to
   * retain its selection in the event of a configuration change
   *
   * @param outState The same bundle you are passed in onSaveInstanceState
   */
  public void save(Bundle outState) {
    helper.save(outState);
  }

  /**
   * Changes the selection of an item. If the item was already in the specified state, nothing is
   * done. May cause the
   * activation of the action mode if an item is selected an no items were previously selected
   *
   * @param position The position of the item to select
   * @param checked The desired state (selected or not) for the item
   */
  public void setItemChecked(long position, boolean checked) {
    helper.setItemChecked(position, checked);
  }

  /**
   * Returns the indices of the currently selectly items.
   *
   * @return Indices of the currently selectly items. The empty set if no item is selected
   */
  public Set<Long> getCheckedItems() {
    return helper.getCheckedItems();
  }

  /**
   * Returns the number of selected items
   *
   * @return Number of selected items
   */
  public int getCheckedItemCount() {
    return helper.getCheckedItemCount();
  }

  /**
   * Returns true if the item at the specified position is selected
   *
   * @param position The item position
   *
   * @return Whether the item is selected
   */
  public boolean isChecked(long position) {
    return helper.isChecked(position);
  }

  /**
   * Subclasses can invoke this method in order to finish the action mode. This has the side effect
   * of unselecting all
   * items
   */
  protected void finishActionMode() {
    helper.finishActionMode();
  }

  /**
   * Convenience method for subclasses that need an activity context
   */
  protected Context getContext() {
    return helper.getContext();
  }

  @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
    MenuInflater inflater = mode.getMenuInflater();
    mode.setTitle("Download");
    inflater.inflate(R.menu.download, menu);
    return true;  //To change body of implemented methods use File | Settings | File Templates.
  }  public void setItemClickInActionModePolicy(ItemClickInActionModePolicy policy) {
    helper.setItemClickInActionModePolicy(policy);
  }

  @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    ArrayList<Long> selectedList;

    switch (item.getItemId()) {
      case R.id.menu_download:
        selectedList = new ArrayList<Long>(getCheckedItems());

        for (Iterator<Long> iterator = selectedList.iterator(); iterator.hasNext(); ) {
          Long id = iterator.next();
          if (!DownloadManager.INSTANCE.mIds.contains(id)) {
            RepoApk apk = Database.getInstance()
                .getApk(getItemId(id.intValue()));
            apk.setRepoName(PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(Constants.LOGIN_USER_DEFAULT_REPO, null));
            new CheckPermissions(selectedList, getContext()).execute(apk);
          }
        }
        mode.finish();

        break;
    }

    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override public void onDestroyActionMode(ActionMode mode) {
    helper.onDestroyActionMode(mode);
  }  public ItemClickInActionModePolicy getItemClickInActionModePolicy() {
    return helper.getItemClickInActionModePolicy();
  }

  public MultiChoiceAdapterHelper getActionMode() {
    return helper;
  }

  @Override public void notifyDataSetChanged() {
    super.notifyDataSetChanged();
    iconPath = Database.getInstance()
        .getIconsPath();
  }

  //
  // ActionMode.Callback implementation
  //

  //
  // MultiChoiceAdapter implementation
  //

  static class ViewHolder {
    public TextView status;
    public TextView timestamp;
    TextView appName;
    TextView versionName;
    TextView size;
    ImageView appIcon;
  }



  @Override public boolean isItemCheckable(int position) {
    return true;
  }


}

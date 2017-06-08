package pt.aptoide.backupapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.multichoiceadapter.MultiChoiceAdapterHelper;
import com.manuelpeinado.multichoiceadapter.MultiChoiceBaseAdapter;
import pt.aptoide.backupapps.download.*;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.model.InstalledApk;
import pt.aptoide.backupapps.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 29-07-2013
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public class InstalledAdapter extends MultiChoiceBaseAdapter {


    private final ArrayList<InstalledApk> objects;
    private final Context context;
    private ArrayList<Integer> backedupPackages;
    private ArrayList<Long> localCheckedItems = new ArrayList<Long>();

    public InstalledAdapter(Bundle savedInstance, Context context, int resource, ArrayList<InstalledApk> objects, ArrayList<Integer> backedupPackages) {
        super(null);
        this.backedupPackages = backedupPackages;
        this.objects = objects;
        this.context = context;
    }



    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public View getViewImpl(int position, View convertView, ViewGroup parent) {


        View v;
        ViewHolder holder;
        if (convertView == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.row_app_backup, null);
            holder = new ViewHolder();
            holder.appIcon = (ImageView) v.findViewById(R.id.app_icon);
            holder.appName = (TextView) v.findViewById(R.id.app_name);
            holder.versionName = (TextView) v.findViewById(R.id.version_name);
            holder.status = (TextView) v.findViewById(R.id.status);
            holder.timestamp = (TextView) v.findViewById(R.id.timestamp);
            holder.size = (TextView) v.findViewById(R.id.size);
            v.setTag(holder);
        } else {
            v = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        if (getItem(position).isBackedUp()) {
            holder.status.setText("Backed Up");
        } else {
            holder.status.setText("");
        }
        holder.versionName.setText(getItem(position).getVersionName());
        holder.appName.setText(getItem(position).getName());
        holder.appIcon.setImageDrawable(getItem(position).getIcon());



        holder.timestamp.setText(df.format(new Date(getItem(position).getDate())));
        holder.size.setText(Utils.formatBytes(getItem(position).getSize()));
        return v;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.backup, menu);
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        ArrayList<Long> items;
        switch (item.getItemId()) {
            case R.id.menu_backup:


                SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(context);

                if (sPrefs.contains(Constants.LOGIN_USER_LOGIN)) {

                    items = new ArrayList<Long>(getCheckedItems());

                    for (Long id : items) {
                        if (!DownloadManager.INSTANCE.mIds.contains(id)) {
                            InstalledApk apk = getItem(id.intValue());
                            UploadFiles files = new UploadFiles(apk.getApkFile(), apk.getMainObbFile(), apk.getPatchObbFile());
                            ApkMetaData metaData = new ApkMetaData();
                            metaData.setName(apk.getName());
                            metaData.setPackageName(apk.getPackageName());

                            UploadModel model = new UploadModel(files, metaData, sPrefs.getString(Constants.LOGIN_USER_TOKEN, ""), sPrefs.getString(Constants.LOGIN_USER_DEFAULT_REPO, ""));
                            DownloadInfo info = DownloadManager.INSTANCE.getDownloadInfo(null,(apk.getPackageName() + apk.getVersionCode()).hashCode(), apk);
                            info.setUploadModel(model);
                            info.download();
                        }
                    }
                    context.startActivity(new Intent(context, Manager.class));

                } else {
                    localCheckedItems.clear();
                    localCheckedItems.addAll(getCheckedItems());
                    Toast.makeText(getContext(), R.string.backup_message_login_to_backup, Toast.LENGTH_LONG).show();
                    BusProvider.getInstance().post(new LoginMoveEvent());
                }
                mode.finish();

                break;
            case R.id.menu_uninstall:
                items = new ArrayList<Long>(getCheckedItems());
                for (Long id : items) {
                    InstalledApk apk = getItem(id.intValue());

                    Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package",
                            context.getPackageManager().getPackageArchiveInfo(apk.getPath(), 0).packageName, null));
                    context.startActivity(intent);

                }

                finishActionMode();


                break;
        }

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCount() {
        return objects.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public InstalledApk getItem(int position) {
        return objects.get(position);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void finishActionMode() {
        helper.finishActionMode();
    }

    public MultiChoiceAdapterHelper getActionMode() {
        return helper;
    }

    public void installCheckedItems() {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        ArrayList<Long> items = new ArrayList<Long>(localCheckedItems);

        for (Long id : items) {
            if (!DownloadManager.INSTANCE.mIds.contains(id)) {
                InstalledApk apk = getItem(id.intValue());
                UploadFiles files = new UploadFiles(apk.getApkFile(), apk.getMainObbFile(), apk.getPatchObbFile());
                ApkMetaData metaData = new ApkMetaData();
                metaData.setName(apk.getName());
                metaData.setPackageName(apk.getPackageName());
                UploadModel model = new UploadModel(files, metaData, sPrefs.getString(Constants.LOGIN_USER_TOKEN, ""), sPrefs.getString(Constants.LOGIN_USER_DEFAULT_REPO, ""));
                DownloadInfo info = DownloadManager.INSTANCE.getDownloadInfo(null,(apk.getPackageName() + apk.getVersionCode()).hashCode(), apk);
                info.setUploadModel(model);
                info.download();
            }
        }
        context.startActivity(new Intent(context, Manager.class));
        finishActionMode();
        localCheckedItems.clear();
    }

    public ArrayList<Long> getLocalCheckedItems() {
        return localCheckedItems;
    }

    static class ViewHolder {
        public TextView status;
        public TextView timestamp;
        TextView appName;
        TextView versionName;
        TextView size;
        ImageView appIcon;
    }
}

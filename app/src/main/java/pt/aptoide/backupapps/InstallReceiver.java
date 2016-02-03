package pt.aptoide.backupapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.download.ApkMetaData;
import pt.aptoide.backupapps.download.DownloadInfo;
import pt.aptoide.backupapps.download.UploadFiles;
import pt.aptoide.backupapps.download.UploadModel;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.model.InstalledApk;
import pt.aptoide.backupapps.util.Constants;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 22-08-2013
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);

        Database.getInstance().removeInstalledApk(intent.getData().getEncodedSchemeSpecificPart());
        BusProvider.getInstance().post(new PackagesChangedEvent());

        if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
            if(sPref.getBoolean("automatic_install", false) && UploadPermissions.isUploadPermited(context)){
                try {
                    InstalledApk apk = InstalledAppsHelper.getInstalledApk(context, intent.getData().getEncodedSchemeSpecificPart());
                    UploadFiles files = new UploadFiles(apk.getApkFile(), apk.getMainObbFile(),apk.getPatchObbFile());
                    ApkMetaData metaData = new ApkMetaData();
                    metaData.setName(apk.getName());
                    metaData.setPackageName(apk.getPackageName());
                    UploadModel model = new UploadModel(files, metaData, sPref.getString(Constants.LOGIN_USER_TOKEN, ""), sPref.getString(Constants.LOGIN_USER_DEFAULT_REPO, ""));
                    DownloadInfo info = new DownloadInfo(new Random().nextInt(Integer.MAX_VALUE), apk);
                    info.setUploadModel(model);
                    info.download();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }



    }
}

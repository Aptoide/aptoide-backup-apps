package pt.aptoide.backupapps;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.model.InstalledApk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 29-07-2013
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
public class InstalledAppsHelper {

    public static ArrayList<InstalledApk> getInstalledApps(Context context, boolean getSysPackages) {
        ArrayList<InstalledApk> res = new ArrayList<InstalledApk>();
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo p : packs) {
            if ((!getSysPackages) && (p.versionName == null)) {
                continue;
            }
            InstalledApk newInfo = new InstalledApk();
            newInfo.setName(p.applicationInfo.loadLabel(context.getPackageManager()).toString());
            newInfo.setPackageName(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersionCode(p.versionCode);
            newInfo.setPath(p.applicationInfo.sourceDir);

            newInfo.setDate(newInfo.getApkFile().lastModified());

            long size = 0;

            if(newInfo.getApkFile()!=null){
                size += newInfo.getApkFile().length();
            }

            if(newInfo.getMainObbFile()!=null){
                size += newInfo.getMainObbFile().length();
            }

            if(newInfo.getPatchObbFile()!=null){
                size += newInfo.getPatchObbFile().length();
            }

            newInfo.setSize(size);

            if(context.getPackageManager().getLaunchIntentForPackage(newInfo.getPackageName())==null){
                newInfo.setSystemApp(true);
            }else{
                newInfo.setSystemApp(false);
            }

            newInfo.setIcon(p.applicationInfo);
            res.add(newInfo);
        }
        return res;
    }

    public static InstalledApk getInstalledApk(Context context, String packageName) throws PackageManager.NameNotFoundException {
        PackageInfo p = context.getPackageManager().getPackageInfo(packageName, 0);
        InstalledApk newInfo = new InstalledApk();
        newInfo.setName(p.applicationInfo.loadLabel(context.getPackageManager()).toString());
        newInfo.setPackageName(p.packageName);
        newInfo.setVersionName(p.versionName);
        newInfo.setVersionCode(p.versionCode);
        newInfo.setPath(p.applicationInfo.sourceDir);

        newInfo.setDate(newInfo.getApkFile().lastModified());

        long size = 0;

        if(newInfo.getApkFile()!=null){
            size += newInfo.getApkFile().length();
        }

        if(newInfo.getMainObbFile()!=null){
            size += newInfo.getMainObbFile().length();
        }

        if(newInfo.getPatchObbFile()!=null){
            size += newInfo.getPatchObbFile().length();
        }

        newInfo.setSize(size);

        if(context.getPackageManager().getLaunchIntentForPackage(newInfo.getPackageName())==null){
            newInfo.setSystemApp(true);
        }else{
            newInfo.setSystemApp(false);
        }

        newInfo.setIcon(p.applicationInfo);

        return newInfo;  //To change body of created methods use File | Settings | File Templates.
    }
}

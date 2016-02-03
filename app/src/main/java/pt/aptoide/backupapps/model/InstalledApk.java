package pt.aptoide.backupapps.model;

import android.content.pm.ApplicationInfo;
import android.database.sqlite.SQLiteStatement;
import android.graphics.drawable.Drawable;
import android.util.Log;
import pt.aptoide.backupapps.BackupAppsApplication;
import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.util.Constants;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 29-07-2013
 * Time: 13:04
 * To change this template use File | Settings | File Templates.
 */

public class InstalledApk extends Apk {

    private ApplicationInfo icon;
    private WeakReference<Drawable> iconDrawable;
    private boolean systemApp;
    private boolean isBackedUp;


    @Override
    public long insert(Database database) {

        SQLiteStatement statement = database.getInsertInstalledApkStatement();

        bindAllArgsAsStrings(statement, new String[]{getPackageName(), getName() , getVersionName(), getVersionCode()+"" /*date, size*/});

        return statement.executeInsert();

    }

    @Override
    public File getApkFile() {
        return new File(getPath());  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public File getMainObbFile() {

        File obbDir = new File(Constants.PATH_SDCARD + "/Android/obb/" + getPackageName()+"/");
        if(obbDir.isDirectory()){

            File[] files = obbDir.listFiles();

            for(File file : files){
                String name = file.getName();
                if(name.contains("main") && !name.contains("-downloading") && !name.contains("temp.")){
                    return file;
                }

            }
        }

        return null;
    }

    @Override
    public File getPatchObbFile() {
        File obbDir = new File(Constants.PATH_SDCARD + "/Android/obb/" + getPackageName()+"/");
        if(obbDir.isDirectory()){

            File[] files = obbDir.listFiles();

            for(File file : files){

                String name = file.getName();
                if(name.contains("patch") && !name.contains("-downloading") && !name.contains("temp.")){
                    return file;
                }

            }


        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void bindAllArgsAsStrings(SQLiteStatement statement, String[] bindArgs) {
        if (bindArgs != null) {
            for (int i = bindArgs.length; i != 0; i--) {
                statement.bindString(i, bindArgs[i - 1]);
            }
        }
    }

    public Drawable getIcon() {

        if (iconDrawable == null || iconDrawable.get() == null) {
            iconDrawable = new WeakReference<Drawable>(icon.loadIcon(BackupAppsApplication.getContext().getPackageManager()));
            return iconDrawable.get();
        } else {
            return iconDrawable.get();
        }

    }

    public void setIcon(ApplicationInfo icon) {
        this.icon = icon;
    }


    public boolean isSystemApp() {
        return systemApp;
    }

    public void setSystemApp(boolean systemApp) {
        this.systemApp = systemApp;
    }

    public boolean isBackedUp() {
        return isBackedUp;
    }

    public void setBackedUp(boolean backedUp) {
        isBackedUp = backedUp;
    }
}

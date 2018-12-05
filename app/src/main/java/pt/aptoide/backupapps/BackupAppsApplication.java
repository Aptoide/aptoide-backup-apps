package pt.aptoide.backupapps;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import io.fabric.sdk.android.Fabric;
import java.io.File;
import pt.aptoide.backupapps.analytics.Analytics;
import pt.aptoide.backupapps.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 29-07-2013
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
public class BackupAppsApplication extends Application {

  private static final String SDCARD = Environment.getExternalStorageDirectory()
      .getAbsolutePath();
  private static final String APTOIDE_PATH = SDCARD + "/.aptoide";
  private static final String ICON_CACHE_PATH = APTOIDE_PATH + "/icons";
  public static boolean DEBUG_MODE = Log.isLoggable("APTOIDE", Log.DEBUG);
  private static Context context;
  private CallbackManager callbackManager;

  public static Context getContext() {
    return context;
  }

  /**
   * Set the default debugging mode. There are several ways to set this outside the Application:
   * 1) set by the Android, also by multiple ways (check documentations of Log.isLoggable): <br />
   * <i>setprop log.tag.APTOIDE DEBUG</i>
   * 2) set by us: via a flag in SharedPreferences
   */
  private void setDebugMode() {
    boolean debugMode = PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean("debugmode", false);
    boolean isDebuggable = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));

    DEBUG_MODE = DEBUG_MODE | debugMode | isDebuggable;
  }

  @Override public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    setDebugMode();
    FacebookSdk.sdkInitialize(this);

    callbackManager = CallbackManager.Factory.create();

    Analytics.Lifecycle.Application.onCreate(this);

    createShortcutOnFirstRun();
    context = getApplicationContext();
    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)

        .defaultDisplayImageOptions(
            new DisplayImageOptions.Builder().displayer(new SimpleBitmapDisplayer())
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .showStubImage(android.R.drawable.sym_def_app_icon)
                .resetViewBeforeLoading(true)
                .build())
        .discCache(new UnlimitedDiscCache(new File(ICON_CACHE_PATH)))
        .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
        .threadPriority(Thread.MIN_PRIORITY)

        .build();
    ImageLoader.getInstance()
        .init(config);
  }

  private void createShortcutOnFirstRun() {
    Handler handler = new Handler();
    handler.post(new Runnable() {
      @Override public void run() {
        SharedPreferences sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(BackupAppsApplication.this);
        if (!sharedPreferences.contains("firstRun")) {
          sharedPreferences.edit()
              .putString("firstRun", "")
              .commit();
          createLauncherShortcut(BackupAppsApplication.this);
        }
      }
    });
  }

  public void deleteLauncherShortcut(Context context) {
    Intent shortcutIntent = context.getPackageManager()
        .getLaunchIntentForPackage(context.getPackageName());
    final Intent intent = new Intent();
    intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
    intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Aptoide Backup Apps");
    Parcelable iconResource;
    iconResource = Intent.ShortcutIconResource.fromContext(context, R.mipmap.backup);
    intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
    intent.putExtra("duplicate", false);
    intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
    context.sendBroadcast(intent);
  }

  public void createLauncherShortcut(Context context) {
    removePreviousShortcuts(context);

    deleteLauncherShortcut(context);

    Intent shortcutIntent = context.getPackageManager()
        .getLaunchIntentForPackage(context.getPackageName());
    final Intent intent = new Intent();
    intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
    intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Aptoide Backup Apps");
    Parcelable iconResource;
    iconResource = Intent.ShortcutIconResource.fromContext(context, R.mipmap.backup);
    intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
    intent.putExtra("duplicate", false);
    intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
    context.sendBroadcast(intent);
  }

  private void removePreviousShortcuts(Context context) {

    Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
    shortcutIntent.setClassName(context, Constants.APTOIDE_CLASS_NAME);
    shortcutIntent.putExtra(Constants.APTOIDE_PACKAGE_NAME,
        context.getString(R.string.app_message_description));

    final Intent intent = new Intent();
    intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
    intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Aptoide Backup Apps");
    Parcelable iconResource;
    iconResource = Intent.ShortcutIconResource.fromContext(context, R.mipmap.backup);
    intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
    intent.putExtra("duplicate", false);
    intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
    context.sendBroadcast(intent);
  }

  public CallbackManager getCallbackManager() {
    return callbackManager;
  }
}
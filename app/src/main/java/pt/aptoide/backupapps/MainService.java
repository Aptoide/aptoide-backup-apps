package pt.aptoide.backupapps;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.download.event.AskRepoLoginDataEvent;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.model.InstalledApk;
import pt.aptoide.backupapps.model.Server;
import pt.aptoide.backupapps.parser.RepoParser;
import pt.aptoide.backupapps.util.Constants;
import pt.aptoide.backupapps.util.NetworkUtils;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 28-07-2013
 * Time: 2:12
 * To change this template use File | Settings | File Templates.
 */
public class MainService extends Service {

  static Database database = Database.getInstance();
  ArrayList<InstalledApk> installedApks = new ArrayList<InstalledApk>();
  private ExecutorService executor = Executors.newSingleThreadExecutor();

  @Override public IBinder onBind(Intent intent) {
    return new LocalBinder();
  }

  public void parse(Server server) throws IOException {
    File file = new File(Environment.getExternalStorageDirectory()
        .getAbsolutePath() + "/.aptoide/info_backupapps.xml");

    SharedPreferences sPref =
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    String urlString;
    if ("".equals(server.getHash())) {
      if (sPref.getBoolean(Constants.LOGIN_FROM_SIGNUP, false)) {
        urlString = server.getUrl() + "info.xml?hash=getupdated";
      } else {
        urlString = server.getUrl() + "info.xml";
      }
    } else {
      urlString = server.getUrl() + "info.xml?hash=" + server.getHash();
    }

    Log.d("TAG", "Getting: " + urlString);

    int responseCode;

    responseCode = NetworkUtils.checkServerConnection(urlString);

    InputStream is = null;

    switch (responseCode) {

      case 401:

        if (server.getLogin() != null && !server.getLogin()
            .getPassword()
            .equals("")) {
          is = NetworkUtils.getInputStream(urlString, server.getLogin()
              .getUsername(), server.getLogin()
              .getPassword(), getApplicationContext());
        } else {
          BusProvider.getInstance()
              .post(new AskRepoLoginDataEvent(false));
          return;
        }
        break;

      default:
        is = NetworkUtils.getInputStream(urlString, getApplicationContext());
        break;
    }

    //HttpURLConnection url = (HttpURLConnection) new URL(urlString).openConnection();
    BufferedInputStream in = new BufferedInputStream(is, 1024 * 32);

    byte[] buffer = new byte[1024 * 32];
    if (!file.getParentFile()
        .exists()) {
      file.getParentFile()
          .mkdirs();
    }
    FileOutputStream out = new FileOutputStream(file);
    try {
      for (; ; ) {
        int rsz = in.read(buffer, 0, buffer.length);
        if (rsz < 0) break;
        out.write(buffer, 0, rsz);
      }
    } finally {
      in.close();
      out.close();
    }

    try {
      Log.d("TAG", "Starting parse");
      Database.getInstance()
          .beginTransaction();
      RepoParser.getInstance()
          .parse(file, server);
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    } finally {
      Database.getInstance()
          .endTransaction();
      file.delete();
    }
  }

  public void resetInstalledApks() {

    executor.submit(new Runnable() {
      @Override public void run() {
        installedApks = InstalledAppsHelper.getInstalledApps(getApplicationContext(), true);
      }
    });
  }

  public InstalledApk getInstalledApk(String auto_backup) {

    for (InstalledApk apk : installedApks) {
      if (apk.getPackageName()
          .equals(auto_backup)) {
        return apk;
      }
    }

    return null;
  }

  public class LocalBinder extends Binder {

    public MainService getService() {
      return MainService.this;
    }
  }
}

package pt.aptoide.backupapps.analytics;

import android.content.Context;
import android.util.Log;
import com.localytics.android.Localytics;
import java.util.HashMap;
import pt.aptoide.backupapps.Logger;

/**
 * Created by neuro on 07-05-2015.f
 */
public class Analytics {

  // Constantes globais a todos os eventos.
  public static final String ACTION = "Action";
  private static final int ALL = Integer.MAX_VALUE;
  private static final int LOCALYTICS = 1 << 0;

  /**
   * Verifica se as flags fornecidas constam em accepted.
   *
   * @param flag flags fornecidas
   * @param accepted flags aceitáveis
   *
   * @return true caso as flags fornecidas constem em accepted.
   */
  private static boolean checkAcceptability(int flag, int accepted) {
    return (flag & accepted) == accepted;
  }

  private static void track(String event, String key, String attr, int flags) {

    try {
      HashMap<String, String> stringObjectHashMap = new HashMap<String, String>();

      stringObjectHashMap.put(key, attr);

      track(event, stringObjectHashMap, flags);

      Logger.d("Analytics", "Event: " + event + ", Key: " + key + ", attr: " + attr);
    } catch (Exception e) {
      Log.d("Analytics", e.getStackTrace()
          .toString());
    }
  }

  private static void track(String event, HashMap map, int flags) {
    try {
      if (checkAcceptability(flags, LOCALYTICS)) Localytics.tagEvent(event, map);

      Logger.d("Analytics", "Event: " + event + ", Map: " + map);
    } catch (Exception e) {
      Log.d("Analytics", e.getStackTrace()
          .toString());
    }
  }

  private static void track(String event, int flags) {

    try {
      if (checkAcceptability(flags, LOCALYTICS)) Localytics.tagEvent(event);

      Logger.d("Analytics", "Event: " + event);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static class Lifecycle {

    public static class Application {

      public static void onCreate(Context applicationContext) {

        // Integrate Localytics
        Localytics.integrate(applicationContext);
      }
    }

    public static class Activity {

      public static void onResume(android.app.Activity activity) {

        // Localytics
        Localytics.openSession();
        Localytics.upload();

        Localytics.handleTestMode(activity.getIntent());
      }

      public static void onPause(android.app.Activity activity) {

        // Localytics
        Localytics.closeSession();
        Localytics.upload();
      }
    }
  }

  public static class Upload {

    public static final String EVENT_NAME = "Upload App";
    private static String RESULT_KEY = "Result";

    public static void uploadApp(String result) {
      track(EVENT_NAME, RESULT_KEY, result, ALL);
    }
  }
}

package pt.aptoide.backupapps.util;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 23-08-2013
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

  public static String formatBytes(long bytes) {
    int unit = 1024;
    if (bytes < unit) return bytes + " B";
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = ("KMGTPE").charAt(exp - 1) + "";
    return String.format(Locale.ENGLISH, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
  }
}

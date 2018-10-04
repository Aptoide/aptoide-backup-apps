package pt.aptoide.backupapps.download;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 08-07-2013
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

  public static String formatEta(long eta, String left) {

    if (eta > 0) {
      long days = eta / (1000 * 60 * 60 * 24);
      eta -= days * 1000 * 60 * 60 * 24;
      long hours = eta / (1000 * 60 * 60);
      eta -= hours * 1000 * 60 * 60;
      long minutes = eta / (1000 * 60);
      eta -= minutes * 1000 * 60;
      long seconds = eta / 1000;

      String etaString = "";
      if (days > 0) {
        etaString += days + "d ";
      }
      if (hours > 0) {
        etaString += hours + "h ";
      }
      if (minutes > 0) {
        etaString += minutes + "m ";
      }
      if (seconds > 0) {
        etaString += seconds + "s";
      }

      return etaString + " " + left;
    }
    return "";
  }

  public static String formatBits(long bytes) {
    int unit = 1024;
    if (bytes < unit) return bytes + " B";
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = ("KMGTPE").charAt(exp - 1) + "";
    return String.format(Locale.ENGLISH, "%.1f %sb", bytes / Math.pow(unit, exp), pre);
  }

  public static String formatBytes(long bytes) {
    int unit = 1024;
    if (bytes < unit) return bytes + " B";
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = ("KMGTPE").charAt(exp - 1) + "";
    return String.format(Locale.ENGLISH, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
  }
}


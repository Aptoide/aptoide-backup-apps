package pt.aptoide.backupapps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pt.aptoide.backupapps.model.RepoApk;
import pt.aptoide.backupapps.util.NetworkUtils;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 11-03-2013
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class WebserviceGetApkInfo {

  JSONObject response;
  String arguments = "getApkInfo/<repo>/<apkid>/<apkversion>/options=(<options>)/<mode>";
  String defaultWebservice = "http://webservices.aptoide.com/webservices/";
  private URL url;
  //private ArrayList<Comment> comments;
  private boolean seeAll = false;
  private boolean screenshotChanged;

  public WebserviceGetApkInfo(String webservice, RepoApk apk) throws IOException, JSONException {

    StringBuilder url = new StringBuilder();

    if (webservice == null) {
      url.append(defaultWebservice);
    } else {
      url.append(webservice);
    }

    ArrayList<WebserviceOptions> options = new ArrayList<WebserviceOptions>();

    //if(token!=null)options.add(new WebserviceOptions("token", token));
    //options.add(new WebserviceOptions("cmtlimit", "6"));
    //options.add(new WebserviceOptions("md5sum", md5));
    //options.add(new WebserviceOptions("payinfo", "true"));
    //options.add(new WebserviceOptions("lang", getMyCountrCode(ApplicationAptoide.getContext())));

    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (WebserviceOptions option : options) {
      sb.append(option);
      sb.append(";");
    }
    sb.append(")");

    url.append("webservices/getApkInfo/")
        .append(apk.getRepoName())
        .append("/")
        .append(apk.getPackageName())
        .append("/")
        .append(URLEncoder.encode(apk.getVersionName(), "UTF-8"))
        .append("/")
        .append("options=")
        .append(sb.toString())
        .append("/")
        .append("json");

    String line;

    BufferedReader br = new BufferedReader(new InputStreamReader(
        NetworkUtils.getInputStream(url.toString(), null, null,
            BackupAppsApplication.getContext())));
    sb = new StringBuilder();
    while ((line = br.readLine()) != null) {
      sb.append(line)
          .append('\n');
    }

    br.close();

    Log.e("REQUEST", url.toString());
    Log.e("RESPONSE", sb.toString());
    response = new JSONObject(sb.toString());
  }

  public static String getMyCountrCode(Context context) {
    return context.getResources()
        .getConfiguration().locale.getLanguage() + "_" + context.getResources()
        .getConfiguration().locale.getCountry();
  }

  public ArrayList<ApkPermission> getApkPermissions() throws JSONException {

    JSONArray permissionArray = response.getJSONArray("permissions");
    PackageManager pm = BackupAppsApplication.getContext()
        .getPackageManager();

    CharSequence csPermissionGroupLabel;
    CharSequence csPermissionLabel;
    List<PermissionGroupInfo> lstGroups = pm.getAllPermissionGroups(0);

    ArrayList<ApkPermission> list = new ArrayList<ApkPermission>();
    for (int i = 0; i != permissionArray.length(); i++) {
      String permission = permissionArray.getString(i);

      for (PermissionGroupInfo pgi : lstGroups) {
        csPermissionGroupLabel = pgi.loadLabel(pm);
        //Log.e("perm", pgi.name + ": " + csPermissionGroupLabel.toString());

        try {
          List<PermissionInfo> lstPermissions = pm.queryPermissionsByGroup(pgi.name, 0);
          for (PermissionInfo pi : lstPermissions) {
            csPermissionLabel = pi.loadLabel(pm);

            if (pi.name.equals(permission)) {
              list.add(new ApkPermission(csPermissionGroupLabel.toString(),
                  csPermissionLabel.toString()));
            }
            //Log.e("perm", "   " + pi.name + ": " + csPermissionLabel.toString());
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }

    return list;
  }

  public boolean hasOBB() {
    return response.has("obb");
  }

  public JSONObject getMainOBB() throws JSONException {
    return response.getJSONObject("obb")
        .getJSONObject("main");
  }

  public JSONObject getPatchOBB() throws JSONException {
    return response.getJSONObject("obb")
        .getJSONObject("patch");
  }

  public boolean hasPatchOBB() throws JSONException {
    return response.getJSONObject("obb")
        .has("patch");
  }

  private class WebserviceOptions {
    String key;
    String value;

    private WebserviceOptions(String key, String value) {
      this.value = value;
      this.key = key;
    }

    @Override protected void finalize() throws Throwable {

      Log.d("TAG", "Garbage Collecting WebserviceResponse");
      super.finalize();
    }

    /**
     * Returns a string containing a concise, human-readable description of this
     * object. Subclasses are encouraged to override this method and provide an
     * implementation that takes into account the object's type and data. The
     * default implementation is equivalent to the following expression:
     * <pre>
     *   getClass().getName() + '@' + Integer.toHexString(hashCode())</pre>
     * <p>See <a href="{@docRoot}reference/java/lang/Object.html#writing_toString">Writing a useful
     * {@code toString} method</a>
     * if you intend implementing your own {@code toString} method.
     *
     * @return a printable representation of this object.
     */
    @Override public String toString() {
      return key
          + "="
          + value;    //To change body of overridden methods use File | Settings | File Templates.
    }
  }
}

package pt.aptoide.backupapps.model;

import pt.aptoide.backupapps.Login;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 30-07-2013
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class Server {

  private String url;
  private String apkPath;
  private String iconsPath;
  private String hash;
  private int appsCount;
  private long id;
  private Login login;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getApkPath() {
    return apkPath;
  }

  public void setApkPath(String apkPath) {
    this.apkPath = apkPath;
  }

  public String getIconsPath() {
    return iconsPath;
  }

  public void setIconsPath(String iconsPath) {
    this.iconsPath = iconsPath;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public int getAppsCount() {
    return appsCount;
  }

  public void setAppsCount(int appsCount) {
    this.appsCount = appsCount;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Login getLogin() {
    return login;
  }

  public void setLogin(Login login) {
    this.login = login;
  }
}

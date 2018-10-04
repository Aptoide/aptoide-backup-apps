package pt.aptoide.backupapps.model;

import android.database.sqlite.SQLiteStatement;
import java.io.File;
import pt.aptoide.backupapps.database.Database;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 29-07-2013
 * Time: 13:01
 * To change this template use File | Settings | File Templates.
 */
public class RepoApk extends Apk {

  private String iconPath;
  private String md5Sum;
  private String repoName;

  public String getIconPath() {

    return iconPath;
  }

  public void setIconPath(String iconPath) {
    this.iconPath = iconPath;
  }

  @Override public long insert(Database database) {
    SQLiteStatement statement = database.getInsertRepoApkStatement();
    bindAllArgsAsStrings(statement, new String[] {
        getPackageName(), getName(), getVersionName(), getVersionCode() + "", getIconPath(),
        getSize() + "", getPath(), getMd5Sum(), getDate() + ""
    });
    return statement.executeInsert();
  }

  @Override public File getApkFile() {
    return new File(
        getPath());  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override public File getMainObbFile() {
    return null;
  }

  @Override public File getPatchObbFile() {
    return null;
  }

  public void bindAllArgsAsStrings(SQLiteStatement statement, String[] bindArgs) {
    if (bindArgs != null) {
      for (int i = bindArgs.length; i != 0; i--) {
        statement.bindString(i, bindArgs[i - 1]);
      }
    }
  }

  public String getMd5Sum() {
    return md5Sum;
  }

  public void setMd5Sum(String md5Sum) {
    this.md5Sum = md5Sum;
  }

  public String getRepoName() {
    return repoName;
  }

  public void setRepoName(String repoName) {
    this.repoName = repoName;
  }
}

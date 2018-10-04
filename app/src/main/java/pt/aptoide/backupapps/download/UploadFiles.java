package pt.aptoide.backupapps.download;

import java.io.File;
import pt.aptoide.backupapps.util.Md5Handler;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 01-08-2013
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public class UploadFiles {

  private File apk;
  private File mainObb;
  private File pathObb;

  private String apkMd5;
  private String mainObbMd5;
  private String patchObbMd5;

  public UploadFiles(File apk, File mainObb, File pathObb) {
    this.apk = apk;
    this.mainObb = mainObb;
    this.pathObb = pathObb;
  }

  public File getApk() {
    return apk;
  }

  public File getMainObb() {
    return mainObb;
  }

  public File getPatchObb() {
    return pathObb;
  }

  public String getApkMd5() {

    if (apkMd5 == null) {
      apkMd5 = Md5Handler.md5Calc(getApk());
    }

    return apkMd5;
  }

  public String getMainObbMd5() {

    if (mainObbMd5 == null) {
      mainObbMd5 = Md5Handler.md5Calc(getMainObb());
    }
    return mainObbMd5;
  }

  public String getPatchObbMd5() {
    if (patchObbMd5 == null) {
      patchObbMd5 = Md5Handler.md5Calc(getPatchObb());
    }
    return patchObbMd5;
  }
}

package pt.aptoide.backupapps.download;

import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import pt.aptoide.backupapps.util.Md5Handler;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 02-07-2013
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public class DownloadFile {

  private RandomAccessFile mFile;
  private String mDestination;
  private String md5;
  public DownloadFile(String destination, String md5) throws FileNotFoundException {
    this.md5 = md5;
    this.mDestination = destination;
    File file = new File(this.mDestination);

    File dir = file.getParentFile();
    if ((dir != null) && (!dir.isDirectory())) {
      dir.mkdirs();
    }

    this.mFile = new RandomAccessFile(mDestination, "rw");
  }

  public static long getFileLength(String path) {
    File f = new File(path);
    if (f.exists()) {
      return f.length();
    }

    return 0L;
  }

  public RandomAccessFile getmFile() {
    return mFile;
  }

  public void delete() {

    new File(mDestination).delete();
  }

  public String getDestination() {
    return this.mDestination;
  }

  public void write(byte[] buffer) throws IOException {
    this.mFile.write(buffer);
  }

  public void close() {
    try {
      this.mFile.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setDownloadedSize(long downloadedSize) throws IOException {
    Log.d("DownloadFile", "Position is: " + downloadedSize);
    mFile.seek(downloadedSize);
  }

  public String getMd5() {
    return md5;
  }

  public synchronized void checkMd5() throws Md5FailedException {

    String md5 = getMd5();

    if (md5.length() > 0) {

      String calculatedMd5 = Md5Handler.md5Calc(new File(mDestination));

      if (!calculatedMd5.equals(md5)) {

        Log.d("TAG",
            "Failed Md5: " + mDestination + "   calculated " + calculatedMd5 + " vs " + md5);
        throw new Md5FailedException();
      }
    }
  }
}

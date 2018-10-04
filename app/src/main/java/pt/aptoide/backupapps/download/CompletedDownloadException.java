package pt.aptoide.backupapps.download;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 03-07-2013
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class CompletedDownloadException extends Throwable {

  private long mSize;

  public CompletedDownloadException(long mSize) {
    this.mSize = mSize;
  }

  public long getSize() {
    return mSize;
  }
}

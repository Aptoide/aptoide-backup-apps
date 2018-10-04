package pt.aptoide.backupapps.download.state;

import pt.aptoide.backupapps.download.DownloadInfo;
import pt.aptoide.backupapps.download.DownloadManager;
import pt.aptoide.backupapps.download.EnumDownloadFailReason;

/**
 * The error state represents the status of a download object when it has failed to download.
 *
 * @author Edward Larsson (edward.larsson@gmx.com)
 */
public class ErrorState extends StatusState {

  /** The error message of this state. */
  private EnumDownloadFailReason mErrorMessage;

  /**
   * Construct an error state with a message.
   *
   * @param downloadInfo The downloadInfo associated with this state.
   * @param errorMessage The error message of this state.
   */
  public ErrorState(DownloadInfo downloadInfo, EnumDownloadFailReason errorMessage) {
    super(downloadInfo);
    mErrorMessage = errorMessage;
  }

  /**
   * @return The error message of this state.
   */
  public EnumDownloadFailReason getErrorMessage() {
    return mErrorMessage;
  }

  @Override public StatusState getShallowCopy() {
    return new ErrorState(null, mErrorMessage);
  }

  @Override public void download() {
    mDownloadInfo.changeStatusState(new ActiveState(mDownloadInfo));
  }

  @Override public void pause() {
    //do nothing, not active
  }

  @Override public void changeFrom() {
    DownloadManager.INSTANCE.removeFromErrorList(mDownloadInfo);
  }

  @Override public boolean changeTo() {

    if (DownloadManager.INSTANCE.addToErrorList(mDownloadInfo)) {
      mDownloadInfo.setStatusState(this);
      return true;
    }

    return false;
  }

  @Override public int getQueuePosition() {
    return Integer.MAX_VALUE;
  }

  @Override public EnumState getEnumState() {
    return EnumState.ERROR;  //To change body of implemented methods use File | Settings | File Templates.
  }
}

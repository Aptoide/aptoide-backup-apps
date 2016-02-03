package pt.aptoide.backupapps.download;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 01-08-2013
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public interface ManagerThread {
    long getmDownloadedSize();

    long getmProgress();

    long getmFullSize();

    long getmRemainingSize();
}

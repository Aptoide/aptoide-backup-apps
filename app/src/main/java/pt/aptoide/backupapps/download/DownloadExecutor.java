package pt.aptoide.backupapps.download;


import pt.aptoide.backupapps.model.Apk;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 08-07-2013
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public interface DownloadExecutor {

    public void execute(String path, Apk apk);

}

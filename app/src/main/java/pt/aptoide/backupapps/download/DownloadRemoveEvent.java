package pt.aptoide.backupapps.download;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 08-07-2013
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class DownloadRemoveEvent {
    private final int id;

    public DownloadRemoveEvent(int id) {
        this.id=id;
    }

    public int getId() {
        return id;
    }
}

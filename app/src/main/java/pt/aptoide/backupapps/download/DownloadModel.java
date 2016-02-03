package pt.aptoide.backupapps.download;


import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class DownloadModel {

    private final String destination;
    private final String url;
    private final String md5;
    private boolean isAutoExecute = false;

    public DownloadFile getFile() {
        return file;
    }

    private DownloadFile file;

    public DownloadConnection createConnection() throws IOException {

        return new DownloadConnectionImpl(new URL(url));
    }

    public DownloadFile createFile() throws FileNotFoundException {
        this.file = new DownloadFile(destination,md5);
        return file;
    }

    public DownloadModel(String url, String destination, String md5) {
        this.url = url;
        this.destination = destination;
        this.md5 = md5;
    }

    public String getDestination() {
        return destination;
    }

    public boolean isAutoExecute() {
        return isAutoExecute;
    }

    public void setAutoExecute(boolean autoExecute) {
        isAutoExecute = autoExecute;
    }

    @Override
    protected void finalize() throws Throwable {
        Log.d("Garbage Collector", "DownloadModel with destination " + destination + " beeing destroyed.");
        super.finalize();
    }
}

package pt.aptoide.backupapps.download;


import android.util.Log;

public class UploadModel {


    private final ApkMetaData metadata;
    private final UploadFiles uploadFiles;
    private final String repo;
    private final String userToken;

    public UploadModel(UploadFiles uploadFiles, ApkMetaData metaData, String userToken, String repo) {
        this.uploadFiles = uploadFiles;
        this.metadata = metaData;
        this.userToken = userToken;
        this.repo = repo;
    }



    @Override
    protected void finalize() throws Throwable {
        Log.d("Garbage Collector", "UploadModel beeing destroyed.");
        super.finalize();
    }

    public ApkMetaData getMetadata() {
        return metadata;
    }

    public UploadFiles getUploadFiles() {
        return uploadFiles;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getRepo() {
        return repo;
    }
}

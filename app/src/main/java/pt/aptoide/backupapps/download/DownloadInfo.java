package pt.aptoide.backupapps.download;

import android.os.Environment;
import android.util.Log;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.download.event.DownloadStatusEvent;
import pt.aptoide.backupapps.download.event.UploadStatusEvent;
import pt.aptoide.backupapps.download.state.*;
import pt.aptoide.backupapps.model.Apk;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 02-07-2013
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class DownloadInfo implements Runnable{

    private Apk apk;
    private int id;
    //    private final DownloadConnection mConnection;
    private List<DownloadModel> mFilesToDownload;
    private long mDownloadedSize;
    private long mSize;
    private double mSpeed;
    private long mETA;


    private StatusState mStatusState;
    private String mDestination;
    private static final int UPDATE_INTERVAL_MILLISECONDS = 1000;

    private ArrayList<ManagerThread> threads = new ArrayList<ManagerThread>();


    private DownloadExecutor downloadExecutor;
    private long mProgress = 0;
    private boolean isPaused = false;
    private UploadModel uploadModel;
    private boolean isUpload;


    public DownloadInfo(int id, Apk apk) {
        this(null, id, apk);
    }

    public void setFilesToDownload(List<DownloadModel> mFilesToDownload) {
        this.mFilesToDownload = mFilesToDownload;
    }

    public void setUploadModel(UploadModel uploadModel){
        this.uploadModel = uploadModel;
    }

    public UploadModel getUploadModel(){
        return this.uploadModel;
    }


    public DownloadInfo(List<DownloadModel> filesToDownload, int id, Apk apk) {
        this.mFilesToDownload = filesToDownload;
        this.id = id;
        this.apk = apk;
        mStatusState = new NoState(this);
    }

    public int getPercentDownloaded() {
        if (mSize == 0) {
            return 0;
        }

        return (int) ((mProgress) * 100  / mSize) ;
    }

    TimerTask task;
    Timer timer;

    @Override
    public void run() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        timer = new Timer();

        try {

            if(uploadModel != null){
                UploadThread thread = new UploadThread(uploadModel, this);
                executor.submit(thread);
                threads.add(thread);
                this.isUpload = true;
            }else{
                for(DownloadModel file : mFilesToDownload){
                    DownloadThread thread = new DownloadThread(file, this);
                    executor.submit(thread);
                    threads.add(thread);
                }

                checkDirectorySize(Environment.getExternalStorageDirectory().getAbsolutePath()+"/.aptoide/apks");
            }

            mSize = getAllThreadSize();
            task = new TimerTask() {


                /** How much was downloaded last time. */
                private long iMLastDownloadedSize = mDownloadedSize;
                /** The nanoTime last time. */
                private long iMLastTime = System.currentTimeMillis();
                private long iMFirstTime = System.currentTimeMillis();
                public long mAvgSpeed;

                @Override
                public void run() {
                    long mReaminingSize = getAllSizeRemaining();
                    mDownloadedSize = getAllDownloadedSize();
                    mProgress = getAllProgress();

                    long timeElapsedSinceLastTime = System.currentTimeMillis() - iMLastTime;
                    long timeElapsed = System.currentTimeMillis() - iMFirstTime;
                    iMLastTime = System.currentTimeMillis();
                    // Difference between last time and this time = how much was downloaded since last run.
                    long downloadedSinceLastTime = mDownloadedSize - iMLastDownloadedSize;
                    iMLastDownloadedSize = mDownloadedSize;
                    if (timeElapsedSinceLastTime > 0) {
                        // Speed (bytes per second) = downloaded bytes / time in seconds (nanoseconds / 1000000000)
                        mAvgSpeed = (mDownloadedSize) * 1000 / timeElapsed;
                        mSpeed = downloadedSinceLastTime * 1000 / timeElapsedSinceLastTime;
                    }


                    if (mAvgSpeed > 0) {
                        // ETA (milliseconds) = remaining byte size / bytes per millisecond (bytes per second * 1000)
                        mETA = (mReaminingSize - mDownloadedSize) * 1000 / mAvgSpeed;
                    }
//                    else {
//                        mETA = 0;
//                    }

//                    Log.d("DownloadManager", "ETA: " + mETA + " AvgSpeed: " + mAvgSpeed / 1000 + " RemainingSize: " + mReaminingSize + " Downloaded: " + mDownloadedSize + " Status: " + mStatusState.toString());
                    Log.d("DownloadManager", "ETA: " + mETA + " Speed: " + mSpeed / 1000 + " Size: " + Utils.formatBytes(mSize) + " Downloaded: " + Utils.formatBytes(mDownloadedSize) + " Status: " + mStatusState.toString() + " TotalDownloaded: " + Utils.formatBytes(mProgress));
//                    Log.d("DownloadManager", threads.size() + " on queue.");

//                    notifyListeners(new DownloadProgressEvent(DownloadInfo.this));

                    BusProvider.getInstance().post(DownloadInfo.this);
//                    mSpeed = 0;
//                    mETA = 0;
                }


            };
            // Schedule above task for every (UPDATE_INTERVAL_MILLISECONDS) milliseconds.
            timer.schedule(task, UPDATE_INTERVAL_MILLISECONDS, UPDATE_INTERVAL_MILLISECONDS);
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

            timer.cancel();
            mSize = getAllThreadSize();
            mProgress = getAllProgress();



            Log.d("TAG", "Downloads done " + mSize + " " + mProgress + " " + mStatusState.getEnumState().name());


            if(mStatusState instanceof ActiveState){
                changeStatusState(new CompletedState(this));
                autoExecute();
            }



//            Log.d("DownloadManager", "AllDownloads done");

        }catch (RuntimeException e){
            changeStatusState(new ErrorState(this, EnumDownloadFailReason.NO_REASON));
            e.printStackTrace();
        } catch (Exception e) {
            changeStatusState(new ErrorState(this, EnumDownloadFailReason.NO_REASON));
            e.printStackTrace();
        }

        BusProvider.getInstance().post(DownloadInfo.this);

        if(isUpload){
            Log.d("TAG",  threads.size() + "");
            BusProvider.getInstance().post(new UploadStatusEvent());
        }else{
            BusProvider.getInstance().post(new DownloadStatusEvent());
        }



        threads.clear();
        mDownloadedSize = 0;
        mSpeed = 0;
        mETA = 0;


    }

    double getDirSize(File dir) {
        double size = 0;
        if (dir.isFile()) {
            size = dir.length();
        } else {
            File[] subFiles = dir.listFiles();
            for (File file : subFiles) {
                if (file.isFile()) {
                    size += file.length();
                } else {
                    size += this.getDirSize(file);
                }

            }
        }
        return size;
    }

    private void checkDirectorySize(String dirPath) {
        File dir = new File(dirPath);
        if(!dir.exists()){
            if(!dir.mkdirs()){
                return;
            }

        }
    }

    public EnumDownloadFailReason getFailReason(){
        return ((ErrorState)mStatusState).getErrorMessage();
    }

    public void autoExecute() {
        if (downloadExecutor != null) {
            for (DownloadModel file : mFilesToDownload) {
                if (file.isAutoExecute()) {
                    downloadExecutor.execute(file.getDestination(), apk);
                }
            }
        }
    }



    public void setDownloadExecutor(DownloadExecutor executor){
        this.downloadExecutor = executor;
    }


    private long getAllDownloadedSize() {

        long sum = 0;
        for(ManagerThread thread : threads){
            sum = sum + thread.getmDownloadedSize();
//            Log.d("DownloadManagerThread", "Downloaded: " + thread.getmDownloadedSize());
        }
        return sum;
    }

    private long getAllProgress() {

        long sum = 0;
        for(ManagerThread thread : threads){
            sum = sum + thread.getmProgress();
//            Log.d("DownloadManagerThread", "Downloaded: " + thread.getmDownloadedSize());
        }
        return sum;
    }

    private long getAllThreadSize() {
        long sum = 0;
        for(ManagerThread thread : threads){
            sum = sum + thread.getmFullSize();
//            Log.d("DownloadManagerThread", "Size: " + thread.getmRemainingSize());
        }
        return sum;
    }


    public void pause() {
        this.isPaused = true;
        this.mStatusState.pause();

    }

    public void remove()
    {

        changeStatusState(new CompletedState(this));

        for(DownloadModel model: mFilesToDownload){
            new File(model.getDestination()).delete();
        }
        mProgress = 0;
        mSize=0;
        mFilesToDownload.clear();
        BusProvider.getInstance().post(new DownloadStatusEvent());
        DownloadManager.INSTANCE.removeDownload(this);
    }


    @Override
    protected void finalize() throws Throwable {

        Log.d("Garbage Collector", "Download with id: " + id + " is beeing garbage collected");

        super.finalize();
    }

    public void download(){
        mProgress = 0;
        this.mStatusState.download();
    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String mDestination) {
        this.mDestination = mDestination;
    }

    public void setStatusState(StatusState statusState) {
        this.mStatusState = statusState;
    }

    public StatusState getStatusState() {
        return this.mStatusState;
    }

    public double getSpeed() {
        return mSpeed * 8;
    }

    public long getEta() {
        return mETA;
    }

    public void changeStatusState(StatusState state) {
//        BusProvider.getInstance().post(this);
        mStatusState.changeTo(state);
    }



    public int getId() {
        return id;
    }

  public long getAllSizeRemaining() {
        long sum = 0;
        for(ManagerThread thread : threads){
            sum = sum + thread.getmRemainingSize();
//            Log.d("DownloadManagerThread", "Size: " + thread.getmRemainingSize());
        }
        return sum;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public Apk getApk() {
        return apk;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }
}

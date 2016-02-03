package pt.aptoide.backupapps.download;

import android.util.Log;
import pt.aptoide.backupapps.download.state.ActiveState;
import pt.aptoide.backupapps.download.state.ErrorState;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 02-07-2013
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
public class DownloadThread implements Runnable, ManagerThread {

    private long mFullSize;
    private long mProgress;
    private DownloadModel download;
    private long fileSize;

    public long getmDownloadedSize() {
        return mDownloadedSize;
    }

    public long getmProgress() {
        if(mProgress > 0){
            return mProgress;
        }else{
            return 0;
        }

    }




    private long mDownloadedSize = 0;


    public long getmFullSize() {
        return mFullSize;
    }

    public long getmRemainingSize() {
        return mRemainingSize;
    }

    private long mRemainingSize;
    private DownloadInfo parent;


    public DownloadThread(DownloadModel download, DownloadInfo parent) throws IOException {

        this.download = download;
        this.parent = parent;
        this.mConnection = download.createConnection();
        this.mProgress = DownloadFile.getFileLength(download.getDestination());
        this.mFullSize = mConnection.getShallowSize();
        this.mRemainingSize = mFullSize;
//        parent.addAlreadyDownloadedSize(mProgress);

    }

    DownloadConnection mConnection = null;
    DownloadFile mDownloadFile = null;


    @Override
    public void run() {

        try{
            mDownloadFile = download.createFile();
            this.mConnection = download.createConnection();
            this.mDownloadedSize = 0;
            fileSize = DownloadFile.getFileLength(download.getDestination());
            mDownloadFile.setDownloadedSize(fileSize);
            this.mRemainingSize = mConnection.connect(fileSize);

            Log.d("DownloadManager", "Starting Download " + (parent.getStatusState() instanceof ActiveState) + " "+this.mDownloadedSize+fileSize + " " +this.mRemainingSize);
            byte[] bytes = new byte[1024];
            int bytesRead;
            BufferedInputStream mStream = mConnection.getStream();
            while ( (bytesRead = mStream.read(bytes)) != -1 &&
                    parent.getStatusState() instanceof ActiveState) {
                mDownloadFile.getmFile().write(bytes, 0 , bytesRead);
                this.mDownloadedSize += bytesRead;
                this.mProgress += bytesRead;
            }

            if(parent.getStatusState() instanceof ActiveState){
                mDownloadFile.checkMd5();
            }


//            Log.d("DownloadManager", "Download done with " + new Md5Handler().md5Calc(new File(mDestination)));
        }catch (NotFoundException exception){
            exception.printStackTrace();
            parent.changeStatusState(new ErrorState(parent, EnumDownloadFailReason.NOT_FOUND));
        }catch (IPBlackListedException e){
            parent.changeStatusState(new ErrorState(parent, EnumDownloadFailReason.IP_BLACKLISTED));
        }catch (Md5FailedException e){
            e.printStackTrace();
            mDownloadFile.delete();
            parent.changeStatusState(new ErrorState(parent, EnumDownloadFailReason.MD5_CHECK_FAILED));
        } catch (UnknownHostException e){
            e.printStackTrace();
            parent.changeStatusState(new ErrorState(parent, EnumDownloadFailReason.CONNECTION_ERROR));
        } catch (IOException e){
            e.printStackTrace();
            parent.changeStatusState(new ErrorState(parent, EnumDownloadFailReason.CONNECTION_ERROR));
        } catch (CompletedDownloadException e) {

            mFullSize = mProgress = fileSize;
            mRemainingSize = 0;

            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
            parent.changeStatusState(new ErrorState(parent, EnumDownloadFailReason.CONNECTION_ERROR));
        }

        if(mDownloadFile!=null){
            mDownloadFile.close();
        }

        if(mConnection!=null){
            mConnection.close();
        }

//        BusProvider.getInstance().post(parent);

    }

    @Override
    protected void finalize() throws Throwable {

        Log.d("Garbage Collector", "DownloadThread beeing destroyed.");

        super.finalize();
    }
}

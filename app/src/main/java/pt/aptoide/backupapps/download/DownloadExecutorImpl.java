package pt.aptoide.backupapps.download;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import pt.aptoide.backupapps.BackupAppsApplication;
import pt.aptoide.backupapps.R;
import pt.aptoide.backupapps.model.Apk;

import java.io.*;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 08-07-2013
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class DownloadExecutorImpl implements DownloadExecutor {
    private final boolean root;
    NotificationManager managerNotification;
    Context context = BackupAppsApplication.getContext();
    NotificationCompat.Builder mBuilder;

    public DownloadExecutorImpl(boolean root) {
        this.root = root;
    }

    @Override
    public void execute(final String path, final Apk apk) {

        if(canRunRootCommands() && root){

            try {


                managerNotification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                final Process p = Runtime.getRuntime().exec("su");

                DataOutputStream os = new DataOutputStream(p.getOutputStream());
                // Execute commands that require root access
                os.writeBytes("pm install -r \"" + path + "\"\n");
                os.flush();
                mBuilder = new NotificationCompat.Builder(context);

                Intent onClick = new Intent(Intent.ACTION_VIEW);
                onClick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                onClick.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");

                // The PendingIntent to launch our activity if the user selects this notification
                PendingIntent onClickAction = PendingIntent.getActivity(context, 0, onClick, 0);
                mBuilder.setContentTitle("Aptoide Backup Apps")
                        .setContentText(context.getString(R.string.installing, apk.getName()));

                Bitmap bm = BitmapFactory.decodeFile(ImageLoader.getInstance().getDiscCache().get(apk.getPackageName() + "|" + apk.getVersionCode()).getAbsolutePath());


                mBuilder.setLargeIcon(bm);
                mBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
                mBuilder.setContentIntent(onClickAction);
                mBuilder.setAutoCancel(true);


                managerNotification.notify(apk.getId(), mBuilder.build());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                            int read;
                            char[] buffer = new char[4096];
                            StringBuilder output = new StringBuilder();
                            while ((read = reader.read(buffer)) > 0) {
                                output.append(buffer, 0, read);
                            }
                            reader.close();
                            p.waitFor();

                            String failure = output.toString();
                            Log.d("TAG", failure + " " + p.exitValue() + " " + apk.getPackageName() + " " + apk.getId());
                            if (p.exitValue() != 255 && !failure.toLowerCase(Locale.ENGLISH).contains("failure") && !failure.toLowerCase(Locale.ENGLISH).contains("segmentation")) {
                                // Sucess :-)

                                mBuilder = new NotificationCompat.Builder(context);

                                Intent onClick = context.getPackageManager().getLaunchIntentForPackage(apk.getPackageName());



                                if(onClick == null){
//                                    onClick = new Intent(Intent.ACTION_VIEW);
//                                    onClick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    onClick.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
                                    managerNotification.cancel(apk.getId());

                                    onClick = new Intent();

                                    try{
                                        context.getPackageManager().getPackageInfo(apk.getPackageName(),0);
                                    } catch (PackageManager.NameNotFoundException e){
                                        Intent install = new Intent(Intent.ACTION_VIEW);
                                        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        install.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
                                        Log.d("Aptoide", "Installing app: " + path);
                                        context.startActivity(install);
                                        return;
                                    }
                                }

                                // The PendingIntent to launch our activity if the user selects this notification
                                PendingIntent onClickAction = PendingIntent.getActivity(context, 0, onClick, 0);
                                mBuilder.setContentTitle(context.getString(R.string.app_name))
                                        .setContentText(context.getString(R.string.finished_install, apk.getName()));

                                Bitmap bm = BitmapFactory.decodeFile(ImageLoader.getInstance().getDiscCache().get(apk.getPackageName() + "|" + apk.getVersionCode()).getAbsolutePath());
                                mBuilder.setLargeIcon(bm);
                                mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                                mBuilder.setContentIntent(onClickAction);
                                mBuilder.setAutoCancel(true);
                                managerNotification.notify(apk.getId(), mBuilder.build());



                            }else{

                                managerNotification.cancel(apk.getId());
                                Intent install = new Intent(Intent.ACTION_VIEW);
                                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                install.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
                                Log.d("Aptoide", "Installing app: " + path);
                                context.startActivity(install);
                            }


                        } catch (Exception e){
                            e.printStackTrace();
                            managerNotification.cancel(apk.getId());
                            Intent install = new Intent(Intent.ACTION_VIEW);
                            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            install.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
                            Log.d("Aptoide", "Installing app: " + path);
                            context.startActivity(install);
                        }


                    }
                }).start();

                os.writeBytes("exit\n");
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

        }else{

            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
            Log.d("Aptoide", "Installing app: "+path);
            context.startActivity(install);

        }


    }

    public static boolean canRunRootCommands()
    {
        boolean retval;
        Process suProcess;

        try
        {
            suProcess = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            DataInputStream osRes = new DataInputStream(suProcess.getInputStream());

            // Getting the id of the current user to check if this is root
            os.writeBytes("id\n");
            os.flush();

            String currUid = osRes.readLine();
            boolean exitSu;
            if (null == currUid)
            {
                retval = false;
                exitSu = false;
                Log.d("ROOT", "Can't get root access or denied by user");
            }
            else if (currUid.contains("uid=0"))
            {
                retval = true;
                exitSu = true;
                Log.d("ROOT", "Root access granted");
            }
            else
            {
                retval = false;
                exitSu = true;
                Log.d("ROOT", "Root access rejected: " + currUid);
            }

            if (exitSu)
            {
                os.writeBytes("exit\n");
                os.flush();
            }
        }
        catch (Exception e)
        {
            // Can't get root !
            // Probably broken pipe exception on trying to write to output stream (os) after su failed, meaning that the device is not rooted

            retval = false;
            Log.d("ROOT", "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        return retval;
    }
}

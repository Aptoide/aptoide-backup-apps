package pt.aptoide.backupapps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONException;
import pt.aptoide.backupapps.download.DownloadExecutorImpl;
import pt.aptoide.backupapps.download.DownloadInfo;
import pt.aptoide.backupapps.download.DownloadManager;
import pt.aptoide.backupapps.download.DownloadModel;
import pt.aptoide.backupapps.model.Apk;
import pt.aptoide.backupapps.model.RepoApk;
import pt.aptoide.backupapps.util.Constants;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 26-08-2013
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class CheckPermissions extends AsyncTask<RepoApk, Void, ArrayList<DownloadModel>>{

    private final Context context;
    ProgressDialog pd;
    private static final String OBB_DESTINATION = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/obb/";
    private ArrayList<Long> list;
    private ArrayList<ApkPermission> permissionsList;
    private boolean root;

    private RepoApk apk;

    public CheckPermissions(ArrayList<Long> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setMessage(context.getString(R.string.please_wait));

        pd.show();
    }

    @Override
    protected ArrayList<DownloadModel> doInBackground(RepoApk... params) {

        this.apk = params[0];
        ArrayList<DownloadModel> models = new ArrayList<DownloadModel>();
        try {
            WebserviceGetApkInfo webservice = new WebserviceGetApkInfo("http://webservices.aptoide.com/", params[0]);
            permissionsList = webservice.getApkPermissions();
            if(webservice.hasOBB()){

                String mainObbUrl = webservice.getMainOBB().getString("path");
                String mainObbMd5 = webservice.getMainOBB().getString("md5sum");
                String mainObbName = webservice.getMainOBB().getString("filename");
                DownloadModel mainObbDownload = new DownloadModel(mainObbUrl, OBB_DESTINATION + apk.getPackageName() + "/" +mainObbName, mainObbMd5);
                models.add(mainObbDownload);
                if(webservice.hasPatchOBB()){
                    String patchObbUrl = webservice.getPatchOBB().getString("path");
                    String patchObbMd5 = webservice.getPatchOBB().getString("md5sum");
                    String patchObbName = webservice.getPatchOBB().getString("filename");
                    DownloadModel patchObbDownload = new DownloadModel(patchObbUrl, OBB_DESTINATION + apk.getPackageName() + "/" +patchObbName, patchObbMd5);
                    models.add(patchObbDownload);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return models;
    }

    @Override
    protected void onPostExecute(final ArrayList<DownloadModel> models) {
        super.onPostExecute(models);
        pd.dismiss();
        View v = LayoutInflater.from(context).inflate(R.layout.app_permission, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.container);

        if (permissionsList != null) {
            root = true;
            if (!permissionsList.isEmpty()) {
                for (ApkPermission permission : permissionsList) {
                    View permissionView = LayoutInflater.from(context).inflate(R.layout.row_permission, null);
                    ((TextView) permissionView.findViewById(R.id.permission)).setText(permission.getPermission());
                    ((TextView) permissionView.findViewById(R.id.description)).setText(permission.getDescription());
                    layout.addView(permissionView);
                }
            }else{
                TextView tv = new TextView(context);
                tv.setPadding(10, 10, 10, 10);
                tv.setText(context.getString(R.string.no_permissions_required));
                layout.addView(tv);
            }
        } else {
            root = false;
        }

        AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.setTitle(context.getString(R.string.restore) + " " + apk.getName() + "?");
        dialog.setIcon(R.drawable.ic_download);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownloadModel model = new DownloadModel(apk.getPath(), Constants.PATH_CACHE_APKS + "/" + apk.getPackageName() + "." + apk.getMd5Sum() + ".apk", apk.getMd5Sum());
                models.add(model);
                model.setAutoExecute(true);

                DownloadInfo info = DownloadManager.INSTANCE.getDownloadInfo(models, (apk.getPackageName() + apk.getVersionCode()).hashCode(), apk);
                info.setDownloadExecutor(new DownloadExecutorImpl(root));
                info.download();
                list.remove(0);

                if(list.isEmpty()){
                    context.startActivity(new Intent(context, Manager.class));
                }
            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.remove(0);

            }
        });


        dialog.show();
    }

}

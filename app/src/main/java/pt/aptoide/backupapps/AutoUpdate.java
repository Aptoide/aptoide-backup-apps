package pt.aptoide.backupapps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import pt.aptoide.backupapps.util.Constants;
import pt.aptoide.backupapps.util.Md5Handler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 23-08-2013
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
public class AutoUpdate extends AsyncTask<Void, Void, AutoUpdate.AutoUpdateInfo> {

    private static final String TMP_UPDATE_FILE = Environment.getExternalStorageDirectory().getPath()+ "/.aptoide/aptoideUpdate.apk";
    private static final String url = Constants.URI_LATEST_VERSION_XML;

    private Activity activity;

    public AutoUpdate(Activity activity){
        this.activity=activity;
    }

    @Override
    protected AutoUpdateInfo doInBackground(Void... params) {

        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            AutoUpdateHandler autoUpdateHandler = new AutoUpdateHandler();
            parser.parse(new URL(url).openStream(),autoUpdateHandler);
            return autoUpdateHandler.getAutoUpdateInfo();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(AutoUpdateInfo autoUpdateInfo) {
        super.onPostExecute(autoUpdateInfo);

        if(autoUpdateInfo!=null){
            String packageName = activity.getPackageName();
            int vercode = autoUpdateInfo.vercode;
            try {
                if(vercode > activity.getPackageManager().getPackageInfo(packageName,0).versionCode){
                    requestUpdateSelf(autoUpdateInfo);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void requestUpdateSelf(final AutoUpdateInfo autoUpdateInfo) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        final AlertDialog updateSelfDialog = dialogBuilder.create();
        updateSelfDialog.setTitle(activity.getText(R.string.update_self_title));
        updateSelfDialog.setIcon(R.drawable.ic_launcher);
        updateSelfDialog.setMessage(activity.getString(R.string.update_self_message, "Aptoide Backup Apps"));
        updateSelfDialog.setCancelable(false);
        updateSelfDialog.setButton(Dialog.BUTTON_POSITIVE, activity.getString(android.R.string.yes), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                new DownloadSelfUpdate().execute(autoUpdateInfo);
            }
        });
        updateSelfDialog.setButton(Dialog.BUTTON_NEGATIVE, activity.getString(android.R.string.no), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        updateSelfDialog.show();
    }


    private class AutoUpdateHandler extends DefaultHandler2{


        AutoUpdateInfo info = new AutoUpdateInfo();

        private AutoUpdateInfo getAutoUpdateInfo() {
            return info;
        }

        private StringBuilder sb = new StringBuilder();


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            sb.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            sb.append(ch,start,length);

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);

            if(localName.equals("versionCode")){
                info.vercode = Integer.parseInt(sb.toString());
            }else if(localName.equals("uri")){
                info.path = sb.toString();
            }else if(localName.equals("md5")){
                info.md5 = sb.toString();
            }

        }
    }

    static class AutoUpdateInfo{
        String md5;
        int vercode;
        String path;
    }

    private class DownloadSelfUpdate extends AsyncTask<AutoUpdateInfo, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(activity);
        String latestVersionUri;
        String referenceMd5;
        void retrieveUpdateParameters(AutoUpdateInfo autoUpdateInfo) {
            try {
                latestVersionUri = autoUpdateInfo.path;
                referenceMd5 = autoUpdateInfo.md5;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Aptoide-Auto-Update", "Update connection failed!  Keeping current version.");
            }
        }
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(activity.getString(R.string.update_self_message_retrieving_update));
            this.dialog.show();
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(AutoUpdateInfo... paramArrayOfParams) {
            try {
                retrieveUpdateParameters(paramArrayOfParams[0]);
                File f_chk = new File(TMP_UPDATE_FILE);
                if (f_chk.exists()) {
                    f_chk.delete();
                }
                FileOutputStream saveit = new FileOutputStream(TMP_UPDATE_FILE);
                DefaultHttpClient mHttpClient = new DefaultHttpClient();
                HttpGet mHttpGet = new HttpGet(latestVersionUri);
                HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
                if (mHttpResponse == null) {
                    Log.d("Aptoide", "Problem in network... retry...");
                    mHttpResponse = mHttpClient.execute(mHttpGet);
                    if (mHttpResponse == null) {
                        Log.d("Aptoide", "Major network exception... Exiting!");
                        saveit.close();
                        throw new TimeoutException();
                    }
                }
                if (mHttpResponse.getStatusLine().getStatusCode() == 401) {
                    saveit.close();
                    throw new TimeoutException();
                } else {
                    InputStream getit = mHttpResponse.getEntity().getContent();
                    byte data[] = new byte[8096];
                    int bytesRead;
                    bytesRead = getit.read(data, 0, 8096);
                    while (bytesRead != -1) {
                        saveit.write(data, 0, bytesRead);
                        bytesRead = getit.read(data, 0, 8096);
                    }
                    Log.d("Aptoide", "Download done!");
                    saveit.flush();
                    saveit.close();
                    getit.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Aptoide-Auto-Update", "Update connection failed!  Keeping current version.");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            super.onPostExecute(result);
            if (!(referenceMd5 == null)) {
                try {
                    File apk = new File(TMP_UPDATE_FILE);
                    if (referenceMd5.equalsIgnoreCase(Md5Handler.md5Calc(apk))) {
                        doUpdateSelf();
                    } else {
                        Log.d("Aptoide", referenceMd5 + " VS " + Md5Handler.md5Calc(apk));
                        throw new Exception(referenceMd5 + " VS " + Md5Handler.md5Calc(apk));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Aptoide-Auto-Update", "Update package checksum failed!  Keeping current version.");
                    if (this.dialog.isShowing()) {
                        this.dialog.dismiss();
                    }
                    super.onPostExecute(result);
                }
            }
        }
    }
    private void doUpdateSelf() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + TMP_UPDATE_FILE), "application/vnd.android.package-archive");
        activity.startActivityForResult(intent, 99);
    }
}

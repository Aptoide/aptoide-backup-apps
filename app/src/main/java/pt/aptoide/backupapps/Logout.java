package pt.aptoide.backupapps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.download.DownloadManager;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 31-07-2013
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class Logout extends AsyncTask<Void, Void, Void>{

    ProgressDialog pd;

    Activity activity;

    Logout(Activity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(activity);
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();
        super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected Void doInBackground(Void... params) {
        Database.getInstance().setLoggedIn(false);
        PreferenceManager.getDefaultSharedPreferences(activity).edit()
                .remove(Constants.LOGIN_USER_LOGIN)
                .remove(Constants.LOGIN_USER_PASSWORD)
                .remove(Constants.LOGIN_USER_DEFAULT_REPO)
                .remove(Constants.LOGIN_USER_TOKEN)
                .remove(Constants.LOGIN_USER_AVATAR)
                .remove(Constants.LOGIN_FROM_SIGNUP)
                .remove(Constants.LOGGED_FROM_ACCOUNT_MANAGER)
                .commit();
        Database.getInstance().removeAllData();
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pd.dismiss();
        BusProvider.getInstance().post(new LogoutEvent());
        DownloadManager.INSTANCE.removeAllActiveDownloads();
        activity.finish();
    }
}

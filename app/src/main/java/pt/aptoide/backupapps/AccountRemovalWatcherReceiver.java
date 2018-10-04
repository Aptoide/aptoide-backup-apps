package pt.aptoide.backupapps;

import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.util.Constants;

/**
 * Created by j-pac on 16-04-2014.
 */
public class AccountRemovalWatcherReceiver extends BroadcastReceiver {

  @Override public void onReceive(Context context, Intent intent) {

    AccountManager accountManager = AccountManager.get(context);
    SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);

    boolean notLoggedInAccountManager =
        accountManager.getAccountsByType("cm.aptoide.pt").length == 0;

    if (notLoggedInAccountManager) {
      if (sPref.getBoolean(Constants.LOGGED_FROM_ACCOUNT_MANAGER, false)) {

        sPref.edit()
            .remove(Constants.LOGIN_USER_LOGIN)
            .remove(Constants.LOGIN_USER_PASSWORD)
            .remove(Constants.LOGIN_USER_DEFAULT_REPO)
            .remove(Constants.LOGIN_USER_TOKEN)
            .remove(Constants.LOGIN_USER_AVATAR)
            .remove(Constants.LOGIN_FROM_SIGNUP)
            .remove(Constants.LOGGED_FROM_ACCOUNT_MANAGER)
            .commit();

        BusProvider.getInstance()
            .post(new LogoutEvent().setFromAccountManager(true));
      }
    }
  }
}

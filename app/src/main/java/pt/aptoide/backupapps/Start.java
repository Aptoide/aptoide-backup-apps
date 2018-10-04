package pt.aptoide.backupapps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import pt.aptoide.backupapps.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 31-07-2013
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class Start extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);

    finish();
    if (!sPref.contains(Constants.LOGIN_USER_LOGIN)) {
      startActivity(new Intent(this, LoginActivity.class));
    } else {
      startActivity(new Intent(this, MainActivity.class));
    }
  }
}

package pt.aptoide.backupapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 31-07-2013
 * Time: 12:01
 * To change this template use File | Settings | File Templates.
 */
public class FragmentLogin extends SherlockFragment {

  private MainActivity main_activity;
  private SharedPreferences previousSPref;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    sPref = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
    previousSPref =
        getSherlockActivity().getSharedPreferences("aptoide_preferences", Context.MODE_PRIVATE);

    return inflater.inflate(R.layout.login_screen, null);
  }

  private EditText username;
  private EditText password;

  private LoginButton fbAuthButton;
  private SignInButton gSignInButton;

  SharedPreferences sPref;

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    username = (EditText) view.findViewById(R.id.box_username);
    password = (EditText) view.findViewById(R.id.box_password);

    if (previousSPref.contains("SERVER_USERNAME")) {
      String previousUsername = previousSPref.getString("SERVER_USERNAME", "");
      String passHash = previousSPref.getString("SERVER_PASSHASH", "");
      username.setText(previousUsername);
      password.setText(passHash);
      Login login = new Login(username.getText()
          .toString(), password.getText()
          .toString(), true);
      login.setFromUpdate(true);
      new CheckUserCredentials(getSherlockActivity()).execute(login);
    }

    view.findViewById(R.id.button_loggin)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {

            if (username.getText()
                .length() == 0) {
              Toast.makeText(getSherlockActivity(), R.string.login_short_bad_login,
                  Toast.LENGTH_LONG)
                  .show();
              return;
            }

            if (password.getText()
                .length() == 0) {
              Toast.makeText(getSherlockActivity(), R.string.login_short_bad_login,
                  Toast.LENGTH_LONG)
                  .show();
              return;
            }

            new CheckUserCredentials(getSherlockActivity()).execute(new Login(username.getText()
                .toString(), password.getText()
                .toString(), false));
          }
        });

    if (getArguments() != null && getArguments().containsKey("username")) {
      username.setText(getArguments().getString("username"));
      password.setText(getArguments().getString("password"));
      new CheckUserCredentials(getSherlockActivity()).execute(new Login(username.getText()
          .toString(), password.getText()
          .toString(), true));
    }

    view.findViewById(R.id.button_new_to_aptoide)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            Fragment fragmentLogin = getFragmentManager().findFragmentByTag("loginFragment");
            Fragment createAccount = new FragmentCreateAccount();
            getFragmentManager().beginTransaction()
                .remove(fragmentLogin)
                .add(R.id.frag_container_A, createAccount, "createAccountFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
          }
        });

    view.findViewById(R.id.button_forgot_password)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            Intent passwordRecovery = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://m.aptoide.com/account/password-recovery"));
            startActivity(passwordRecovery);
          }
        });

    if (Build.VERSION.SDK_INT >= 8) {
      fbAuthButton = (LoginButton) view.findViewById(R.id.fb_loginButton);
      gSignInButton = (SignInButton) view.findViewById(R.id.g_sign_in_button);

      fbAuthButton.setReadPermissions(Arrays.asList("email", "user_friends"));

      gSignInButton.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          int val = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getSherlockActivity());
          if (val == ConnectionResult.SUCCESS) {
            if (main_activity != null) {
              main_activity.connectPlusClient();
            }
          } else {
            Toast.makeText(getSherlockActivity(),
                getString(R.string.google_login_message_play_services_not_availab1le),
                Toast.LENGTH_SHORT)
                .show();
          }
        }
      });
    }
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.d("TAG", "Fragments List " + getFragmentManager().getFragments());
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    main_activity = (MainActivity) activity;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    MenuItem populateItem = menu.add(Menu.NONE, 1, 0, "Populate");
    populateItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    MenuItem clearItem = menu.add(Menu.NONE, 2, 0, "Clear");
    clearItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    super.onCreateOptionsMenu(menu, inflater);
  }
}

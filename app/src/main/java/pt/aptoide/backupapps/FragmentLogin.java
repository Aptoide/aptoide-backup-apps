package pt.aptoide.backupapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 31-07-2013
 * Time: 12:01
 * To change this template use File | Settings | File Templates.
 */
public class FragmentLogin extends Fragment {

  SharedPreferences sPref;
  private MainActivity main_activity;
  private SharedPreferences previousSPref;

  private LoginButton fbAuthButton;
  private SignInButton gSignInButton;
  private Button logoutButton;
  private GoogleSignInClient mGoogleSignInClient;
  private GoogleSignInOptions gso;
  private static final int RC_SIGN_IN = 9001;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    main_activity = (MainActivity) activity;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(BuildConfig.GOOGLE_OAUTH_SERVER_ID)
        .build();

    mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

    setHasOptionsMenu(true);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    previousSPref = getActivity().getSharedPreferences("aptoide_preferences", Context.MODE_PRIVATE);

    return inflater.inflate(R.layout.login_screen, null);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    logoutButton = view.findViewById(R.id.logout_button);
    logoutButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mGoogleSignInClient.signOut();
        Toast.makeText(getContext(), "User signed out", Toast.LENGTH_LONG).show();
        showLogins();
      }
    });

    gSignInButton = (SignInButton) view.findViewById(R.id.g_sign_in_button);
    gSignInButton.setSize(SignInButton.SIZE_STANDARD);
    gSignInButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
      }
    });

    fbAuthButton = (LoginButton) view.findViewById(R.id.fb_loginButton);
    fbAuthButton.setReadPermissions(Arrays.asList("email", "user_friends"));
    fbAuthButton.registerCallback(
        ((BackupAppsApplication) getActivity().getApplicationContext()).getCallbackManager(),
        new FacebookCallback<LoginResult>() {
          @Override public void onSuccess(LoginResult loginResult) {
            Log.d("Facebook token", AccessToken.getCurrentAccessToken()
                .getToken());
            GraphRequest meRequest =
                GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                      @Override
                      public void onCompleted(final JSONObject user, GraphResponse response) {

                        String username = null;
                        try {
                          username = user.get("email") == null ? "" : user.get("email")
                              .toString();
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }

                        if (TextUtils.isEmpty(username)) {

                          //session.close();

                          getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                              Toast.makeText(getActivity(), R.string.facebook_error,
                                  Toast.LENGTH_LONG)
                                  .show();
                            }
                          });
                        } else {
                          Login login = new Login(username, AccessToken.getCurrentAccessToken()
                              .getToken(), "facebook_backupapps");
                          new CheckUserCredentials((AppCompatActivity) getActivity()).execute(
                              login);
                        }
                        //session.closeAndClearTokenInformation();
                      }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            meRequest.setParameters(parameters);
            meRequest.executeAsync();
          }

          @Override public void onCancel() {

          }

          @Override public void onError(FacebookException error) {
            if (error.getMessage()
                .equals("Log in attempt aborted.")) {
              return;
            }

            error.printStackTrace();
          }
        });

  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.d("TAG", "Fragments List " + getFragmentManager().getFragments());
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      handleSignInResult(task);
    } else {
      ((BackupAppsApplication) getActivity().getApplicationContext()).getCallbackManager().onActivityResult(
          requestCode, resultCode, data);
    }
  }

  private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
      GoogleSignInAccount account = completedTask.getResult(ApiException.class);
      Login login = new Login(account.getDisplayName(), AccessToken.getCurrentAccessToken()
          .getToken(), "google backupapps", account.getEmail()); //authmode string might not be the one provided
      new CheckUserCredentials((AppCompatActivity) getActivity()).execute(
          login);
      updateUI(account);
    } catch (ApiException e) {
      Log.w("googleSignIn", "signInResult:failed code=" + e.getStatusCode());
      updateUI(null);
    }
  }

  private void updateUI(GoogleSignInAccount account) {
    if (account != null) {
      Toast.makeText(getContext(), "User signed in with Google", Toast.LENGTH_LONG).show();
      showGoogleLogout();
    }
  }
  private void showLogins(){
    logoutButton.setVisibility(View.GONE);
    fbAuthButton.setVisibility(View.VISIBLE);
    gSignInButton.setVisibility(View.VISIBLE);
  }
  private void showGoogleLogout(){
    logoutButton.setVisibility(View.VISIBLE);
    fbAuthButton.setVisibility(View.GONE);
    gSignInButton.setVisibility(View.GONE);
  }
}

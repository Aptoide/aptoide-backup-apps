package pt.aptoide.backupapps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.util.Constants;
import pt.aptoide.backupapps.util.NetworkUtils;

/**
 * Created with IntelliJ IDEA.
 * User: brutus
 * Date: 29-11-2013
 * Time: 12:08
 * To change this template use File | Settings | File Templates.
 */
public class CheckUserCredentials extends AsyncTask<Login, Void, LoginResponse> {

  SharedPreferences previousSPref;
  SharedPreferences sPref;
  ProgressDialog pd;
  Login login;

  SherlockFragmentActivity activity;

  public CheckUserCredentials(SherlockFragmentActivity activity) {
    this.activity = activity;
    sPref = PreferenceManager.getDefaultSharedPreferences(activity);
    previousSPref = activity.getSharedPreferences("aptoide_preferences", Context.MODE_PRIVATE);
  }

  @Override protected LoginResponse doInBackground(Login... params) {
    LoginResponse response = new LoginResponse();
    login = params[0];

    JSONObject loginJson = null;

    try {
      loginJson = NetworkUtils.post(Constants.URI_LOGIN_WS, formatPostData(login));

      if (loginJson.has("status")) {

        String status = loginJson.getString("status");

        if ("OK".equals(status)) {

          if (!loginJson.isNull("repo")) {
            response.setToken(loginJson.getString("token"));
            response.setStoreAvatar(loginJson.getString("avatar"));
            response.setDefaultStore(loginJson.getString("repo"));
            response.setFromSignup(login.isFromSignUp());
          } else {
            response.setError(EnumServerLoginStatus.NO_DEFAULT_REPO);
          }
        } else {
          if (loginJson.has("errors")) {
            try {
              Log.d("BackupApps", loginJson.toString(4));
            } catch (JSONException e1) {
              e1.printStackTrace();
            }
            JSONArray array = loginJson.getJSONArray("errors");
            EnumServerLoginStatus statusEnum = EnumServerLoginStatus.REPO_SERVICE_UNAVAILABLE;
            for (int i = 0; i != array.length(); i++) {
              String error = array.getJSONObject(i)
                  .getString("code");

              if (error.equals("MARG-201")) {
                //Missing authentication parameter(s): user and/or passhash
                statusEnum = EnumServerLoginStatus.BAD_LOGIN;
              } else if (error.equals("AUTH-1")) {
                //Missing authentication parameter(s): user and/or passhash
                statusEnum = EnumServerLoginStatus.BAD_LOGIN;
              } else if (error.equals("REPO-1")) {
                //The provided store does not exist.
                statusEnum = EnumServerLoginStatus.REPO_SERVICE_UNAVAILABLE;
              } else if (error.equals("REPO-3")) {
                //The provided store does not belong to this user.
                statusEnum = EnumServerLoginStatus.REPO_NOT_FROM_DEVELOPPER;
              } else if (error.equals("WOP-3")) {
                //A store already exists with the same name. Please give another name for your store.
                statusEnum = EnumServerLoginStatus.REPO_NAME_ALREADY_EXISTS;
              }
            }
            response.setError(statusEnum);
          }
          return response;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();

      response.setError(EnumServerLoginStatus.LOGIN_SERVICE_UNAVAILABLE);
      try {
        if (loginJson != null) Log.d("BackupApps", loginJson.toString(4));
      } catch (JSONException e1) {
        e1.printStackTrace();
      }
      return response;
    } catch (JSONException e) {

      response.setError(EnumServerLoginStatus.LOGIN_SERVICE_UNAVAILABLE);
      try {
        Log.d("BackupApps", loginJson.toString(4));
      } catch (JSONException e1) {
        e1.printStackTrace();
      }

      e.printStackTrace();
      return response;
    }

    return response;
  }  @Override protected void onPreExecute() {
    super.onPreExecute();
    pd = new ProgressDialog(activity);
    pd.setMessage("Please wait");
    pd.setCancelable(false);
    pd.show();
  }

  private String formatPostData(Login login) {
    Login.LoginMode loginMode = login.getLoginMode();
    String requestData = null;

    try {

      if (loginMode == Login.LoginMode.REGULAR) {
        requestData =
            URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(login.getUsername(),
                "UTF-8");
        requestData += "&" + URLEncoder.encode("passhash", "UTF-8") + "=" + URLEncoder.encode(
            login.getPasswordSha1(), "UTF-8");

        if (login.getRepo() != null) {
          login.setFromSignUp(true);
          requestData += "&" + URLEncoder.encode("createRepo", "UTF-8") + "=" + URLEncoder.encode(
              Integer.toString(1), "UTF-8");
          requestData +=
              "&" + URLEncoder.encode("repo", "UTF-8") + "=" + URLEncoder.encode(login.getRepo(),
                  "UTF-8");
          if (login.isRepoPrivate()) {
            requestData +=
                "&" + URLEncoder.encode("privacy", "UTF-8") + "=" + URLEncoder.encode("true",
                    "UTF-8");
            requestData +=
                "&" + URLEncoder.encode("privacy_user", "UTF-8") + "=" + URLEncoder.encode(
                    login.getPrivateRepoUsername(), "UTF-8");
            requestData +=
                "&" + URLEncoder.encode("privacy_pass", "UTF-8") + "=" + URLEncoder.encode(
                    login.getPrivateRepoPassword(), "UTF-8");
          }
        }
      } else if (loginMode == Login.LoginMode.FACEBOOK_OAUTH
          || loginMode == Login.LoginMode.GOOGLE_OAUTH) {
        if (login.getRepo() == null) {
          requestData =
              URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(login.getUsername(),
                  "UTF-8");
          requestData += "&" + URLEncoder.encode("authMode", "UTF-8") + "=" + URLEncoder.encode(
              login.getoAuthMode(), "UTF-8");
          requestData += "&" + URLEncoder.encode("oauthToken", "UTF-8") + "=" + URLEncoder.encode(
              login.getoAuthAccessToken(), "UTF-8");
          if (loginMode == Login.LoginMode.GOOGLE_OAUTH) {
            requestData +=
                "&" + URLEncoder.encode("oauthUserName", "UTF-8") + "=" + URLEncoder.encode(
                    login.getoAuthUsername(), "UTF-8");
          }
        } else {
          login.setFromSignUp(true);
          requestData =
              URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(login.getUsername(),
                  "UTF-8");
          requestData +=
              "&" + URLEncoder.encode("repo", "UTF-8") + "=" + URLEncoder.encode(login.getRepo(),
                  "UTF-8");
          if (login.isRepoPrivate()) {
            requestData +=
                "&" + URLEncoder.encode("privacy", "UTF-8") + "=" + URLEncoder.encode("true",
                    "UTF-8");
            requestData +=
                "&" + URLEncoder.encode("privacy_user", "UTF-8") + "=" + URLEncoder.encode(
                    login.getPrivateRepoUsername(), "UTF-8");
            requestData +=
                "&" + URLEncoder.encode("privacy_pass", "UTF-8") + "=" + URLEncoder.encode(
                    login.getPrivateRepoPassword(), "UTF-8");
          }
          requestData += "&" + URLEncoder.encode("authMode", "UTF-8") + "=" + URLEncoder.encode(
              login.getoAuthMode(), "UTF-8");
          requestData += "&" + URLEncoder.encode("oauthToken", "UTF-8") + "=" + URLEncoder.encode(
              login.getoAuthAccessToken(), "UTF-8");
          if (loginMode == Login.LoginMode.GOOGLE_OAUTH) {
            requestData +=
                "&" + URLEncoder.encode("oauthUserName", "UTF-8") + "=" + URLEncoder.encode(
                    login.getoAuthUsername(), "UTF-8");
          }
          requestData +=
              "&" + URLEncoder.encode("oauthCreateRepo", "UTF-8") + "=" + URLEncoder.encode(
                  Integer.toString(1), "UTF-8");
          requestData += "&" + URLEncoder.encode("privacy", "UTF-8") + "=" + URLEncoder.encode(
              ((login.isRepoPrivate()) ? "true" : "false"), "UTF-8");
        }
      }
      requestData +=
          "&" + URLEncoder.encode("mode", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    Log.d("MYLOG", "Post data: " + requestData);
    return requestData;
  }

  public void showRepoCreatorDialog() {
    DialogFragment dialog = new RepoCreatorDialog();
    dialog.show(activity.getSupportFragmentManager(), "RepoCreatorDialog");
  }

  public class RepoCreatorDialog extends DialogFragment {

    private EditText repository;
    private RadioButton privateButton;
    private RadioButton publicButton;
    private EditText repoUsername;
    private EditText repoPassword;

    private boolean logoutOnDismiss = true;

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {

      ContextThemeWrapper contextThemeWrapper =
          new ContextThemeWrapper(activity, R.style.RepoDialog);

      AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);

      LayoutInflater inflater = LayoutInflater.from(contextThemeWrapper);
      View view = inflater.inflate(R.layout.repo_creator, null);

      repository = (EditText) view.findViewById(R.id.repository);
      privateButton = (RadioButton) view.findViewById(R.id.private_store);
      publicButton = (RadioButton) view.findViewById(R.id.public_store);
      repoUsername = (EditText) view.findViewById(R.id.repo_username);
      repoPassword = (EditText) view.findViewById(R.id.repo_password);

      builder.setTitle(R.string.store_creation_title)
          .setView(view)
          .setPositiveButton(R.string.create_repo_button, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              if (repository.getText() != null) {

                boolean isRepoPrivate = privateButton.isChecked();

                login.setRepo(repository.getText()
                    .toString(), isRepoPrivate);

                if (isRepoPrivate) {
                  login.setPrivateRepoUsername(repoUsername.getText()
                      .toString()
                      .trim());
                  login.setPrivateRepoPassword(repoPassword.getText()
                      .toString()
                      .trim());
                }

                if (login.getRepo() != null
                    && login.getRepo()
                    .length() != 0) {
                  if (isRepoPrivate) {
                    if (login.getPrivateRepoUsername() == null
                        || login.getPrivateRepoUsername()
                        .length() == 0) {
                      Toast.makeText(getActivity(),
                          getString(R.string.store_creation_message_username_undefined),
                          Toast.LENGTH_SHORT)
                          .show();
                      showRepoCreatorDialog();
                    } else if (login.getPrivateRepoPassword() == null
                        || login.getPrivateRepoPassword()
                        .length() == 0) {
                      Toast.makeText(getActivity(),
                          getString(R.string.store_creation_message_store_password_undefined),
                          Toast.LENGTH_SHORT)
                          .show();
                      showRepoCreatorDialog();
                    } else {
                      new CheckUserCredentials(activity).execute(login);
                    }
                  } else {
                    new CheckUserCredentials(activity).execute(login);
                  }
                } else {
                  Toast.makeText(getActivity(), getString(R.string.store_creation_short_no_name),
                      Toast.LENGTH_SHORT)
                      .show();
                  showRepoCreatorDialog();
                }
              }

              logoutOnDismiss = false;
            }
          })
          .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              RepoCreatorDialog.this.getDialog()
                  .cancel();
              BusProvider.getInstance()
                  .post(new LogoutEvent());
            }
          });

      repoUsername.setText(login.getUsername()
          .split("@")[0]);

      privateButton.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (privateButton.isChecked()) {
            repoPassword.setVisibility(View.VISIBLE);
            repoUsername.setVisibility(View.VISIBLE);
          }
        }
      });

      publicButton.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (publicButton.isChecked()) {
            repoPassword.setVisibility(View.GONE);
            repoUsername.setVisibility(View.GONE);
          }
        }
      });

      InputFilter filter = new InputFilter() {
        private Pattern p = Pattern.compile("^[a-zA-Z0-9-]*$");

        @Override public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
            int dstart, int dend) {
          for (int i = start; i < end; i++) {
            if (!p.matcher(Character.toString(source.charAt(i)))
                .find()) {
              return "";
            }
          }
          return null;
        }
      };

      repository.setFilters(new InputFilter[] { filter });

      return builder.create();
    }

    @Override public void onDismiss(DialogInterface dialog) {
      super.onDismiss(dialog);
      if (logoutOnDismiss) {
        BusProvider.getInstance()
            .post(new LogoutEvent());
      }
    }
  }  @Override protected void onPostExecute(LoginResponse response) {
    super.onPostExecute(response);
    pd.dismiss();

    switch (response.getError()) {

      case SUCCESS:
        if (login.isUpdate()) {
          previousSPref.edit()
              .remove("SERVER_USERNAME")
              .remove("SERVER_PASSHASH")
              .commit();
        }

        sPref.edit()
            .putString(Constants.LOGIN_USER_LOGIN, login.getUsername())
            .putString(Constants.LOGIN_USER_PASSWORD,
                ((login.getPassword() != null) ? login.getPasswordSha1() : ""))
            .putString(Constants.LOGIN_USER_TOKEN, response.getToken())
            .putString(Constants.LOGIN_USER_DEFAULT_REPO, response.getDefaultStore())
            .putString(Constants.LOGIN_USER_AVATAR, response.getStoreAvatar())
            .putBoolean(Constants.LOGIN_FROM_SIGNUP, response.isFromSignup())
            .commit();

        if (login.isRepoPrivate()) {
          sPref.edit()
              .putBoolean(Constants.LOGIN_USER_REPO_PRIVACY, login.isRepoPrivate())
              .putString(Constants.LOGIN_USER_PRIVATE_REPO_USERNAME, login.getPrivateRepoUsername())
              .putString(Constants.LOGIN_USER_PRIVATE_REPO_PASSWORD, login.getPrivateRepoPassword())
              .commit();
        }

        if (login.isFromAccountManager()) {
          sPref.edit()
              .putBoolean(Constants.LOGGED_FROM_ACCOUNT_MANAGER, true)
              .commit();
        }

        new PostLogin().execute(response);
        break;

      case NO_DEFAULT_REPO:
        showRepoCreatorDialog();
        break;

      case REPO_NAME_ALREADY_EXISTS:
        Toast.makeText(activity, response.getError()
            .toString(activity), Toast.LENGTH_SHORT)
            .show();
        showRepoCreatorDialog();
        break;

      default:
        Toast.makeText(activity, response.getError()
            .toString(activity), Toast.LENGTH_SHORT)
            .show();
        break;
    }
  }

  public class PostLogin extends AsyncTask<LoginResponse, Void, Void> {

    @Override protected Void doInBackground(LoginResponse... params) {

      LoginResponse response = params[0];
      Database.getInstance()
          .setLoggedIn(true);
      Database.getInstance()
          .removeAllData();

        /*if(sPref.getBoolean(Constants.LOGIN_USER_REPO_PRIVACY, false)) {
            Database.getInstance().insertServer("http://" + response.getDefaultStore() + Constants.DOMAIN_APTOIDE_STORE,
                    sPref.getString(Constants.LOGIN_USER_PRIVATE_REPO_USERNAME, ""),
                    sPref.getString(Constants.LOGIN_USER_PRIVATE_REPO_PASSWORD, "") );
        } else {*/

      String serverUsername = null;
      String serverPassword = null;

      if (login.getLoginMode() == Login.LoginMode.REGULAR) {
        serverUsername = sPref.getString(Constants.LOGIN_USER_LOGIN, "");
        serverPassword = sPref.getString(Constants.LOGIN_USER_PASSWORD, "");
      }

      Database.getInstance()
          .insertServer("http://" + response.getDefaultStore() + Constants.DOMAIN_APTOIDE_STORE,
              serverUsername, serverPassword);

      return null;
    }

    @Override protected void onPostExecute(Void aVoid) {
      BusProvider.getInstance()
          .post(new LoginEvent());
      super.onPostExecute(aVoid);
    }
  }




}

package pt.aptoide.backupapps;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pt.aptoide.backupapps.model.NewAccount;
import pt.aptoide.backupapps.util.Algorithms;
import pt.aptoide.backupapps.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 31-07-2013
 * Time: 12:01
 * To change this template use File | Settings | File Templates.
 */
public class FragmentCreateAccount extends Fragment {

  NewAccount account;
  private Button signup;
  private View button;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.form_signup,
        null);    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Override public void onViewCreated(final View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final EditText email = (EditText) view.findViewById(R.id.username);
    final EditText password = (EditText) view.findViewById(R.id.password);
    final EditText repository = (EditText) view.findViewById(R.id.repository);
    final RadioButton privateButton = (RadioButton) view.findViewById(R.id.private_store);
    signup = (Button) view.findViewById(R.id.signup);

    signup.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        v.setEnabled(false);
        button = v;
        if (email.getText()
            .toString()
            .length() == 0
            || password.getText()
            .toString()
            .length() == 0
            || repository.getText()
            .toString()
            .length() == 0) {
          Toast.makeText(getActivity(),
              getString(R.string.create_account_toast_short_fill_all_forms), Toast.LENGTH_LONG)
              .show();
          v.setEnabled(true);
        } else if (!has1number1letter(password.getText()
            .toString())) {
          Toast.makeText(getActivity(), getString(R.string.signup_message_password_validation_text),
              Toast.LENGTH_LONG)
              .show();
          v.setEnabled(true);
        } else {

          String emailString = email.getText()
              .toString();
          String passwordString = password.getText()
              .toString();
          String repositoryString = repository.getText()
              .toString();
          boolean isPrivate = privateButton.isChecked();
          account = new NewAccount(emailString, passwordString, repositoryString, isPrivate);
          new CreateAccount().execute(account);
        }
      }
    });

    view.findViewById(R.id.button_new_to_aptoide)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            Log.d("TAG", "Fragments list onClick " + getFragmentManager().getFragments());
            Fragment loginFragment = new FragmentLogin();
            getFragmentManager().beginTransaction()
                .replace(R.id.frag_container_A, loginFragment, "loginFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
          }
        });
  }

  private boolean has1number1letter(String pass) {
    boolean hasLetter = false;
    boolean hasNumber = false;

    if (pass.contains("!")
        || pass.contains("@")
        || pass.contains("#")
        || pass.contains("$")
        || pass.contains("#")
        || pass.contains("*")) {

      hasNumber = true;
    }

    for (char c : pass.toCharArray()) {
      if (Character.isLetter(c) && !hasLetter) {
        hasLetter = true;
      } else if (Character.isDigit(c) && !hasNumber) {
        hasNumber = true;
      }
    }

    return hasNumber && hasLetter;
  }

  public class CreateAccount extends AsyncTask<NewAccount, Void, JSONObject> {

    ProgressDialog pd;
    String url = Constants.URI_LOGIN_CREATE_WS;

    @Override protected JSONObject doInBackground(NewAccount... params) {

      NewAccount account = params[0];
      String data;
      try {

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        String password_sha1 = Algorithms.computeSHA1sum(account.getPassword());
        String hmac = Algorithms.computeHmacSha1(
            account.getEmail() + password_sha1 + account.getStoreName() + (account.isPrivate()
                ? "true" : ""), "bazaar_hmac");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(account.getEmail(),
            "UTF-8");
        data +=
            "&" + URLEncoder.encode("passhash", "UTF-8") + "=" + URLEncoder.encode(password_sha1,
                "UTF-8");
        data += "&" + URLEncoder.encode("repo", "UTF-8") + "=" + URLEncoder.encode(
            account.getStoreName(), "UTF-8");
        if (account.isPrivate()) {
          data += "&" + URLEncoder.encode("privacy", "UTF-8") + "=" + URLEncoder.encode("true",
              "UTF-8");
        }
        data += "&" + URLEncoder.encode("hmac", "UTF-8") + "=" + URLEncoder.encode(hmac, "UTF-8");
        data += "&" + URLEncoder.encode("mode", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8");
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(data);
        wr.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
          sb.append(line)
              .append("\n");
        }
        wr.close();
        br.close();
        return new JSONObject(sb.toString());
      } catch (IOException e) {
        e.printStackTrace();
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      } catch (InvalidKeyException e) {
        e.printStackTrace();
      } catch (JSONException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override protected void onPreExecute() {
      super.onPreExecute();

      pd = new ProgressDialog(getActivity());
      pd.setMessage(getString(R.string.short_please_wait));
      pd.show();
      pd.setCancelable(false);
    }

    @Override protected void onPostExecute(JSONObject jsonObject) {
      super.onPostExecute(jsonObject);
      pd.dismiss();
      signup.setEnabled(true);
      if (jsonObject != null) {
        try {
          if (jsonObject.getString("status")
              .equals("OK")) {

            FragmentLogin loginFragment = new FragmentLogin();

            Bundle bundle = new Bundle();

            bundle.putString("username", account.getEmail());
            bundle.putString("password", account.getPassword());
            loginFragment.setArguments(bundle);

            getFragmentManager().beginTransaction()
                .replace(R.id.frag_container_A, loginFragment, "loginFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
          } else if (jsonObject.has("errors")) {
            button.setEnabled(true);
            JSONArray array = jsonObject.getJSONArray("errors");
            EnumServerLoginCreateStatus status = EnumServerLoginCreateStatus.BAD_LOGIN;
            String error = "";

            for (int i = 0; i != array.length(); i++) {
              error = array.getString(i);
            }
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG)
                .show();
          }
        } catch (JSONException e) {
          Toast.makeText(getActivity(), R.string.upload_fail_short_failed_server_connection,
              Toast.LENGTH_SHORT)
              .show();
          e.printStackTrace();
        }
      } else {
        button.setEnabled(true);
        Toast.makeText(getActivity(), R.string.upload_fail_short_failed_server_connection,
            Toast.LENGTH_SHORT)
            .show();
      }
    }
  }
}

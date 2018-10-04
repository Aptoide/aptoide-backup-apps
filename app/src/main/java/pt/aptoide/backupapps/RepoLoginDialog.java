package pt.aptoide.backupapps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.download.event.AskRepoLoginDataEvent;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: brutus
 * Date: 01-12-2013
 * Time: 2:18
 * To change this template use File | Settings | File Templates.
 */

public class RepoLoginDialog extends DialogFragment {

  private EditText repoUsername;
  private EditText repoPassword;

  private String repoName;
  private boolean logoutOnDismiss = true;

  public RepoLoginDialog(String repoName) {
    this.repoName = repoName;
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {

    ContextThemeWrapper contextThemeWrapper =
        new ContextThemeWrapper(getActivity(), R.style.RepoDialog);

    AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);

    LayoutInflater inflater = LayoutInflater.from(contextThemeWrapper);
    View view = inflater.inflate(R.layout.form_login_repo, null);

    repoUsername = (EditText) view.findViewById(R.id.repo_username);
    repoPassword = (EditText) view.findViewById(R.id.repo_password);

    builder.setTitle("Private store: " + repoName)
        .setView(view)
        .setPositiveButton(R.string.store_login_dialog_button,
            new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {

                Database.getInstance()
                    .setLoggedIn(true);

                if (repoUsername.getText()
                    .toString() == null
                    || repoUsername.getText()
                    .toString()
                    .length() == 0) {
                  Toast.makeText(getActivity(), R.string.store_login_short_empty_store_username,
                      Toast.LENGTH_SHORT)
                      .show();
                  BusProvider.getInstance()
                      .post(new AskRepoLoginDataEvent(false));
                } else if (repoPassword.getText()
                    .toString() == null
                    || repoPassword.getText()
                    .toString()
                    .length() == 0) {
                  Toast.makeText(getActivity(), R.string.store_login_short_empty_store_password,
                      Toast.LENGTH_SHORT)
                      .show();
                  BusProvider.getInstance()
                      .post(new AskRepoLoginDataEvent(false));
                } else {

                  Database.getInstance()
                      .updateServerPassword("http://" + repoName + Constants.DOMAIN_APTOIDE_STORE,
                          repoUsername.getText()
                              .toString()
                              .trim(), repoPassword.getText()
                              .toString()
                              .trim());

                  BusProvider.getInstance()
                      .post(new BackedUpRefreshEvent(MainActivity.currentSort));
                  BusProvider.getInstance()
                      .post(new ParseServerEvent("login"));

                  logoutOnDismiss = false;
                }
              }
            })
        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            RepoLoginDialog.this.getDialog()
                .cancel();
            BusProvider.getInstance()
                .post(new LogoutEvent());
          }
        });

    return builder.create();
  }

  @Override public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    if (logoutOnDismiss) {
      BusProvider.getInstance()
          .post(new LogoutEvent());
    }
  }
}


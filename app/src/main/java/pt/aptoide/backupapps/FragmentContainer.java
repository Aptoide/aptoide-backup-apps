package pt.aptoide.backupapps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.squareup.otto.Subscribe;
import pt.aptoide.backupapps.download.event.BusProvider;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 20-08-2013
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 */
public class FragmentContainer extends SherlockFragment {

  private FragmentBackedup fragmentBackup;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(
        savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
    setHasOptionsMenu(true);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    BusProvider.getInstance()
        .register(this);
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    BusProvider.getInstance()
        .unregister(this);
  }

  @Subscribe public void onLoginEvent(LoginEvent event) {
    Log.d("TAG", "onLoginEvent");

    fragmentBackup = new FragmentBackedup();

    Fragment fragmentLogin = getChildFragmentManager().findFragmentByTag("loginFragment");
    getChildFragmentManager().beginTransaction()
        .replace(((ViewGroup) fragmentLogin.getView()
            .getParent()).getId(), fragmentBackup, "backupFragment")
        .replace(((ViewGroup) fragmentLogin.getView()
            .getParent()).getId(), fragmentBackup, "backupFragment")
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .commit();

    BusProvider.getInstance()
        .post(new BackedUpRefreshEvent(MainActivity.currentSort));
    BusProvider.getInstance()
        .post(new ParseServerEvent("login"));
    //getSherlockActivity().invalidateOptionsMenu();
  }

  @Subscribe public void onLogoutEvent(LogoutEvent event) {
    Log.d("TAG", "onLogoutEvent");

    if (event.isFromAccountManager()) {
      Toast.makeText(getSherlockActivity(),
          getString(R.string.logout_toast_message_logged_out_from_aptoide), Toast.LENGTH_SHORT)
          .show();
    }

    Fragment loginFragment = getFragmentManager().findFragmentByTag("loginFragment");
    if (loginFragment == null) {
      loginFragment = new FragmentLogin();
    }
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_container_A, loginFragment, "loginFragment")
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .commitAllowingStateLoss();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.login_frag_container,
        null);    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Subscribe public void onShowBackedUpApps(ShowBackedUpApps event) {
    fragmentBackup = new FragmentBackedup();
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_container_A, fragmentBackup, "backup")
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .commitAllowingStateLoss();
    //getSherlockActivity().invalidateOptionsMenu();
  }

  @Subscribe public void onShowLoginScreen(ShowLoginScreen event) {
    Fragment loginFragment = new FragmentLogin();
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_container_A, loginFragment, "loginFragment")
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .commitAllowingStateLoss();
  }
}

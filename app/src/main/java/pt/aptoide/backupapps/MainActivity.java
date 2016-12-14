package pt.aptoide.backupapps;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.squareup.otto.Subscribe;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.download.event.AskRepoLoginDataEvent;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.download.event.UploadStatusEvent;
import pt.aptoide.backupapps.model.InstalledApk;
import pt.aptoide.backupapps.model.Server;
import pt.aptoide.backupapps.util.Constants;

public class MainActivity extends BaseSherlockFragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static EnumSortBy currentSort;
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    static private Database database = Database.getInstance();
    SharedPreferences sPref;
    private boolean showSystemApps = false;
    private UiLifecycleHelper uiLifecycleHelper;
    private GoogleApiClient googleApiClient;
    private ProgressDialog mConnectionProgressDialog;
    private int RESOLVE_GOOGLE_PLUS_LOGIN_REQUEST_CODE = 9000;
    private MainService service;
    private ArrayList<InstalledApk> installedApks = new ArrayList<InstalledApk>();
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MainActivity.this.service = ((MainService.LocalBinder) service).getService();

            final SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

            if (sPref.contains(Constants.LOGIN_USER_LOGIN)) {

                BusProvider.getInstance().post(new ShowBackedUpApps());
                parseServer();

            } else {

                if (AccountManager.get(MainActivity.this).getAccountsByType("cm.aptoide.pt").length != 0) {

                    Log.d("TAG", "User logged in from account manager");
                    String token = null;
                    String repo = null;
                    Account account = null;
                    String passHash = null;
                    String loginName = null;
                    String loginType = null;
                    try {


                        account = AccountManager.get(MainActivity.this).getAccountsByType("cm.aptoide.pt")[0];

                        String URL = "content://cm.aptoide.pt.StubProvider";
                        Uri token_uri = Uri.parse(URL + "/token");
                        Uri repo_uri = Uri.parse(URL + "/repo");
                        Uri passHash_uri = Uri.parse(URL + "/passHash");
                        Uri loginType_uri = Uri.parse(URL + "/loginType");
                        Uri loginName_uri = Uri.parse(URL + "/loginName");

                        Cursor c1 = getContentResolver().query(token_uri, null, null, null, null);
                        Cursor c2 = getContentResolver().query(repo_uri, null, null, null, null);
                        Cursor c3 = getContentResolver().query(passHash_uri, null, null, null, null);

                        Cursor c4 = getContentResolver().query(loginName_uri, null, null, null, null);
                        Cursor c5 = getContentResolver().query(loginType_uri, null, null, null, null);

                        c1.moveToFirst();
                        c2.moveToFirst();
                        c3.moveToFirst();
                        c4.moveToFirst();
                        c5.moveToFirst();

                        token = c1.getString(c1.getColumnIndex("userToken"));
                        repo = c2.getString(c2.getColumnIndex("userRepo"));
                        passHash = c3.getString(c3.getColumnIndex("userPass"));
                        loginName = c4.getString(c4.getColumnIndex("loginName"));
                        loginType = c5.getString(c5.getColumnIndex("loginType"));

                        c1.close();
                        c2.close();
                        c3.close();
                        c4.close();
                        c5.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        BusProvider.getInstance().post(new ShowLoginScreen());
                    }
                    if (passHash != null && passHash.length() > 0 && loginType != null) {

//                        sPref.edit().putString(Constants.LOGIN_USER_LOGIN, account.name)
//                                .putString(Constants.LOGIN_USER_TOKEN, token)
//                                .putString(Constants.LOGIN_USER_DEFAULT_REPO, repo)
//                                .putBoolean(Constants.LOGGED_FROM_ACCOUNT_MANAGER, true)
//                                .commit();
//
//                        Database.getInstance().setLoggedIn(true);
//                        Database.getInstance().removeAllData();
//                        Database.getInstance().insertServer("http://" + repo + Constants.DOMAIN_APTOIDE_STORE, account.name, "");
//
                        Toast.makeText(MainActivity.this, getString(R.string.logged_in_from_aptoide) + " " + account.name + "!", Toast.LENGTH_SHORT).show();
//
                        BusProvider.getInstance().post(new ShowLoginScreen());
//                        parseServer();

                        Login login;

                        if (loginType.equals("aptoide")) {
                            login = new Login(account.name, passHash, false);
                        } else if (loginType.equals("facebook")) {
                            login = new Login(account.name, passHash, loginType);
                            login.setLoginMode(Login.LoginMode.FACEBOOK_OAUTH);
                        } else {
                            login = new Login(account.name, passHash, loginType, loginName);
                            login.setLoginMode(Login.LoginMode.GOOGLE_OAUTH);
                        }

                        login.setFromUpdate(true);
                        login.setFromAccountManager(true);
                        new CheckUserCredentials(MainActivity.this).execute(login);
                    } else {
                        BusProvider.getInstance().post(new ShowLoginScreen());
                    }

                } else {
                    BusProvider.getInstance().post(new ShowLoginScreen());
                }

            }

            new Thread(new Runnable() {

                @Override
                public void run() {

                    installedApks = InstalledAppsHelper.getInstalledApps(getApplicationContext(), true);
                    fillAndInsertInstalledApps();
                    BusProvider.getInstance().post(new RefreshInstalledAppsEvent(getInstalledApks(currentSort), showSystemApps));

                }

            }).start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    /**
     * Called when the activity is first created.
     */

    private ViewPager viewPager;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, SessionState state, Exception exception) {
            if (state.isOpened()) {
                Log.d("Facebook token", session.getAccessToken());
                Request meRequest = Request.newMeRequest(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {

                        String username = user.getProperty("email") == null ? "" : user.getProperty("email").toString();

                        if (TextUtils.isEmpty(username)) {

                            session.close();

                            MainActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(MainActivity.this, R.string.facebook_error, Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            Login login = new Login(username, session.getAccessToken(), "facebook");
                            new CheckUserCredentials(MainActivity.this).execute(login);
                        }
                        session.closeAndClearTokenInformation();
                    }
                });
                meRequest.executeAsync();
            }
        }
    };

    @Subscribe
    public void onUploadStatusEvent(UploadStatusEvent event) {
        Log.d("TAG", "receiving Upload event");
        parseServer();
    }

    @Subscribe
    public void onParseServerEvent(ParseServerEvent event) {
        Log.d("TAG", "receiving Refresh event");
        parseServer();
    }

    @Subscribe
    public void onAskRepoLoginDataEvent(AskRepoLoginDataEvent event) {
        Log.d("TAG", "getting repo login data event");

        if (event.isAfterWrongCredentials()) {
            Toast.makeText(this, getString(R.string.incorrect_private_store_credentials), Toast.LENGTH_SHORT).show();
        }

        RepoLoginDialog dialog = new RepoLoginDialog(sPref.getString(Constants.LOGIN_USER_DEFAULT_REPO, ""));
        dialog.show(getSupportFragmentManager(), "repoLoginDialog");
    }

    public void fillAndInsertInstalledApps() {
        ArrayList<String> dbInstalledApks = database.getInstalledApps();
        try {
            database.beginTransaction();
            for (InstalledApk installedApk : installedApks) {

                if (!dbInstalledApks.contains(installedApk.getPackageName())) {
                    database.insertApk(installedApk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    @Subscribe
    public void onLogoutEvent(LogoutEvent event) {
        viewPager.setCurrentItem(1, true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<InstalledApk> installedApks = getInstalledApks(currentSort);

                BusProvider.getInstance().post(new RefreshInstalledAppsEvent(installedApks, showSystemApps));

                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().remove(Constants.LOGIN_USER_LOGIN).remove(Constants.LOGIN_USER_PASSWORD).remove(Constants
                        .LOGIN_USER_DEFAULT_REPO).remove(Constants.LOGIN_USER_TOKEN).remove(Constants.LOGIN_USER_AVATAR).remove(Constants.LOGIN_FROM_SIGNUP).remove(Constants
                        .LOGGED_FROM_ACCOUNT_MANAGER).commit();

            }
        }).start();


    }

    @Subscribe
    public void onLoginMoveEvent(LoginMoveEvent evensetServerHasht) {
        viewPager.setCurrentItem(1, true);
    }

    private void parseServer() {

        executor.submit(new Runnable() {
            public int retryCount;
            public boolean error = false;
            int maxTries = 3;

            @Override
            public void run() {
                Server server;

                while (retryCount < maxTries) {
                    try {
                        BusProvider.getInstance().post(new StartParseEvent());
                        server = Database.getInstance().getServer();
                        MainActivity.this.service.parse(server);
                        break;
                    } catch (IOException e) {
                        retryCount++;
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        retryCount++;
                        e.printStackTrace();
                    }
                }
                if (retryCount == maxTries) {
                    error = true;
                }
                Log.d("TAG", "Parse ended");
                BusProvider.getInstance().post(new StopParseEvent(error));

                BusProvider.getInstance().post(new BackedUpRefreshEvent(currentSort));
                ArrayList<InstalledApk> installedApks = getInstalledApks(currentSort);
                BusProvider.getInstance().post(new RefreshInstalledAppsEvent(installedApks, showSystemApps));
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Build.VERSION.SDK_INT >= 8) {
            uiLifecycleHelper.onSaveInstanceState(outState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.actionbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.system).setChecked(showSystemApps);

        switch (currentSort) {
            case NAME:
                menu.findItem(R.id.name).setChecked(true);
                break;
            case DATE:
                menu.findItem(R.id.date).setChecked(true);
                break;
            case SIZE:
                menu.findItem(R.id.size).setChecked(true);
                break;
            case STATE:
                menu.findItem(R.id.status).setChecked(true);
                break;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 8) {
            uiLifecycleHelper.onPause();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 8) {
            uiLifecycleHelper.onResume();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);

        if (Build.VERSION.SDK_INT >= 8) {
            uiLifecycleHelper.onDestroy();
        }
        unbindService(conn);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aptoide);
        BusProvider.getInstance().register(this);
        new AutoUpdate(this).execute();
        int currentSortOrdinal = PreferenceManager.getDefaultSharedPreferences(this).getInt("sort", 0);

        currentSort = EnumSortBy.values()[currentSortOrdinal];

        sPref = PreferenceManager.getDefaultSharedPreferences(this);

        viewPager = (ViewPager) findViewById(R.id.list_pager);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);

        TabsAdapter tabHost = new TabsAdapter(this, viewPager);
        tabHost.addFragment(new FragmentInstalled());
        tabHost.addFragment(new FragmentContainer());

        indicator.setViewPager(viewPager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                BusProvider.getInstance().post(new ActionModeFinishEvent());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bindService(new Intent(this, MainService.class), conn, BIND_AUTO_CREATE);

        if (Build.VERSION.SDK_INT >= 8) {

            uiLifecycleHelper = new UiLifecycleHelper(this, callback);
            uiLifecycleHelper.onCreate(savedInstanceState);

            final Plus.PlusOptions options = new Plus.PlusOptions.Builder()
                    .addActivityTypes("http://schemas.google.com/AddActivity",
                            "http://schemas.google.com/BuyActivity")
                    .build();
            googleApiClient = new GoogleApiClient
                    .Builder(this, this, this)
                    .addApi(Plus.API, options)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .build();
            mConnectionProgressDialog = new ProgressDialog(this);
            mConnectionProgressDialog.setMessage("Signing in...");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Build.VERSION.SDK_INT >= 8) {
            uiLifecycleHelper.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RESOLVE_GOOGLE_PLUS_LOGIN_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    connectPlusClient();
                }
            }
        }
    }

    @Subscribe
    public void onPackageChangedEvent(PackagesChangedEvent event) {

        if (MainActivity.this.service != null) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    installedApks = InstalledAppsHelper.getInstalledApps(getApplicationContext(), true);
                    fillAndInsertInstalledApps();
                    BusProvider.getInstance().post(new RefreshInstalledAppsEvent(getInstalledApks(currentSort), showSystemApps));
                }

            }).start();
        }

    }

    public ArrayList<InstalledApk> getInstalledApks(EnumSortBy sort) {

        Comparator<InstalledApk> comparator;

        database.setBackedUpApks(installedApks);

        switch (sort) {
            case NAME:
                comparator = new Comparator<InstalledApk>() {
                    private final Collator sCollator = Collator.getInstance();

                    @Override
                    public int compare(InstalledApk lhs, InstalledApk rhs) {
                        return sCollator.compare(lhs.getName(), rhs.getName());
                    }
                };
                break;

            case DATE:
                comparator = new Comparator<InstalledApk>() {
                    @Override
                    public int compare(InstalledApk lhs, InstalledApk rhs) {

                        long difference = rhs.getDate() - lhs.getDate();

                        if (difference > 0) {
                            return 1;
                        } else if (difference == 0) {
                            return 0;
                        } else {
                            return -1;
                        }

                    }
                };
                break;
            case SIZE:
                comparator = new Comparator<InstalledApk>() {
                    @Override
                    public int compare(InstalledApk lhs, InstalledApk rhs) {
                        return (int) (rhs.getSize() - lhs.getSize());
                    }
                };
                break;
            case STATE:

                comparator = new Comparator<InstalledApk>() {

                    @Override
                    public int compare(InstalledApk lhs, InstalledApk rhs) {


                        if (lhs.isBackedUp() && !rhs.isBackedUp()) {
                            return 1;
                        } else if (!lhs.isBackedUp() && rhs.isBackedUp()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                };
                break;
            default:
                comparator = new Comparator<InstalledApk>() {
                    private final Collator sCollator = Collator.getInstance();

                    @Override
                    public int compare(InstalledApk lhs, InstalledApk rhs) {
                        return sCollator.compare(lhs.getName(), rhs.getName());
                    }
                };
        }


//        ArrayList<Integer> backedUpHashs = database.setBackedUpApks();
//
//        for(InstalledApk apk: installedApks){
//
//            if(backedUpHashs.contains((apk.getPackageName() + apk.getVersionCode()).hashCode())){
//                apk.setBackedUp(true);
//            }else{
//                apk.setBackedUp(false);
//            }
//        }

        Collections.sort(installedApks, comparator);

        return installedApks;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_manager:
                startActivity(new Intent(this, Manager.class));
                break;
            case R.id.menu_settings:
                startActivity(new Intent(this, Settings.class));
                break;
            case R.id.date:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                currentSort = EnumSortBy.DATE;
                BusProvider.getInstance().post(new BackedUpRefreshEvent(currentSort));
                BusProvider.getInstance().post(new RefreshInstalledAppsEvent(getInstalledApks(currentSort), showSystemApps));
                break;
            case R.id.name:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                currentSort = EnumSortBy.NAME;
                BusProvider.getInstance().post(new BackedUpRefreshEvent(currentSort));
                BusProvider.getInstance().post(new RefreshInstalledAppsEvent(getInstalledApks(currentSort), showSystemApps));

                break;
            case R.id.size:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                currentSort = EnumSortBy.SIZE;
                BusProvider.getInstance().post(new BackedUpRefreshEvent(currentSort));
                BusProvider.getInstance().post(new RefreshInstalledAppsEvent(getInstalledApks(currentSort), showSystemApps));
                break;
            case R.id.system:

                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);

                showSystemApps = item.isChecked();
                BusProvider.getInstance().post(new RefreshInstalledAppsEvent(getInstalledApks(currentSort), showSystemApps));
                break;
            case R.id.status:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);

                currentSort = EnumSortBy.STATE;
                BusProvider.getInstance().post(new RefreshInstalledAppsEvent(getInstalledApks(currentSort), showSystemApps));
                break;

        }

        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("sort", currentSort.ordinal()).commit();

        return super.onMenuItemSelected(featureId, item);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("AptoideBackupApps", "Connected!");

        AsyncTask task = new AsyncTask() {

            @Override
            protected Object doInBackground(Object... params) {
                String token = null;

                try {
                    token = GoogleAuthUtil.getToken(MainActivity.this, Plus.AccountApi.getAccountName(googleApiClient), "oauth2:server:client_id:" + BuildConfig.GOOGLE_OAUTH_SERVER_ID + ":api_scope:" + Scopes.PLUS_LOGIN);

                } catch (GooglePlayServicesAvailabilityException e) {
                    Dialog alert = GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), MainActivity.this, RESOLVE_GOOGLE_PLUS_LOGIN_REQUEST_CODE);
                } catch (UserRecoverableAuthException e) {
                    startActivityForResult(e.getIntent(), RESOLVE_GOOGLE_PLUS_LOGIN_REQUEST_CODE);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }

                return token;
            }

            @Override
            protected void onPostExecute(Object token) {
                super.onPostExecute(token);

                if (token != null && Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
                    Login login = new Login(Plus.AccountApi.getAccountName(googleApiClient), token.toString(), "google", Plus.PeopleApi.getCurrentPerson(googleApiClient).getDisplayName());
                    new CheckUserCredentials(MainActivity.this).execute(login);

                    Plus.AccountApi.clearDefaultAccount(googleApiClient);
                    googleApiClient.disconnect();
                    googleApiClient.connect();
                } else {
                    googleApiClient.disconnect();
                }
            }
        };
        task.execute((Void) null);
        mConnectionProgressDialog.dismiss();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("AptoideBackupApps", "Suspended!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("AptoideBackupApps", "Connection Failed!");

        if (mConnectionProgressDialog.isShowing()) {
            mConnectionProgressDialog.dismiss();
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RESOLVE_GOOGLE_PLUS_LOGIN_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    connectPlusClient();
                }
            } else {
                Toast.makeText(this, getString(R.string.google_not_available), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void connectPlusClient() {
        if (!googleApiClient.isConnected()
                && !googleApiClient.isConnecting()) {
            mConnectionProgressDialog.show();
            googleApiClient.connect();
        }
    }

    public static class TabsAdapter extends FragmentStatePagerAdapter
            implements IconPagerAdapter {
        private final Context mContext;
        private final ViewPager mViewPager;
        private final ArrayList<Fragment> mTabs = new ArrayList<Fragment>();
        private String[] CONTENT;
        private int[] ICONS = new int[]{R.drawable.ic_upload, R.drawable.ic_download};

        public TabsAdapter(FragmentActivity activity, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mViewPager = pager;
            mViewPager.setAdapter(this);
            CONTENT = new String[]{activity.getString(R.string.left_tab), activity.getString(R.string.right_tab)};
        }

        public void addFragment(Fragment fragment) {
            mTabs.add(fragment);
            notifyDataSetChanged();
        }

        @Override
        public int getIconResId(int index) {
            return ICONS[index];
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment.instantiate(mContext, ((Object) mTabs.get(position)).getClass().getName());
        }
    }


}

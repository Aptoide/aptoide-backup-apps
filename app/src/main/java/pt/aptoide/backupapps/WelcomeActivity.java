package pt.aptoide.backupapps;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelcomeActivity extends Activity {
  private static final int MULTIPLE_PERMISSIONS_REQUEST = 12;
  private Button button;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    button = (Button) findViewById(R.id.start);

    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          requestRuntimePermissions();
        } else {
          launchMainActivity();
        }
      }
    });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    button = null;
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case MULTIPLE_PERMISSIONS_REQUEST: {
        Map<String, Integer> perms = new HashMap<>();
        perms.put(Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
        for (int i = 0; i < permissions.length; i++)
          perms.put(permissions[i], grantResults[i]);
        if (perms.get(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED
            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
          // All Permissions Granted
          launchMainActivity();
        } else {
          // Permission Denied
          Logger.d(this.getClass()
              .getName(), "Some Permission is Denied");
        }
      }
      break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) private void requestRuntimePermissions() {
    List<String> permissionsNeeded = new ArrayList<>();

    final List<String> permissionsList = new ArrayList<>();

    if (addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)) {
      permissionsNeeded.add("STORAGE");
    }

    if (addPermission(permissionsList, Manifest.permission.GET_ACCOUNTS)) {
      permissionsNeeded.add("ACCOUNT");
    }

    if (permissionsList.size() > 0) {
      if (permissionsNeeded.size() > 0) {
        ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[0]),
            MULTIPLE_PERMISSIONS_REQUEST);
      }
    } else {
      launchMainActivity();
    }
  }

  private boolean addPermission(List<String> permissionsList, String permission) {
    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
      permissionsList.add(permission);
      return true;
    }
    return false;
  }

  private void launchMainActivity() {
    startActivity(new Intent(this, MainActivity.class));
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    finish();
  }
}

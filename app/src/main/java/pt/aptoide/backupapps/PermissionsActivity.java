package pt.aptoide.backupapps;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionsActivity extends Activity {
  private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_permissions);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      requestRuntimePermissions();
    } else {
      launchMainActivity();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          launchMainActivity();
        } else {
          requestRuntimePermissions();
        }
      }
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) private void requestRuntimePermissions() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
          MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    } else {
      launchMainActivity();
    }
  }

  private void launchMainActivity() {
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }
}

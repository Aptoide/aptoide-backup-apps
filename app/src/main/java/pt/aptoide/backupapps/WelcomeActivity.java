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

public class WelcomeActivity extends Activity {
  private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
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
      case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          launchMainActivity();
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
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    finish();
  }
}

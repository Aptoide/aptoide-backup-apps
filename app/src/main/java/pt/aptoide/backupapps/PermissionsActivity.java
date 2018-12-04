package pt.aptoide.backupapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PermissionsActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    startActivity(new Intent(this, MainActivity.class));
  }
}

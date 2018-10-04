package pt.aptoide.backupapps.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 29-07-2013
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class Schema extends SQLiteOpenHelper {

  public static final String PACKAGE_NAME = "package_name";
  public static final String _ID = "_id";
  public static final String NAME = "name";
  public static final String VERSION_NAME = "version_name";
  public static final String VERSION_CODE = "version_code";
  public static final String ICON_PATH = "icon_path";
  public static final String APK_PATH = "apk_path";
  public static final String SIZE = "size";
  public static final String DATE = "date";
  public static final String APPSCOUNT = "appscount";
  public static final String HASH = "hash";
  public static final String PATH = "path";
  public static final String URL = "url";
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String MD5 = "md5";
  public static final String TABLE_REPO_APK = "repo_apk";
  public static final String TABLE_REPO = "repo";
  public static final String TABLE_INSTALLED_APK = "installed_apk";

  private static final String createTable_RepoApk = "CREATE TABLE "
      + TABLE_REPO_APK
      + " ( "
      + "    "
      + _ID
      + "          INTEGER PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT,"
      + "    "
      + PACKAGE_NAME
      + " VARCHAR DEFAULT ( '' ) "
      + "                         COLLATE 'NOCASE',"
      + "    "
      + NAME
      + "         VARCHAR DEFAULT ( '' ) "
      + "                         COLLATE 'NOCASE',"
      + "    "
      + VERSION_NAME
      + " VARCHAR DEFAULT ( '' ),"
      + "    "
      + VERSION_CODE
      + " INTEGER,"
      + "    "
      + ICON_PATH
      + "    VARCHAR DEFAULT ( '' ),"
      + "    "
      + PATH
      + "    VARCHAR DEFAULT ( '' ),"
      + "    "
      + MD5
      + "    VARCHAR DEFAULT ( '' ),"
      + "    "
      + SIZE
      + "         REAL    DEFAULT ( 0 ), "
      + "    "
      + DATE
      + "         DATE    "
      + ");";
  private static final String createTable_Repo = "CREATE TABLE "
      + TABLE_REPO
      + " ( "
      + "    "
      + _ID
      + "       INTEGER PRIMARY KEY,"
      + "    "
      + URL
      + "       VARCHAR NOT NULL,"
      + "    "
      + HASH
      + "      VARCHAR DEFAULT ( '' ),"
      + "    "
      + APK_PATH
      + "  VARCHAR DEFAULT ( '' ),"
      + "    "
      + ICON_PATH
      + " VARCHAR DEFAULT ( '' ),"
      + "    "
      + USERNAME
      + " VARCHAR DEFAULT ( '' ),"
      + "    "
      + PASSWORD
      + " VARCHAR DEFAULT ( '' ),"
      + "    "
      + APPSCOUNT
      + " INTEGER DEFAULT ( 0 ) "
      + ");";
  private static final String createTable_InstalledApk = "CREATE TABLE "
      + TABLE_INSTALLED_APK
      + " ( "
      + "    "
      + _ID
      + "          INTEGER PRIMARY KEY,"
      + "    "
      + PACKAGE_NAME
      + " VARCHAR UNIQUE ON CONFLICT REPLACE,"
      + "    "
      + NAME
      + "         VARCHAR DEFAULT ( '' ),"
      + "    "
      + VERSION_NAME
      + " VARCHAR DEFAULT ( '' ),"
      + "    "
      + VERSION_CODE
      + " INTEGER "
      + ");";

  private static final String createIndex_Installed = "CREATE INDEX idx_installed_apk ON "
      + TABLE_INSTALLED_APK
      + " ( "
      + "    "
      + _ID
      + ","
      + "    "
      + PACKAGE_NAME
      + ","
      + "    "
      + VERSION_CODE
      + " "
      + ");";

  private static final String createIndex_RepoApk = "CREATE INDEX idx_repo_apk ON "
      + TABLE_REPO_APK
      + " ( "
      + "    "
      + _ID
      + ","
      + "    "
      + PACKAGE_NAME
      + ","
      + "    "
      + VERSION_CODE
      + " "
      + ");";

  public Schema(Context context) {
    super(context, "backupapps.db", null, 1);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(createTable_InstalledApk);
    db.execSQL(createTable_RepoApk);
    db.execSQL(createTable_Repo);
    db.execSQL(createIndex_Installed);
    db.execSQL(createIndex_RepoApk);
    //db.execSQL(createTable_Login);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onCreate(db);
  }
}


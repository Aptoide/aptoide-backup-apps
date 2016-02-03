package pt.aptoide.backupapps.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import pt.aptoide.backupapps.BackupAppsApplication;
import pt.aptoide.backupapps.EnumSortBy;
import pt.aptoide.backupapps.InstalledAppsHelper;
import pt.aptoide.backupapps.Login;
import pt.aptoide.backupapps.model.Apk;
import pt.aptoide.backupapps.model.InstalledApk;
import pt.aptoide.backupapps.model.RepoApk;
import pt.aptoide.backupapps.model.Server;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 29-07-2013
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class Database {

    private SQLiteDatabase database;
    private SQLiteStatement insertRepoApkStatement;
    private SQLiteStatement insertInstalledApkStatement;
    private String iconsPath;
    private boolean loggedIn = true;

    private Database() {

        database = new Schema(BackupAppsApplication.getContext()).getWritableDatabase();


    }

    public void beginTransaction() {

        database.beginTransaction();

    }

    public void endTransaction(){

        database.setTransactionSuccessful();
        database.endTransaction();

    }

    public Cursor getAvailable(int order) {

        EnumSortBy sort = EnumSortBy.values()[order];


        String orderBy = "";

        switch (sort){
            case DATE:
                orderBy = "order by " + Schema.DATE + " desc";
                break;
            case NAME:
                orderBy = "order by " + Schema.NAME + " collate nocase";
                break;
            case SIZE:
                orderBy = "order by " + Schema.SIZE + " desc";
                break;
        }



        return database.rawQuery("select * from repo_apk " + orderBy, null);
    }

    public Server insertServer(String url, String username, String password) {
        Server server = new Server();
        server.setUrl(url);
        server.setLogin(new Login(username, password, false));
        ContentValues values = new ContentValues();
        values.put(Schema.URL, url);
        values.put(Schema.USERNAME, username);
        values.put(Schema.PASSWORD, password);
        long id = database.insert(Schema.TABLE_REPO, null, values);
        Log.d("Server", values.toString());
        server.setId(id);
        return server;
    }

    public void updateServerPassword(String url, String username, String password){

        ContentValues values = new ContentValues();

        values.put(Schema.USERNAME, username);
        values.put(Schema.PASSWORD, password);

        database.update(Schema.TABLE_REPO, values, Schema.URL + " = ?", new String[]{url});
    }

    public Server getServer(){

        Server server = null;

        Cursor c = database.query(Schema.TABLE_REPO, null, null, null, null, null, null, null);

        if(c.moveToFirst()){
            server = new Server();
            server.setId(c.getLong(c.getColumnIndex(Schema._ID)));
            server.setUrl(c.getString(c.getColumnIndex(Schema.URL)));
            server.setHash(c.getString(c.getColumnIndex(Schema.HASH)));
            server.setAppsCount(c.getInt(c.getColumnIndex(Schema.APPSCOUNT)));
            server.setApkPath(c.getString(c.getColumnIndex(Schema.APK_PATH)));
            server.setIconsPath(c.getString(c.getColumnIndex(Schema.ICON_PATH)));

            String username = c.getString(c.getColumnIndex(Schema.USERNAME));
            if(username != null){
                server.setLogin(new Login(c.getString(c.getColumnIndex(Schema.USERNAME)),c.getString(c.getColumnIndex(Schema.PASSWORD)), false));
            }


        }

        c.close();


        return server;
    }

    public void updateServer(Server server){

        ContentValues values = new ContentValues();
        values.put(Schema.APK_PATH, server.getApkPath());
        values.put(Schema.ICON_PATH, server.getIconsPath());
        values.put(Schema.APPSCOUNT, server.getAppsCount());
        database.update(Schema.TABLE_REPO, values, Schema._ID + "=?", new String[]{server.getId()+""});

    }

    public void setServerHash(Server server){
        ContentValues values = new ContentValues();
        values.put(Schema.HASH, server.getHash());
        database.update(Schema.TABLE_REPO, values, Schema._ID + "=?", new String[]{server.getId()+""});

    }

    public String getIconsPath() {

        String iconsPath = "";
        Cursor c = database.query(Schema.TABLE_REPO, new String[]{Schema.ICON_PATH}, null, null, null, null, null);

        if(c.moveToFirst()){
            iconsPath = c.getString(0);
        }
        c.close();

        return iconsPath;
    }

    public void removeAllData() {
        database.delete(Schema.TABLE_REPO,null, null);
        database.delete(Schema.TABLE_REPO_APK,null, null);
        //database.delete(Schema.TABLE_INSTALLED_APK,null, null);
    }

    public void removeRepoData() {
        database.delete(Schema.TABLE_REPO_APK,null, null);
    }

        public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public RepoApk getApk(long id) {
        RepoApk apk = new RepoApk();

        Cursor c = database.query(Schema.TABLE_REPO_APK, null, Schema._ID + "=?", new String[]{id +""}, null, null, null);

        if(c.moveToFirst()){
            apk.setId((int) id);
            apk.setName(c.getString(c.getColumnIndex(Schema.NAME)));
            apk.setPackageName(c.getString(c.getColumnIndex(Schema.PACKAGE_NAME)));
            apk.setPath(getServer().getApkPath() + c.getString(c.getColumnIndex(Schema.PATH)));
            apk.setIconPath(getServer().getIconsPath() + c.getString(c.getColumnIndex(Schema.ICON_PATH)));
            apk.setMd5Sum(c.getString(c.getColumnIndex(Schema.MD5)));
            apk.setVersionName((c.getString(c.getColumnIndex(Schema.VERSION_NAME))));
        }

        c.close();


        return apk;  //To change body of created methods use File | Settings | File Templates.
    }

    public void removeInstalledApk(String packageName) {

        database.delete(Schema.TABLE_INSTALLED_APK, Schema.PACKAGE_NAME + "=?", new String[]{packageName});

    }

    public void deleteApk(String packageName) {

        database.delete(Schema.TABLE_REPO_APK, Schema.PACKAGE_NAME + "=?", new String[]{packageName});

    }

    private static class SingletonHolder {

        public static final Database INSTANCE = new Database();

    }

    public static Database getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void prepareDatabase(){
        insertRepoApkStatement = database.compileStatement("INSERT INTO " + Schema.TABLE_REPO_APK  +
                " ( " + Schema.PACKAGE_NAME + " , " + Schema.NAME + " , "
                + Schema.VERSION_NAME + " , " + Schema.VERSION_CODE + " , "
                + Schema.ICON_PATH + " ," + Schema.SIZE + " , " + Schema.PATH + " , " +  Schema.MD5 + " , " +  Schema.DATE +   ")" +
                " values (?,?,?,?,?,?,?,?,?)");
    }

    public SQLiteStatement getInsertRepoApkStatement(){
        return insertRepoApkStatement;
    }

    public SQLiteStatement getInsertInstalledApkStatement(){

        if(insertInstalledApkStatement== null){
            insertInstalledApkStatement = database.compileStatement("INSERT INTO " + Schema.TABLE_INSTALLED_APK  +
                    " ( " + Schema.PACKAGE_NAME + " , " + Schema.NAME + " , "
                    + Schema.VERSION_NAME + " , " + Schema.VERSION_CODE + " ) "
                    +" values (?,?,?,?)");
        }

        return insertInstalledApkStatement;
    }


    public long insertApk(Apk apk) throws SAXException {
        long id = -1;
        if (isLoggedIn()) {
            id = apk.insert(this);
        }else {
            throw new SAXException("Is not logged in");
        }
        if (database.inTransaction() && database.yieldIfContendedSafely()) {
            Log.d("TAG", "Database yielded");
        }

        return id;
    }

    public long getRepoCount(){

        Cursor c = database.rawQuery("select count(*) from repo_apk", null);
        long i = 0;
        if(c.moveToFirst()){
            i = c.getLong(0);
        }
        c.close();

        return i;
    }

    public void setBackedUpApks(ArrayList<InstalledApk> installedApks){

        Cursor c = database.rawQuery("SELECT repo.package_name, repo." + Schema.VERSION_CODE + " FROM " + Schema.TABLE_REPO_APK + " as repo , " + Schema.TABLE_INSTALLED_APK + " as installed where repo.package_name=installed.package_name and repo." + Schema.VERSION_CODE+ "=installed." + Schema.VERSION_CODE, null);


        ArrayList<String> backedUpPackages = new ArrayList<String>();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext() ){

            backedUpPackages.add(c.getString(0));

        }

        for(InstalledApk apk: installedApks){

            if(backedUpPackages.contains(apk.getPackageName())){
                apk.setBackedUp(true);
            }else{
                apk.setBackedUp(false);
            }

        }

        Log.d("TAG", c.getCount()+"");
        c.close();
    }



    public ArrayList<String> getInstalledApps(){
        ArrayList<String> installedPackages = new ArrayList<String>(10);
        Cursor c = database.query(Schema.TABLE_INSTALLED_APK, new String[]{Schema.PACKAGE_NAME}, null, null,null,null,null);

       for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            installedPackages.add(c.getString(c.getColumnIndex(Schema.PACKAGE_NAME)));
        }
        c.close();

        return installedPackages;
    }








}

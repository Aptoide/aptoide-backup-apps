/*******************************************************************************
 * Copyright (c) 2012 rmateus.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package pt.aptoide.backupapps.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NetworkUtils {


	private static int TIME_OUT = 30000;


	public static BufferedInputStream getInputStream(String url, String username, String password, Context mctx) throws IOException{

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        connection.setConnectTimeout(TIME_OUT);
        connection.setReadTimeout(TIME_OUT);
        if(username != null){
            String userPassword = username + ":" + password;
            String encoding = Base64.encodeToString(userPassword.getBytes(), Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
        }
		connection.setRequestProperty("User-Agent", getUserAgentString(mctx));
        System.out.println("Using user-agent: " + (getUserAgentString(mctx)));
        Log.d("TAG", username + " " + password);
		BufferedInputStream bis = new BufferedInputStream(connection.getInputStream(), 8 * 1024);

//		if(ApplicationAptoide.DEBUG_MODE)
            Log.i("Aptoide-NetworkUtils", "Getting: "+url);

		return bis;

	}

	public static void setTimeout(int timeout){
		NetworkUtils.TIME_OUT=timeout;
	}

    public static int checkServerConnection (String string) throws IOException {

        Log.d("TAG", "checking " + string);

        return checkServerConnection(string, null, null);
    }

	public static int checkServerConnection(String string, String username, String password) throws IOException {
        HttpURLConnection client = (HttpURLConnection) new URL(string).openConnection();
        if (username != null && password != null) {
            String basicAuth = "Basic "
                    + new String(Base64.encode(
                    (username + ":" + password).getBytes(),
                    Base64.NO_WRAP));
            client.setRequestProperty("Authorization", basicAuth);
        }
        client.setConnectTimeout(TIME_OUT);
        client.setReadTimeout(TIME_OUT);

        if (client.getContentType().equals("application/xml")) {


            client.disconnect();

            return 0;
        } else {

            int responseCode = client.getResponseCode();
            client.disconnect();

            return responseCode;
        }

    }

	public static JSONObject getJsonObject(String url, Context mctx) throws IOException, JSONException{
		String line = null;
        InputStream is = getInputStream(url, null, null, mctx);
		BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null){
			sb.append(line + '\n');
		}

        is.close();

		return new JSONObject(sb.toString());

	}

    public static JSONObject post(String url_string, String encoded_data) throws IOException {
        URL url;
        StringBuilder sb = null;
        String data;


            url = new URL(url_string);
            HttpURLConnection connection = null;

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(encoded_data);
            wr.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            wr.close();
            br.close();


        JSONObject response = null;
        try {
            response = new JSONObject(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return response;
    }

	public static String  getUserAgentString(Context mctx){
		SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(mctx);
		String myid = sPref.getString("myId", "NoInfo");
		String myscr = sPref.getInt("scW", 0)+"x"+sPref.getInt("scH", 0);
        String verString = null;
        try {
            verString = mctx.getPackageManager().getPackageInfo(mctx.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String partnerid = "";

		return "aptoideAppsBackup-" + verString+";"+ Constants.TERMINAL_INFO+";"+myscr+";id:"+myid+";"+sPref.getString(Constants.LOGIN_USER_LOGIN, "")+";"+partnerid;
	}


	public static boolean isConnectionAvailable(Context context){
		ConnectivityManager connectivityState = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean connectionAvailable = false;
		try {
			connectionAvailable = connectivityState.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED;
			Log.d("ManagerDownloads", "isConnectionAvailable mobile: "+connectionAvailable);
		} catch (Exception e) { }
		try {
			connectionAvailable = connectionAvailable || connectivityState.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED;
			Log.d("ManagerDownloads", "isConnectionAvailable wifi: "+connectionAvailable);
		} catch (Exception e) { }
		try {
			connectionAvailable = connectionAvailable || connectivityState.getNetworkInfo(6).getState() == NetworkInfo.State.CONNECTED;
			Log.d("ManagerDownloads", "isConnectionAvailable wimax: "+connectionAvailable);
		} catch (Exception e) { }
		try {
			connectionAvailable = connectionAvailable || connectivityState.getNetworkInfo(9).getState() == NetworkInfo.State.CONNECTED;
			Log.d("ManagerDownloads", "isConnectionAvailable ethernet: "+connectionAvailable);
		} catch (Exception e) { }

		return connectionAvailable;
	}



    public long getLastModified(URL url) throws IOException {

        return url.openConnection().getLastModified();

    }


    public static InputStream getInputStream(String urlString, Context applicationContext) throws IOException {
        return getInputStream(urlString, null, null, applicationContext);
    }
}

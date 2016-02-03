package pt.aptoide.backupapps;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pt.aptoide.backupapps.download.DownloadInfo;
import pt.aptoide.backupapps.download.DownloadManager;
import pt.aptoide.backupapps.download.EnumRating;
import pt.aptoide.backupapps.download.state.EnumUploadFailReason;
import pt.aptoide.backupapps.download.state.UploadErrorState;
import pt.aptoide.backupapps.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 01-08-2013
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class SubmitForm extends BaseSherlockActivity {


    DownloadInfo info;
    EditText form_name;
    Spinner form_rating;
    Spinner form_category;
    EditText form_desc;
    EditText form_phone;
    EditText form_e_mail;
    EditText form_url;
    TextView nameLabel;
    TextView ratingLabel;
    TextView categoryLabel;
    TextView descriptionLabel;
    TextView mailLabel;
    TextView websiteLabel;
    AsyncTask<String, Void, ArrayList<Category>> asyncTask;

    public static String getJSON(String url, int timeout) {
        try {
            URL u = new URL(url);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {

        } catch (IOException ex) {

        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.submit, menu);

        return super.onCreateOptionsMenu(menu);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.submit:
                info.getUploadModel().getMetadata().setDescription(form_desc.getText().toString());
                info.getUploadModel().getMetadata().setApk_phone(form_phone.getText().toString());
                info.getUploadModel().getMetadata().setApk_email(form_e_mail.getText().toString());
                info.getUploadModel().getMetadata().setApk_website(form_url.getText().toString());

                info.getUploadModel().getMetadata().setCategory(form_category.getAdapter().getItemId(form_category.getSelectedItemPosition()));
                info.getUploadModel().getMetadata().setRating(EnumRating.values()[form_rating.getSelectedItemPosition()+1]);


                info.download();
                finish();
                break;
            case android.R.id.home:
            case R.id.abs__home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asyncTask.cancel(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_submit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        form_name = (EditText) findViewById(R.id.form_name);
        form_rating = (Spinner) findViewById(R.id.form_rating);
        form_category = (Spinner) findViewById(R.id.form_category);
        form_desc = (EditText) findViewById(R.id.form_desc);
        form_phone = (EditText) findViewById(R.id.form_phone);
        form_e_mail = (EditText) findViewById(R.id.form_e_mail);
        form_url = (EditText) findViewById(R.id.form_url);

        nameLabel = (TextView) findViewById(R.id.form_name_id);
        ratingLabel = (TextView) findViewById(R.id.form_rating_id);
        categoryLabel = (TextView) findViewById(R.id.form_category_id);
        descriptionLabel = (TextView) findViewById(R.id.form_desc_id);
        mailLabel = (TextView) findViewById(R.id.form_e_mail_id);
        websiteLabel = (TextView) findViewById(R.id.form_url_id);

        int id = getIntent().getIntExtra("id", 0);

        info = DownloadManager.INSTANCE.mNotOngoingList.get(id);
        form_name.setText(info.getApk().getName());
        if(info.getUploadModel().getMetadata().getDescription()!=null){
            form_desc.setText(info.getUploadModel().getMetadata().getDescription());
        }


        String repo = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.LOGIN_USER_DEFAULT_REPO, "");


        String rating = info.getUploadModel().getMetadata().getRating();

        if(rating!=null){
            int ordinal = Integer.parseInt(rating);
            Log.d("TAG Uploader", "Rating is " + ordinal);
            form_rating.setSelection(ordinal - 1);
        }

        long category = info.getUploadModel().getMetadata().getCategory();

        ArrayList<Category> categories = new ArrayList<Category>();
        BaseAdapter adapter = new CategoriesSpinnerAdapter(this,categories );
        form_category.setAdapter(adapter);

        asyncTask = new GetCategories(form_category, category, adapter, categories).execute();

        String phoneNumber = info.getUploadModel().getMetadata().getApk_phone();

        if(phoneNumber!=null){
            form_phone.setText(phoneNumber);
        }

        String email = info.getUploadModel().getMetadata().getApk_email();

        if(email != null){
            form_e_mail.setText(email);
        }

        String websiteUrl = info.getUploadModel().getMetadata().getApk_website();

        if(websiteUrl!=null){
            form_url.setText(websiteUrl);
        }

        ArrayList<EnumUploadFailReason> errorMessage = new ArrayList<EnumUploadFailReason>(((UploadErrorState) info.getStatusState()).getErrorMessage());
        while(!errorMessage.isEmpty()){
            EnumUploadFailReason reason = errorMessage.remove(0);
            switch (reason){
                case MISSING_APK_NAME:
                    nameLabel.setTextColor(Color.RED);
                    break;
                case MISSING_DESCRIPTION:
                    descriptionLabel.setTextColor(Color.RED);
                    break;
                case MISSING_RATING:
                    ratingLabel.setTextColor(Color.RED);
                    break;
                case MISSING_CATEGORY:
                    categoryLabel.setTextColor(Color.RED);
                    break;
                case BAD_EMAIL:
                    mailLabel.setTextColor(Color.RED);
                    break;
                case BAD_WEBSITE:
                    websiteLabel.setTextColor(Color.RED);
                    break;
            }

        }


//        findViewById(R.id.form_submit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                info.getUploadModel().getMetadata().setDescription(form_desc.getText().toString());
//                info.getUploadModel().getMetadata().setApk_phone(form_phone.getText().toString());
//                info.getUploadModel().getMetadata().setApk_email(form_e_mail.getText().toString());
//                info.getUploadModel().getMetadata().setApk_website(form_url.getText().toString());
//                info.download();
//                finish();
//
//            }
//        });
    }

    static class GetCategories extends AsyncTask<String, Void, ArrayList<Category>>{

        private final Spinner form_category;
        private final long category;
        private final ArrayList<Category> categoryArrayList;
        private BaseAdapter adapter;

        public GetCategories(Spinner form_category, long category, BaseAdapter adapter, ArrayList<Category> categoriesList) {
            this.form_category = form_category;
            this.category = category;
            this.adapter = adapter;
            this.categoryArrayList = categoriesList;
        }

        @Override
        protected ArrayList<Category> doInBackground(String... params) {

            ArrayList<Category> categoriesList = new ArrayList<Category>();

            try {

                String json = getJSON("http://webservices.aptoide.com/webservices/2/listCategories/json", 5000);

                JSONObject object = new JSONObject(json);

                JSONObject categories = object.getJSONObject("categories");

                JSONArray standardCategories = categories.getJSONArray("standard");

                for(int i = 0; i<standardCategories.length();i++){
                    long id = standardCategories.getJSONObject(i).getLong("id");
                    String name = standardCategories.getJSONObject(i).getString("name");
                    if(!standardCategories.getJSONObject(i).isNull("parent")){
                        categoriesList.add(new Category(id, name ));
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return categoriesList;
        }

        @Override
        protected void onPostExecute(ArrayList<Category> categories) {
            super.onPostExecute(categories);

            if(categories!=null){
                categoryArrayList.clear();
                categoryArrayList.addAll(categories);
                adapter.notifyDataSetChanged();
                form_category.setSelection(((CategoriesSpinnerAdapter) adapter).getPositionFromId(category));
            }
        }
    }

    private class CategoriesSpinnerAdapter extends ArrayAdapter<Category>{

        public CategoriesSpinnerAdapter(Context context, ArrayList<Category> objects) {
            super(context, android.R.layout.simple_spinner_item, android.R.id.text1, objects);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        public int getPositionFromId(long category) {

            for (int i = 0; i < getCount(); i++)
            {
                if (getItemId(i) == category)
                    return i;
            }

            return 0;
        }
    }
}

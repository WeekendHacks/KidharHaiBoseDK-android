package weekendhacks.com.kahahaibosedk;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyFirebaseIIDService";
    private ArrayList<User> mUser;
    private ListView mContactView;
    private ContactAdapter mContactAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRegistered = preferences.contains(getString(R.string.is_registered));
        if (!isRegistered) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.is_registered), true);
            editor.apply();
        }
        String phone_no = preferences.getString(getString(R.string.phone_no), null);
        if (phone_no != null) {
            final String my_phone_no = phone_no;
            setContentView(R.layout.activity_main);
            mContactView = (ListView) findViewById(R.id.contactList);

            mUser = new ArrayList<>();
            mContactAdapter = new ContactAdapter(this, R.layout.contact_item, mUser);
            mContactView.setAdapter(mContactAdapter);
            mContactView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    User user = mUser.get(position);
                    String url = "https://khb.herokuapp.com/request";
                    String urlParameters = "to=" + user.getPhone() + "&from=" + my_phone_no;
                    new SendToken().execute(url, urlParameters);
                }
            });

            String url = getString(R.string.app_server_url) + "users?phone=" + phone_no;
            GetContacts contactsTask = new GetContacts();
            contactsTask.execute(url);
            Log.d(TAG, "I have been called here");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class GetContacts extends AsyncTask<String,Void,Integer> {

        private final String LOG_TAG = GetContacts.class.getSimpleName();
        @Override
        protected Integer doInBackground(String ... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String contactsJsonStr = null;

            String baseUrl = params[0];

            try {
                URL url = new URL(baseUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null){
                    return 0;
                }

                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine())!=null){
                    buffer.append(line+"\n");
                }

                if (buffer.length() == 0) {
                    return  0;
                }
                contactsJsonStr = buffer.toString();
                //Populate list view
                populateListData(contactsJsonStr);
                Log.d(LOG_TAG,contactsJsonStr);
            }
            catch (IOException e){
                Log.e(LOG_TAG, "Error ", e);
                return 0;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error", e);
                        return 0;
                    }
                }

            }

            return 1;
        }

        @Override
        protected void onPostExecute(Integer result){
            if(result==1){
                mContactAdapter.setListData(mUser);
                Log.d(TAG,mUser.toString());
            }
        }

    }

    private void populateListData(String response){
        Log.d(TAG, "response is " + response);
        Type listType = new TypeToken<List<User>>(){}.getType();
        List<User> users = new Gson().fromJson(response, listType);
        for(int i = 0;i<users.size();i++){
            mUser.add(users.get(i));
            Log.d(TAG,users.get(i).getName());
        }
    }

}

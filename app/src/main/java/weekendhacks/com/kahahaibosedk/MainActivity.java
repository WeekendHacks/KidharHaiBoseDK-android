package weekendhacks.com.kahahaibosedk;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyFirebaseIIDService";
    private ArrayList<User> mUser;
    private ListView mContactView;
    private ContactAdapter mContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContactView = (ListView) findViewById(R.id.contactList);

        mUser = new ArrayList<>();
        mContactAdapter = new ContactAdapter(this,R.layout.contact_item, mUser);

        mContactView.setAdapter(mContactAdapter);
        givePermissions(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRegistered = preferences.contains(getString(R.string.is_registered));
        if(!isRegistered) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.is_registered), true);
            editor.apply();
        }
        GetContacts contactsTask = new GetContacts();
        contactsTask.execute();
        Log.d(TAG, "I have been called here");

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

    private boolean givePermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.READ_PHONE_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        1);
            }
        }
        return true;
    }

    public class GetContacts extends AsyncTask<Void,Void,Integer> {

        private final String LOG_TAG = GetContacts.class.getSimpleName();
        @Override
        protected Integer doInBackground(Void ... Params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String contactsJsonStr = null;

            Utility utility = new Utility();
            String phoneNumber = utility.getPhoneNumber(getApplicationContext());
            String baseUrl = getString(R.string.app_server_url)+"users?phone="+phoneNumber;

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

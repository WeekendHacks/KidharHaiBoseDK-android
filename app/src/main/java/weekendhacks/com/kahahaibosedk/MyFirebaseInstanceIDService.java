package weekendhacks.com.kahahaibosedk;

/**
 * Created by melvin on 8/13/16.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static String token;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);
    }

    public static String getToken() {
        return new String(token);
    }
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server
        Utility utility = new Utility();
        String phoneNumber = utility.getPhoneNumber(getApplicationContext());
        //new SendToken().execute(url1, url2, url3);

    }



}



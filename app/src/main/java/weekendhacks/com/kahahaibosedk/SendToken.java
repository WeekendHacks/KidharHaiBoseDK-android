package weekendhacks.com.kahahaibosedk;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by melvin on 8/14/16.
 */
public class SendToken extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {
                String url = params[0];
                String urlParameters = params[1];
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                //add reuqest header
                con.setRequestMethod("POST");
                // Send post request
                con.setDoOutput(true);
                con.connect();
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
                Log.d("Response Code is", Integer.toString(con.getResponseCode()));
            } catch (IOException e) {
                e.printStackTrace();
                //Log.d("Response Code is", "Error");
            }
            return null;
        }
}

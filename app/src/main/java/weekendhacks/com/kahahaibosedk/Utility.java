package weekendhacks.com.kahahaibosedk;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.widget.TextView;

/**
 * Created by nishantr on 8/13/16.
 */
public class Utility {
    String mPhoneNumber;
    public String getPhoneNumber(Context context, Activity activity) {
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

                TelephonyManager phoneManager = (TelephonyManager)
                        context.getSystemService(Context.TELEPHONY_SERVICE);
                mPhoneNumber = phoneManager.getLine1Number();
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return mPhoneNumber;
    }
}

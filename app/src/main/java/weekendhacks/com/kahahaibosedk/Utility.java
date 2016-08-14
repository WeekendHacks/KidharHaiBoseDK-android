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
    public String getPhoneNumber(Context context) {
        TelephonyManager phoneManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = phoneManager.getLine1Number();
        return mPhoneNumber;
    }
}

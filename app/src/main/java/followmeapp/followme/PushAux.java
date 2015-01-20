package followmeapp.followme;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by salih on 1/17/2015.
 */
public class PushAux extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /******************************* Connect To Parse ***********************************/
        Parse.initialize(this, "nKW5FeyMcsHSazS0HrN07sE2HWnzqZsMkX8smWDV", "wAq9RGWWLdHfbBJKwIKC1it88YKM8lM1S7y6czrG");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}

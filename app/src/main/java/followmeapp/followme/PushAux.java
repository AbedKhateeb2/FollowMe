package followmeapp.followme;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by salih on 1/17/2015.
 */
public class PushAux extends Application {


//    private static PushAux instance = new PushAux();
//
//    public PushAux() {
//        instance = this;
//    }
//
//    public static Context getContext() {
//        return instance;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        /******************************* Connect To Parse ***********************************/
        Parse.initialize(this, "nKW5FeyMcsHSazS0HrN07sE2HWnzqZsMkX8smWDV", "wAq9RGWWLdHfbBJKwIKC1it88YKM8lM1S7y6czrG");
        Log.d("PARSE", "Called");
//        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        PushService.setDefaultPushCallback(this,MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground("aaaa", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

    }
}

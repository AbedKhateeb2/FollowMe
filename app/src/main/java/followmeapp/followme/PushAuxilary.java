package followmeapp.followme;

import android.app.Application;

import com.parse.ParseInstallation;

/**
 * Created by salih on 1/17/2015.
 */
public class PushAuxilary extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}

package followmeapp.followme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.parse.ParseBroadcastReceiver;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by salih on 1/17/2015.
 */
public class RouteReceive extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushOpen(Context context, Intent intent) {

    }

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
           return null;
    }
}

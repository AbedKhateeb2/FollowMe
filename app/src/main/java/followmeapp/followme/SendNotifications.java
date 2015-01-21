package followmeapp.followme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by salih on 1/17/2015.
 */
public class SendNotifications extends AsyncTask<Void,Void,Void> {
    private ProgressDialog dialog;
    private Context context;

    SendNotifications(Context ctx){
        context = ctx;
    }
    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context,"","Sending Route...");
//        MainActivity.showProgressDialog();
    }

    @Override
    protected Void doInBackground(Void... params) {
            // count the checked friends and prepare the list of fbIds that we want to send
            // the route to them

            for(FriendView frv : Database.friendsList ){
                if(frv.checked){
                    frv.checked = false;// return to unchecked
                    Database.sendTo.add(frv.fbId);
                    Log.d("PUSH","SendToId"+frv.fbId);
                }
            }
            Log.d("PUSH","currenUserId"+Database.currentUserFbId);

            for(String recFbId : Database.sendTo){
                // Create our Installation query
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("fbUserId", recFbId);
                try {
                    JSONObject data = new JSONObject("{\"alert\": \"You've got a new Route from "+Database.currentUserName+"\",\"title\": \"Follow Me\",\"route_id\": \""+Database.sendRouteId+"\"}");
//                    Send push notification to query
//                    ParsePush push = new ParsePush();
//                    push.setData(data);
//                   push.sendInBackground();
                    SystemClock.sleep(500);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
//        MainActivity.closeProgressDialog();
        Log.d("PUSH", "Finished push");
    }
}

package followmeapp.followme;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

/**
 * Created by salih on 1/17/2015.
 */
public class SendNotifications extends AsyncTask<Database.OnSendData,Void,Void> {
    @Override
    protected Void doInBackground(Database.OnSendData... params) {

        for(String recFbId : params[0].receivers){
            // Create our Installation query
            ParseQuery pushQuery = ParseInstallation.getQuery();
            pushQuery.whereEqualTo("fbUserId", recFbId);

            // Send push notification to query
            ParsePush push = new ParsePush();
            push.setQuery(pushQuery); // Set our Installation query
            push.setMessage("You've got one new Route from");
            push.sendInBackground();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("PUSH", "Finished push");
    }
}

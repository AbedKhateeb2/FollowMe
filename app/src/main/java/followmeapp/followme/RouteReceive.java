package followmeapp.followme;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseBroadcastReceiver;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by salih on 1/17/2015.
 */
public class RouteReceive extends ParsePushBroadcastReceiver {
//    @Override
//    protected Notification getNotification(Context context, Intent intent) {
//        return super.getNotification(context, intent);
//    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        try {
            String jsonStr = intent.getExtras().getString("com.parse.Data");
            if(jsonStr == null || jsonStr == ""){
                Log.d("PUSH","nulll");
            }
            JSONObject JObj = new JSONObject(jsonStr);
            if(JObj == null ){
                Log.d("PUSH","jjjjj");
            }
            Log.d("PUSH","----->"+jsonStr);

            Database.receivedRouteId = JObj.getString("route_id");
            Log.d("PUSH","----->>>>>"+Database.receivedRouteId);
            ReceiveRouteDialog rDialog = new ReceiveRouteDialog();

             rDialog.show(MainActivity.fragmentManager,"Route Receive");

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    protected void onPushOpen(Context context, Intent intent) {
//        try {
//            String jsonStr = intent.getExtras().getString("com.parse.Data");
//            if(jsonStr == null || jsonStr == ""){
//                Log.d("PUSH","nulll");
//            }
//            JSONObject JObj = new JSONObject(jsonStr);
//            if(JObj == null ){
//                Log.d("PUSH","jjjjj");
//            }
//            Log.d("PUSH","----->"+jsonStr);
//
//            Database.receivedRouteId = JObj.getString("route_id");
//            Log.d("PUSH","----->>>>>"+Database.receivedRouteId);
//            ReceiveRouteDialog rDialog = new ReceiveRouteDialog();
//
//            rDialog.show(MainActivity.fragmentManager,"Route Receive");
//
//        }catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
}

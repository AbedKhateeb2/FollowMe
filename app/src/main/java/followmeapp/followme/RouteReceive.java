package followmeapp.followme;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
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
//
//    @Override
//    protected int getSmallIconId(Context context, Intent intent) {
//        return R.drawable.ic_launcher;
//    }
//

    @Override
    protected void onPushOpen(Context context, Intent intent) {











        try{
            String jsonStr = intent.getExtras().getString("com.parse.Data");
            if(jsonStr == null || jsonStr == ""){
                Log.d("PUSH","nulll");
            }
            JSONObject JObj = new JSONObject(jsonStr);
            if(JObj == null ){
                Log.d("PUSH","jjjjj");
            }
            Log.d("PUSH","----->"+jsonStr);

            String routeID = JObj.getString("route_id");
            Log.d("PUSH","----->>>>>"+routeID);
            ReceiveRouteDialog rDialog = new ReceiveRouteDialog();

            rDialog.setRouteId(routeID);



            rDialog.show(MainActivity.fragmentManager,"New Route Received");



        }catch (JSONException e) {
            e.printStackTrace();
        }catch(Exception e){
             Log.e("Push", "Clicked");
             Intent i = new Intent(context, MainActivity.class);
             i.putExtras(intent.getExtras());
             i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(i);
        }

    }


    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return MainActivity.class;
    }
}

package followmeapp.followme;


import android.util.Log;

import com.facebook.widget.LoginButton;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by abed on 1/9/2015.
 */
public class Database {
    static public ReceiveRouteDialog rDialog;
    static public String receivedRouteId;
    static public String sendRouteId;
     static public String currentUserFbId;
    static public String currentUserName;
    static public String deviceId;
    static public boolean fromShare = false;
    static public List<String> sendTo = new LinkedList<>();
     static List<RouteView> routeList = new LinkedList<>();
     static List<FriendView> friendsList = new LinkedList<>();
    public static boolean loadRoute=false;

    static public RouteView getRoutes(int position){
         return routeList.get(position);
     }
    static public FriendView getFriends(int position){
        return friendsList.get(position);
    }
    static public int getRoutesSize(){
        return routeList.size();
    }
    static public void update_routes(){
        new ImportDataFromDatabase().execute();
    }
    static public int getFriendsSize(){
        return friendsList.size();
    }

    public static void addRoute(RouteView routeView) {
        ParseObject routeObject = new ParseObject("Route");
        routeObject.put("route_name",routeView.name);
        routeObject.put("route_area",routeView.area);
        routeObject.put("route_date",routeView.date);
        routeObject.put("route_duration",routeView.duration);
        routeObject.put("route_image_URL",routeView.imageURL);
        routeObject.put("route_length",routeView.length);
        routeObject.put("route_type",routeView.type);
        routeObject.put("route_owner", ParseUser.getCurrentUser().getObjectId());
        routeObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    update_routes();
                }
            }
        });
    }
    public static void addFriend(FriendView fr){ friendsList.add(fr);}

    static class OnSendData{
        String senderDevice;
        String senderFbId;
        List<String> receivers;

        OnSendData(String devId,String senderId,List<String> list){
            senderDevice = devId;
            senderFbId = senderId;
            receivers = list;
        }
    };
}



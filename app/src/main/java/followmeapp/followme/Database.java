package followmeapp.followme;


import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by abed on 1/9/2015.
 */
public class Database {
    static public String sendRouteId;
     static public String currentUserFbId;
    static public String currentUserName;
    static public String deviceId;
    static public boolean fromShare = false;
    static public List<String> sendTo = new LinkedList<>();
     static List<RouteView> routeList = new LinkedList<>();
     static List<FriendView> friendsList = new LinkedList<>();
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
        ParseUser user = ParseUser.getCurrentUser();
        routeList.clear();
        if (user == null){
            return;
        }
        ParseQuery routeQuery = ParseQuery.getQuery("Route");
        routeQuery.whereEqualTo("route_owner",user.getObjectId());
        List<ParseObject> routes=null;
        try {
            routes = routeQuery.find();
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        for (ParseObject route : routes){
            String name = route.getString("route_name");
            String area = route.getString("route_area");
            String date = route.getString("route_date");
            String duration = route.getString("route_duration");
            String imageURL = route.getString("route_image_URL");
            String length = route.getString("route_length");
            String type = route.getString("route_type");
            String objectID = route.getString("route_owner");
            routeList.add(routeList.size(),new RouteView(name,imageURL,area,length,duration,type,date,objectID));
        }
        if (RoutesFragment.routesListAdapter!=null){
            RoutesFragment.routesListAdapter.notifyDataSetChanged();
        }
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
        routeObject.saveInBackground();
//        JSONArray routes = ParseUser.getCurrentUser().getJSONArray("Routes");
//        if (routes ==null){
//            routes = new JSONArray();
//            routes.put(routeObject.getObjectId());
//        }else{
//            routes.put(routeObject.getObjectId());
//        }
//        Log.d("routeID",routeObject.getObjectId());
//        Log.d("jsonArray",routes.toString());
//        ParseUser.getCurrentUser().put("Routes",routes);
//        ParseUser.getCurrentUser().saveInBackground();
//        routeList.add(routeView);
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



package followmeapp.followme;


import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by abed on 1/9/2015.
 */
public class Database {
     static List<RouteView> routeList = new LinkedList<>();
     static public RouteView getRoutes(int position){
         return routeList.get(position);
     }
    static public int getRoutesSize(){
        return routeList.size();
    }
    static public void update_routes(){
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null){
            routeList = new LinkedList<>();
            return;
        }

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
        routeObject.put("route_name",routeView.name);
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
}

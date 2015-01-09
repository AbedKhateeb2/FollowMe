package followmeapp.followme;

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

    public static void addRoute(RouteView routeView) {
        routeList.add(routeView);
    }
}

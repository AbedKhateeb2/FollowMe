package followmeapp.followme;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by abed on 1/9/2015.
 */
public class Database {
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
    static public int getFriendsSize(){
        return friendsList.size();
    }
    public static void addRoute(RouteView routeView) {
        routeList.add(routeView);
    }
    public static void addFriend(FriendView fr){ friendsList.add(fr);}
}

package followmeapp.followme;

import android.os.AsyncTask;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by abed on 1/20/2015.
 */
public class ImportDataFromDatabase extends AsyncTask<Void, Void, Integer> {
    @Override
    protected void onPreExecute() {
        Database.routeList.clear();
        if (RoutesFragment.routesListAdapter != null) {
            RoutesFragment.routesListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected Integer doInBackground(Void... params) {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null){
            return 0;
        }
        ParseQuery routeQuery = ParseQuery.getQuery("Route");
        routeQuery.whereEqualTo("route_owner",user.getObjectId());
        List<ParseObject> routes=null;
        try {
            routes = routeQuery.find();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        for (ParseObject route : routes) {
            String name = route.getString("route_name");
            String area = route.getString("route_area");
            String date = route.getString("route_date");
            String duration = route.getString("route_duration");
            String imageURL = route.getString("route_image_URL");
            String length = route.getString("route_length");
            String type = route.getString("route_type");
            String objectId = route.getObjectId();//route.getString("objectId");
            String owner = route.getString("route_owner");
            //Log.d("OBJ","===>"+objectId);
            Database.routeList.add(Database.routeList.size(), new RouteView(name, imageURL, area, length, duration, type, date, owner, objectId));
        }
        return 1;
    }

    @Override
    protected void onPostExecute(Integer res) {
        if (res==1 && RoutesFragment.routesListAdapter != null) {
            RoutesFragment.routesListAdapter.notifyDataSetChanged();
        }
    }
}

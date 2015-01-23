package followmeapp.followme;

/**
 * Created by Mohammad on 1/15/2015.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ReceiveRouteDialog extends DialogFragment {
    private String routeId;

    public ReceiveRouteDialog() {
        this.routeId = "";
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.push_notification, container);
        final Button acceptButton = (Button) view.findViewById(R.id.btn_accept);
        Button rejectButton = (Button) view.findViewById(R.id.btn_reject);
        final EditText name = (EditText) view.findViewById(R.id.route_name_pushed);
        getDialog().setTitle(R.string.new_notification);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveRoute(routeId,name.getText().toString()).execute();
                ReceiveRouteDialog.this.dismiss();
            }

        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiveRouteDialog.this.dismiss();
            }
        });
        return view;
    }

    public void setRouteId(String id) {
        this.routeId=id;
    }

    public class  SaveRoute extends AsyncTask<Void,Void,RouteView>{
        private final String routeId;
        private final String routeName;

        public SaveRoute(String routeId,String routeName){
            this.routeId=routeId;
            this.routeName = routeName;
        }
        @Override
        protected RouteView doInBackground(Void... params) {
            ParseQuery routeQuery = ParseQuery.getQuery("Route");
            routeQuery.whereEqualTo("objectId",routeId);
            ParseObject senderRoute = null;
            try {
                List<ParseObject> sList = routeQuery.find();
                senderRoute = sList.get(0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String rName = senderRoute.getString("route_name");
            String area = senderRoute.getString("route_area");
            String date = senderRoute.getString("route_date");
            String duration = senderRoute.getString("route_duration");
            String imageURL = senderRoute.getString("route_image_URL");
            String length = senderRoute.getString("route_length");
            String type = senderRoute.getString("route_type");
            if (!routeName.isEmpty()){
                rName = routeName;
            }
            RouteView newRoute = new RouteView(rName,imageURL,area,length,duration,type,date, ParseUser.getCurrentUser().getObjectId(),null);
            return newRoute;
        }
        @Override
        public void onPostExecute(RouteView newRoute){
            Database.addRoute(newRoute);
        }
    }
};
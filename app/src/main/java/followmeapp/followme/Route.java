package followmeapp.followme;

import android.graphics.Color;
import android.location.Location;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by abed on 12/29/2014.
 */
public class Route {
    static PolylineOptions polylineOptions=null;
    static boolean routing_mode=false;
    private static LatLng lastPoint =null;
    private static double distance = 0.0;
    static void start_routing(){
        routing_mode = true;
        polylineOptions=new PolylineOptions();
        lastPoint = null;
        distance = 0.0;
        polylineOptions.color(Color.rgb(65,105,225)).width(10).visible(true);
    }

    static void stop_routing(){
        routing_mode = false;
    }
    static boolean is_started(){
        return routing_mode;
    }

    public static double add_point(LatLng currentLocation) {
        if (lastPoint!=null){
            float[] results = new float[1];
            Location.distanceBetween(lastPoint.latitude,lastPoint.longitude,currentLocation.latitude,currentLocation.longitude,results);
            distance += (results[0]/1000);
        }
        lastPoint = new LatLng(currentLocation.latitude,currentLocation.longitude);
        polylineOptions.add(currentLocation);
        return distance;
    }
}

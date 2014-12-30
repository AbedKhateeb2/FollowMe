package followmeapp.followme;

import android.graphics.Color;
import android.view.View;

import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by abed on 12/29/2014.
 */
public class Route {
    static PolylineOptions polylineOptions=null;
    static boolean routing_mode=false;
    static void start_routing(){
        routing_mode = true;
        polylineOptions=new PolylineOptions();
        polylineOptions.color(Color.CYAN).width(5).visible(true);
    }
    static void stop_routing(){
        routing_mode = false;
    }
    static boolean is_started(){
        return routing_mode;
    }

}

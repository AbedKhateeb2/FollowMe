package followmeapp.followme;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import followmeapp.followme.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
    private static final long ZOOM_IN_DEFAULT = 17;
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        googleMap.setMyLocationEnabled(true);
        // latitude and longitude
        double latitude = 32.77777;
        double longitude = 35.0215;
        Location location=googleMap.getMyLocation();
        LatLng myLocation=null;
        if (location!=null){
            myLocation = new LatLng(location.getLatitude(),location.getLongitude());
        }else{
            myLocation = new LatLng(latitude,longitude);
        }
        //googleMap.addPolyline(new PolylineOptions().color(Color.CYAN).add(myLocation).add(new LatLng(latitude+1,longitude+1)).width(5).visible(true));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myLocation).zoom(ZOOM_IN_DEFAULT).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM_IN_DEFAULT);
                googleMap.animateCamera(cameraUpdate);
                if (Route.is_started()&&location.getAccuracy()<=10){
                    PolylineOptions polylineOptions = Route.polylineOptions;
                    polylineOptions.add(currentLocation);
                    googleMap.clear();
                    googleMap.addPolyline(polylineOptions);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MINIMUM_TIME_BETWEEN_UPDATE,MINIMUM_DISTANCECHANGE_FOR_UPDATE,listener);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,MINIMUM_TIME_BETWEEN_UPDATE,MINIMUM_DISTANCECHANGE_FOR_UPDATE,listener);

        // Perform any camera updates here
        return v;
    }


}

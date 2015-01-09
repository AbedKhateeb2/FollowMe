package followmeapp.followme;


import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import java.text.DecimalFormat;

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
    RelativeLayout information;
    Animation slide_up;
    Animation slide_down;
    private static MapFragment instance = null;

    /**
     * *******************************************************************
     */
    public static MapFragment newInstance(String param1, String param2) {
        if (instance == null) {
            MapFragment fragment = new MapFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            instance = fragment;
            return fragment;
        }
        return instance;
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
        final TextView speedView = (TextView) v.findViewById(R.id.textView5);
        final TextView distanceView = (TextView) v.findViewById(R.id.textView6);
        final Chronometer mChronometer = (Chronometer) v.findViewById(R.id.chronometer);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        information = (RelativeLayout) v.findViewById(R.id.info_frame);
        slide_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        slide_down = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM_IN_DEFAULT);
                googleMap.animateCamera(cameraUpdate);
                if (Route.is_started() && location.getAccuracy() <= 10) {
                    double total_distance = Route.add_point(currentLocation);
                    googleMap.clear();
                    googleMap.addPolyline(Route.polylineOptions);
                    DecimalFormat df = new DecimalFormat("####0.00");
                    distanceView.setText(" "+ df.format(total_distance) +" km");
                    speedView.setText(" " + location.getSpeed() + " m/s");
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
        final ImageButton button = (ImageButton) v.findViewById(R.id.button);
        button.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Route.is_started()) {
                    Route.start_routing();
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mChronometer.start();
                    button.setImageResource(R.drawable.pause);
                    information.setVisibility(View.VISIBLE);
                    information.startAnimation(slide_down);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, listener);
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, listener);
                } else {
                    Route.stop_routing();
                    mChronometer.stop();
                    mChronometer.setText(R.string.empty);
                    button.setImageResource(R.drawable.play);
                    information.setVisibility(View.GONE);
                    information.startAnimation(slide_up);
                    speedView.setText(R.string.empty);
                    distanceView.setText(R.string.empty);
                    locationManager.removeUpdates(listener);
//                    locationManager.
                }
            }
        });


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
        Location location = googleMap.getMyLocation();
        LatLng myLocation = null;
        if (location != null) {
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            myLocation = new LatLng(latitude, longitude);
        }
        //googleMap.addPolyline(new PolylineOptions().color(Color.CYAN).add(myLocation).add(new LatLng(latitude+1,longitude+1)).width(5).visible(true));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myLocation).zoom(ZOOM_IN_DEFAULT).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        // Perform any camera updates here
        return v;
    }


}

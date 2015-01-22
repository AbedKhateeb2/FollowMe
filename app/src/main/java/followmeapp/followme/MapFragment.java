package followmeapp.followme;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseUser;

import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    static final String prefixImageURL =
            "https://maps.googleapis.com/maps/api/staticmap?size=400x200&path=weight:5%7Ccolor:blue%7Cenc:";
    private static TextView distanceView;
    public static LocationManager locationManager;
    MapView mMapView;
    private static DecimalFormat df = new DecimalFormat("####0.00");

    static GoogleMap googleMap;
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 10000; // in Milliseconds
    private static final long ZOOM_IN_DEFAULT = 17;
    static RelativeLayout information;
    Animation slide_up;
    static Animation slide_down;
    private static MapFragment instance = null;
    static Chronometer mChronometer;
    private static ImageButton button;
    public static LocationListener listener;
    static long timeElapsed = 0;
    private TextView speedView;
    static ImageButton clearButton;

    /**
     * *******************************************************************
     */
    private static String encodeNumber(int num) {
        int sgn_num = num << 1;
        if (num < 0) {
            sgn_num = ~(sgn_num);
        }
        num = sgn_num;
        StringBuffer encodeString = new StringBuffer();

        while (num >= 0x20) {
            encodeString.append((char) ((0x20 | (num & 0x1f)) + 63));
            num >>= 5;
        }

        encodeString.append((char) (num + 63));

        return encodeString.toString();

    }

     public static ArrayList<LatLng> decode(String encodedString) {
         ArrayList<LatLng> polyline = new ArrayList<LatLng>();
         int index = 0;
         int len = encodedString.length();
         int lat = 0, lng = 0;

         while (index < len) {
             int b, shift = 0, result = 0;
             do {
                 b = encodedString.charAt(index++) - 63;
                 result |= (b & 0x1f) << shift;
                 shift += 5;
             } while (b >= 0x20);
             int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
             lat += dlat;

             shift = 0;
             result = 0;
             do {
                 b = encodedString.charAt(index++) - 63;
                 result |= (b & 0x1f) << shift;
                 shift += 5;
             } while (b >= 0x20);
             int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
             lng += dlng;

             LatLng p = new LatLng(lat/100000.0, lng/100000.0);
             polyline.add(p);
         }

         return polyline;
     }

    public void dialogShow() {
        if (ParseUser.getCurrentUser() == null) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage("Please Login before");
            builder1.setCancelable(false);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return;
        }
        FragmentManager fm = getFragmentManager();
        AddRouteDialog dialog = new AddRouteDialog();
        Bundle args = new Bundle();
//        args.putString("title","Add Route");
        dialog.setArguments(args);
//        dialog.setRetainInstance(true);
        dialog.show(fm, "Adding Route");
    }

    public static void addRouteToDatabase(String name, String type, GetAddressTask getAddress, FragmentActivity activity) {
        List<LatLng> points = Route.polylineOptions.getPoints();
        if (points == null || points.size() == 0) {
            getAddress.cancel(true);
            Toast.makeText(activity.getApplicationContext(), "Please Record a Route First!", Toast.LENGTH_LONG).show();
            return;
        }
        String address = "Israel";

        String duration = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeElapsed),
                TimeUnit.MILLISECONDS.toMinutes(timeElapsed) % TimeUnit.HOURS.toMinutes(1));
        String length = df.format(Route.distance);
        String imageURL = prefixImageURL;
        if (points != null) {
            imageURL += encodeList(points);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm");
        String date = sdf.format(new Date());
        try {
            getAddress.get();
            if (!Route.address.startsWith("Exception")) {
                address = Route.address;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Database.addRoute(new RouteView(name, imageURL, address, length, duration, type, date, ParseUser.getCurrentUser().getObjectId(), null));
    }

    private static String encodeList(List<LatLng> points) {
        StringBuilder encodedPoints = new StringBuilder();
        int prev_lat = 0, prev_lng = 0;
        for (LatLng point : points) {
            int lat = Double.valueOf(point.latitude * 100000).intValue();
            int lng = Double.valueOf(point.longitude * 100000).intValue();
            encodedPoints.append(encodeNumber(lat - prev_lat));
            encodedPoints.append(encodeNumber(lng - prev_lng));
            prev_lat = lat;
            prev_lng = lng;
        }
        return encodedPoints.toString();
    }

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
        speedView = (TextView) v.findViewById(R.id.textView5);
        distanceView = (TextView) v.findViewById(R.id.textView6);
        mChronometer = (Chronometer) v.findViewById(R.id.chronometer);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        information = (RelativeLayout) v.findViewById(R.id.info_frame);
        slide_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        slide_down = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        clearButton = (ImageButton) v.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMap(v);
                MapFragment.clearButton.setVisibility(View.GONE);
            }
        });
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (Route.lastPoint != null&&location.getAccuracy() <= 8){
                    float[] results = new float[1];
                    Location.distanceBetween(Route.lastPoint.latitude,Route.lastPoint.longitude,currentLocation.latitude,currentLocation.longitude,results);
                    distanceView.setText(" " + df.format(results[0]/1000) + " km");
                    speedView.setText(" " + location.getSpeed() + " m/s");
                }
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM_IN_DEFAULT);
                googleMap.animateCamera(cameraUpdate);
                timeElapsed = SystemClock.elapsedRealtime() - mChronometer.getBase();
                Route.lastPoint = currentLocation;
                if (Route.is_started() && location.getAccuracy() <= 8) {
                    Route.add_point(currentLocation);
                    googleMap.clear();
                    googleMap.addPolyline(Route.polylineOptions);
//                    distanceView.setText(" " + df.format(total_distance) + " km");
//                    speedView.setText(" " + location.getSpeed() + " m/s");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, listener);
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, listener);
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
        button = (ImageButton) v.findViewById(R.id.button);
        button.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Route.is_started()) {
                    if (ParseUser.getCurrentUser() == null) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("Please Login before");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                        return;
                    }
                    Route.start_routing();
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    timeElapsed = 0;
                    mChronometer.start();
                    button.setImageResource(R.drawable.record_icon);
                    information.setVisibility(View.VISIBLE);
                    information.startAnimation(slide_down);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, listener);
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, listener);
                } else {
                    Route.stop_routing();
                    mChronometer.stop();
                    timeElapsed = SystemClock.elapsedRealtime() - mChronometer.getBase();
                    mChronometer.setText(R.string.empty);
                    dialogShow();
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
            Log.d("MapsInitializer", "failed");
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
//        googleMap.addPolyline(new PolylineOptions().color(Color.CYAN).add(myLocation).add(new LatLng(latitude+1,longitude+1)).width(5).visible(true));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myLocation).zoom(ZOOM_IN_DEFAULT).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        // Perform any camera updates here
        return v;
    }

    private void clearMap(View v) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Do You want to Clear the map");
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Route.clear();
                        mChronometer.stop();
                        googleMap.clear();
                        googleMap.addPolyline(Route.polylineOptions);
                        speedView.setText(R.string.empty);
                        distanceView.setText(R.string.empty);
                        mChronometer.setText(R.string.empty);
                        button.setVisibility(View.VISIBLE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("The Map had been Cleared");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (Route.is_started()){
                                            mChronometer.setBase(SystemClock.elapsedRealtime());
                                            mChronometer.start();
                                        }else{
                                            information.setVisibility(View.GONE);
                                        }
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("NO",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onPause(){
        super.onPause();
        timeElapsed = SystemClock.elapsedRealtime()-mChronometer.getBase();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    @Override
    public void onStart(){
        super.onStart();
        mChronometer.setBase(SystemClock.elapsedRealtime()-timeElapsed);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Database.loadRoute) {
            clearButton.setVisibility(View.VISIBLE);
            Database.loadRoute = false;
            button.setVisibility(View.GONE);
            information.setVisibility(View.VISIBLE);
            information.startAnimation(slide_down);
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            distanceView.setText(" " + 0 + " km");
            googleMap.addPolyline(Route.polylineOptions);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(Route.polylineOptions.getPoints().get(0)).zoom(ZOOM_IN_DEFAULT).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, listener);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, listener);
        }
        if (Route.is_started()){
            googleMap.addPolyline(Route.polylineOptions);
            List<LatLng> points = Route.polylineOptions.getPoints();
            if (points!=null && points.size()>0) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(points.get(points.size() - 1)).zoom(ZOOM_IN_DEFAULT).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
            MapFragment.clearButton.setVisibility(View.GONE);
            mChronometer.start();
            button.setImageResource(R.drawable.record_icon);
            information.setVisibility(View.VISIBLE);
            information.startAnimation(slide_down);
            distanceView.setText(" "+Route.distance+ " km");
            mChronometer.start();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, listener);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, listener);
        }
        
    }
}

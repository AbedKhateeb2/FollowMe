package followmeapp.followme;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by abed on 1/9/2015.
 */
public class GetAddressTask extends AsyncTask<LatLng, Void, String> {
    Context mContext;

    public GetAddressTask(Context context) {
        super();
        mContext = context;
    }

    /**
     * Get a Geocoder instance, get the latitude and longitude
     * look up the address, and return it
     *
     * @return A string containing the address of the current
     * location, or an empty string if no address can be found,
     * or an error message
     * @params params One or more Location objects
     */
    @Override
    protected String doInBackground(LatLng... params) {
        Geocoder geocoder = new Geocoder(mContext, Locale.US);
        // Get the current location from the input parameter list
        LatLng loc = params[0];
        // Create a list to contain the result address
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(loc.latitude,
                    loc.longitude, 1);
        } catch (IOException e1) {

            return ("IO Exception trying to get address");
        } catch (IllegalArgumentException e2) {
            // Error message to post in the log
            String errorString = "Illegal arguments " +
                    Double.toString(loc.latitude) +
                    " , " +
                    Double.toString(loc.longitude) +
                    " passed to address service";

            return errorString;
        }
        // If the reverse geocode returned an address
        if (addresses != null && addresses.size() > 0) {
            // Get the first address
            Address address = addresses.get(0);
            String addressText = String.format(
                    "%s, %s, %s", address.getMaxAddressLineIndex() > 0 ?
                            address.getAddressLine(0) : "",
                    address.getLocality(),
                    address.getCountryName());
            Route.address = address.getLocality()+address.getCountryName();
            // Return the text
            return addressText;
        } else {
            return "No address found";
        }
    }
}

package followmeapp.followme;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;


import android.widget.TextView;

/**
 * Created by salih on 12/30/2014.
 */
public class RoutesFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.helloworld, container,
                false);
        // Perform any camera updates here
        return v;
    }

}

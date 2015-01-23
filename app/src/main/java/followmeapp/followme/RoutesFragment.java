package followmeapp.followme;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ListView;


import com.facebook.widget.LoginButton;

import java.lang.ref.WeakReference;

/**
 * Created by salih on 12/30/2014.
 */
public class RoutesFragment extends Fragment {
    static RoutesListAdapter routesListAdapter= null;

    private ListView routesList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.routes_fragment, container,
                false);
        return v;
    }
    @Override
    public void onStart(){
        super.onStart();
        routesList = (ListView)getView().findViewById(R.id.routesListView);
        WeakReference<Context> Mcontext = new WeakReference<Context>(getActivity());
        FragmentManager fm = getFragmentManager();
        routesListAdapter = new RoutesListAdapter(Mcontext.get(),fm);
        routesList.setAdapter(routesListAdapter);
    }


}

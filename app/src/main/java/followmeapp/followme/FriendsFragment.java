package followmeapp.followme;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.widget.ListView;

import java.lang.ref.WeakReference;

/**
 * Created by salih on 1/5/2015.
 */
public class FriendsFragment extends Fragment {

    ListView friendsList;
    FriendsListAdapter friendsAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friendslist, container,false);
        return v;
    }


    @Override
    public void onStart(){
        super.onStart();
        friendsList = (ListView)getView().findViewById(R.id.friendsListView);
        WeakReference<Context> Mcontext = new WeakReference<Context>(getActivity());
        friendsAdapter = new FriendsListAdapter(Mcontext.get());
        friendsList.setAdapter(friendsAdapter);
    }

}

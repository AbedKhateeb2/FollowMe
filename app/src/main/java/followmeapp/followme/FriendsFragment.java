package followmeapp.followme;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.parse.entity.mime.MinimalField;

import java.lang.ref.WeakReference;

/**
 * Created by salih on 1/5/2015.
 */
public class FriendsFragment extends Fragment {

    ListView friendsList;
    FriendsListAdapter friendsAdapter;
    Button sendB;
    Button cancelB;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friendslist, container,false);
        sendB = (Button)v.findViewById(R.id.send_button);
        cancelB = (Button)v.findViewById(R.id.cancel_button);
        sendB.setVisibility(Button.INVISIBLE);
        cancelB.setVisibility(Button.INVISIBLE);
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRoutes();
            }
        });
        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // count the checked friends and prepare the list of fbIds that we want to send
                // the route to them
                for(FriendView frv : Database.friendsList ){
                    if(frv.checked){
                        Database.sendTo.add(frv.fbId);
                    }
                }
                goToRoutes();
                // call the send function on background and
                // send the routes along with pushNotifications to each user
            }
        });
        return v;
    }


    @Override
    public void onStart(){
        super.onStart();
        friendsList = (ListView)getView().findViewById(R.id.friendsListView);
        WeakReference<Context> Mcontext = new WeakReference<Context>(getActivity());
        friendsAdapter = new FriendsListAdapter(Mcontext.get());
        friendsList.setAdapter(friendsAdapter);
//        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if( Database.friendsList.get(position).checked == true ){
//                    Database.friendsList.get(position).checked = false;
//                }else{
//                    Database.friendsList.get(position).checked = true;
//                }
//                friendsAdapter.notifyDataSetChanged();
//            }
//        });
        if(Database.fromShare){
            sendB.setVisibility(Button.VISIBLE);
            cancelB.setVisibility(Button.VISIBLE);
        }
    }

    private void goToRoutes(){
        MainActivity.unLockNavigationDrawer();
        MainActivity.fragmentManager.beginTransaction()
                .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance(2))
                .commit();
    }

    public void notifyDataChanged(){
        if(friendsAdapter != null){
            friendsAdapter.notifyDataSetChanged();
        }
    }
}

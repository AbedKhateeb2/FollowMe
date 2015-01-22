package followmeapp.followme;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.entity.mime.MinimalField;

import org.json.JSONException;
import org.json.JSONObject;

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
                for(FriendView f : Database.friendsList){
                    f.checked = false;
                }
                goToRoutes();
            }
        });
        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send the routes in back ground

                 // new SendNotifications(getActivity()).execute();
                for(FriendView frv : Database.friendsList ){
                    if(frv.checked){
                        frv.checked = false;// return to unchecked
                        Database.sendTo.add(frv.fbId);
                        Log.d("PUSH","SendToId"+frv.fbId);
                    }
                }
                Log.d("PUSH","currenUserId"+Database.currentUserFbId + "+++++" + Database.sendTo.size());

                for(String recFbId : Database.sendTo){
                    // Create our Installation query
//                    ParseQuery pushQuery = ParseInstallation.getQuery();
//                    pushQuery.whereEqualTo("fbUserId", recFbId);
//                    try {
                       // Log.d("JSON","{\"alert\": \"You've got a new Route from "+Database.currentUserName+"\",\"title\": \"Follow Me\",\"route_id\": \""+Database.sendRouteId+"\"}");
                       // JSONObject data = new JSONObject("{\"alert\": \"You've got a new Route from "+Database.currentUserName+"\",\"title\": \"Follow Me\",\"route_id\": \""+Database.sendRouteId+"\"}");
//                        ParsePush push = new ParsePush();
//                        push.setData(data);
//                        push.setMessage("You've got a new Route from "+Database.currentUserName);
//                        push.sendInBackground();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    try {
                        JSONObject data = new JSONObject("{\"alert\": \"You've got a new Route from "+Database.currentUserName+"\",\"title\": \"Follow Me\",\"route_id\": \""+Database.sendRouteId+"\"}");
                        ParsePush push = new ParsePush();
                        push.setChannel("a"+recFbId);
                        //push.setMessage("You've got a new Route from "+Database.currentUserName);
                        push.setData(data);
                        push.sendInBackground();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
                Database.sendTo.clear();
                goToRoutes();
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

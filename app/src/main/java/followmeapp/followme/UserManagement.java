package followmeapp.followme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by salih on 1/18/2015.
 */
public class UserManagement {
    private boolean loggedIn;
    private Context ctx;
    private Activity logActv;
    UserManagement(Context context, Activity actv){
        ctx = context;
        logActv = actv;
        if(ParseUser.getCurrentUser() != null){
            logIn();
        }else{
            loggedIn = false;
            MainActivity.changeLogButtonMessage(true);
        }
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public void logOut(){
        MainActivity.changeLogButtonMessage(true);
        ParseUser.logOut();
        Session fbs = Session.getActiveSession();
        if (fbs == null){
            fbs = new Session(ctx);
            Session.setActiveSession(fbs);
        }
        fbs.closeAndClearTokenInformation();
        loggedIn = false;
        Database.friendsList.clear();
        Database.update_routes();
        Log.d("LOG", "Logged out finished !");
    }

    void logIn() {
        List<String> permissions = Arrays.asList("public_profile", "user_friends");

        ParseFacebookUtils.logIn(permissions,logActv, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException error) {
                Log.d("INFO", "in done");
                // When your user logs in, immediately get and store its Facebook ID
                if (user != null) {
                    // logged in successful
                    MainActivity.changeLogButtonMessage(false);
                    loggedIn = true;
                    getFacebookIdInBackground(ctx);
                }else{
                    Log.d("INFO", "No network");
                    // show Message to the user to connect to the internet
                    AlertDialog.Builder builder = new AlertDialog.Builder(logActv);
                    builder.setMessage("Please Connect Your Device To The Internet!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    private static void getFacebookIdInBackground(final Context applicationContext) {
        Request.executeMeRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                Log.d("INFO", "onComplete out start");
                if (user != null) {
                    Database.currentUserFbId = user.getId();
                    Database.currentUserName = user.getName();
                    ParseUser.getCurrentUser().put("fbId", user.getId());
                    ParseUser.getCurrentUser().put("name", user.getName());
                    ParseUser.getCurrentUser().saveInBackground();

                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("fbUserId", Database.currentUserFbId);
                    Database.deviceId = installation.getInstallationId();
                    Log.d("INFO", "device id ==" + Database.deviceId );
                    installation.saveInBackground();

                    Request.executeMyFriendsRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserListCallback() {

                        @Override
                        public void onCompleted(List<GraphUser> users, Response response) {
                            Database.update_routes();
                            Log.d("INFO", "onComplete in start");
                            if (users != null) {
                                List<String> friendsList = new ArrayList<String>();
                                for (GraphUser user : users) {
                                    Log.d("FB", user.getId());
                                    friendsList.add(user.getId());
                                }

                                // Construct a ParseUser query that will find friends whose
                                // facebook IDs are contained in the current user's friend list.
                                ParseQuery friendQuery = ParseQuery.getUserQuery();
                                friendQuery.whereContainedIn("fbId", friendsList);
                                // findObjects will return a list of ParseUsers that are friends with
                                // the current user
                                try {
                                    Log.d("INFO", "parse object");
                                    List<ParseObject> friendUsers = friendQuery.find();
                                    Log.d("INFO", "size : " + friendUsers.size());
                                    updateViewsWithProfileInfo(friendUsers, applicationContext);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private static void updateViewsWithProfileInfo(List<ParseObject> friendUsers,final Context ctx) {
        Log.d("INFO", "profileInfo size = " + friendUsers.size());
        for (ParseObject fr : friendUsers) {
            ParseUser currentUser = (ParseUser) fr;
            FriendView frView = null;
            Log.d("INFO", "beforeIf");
            if (currentUser.get("name") != null) {
                Log.d("INFO", "in Name");
                frView = new FriendView();
                frView.name = currentUser.get("name").toString();
            }
            if (currentUser.get("fbId") != null) {
                String facebookId = currentUser.get("fbId").toString();
                Log.d("INFO", "setting profile picture 1");
                Log.d("INFO", "facebook Id "+facebookId);
                frView.fbId =  facebookId;
            } else {
                Log.d("INFO", "setting profile picture 2");
            }
            // Set additional UI elements
            Database.addFriend(frView);
            Log.d("INFO", "finished");
        }
        MainActivity.notifyFriendsListChange();
    }
}

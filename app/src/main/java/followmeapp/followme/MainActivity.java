package followmeapp.followme;

import java.lang.CharSequence;import java.lang.Override;import java.lang.String;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfRenderer;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    static RoutesFragment userRoutes;
    static FriendsFragment userFriends;
    private boolean logedIn;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /******************************* Connect To Parse ***********************************/
        Parse.initialize(this, "nKW5FeyMcsHSazS0HrN07sE2HWnzqZsMkX8smWDV", "wAq9RGWWLdHfbBJKwIKC1it88YKM8lM1S7y6czrG");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        /***************************************************************************/
        userFriends = new FriendsFragment();
        userRoutes = new RoutesFragment();
        logedIn = false;
        /*************************** Navigation Drawable Code ************************************/
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        Database.update_routes();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if( position == 3){ // LOGIN
            if(logedIn == true){
                Toast.makeText(getApplicationContext(),"You Already Logged In",Toast.LENGTH_LONG).show();
                return ;
            }
            if(logedIn == false) {
                List<String> permissions = Arrays.asList("public_profile", "user_friends");
                ParseFacebookUtils.logIn(permissions,this, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException error) {
                        Log.d("INFO", "in done");
                        // When your user logs in, immediately get and store its Facebook ID
                        if (user != null) {
                            logedIn = true;
                             getFacebookIdInBackground(getApplicationContext());
                            Log.d("INFO", user.getUsername());
                                                /*Mohammad*/
                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("fbUserId",Database.currentUserFbId);
                            Database.deviceId = installation.getInstallationId();
                            installation.saveInBackground();
                        }
                    }
                });
            }
            return ;
        }
        if(position == 4){//Logout
            if(logedIn == true){
                ParseUser.logOut();
                Session fbs = Session.getActiveSession();
                if (fbs ==null){
                    fbs = new Session(getApplicationContext());
                    Session.setActiveSession(fbs);
                }
                fbs.closeAndClearTokenInformation();
                logedIn = false;
                Database.friendsList.clear();
                Database.update_routes();
                Toast.makeText(getApplicationContext(),"Logged In == true :D !",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"You Didn't LogIn",Toast.LENGTH_LONG).show();
            }
            return ;
        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }


    public void OnShare() {
        
    }

    private static void getFacebookIdInBackground(final Context applicationContext) {
        Request.executeMeRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                Log.d("INFO", "onComplete out start");
                if (user != null) {
                    Database.currentUserFbId = user.getId();
                    ParseUser.getCurrentUser().put("fbId", user.getId());
                    ParseUser.getCurrentUser().put("name", user.getName());
                    ParseUser.getCurrentUser().saveInBackground();
                    Request.executeMyFriendsRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserListCallback() {

                        @Override
                        public void onCompleted(List<GraphUser> users, Response response) {
                            Database.update_routes();
                            Log.d("INFO", "onComplete in start");
                            if (users != null) {
                                List<String> friendsList = new ArrayList<String>();
                                for (GraphUser user : users) {
                                    Log.d("FB",user.getId());
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
                                    Log.d("INFO", "size : "+friendUsers.size());
                                    updateViewsWithProfileInfo(friendUsers,applicationContext);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    private static void updateViewsWithProfileInfo(List<ParseObject> friendUsers,final Context ctx) {
        Log.d("INFO", "profileInfo size = " + friendUsers.size());
        for (ParseObject fr : friendUsers) {
            // ParseUser currentUser = ParseUser.getCurrentUser();
            ParseUser currentUser = (ParseUser) fr;
            FriendView frView = null;
            Log.d("INFO", "beforeIf");


            if (currentUser.get("name") != null) {
                Log.d("INFO", "in Name");
                frView = new FriendView(ctx);
                frView.name = currentUser.get("name").toString();
            }
            if (currentUser.get("fbId") != null) {
                String facebookId = currentUser.get("fbId").toString();
                Log.d("INFO", "setting profile picture 1");
                Log.d("INFO", "facebook Id "+facebookId);
                frView.fbId =  facebookId;
                //frView.friendPicView.setProfileId(facebookId);
               // frView.friendPicView.setCropped(true);
               // ImageView fbImage = ( ( ImageView)frView.friendPicView.getChildAt(0));
               // frView.bitMapPic  = ( (BitmapDrawable) fbImage.getDrawable()).getBitmap();


//                frView.friendPicView.setDrawingCacheEnabled(true);
//                frView.friendPicView.setProfileId(facebookId);
//                frView.bitMapPic = frView.friendPicView.getDrawingCache();
            } else {
                // Show the default, blank user profile picture
                frView.friendPicView.setProfileId(null);
                Log.d("INFO", "setting profile picture 2");
            }
            // Set additional UI elements
            // ...
            Database.addFriend(frView);


            Log.d("INFO", "finished");
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);

            switch(sectionNumber){
                case 1:
                    return MapFragment.newInstance(null,null);
                case 2:
                    return userRoutes;
                case 3:
                    return userFriends;
            }

            return null;
       //     return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}

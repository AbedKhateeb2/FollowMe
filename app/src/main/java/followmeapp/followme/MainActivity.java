package followmeapp.followme;

import java.lang.CharSequence;import java.lang.Override;import java.lang.String;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    static RoutesFragment userRoutes;
    static FriendsFragment userFriends;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    static NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    static FragmentManager fragmentManager = null;
    UserManagement loginObj = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /***************************************************************************/
        userFriends = new FriendsFragment();
        userRoutes = new RoutesFragment();
        /*************************** Navigation Drawable Code ************************************/
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        Database.update_routes();
        /********************************************************************************/
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        /***************** Last to do ******************/
        loginObj = new UserManagement(getApplicationContext(),this);
        fragmentManager = getSupportFragmentManager();
//        Intent intent = getIntent();
//        if(intent == null){
//            Log.d("INTENT","nullllll");
//        }else{
////            Log.d("INTENT","noooooooo nulll"+nt.toString()+"-->"+nt.getCategories().size());
//            if(intent.getCategories() == null){//from Push
//                try{
//                    String jsonStr = intent.getExtras().getString("com.parse.Data");
//                    if(jsonStr == null || jsonStr == ""){
//                        Log.d("PUSH","nulll");
//                    }
//                    JSONObject JObj = new JSONObject(jsonStr);
//                    if(JObj == null ){
//                        Log.d("PUSH","jjjjj");
//                    }
//                    Log.d("PUSH","----->"+jsonStr);
//
//                    String routeID = JObj.getString("route_id");
//                    Log.d("PUSH","----->>>>>"+routeID);
//                    ReceiveRouteDialog rDialog = new ReceiveRouteDialog();
//                    rDialog.setRouteId(routeID);
//                    rDialog.show(MainActivity.fragmentManager,"New Route Received");
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    static void lockNavigationDrawer(){
        mNavigationDrawerFragment.setMenuVisibility(false);
        mNavigationDrawerFragment.lockDrawer();
    }

    static void unLockNavigationDrawer(){
        mNavigationDrawerFragment.setMenuVisibility(true);
        mNavigationDrawerFragment.unLockDrawer();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if( position == 3){ // LOGIN/PUT
            if(loginObj.isLoggedIn()){//user wants to log out
                Log.d("LOG", "log out start");
                loginObj.logOut();
                Log.d("LOG", "Log out finished !");
                return ;
            }
            if(!loginObj.isLoggedIn()) {//user wants to log in
                Log.d("LOG", "log in start");
                loginObj.logIn();
                return ;
            }
            return ;
        }
        // update the main content by replacing fragments
        Database.fromShare = false;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    static public void changeLogButtonMessage(boolean to){
        mNavigationDrawerFragment.changeLogInOutText(to);
    }

    static public void notifyFriendsListChange(){
        userFriends.notifyDataChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
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
            if (MapFragment.locationManager != null) {
                MapFragment.locationManager.removeUpdates(MapFragment.listener);
            }
            if (MapFragment.mChronometer!=null ){
                MapFragment.mChronometer.stop();
            }
            switch(sectionNumber){
                case 1:
                    MapFragment res = MapFragment.newInstance(null,null);
                    return res;
                case 2:
                    return userRoutes;
                case 3:
                    return userFriends;
            }

            return null;
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("INTENT","herere");
        Intent intent = getIntent();
        if(intent == null){
            Log.d("INTENT","nullllll");
        }else{
//            Log.d("INTENT","noooooooo nulll"+nt.toString()+"-->"+nt.getCategories().size());
            if( intent.getExtras() != null){//from Push
                try{
                    Bundle ext = intent.getExtras();
                    String jsonStr = ext.getString("com.parse.Data");
                    if(jsonStr == null || jsonStr == ""){
                        Log.d("PUSH","nulll");
                        return ;
                    }
                    JSONObject JObj = new JSONObject(jsonStr);
                    if(JObj == null ){
                        Log.d("PUSH","jjjjj");
                    }
                    Log.d("PUSH","----->"+jsonStr);

                    String routeID = JObj.getString("route_id");
                    Log.d("PUSH","----->>>>>"+routeID);
                    ReceiveRouteDialog rDialog = new ReceiveRouteDialog();
                    rDialog.setRouteId(routeID);
                    rDialog.show(MainActivity.fragmentManager,"New Route Received");
                    getIntent().removeExtra("com.parse.Data");
//                    ext.remove("com.parse.Data");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}



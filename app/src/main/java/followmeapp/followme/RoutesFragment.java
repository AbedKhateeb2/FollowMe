package followmeapp.followme;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;


import android.widget.TextView;

import com.facebook.Session;
import com.facebook.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by salih on 12/30/2014.
 */
public class RoutesFragment extends Fragment {
    LoginButton authButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.helloworld, container,
                false);


        LoginButton authButton = (LoginButton) v.findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));
        authButton.setFragment(this);

//        PackageInfo info;
//        try {
//            info = getActivity().getPackageManager().getPackageInfo("followmeapp.followme", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                TextView t = (TextView)v.findViewById(R.id.textView);
//                t.setText(something);
//                //String something = new String(Base64.encodeBytes(md.digest()));
//                Log.e("hash key", something);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("exception", e.toString());
//        }

        return v;
    }

}

package followmeapp.followme;

import android.content.Context;

import com.facebook.widget.ProfilePictureView;

/**
 * Created by salih on 1/13/2015.
 */
public class FriendView {
    public String name;
    public ProfilePictureView friendPicView;

    FriendView(Context ctx){
        friendPicView = new ProfilePictureView(ctx);

    }
}

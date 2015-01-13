package followmeapp.followme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

/**
 * Created by salih on 1/5/2015.
 */
public class FriendsListAdapter extends BaseAdapter {
    private Context ctx;
    FriendsListAdapter(Context c){
        ctx =  c;
    }

    @Override
    public int getCount(){
        return Database.getFriendsSize();
    }
    @Override
    public Object getItem(int position){
        return null;
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        FriendItemViewHolder fHolder;
        if( convertView == null ){
            // Creat new View
            LayoutInflater inflater = LayoutInflater.from(ctx);
            view = inflater.inflate(R.layout.friendsitem,null);

            //create view holder
            fHolder = new FriendItemViewHolder();
            fHolder.friendName = (TextView)view.findViewById(R.id.friend_name);
            fHolder.friendPic = (ProfilePictureView)view.findViewById(R.id.friend_picture);
           // fHolder.bitMapPic = (ImageView)view.findViewById(R.id.friend_image);

            view.setTag(fHolder);
        }else{
            //Cast convertView to View
            view = convertView;
            fHolder = (FriendItemViewHolder)view.getTag();
        }
        //assign values to the view
        fHolder.friendName.setText(Database.getFriends(position).name);
        //fHolder.bitMapPic.setImageBitmap(Database.getFriends(position).bitMapPic);
        //fHolder.friendPic = new ProfilePictureView(ctx);
        fHolder.friendPic.setProfileId(Database.getFriends(position).fbId);
        return view;
    }

}

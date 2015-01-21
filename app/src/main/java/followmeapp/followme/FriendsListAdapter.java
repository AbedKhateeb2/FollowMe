package followmeapp.followme;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        FriendItemViewHolder fHolder;
        if( convertView == null ){
            // Creat new View
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.friendsitem,null);
            //create view holder
            fHolder = new FriendItemViewHolder();
            fHolder.friendName = (TextView)convertView.findViewById(R.id.friend_name);
//            fHolder.friendPic = (ProfilePictureView)convertView.findViewById(R.id.friend_picture);
            fHolder.friendPic = (ImageView)convertView.findViewById(R.id.friend_picture);
            fHolder.check = (CheckBox)convertView.findViewById(R.id.check_box);
            convertView.setTag(fHolder);
        }else{
            //Cast convertView to View
//            view = convertView;
            fHolder = (FriendItemViewHolder)convertView.getTag();
        }

//        fHolder.check.setTag(position);
        //assign values to the view
        fHolder.friendName.setText(Database.getFriends(position).name);
//        fHolder.friendPic.setProfileId(Database.getFriends(position).fbId);
        //use picasso
        //https://graph.facebook.com/10153059665372608/picture
        Log.d("PIC","https://graph.facebook.com/"+Database.friendsList.get(position).fbId+"/picture?height=200&width=200");
        Picasso.with(ctx)
                .load("https://graph.facebook.com/"+Database.friendsList.get(position).fbId+"/picture?height=200&width=200")
                .transform(new CircleTransform())
                .placeholder(R.drawable.default_portrait)
                .error(R.drawable.default_portrait)
                .into(fHolder.friendPic);

        fHolder.check.setVisibility(CheckBox.INVISIBLE);
        if(Database.fromShare){
            fHolder.check.setVisibility(CheckBox.VISIBLE);
            fHolder.check.setChecked(Database.getFriends(position).checked);
        }
        fHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database.friendsList.get(position).checked = ((CheckBox)v).isChecked();
            }
        });
        return convertView;
    }
}



//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        View view = null;
//        FriendItemViewHolder fHolder;
//        if( convertView == null ){
//            // Creat new View
//            LayoutInflater inflater = LayoutInflater.from(ctx);
//            view = inflater.inflate(R.layout.friendsitem,null);
//            //create view holder
//            fHolder = new FriendItemViewHolder();
//            fHolder.friendName = (TextView)view.findViewById(R.id.friend_name);
//            fHolder.friendPic = (ProfilePictureView)view.findViewById(R.id.friend_picture);
//            fHolder.check = (CheckBox)view.findViewById(R.id.check_box);
//            fHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                    Log.d("CHECK","-->"+isChecked);
////                    Database.friendsList.get(position).checked = isChecked;
//                    int getPosition = (Integer)buttonView.getTag();
//                    Database.friendsList.get(position).checked = buttonView.isChecked();
//                }
//            });
//            view.setTag(fHolder);
//        }else{
//            //Cast convertView to View
//            view = convertView;
//            fHolder = (FriendItemViewHolder)view.getTag();
//        }
//        fHolder.check.setTag(position);
//        //assign values to the view
//        fHolder.friendName.setText(Database.getFriends(position).name);
//        fHolder.friendPic.setProfileId(Database.getFriends(position).fbId);
//        fHolder.check.setVisibility(CheckBox.INVISIBLE);
//        if(Database.fromShare){
//            fHolder.check.setVisibility(CheckBox.VISIBLE);
//            fHolder.check.setChecked(Database.getFriends(position).checked);
//        }
//        return view;
//    }
//}

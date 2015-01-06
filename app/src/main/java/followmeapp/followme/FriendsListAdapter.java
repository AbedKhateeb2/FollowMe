package followmeapp.followme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
        return 30;
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
            fHolder.friendName = (TextView)view.findViewById(R.id.friendName);

            view.setTag(fHolder);
        }else{
            //Cast convertView to View
            view = convertView;
            fHolder = (FriendItemViewHolder)view.getTag();
        }
        //assign values to the view
        fHolder.friendName.setText("Salih Boshnak");

        return view;
    }

}

package followmeapp.followme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by salih on 1/12/2015.
 */
public class NavigatorListAdapter extends BaseAdapter {
    private Context m_context;
    boolean toLogin = true;
    String[] options;
    int[] navIcons = {R.drawable.maps_icon,R.drawable.routes_icon,R.drawable.friends_icon,R.drawable.facebook_icon};
    NavigatorListAdapter(Context ctx){
        m_context = ctx;
        options = ctx.getResources().getStringArray(R.array.navigationOptions);
    }
    @Override
    public int getCount() {
        return navIcons.length;
    }

    @Override
    public Object getItem(int position) {
        return options[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        if(convertView == null){
            // creating for the first time
            LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.navigator_list_item,parent,false);
        }else{
            row = convertView;
        }

        TextView nTitle = (TextView)row.findViewById(R.id.navigator_text);
        ImageView nIcon = (ImageView)row.findViewById(R.id.navigator_icon);
        if( (position == 3) && !toLogin ){
            nTitle.setText(options[position+1]);
        }else{
            nTitle.setText(options[position]);
        }
        nIcon.setImageResource(navIcons[position]);
        return row;
    }
};
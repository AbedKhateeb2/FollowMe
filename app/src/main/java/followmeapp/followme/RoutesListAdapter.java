package followmeapp.followme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by abed on 1/9/2015.
 */

public class RoutesListAdapter extends BaseAdapter {
    private Context ctx;
    RoutesListAdapter(Context c){
        ctx =  c;
    }

    @Override
    public int getCount(){
        return Database.getRoutesSize();
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
        RoutesHolder holder;
        if( convertView == null ){
            // Creat new View
            LayoutInflater inflater = LayoutInflater.from(ctx);
            view = inflater.inflate(R.layout.routes_card_view,null);

            //create view holder
            holder = new RoutesHolder();
            holder.name = (TextView)view.findViewById(R.id.txtName);
            holder.area = (TextView)view.findViewById(R.id.txtArea);
            holder.type = (TextView)view.findViewById(R.id.txtType);
            holder.date = (TextView)view.findViewById(R.id.txtdate);
            holder.duration = (TextView)view.findViewById(R.id.txtDuration);
            holder.length = (TextView)view.findViewById(R.id.txtLength);
            holder.map = (ImageView)view.findViewById(R.id.mapImage);
            holder.shareButton = (TextView)view.findViewById(R.id.txtShare);
            view.setTag(holder);
        }else{
            //Cast convertView to View
            view = convertView;
            holder = (RoutesHolder)view.getTag();
        }

        final RouteView routeView = Database.getRoutes(position);
        holder.name.setText("Route Name : "+routeView.name);
        holder.area.setText("Area : "+routeView.area);
        holder.type.setText("Activity : "+routeView.type);
        holder.duration.setText("Duration : " + routeView.duration);
        holder.date.setText("Date : "+routeView.date);
        holder.length.setText( "Distance "+routeView.length+" KM");
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String routeID = routeView.objectID;

            }
        });
        Picasso.with(ctx)
                .load(routeView.imageURL)
                .placeholder(R.drawable.staticmap)
                .error(R.drawable.staticmap)
                .into(holder.map);
        return view;
    }
    public class RoutesHolder{
        public ImageView map;
        public TextView name;
        public TextView area;
        public TextView length;
        public TextView duration;
        public TextView type;
        public TextView date;
        public TextView shareButton;
    }

}

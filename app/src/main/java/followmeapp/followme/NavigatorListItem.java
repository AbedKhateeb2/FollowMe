package followmeapp.followme;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by salih on 1/9/2015.
 */
public class NavigatorListItem {
    TextView title;
    ImageView icon;
    public NavigatorListItem(String str,Context con,int imgRcsr){
        title = new TextView(con);
        title.setText(str);
        icon = new ImageView(con);
        icon.setImageResource(imgRcsr);
    }
}

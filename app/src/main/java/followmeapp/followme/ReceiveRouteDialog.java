package followmeapp.followme;

/**
 * Created by Mohammad on 1/15/2015.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ReceiveRouteDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.push_notification, container);
        final Button acceptButton = (Button) view.findViewById(R.id.btn_accept);
        Button rejectButton = (Button) view.findViewById(R.id.btn_reject);
        final EditText name = (EditText) view.findViewById(R.id.route_name_pushed);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.unLockNavigationDrawer();
                MainActivity.fragmentManager.beginTransaction()
                        .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance(1))
                        .commit();
            }
        });
        return view;
    }
};
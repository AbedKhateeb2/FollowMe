package followmeapp.followme;

/**
 * Created by abed on 1/12/2015.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddRouteDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final GetAddressTask getAddress = new GetAddressTask(getActivity());
        if (Route.lastPoint!=null){
            Route.address ="";
            getAddress.execute(Route.lastPoint);
        }
        View view = inflater.inflate(R.layout.route_dialog, container);
        final Button loginbutton = (Button) view.findViewById(R.id.btn_login);
        Button cancelbutton = (Button) view.findViewById(R.id.btn_cancel);
        final EditText name = (EditText) view.findViewById(R.id.route_name);
        final Spinner spinner = (Spinner) view.findViewById(R.id.activities_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.activities,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        getDialog().setTitle("Adding Route");
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("name route",name.getText().toString());
                Log.d("activity",spinner.getSelectedItem().toString());
                if (name.getText().toString().isEmpty()){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Please insert a route name");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    MapFragment.addRouteToDatabase(name.getText().toString(),spinner.getSelectedItem().toString(),getAddress,getActivity());
                    AddRouteDialog.this.dismiss();
                }
            }
        });
        cancelbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddRouteDialog.this.dismiss();
            }
        });
        return view;
    }

}


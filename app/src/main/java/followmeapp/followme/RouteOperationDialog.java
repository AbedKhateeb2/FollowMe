package followmeapp.followme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by abed on 1/22/2015.
 */
public class RouteOperationDialog extends DialogFragment {
    View v;
    public int position;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.Operations, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("whiche", "" + which);
                if (which == 2){
                    dismiss();
                }
                if (which ==0) {
                    v.callOnClick();
                    dismiss();
                }
                if (which==1) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Are you Sure to Delete the Route ?");
                    builder1.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    RouteView routeView = Database.getRoutes(position);
                                    String objectID = routeView.objectID;
                                    Database.routeList.remove(position);
                                    RoutesFragment.routesListAdapter.notifyDataSetChanged();
                                    ParseObject.createWithoutData("Route", objectID).deleteInBackground();
                                    dialog.cancel();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("The Route had been Deleted");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    dismiss();
                                }
                            });
                    builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dismiss();
                        }
                    });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

}

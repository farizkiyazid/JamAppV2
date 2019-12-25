package id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.R;

public class WorldClockFragment extends Fragment implements View.OnClickListener {

    private TextView mTextViewResult;
    private RequestQueue mQueue;
    private WifiManager wifiManager;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    TextView user_location;
    private FusedLocationProviderClient mFusedLocationClient;

    public WorldClockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment SecondFragment.
     */
    public static WorldClockFragment newInstance() {
        return new WorldClockFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Right", "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_world_clock, container, false);

        mTextViewResult = view.findViewById(R.id.text_view_result);
        Button buttonGetTimeLoc = view.findViewById(R.id.button_parse);
        user_location = view.findViewById(R.id.user_location);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        mQueue = Volley.newRequestQueue(this.getActivity());

        buttonGetTimeLoc .setOnClickListener(v -> {
            fetchLocationAndTime();
        });

        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Button wifiOn = view.findViewById(R.id.wifiOn);
        Button wifiOff = view.findViewById(R.id.wifiOff);
        wifiOn.setOnClickListener(this);
        wifiOff.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wifiOn:
                wifiManager.setWifiEnabled(true);
                break;
            case R.id.wifiOff:
                wifiManager.setWifiEnabled(false);
                break;
        }
    }

    private void fetchLocationAndTime() {

        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this.getActivity())
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this.getActivity(), location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Double latitude = location.getLatitude();
                            Double longitude = location.getLongitude();

                            user_location.setText("Latitude = "+latitude + "\nLongitude = " + longitude);
                            jsonParseDatetime();
                        }
                    });
        }

    }

    private void jsonParseDatetime() {
        String url = "http://worldtimeapi.org/api/timezone/Asia/Jakarta";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String abbreviation = response.getString("abbreviation");
                        String datetime = response.getString("datetime");
                        mTextViewResult.setText(abbreviation + ", " + datetime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> error.printStackTrace());

        mQueue.add(request);
    }
}

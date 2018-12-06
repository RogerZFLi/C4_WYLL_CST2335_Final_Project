package com.example.amusu.teamname_cst2335_final_project.rogerli.octranspo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.amusu.teamname_cst2335_final_project.R;

/**
 * the activity showing the trip info of a specific route
 */

public class OCTranspoTripDetails extends Activity {

    private final static String ACTIVITY_NAME = "OCTranspoTripDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo_route_fragment);

        Log.i(ACTIVITY_NAME, "In onCreate");
        Intent i = getIntent();
        OCTranspoRouteFragment octf = new OCTranspoRouteFragment();
        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putString("stopNo", i.getStringExtra("stopNo"));
        fragmentArgs.putString("routeNo", i.getStringExtra("chosenRoute"));
        fragmentArgs.putStringArrayList("tripList2", i.getStringArrayListExtra("tripList"));
        fragmentArgs.putBoolean("phone", true);
        octf.setArguments(fragmentArgs);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.route_fragment, octf);
        ft.commit();
    }
}

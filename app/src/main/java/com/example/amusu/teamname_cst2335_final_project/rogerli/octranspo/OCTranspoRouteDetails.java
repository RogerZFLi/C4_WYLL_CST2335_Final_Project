package com.example.amusu.teamname_cst2335_final_project.rogerli.octranspo;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.amusu.teamname_cst2335_final_project.CBCNewsReaderActivity;
import com.example.amusu.teamname_cst2335_final_project.FoodNutrition.FoodMainActivity;
import com.example.amusu.teamname_cst2335_final_project.MainActivity;
import com.example.amusu.teamname_cst2335_final_project.movie.MovieInformationActivity;
import com.example.amusu.teamname_cst2335_final_project.R;

/**
 * the activity to show all the routes at a specific stop
 */
public class OCTranspoRouteDetails extends AppCompatActivity {

    /**
     * the name of the activity
     */

    private final static String ACTIVITY_NAME = "OCTranspoRouteDetails";
    /**
     * the toolbar showing at the top of the layout
     */
    private Toolbar octranspoToolbar;
    /**
     * the dialog builder
     */
    private AlertDialog.Builder builder;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo_stop_fragment);
        octranspoToolbar = findViewById(R.id.octranspo_toolbar);
        setSupportActionBar(octranspoToolbar);
        builder = new AlertDialog.Builder(this);
        Log.i(ACTIVITY_NAME, "In onCreate");
        Intent i = getIntent();
        OCTranspoStopFragment octf = new OCTranspoStopFragment();
        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putString("stopNo", i.getStringExtra("chosenStop"));
        fragmentArgs.putStringArrayList("routeList2", i.getStringArrayListExtra("routeList"));
        fragmentArgs.putBoolean("phone", true);
        octf.setArguments(fragmentArgs);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.stop_fragment, octf);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.octranspo_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        builder =new AlertDialog.Builder(OCTranspoRouteDetails.this);
        switch (mi.getItemId()) {
            case R.id.action_cbc:
                Log.i("Toolbar", "going to cbc news app");
                startActivity(new Intent(OCTranspoRouteDetails.this, CBCNewsReaderActivity.class));
                return true;
            case R.id.action_food:
                Log.i("Toolbar", "going to food nutrition app");
                startActivity(new Intent(OCTranspoRouteDetails.this, FoodMainActivity.class));
                return true;
            case R.id.action_movie:
                Log.i("Toolbar", "going to movie app");

                startActivity(new Intent(OCTranspoRouteDetails.this, MovieInformationActivity.class));
                return true;
            case R.id.action_return:
                Log.i("Toolbar", "return to main window");
                startActivity(new Intent(OCTranspoRouteDetails.this, MainActivity.class));
                return true;
            case R.id.action_help:
                Log.i("Toolbar", "help message");
                builder.setTitle(getString(R.string.octranspo_info))
                        .setMessage(getString(R.string.octranspo_details)).setPositiveButton(R.string.octranspo_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}

                }).create().show();
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }

    }


}

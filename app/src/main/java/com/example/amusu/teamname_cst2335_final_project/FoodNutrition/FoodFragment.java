package com.example.amusu.teamname_cst2335_final_project.FoodNutrition;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amusu.teamname_cst2335_final_project.R;

import java.util.HashMap;
import java.util.List;

public class FoodFragment extends Fragment {
    private Context foodpage;
    private List<HashMap<String, String>> favorList;
    private ListView favorListView;
    private FavorAdapter favorAdapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        foodpage = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View favorFragment = inflater.inflate(R.layout.activity_food_fragment, null);
        favorListView = favorFragment.findViewById(R.id.favourite_list_view);
        return favorFragment;
    }


    class FavorAdapter extends ArrayAdapter{
            int resourceID;
        public FavorAdapter(Context context, int resource,  List objects) {
            super(context, resource, objects);
            resourceID = resource;
        }


        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {

            final HashMap<String, String> food = (HashMap<String, String>) getItem(position);
            View item = LayoutInflater.from(getContext()).inflate(resourceID,null);
            TextView label1 =item.findViewById(R.id.foodLabel_f);
            label1.setText(food.get("Label"));
            TextView label2 =item.findViewById(R.id.caloriesV_f);
            label2.setText(food.get("Calories"));
            TextView label3 =item.findViewById(R.id.fatV_f);
            label3.setText(food.get("Fat"));
            TextView label4 =item.findViewById(R.id.carbsV_f);
            label4.setText(food.get("Carbs"));
            TextView label5 =item.findViewById(R.id.fiberV_f);
            label5.setText(food.get("Fiber"));
            Button delete = item.findViewById(R.id.delete_item);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View e) {
                    FoodMainActivity.getDb().delete(DataBaseHelper.TABLE_NAME, DataBaseHelper.KEY_NAME + "=?", new String[]{food.get("Label")});
                }
            });


            return item;
        }
    }
}

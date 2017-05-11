/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sushinski.pogodka.DAL.CityManager;
import com.sushinski.pogodka.DL.models.CityModel;
import com.sushinski.pogodka.R;
import com.sushinski.pogodka.adapter.SelectCityRecyclerViewAdapter;
import com.sushinski.pogodka.interfaces.OnEditCitiesListEndListener;
import java.util.List;
import static com.sushinski.pogodka.DL.models.CityModel.CHECKED;

/**
 * Fragment class for selecting/deselecting cities fro show forecasts
 */
public class SelectCitiesFragment extends Fragment implements OnEditCitiesListEndListener {
    List<CityModel> mCities;
    CityManager mCManager;
    public SelectCitiesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            mCManager = new CityManager(getContext());
            mCities = mCManager.getCitiesList(null);
            recyclerView.setAdapter(new SelectCityRecyclerViewAdapter(mCities, this));
        }
        return view;
    }

    @Override
    public void onEditCitiesListEnd(CityModel city_model) {
        mCManager.setCitySelection(city_model.mCityName, city_model.mIsSelected.equals(CHECKED));
    }
}

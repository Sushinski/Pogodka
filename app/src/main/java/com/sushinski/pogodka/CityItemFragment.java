package com.sushinski.pogodka;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.sushinski.pogodka.DAL.CityManager;
import com.sushinski.pogodka.DAL.ForecastManager;
import com.sushinski.pogodka.DL.POJO.CitiesItemField;
import com.sushinski.pogodka.DL.models.ForecastModel;
import com.sushinski.pogodka.interfaces.OnListFragmentInteractionListener;
import com.sushinski.pogodka.DL.models.CityModel;
import com.sushinski.pogodka.interfaces.UpdateFinishListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.sushinski.pogodka.MainActivity.CITY_NAME;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CityItemFragment extends Fragment
        implements UpdateFinishListener, OnListFragmentInteractionListener {
    private List<CitiesItemField> mListItems;
    private CityItemRecyclerViewAdapter mAdapter;
    CityManager mC_Mngr;
    ForecastManager mF_Mngr;

    public CityItemFragment() {
    }

    @SuppressWarnings("unused")
    public static CityItemFragment newInstance() {
        return new CityItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mC_Mngr = new CityManager(getContext());
        mF_Mngr = new ForecastManager(getContext());
        List<CityModel> c_list = mC_Mngr.getCitiesList(true);
        ArrayList<String> city_names = new ArrayList<>(c_list.size());
        for(CityModel cm: c_list){
            city_names.add(cm.mCityName);
        }
        mF_Mngr.updateWeatherData(city_names, "1", this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cityitem_list, container, false);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            mListItems = new ArrayList<>();
            mAdapter = new CityItemRecyclerViewAdapter(mListItems, this);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CityItemFragment.this.onListFragmentInteraction(mListItems.get(view.getId()));
                }
            });
        }
        return view;
    }


    private void updateItemList(){
        List<CityModel> cm_list = mC_Mngr.getCitiesList(true);
        Calendar calendar = Calendar.getInstance();
// reset hour, minutes, seconds and millis
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        mListItems.clear();
        for(CityModel cm: cm_list){
            try{
                ForecastModel fm = mF_Mngr.getActualForecast(cm.mCityName,
                        "1", calendar.getTimeInMillis()).get(0);
                CitiesItemField field = new CitiesItemField();
                field.mCityName = cm.mCityName;
                field.mForecastFields = mF_Mngr.parseForecastJson(fm.mForecast);
                mListItems.add(field);
            }catch(IndexOutOfBoundsException e){
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void updateFinish() {
        updateItemList();
    }

    @Override
    public void onListFragmentInteraction(CitiesItemField item) {
        Intent detail = new Intent(getActivity(), DetailActivity.class);
        detail.putExtra(CITY_NAME, item.toString());
        startActivity(detail);
    }
}

/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sushinski.pogodka.DAL.CityManager;
import com.sushinski.pogodka.DAL.ForecastManager;
import com.sushinski.pogodka.DL.POJO.CitiesItemField;
import com.sushinski.pogodka.DL.POJO.ForecastField;
import com.sushinski.pogodka.DL.models.ForecastModel;
import com.sushinski.pogodka.R;
import com.sushinski.pogodka.activity.DetailActivity;
import com.sushinski.pogodka.adapter.CityItemRecyclerViewAdapter;
import com.sushinski.pogodka.interfaces.INetworkStateChangeListener;
import com.sushinski.pogodka.interfaces.OnListFragmentInteractionListener;
import com.sushinski.pogodka.DL.models.CityModel;
import com.sushinski.pogodka.interfaces.UpdateFinishListener;
import com.sushinski.pogodka.receivers.NetworkStateReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.sushinski.pogodka.activity.MainActivity.CITY_NAME;

/**
 * Class describes primary forecast list fragment unit
 */
public class CityItemFragment
        extends Fragment
        implements UpdateFinishListener,
        OnListFragmentInteractionListener,
        INetworkStateChangeListener{
    private List<CitiesItemField> mListItems;
    private CityItemRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyMessage;
    CityManager mC_Mngr;
    ForecastManager mF_Mngr;
    private BroadcastReceiver mStateReceiver;

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
        mStateReceiver = new NetworkStateReceiver(this);
    }

    /**
     * Updates cities-forecast list contents
     * @param city_model city description to update for, if presented
     */
    public void updateFragmentContent(@Nullable CityModel city_model){
        ArrayList<String> city_names;
        if(city_model == null) {
            List<CityModel> c_list = mC_Mngr.getCitiesList(true);
            city_names = new ArrayList<>(c_list.size());
            for(CityModel cm : c_list) {
                city_names.add(cm.mCityName);
            }
        }else{
            city_names = new ArrayList<>(1);
            city_names.add(city_model.mCityName);
        }
        // todo: set wait icon until updating to finish
        mF_Mngr.updateWeatherData(city_names, "1", this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cityitem_list, container, false);
        updateFragmentContent(null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cities_list);
        mEmptyMessage = (TextView) view.findViewById(R.id.empty_list_text);
        mListItems = new ArrayList<>();
        mAdapter = new CityItemRecyclerViewAdapter(mListItems, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CityItemFragment.this.
                        onListFragmentInteraction(mListItems.get(view.getId()));
            }
        });
        return view;
    }

    /**
     * updates cities-forecasts list with saved in database values
     */
    private void updateItemList(){
        List<CityModel> cm_list = mC_Mngr.getCitiesList(true);
        // get time of today`s begin
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        mListItems.clear();
        boolean bActual = true;
        for(CityModel cm: cm_list){
            CitiesItemField field = new CitiesItemField();
            field.mCityName = cm.mCityName;
            try{
                // gets actual saved forecast from db
                ForecastModel fm = mF_Mngr.getActualForecast(cm.mCityName,
                        "1", calendar.getTimeInMillis()).get(0);
                // parse forecast field
                field.mForecastFields = mF_Mngr.parseForecastJson(fm.mForecast);
                mListItems.add(field);
            }catch(IndexOutOfBoundsException e){
                // skip empty forecasts
            }

        }
        if(mListItems.size() > 0) {
            showEmptyView(false);
            mAdapter.notifyDataSetChanged();
        }else{
            showEmptyView(true);
        }

        if(mListItems.size() < cm_list.size()) {
            // shows warn if not all cities updated
            Toast.makeText(getContext(),
                    getContext().getString(R.string.unable_to_get),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * show/hides empty view for empty/non-empty list
     * @param bShow State of displayed view
     */
    private void showEmptyView(boolean bShow){
        mRecyclerView.setVisibility(bShow ? View.GONE : View.VISIBLE);
        mEmptyMessage.setVisibility(bShow ? View.VISIBLE : View.GONE);
    }

    /**
     * remote update finish callback
     */
    @Override
    public void updateFinish() {
        updateItemList();
    }

    /**
     * cities list interaction callback
     * @param item object of interacted list otem content
     */
    @Override
    public void onListFragmentInteraction(CitiesItemField item) {
        Intent detail = new Intent(getActivity(), DetailActivity.class);
        detail.putExtra(CITY_NAME, item.mCityName);
        startActivity(detail);
    }

    @Override
    public void onStart(){
        super.onStart();
        // registers network state receiver
        getContext().registerReceiver(mStateReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onStop(){
        super.onStop();
        // unregisters receiver
        getContext().unregisterReceiver(mStateReceiver);
    }

    @Override
    public void onNetworkStateChange() {
        updateFragmentContent(null);
    }
}

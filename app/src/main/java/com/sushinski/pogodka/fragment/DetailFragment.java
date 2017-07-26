/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sushinski.pogodka.DAL.ForecastManager;
import com.sushinski.pogodka.DL.POJO.ForecastField;
import com.sushinski.pogodka.R;
import com.sushinski.pogodka.interfaces.INetworkStateChangeListener;
import com.sushinski.pogodka.interfaces.OnDetailInteractionListener;
import com.sushinski.pogodka.DL.models.ForecastModel;
import com.sushinski.pogodka.interfaces.UpdateFinishListener;
import com.sushinski.pogodka.receivers.NetworkStateReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.sushinski.pogodka.DL.POJO.ForecastField.CELSIUM;
import static com.sushinski.pogodka.activity.MainActivity.CITY_NAME;



public class DetailFragment
        extends Fragment
        implements INetworkStateChangeListener,
        AdapterView.OnItemSelectedListener,
        UpdateFinishListener {
    public static final String[] mDaysArray =  new String[]{"3","5","7"};
    public static final String DAYS_COUNT_SEL = "com.sushinski.podvodka.DAYS_COUNT_SEL";
    private OnDetailInteractionListener mListener;
    private String mCityName;
    private int mDaysCountSel;
    private ListView mForecastList;
    private List<ForecastModel> mForecastListItems;
    private ForecastManager mMngr;
    private BroadcastReceiver mStateReceiver;


    public DetailFragment() {
    }

    @SuppressWarnings("unused")
    public static DetailFragment newInstance(String city_name) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(CITY_NAME, city_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDaysCountSel = savedInstanceState.getInt(DAYS_COUNT_SEL);
        }
        mForecastListItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView mMainText = (TextView) v.findViewById(R.id.mainText);
        mMainText.setText(mCityName);
        mForecastList = (ListView) v.findViewById(R.id.forecast_list_view);
        mMngr = new ForecastManager(getContext());
        mStateReceiver = new NetworkStateReceiver(this);
        initSpinner(v);
        setAdapter();
        return v;
    }

    public void initSpinner(View view){
        Spinner mSpinner = (Spinner) view.findViewById(R.id.duration_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, mDaysArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(mDaysCountSel);
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailInteractionListener) {
            mListener = (OnDetailInteractionListener) context;
            mCityName = mListener.getFragmentParams().getString(CITY_NAME);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void setAdapter(){
        @SuppressWarnings("unchecked")
        ArrayAdapter mAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                mForecastListItems) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                ForecastModel fm = mForecastListItems.get(position);
                Long time_msec = fm.mDate;
                Date dt = new Date(time_msec);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat frmt = new SimpleDateFormat("dd MMMM yyyy");

                ForecastField fields = mMngr.parseForecastJson(fm.mForecast);
                String t = getResources().getString(R.string.temperature);
                text1.setText(frmt.format(dt) + t + fields.day_temp + CELSIUM);
                text2.setText(fields.detailed_descr);
                return view;
            }
        };
        mForecastList.setAdapter(mAdapter);
    }

    public void updateAdapter(){
        mForecastListItems = mMngr.getActualForecast(mCityName, mDaysArray[mDaysCountSel], null);
        if(mForecastListItems.size() < Integer.parseInt(mDaysArray[mDaysCountSel])){
            // shows warn if actual items less than desired
            Toast.makeText(getContext(),
                    getContext().getText(R.string.unable_to_get),Toast.LENGTH_LONG).show();
        }
        setAdapter();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mDaysCountSel = i;
        mMngr.updateWeatherData(new ArrayList<>(Collections.singletonList(mCityName)),
                mDaysArray[mDaysCountSel], this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void updateFinish() {
        updateAdapter();
    }

    @Override
    public void onStart(){
        super.onStart();
        // register network status receiver
        getContext().registerReceiver(mStateReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onStop(){
        super.onStop();
        // unregister receiver
        getContext().unregisterReceiver(mStateReceiver);
    }

    /**
     * network changes receiver callback implementation
     */
    @Override
    public void onNetworkStateChange() {
        // update forecasts from remote if going online
        mMngr.updateWeatherData(new ArrayList<>(Collections.singletonList(mCityName)),
                mDaysArray[mDaysCountSel], this);
    }
}

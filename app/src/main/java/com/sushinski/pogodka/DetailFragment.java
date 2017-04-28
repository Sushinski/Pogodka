package com.sushinski.pogodka;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sushinski.pogodka.DAL.ForecastManager;
import com.sushinski.pogodka.DL.POJO.ForecastFields;
import com.sushinski.pogodka.interfaces.OnDetailInteractionListener;
import com.sushinski.pogodka.DL.models.ForecastModel;
import com.sushinski.pogodka.interfaces.UpdateFinishListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sushinski.pogodka.MainActivity.CITY_NAME;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.sushinski.pogodka.interfaces.OnDetailInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment
        implements AdapterView.OnItemSelectedListener, UpdateFinishListener {
    public static final String[] mDaysArray =  new String[]{"3", "7"};
    public static final String DAYS_COUNT_SEL = "com.sushinski.podvodka.DAYS_COUNT_SEL";
    private OnDetailInteractionListener mListener;
    private String mCityName;
    private int mDaysCountSel;
    private TextView mMainText;
    private ListView mForecastList;
    private List<ForecastModel> mForecastListItems;
    private Spinner mSpinner;
    private ArrayAdapter mAdapter;
    private ForecastManager mMngr;



    public DetailFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        mMainText = (TextView) v.findViewById(R.id.mainText);
        mMainText.setText(mCityName);
        mForecastList = (ListView) v.findViewById(R.id.forecast_list_view);
        mMngr = new ForecastManager(getContext());
        initSpinner(v);
        //setAdapter();
        return v;
    }

    public void initSpinner(View view){
        mSpinner = (Spinner) view.findViewById(R.id.duration_spinner);
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
        mAdapter =
                new ArrayAdapter(getActivity(),
                        android.R.layout.simple_list_item_2,
                        android.R.id.text1,
                        mForecastListItems){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                        ForecastModel fm = mForecastListItems.get(position);
                        Long time_sec = fm.mDate;
                        Date dt = new Date(time_sec * 1000);
                        SimpleDateFormat frmt = new SimpleDateFormat("dd MMMMMMMMM yyyy");
                        ForecastFields fields = mMngr.parseForecastJson(fm.mForecast);
                        text1.setText(frmt.format(dt) + " Температура: " + fields.day_temp + "\u00B0 C");
                        text2.setText(fields.detailed_descr);
                        return view;
                    }
                };
        mForecastList.setAdapter(mAdapter);
    }

    public void updateAdapter(){
        mForecastListItems = mMngr.getActualForecast(mCityName, mDaysArray[mDaysCountSel]);
        setAdapter();
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mDaysCountSel = i;
        mMngr.updateWeatherData(mCityName, mDaysArray[mDaysCountSel], this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void updateFinish() {
        updateAdapter();
    }


}

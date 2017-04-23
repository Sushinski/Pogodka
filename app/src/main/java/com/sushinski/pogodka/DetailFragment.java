package com.sushinski.pogodka;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sushinski.pogodka.DAL.RemoteFetcher;
import com.sushinski.pogodka.interfaces.OnDetailInteractionListener;
import com.sushinski.pogodka.models.ForecastModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.sushinski.pogodka.MainActivity.CITY_NAME;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.sushinski.pogodka.interfaces.OnDetailInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    private OnDetailInteractionListener mListener;
    private String mCityName;
    private Handler handler;
    private TextView mMainText;
    private ListView mForecastList;
    private List<ForecastModel> mForecastListItems;

    public DetailFragment() {
        handler = new Handler();
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
        Bundle args = getArguments();
        if( args != null &&
                args.containsKey(CITY_NAME)) {
            mCityName = args.getString(CITY_NAME);
        }
        mForecastListItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        mMainText = (TextView) v.findViewById(R.id.mainText);
        mForecastList = (ListView) v.findViewById(R.id.forecast_list_view);
        updateWeatherData(mCityName);
        return v;
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

    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetcher.getWeatherJSON(getActivity(), city);
                Runnable r;
                if(json == null){
                    r = new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.bad_request),
                                    Toast.LENGTH_LONG).show();
                        }
                    };
                } else {
                    r = new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    };
                }
                handler.post(r);
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {
            mMainText.setText(json.getJSONObject("city").getString("name").toUpperCase(Locale.US));
            mForecastListItems.clear();
            JSONArray objs = json.getJSONArray("list");
            for(int i = 0; i < Integer.parseInt(json.getString("cnt")); ++i){
                JSONObject obj = objs.getJSONObject(i);
                if(obj != null){
                    ForecastModel item = new ForecastModel();
                    item.mDate = obj.getString("dt");
                    item.mTemp = obj.getJSONObject("temp").getString("day");
                    item.mSecondaryForecast = obj.getJSONArray("weather").
                            getJSONObject(0).getString("description");
                    mForecastListItems.add(item);
                }
            }
            ArrayAdapter adapter =
                    new ArrayAdapter(getActivity(),
                            android.R.layout.simple_list_item_2,
                            android.R.id.text1,
                            mForecastListItems){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                            TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                            ForecastModel fi = mForecastListItems.get(position);
                            text1.setText(fi.mDate + " Температура: " + fi.mTemp);
                            text2.setText(fi.mSecondaryForecast);
                            return view;
                        }
                    };
            mForecastList.setAdapter(adapter);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
}

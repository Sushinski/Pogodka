/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sushinski.pogodka.DL.POJO.CitiesItemField;
import com.sushinski.pogodka.DL.models.ForecastModel;
import com.sushinski.pogodka.R;
import com.sushinski.pogodka.dummy.DummyContent.DummyItem;
import com.sushinski.pogodka.interfaces.OnListFragmentInteractionListener;

import java.util.List;

import static com.sushinski.pogodka.DL.POJO.ForecastField.CELSIUM;

/**
 * recycler view adapter shows city with current temperature
 *
 */
public class CityItemRecyclerViewAdapter extends
        RecyclerView.Adapter<CityItemRecyclerViewAdapter.ViewHolder> {

    private final List<CitiesItemField> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CityItemRecyclerViewAdapter(List<CitiesItemField> items,
                                       OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cityitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CitiesItemField field = mValues.get(position);
        holder.mItem = field;
        // form item text : city name , temperature and celsium sign with detailed description
        holder.mIdView.setText(field.mCityName + ": " + field.mForecastFields.day_temp + CELSIUM +
        ", " + field.mForecastFields.detailed_descr);
        holder.mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final ImageButton mInfoButton;
        CitiesItemField mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.textView);
            mInfoButton = (ImageButton) view.findViewById(R.id.button_info);
        }

        @Override
        public String toString() {
            return mIdView.getText().toString();
        }
    }
}

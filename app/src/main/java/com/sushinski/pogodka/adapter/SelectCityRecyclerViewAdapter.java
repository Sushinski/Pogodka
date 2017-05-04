package com.sushinski.pogodka.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.sushinski.pogodka.DL.models.CityModel;
import com.sushinski.pogodka.R;
import com.sushinski.pogodka.interfaces.OnEditCitiesListEndListener;
import java.util.List;
import static com.sushinski.pogodka.DL.models.CityModel.CHECKED;
import static com.sushinski.pogodka.DL.models.CityModel.UNCHECKED;


public class SelectCityRecyclerViewAdapter
        extends RecyclerView.Adapter<SelectCityRecyclerViewAdapter.ViewHolder> {
    private final List<CityModel> mValues;
    private final OnEditCitiesListEndListener mListener;

    public SelectCityRecyclerViewAdapter(List<CityModel> items,
                                         OnEditCitiesListEndListener listener) {
        mListener = listener;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTextView.setText(holder.mItem.mCityName);
        holder.mCheckBox.setChecked(holder.mItem.mIsSelected.equals(CHECKED));

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mItem.mIsSelected = holder.mCheckBox.isChecked() ? CHECKED : UNCHECKED;
                if (null != mListener) {
                    mListener.onEditCitiesListEnd(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public CityModel mItem;
        public final TextView mTextView;
        public final CheckBox mCheckBox;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextView = (TextView) view.findViewById(R.id.text_view);
            mCheckBox = (CheckBox) view.findViewById(R.id.check_box);
        }

        @Override
        public String toString() {
            return mItem.mCityName;
        }
    }
}

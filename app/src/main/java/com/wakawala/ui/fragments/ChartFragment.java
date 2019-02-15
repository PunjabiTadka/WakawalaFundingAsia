package com.wakawala.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.wakawala.DayAxisValueFormatter;
import com.wakawala.R;
import com.wakawala.ValueFormatter;
import com.wakawala.XYMarkerView;
import com.wakawala.baseclasses.BaseFragment;
import com.wakawala.databinding.FragmentChatBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class ChartFragment extends BaseFragment implements View.OnClickListener {

    private FragmentChatBinding mBinding;

    public ChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static ChartFragment newInstance() {
        ChartFragment fragment = new ChartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        initUI();
        loadData();
        return mBinding.getRoot();
    }


    @Override
    protected void initUI() {

    }

    @Override
    protected void loadData() {

//        mBinding.days.setSelected(true);

        mBinding.days.setOnClickListener(this);
        mBinding.weeks.setOnClickListener(this);
        Chart chart = mBinding.ivChart;
        ((BarChart) chart).setPinchZoom(false);
        ((BarChart) chart).setDrawBarShadow(false);
        ((BarChart) chart).setDoubleTapToZoomEnabled(false);
        setData(5, 10);

    }

    private void setData(int count, float range) {

        float start = 1f;
        Chart chart = mBinding.ivChart;
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = (int) start; i < start + count; i++) {
            float val = (float) (Math.random() * (range + 1));

            if (Math.random() * 100 < 25) {
                values.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.ic_star)));
            } else {
                values.add(new BarEntry(i, val));
            }
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "");

            set1.setDrawIcons(false);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            ValueFormatter xAxisFormatter = new DayAxisValueFormatter((BarLineChartBase<?>) chart);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);
            xAxis.setValueFormatter(xAxisFormatter);

            XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
            chart.setMarker(mv);
//            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/

            int startColor1 = ContextCompat.getColor(getContext(), R.color.colorPrimary);
            int endColor1 = ContextCompat.getColor(getContext(), R.color.colorAccent);


            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new GradientColor(startColor1, endColor1));

            set1.setGradientColors(gradientColors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);

            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.days:
                mBinding.days.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_green));
                mBinding.days.setTextColor(Color.WHITE);

                mBinding.weeks.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_transparent_stroke));
                mBinding.weeks.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                break;
            case R.id.weeks:
                mBinding.weeks.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_green));
                mBinding.weeks.setTextColor(Color.WHITE);

                mBinding.days.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_transparent_stroke));
                mBinding.days.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                break;
        }
    }
}

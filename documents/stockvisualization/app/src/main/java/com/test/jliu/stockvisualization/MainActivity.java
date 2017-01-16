package com.test.jliu.stockvisualization;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private boolean average = false;
    private LineChart mLineChart;
    private LineDataSet averageSetComp1;
    private int counter = -1;
    private int x = 0;
    private ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
    private ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
    private ArrayList<String> xVals = new ArrayList<String>(); //setting x values
    private LineData data = new LineData(xVals, dataSets);//reference for average

    private ArrayList<Entry> averageComp = new ArrayList<Entry>();
    private ArrayList<ILineDataSet> averageDataSets = new ArrayList<ILineDataSet>();
    private ArrayList<String> averageXVals = new ArrayList<String>(); //setting x values
    private LineData averageData = new LineData(xVals, averageDataSets);//reference for average

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
//test comment
        LineDataSet setComp1 = new LineDataSet(valsComp1, "Stock Value"); //compiled data
        // setComp1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        setComp1.setLineWidth(5);
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSets.add(setComp1);

        averageSetComp1 = new LineDataSet(averageComp, "Average"); //compiled data
        //setComp1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        averageSetComp1.setColor(Color.RED);
        averageSetComp1.setLineWidth(5);
        averageSetComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        //dataSets.add(averageSetComp1);

        mLineChart = (LineChart) findViewById(R.id.chart);
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.setData(data);
        mLineChart.invalidate();
    }

    public void addPoint(View view) {
        average();
        x++;
        Firebase ref = new Firebase("https://stockvisualization.firebaseio.com/");
        ref.child("0/data/1/" + x).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String value = (String) snapshot.child("c").getValue();
                String time = (String) snapshot.child("d").getValue();
                time = time.substring(time.length() - 11);
                System.out.println("" + value + " : " + time);
                counter++;
                xVals.add("" + time);

                LineChart mLineChart = (LineChart) findViewById(R.id.chart);
                data.addEntry(new Entry(new Float(value), counter), 0);
                data.notifyDataChanged();
                mLineChart.setAutoScaleMinMaxEnabled(true);
                mLineChart.notifyDataSetChanged(); // let the chart know it's data changed
                mLineChart.invalidate(); // refresh
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        average();
    }

    public void average(View view) {
        if (average == false) {
            data.addDataSet(averageSetComp1);
            System.out.println("average");
            float y = 0;
            float total = 0;
            for (int i = 5; i < x; i++) {
                for (int count = i - 5; count < i; count++) {
                    float value = valsComp1.get(count).getVal();
                    total = total + value;
                }
                total = total / 5;
                averageSetComp1.addEntry(new Entry(total, i));
                //averageData.notifyDataChanged();
                //mLineChart.setAutoScaleMinMaxEnabled(true);
                mLineChart.notifyDataSetChanged(); // let the chart know it's data changed
                mLineChart.invalidate(); // refresh
                total = 0;
            }
            average = true;
        } else {
            data.removeDataSet(averageSetComp1);
            averageSetComp1.clear();
            mLineChart.notifyDataSetChanged(); // let the chart know it's data changed
            mLineChart.invalidate(); // refresh
            average = false;
        }
    }

    public void average() {
        if (average == false) {
            data.addDataSet(averageSetComp1);
            System.out.println("average");
            float y = 0;
            float total = 0;
            for (int i = 5; i < x; i++) {
                for (int count = i - 5; count < i; count++) {
                    float value = valsComp1.get(count).getVal();
                    total = total + value;
                }
                total = total / 5;
                averageSetComp1.addEntry(new Entry(total, i));
                //averageData.notifyDataChanged();
                //mLineChart.setAutoScaleMinMaxEnabled(true);
                mLineChart.notifyDataSetChanged(); // let the chart know it's data changed
                mLineChart.invalidate(); // refresh
                total = 0;
            }
            average = true;
        } else {
            data.removeDataSet(averageSetComp1);
            averageSetComp1.clear();
            mLineChart.notifyDataSetChanged(); // let the chart know it's data changed
            mLineChart.invalidate(); // refresh
            average = false;
        }
    }
}

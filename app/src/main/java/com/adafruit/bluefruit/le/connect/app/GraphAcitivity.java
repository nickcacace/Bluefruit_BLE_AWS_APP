package com.adafruit.bluefruit.le.connect.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.adafruit.bluefruit.le.connect.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;


public class GraphAcitivity extends AppCompatActivity {
    LineChart lineChart;
    ArrayList<ILineDataSet> lineDataSets;
    static String test = "123,234,222,333,444,555,666,777,888,1,2,3,41,4,521,35,2,234,234,2342,34,234";
    ArrayList<String> data_1, data_2;
    String value_1 = "value_1";
    String value_2 = "value_2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_acitivity);

        lineChart = findViewById(R.id.linechart);
        lineDataSets = new ArrayList<>();
        Intent intent = getIntent();
        data_1 = intent.getStringArrayListExtra("Vdata");
        data_2 = intent.getStringArrayListExtra("Cdata");

        Log.d("data_1 in Graph : ", data_1.toString());
        Log.d("data_2 in Graph : ", data_2.toString());

        ArrayList<LineDataSet> dataset_1 = makingDataSet(data_1,value_1);
        ArrayList<LineDataSet> dataset_2 = makingDataSet(data_2,value_2);

        ArrayList<ILineDataSet> totaldatasets = new ArrayList<>();
        totaldatasets.addAll(dataset_1);
        totaldatasets.addAll(dataset_2);

        lineChart.setData(new LineData(totaldatasets));


    }
    public void update(){
        String[] con = test.split(",");
        ArrayList<Entry> ndataset = new ArrayList<>();

        for(int i = 0;i<con.length;i++){
            ndataset.add(new Entry(i,Integer.parseInt(con[i])));
        }
        drawlineChart(ndataset);
    }

    public void drawlineChart(ArrayList<Entry> dataset){

        LineDataSet lineDataSet1 = new LineDataSet(dataset,"test");
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColor(Color.BLUE);

        ArrayList<Entry> set2 = new ArrayList<>();
        for(int i = 0;i<dataset.size();i++){
            set2.add(new Entry(i,i));
        }
        LineDataSet lineDataSet2 = new LineDataSet(set2,"linear");
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColor(Color.RED);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        lineChart.setData(new LineData(lineDataSets));

    }

    public ArrayList<LineDataSet> makingDataSet(ArrayList<String> input, String name){
        ArrayList<LineDataSet> result = new ArrayList<>();
        for(int i = 0; i<input.size();i++){
            String ip = input.get(i);
            String[] con = ip.split(",");
            ArrayList<Entry> ndataset = new ArrayList<>();

            for(int j = 0;j<con.length;j++){
                ndataset.add(new Entry(j,Integer.parseInt(con[j])));
            }

            LineDataSet linedataset = new LineDataSet(ndataset,name+ " "+Integer.toString(i+1));
            linedataset.setDrawCircles(false);
            if(name.equals(value_2)) {
                linedataset.setColor(Color.RED);
            }
            if(name.equals(value_1)){
                linedataset.setColor(Color.BLUE);
            }
            result.add(linedataset);
        }

    return result;
    }


 }

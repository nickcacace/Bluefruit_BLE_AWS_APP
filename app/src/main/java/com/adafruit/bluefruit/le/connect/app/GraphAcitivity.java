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
    ArrayList<String> vData, cData;
    String[] xaxes;
    ArrayList<Entry> dataset;
    String voltage = "Voltage";
    String current = "Current";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_acitivity);

        lineChart = (LineChart)findViewById(R.id.linechart);
        lineDataSets = new ArrayList<>();
        Intent intent = getIntent();
        vData = intent.getStringArrayListExtra("Vdata");
        cData = intent.getStringArrayListExtra("Cdata");

        Log.d("vData in Graph : ",vData.toString());
        Log.d("cData in Graph : ",cData.toString());

        ArrayList<LineDataSet> vdatasets = makingDataSet(vData,voltage);
        ArrayList<LineDataSet> cdatasets = makingDataSet(cData,current);

        ArrayList<ILineDataSet> totaldatsets = new ArrayList<>();
        for(int i = 0;i<vdatasets.size();i++){
            totaldatsets.add(vdatasets.get(i));
        }
        for(int i = 0;i<cdatasets.size();i++){
            totaldatsets.add(cdatasets.get(i));
        }

        lineChart.setData(new LineData(totaldatsets));


    }
    public void update(String msg){
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
            if(name.equals(current)) {
                linedataset.setColor(Color.RED);
            }
            if(name.equals(voltage)){
                linedataset.setColor(Color.BLUE);
            }
            result.add(linedataset);
        }

    return result;
    }


 }

package com.goyourlife.gofit_demo;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HeartAnalysisActivity extends Activity {

    private Button btnMood, btnMode;
    private Spinner spnMonth;
    private TextView tvSuggest, tvContent, tvURL;
    private ImageButton btnGoBack;
    String text = "mood";
    ArrayList<String> selectMonth;
    ArrayList<String> fourYearMonth;


    PieDataSet pieDataSet;
    PieData pieData;
    PieChart pieChart;
    Description desc;

    float a, b, c;
    float d, e, f, g;
    String temp;
    String suggestText = "", contentText = "", urlText = "";

    String account, values;
    ArrayList<String> userAcc = new ArrayList<String>(), userMode = new ArrayList<String>(), userType = new ArrayList<String>(), userMusic = new ArrayList<String>(), userLight = new ArrayList<String>();
    ArrayList<String>  startDate = new ArrayList<String>(), startTime = new ArrayList<String>(), userHrRate = new ArrayList<String>();

    ArrayList<String>  analysisResult = new ArrayList<String>(),analysisSuggest = new ArrayList<String>(), analysisContent = new ArrayList<String>(), analysisURL = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartanalysis);

        //???????????????
        btnGoBack = findViewById(R.id.imageButton28);
        btnMood = findViewById(R.id.button25);
        btnMode = findViewById(R.id.button26);
        btnMode.setOnClickListener(myListener);
        btnMood.setOnClickListener(myListener);

        spnMonth = findViewById(R.id.spinner6);
        tvSuggest = findViewById(R.id.textView80);
        tvContent = findViewById(R.id.textView81);
        tvURL = findViewById(R.id.textView82);
        pieChart = findViewById(R.id.piechart);

        //??? mysql ???????????? heartAnalysis ?????????
        Bundle bundle = getIntent().getExtras();
        account = bundle.getString("account");
        values = bundle.getString("value");
        String[] split = values.split("good");

        //?????????????????????????????????
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        selectMonth = new ArrayList<String>();
        fourYearMonth = new ArrayList<String>();

        //???????????????????????????
        for(int i = 0; i< 4; i++) {
            if(month - 1 <=0) {
                year -= 1;
                month = 12;
                selectMonth.add(year + "??? " + month + "???");

                if(month < 10) {
                    fourYearMonth.add(year + "/0" + month);
                }
                else {
                    fourYearMonth.add(year + "/" + month);
                }
            }
            else {
                month -= 1;
                selectMonth.add(year + "??? " + month + "???");
                if(month < 10) {
                    fourYearMonth.add(year + "/0" + month);
                }
                else {
                    fourYearMonth.add(year + "/" + month);
                }
            }
        }


        //?????????????????????
        try{
            //????????????JSONArray?????????JSON???????????????getString(String key)?????????????????????
            JSONArray array = new JSONArray(split[0]);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                userAcc.add(jsonObject.getString("user_acc"));
                userMode.add(jsonObject.getString("user_mode"));
                userType.add(jsonObject.getString("user_type"));
                userMusic.add(jsonObject.getString("user_music"));
                userLight.add(jsonObject.getString("user_light"));
                startDate.add(jsonObject.getString("start_date"));
                startTime.add(jsonObject.getString("start_time"));
                userHrRate.add(jsonObject.getString("user_hr"));
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }


        //?????????????????????
        try{
            //????????????JSONArray?????????JSON???????????????getString(String key)?????????????????????
            JSONArray array = new JSONArray(split[1]);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                analysisResult .add(jsonObject.getString("analysis_result"));
                analysisSuggest.add(jsonObject.getString("analysis_suggest"));
                analysisContent.add(jsonObject.getString("analysis_content"));
                analysisURL.add(jsonObject.getString("analysis_url"));
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapterSelectQuestion = new ArrayAdapter<String>(HeartAnalysisActivity.this, R.layout.myspinner, selectMonth);
        spnMonth.setAdapter(adapterSelectQuestion);
        spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                temp = parent.getSelectedItem().toString();   //??????????????????????????????
                a=0;b=0;c=0;d=0;e=0;f=0;g=0;
                for(int i = 0; i< userMode.size(); i++) {
                    if(startDate.get(i).contains(fourYearMonth.get(position))) {
                        if(userMode.get(i).equals("low")) {
                            a += 1;
                        }
                        if(userMode.get(i).equals("calm")) {
                            b += 1;
                        }
                        if(userMode.get(i).equals("excitement")) {
                            c += 1;
                        }
                        if(userMode.get(i).equals("office")) {
                            d += 1;
                        }
                        if(userMode.get(i).equals("relax")) {
                            e += 1;
                        }
                        if(userMode.get(i).equals("morning")) {
                            f += 1;
                        }
                        if(userMode.get(i).equals("night")) {
                            g += 1;
                        }
                    }
                }
                pieListShow();  //???????????????
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //???????????????
    public void pieListShow() {
        suggestText = "";   //????????????
        contentText = "";   //????????????
        urlText = "";       //????????????

        pieChart.setUsePercentValues(true);   //????????????

        desc = new Description();    //????????????????????????
        desc.setText(temp + " ????????????");
        desc.setTextSize(12f);

        pieChart.setDescription(desc);

        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(40f);

        List<PieEntry> value = new ArrayList<>();    //?????????????????????

        if(text == "mood") {
            if(a != 0) {
                value.add(new PieEntry(a,"??????"));
            }
            if(b != 0) {
                value.add(new PieEntry(b, "??????"));
            }
            if(c != 0) {
                value.add(new PieEntry(c, "??????"));
            }
            if(a == 0 && b == 0 && c == 0) {
                value = new ArrayList<>();
                tvSuggest.setText("?????????????????????");
                tvContent.setText("");
                tvURL.setText("");
            }
            if(a != 0 || b != 0 || c != 0) {
                if(a == b && a == c) {
                    tvSuggest.setText("??????????????????????????????\n");
                    tvContent.setText("??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????!\n");
                    tvURL.setText("\n");
                }
                else {
                    for(int j = 0; j < analysisResult.size(); j ++) {
                        if(analysisResult.get(j).equals("low") && a >= b && a >= c) {
                            suggestText += analysisSuggest.get(j) + "\n";
                            contentText += analysisContent.get(j) + "\n";
                            urlText += analysisURL.get(j) + "\n";
                        }
                        if(analysisResult.get(j).equals("calm") && b >= a && b >= c) {
                            suggestText += analysisSuggest.get(j) + "\n";
                            contentText += analysisContent.get(j) + "\n";
                            urlText += analysisURL.get(j) + "\n";
                        }
                        if(analysisResult.get(j).equals("excitement") && c >= a && c >= b) {
                            suggestText += analysisSuggest.get(j) + "\n";
                            contentText += analysisContent.get(j) + "\n";
                            urlText += analysisURL.get(j) + "\n";
                        }
                    }
                    tvSuggest.setText(suggestText);
                    tvContent.setText(contentText);
                    tvURL.setText("????????????\n" + urlText);
                }
            }
        }

        if(text == "mode") {
            if(d != 0) {
                value.add(new PieEntry(d,"??????"));
            }
            if(e != 0) {
                value.add(new PieEntry(e, "??????"));
            }
            if(f != 0) {
                value.add(new PieEntry(f, "??????"));
            }
            if(g != 0) {
                value.add(new PieEntry(g, "??????"));
            }
            if(d == 0 && e == 0 && f == 0 && g == 0) {
                value = new ArrayList<>();
                tvSuggest.setText("?????????????????????");
                tvContent.setText("");
                tvURL.setText("");
            }
            if(d != 0 || e != 0 || f != 0 || g != 0) {
                if(d == e && d == f && d == g) {
                    tvSuggest.setText("??????????????????????????????\n");
                    tvContent.setText("??????????????????????????????????????????????????????????????????????????????????????????????????????!\n");
                    tvURL.setText("\n");
                }
                else {
                    for(int j = 0; j < analysisResult.size(); j ++) {
                        if(analysisResult.get(j).equals("office") && d >= e && d >= f && d >= g) {
                            suggestText += analysisSuggest.get(j) + "\n";
                            contentText += analysisContent.get(j) + "\n";
                            urlText += analysisURL.get(j) + "\n";
                        }
                        if(analysisResult.get(j).equals("relax") && e >= d && e >= f && e >= g) {
                            suggestText += analysisSuggest.get(j) + "\n";
                            contentText += analysisContent.get(j) + "\n";
                            urlText += analysisURL.get(j) + "\n";
                        }
                        if(analysisResult.get(j).equals("morning") && f >= e && f >= d && f >= g) {
                            suggestText += analysisSuggest.get(j) + "\n";
                            contentText += analysisContent.get(j) + "\n";
                            urlText += analysisURL.get(j) + "\n";
                        }
                        if(analysisResult.get(j).equals("night") && g >= e && g >= f && g >= d) {
                            suggestText += analysisSuggest.get(j) + "\n";
                            contentText += analysisContent.get(j) + "\n";
                            urlText += analysisURL.get(j) + "\n";
                        }
                    }
                    tvSuggest.setText(suggestText);
                    tvContent.setText(contentText);
                    tvURL.setText("????????????\n" + urlText);
                }
            }
        }

        pieDataSet = new PieDataSet(value, text);
        pieDataSet.setValueTextSize(20f);
        pieDataSet.setValueTextColor(0XFFD3ECF3);

        pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.invalidate();

        //?????????????????????????????? --> ???????????? '??????????????????' ??????????????????????????????
        Legend legend1 = pieChart.getLegend();
        legend1.setFormSize(0f); // set the size of the legend forms/shapes
        legend1.setTextSize(0f);
        legend1.setTextColor(Color.WHITE);

        if(text == "mood") {
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        }
        if(text == "mode") {
            pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        }

        pieChart.animateXY(1400, 1400);    //???????????????
    }

    Button.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button25:   //btn ????????????
                    btnMood.setBackgroundResource(R.drawable.example6);
                    btnMood.setTextColor(0XFFFFFFFF);
                    btnMode.setBackgroundColor(0X00FFFFFF);
                    btnMode.setTextColor(0XFF000000);

                    text = "mood";
                    pieListShow();
                    break;
                case R.id.button26:   //btn ????????????
                    btnMode.setBackgroundResource(R.drawable.example6);
                    btnMode.setTextColor(0XFFFFFFFF);
                    btnMood.setBackgroundColor(0X00FFFFFF);
                    btnMood.setTextColor(0XFF000000);

                    text = "mode";
                    pieListShow();
                    break;
            }
        }
    };


}

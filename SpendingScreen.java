package com.example.bizwallet3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Objects;

public class SpendingScreen extends AppCompatActivity {



    Button enter_button;

    int[] colors;

    private DBHandler dbHandler;


    TextView txtMarket, txtEquip, txtRent, txtEmployee, txtOther, txtAverageSpent, txtStartingBud, txtRemainingBud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_screen);

        txtMarket = findViewById(R.id.txtMarket);
        txtRent = findViewById(R.id.txtRent);
        txtEquip = findViewById(R.id.txtEquip);
        txtEmployee = findViewById(R.id.txtEmployee);
        txtOther = findViewById(R.id.txtOther);
        txtStartingBud = findViewById(R.id.txtStartingBud);
        txtRemainingBud = findViewById(R.id.txtRemainingBud);
        txtAverageSpent = findViewById(R.id.txtAverageSpent);

        colors = new int[5];
        colors[0] = ColorTemplate.rgb("266A31");
        colors[1] = ColorTemplate.rgb("65CC0D");
        colors[2] = ColorTemplate.rgb("5B9C24");
        colors[3] = ColorTemplate.rgb("8CBA44");
        colors[4] = ColorTemplate.rgb("515F47");

        dbHandler = new DBHandler(SpendingScreen.this);
        //dbHandler.delete();

        txtMarket.setText("Marketing: " + (int) dbHandler.getMarketingNum());
        txtRent.setText("Rent: " + (int) dbHandler.getRentNum());
        txtEquip.setText("Equipment: " + (int) dbHandler.getEquipmentNum());
        txtEmployee.setText("Employee Needs: " + (int) dbHandler.getEmployeeNeedsNum());
        txtOther.setText("Other: " + (int) dbHandler.getOtherNum());

        Intent intentBudget = getIntent();
        double num = Double.parseDouble(intentBudget.getStringExtra("message_key"));
        txtStartingBud.setText("$"+num);

        txtRemainingBud.setText("$"+String.valueOf(num - dbHandler.getTotalAmount()));


        double avg = dbHandler.getTotalAmount()/30.0;
        txtAverageSpent.setText("$"+ (double) Math.round(avg * 100) / 100);


        PieChart pieChart = findViewById(R.id.chart);
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) dbHandler.getMarketingNum(), "% Marketing"));
        entries.add(new PieEntry((float) dbHandler.getRentNum(), "% Rent"));
        entries.add(new PieEntry((float) dbHandler.getEquipmentNum(), "% Equipment"));
        entries.add(new PieEntry((float) dbHandler.getEmployeeNeedsNum(), "% Employee Needs"));
        entries.add(new PieEntry((float) dbHandler.getOtherNum(), "% Other"));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(ColorTemplate.createColors(colors));

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();
        pieChart.setHoleRadius(10);
        pieChart.setTransparentCircleRadius(10);
        pieChart.setHoleColor(197199192);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(14);
        pieData.setValueTextSize(14);
        pieData.setValueTextColor(ColorTemplate.rgb("FFFFFF"));



        enter_button = findViewById(R.id.enter_button_id);

        enter_button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

}
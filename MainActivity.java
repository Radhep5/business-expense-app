package com.example.bizwallet3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    String[] item = {"Marketing", "Rent", "Equipment", "Employee Needs", "Other"};
    String[] completionStatusItems = {"Complete", "Pending"};

    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextView2;

    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItems2;

    EditText enter_amount, enter_budget;
    Button back_button, submit_button, btnBudget;

    String completionItem;
    String category;

    private DBHandler dbHandler;

    TextView txtPendingPrice;

    ArrayList<String> pendingPrices = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        completionItem = "";
        category = "";

        dbHandler = new DBHandler(MainActivity.this);

        enter_budget = findViewById(R.id.enter_budget_id);
        btnBudget = findViewById(R.id.btnBudget);
        enter_amount = findViewById(R.id.enter_amount_id);
        back_button = findViewById(R.id.backBtn);
        submit_button = findViewById(R.id.btnSubmit);
        txtPendingPrice = findViewById(R.id.txtPendingPrice);

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);
        autoCompleteTextView2 = findViewById(R.id.auto_complete_txt2);
        adapterItems2 = new ArrayAdapter<String>(this, R.layout.list_completion, completionStatusItems);

        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView2.setAdapter(adapterItems2);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Category: " + category, Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                completionItem = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Status: " + completionItem, Toast.LENGTH_SHORT).show();
            }
        });

        back_button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SpendingScreen.class);
            startActivity(intent);

        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // below line is to get data from all edit text fields.
                String amountSpent = enter_amount.getText().toString();

                // validating if the text fields are empty or not.
                if (amountSpent.isEmpty() || completionItem.equals("") || category.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dbHandler.addNewExpense(amountSpent, category, completionItem);
                    Toast.makeText(MainActivity.this, "Expense has been added.", Toast.LENGTH_SHORT).show();
                  //  Intent intent = new Intent(getApplicationContext(), SpendingScreen.class);
                  //  startActivity(intent);
                    if(completionItem.equals("Pending")){
                        pendingPrices.add(amountSpent);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String item : pendingPrices) {
                        stringBuilder.append(item).append("\n");
                    }
                    txtPendingPrice.setText(stringBuilder.toString());
                }

                completionItem = "";
                category = "";
                System.out.println(dbHandler.getTotalAmount());

            }
        });

        btnBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = enter_budget.getText().toString();
                Intent intent = new Intent(getApplicationContext(), SpendingScreen.class);
                intent.putExtra("message_key", str);
                Toast.makeText(MainActivity.this, "Budget Saved", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }


}
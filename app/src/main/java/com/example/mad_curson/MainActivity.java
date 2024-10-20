package com.example.mad_curson;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextDept, editTextSalary;
    private Button buttonInsert, buttonDisplay;
    private TextView textViewRecords;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextDept = findViewById(R.id.editTextDept);
        editTextSalary = findViewById(R.id.editTextSalary);
        buttonInsert = findViewById(R.id.buttonInsert);
        buttonDisplay = findViewById(R.id.buttonDisplay);
        textViewRecords = findViewById(R.id.textViewRecords);

        // Initialize the DBHelper
        dbHelper = new DBHelper(this);

        // Insert record on button click
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ename = editTextName.getText().toString().trim();
                String dept = editTextDept.getText().toString().trim();
                String salaryStr = editTextSalary.getText().toString().trim();

                if (ename.isEmpty() || dept.isEmpty() || salaryStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double salary = Double.parseDouble(salaryStr);

                boolean isInserted = dbHelper.insertEmployee(ename, dept, salary);
                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Record inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error inserting record", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Display records on button click
        buttonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllEmployees();
            }
        });
    }

    // Fetch and display records using Cursor
    private void displayAllEmployees() {
        Cursor cursor = dbHelper.getAllEmployees();
        StringBuilder builder = new StringBuilder();

        // Move to the first row in the result set
        if (cursor.moveToFirst()) {
            do {
                // Get data from each column in the current row
                int eid = cursor.getInt(0);  // First column is eid
                String ename = cursor.getString(1);  // Second column is ename
                String dept = cursor.getString(2);  // Third column is adept
                double salary = cursor.getDouble(3);  // Fourth column is esalary

                // Append the data to the StringBuilder
                builder.append("EID: ").append(eid).append(", ");
                builder.append("Name: ").append(ename).append(", ");
                builder.append("Dept: ").append(dept).append(", ");
                builder.append("Salary: ").append(salary).append("\n");
            } while (cursor.moveToNext());  // Move to the next row
        } else {
            builder.append("No records found.");
        }

        cursor.close();
        textViewRecords.setText(builder.toString());
    }
}

package com.example.lab21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editName, editWeight, editHeight, editAge;
    Button btnSave, btnView, btnClear;
    TableLayout tableLayout;
    TextView textView;
    TableRow tableRow;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = (EditText) findViewById(R.id.editName);
        editWeight = (EditText) findViewById(R.id.editWeight);
        editHeight = (EditText) findViewById(R.id.editHeight);
        editAge = (EditText) findViewById(R.id.editAge);

        btnSave = (Button) findViewById(R.id.buttonSave);
        btnView = (Button) findViewById(R.id.buttonView);
        btnClear = (Button) findViewById(R.id.buttonClear);

        dbHelper = new DBHelper(this);
        tableLayout = (TableLayout) findViewById(R.id.tableView);

    }

    @Override
    public void onClick(View v){
        String name = editName.getText().toString();
        int weight = tryParse(editWeight.getText().toString());
        int height = tryParse(editHeight.getText().toString());
        int age = tryParse(editAge.getText().toString());

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        editName.setText("");
        editAge.setText("");
        editHeight.setText("");
        editWeight.setText("");

        switch (v.getId()){


            case R.id.buttonSave:
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_WEIGHT, weight);
                contentValues.put(DBHelper.KEY_HEIGHT, height);
                contentValues.put(DBHelper.KEY_AGE, age);

                database.insert(DBHelper.TABLE_NAME,null,contentValues);
                break;

            case R.id.buttonView:

                Cursor cursor = database.query(DBHelper.TABLE_NAME,null,null,
                        null,null,null,DBHelper.KEY_AGE,null);
                ArrayList<Integer> data = new ArrayList<Integer>();

                if(cursor.moveToFirst()){
                    data.add(cursor.getColumnIndex(DBHelper.KEY_ID));
                    data.add(cursor.getColumnIndex(DBHelper.KEY_NAME));
                    data.add(cursor.getColumnIndex(DBHelper.KEY_WEIGHT));
                    data.add(cursor.getColumnIndex(DBHelper.KEY_HEIGHT));
                    data.add(cursor.getColumnIndex(DBHelper.KEY_AGE));

                    tableRow = new TableRow(getApplicationContext());
                    tableRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    for(int j = 0; j < 5; j++){
                        textView = new TextView(getApplicationContext());
                        textView.setText(cursor.getColumnName(data.get(j)));
                        textView.setPadding(20,20,20,20);
                        tableRow.addView(textView);
                    }
                    tableLayout.addView(tableRow);


                   do{
                       tableRow = new TableRow(getApplicationContext());
                       tableRow.setLayoutParams(new TableLayout.LayoutParams(
                               TableRow.LayoutParams.MATCH_PARENT,
                               TableRow.LayoutParams.WRAP_CONTENT));
                       for(int j = 0; j < 5; j++){
                           textView = new TextView(getApplicationContext());
                           textView.setText(cursor.getString(data.get(j)));
                           textView.setPadding(20,20,20,20);
                           tableRow.addView(textView);
                       }
                       tableLayout.addView(tableRow);
                    } while(cursor.moveToNext());
                }
                cursor.close();
                break;

            case R.id.buttonClear:
                database.delete(DBHelper.TABLE_NAME, null, null);
                tableLayout.removeAllViews();
                break;
        }
        dbHelper.close();

    }

    public Integer tryParse(Object obj) {
        Integer retVal;
        try {
            retVal = Integer.parseInt((String) obj);
        } catch (NumberFormatException nfe) {
            retVal = 0;
        }
        return retVal;
    }
}
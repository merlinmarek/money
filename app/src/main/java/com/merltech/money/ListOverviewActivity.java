package com.merltech.money;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListOverviewActivity extends Activity {

    SQLiteHelper database = new SQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_overview);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.overviewTableLayout);
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        {
            View row = layoutInflater.inflate(R.layout.overview_row, null);

            TextView id = (TextView) row.findViewById(R.id.idTextView);
            TextView timestamp = (TextView) row.findViewById(R.id.timestampTextView);
            TextView category = (TextView) row.findViewById(R.id.categoryTextView);
            TextView description = (TextView) row.findViewById(R.id.descriptionTextView);
            TextView amount = (TextView) row.findViewById(R.id.amountTextView);

            id.setText("ID");
            timestamp.setText("Date");
            category.setText("Category");
            description.setText("Description");
            amount.setText("Amount");

            row.setBackgroundColor(Color.rgb(216, 216, 255));

            tableLayout.addView(row);
        }

        List<Transaction> transactions = database.getAllTransactions();

        int i = 0;
        for(Transaction transaction : transactions)
        {
            View row = layoutInflater.inflate(R.layout.overview_row, null);

            TextView id = (TextView)row.findViewById(R.id.idTextView);
            TextView timestamp = (TextView)row.findViewById(R.id.timestampTextView);
            TextView category = (TextView)row.findViewById(R.id.categoryTextView);
            TextView description = (TextView)row.findViewById(R.id.descriptionTextView);
            TextView amount = (TextView)row.findViewById(R.id.amountTextView);

            id.setText(Integer.toString(transaction.id));
            timestamp.setText("time");
            category.setText(transaction.category);
            description.setText(transaction.description);
            //amount.setText(Integer.toString(transaction.amount/100) + "." + Integer.toString((transaction.amount - ((transaction.amount/100)*100))) + " €");
            amount.setText(String.format("%d.%02d €", transaction.amount/100, (transaction.amount - ((transaction.amount/100)*100))));
            row.setBackgroundColor(i++%2 == 0 ?  Color.rgb(216, 216, 216) : Color.rgb(230, 230, 230));

            tableLayout.addView(row);
        }
    }
}

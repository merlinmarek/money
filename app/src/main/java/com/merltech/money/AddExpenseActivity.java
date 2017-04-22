package com.merltech.money;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddExpenseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        String category = getIntent().getExtras().getString("category");
        TextView category_label = (TextView)findViewById(R.id.categoryLabel);
        category_label.setText(category);
    }

    public void Confirm(View view)
    {
        Intent resultIntent = new Intent();

        TextView category_label = (TextView)findViewById(R.id.categoryLabel);
        EditText description_text = (EditText)findViewById(R.id.descriptionEditText);
        EditText amount_text = (EditText)findViewById(R.id.amountEditText);

        String category = category_label.getText().toString();
        String description = description_text.getText().toString();
        int amount = 0;
        if(amount_text.getText().toString() != "")
            amount = (int)(Double.parseDouble(amount_text.getText().toString()) * 100);

        Transaction transaction = new Transaction();
        transaction.amount = amount;
        transaction.category = category;
        transaction.description = description;

        resultIntent.putExtra("transaction", transaction);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}

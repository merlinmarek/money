package com.merltech.money;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> categories = new ArrayList<>();
        for(int i = 0; i< 20; ++i)
            categories.add(Integer.toString(i));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.category, categories);
        GridView view = (GridView)findViewById(R.id.categoriesGridView);
        view.setAdapter(adapter);
    }

    public void AddExpense(View view)
    {
        ((TextView)findViewById(R.id.totalTextView)).setText(((Button)view).getText());
    }
}

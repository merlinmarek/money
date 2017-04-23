package com.merltech.money;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    SQLiteHelper database = new SQLiteHelper(this);

    private List<String> categories;
    private ArrayAdapter<String> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categories = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<>(this, R.layout.category, categories);

        GridView view = (GridView)findViewById(R.id.categoriesGridView);
        view.setAdapter(categoryAdapter);
    }

    @Override
    public void onResume() {
        refreshViews();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addCategoryOption:
                Intent intent = new Intent(this, AddCategoryActivity.class);
                startActivityForResult(intent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddExpense(View view)
    {
        Intent intent = new Intent(view.getContext(), AddExpenseActivity.class);
        intent.putExtra("category", ((Button)view).getText().toString());
        startActivityForResult(intent, 0);
    }

    public void ShowListOverview(View view)
    {
        Intent intent = new Intent(view.getContext(), ListOverviewActivity.class);
        startActivityForResult(intent, 0);
    }

    public void ShowChartOverview(View view)
    {
        Intent intent = new Intent(view.getContext(), ChartOverviewActivity.class);
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != RESULT_OK)
            return;
        if (requestCode == 0) // Transaction added
        {
            database.addTransaction((Transaction)data.getExtras().get("transaction"));
        }
        else if(requestCode == 1) // Category added
        {
            String category = data.getExtras().getString("category");
            if(!category.isEmpty())
                database.addCategory(category);
        }

    }

    private void refreshViews()
    {
        int total_amount = database.getTotal();
        TextView total_expenses_label = (TextView)findViewById(R.id.totalTextView);
        total_expenses_label.setText(String.format("%d.%02d €", total_amount/100, (total_amount - ((total_amount/100)*100))));
        categories.clear();
        categories.addAll(database.getCategoriesOrderedByCount());
        categoryAdapter.notifyDataSetChanged();
    }
}

package com.merltech.money;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddCategoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
    }

    public void Confirm(View view)
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("category", ((EditText)findViewById(R.id.categoryEditText)).getText().toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}

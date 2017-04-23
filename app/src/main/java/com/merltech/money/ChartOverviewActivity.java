package com.merltech.money;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class ChartOverviewActivity extends Activity {

    SQLiteHelper database = new SQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_overview);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Prepare image
        ImageView imageView = (ImageView) findViewById(R.id.chartImageView);

        int width = imageView.getMeasuredWidth();
        int height = (int)(width * 0.8f);

        imageView.getLayoutParams().height = height;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        float radius = (Math.min(width, height)/2) * 0.9f;

        float top = height/2 - radius;
        float bottom = height/2 + radius;
        float left = width/2 - radius;
        float right = width/2 + radius;

        RectF rectF = new RectF(left, top, right, bottom);
        // end prepare image

        List<String> categories = database.getCategoriesOrderedByAmount();

        int total = database.getTotal();

        TableLayout tableLayout = (TableLayout)findViewById(R.id.chartTableLayout);
        tableLayout.removeAllViews();

        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        float last_angle = 0.0f;

        for(int i = 0; i < categories.size(); ++i) {
            int category_total = database.getCategoryTotal(categories.get(i));

            float angle = ((float) category_total) / ((float) total) * 360.0f;

            paint.setColor(Color.HSVToColor(new float[]{(360.0f / categories.size())*i, 0.7f, 1.0f}));

            canvas.drawArc(rectF, last_angle, angle, true, paint);

            last_angle = last_angle + angle;

            View row = layoutInflater.inflate(R.layout.chart_row, null);

            ImageView color = (ImageView) row.findViewById(R.id.colorImageView);
            TextView category = (TextView) row.findViewById(R.id.categoryTextView);
            TextView share = (TextView) row.findViewById(R.id.shareTextView);
            TextView sum = (TextView) row.findViewById(R.id.sumTextView);

            ColorDrawable colorDrawable = new ColorDrawable(Color.HSVToColor(new float[]{(360.0f / categories.size())*i, 0.7f, 1.0f}));

            color.setImageDrawable(colorDrawable);
            category.setText(categories.get(i));
            share.setText(String.valueOf(Math.round((((float) category_total) / ((float) total))*100.0f)) + " %");
            sum.setText(String.format("%d.%02d €", category_total/100, (category_total - ((category_total/100)*100))));

            tableLayout.addView(row);
        }
        imageView.setImageBitmap(bitmap);
    }
}

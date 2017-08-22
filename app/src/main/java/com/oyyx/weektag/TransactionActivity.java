package com.oyyx.weektag;


import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import butterknife.BindView;


public class TransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private int mSelectColor;


    private FloatingActionButton fab_date;
    private FloatingActionButton fab_color_picker;
    private FloatingActionButton fab_time;

    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private ColorPickerDialog colorPickerDialog;
    private TimePickerDialog timePickerDialog;

    private TextView tv_date;
    private TextView tv_time;

    @BindView(R.id.title_til)
    TextInputLayout til_title;
    @BindView(R.id.memo_til)
    TextInputLayout til_memo;

    private Toolbar.OnMenuItemClickListener onMenuClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.action_done) {
                //...
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transaction);

        mSelectColor = ContextCompat.getColor(this, R.color.flamingo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("添加事件");

        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(onMenuClick);

        tv_date = (TextView) findViewById(R.id.date_tv);
        tv_time = (TextView) findViewById(R.id.time_tv);

        tv_date.setText(getDate());
        tv_time.setText(getTime());

        fab_color_picker = (FloatingActionButton) findViewById(R.id.color_picker_fab);
        fab_date = (FloatingActionButton) findViewById(R.id.date_fab);
        fab_time = (FloatingActionButton) findViewById(R.id.time_fab);

        fab_color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int[] colors = getResources().getIntArray(R.array.default_rainbow);

                colorPickerDialog = ColorPickerDialog.newInstance(
                        R.string.color_picker_default_title,
                        colors,
                        mSelectColor,
                        5,
                        ColorPickerDialog.SIZE_SMALL,
                        true
                );

                colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        mSelectColor = color;
                        fab_color_picker.setBackgroundTintList(ColorStateList.valueOf(mSelectColor));
                    }
                });

                colorPickerDialog.show(getFragmentManager(),"color_picker");
            }
        });

        fab_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = DatePickerDialog.newInstance(TransactionActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
                datePickerDialog.setYearRange(1999,2037);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(),"datePicker");
            }
        });

        fab_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = TimePickerDialog.newInstance(TransactionActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.setVibrate(false);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getSupportFragmentManager(),"timePicker");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_transaction_menu,menu);
        return true;
    }


    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date());
    }

    private String getTime(){
        return ""+(calendar.get(Calendar.HOUR_OF_DAY)+1)+":00";
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        tv_date.setText(year+"年"+month+"月"+day+"日");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        tv_time.setText(hourOfDay+":"+minute);
    }
}

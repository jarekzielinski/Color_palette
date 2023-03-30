package pl.java.colorpalette;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.Random;


public class ColorActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final String LOGTAG = ColorActivity.class.getSimpleName();//POBIERA NAZWĘ KLASY
    public static final String RED = "red";
    public static final String GREEN = "green";
    public static final String BLUE = "blue";
    public static final String COLOR_IN_HEX_KEY = "color in hex";
    public static final String OLD_COLOR = "old_color";
    private ActionBar actionBar;
    private Random random = new Random();
    LinearLayout linearLayout;
    private int red;
    private int green;
    private int blue;
    private Button generateButton;
    private SeekBar redSeekbar;
    private SeekBar greenSeekbar;
    private SeekBar blueSeekbar;
    private Button saveButton;
    private Intent intent;
    private String oldcolor;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
//        ButterKnife.bind(this);
//        SeekBar seekBar = (SeekBar) findViewById(R.id.red_seekbar);
        linearLayout = (LinearLayout) findViewById(R.id.color_layout);
        generateButton = (Button) findViewById(R.id.generate_button);
        saveButton = (Button) findViewById(R.id.save_button);
        redSeekbar = (SeekBar) findViewById(R.id.red_seekbar);
        greenSeekbar = (SeekBar) findViewById(R.id.green_seekbar);
        blueSeekbar = (SeekBar) findViewById(R.id.blue_seekbar);
        generateButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        redSeekbar.setOnSeekBarChangeListener(this);
        greenSeekbar.setOnSeekBarChangeListener(this);
        blueSeekbar.setOnSeekBarChangeListener(this);
        //Ustawienia strzałki w górę
        actionBar = getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        Log.d(LOGTAG, "onCreate");
        Intent intent=getIntent();
        oldcolor = intent.getStringExtra(OLD_COLOR);
        if(oldcolor!=null){
            int color=Color.parseColor(oldcolor);
            red=Color.red(color);
            green=Color.green(color);
            blue=Color.blue(color);
            updateSeekBarsColor();
            updateColor();
            generateButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.getInt(RED, red);
        outState.getInt(GREEN, green);
        outState.getInt(BLUE, blue);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        red = savedInstanceState.getInt(RED);
        green = savedInstanceState.getInt(GREEN);
        blue = savedInstanceState.getInt(BLUE);
    }

    //Ustawienia strzałki w górę
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    //    @OnClick(R.id.generate_button)
    public void generate() {
        red = random.nextInt(256);
        green = random.nextInt(256);
        blue = random.nextInt(256);
        updateSeekBarsColor();
        updateColor();


    }

    private void updateSeekBarsColor() {
        redSeekbar.setProgress(red);
        greenSeekbar.setProgress(green);
        blueSeekbar.setProgress(blue);
    }

    private void updateColor() {
        int color = Color.rgb(red, green, blue);
        linearLayout.setBackgroundColor(color);
        int textColor=MainActivity.getTextColorFromColor(color);
        TextView tvRed=(TextView)findViewById(R.id.red_textView);
        tvRed.setTextColor(textColor);
        TextView tvGreen=(TextView)findViewById(R.id.green_textView);
        tvGreen.setTextColor(textColor);
        TextView tvBlue=(TextView)findViewById(R.id.blue_textView);
        tvBlue.setTextColor(textColor);
    }

    //    @OnClick(R.id.save_button)
    public void save() {
        //Tworzenie danych do przesłania
        Intent intent = new Intent();
        intent.putExtra(COLOR_IN_HEX_KEY, String.format("#%02X%02X%02X", red, green, blue));
        if(oldcolor!=null)
            intent.putExtra(OLD_COLOR,oldcolor);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOGTAG, "onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOGTAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOGTAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOGTAG, "onPause");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(LOGTAG, "onResume");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.generate_button:
                generate();
                break;
            case R.id.save_button:
                save();
                break;
        }
    }

    //Obsługa SeekBar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.red_seekbar:
                red = progress;
                break;
            case R.id.green_seekbar:
                green = progress;
                break;
            case R.id.blue_seekbar:
                blue = progress;
                break;
        }
        updateColor();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

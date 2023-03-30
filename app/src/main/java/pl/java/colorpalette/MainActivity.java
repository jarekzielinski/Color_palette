package pl.java.colorpalette;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements ColorAdapter.ColorClickListener {

    public static final String LOGTAG = MainActivity.class.getSimpleName();//POBIERA NAZWĘ KLASY
    public static final int REQUEST_CODE_CREATE = 1;
    public static final int REQUEST_CODE_EDIT = 2;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    ColorAdapter colorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.colorRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        colorAdapter=new ColorAdapter(getLayoutInflater(), PreferenceManager.getDefaultSharedPreferences(this));
        recyclerView.setAdapter(colorAdapter);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addColor();
            }
        });
        colorAdapter.setColorClickListener(this);
    Log.d(LOGTAG,"onCreate");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOGTAG,"onDestroy");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOGTAG,"onStart");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOGTAG,"onStop");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOGTAG,"onPause");
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(LOGTAG,"onResume");
    }

    private void addColor() {
        Intent intent=new Intent(MainActivity.this, ColorActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CREATE);//Activity z oczekiwaniem rezultatu
    }

    //Metoda odbierająca dane
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
        if(requestCode==REQUEST_CODE_CREATE) {
            String colorHex = data.getStringExtra(ColorActivity.COLOR_IN_HEX_KEY);
            final int position=colorAdapter.add(colorHex);
            Snackbar.make(fab, getString(R.string.new_color_created, colorHex), Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           colorAdapter.remove(position);
                           colorAdapter.notifyItemRemoved(position);
                        }
                    }).show();
//            colorAdapter.setColorClickListener(this);
        }else if(requestCode==REQUEST_CODE_EDIT){
            String colorInHex=data.getStringExtra(ColorActivity.COLOR_IN_HEX_KEY);
            String oldColor=data.getStringExtra(ColorActivity.OLD_COLOR);
            colorAdapter.replace(oldColor,colorInHex);
        }
            //Ustawienie usuwania elementów
            ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    int position=viewHolder.getAdapterPosition();
                    colorAdapter.remove(position);
                }
            };
            ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);//Odświeżanie listy
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button_selector, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            addColor();
        }
        if(id==R.id.action_clear){
            colorAdapter.clear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onColorClicked(String colorInHex) {
        Intent intent=new Intent(this,ColorActivity.class);
        intent.putExtra(ColorActivity.OLD_COLOR,colorInHex);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    public static int getTextColorFromColor(int color){
        return new Palette.Swatch(color,1).getTitleTextColor();
    }
}

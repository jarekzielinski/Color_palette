package pl.java.colorpalette;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

class ColorAdapter extends RecyclerView.Adapter<ColorViewHolder> {


    public static final String COLORS_KEY = "colors";
    List<String> colors = new ArrayList<>();
    LayoutInflater layoutInflater;
    private SharedPreferences sharedPreferences;
    ColorClickListener colorClickListener;

    ColorAdapter(LayoutInflater layoutInflater, SharedPreferences sharedPreferences) {
        this.layoutInflater = layoutInflater;

        this.sharedPreferences = sharedPreferences;
        String colorsJson = sharedPreferences.getString(COLORS_KEY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(colorsJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                colors.add(i, jsonArray.getString(i));
            }
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);
        return new ColorViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ColorViewHolder colorViewHolder, int i) {
        String colorHex = colors.get(i);
        colorViewHolder.setColor(colorHex);
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public int add(String color) {
        colors.add(color);
        int position = colors.size() - 1;
        storeInPreferences();
        return position;
    }

    private void storeInPreferences() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < colors.size(); i++) {

                jsonArray.put(i, colors.get(i));
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(COLORS_KEY, jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void remove(int position) {
        colors.remove(position);
        storeInPreferences();
    }

    public void setColorClickListener(ColorClickListener colorClickListener) {
        this.colorClickListener = colorClickListener;
    }

    public void clicked(int adapterPosition) {
        if (colorClickListener != null) {
            colorClickListener.onColorClicked(colors.get(adapterPosition));
        }
    }

    public void replace(String oldColor, String colorInHex) {
        int indexOf = colors.indexOf(oldColor);
        colors.set(indexOf, colorInHex);
        notifyItemChanged(indexOf);
        storeInPreferences();
    }

    public void clear() {
        colors.clear();
        notifyDataSetChanged();
        sharedPreferences.edit().clear().apply();
    }

    public interface ColorClickListener {
        void onColorClicked(String colorInHex);
    }
}

class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    String color;
    TextView textView;
    private final ColorAdapter colorAdapter;
    private int backgroundColor;

    public ColorViewHolder(View itemView, ColorAdapter colorAdapter) {
        super(itemView);
        this.colorAdapter = colorAdapter;
        textView = (TextView) itemView;
        textView.setOnClickListener(this);
    }

    public void setColor(String color) {
        this.color = color;
        textView.setText(color);
        int backgroundColor = Color.parseColor(color);
        textView.setBackgroundColor(backgroundColor);
        textView.setTextColor(MainActivity.getTextColorFromColor(backgroundColor));
    }

    public String getColor() {
        return color;
    }

    @Override
    public void onClick(View v) {
        colorAdapter.clicked(getAdapterPosition());
    }
}

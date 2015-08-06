package com.yahoo.googleimagesearch.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.yahoo.googleimagesearch.R;

/**
 * Created by jhkao on 8/4/15.
 */
public class ColorSpinnerAdapter extends ArrayAdapter {

    // I don't like this...
    private static String[] COLOR_VALUES = {
            "#00FFFFFF",
            "#000",
            "#00F",
            "#8B4513",
            "#AAA",
            "#0F0",
            "#D2691E",
            "#FFC0CB",
            "#800080",
            "#F00",
            "#028482",
            "#FFF",
            "#FF0"
    };

    public ColorSpinnerAdapter(Context context) {
        super(context, R.layout.color_spinner_item, context.getResources().getStringArray(R.array.image_colors));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.color_spinner_item, parent, false);
        }

        convertView.setBackground(Drawable.createFromPath(COLOR_VALUES[position]));

        return convertView;
    }
}

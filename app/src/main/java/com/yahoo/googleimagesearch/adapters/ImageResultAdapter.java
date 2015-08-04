package com.yahoo.googleimagesearch.adapters;

import android.content.Context;
import android.media.Image;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.googleimagesearch.R;
import com.yahoo.googleimagesearch.models.ImageResult;

import java.util.List;

/**
 * Created by jhkao on 8/3/15.
 */
public class ImageResultAdapter extends ArrayAdapter<ImageResult> {

    public ImageResultAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        ivImage.setImageResource(0);
        Picasso.with(getContext()).load(imageInfo.thumbUrl).into(ivImage);
        tvTitle.setText(Html.fromHtml(imageInfo.title));

        return convertView;
    }
}

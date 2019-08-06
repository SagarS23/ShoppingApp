package com.seasapps.shoppingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.activities.ProductDetailsActivity;
import com.seasapps.shoppingapp.utils.Utils;

import java.io.ByteArrayOutputStream;

public class CarouselAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;


    private String[] names = {
            "Nestle Cerelac",
            "Britannia Toast Tea",
            "Cadbury Celebration Pack",
            "Kellogg's Corn Flakes",
            "Kellogg's Chocos",
            "Kellogg's Fruit & Nut",
            "Safal Sweet Corn",
            "Sunfeast Oat's",
            "Tasties Biscuits",
            "Knorr Tomato Soup"
    };

    public CarouselAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Utils.mResources.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.carousel_offers_layout, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(Utils.mResources[position]);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mContext.startActivity(new Intent(mContext, ProductDetailsActivity.class)
                        .putExtra("name", "" + names[position])
                        .putExtra("position", "" + position)
                        .putExtra("from", "carousel"));

            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}

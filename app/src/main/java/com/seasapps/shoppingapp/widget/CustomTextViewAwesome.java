package com.seasapps.shoppingapp.widget;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class CustomTextViewAwesome extends TextView {
    public CustomTextViewAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/fontawesome-webfont.ttf"));
    }
}

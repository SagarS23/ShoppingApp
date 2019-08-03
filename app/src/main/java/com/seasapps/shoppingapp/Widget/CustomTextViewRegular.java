package com.seasapps.shoppingapp.Widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Custom text view with custom font
 */

public class CustomTextViewRegular extends TextView {
    public CustomTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Regular.ttf"));
    }
}

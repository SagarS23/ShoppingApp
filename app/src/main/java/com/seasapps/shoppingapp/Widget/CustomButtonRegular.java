package com.seasapps.shoppingapp.Widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Custom button with custom font
 */

public class CustomButtonRegular extends Button {
    public CustomButtonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Regular.ttf"));
    }
}

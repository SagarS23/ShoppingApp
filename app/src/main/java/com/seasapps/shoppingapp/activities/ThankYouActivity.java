package com.seasapps.shoppingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.widget.CustomButtonRegular;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Thank you Activity
 * redirecting user to dashboard
 */

public class ThankYouActivity extends AppCompatActivity {

    Context context;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_continue)
    CustomButtonRegular btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        ButterKnife.bind(this);

        context = ThankYouActivity.this;
        toolbar.setTitle("");

    }

    @OnClick(R.id.btn_continue)
    public void onClickButton() {

        MainActivity.moreProductsModels.clear();
        startActivity(new Intent(context, MainActivity.class));
        finish();

    }
}

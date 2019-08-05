package com.seasapps.shoppingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.ShoppingApp;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.wangyuwei.particleview.ParticleView;

/**
 * Splash Activity
 */

public class SplashActivity extends AppCompatActivity {

    ShoppingApp shoppingApp;
    Context context;
    @BindView(R.id.part)
    ParticleView mParticleView;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        shoppingApp = (ShoppingApp) getApplicationContext();
        context = SplashActivity.this;

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //Starting particleView for animation
        mParticleView.startAnim();
        mParticleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {

                if (auth.getCurrentUser() != null) {
                    // If user is already registered then
                    // redirect to Main Activity
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    // If user is not registered then
                    // redirect user to Login Activity
                    startActivity(new Intent(context, LoginActivity.class));
                    finish();
                }

            }
        });
    }
}

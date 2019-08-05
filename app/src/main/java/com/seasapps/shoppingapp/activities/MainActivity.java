package com.seasapps.shoppingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.ShoppingApp;
import com.seasapps.shoppingapp.adapter.NavigationAdapter;
import com.seasapps.shoppingapp.interfaces.PositionClickListener;
import com.seasapps.shoppingapp.model.NavigationModel;
import com.seasapps.shoppingapp.utils.Utils;
import com.seasapps.shoppingapp.widget.CustomButtonRegular;
import com.seasapps.shoppingapp.widget.CustomTextViewRegular;
import com.seasapps.shoppingapp.widget.MaterialProgressBar.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main Activity where Dashboard is implemented
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.rv_nav_drawer)
    RecyclerView rv_nav_drawer;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.rootFrame)
    RelativeLayout rootFrame;

    List<NavigationModel> menuList = new ArrayList<>();
    NavigationAdapter navigationAdapter;
    ShoppingApp shoppingApp;
    Context context;
    CustomProgressDialog customProgressDialog;
    private CustomTextViewRegular tvEmailID;
    private View mHeaderView;
    private long lastPress;
    private Toast backpressToast;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        shoppingApp = (ShoppingApp) getApplicationContext();
        context = MainActivity.this;
        customProgressDialog = new CustomProgressDialog(context);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
            }
        });

        mHeaderView = navigationView.getHeaderView(0);
        tvEmailID = (CustomTextViewRegular) mHeaderView.findViewById(R.id.tv_email);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvEmailID.setText(user.getEmail());
        }

        initViews();

    }

    private void initViews() {

        menuList.add(new NavigationModel(R.drawable.ic_logout_menu, getResources().getString(R.string.logout)));
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_nav_drawer.setLayoutManager(LayoutManager);
        rv_nav_drawer.setItemAnimator(new DefaultItemAnimator());

        navigationAdapter = new NavigationAdapter(MainActivity.this, new PositionClickListener() {
            @Override
            public void itemClicked(String s) {
                int ch = Integer.parseInt(s) + 1;
                switch (ch) {
                    case 1:
                        drawer.closeDrawers();
                        showLogoutDialog();
                        break;
                }
                if (drawer.isDrawerOpen(Gravity.START))
                    drawer.closeDrawers();
            }
        }, menuList);
        rv_nav_drawer.setAdapter(navigationAdapter);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                rootFrame.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }

        };
        drawer.addDrawerListener(actionBarDrawerToggle);

    }

    private void showLogoutDialog() {

        final AlertDialog dialogSelectRole;
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialog = inflater.inflate(R.layout.dialog_logout, null);
        dialogSelectRole = new AlertDialog.Builder(context).create();
        dialogSelectRole.setView(dialog);
        dialogSelectRole.setCancelable(false);

        WindowManager.LayoutParams lp = dialogSelectRole.getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        dialogSelectRole.getWindow().setAttributes(lp);
        dialogSelectRole.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogSelectRole.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSelectRole.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        CustomButtonRegular btnYes = dialog.findViewById(R.id.btn_yes);
        CustomButtonRegular btnNo = dialog.findViewById(R.id.btn_no);
        ImageView ivClose = dialog.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectRole.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();
                dialogSelectRole.dismiss();
                Utils.showSuccessToast(context, getResources().getString(R.string.logout_successfully));
                startActivity(new Intent(context, LoginActivity.class));
                finish();

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectRole.dismiss();
            }
        });

        dialogSelectRole.show();

    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPress > 5000) {
            backpressToast = Toast.makeText(getBaseContext(), getResources().getString(R.string.press_back_again_to_exit), Toast.LENGTH_LONG);
            backpressToast.show();
            lastPress = currentTime;
        } else {
            if (backpressToast != null) backpressToast.cancel();
            super.onBackPressed();
        }
    }

}

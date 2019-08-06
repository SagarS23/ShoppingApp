package com.seasapps.shoppingapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.ShoppingApp;
import com.seasapps.shoppingapp.adapter.CarouselAdapter;
import com.seasapps.shoppingapp.adapter.MoreProductsAdapter;
import com.seasapps.shoppingapp.adapter.NavigationAdapter;
import com.seasapps.shoppingapp.adapter.PagerContainer;
import com.seasapps.shoppingapp.interfaces.PositionClickListener;
import com.seasapps.shoppingapp.model.MoreProductsModel;
import com.seasapps.shoppingapp.model.NavigationModel;
import com.seasapps.shoppingapp.utils.GeoAddress;
import com.seasapps.shoppingapp.utils.Utils;
import com.seasapps.shoppingapp.widget.CustomButtonRegular;
import com.seasapps.shoppingapp.widget.CustomTextViewRegular;
import com.seasapps.shoppingapp.widget.MaterialProgressBar.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Main Activity where Dashboard is implemented
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    public static final String TAG = MainActivity.class.getSimpleName();

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
    @BindView(R.id.pager_container)
    PagerContainer mContainer;
    @BindView(R.id.indicator)
    CircleIndicator indicator;

    List<NavigationModel> menuList = new ArrayList<>();
    public static List<MoreProductsModel> moreProductsModels = new ArrayList<>();
    NavigationAdapter navigationAdapter;
    ShoppingApp shoppingApp;
    Context context;
    CustomProgressDialog customProgressDialog;
    @BindView(R.id.rv_more_products)
    RecyclerView rvMoreProducts;

    private CustomTextViewRegular tvEmailID;
    private View mHeaderView;
    private long lastPress;
    private Toast backpressToast;
    private FirebaseAuth auth;
    ViewPager pager;
    PagerAdapter adapter;
    MoreProductsAdapter moreProductsAdapter;

    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Location location;
    private double longitude = 0.0;
    private double latitude = 0.0;

    private String city = "";
    private String country = "";
    private String state = "";

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
            shoppingApp.getPreferences().setLoggedInUser(user.getEmail());
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();

            startLocationUpdates();
        }

        //Initialize navigation drawer with handle click event
        initViews();

        initializeCarousel();

        //Grid
        RecyclerView.LayoutManager mLayoutManagerChef = new GridLayoutManager(context, 2);
        rvMoreProducts.setLayoutManager(mLayoutManagerChef);
        rvMoreProducts.setItemAnimator(new DefaultItemAnimator());

        addProducts();

        appPermissions();

    }

    private void appPermissions() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            CheckGPS();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void startLocationUpdates() {

        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        if (mGoogleApiClient.isConnected()) {
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    }
                },
                Looper.myLooper());

    }

    private void CheckGPS() {

        mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result
                        .getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling
                            // startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have
                        // no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }
        });
    }

    private void addProducts() {

        moreProductsModels.add(new MoreProductsModel(R.drawable.ic_kissan_jam, "Kisan Jam"));
        moreProductsModels.add(new MoreProductsModel(R.drawable.ic_noodles, "Maggy Noodles"));
        moreProductsModels.add(new MoreProductsModel(R.drawable.ic_hearts_biscuits, "Hearts Biscuits"));
        moreProductsModels.add(new MoreProductsModel(R.drawable.ic_oreo, "Oreo Biscuits"));
        moreProductsModels.add(new MoreProductsModel(R.drawable.ic_aloo_tikki, "Aloo Tikki"));
        moreProductsModels.add(new MoreProductsModel(R.drawable.ic_peanut, "Peanut Butter"));

        moreProductsAdapter = new MoreProductsAdapter(context, moreProductsModels);
        rvMoreProducts.setAdapter(moreProductsAdapter);
    }

    private void initializeCarousel() {

        pager = mContainer.getViewPager();
        adapter = new CarouselAdapter(this);
        pager.setAdapter(adapter);
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(adapter.getCount());
        //A little space between pages
        pager.setPageMargin(5);
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.setClipChildren(false);
        indicator.setViewPager(pager);

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
        moreProductsModels.clear();
        GeoAddress.address = "";
        auth.signOut();
    }

    @Override
    public void onDestroy() {

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
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

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //When our map is ready we use our location to animate
        //the camera and create a marker showing where we are.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else if (mGoogleApiClient.isConnected()) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            city = GeoAddress.getAddressData(context, latitude, longitude);

            state = GeoAddress.stateName;

            country = GeoAddress.countryName;

            if (!GeoAddress.address.equalsIgnoreCase("")){
                shoppingApp.getPreferences().setLat(String.valueOf(latitude));
                shoppingApp.getPreferences().setLng(String.valueOf(longitude));
            }

        } else {
            Toast.makeText(context, "Google Api is not connected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended" + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed" + connectionResult);
    }
}

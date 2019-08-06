package com.seasapps.shoppingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.ShoppingApp;
import com.seasapps.shoppingapp.adapter.OffersAdapter;
import com.seasapps.shoppingapp.model.OffersModel;
import com.seasapps.shoppingapp.utils.GeoAddress;
import com.seasapps.shoppingapp.utils.Utils;
import com.seasapps.shoppingapp.widget.CustomTextViewRegular;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Product Details Activity
 * from user is ordering products
 */

public class ProductDetailsActivity extends AppCompatActivity {

    ShoppingApp shoppingApp;
    Context context;
    List<OffersModel> offersModels = new ArrayList<>();
    OffersAdapter offersAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_address)
    CustomTextViewRegular tvAddress;
    @BindView(R.id.iv_product)
    ImageView ivProduct;
    @BindView(R.id.tv_product_name)
    CustomTextViewRegular tvProductName;
    @BindView(R.id.rv_offers)
    RecyclerView rvOffers;

    private String name = "";
    private String position = "";
    private String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);

        shoppingApp = (ShoppingApp) getApplicationContext();
        context = ProductDetailsActivity.this;

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        RecyclerView.LayoutManager mLayoutManagerChef = new LinearLayoutManager(context);
        rvOffers.setLayoutManager(mLayoutManagerChef);
        rvOffers.setItemAnimator(new DefaultItemAnimator());

        //Getting product details
        from = getIntent().getStringExtra("from");
        if (from.equalsIgnoreCase("more_products")) {

            name = getIntent().getStringExtra("name");
            position = getIntent().getStringExtra("position");
            tvProductName.setText(name);
            ivProduct.setImageResource(MainActivity.moreProductsModels.get(Integer.parseInt(position)).getIcon());

        } else {
            name = getIntent().getStringExtra("name");
            position = getIntent().getStringExtra("position");
            tvProductName.setText(name);
            ivProduct.setImageResource(Utils.mResources[Integer.parseInt(position)]);
        }

        loadOffers();

    }

    private void loadOffers() {

        offersModels.add(new OffersModel("230", "FREE Delivery", "Navkar Stores", "75% positive(54 ratings)"));
        offersModels.add(new OffersModel("200", "+50 Delivery", "Shubham Kirana Stores", "80% positive(46 ratings)"));
        offersModels.add(new OffersModel("190", "FREE Delivery", "Patil Stores", "62% positive(18 ratings)"));
        offersModels.add(new OffersModel("250", "+30 Delivery", "Jai Super Market", "87% positive(31 ratings)"));
        offersModels.add(new OffersModel("210", "+20 Delivery", "Big Bazaar", "96% positive(94 ratings)"));
        offersModels.add(new OffersModel("180", "+30 Delivery", "Manav Stores", "68% positive(20 ratings)"));

        offersAdapter = new OffersAdapter(context, offersModels, name);
        rvOffers.setAdapter(offersAdapter);

    }


    @OnClick(R.id.tv_address)
    public void onAddressClick() {

        startActivity(new Intent(context, MapActivity.class));

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!shoppingApp.getPreferences().getLat().equalsIgnoreCase("")) {

            double lat = Double.parseDouble(shoppingApp.getPreferences().getLat());
            double lng = Double.parseDouble(shoppingApp.getPreferences().getLng());

            GeoAddress.getAddressData(context, lat, lng);

            tvAddress.setText(GeoAddress.address);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.seasapps.shoppingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.ShoppingApp;
import com.seasapps.shoppingapp.activities.LoginActivity;
import com.seasapps.shoppingapp.activities.ProductDetailsActivity;
import com.seasapps.shoppingapp.activities.ThankYouActivity;
import com.seasapps.shoppingapp.model.OffersModel;
import com.seasapps.shoppingapp.utils.GeoAddress;
import com.seasapps.shoppingapp.utils.Utils;
import com.seasapps.shoppingapp.widget.CustomButtonRegular;
import com.seasapps.shoppingapp.widget.CustomTextViewRegular;
import com.seasapps.shoppingapp.widget.MaterialProgressBar.CustomProgressDialog;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private Context context;
    private List<OffersModel> data;
    private int pos = 0;
    private String productName = "";
    private FirebaseFirestore mFirebaseDatabase;
    private ShoppingApp shoppingApp;
    private CustomProgressDialog customProgressDialog;

    public OffersAdapter(Context context, List<OffersModel> _data, String _name) {
        this.context = context;
        this.data = _data;
        this.productName = _name;
        mFirebaseDatabase = FirebaseFirestore.getInstance();
        shoppingApp = (ShoppingApp) context.getApplicationContext();
        customProgressDialog = new CustomProgressDialog(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_offers_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvPrice.setText(data.get(position).getPrice());
        holder.tvDelivery.setText(data.get(position).getDelivery());
        holder.tvSellerName.setText(data.get(position).getSeller_name());
        holder.tvRating.setText(data.get(position).getPositive_rating());

        holder.mrlBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (GeoAddress.address.equalsIgnoreCase("")) {

                    Utils.showErrorToast(context, context.getString(R.string.please_select_address));

                } else {
                    pos = position;
                    //Show dialog for order confirmation
                    showConfirmationDialog();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_price)
        CustomTextViewRegular tvPrice;
        @BindView(R.id.tv_delivery)
        CustomTextViewRegular tvDelivery;
        @BindView(R.id.tv_seller_name)
        CustomTextViewRegular tvSellerName;
        @BindView(R.id.tv_rating)
        CustomTextViewRegular tvRating;
        @BindView(R.id.cv)
        CardView cv;
        @BindView(R.id.mrl_buy)
        MaterialRippleLayout mrlBuy;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void showConfirmationDialog() {

        final AlertDialog dialogSelectRole;
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialog = inflater.inflate(R.layout.dialog_confirm, null);
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

                dialogSelectRole.dismiss();
                customProgressDialog.show();
                //Storing order data to firestore database
                storeUserOrder();

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

    private void storeUserOrder() {

        HashMap<String, String> orderData = new HashMap<>();
        orderData.put("User Email", "" + shoppingApp.getPreferences().getLoggedInUser());
        orderData.put("Product Price", "" + data.get(pos).getPrice());
        orderData.put("Product Delivery", "" + data.get(pos).getDelivery());
        orderData.put("Seller Name", "" + data.get(pos).getSeller_name());
        orderData.put("Product Rating", "" + data.get(pos).getPositive_rating());
        orderData.put("Shipping Address", "" + GeoAddress.address);
        orderData.put("Product Name", productName);

        HashMap<String, Object> mapOrder = new HashMap<>();
        mapOrder.put("Order Data " + Utils.getCurrentTimeStamp(), orderData);

        mFirebaseDatabase
                .collection("Users")
                .document("" + shoppingApp.getPreferences().getLoggedInUser())
                .set(mapOrder, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Profile", "DocumentSnapshot successfully written!");
                        customProgressDialog.dismiss();
                        Utils.showSuccessToast(context, context.getString(R.string.data_stored_successfully));
                        context.startActivity(new Intent(context, ThankYouActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customProgressDialog.dismiss();
                        Log.w("Profile", "Error writing document", e);
                    }
                });

    }
}

package com.seasapps.shoppingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.activities.ProductDetailsActivity;
import com.seasapps.shoppingapp.model.MoreProductsModel;
import com.seasapps.shoppingapp.widget.CustomTextViewRegular;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreProductsAdapter extends RecyclerView.Adapter<MoreProductsAdapter.ViewHolder> {

    private Context context;
    private List<MoreProductsModel> data;

    public MoreProductsAdapter(Context context, List<MoreProductsModel> _data) {
        this.context = context;
        this.data = _data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_more_products_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvName.setText(data.get(position).getName());
        holder.ivProduct.setImageResource(data.get(position).getIcon());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, ProductDetailsActivity.class)
                        .putExtra("name", "" + data.get(position).getName())
                        .putExtra("position", "" + position)
                        .putExtra("from", "more_products"));

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_product_name)
        CustomTextViewRegular tvName;
        @BindView(R.id.cv)
        CardView cv;
        @BindView(R.id.iv_product)
        ImageView ivProduct;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

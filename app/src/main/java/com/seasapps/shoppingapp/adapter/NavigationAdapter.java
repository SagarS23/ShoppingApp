package com.seasapps.shoppingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.interfaces.PositionClickListener;
import com.seasapps.shoppingapp.model.NavigationModel;
import com.seasapps.shoppingapp.widget.CustomTextViewRegular;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.MyViewHolder> {

    Context context;
    List<NavigationModel> menulist;
    PositionClickListener positionListner;


    public NavigationAdapter(Context context, PositionClickListener positionListner, List<NavigationModel> menulist) {
        this.context = context;
        this.menulist = menulist;
        this.positionListner = positionListner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_drawer_menu, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.ivIcon.setImageDrawable(context.getResources().getDrawable(menulist.get(position).getIcon()));
        holder.tvMenu.setText(menulist.get(position).getMenu());

        holder.mrlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionListner.itemClicked(position + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return menulist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_menu)
        CustomTextViewRegular tvMenu;
        @BindView(R.id.mrl_menu)
        MaterialRippleLayout mrlMenu;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

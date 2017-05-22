package com.actiknow.clearsale.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.activity.PropertyDetailActivity;
import com.actiknow.clearsale.model.AllProperty;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;


public class AllPropertyListAdapter extends RecyclerView.Adapter<AllPropertyListAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<AllProperty> AllPropertyList = new ArrayList<AllProperty>();

    public AllPropertyListAdapter(Activity activity, List<AllProperty> AllPropertyList) {
        this.activity = activity;
        this.AllPropertyList = AllPropertyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.listview_item_all_property2, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final AllProperty allProperty = AllPropertyList.get(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.ivImage.setClipToOutline(true);
        }

        if (allProperty.is_offer()) {
            holder.tvAvailable.setVisibility(View.VISIBLE);
            holder.tvAvailable.setText("Available");
            holder.tvAcceptingOffer.setVisibility(View.VISIBLE);
            holder.tvAvailable.setBackgroundColor(activity.getResources().getColor(R.color.mb_green_dark));
        } else {
            holder.tvAvailable.setText("Sold");
            holder.tvAvailable.setVisibility(View.VISIBLE);
            holder.tvAcceptingOffer.setVisibility(View.GONE);
            holder.tvAvailable.setBackgroundColor(activity.getResources().getColor(R.color.mb_red));
        }

        holder.tvAddress1.setText(allProperty.getAddress1()+", "+allProperty.getAddress2());
        holder.tvAddress2.setText(allProperty.getAddress2());
        holder.tvPropertySize.setText(allProperty.getNumber_bedroom() + ", " + allProperty.getNumber_bathroom() + ", " + allProperty.getSize() + ", " + allProperty.getYear_build());


        final ViewHolder tempholder = holder;

        Glide.with(activity)
                .load(allProperty.getImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        tempholder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        tempholder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return AllPropertyList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvAvailable;
        TextView tvAcceptingOffer;
        TextView tvAddress1;
        TextView tvAddress2;
        TextView tvPropertySize;

        ImageView ivImage;
        ProgressBar progressBar;


        public ViewHolder(View view) {
            super(view);
            tvAvailable = (TextView) view.findViewById(R.id.tvAvailable);
            tvAcceptingOffer = (TextView) view.findViewById(R.id.tvAcceptingOffer);
            tvAddress1 = (TextView) view.findViewById(R.id.tvAddress1);
            tvAddress2 = (TextView) view.findViewById(R.id.tvAddress2);
            tvPropertySize = (TextView) view.findViewById(R.id.tvPropertyName);

            ivImage = (ImageView) view.findViewById(R.id.ivImage);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AllProperty allProperty = AllPropertyList.get(getLayoutPosition());
            Intent intent=new Intent(activity, PropertyDetailActivity.class);
            activity.startActivity(intent);

            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
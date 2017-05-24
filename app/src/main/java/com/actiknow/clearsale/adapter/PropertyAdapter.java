package com.actiknow.clearsale.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.activity.PropertyDetailActivity;
import com.actiknow.clearsale.model.Property;
import com.actiknow.clearsale.utils.SetTypeFace;
import com.actiknow.clearsale.utils.Utils;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;


public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Property> propertyList = new ArrayList<Property> ();
    
    public PropertyAdapter (Activity activity, List<Property> propertyList) {
        this.activity = activity;
        this.propertyList = propertyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate (R.layout.list_item_property, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Property property = propertyList.get (position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.ivImage.setClipToOutline(true);
        }
    
    
        switch (property.getStatus ()) {
            case 0:
                Drawable img = activity.getResources ().getDrawable (R.drawable.circle_red);
                img.setBounds (0, 0, 30, 30);
                holder.tvStatus.setCompoundDrawables (img, null, null, null);
                holder.tvStatus.setText ("Sold");
                break;
            case 1:
                Drawable img2 = activity.getResources ().getDrawable (R.drawable.circle_green);
                img2.setBounds (0, 0, 30, 30);
                holder.tvStatus.setCompoundDrawables (img2, null, null, null);
                holder.tvStatus.setText ("Available");
                break;
            case 2:
                Drawable img3 = activity.getResources ().getDrawable (R.drawable.circle_yellow);
                img3.setBounds (0, 0, 30, 30);
                holder.tvStatus.setCompoundDrawables (img3, null, null, null);
                holder.tvStatus.setText ("Pending");
                break;
        }
    
        if (property.is_offer ()) {
            holder.tvAcceptingOffer.setVisibility(View.VISIBLE);
        } else {
            holder.tvAcceptingOffer.setVisibility(View.GONE);
        }
    
        holder.tvStatus.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvAcceptingOffer.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvAddress1.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvAddress2.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvPropertyRate.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvBuiltYear.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvBeds.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvBaths.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvSqFeet.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tv1.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tv2.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tv3.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvSliderPosition.setTypeface (SetTypeFace.getTypeface (activity));
    
    
        holder.tvAddress1.setText (property.getAddress1 ());
        holder.tvAddress2.setText (property.getAddress2 ());
        holder.tvBeds.setText (property.getBedroom ());
        holder.tvBaths.setText (property.getBathroom ());
        holder.tvSqFeet.setText (property.getArea ());
        holder.tvBuiltYear.setText ("Year built : " + property.getYear_built ());
    
    
        holder.slider.removeAllSliders ();
        for (int i = 0; i < property.getImageList ().size (); i++) {
            String image = property.getImageList ().get (i);
            DefaultSliderView defaultSliderView = new DefaultSliderView (activity);
            defaultSliderView
                    .image (image)
                    .setScaleType (BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener (new BaseSliderView.OnSliderClickListener () {
                        @Override
                        public void onSliderClick (BaseSliderView slider) {
                            Utils.showToast (activity, "Property id" + property.getId (), true);
                            Intent intent = new Intent (activity, PropertyDetailActivity.class);
                            activity.startActivity (intent);
                            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });
        
            defaultSliderView.bundle (new Bundle ());
            // defaultSliderView.getBundle ().putString ("extra", String.valueOf (s));
            holder.slider.addSlider (defaultSliderView);
        }
        holder.slider.setIndicatorVisibility (PagerIndicator.IndicatorVisibility.Invisible);
        holder.slider.setPresetTransformer (SliderLayout.Transformer.Default);
        holder.slider.setCustomAnimation (new DescriptionAnimation ());
//        holder.slider.setDuration (5000);
        holder.slider.addOnPageChangeListener (new ViewPagerEx.OnPageChangeListener () {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
//                Log.e ("karman page " + property.getId (), "state " + position);
//                holder.rlSliderIndicator.setVisibility (View.VISIBLE);
//                holder.tvSliderPosition.setText (position + " of " + property.getImageList ().size ());
            }
        
            @Override
            public void onPageSelected (int position) {
                holder.tvSliderPosition.setText ((position + 1) + " of " + property.getImageList ().size ());
            }
        
            @Override
            public void onPageScrollStateChanged (int state) {
                switch (state) {
                    case 0:
                        final Handler handler = new Handler ();
                        handler.postDelayed (new Runnable () {
                            @Override
                            public void run () {
                                holder.rlSliderIndicator.setVisibility (View.GONE);
                                holder.rlFooter.setVisibility (View.VISIBLE);
                            }
                        }, 500);
                        break;
                    case 1:
                        holder.rlFooter.setVisibility (View.GONE);
                        holder.rlSliderIndicator.setVisibility (View.VISIBLE);
                        break;
                    case 2:
                        break;
                }
//                if (property.getId () == 1){
//                    Log.e ("karman " + property.getId (), "state " + state);
//                }
            }
        });
// holder.slider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
        holder.slider.setPresetIndicator (SliderLayout.PresetIndicators.Center_Bottom);
    
    
        final ViewHolder tempholder = holder;
/*
        Glide.with(activity)
                .load(property.g))
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
        
        */
    }

    @Override
    public int getItemCount() {
        return propertyList.size ();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvStatus;
        TextView tvAcceptingOffer;
        TextView tvAddress1;
        TextView tvAddress2;
        TextView tvPropertyRate;
        TextView tvBuiltYear;
        TextView tvBeds;
        TextView tvBaths;
        TextView tvSqFeet;
        ImageView ivFavourite;
    
        ImageView ivImage;
        ProgressBar progressBar;
    
        SliderLayout slider;
        RelativeLayout rlHeader;
        RelativeLayout rlFooter;
        RelativeLayout rlSliderIndicator;
        TextView tvSliderPosition;
    
        TextView tv1;
        TextView tv2;
        TextView tv3;
    
    
        public ViewHolder(View view) {
            super(view);
            rlSliderIndicator = (RelativeLayout) view.findViewById (R.id.rlSliderIndicator);
            tvSliderPosition = (TextView) view.findViewById (R.id.tvSliderPosition);
            rlHeader = (RelativeLayout) view.findViewById (R.id.rlHeader);
            rlFooter = (RelativeLayout) view.findViewById (R.id.rlFooter);
            slider = (SliderLayout) view.findViewById (R.id.slider);
            tvStatus = (TextView) view.findViewById (R.id.tvStatus);
            tvAcceptingOffer = (TextView) view.findViewById(R.id.tvAcceptingOffer);
            tvAddress1 = (TextView) view.findViewById(R.id.tvAddress1);
            tvAddress2 = (TextView) view.findViewById(R.id.tvAddress2);
            tvBeds = (TextView) view.findViewById (R.id.tvBedroom);
            tvBaths = (TextView) view.findViewById (R.id.tvBathroom);
            tvSqFeet = (TextView) view.findViewById (R.id.tvSqFeet);
            tvPropertyRate = (TextView) view.findViewById (R.id.tvPropertyRate);
            tvBuiltYear = (TextView) view.findViewById (R.id.tvBuildYear);
    
    
            tv1 = (TextView) view.findViewById (R.id.tv1);
            tv2 = (TextView) view.findViewById (R.id.tv2);
            tv3 = (TextView) view.findViewById (R.id.tv3);
    
            ivImage = (ImageView) view.findViewById(R.id.ivImage);
            ivFavourite = (ImageView) view.findViewById (R.id.ivFavourite);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Property property = propertyList.get (getLayoutPosition ());
            Intent intent = new Intent (activity, PropertyDetailActivity.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
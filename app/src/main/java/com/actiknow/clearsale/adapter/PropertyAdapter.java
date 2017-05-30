package com.actiknow.clearsale.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.BuyerDetailsPref;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.SetTypeFace;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


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
            case 1:
                Drawable img = activity.getResources ().getDrawable (R.drawable.circle_green);
                img.setBounds (0, 0, 30, 30);
                holder.tvStatus.setCompoundDrawables (img, null, null, null);
                holder.tvStatus.setText ("Available");
                holder.tvAcceptingOffer.setVisibility (View.VISIBLE);
                break;
            case 2:
                Drawable img2 = activity.getResources ().getDrawable (R.drawable.circle_yellow);
                img2.setBounds (0, 0, 30, 30);
                holder.tvStatus.setCompoundDrawables (img2, null, null, null);
                holder.tvStatus.setText ("Pending");
                holder.tvAcceptingOffer.setVisibility (View.GONE);
                break;
            case 3:
                Drawable img3 = activity.getResources ().getDrawable (R.drawable.circle_red);
                img3.setBounds (0, 0, 30, 30);
                holder.tvStatus.setCompoundDrawables (img3, null, null, null);
                holder.tvStatus.setText ("Sold");
                holder.tvAcceptingOffer.setVisibility (View.GONE);
                break;
            case 4:
                Drawable img4 = activity.getResources ().getDrawable (R.drawable.circle_red);
                img4.setBounds (0, 0, 30, 30);
                holder.tvStatus.setCompoundDrawables (img4, null, null, null);
                holder.tvStatus.setText ("Closed");
                holder.tvAcceptingOffer.setVisibility (View.GONE);
                break;
            case 9:
                Drawable img9 = activity.getResources ().getDrawable (R.drawable.circle_red);
                img9.setBounds (0, 0, 30, 30);
                holder.tvStatus.setCompoundDrawables (img9, null, null, null);
                holder.tvStatus.setText ("Offer Window Closing");
                holder.tvAcceptingOffer.setVisibility (View.GONE);
                break;
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

//            CustomImageSlider slider = new CustomImageSlider (activity);
//            slider
//                    .image (image)
//                    .setScaleType (BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener (new BaseSliderView.OnSliderClickListener () {
//                        @Override
//                        public void onSliderClick (BaseSliderView slider) {
//                            Utils.showToast (activity, "Property id" + property.getId (), true);
//                            Intent intent = new Intent (activity, PropertyDetailActivity.class);
//                            activity.startActivity (intent);
//                            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
//                        }
//                    });

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
//            holder.slider.addSlider (slider);
        }
        holder.slider.getPagerIndicator ().setVisibility (View.GONE);
        holder.slider.setPresetTransformer (SliderLayout.Transformer.Default);
        holder.slider.setCustomAnimation (new DescriptionAnimation ());
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
                final Handler handler = new Handler ();
                Runnable finalizer = null;
                switch (state) {
                    case 0:
                        finalizer = new Runnable () {
                            public void run () {
                                holder.rlSliderIndicator.setVisibility (View.GONE);
                                holder.rlFooter.setVisibility (View.VISIBLE);
                            }
                        };
                        handler.postDelayed (finalizer, 1500);
                        break;
                    case 1:
//                        handler.removeCallbacks (finalizer);
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
        holder.slider.setPresetIndicator (SliderLayout.PresetIndicators.Center_Bottom);
    
        if (property.is_favourite ()) {
            holder.ivFavourite.setImageResource (R.drawable.ic_heart_filled);
        } else {
            holder.ivFavourite.setImageResource (R.drawable.ic_heart);
        }
    
        holder.ivFavourite.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (property.is_favourite ()) {
                    property.setIs_favourite (false);
                    holder.ivFavourite.setImageResource (R.drawable.ic_heart);
                    updateFavouriteStatus (false, property.getId ());
                } else {
                    property.setIs_favourite (true);
                    holder.ivFavourite.setImageResource (R.drawable.ic_heart_filled);
                    updateFavouriteStatus (true, property.getId ());
                }
            }
        });
        
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
    
    public void updateFavouriteStatus (final boolean favourite, final int property_id) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_TESTIMONIALS, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_TESTIMONIALS,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        if (favourite) {
                                            
                                        } else {
                                        }
                                    } else {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    BuyerDetailsPref buyerDetailsPref = BuyerDetailsPref.getInstance ();
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.TYPE, "property_favourite");
                    params.put (AppConfigTags.BUYER_ID, String.valueOf (buyerDetailsPref.getIntPref (activity, BuyerDetailsPref.BUYER_ID)));
                    params.put (AppConfigTags.PROPERTY_ID, String.valueOf (property_id));
                    if (favourite) {
                        params.put (AppConfigTags.IS_FAVOURITE, String.valueOf (1));
                    } else {
                        params.put (AppConfigTags.IS_FAVOURITE, String.valueOf (0));
                    }
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
        }
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
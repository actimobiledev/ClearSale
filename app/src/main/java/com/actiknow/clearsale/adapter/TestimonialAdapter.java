package com.actiknow.clearsale.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.model.Testimonial;
import com.actiknow.clearsale.utils.SetTypeFace;

import java.util.ArrayList;
import java.util.List;


public class TestimonialAdapter extends RecyclerView.Adapter<TestimonialAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Testimonial> testimonials = new ArrayList<Testimonial>();

    public TestimonialAdapter(Activity activity, List<Testimonial> testimonials) {
        this.activity = activity;
        this.testimonials = testimonials;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.list_item_testimonials, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Testimonial testimonial = testimonials.get(position);
    
        holder.tvText.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvName.setTypeface (SetTypeFace.getTypeface (activity));
    
        holder.tvText.setText (testimonial.getDescription ());
        holder.tvName.setText(testimonial.getName());

//        Glide.with(activity).load("").placeholder(testimonial.getImage2()).into(holder.ivVideo);
    }

    @Override
    public int getItemCount() {
        return testimonials.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvText;
        TextView tvName;
        ImageView ivVideo;
    
    
        public ViewHolder(View view) {
            super(view);
            tvText = (TextView) view.findViewById (R.id.tvText);
            tvName = (TextView) view.findViewById(R.id.tvName);
            ivVideo = (ImageView) view.findViewById (R.id.ivVideo);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Testimonial testimonial = testimonials.get(getLayoutPosition());
        }
    }
}
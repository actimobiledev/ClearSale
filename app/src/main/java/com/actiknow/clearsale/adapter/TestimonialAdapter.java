package com.actiknow.clearsale.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.model.Faq;
import com.actiknow.clearsale.model.Testimonial;
import com.bumptech.glide.Glide;

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


        holder.tvDescription.setText(testimonial.getDescription());
        holder.tvName.setText(testimonial.getName());

        Glide.with(activity).load("").placeholder(testimonial.getImage2()).into(holder.ivPlayVedio);

        final ViewHolder tempholder = holder;


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
        TextView tvDescription;
        TextView tvName;
        ImageView ivPlayVedio;



        public ViewHolder(View view) {
            super(view);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            tvName = (TextView) view.findViewById(R.id.tvName);
            ivPlayVedio=(ImageView)view.findViewById(R.id.ivPlayVedio);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Testimonial testimonial = testimonials.get(getLayoutPosition());
            //Intent intent=new Intent(activity, PropertyDetailActivity.class);
            // activity.startActivity(intent);

            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
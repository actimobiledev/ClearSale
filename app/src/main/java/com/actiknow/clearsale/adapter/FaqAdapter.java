package com.actiknow.clearsale.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.activity.PropertyDetailActivity;
import com.actiknow.clearsale.model.Faq;

import java.util.ArrayList;
import java.util.List;


public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Faq> faqList = new ArrayList<Faq>();

    public FaqAdapter(Activity activity, List<Faq> faqList) {
        this.activity = activity;
        this.faqList = faqList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.listview_item_faq, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Faq faq = faqList.get(position);


        holder.tvQuestion.setText(faq.getQuestion());
        holder.tvAnswer.setText(faq.getAnswer());
        final ViewHolder tempholder = holder;


    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvQuestion;
        TextView tvAnswer;


        public ViewHolder(View view) {
            super(view);
            tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
            tvAnswer = (TextView) view.findViewById(R.id.tvAnswers);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Faq faq = faqList.get(getLayoutPosition());
            //Intent intent=new Intent(activity, PropertyDetailActivity.class);
            // activity.startActivity(intent);

            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
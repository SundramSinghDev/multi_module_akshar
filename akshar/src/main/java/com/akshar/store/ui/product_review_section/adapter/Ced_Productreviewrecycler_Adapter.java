package com.akshar.store.ui.product_review_section.adapter;

/*public class Ced_Productreviewrecycler_Adapter {
}*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.R;
import com.akshar.store.databinding.MagenativeCreateLayoutForReviewBinding;

import org.json.JSONArray;
import org.json.JSONObject;

public class Ced_Productreviewrecycler_Adapter extends RecyclerView.Adapter<Ced_Productreviewrecycler_Adapter.Ced_ProductreviewrecyclerViewHolder> {

    private Context context;
    private JSONArray jsonArray;
    private LayoutInflater layoutInflater;

    public Ced_Productreviewrecycler_Adapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public Ced_ProductreviewrecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MagenativeCreateLayoutForReviewBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.magenative_create_layout_for_review,parent,false);
        return new Ced_ProductreviewrecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Ced_ProductreviewrecyclerViewHolder holder, int position) {
        try {
            final JSONObject object = jsonArray.getJSONObject(position);
            holder.binding.headtitle.setText(object.getString("review-title"));
            holder.binding.by.setText(object.getString("review-by"));
            holder.binding.postedOn.setText(object.getString("posted_on"));
            holder.binding.description.setText(object.getString("review-description"));
            if (object.has("rating1")) {
                holder.binding.fillRatingCode1.setText(object.getString("rating_code1"));
                holder.binding.reviewtext1.setText(object.getString("rating_value1"));
                holder.binding.rating1.setVisibility(View.VISIBLE);
            }
            else {
                holder.binding.rating1.setVisibility(View.GONE);
            }

            if (object.has("rating2")) {
                holder.binding.fillRatingCode2.setText(object.getString("rating_code2"));
                holder.binding.reviewtext2.setText(object.getString("rating_value2"));
                holder.binding.rating2.setVisibility(View.VISIBLE);
            } else {
                holder.binding.rating2.setVisibility(View.GONE);
            }

            if (object.has("rating3")) {
                holder.binding.fillRatingCode3.setText(object.getString("rating_code3"));
                holder.binding.reviewtext3.setText(object.getString("rating_value3"));
                holder.binding.rating3.setVisibility(View.VISIBLE);
            } else {
                holder.binding.rating3.setVisibility(View.GONE);
            }

            if (object.has("rating4")) {
                holder.binding.fillRatingCode4.setText(object.getString("rating_code4"));
                holder.binding.reviewtext4.setText(object.getString("rating_value4"));
                holder.binding.rating4.setVisibility(View.VISIBLE);
            } else {
                holder.binding.rating4.setVisibility(View.GONE);
            }

            if (object.has("rating5")) {
                holder.binding.fillRatingCode5.setText(object.getString("rating_code5"));
                holder.binding.reviewtext5.setText(object.getString("rating_value5"));
                holder.binding.rating5.setVisibility(View.VISIBLE);
            } else {
                holder.binding.rating5.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    public class Ced_ProductreviewrecyclerViewHolder extends RecyclerView.ViewHolder /* implements View.OnClickListener*/ {
        public MagenativeCreateLayoutForReviewBinding binding;
        public Ced_ProductreviewrecyclerViewHolder(MagenativeCreateLayoutForReviewBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}


package com.akshar.store.ui.productsection.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.R;
import com.akshar.store.databinding.HomeDealItemBinding;
import com.akshar.store.databinding.SubacategoryLayoutBinding;
import com.akshar.store.ui.newhomesection.adapter.HomeDealSliderAdapter;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Sub_category_recycler_Adapter extends RecyclerView.Adapter<Sub_category_recycler_Adapter.ViewHolder> {

    String data;
    private JSONArray data_array;
    private Context context;

    public Sub_category_recycler_Adapter(Context context, JSONArray data_array) throws JSONException {
        this.context = context;
        /*this.data=data;*/
        /*this.data_array = new JSONArray(data.toString());*/
        this.data_array=data_array;
    }

    @Override
    public int getItemCount() {

        return (null != data_array ? data_array.length() : 0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        SubacategoryLayoutBinding binding= DataBindingUtil.inflate( LayoutInflater.from(viewGroup.getContext()), R.layout.subacategory_layout,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        try {
            JSONObject object=data_array.getJSONObject(position);
            holder.binding.subcatname1.setText(object.getString("category_name"));
            holder.binding.subcatId1.setText(object.getString("category_id"));
            holder.binding.card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Ced_New_Product_Listing.class);
                    intent.putExtra("ID", holder.binding.subcatId1.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public SubacategoryLayoutBinding binding;
        public ViewHolder(SubacategoryLayoutBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }

    }

}


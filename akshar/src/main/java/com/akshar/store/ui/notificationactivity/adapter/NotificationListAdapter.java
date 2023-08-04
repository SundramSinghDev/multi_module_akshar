package com.akshar.store.ui.notificationactivity.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.ui.websection.Ced_Weblink;
import com.akshar.store.databinding.NotificationItemBinding;
import com.akshar.store.ui.notificationactivity.activity.Ced_NotificationList;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;
import com.akshar.store.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationListViewHolder> {

    private Context context;
    private JSONArray jsonArray;
    private LayoutInflater layoutInflater;

    public NotificationListAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public NotificationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NotificationItemBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.notification_item,parent,false);
        return new NotificationListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListViewHolder holder, int position) {
        try {
            final JSONObject object = jsonArray.getJSONObject(position);
            Glide.with(context)
                    .load(object.getString("image"))
                    .dontTransform()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.binding.image1);
            holder.binding.heading.setText(object.getString("title"));
            holder.binding.content.setText(object.getString("message_text"));
            final String link_type = (object.getString("link_type"));
            final String link_id = (object.getString("link_id"));
            holder.binding.image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        switch (link_type) {
                            case "1":
                                Intent resultIntent = new Intent(context, Ced_NewProductView.class);
                                resultIntent.putExtra("product_id", link_id);
                                resultIntent.putExtra("fromnotification", "true");
                                /*resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                ((Ced_NotificationList)context).startActivity(resultIntent);
                                ((Ced_NotificationList)context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                break;

                            case "2":
                                Intent resultIntent1 = new Intent(context, Ced_New_Product_Listing.class);
                                resultIntent1.putExtra("ID", link_id);
                                resultIntent1.putExtra("fromnotification", "true");
                                /*resultIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                resultIntent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                ((Ced_NotificationList)context).startActivity(resultIntent1);
                                ((Ced_NotificationList)context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                break;

                            case "3":
                                Intent resultIntent2 = new Intent(context, Ced_Weblink.class);
                                resultIntent2.putExtra("link", link_id);
                                resultIntent2.putExtra("fromnotification", "true");
                                /*resultIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                resultIntent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                ((Ced_NotificationList)context).startActivity(resultIntent2);
                                ((Ced_NotificationList)context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                break;

                            default:
                                Log.d(Urls.TAG, "Null data onMessageReceived: " + link_type);
                        }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    public class NotificationListViewHolder extends RecyclerView.ViewHolder /* implements View.OnClickListener*/ {
        public NotificationItemBinding binding;
        public NotificationListViewHolder(NotificationItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}


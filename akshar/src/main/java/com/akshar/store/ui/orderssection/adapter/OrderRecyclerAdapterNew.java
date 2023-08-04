package com.akshar.store.ui.orderssection.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.R;
import com.akshar.store.databinding.NewSingleOrderItemViewBinding;
import com.akshar.store.ui.orderssection.activity.Ced_Orderview;
import com.akshar.store.ui.orderssection.datamodel.Order_Item_Model;

import java.util.List;

public class OrderRecyclerAdapterNew extends RecyclerView.Adapter<OrderRecyclerAdapterNew.ViewHolder> {

    Context context;
    public List<Order_Item_Model> data;

    public OrderRecyclerAdapterNew(Context context, List<Order_Item_Model> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewSingleOrderItemViewBinding singleOrderItemViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.new_single_order_item_view, parent, false);
        return new ViewHolder(singleOrderItemViewBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Order_Item_Model order = data.get(position);
            holder.binding.orderDate.setText(order.getDate());
            holder.binding.orderNumberTxt.setText("#"+order.getNumber());
            holder.binding.orderStatusTV.setText(order.getOrder_status());
            holder.binding.viewMore.setOnClickListener(v -> {
                Intent order_view_intent = new Intent(context, Ced_Orderview.class);
                order_view_intent.putExtra("orderid", order.getOrder_id());
                context.startActivity(order_view_intent);
            });
            holder.binding.orderedProductItemRV.setLayoutManager(new LinearLayoutManager(context));
            if (order.getOrderedItems() != null) {
                Ced_Order_List_Product_Item_Adapter adapter = new Ced_Order_List_Product_Item_Adapter(order.getOrderedItems(), context);
                holder.binding.orderedProductItemRV.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        NewSingleOrderItemViewBinding binding;

        public ViewHolder(@NonNull NewSingleOrderItemViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

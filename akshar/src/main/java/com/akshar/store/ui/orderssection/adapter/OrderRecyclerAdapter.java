package com.akshar.store.ui.orderssection.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.ui.orderssection.activity.Ced_Orderview;
import com.akshar.store.R;
import com.akshar.store.databinding.ListGroupBinding;
import com.akshar.store.ui.orderssection.datamodel.Order_Item_Model;

import java.util.List;


public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder> {

    Context context;
    public List<Order_Item_Model> data;

    public OrderRecyclerAdapter(Context context, List<Order_Item_Model> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListGroupBinding listGroupBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_group, parent, false);
        return new OrderViewHolder(listGroupBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        try {
            Order_Item_Model order = data.get(position);
//            holder.listGroupBinding.orderId.setText(order_number);
            holder.listGroupBinding.orderId.setText(order.getNumber());
            holder.listGroupBinding.orderDate.setText(order.getDate());
            holder.listGroupBinding.amountTextValue.setText(order.getTotal_amount());
            holder.listGroupBinding.shipToTextValue.setText(order.getShip_to());
            holder.listGroupBinding.orderStatusTextValue.setText(order.getOrder_status());
//            holder.listGroupBinding.itemsTextValue.setText(order_number);
            holder.listGroupBinding.itemsTextValue.setText(order.getOrder_id());

            holder.listGroupBinding.orderInfo.setOnClickListener(v -> {
                Intent orderview_intent = new Intent(context, Ced_Orderview.class);
                orderview_intent.putExtra("orderid", order.getOrder_id());
                context.startActivity(orderview_intent);
            });
            holder.listGroupBinding.orderedProductItemRV.setLayoutManager(new LinearLayoutManager(context));
            if (order.getOrderedItems() != null) {
                Ced_Order_List_Product_Item_Adapter adapter = new Ced_Order_List_Product_Item_Adapter(order.getOrderedItems(), context);
                holder.listGroupBinding.orderedProductItemRV.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ListGroupBinding listGroupBinding;

        public OrderViewHolder(@NonNull ListGroupBinding listGroupBinding) {
            super(listGroupBinding.getRoot());

            this.listGroupBinding = listGroupBinding;
        }
    }
}
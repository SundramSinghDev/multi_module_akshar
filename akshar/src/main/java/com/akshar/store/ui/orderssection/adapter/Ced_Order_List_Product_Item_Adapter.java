package com.akshar.store.ui.orderssection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.R;
import com.akshar.store.databinding.SingleProductItemViewForOrderListBinding;
import com.akshar.store.ui.orderssection.datamodel.OrderedItem;

import java.util.List;

public class Ced_Order_List_Product_Item_Adapter extends RecyclerView.Adapter<Ced_Order_List_Product_Item_Adapter.ViewHolder> {

    private final List<OrderedItem> orderedItems;
    Context context;

    public Ced_Order_List_Product_Item_Adapter(List<OrderedItem> orderedItems, Context context) {
        this.orderedItems = orderedItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SingleProductItemViewForOrderListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_product_item_view_for_order_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.orderListBinding.setData(orderedItems.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderedItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SingleProductItemViewForOrderListBinding orderListBinding;

        public ViewHolder(@NonNull SingleProductItemViewForOrderListBinding itemView) {
            super(itemView.getRoot());
            orderListBinding = itemView;
        }
    }
}

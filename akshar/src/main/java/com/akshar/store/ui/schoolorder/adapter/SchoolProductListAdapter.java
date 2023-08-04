package com.akshar.store.ui.schoolorder.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.databinding.SingleSchoolProductListViewBinding;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.schoolorder.fragments.ProductListFragment;
import com.akshar.store.ui.schoolorder.model.ProductDataModel;

import java.util.List;

public class SchoolProductListAdapter extends RecyclerView.Adapter<SchoolProductListAdapter.ViewHolder> {

    SingleSchoolProductListViewBinding binding;
    public List<ProductDataModel> data;
    private final Activity activity;
    onItemClickListeners mOnItemClickLister;

    public void setOnItemClickListener(onItemClickListeners listener) {
        mOnItemClickLister = listener;
    }

    public SchoolProductListAdapter(Activity activity, List<ProductDataModel> datas) {
        this.activity = activity;
        this.data = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_school_product_list_view, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
//            if (data.get(position).getFrom_price() != null && data.get(position).getTo_price() != null) {
//                holder.productListViewBinding.setBundlePrice(data.get(position).getFrom_price() + " - " + data.get(position).getTo_price());
//            }
            holder.productListViewBinding.setData(data.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SingleSchoolProductListViewBinding productListViewBinding;

        public ViewHolder(@NonNull SingleSchoolProductListViewBinding itemView) {
            super(itemView.getRoot());
            itemView.setClickListeners(SchoolProductListAdapter.this);
            this.productListViewBinding = itemView;
        }
    }

    public void addToWishList(View view, ProductDataModel dataModel) {
        int position = view.getLayoutDirection();
//        mOnItemClickLister.onAddToWishListClicked(view, dataModel,position);
    }

    public void onParentViewClick(View view, ProductDataModel dataModel) {
        try {
            mOnItemClickLister.onItemClick(view, dataModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface onItemClickListeners {
        void onItemClick(View view, ProductDataModel dataModel);

        void onAddToWishListClicked(View view, ProductDataModel dataModel, int position);
    }
}

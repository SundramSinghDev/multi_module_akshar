package com.akshar.store.ui.schoolorder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.SingleSchoolListItemBinding;
import com.akshar.store.ui.profilesection.activity.Ced_User_Profile;
import com.akshar.store.ui.schoolorder.model.School;
import com.akshar.store.ui.schoolorder.ui.SchoolDetailsActivity;

import java.util.List;

public class SchoolListAdapter extends RecyclerView.Adapter<SchoolListAdapter.ViewHolder> {

    public List<School> data;
    private final Context context;
    SingleSchoolListItemBinding itemBinding;
    public boolean isFromSchoolOrder = false;

    public SchoolListAdapter(List<School> schoolListDataModelList, Context context) {
        this.data = schoolListDataModelList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_school_list_item, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.binding.setBindData(data.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SingleSchoolListItemBinding binding;

        public ViewHolder(@NonNull SingleSchoolListItemBinding itemView) {
            super(itemView.getRoot());
            itemView.setClickListeners(SchoolListAdapter.this);
            this.binding = itemView;
        }
    }

    public void onItemClick(View view, School data) {
        try {
//            if (isFromSchoolOrder) {
//                if (((Ced_NavigationActivity) context).session.getSubSellerId().length() <= 0) {
////                    jumpToProfile(data);
//                } else if (!itemBinding.sellerId.getText().toString().trim().equals("0")) {
//                    jumpToSchoolDetailsActivity(data);
//                }
//            } else if (((Ced_NavigationActivity) context).session.getSubSellerId().length() > 0) {
//                if (!itemBinding.sellerId.getText().toString().trim().equals("0")) {
//                    jumpToSchoolDetailsActivity(data);
//                }
//            }
//            else {
//                jumpToProfile(data);
//            }
            jumpToSchoolDetailsActivity(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jumpToProfile(School data) {
        ((Ced_NavigationActivity) context).showMsgWithLongDuration("Please Select the School From Profile and Update The Profile..");
        Intent intent = new Intent(context.getApplicationContext(), Ced_User_Profile.class);
        intent.putExtra("seller_id", data.getSellerId());
        intent.putExtra("sub_seller_id", data.getSub_seller_id());
        (context).startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
    }

    private void jumpToSchoolDetailsActivity(School data) {
        Intent intent = new Intent(context.getApplicationContext(), SchoolDetailsActivity.class);
        intent.putExtra("seller_id", data.getSellerId());
        intent.putExtra("sub_seller_id", data.getSub_seller_id());
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
    }
}

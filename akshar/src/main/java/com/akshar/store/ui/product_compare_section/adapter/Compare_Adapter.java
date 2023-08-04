package com.akshar.store.ui.product_compare_section.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.AttributeTextviewBinding;
import com.akshar.store.databinding.AttributeheadingTextviewBinding;
import com.akshar.store.databinding.ComapreProductLayoutBinding;
import com.akshar.store.databinding.CompareheadingsProductLayoutBinding;
import com.akshar.store.databinding.MagenativeFragmentShowZoomImageBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.cartsection.activity.Ced_CartListing;
import com.akshar.store.ui.cartsection.viewmodel.CartViewModel;
import com.akshar.store.ui.product_compare_section.activity.Ced_CompareList;
import com.akshar.store.ui.product_compare_section.viewmodel.CompareItemViewModel;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class Compare_Adapter extends RecyclerView.Adapter<Compare_Adapter.MyViewHolder> {

    private JSONArray products;
    private Context context;
    JSONArray comparable_attributes;
    private static final int LAYOUT_ONE= 0;
    private static final int LAYOUT_TWO= 1;
    CompareItemViewModel compareItemViewModel;
    private LayoutInflater layoutInflater;

    public Compare_Adapter(Context context, JSONArray products, JSONArray comparable_attributes, CompareItemViewModel compareItemViewModel) throws JSONException {
        this.context = context;
        this.products=products;
        this.compareItemViewModel=compareItemViewModel;
        this.comparable_attributes=comparable_attributes;
    }

    @Override
    public int getItemCount() {

        return (null != products ? (products.length()+1) : 0);
    }

    @Override
    public int getItemViewType(int position)
    {
        if(position==0)
            return LAYOUT_ONE;
        else
            return LAYOUT_TWO;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ViewDataBinding binding;
        switch (viewType)
        {
            case LAYOUT_ONE:
                binding = DataBindingUtil.inflate(layoutInflater, R.layout.compareheadings_product_layout, viewGroup, false);
                return new MyViewHolder((CompareheadingsProductLayoutBinding) binding);
            case LAYOUT_TWO:
                binding = DataBindingUtil.inflate(layoutInflater, R.layout.comapre_product_layout, viewGroup, false);
                return new MyViewHolder((ComapreProductLayoutBinding) binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        switch (holder.getItemViewType()) {
            case LAYOUT_ONE:
                try {
                    CompareheadingsProductLayoutBinding headerBinding = holder.headerBinding;
                    // viewModel.setSomething(...);
                    // headerBinding.setViewModel(viewModel);
                    JSONObject position_attr = comparable_attributes.getJSONObject(position);
                    Iterator<String> iter = position_attr.keys(); //This should be the iterator you want.
                    while(iter.hasNext()){
                        String key = iter.next();
                        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        AttributeheadingTextviewBinding attributeheadingTextviewBinding = DataBindingUtil.inflate(layoutInflater,R.layout.attributeheading_textview, null, true);
                        attributeheadingTextviewBinding.text.setText(Html.fromHtml(key));
                        headerBinding.attributes.addView(attributeheadingTextviewBinding.getRoot());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case LAYOUT_TWO:
                ComapreProductLayoutBinding vaultItemHolder = holder.ItemBinding;
                /*viewModel.setSomething(...);
                regularItemBinding.setViewModel(viewModel);*/
                try {
                    vaultItemHolder.addtocartNormal.setVisibility(View.VISIBLE);
                    String special_price="";
                    String regular_price="";
                    JSONObject object=products.getJSONObject(position-1);
                    final String type=object.getString("type");

                    vaultItemHolder.productname1.setText(object.getString("product_name"));
                    Glide.with(context)
                            .load(object.getString("product_image"))
                            .dontTransform()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(vaultItemHolder.image1);
                    vaultItemHolder.productId1.setText(object.getString("product_id"));
                    if(object.has("special_price"))
                    {
                        special_price=object.getString("special_price");
                    }
                    if(object.has("regular_price"))
                    {
                        regular_price=object.getString("regular_price");
                    }
                    if (special_price.equals("no_special"))
                    {
                        vaultItemHolder.specialprice1.setVisibility(View.GONE);
                        vaultItemHolder.normalprice1.setPaintFlags(vaultItemHolder.normalprice1.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        vaultItemHolder.normalprice1.setTextColor(context.getResources().getColor(R.color.black));
                        vaultItemHolder.normalprice1.setText(regular_price);
                    } else {
                        vaultItemHolder.specialprice1.setVisibility(View.VISIBLE);
                        vaultItemHolder.specialprice1.setText(special_price);
                        vaultItemHolder.normalprice1.setText(regular_price);
                        vaultItemHolder.normalprice1.setTextColor(context.getResources().getColor(R.color.main_color_gray));
                        vaultItemHolder.normalprice1.setPaintFlags(vaultItemHolder.normalprice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    if(object.has("offer"))
                    {
                        vaultItemHolder.offer.setVisibility(View.VISIBLE);
                        vaultItemHolder.offer.setText(object.getString("offer")+context.getResources().getString(R.string.OFF));
                    }
                    vaultItemHolder.image1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, Ced_NewProductView.class);
                            intent.putExtra("product_id", vaultItemHolder.productId1.getText().toString());
                            intent.putExtra("product_name", vaultItemHolder.productname1.getText().toString());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                            context.startActivity(intent);
                        }
                    });
                    vaultItemHolder.addtocartNormal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addtocart(type,vaultItemHolder,"1");
                        }
                    });
                    vaultItemHolder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deletefromcomparelist(vaultItemHolder.productId1.getText().toString());
                        }
                    });
                    JSONObject position_attr=comparable_attributes.getJSONObject(position-1);
                    Iterator<String> iter = position_attr.keys(); //This should be the iterator you want.
                    while(iter.hasNext())
                    {
                        String key = iter.next();
                        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        AttributeTextviewBinding binding1 = DataBindingUtil.inflate(li, R.layout.attribute_textview, null, false);
                        binding1.text.setText(Html.fromHtml(position_attr.getString(key)));
                        vaultItemHolder.attributes.addView(binding1.getRoot());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void deletefromcomparelist(String product_id) {
        JsonObject param=new JsonObject();
        param.addProperty("prodID",product_id);
        param.addProperty("customer_id",((Ced_NavigationActivity)context).session.getCustomerid());
        compareItemViewModel.removefrom_Compare(context, param,((Ced_NavigationActivity)context).session.getHahkey()).observe((FragmentActivity) context, this::RemovefromCompareResponse);
    }

    private void RemovefromCompareResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                try {
                    JSONObject object=new JSONObject(apiResponse.data);
                    JSONObject data = object.getJSONObject("data");
                    if(data.has("message"))
                    {
                        ((Ced_NavigationActivity)context).showmsg(data.getString("message"));
                    }
                    if(data.getString("status").equals("true"))
                    {
                        ((Ced_CompareList)context).getproductcomparelist();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case ERROR:
                Log.e("ERROR ", Objects.requireNonNull(apiResponse.error));
                ((Ced_NavigationActivity)context).showmsg(context.getResources().getString(R.string.errorString));
                break;
        }
    }

    private void addtocart(String type, ComapreProductLayoutBinding holder, String quantity) {
        if(type.equalsIgnoreCase("simple"))
        {
                JsonObject addtocart = new JsonObject();
                addtocart.addProperty("product_id",  holder.productId1.getText().toString());
                addtocart.addProperty("qty",quantity);
                addtocart.addProperty("type", type);
                if (((Ced_NavigationActivity)context).session.isLoggedIn())
                {
                    addtocart.addProperty("customer_id", ((Ced_NavigationActivity)context).session.getCustomerid());
                }
                if (((Ced_NavigationActivity)context).cedSessionManagement.getCartId() != null)
                {
                    addtocart.addProperty("cart_id", ((Ced_NavigationActivity)context).cedSessionManagement.getCartId());
                }
                compareItemViewModel.addtocart(context, addtocart).observe((FragmentActivity) context, this::AddtoCompareResponse);
        }
        else
        {
            holder.image1.callOnClick();
        }
    }

    private void AddtoCompareResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                try {
                    JSONObject jsonObject_1 = new JSONObject(apiResponse.data);
                    final JSONObject nameArray = jsonObject_1.getJSONObject("cart_id");
                    if (nameArray.getString("success").equals("true"))
                    {
                        Ced_MainActivity.latestcartcount = String.valueOf(nameArray.getInt("items_count"));
                        if(context instanceof Ced_CompareList)
                        {
                            ((Ced_CompareList)context).setcount(String.valueOf(nameArray.getInt("items_count")));
                        }
                        String cart_id = nameArray.getString("cart_id");
                        ((Ced_NavigationActivity)context).cedSessionManagement.savecartid(cart_id);
                        ((Ced_NavigationActivity)context).showmsg(nameArray.getString("message"));
                    }
                    else
                    {
                        if (nameArray.getString("success").equals("false"))
                        {
                            ((Ced_NavigationActivity)context).showmsg(nameArray.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case ERROR:
                Log.e("ERROR ", Objects.requireNonNull(apiResponse.error));
                ((Ced_NavigationActivity)context).showmsg(context.getResources().getString(R.string.errorString));
                break;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CompareheadingsProductLayoutBinding headerBinding;
        private ComapreProductLayoutBinding ItemBinding;

        MyViewHolder(CompareheadingsProductLayoutBinding binding) {
            super(binding.getRoot());
            headerBinding = binding;
        }

        MyViewHolder(ComapreProductLayoutBinding binding) {
            super(binding.getRoot());
            ItemBinding = binding;
        }
    }

  }





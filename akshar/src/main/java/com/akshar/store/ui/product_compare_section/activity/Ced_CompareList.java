package com.akshar.store.ui.product_compare_section.activity;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.ActivityCedCompareListBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.product_compare_section.adapter.Compare_Adapter;
import com.akshar.store.ui.product_compare_section.viewmodel.CompareItemViewModel;
import com.akshar.store.ui.product_compare_section.viewmodel.CompareViewmodel;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_CompareList extends Ced_NavigationActivity {

    ActivityCedCompareListBinding cedCompareListBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    CompareViewmodel compareViewmodel;
    CompareItemViewModel compareItemViewModel;
    String counter_string="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        counter_string= Ced_MainActivity.latestcartcount;
        setcount(counter_string);
        cedCompareListBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_ced__compare_list, content, true);
        compareViewmodel = new ViewModelProvider(Ced_CompareList.this, viewModelFactory).get(CompareViewmodel.class);
        compareItemViewModel = new ViewModelProvider(Ced_CompareList.this, viewModelFactory).get(CompareItemViewModel.class);
        getproductcomparelist();

    }

    public void getproductcomparelist() {
        JsonObject param=new JsonObject();
        param.addProperty("customer_id",session.getCustomerid());
        compareViewmodel.getcomparelist(this, param,session.getHahkey()).observe(this, this::consumeCompareResponse);
    }

    private void consumeCompareResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                showdata(apiResponse.data);
                break;

            case ERROR:
                Log.e("ERROR ", Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }
    public void setcount(String count)
    {
        counter_string = count;
        try
        {
            invalidateOptionsMenu();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void showdata(String output) {
        try {
            JSONObject object = new JSONObject(output);
            JSONObject data=object.getJSONObject("data");
            if(data.getString("status").equals("true"))
            {
                cedCompareListBinding.msg.setVisibility(View.GONE);
                cedCompareListBinding.recycler.setVisibility(View.VISIBLE);
                JSONArray products=data.getJSONArray("products");
                JSONArray comparable_attributes=data.getJSONArray("comparable_attributes");
                if(products.length()==comparable_attributes.length())
                {
                    Compare_Adapter adapter = new Compare_Adapter(Ced_CompareList.this, products,comparable_attributes,compareItemViewModel);
                    cedCompareListBinding.recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    //BestSelling_Adapter
                }
                else
                {
                    showmsg("Something Went Wrong");
                }
            }
            else
            {
                if(data.has("message"))
                {
                    cedCompareListBinding.msg.setVisibility(View.VISIBLE);
                    cedCompareListBinding.recycler.setVisibility(View.GONE);
                    cedCompareListBinding.msg.setText(data.getString("message"));
                   // showmsg(data.getString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
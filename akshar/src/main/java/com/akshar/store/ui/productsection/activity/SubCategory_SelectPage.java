package com.akshar.store.ui.productsection.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.MagenativeDatetyprlayoutBinding;
import com.akshar.store.databinding.RadiobuttonlayoutBinding;
import com.akshar.store.databinding.SubcategorySelectLayoutpageBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SubCategory_SelectPage extends Ced_NavigationActivity {
JSONArray subcat_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sub_category__select_page);
        SubcategorySelectLayoutpageBinding sub_categorybinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.subcategory_select_layoutpage
                , content, true);
        try {
            subcat_array=new JSONArray(getIntent().getStringExtra("sub_category"));
            showbackbutton();
            showtootltext("BY CATEGORY");
            for  (int i=0;i<subcat_array.length();i++)
            {
                 RadiobuttonlayoutBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.radiobuttonlayout, null, false);
                try {
                    JSONObject jsonObject=subcat_array.getJSONObject(i);
                    binding.rdbtn.setText("   "+jsonObject.getString("category_name")/*+"("+jsonObject.getString("product_count")+")"*/);
                    binding.rdbtn.setTag(jsonObject.getString("category_id"));
                    binding.rdbtn.setId(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(binding.rdbtn.getParent() != null) {
                    ((ViewGroup)binding.rdbtn.getParent()).removeView(binding.rdbtn); // <- fix
                }
                sub_categorybinding.rg.addView(binding.rdbtn);
                sub_categorybinding.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int rbid=sub_categorybinding.rg.getCheckedRadioButtonId();
                        RadioButton id = (RadioButton) findViewById(rbid);
                        String radioText = id.getText().toString();
                        String radioTag=id.getTag().toString();
                        Log.e("selected_value",radioText+" _radioTag"+radioTag);

                        Intent intent = new Intent(SubCategory_SelectPage.this, Ced_New_Product_Listing.class);
                        intent.putExtra("ID", radioTag);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
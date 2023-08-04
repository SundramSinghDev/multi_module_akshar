package com.akshar.store.ui.productsection.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.databinding.OthersCategoryListItemBinding;
import com.akshar.store.databinding.SingleItemBinding;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeCongifAttr_Item_Adapter extends RecyclerView.Adapter<HomeCongifAttr_Item_Adapter.ViewHolder> {

    private Context context;
    private JSONArray jsonArray;
    LayoutInflater layoutInflater;
    private String row_id;
    private String row_position;
    private JSONObject attribute_index;
    private RecyclerView productpagerecyler;
    private RecyclerView popuprecyler;
    String att_name;

    public HomeCongifAttr_Item_Adapter(Context context, String att_name, JSONArray jsonArray, String row_id, String row_position, JSONObject attribute_index, RecyclerView productpagerecyler, RecyclerView popuprecyler) {
        this.context = context;
        this.jsonArray = jsonArray;
        this.row_id = row_id;
        this.row_position = row_position;
        this.attribute_index = attribute_index;
        this.productpagerecyler = productpagerecyler;
        this.popuprecyler = popuprecyler;
        this.att_name = att_name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View view = LayoutInflater.from(context).inflate(R.layout.others_category_list_item, parent, false);
        return new ViewHolder(view);*/
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SingleItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.single_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String id = jsonObject.getString("id");
            String label = jsonObject.getString("label");
            if (jsonObject.has("value") && jsonObject.getString("value").contains("#")) {
                String value = jsonObject.getString("value");
                holder.binding.text.setText("");
                GradientDrawable gradientDrawable = new GradientDrawable();
//                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                Log.d("REpo", "colorr: " + value);
                gradientDrawable.setColor(Color.parseColor(value));
                holder.binding.text.setBackground(gradientDrawable);
                holder.binding.text.setTag(label);
                holder.binding.linear.setTag("color");
            } else {
                holder.binding.linear.setTag("");
                holder.binding.text.setText(label);
                holder.binding.text.setTag(label);
            }
            holder.binding.linear.setBackground(context.getResources().getDrawable(R.drawable.new_corner_round));
            holder.binding.text.setTextColor(context.getResources().getColor(R.color.COLOR_545454));
            holder.binding.attName.setText(att_name);

//           holder.binding.linear.setBackground(context.getResources().getDrawable(R.drawable.cardcorner));
//           holder.binding.text.setTextColor(context.getResources().getColor(R.color.darker_gray2));
//           holder.binding.text.setTextColor(context.getResources().getColor(R.color.txtapptheme_color));
            holder.binding.linear.setOnClickListener(v -> {
                try {
                    ((Ced_NewProductView) context).selected_row_image_id.remove(Integer.valueOf(row_position));
                    ((Ced_NewProductView) context).selected_row_image_id.put(Integer.valueOf(row_position), row_id + "#" + id);
                    //-remove old selected index value---
                    ((Ced_NewProductView) context).selected_config_row_values.remove(row_id);
                    ((Ced_NewProductView) context).selected_config_row_values.put((row_id), id);

                    ((Ced_NewProductView) context).selected_config_att_name_and_option_name.remove(att_name);
//                    ((Ced_NewProductView) context).selected_config_att_name_and_option_name.put(att_name, holder.binding.text.getText().toString());
                    ((Ced_NewProductView) context).selected_config_att_name_and_option_name.put(att_name, holder.binding.text.getTag().toString());
                    //-----------
                    if ((((Ced_NewProductView) context).totalconfig_attributes == ((Ced_NewProductView) context).selected_row_image_id.size()) && context instanceof Ced_NewProductView) {
                        String imageindex_key = "";
                        for (int k = 0; k < ((Ced_NewProductView) context).selected_row_image_id.size(); k++) {
                            if (k == 0) {
                                imageindex_key += ((Ced_NewProductView) context).selected_row_image_id.get(k);
                            } else {
                                imageindex_key += "#" + ((Ced_NewProductView) context).selected_row_image_id.get(k);
                            }
                            Log.d("REpo", "imageindex_key: " + imageindex_key);
                        }

                        ((Ced_NewProductView) context).SearchImage2(imageindex_key, attribute_index);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (context instanceof Ced_NewProductView) {
                    if (!holder.binding.linear.getTag().toString().trim().equalsIgnoreCase("color"))
                        ((Ced_NewProductView) context).unselectallselectedimages2(productpagerecyler, popuprecyler, position);
                    else
                        ((Ced_NewProductView) context).unselectallselectedimages(productpagerecyler, position);

                }
            });

          /* if(position==0)
           {
               holder.binding.linear.setBackground(context.getResources().getDrawable(R.drawable.border_image_gallery_selected));
               holder.binding.text.setTextColor(context.getResources().getColor(R.color.txtapptheme_color));
               holder.binding.linear.callOnClick();
           }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/ {

        public SingleItemBinding binding;

        public ViewHolder(SingleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

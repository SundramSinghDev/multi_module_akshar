package com.akshar.store.ui.newhomesection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.databinding.HomeBannerOrientationRecyclerLayoutBinding;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BannerRecycler_Adapter extends RecyclerView.Adapter<BannerRecycler_Adapter.ViewHolder> {

    private JSONArray data_array;
    private Context context;
    private String banner_orientiation;
    private LayoutInflater layoutInflater;

    public BannerRecycler_Adapter(Context context, JSONArray data, String banner_orientiation)
    {
        this.context = context;
        this.data_array=data;
        this.banner_orientiation=banner_orientiation;
    }

    @Override
    public int getItemCount() {

        return (null != data_array ? data_array.length() : 0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        /*View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_banner_orientation_recycler_layout, viewGroup, false);
        return new ViewHolder(itemView);*/
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        HomeBannerOrientationRecyclerLayoutBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.home_banner_orientation_recycler_layout,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        try {
            if(banner_orientiation.equals("vertical"))
            {
                holder.binding.image1.getLayoutParams().width=(Ced_NavigationActivity.device_width/2);
                holder.binding.image1.getLayoutParams().height=(Ced_NavigationActivity.device_width);
            }
            else
            {
                holder.binding.image1.getLayoutParams().height=(Ced_NavigationActivity.device_width-(Ced_NavigationActivity.device_width/6));
            }
            JSONObject object=data_array.getJSONObject(position);
            Glide.with(context)
                    .load(object.getString("banner_image"))
                    .dontTransform()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .dontTransform()
                    .dontAnimate()
                    .into(holder.binding.image1);
            final String product_id=(object.getString("product_id"));
            final String link_to=(object.getString("link_to"));
            if(context instanceof Magenative_HomePageNewTheme)
            {
                ((Magenative_HomePageNewTheme)context).BannerIntent(holder.binding.image1,link_to,product_id);;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public HomeBannerOrientationRecyclerLayoutBinding binding;
        public ViewHolder(HomeBannerOrientationRecyclerLayoutBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }

}

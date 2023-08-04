package com.akshar.store.ui.notificationactivity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.ActivityCedNotificationListBinding;
import com.akshar.store.databinding.HomeDealSliderDynamicBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.homesection.viewmodel.HomeViewModel;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.newhomesection.adapter.HomeDealSliderAdapter;
import com.akshar.store.ui.notificationactivity.adapter.NotificationListAdapter;
import com.akshar.store.ui.notificationactivity.viewmodel.NotificationViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_NotificationList extends Ced_NavigationActivity {
    ActivityCedNotificationListBinding activityCedNotificationListBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    NotificationViewModel notificationViewModel;
    int  page=1;
    private boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         activityCedNotificationListBinding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_ced__notification_list,  content,true);
        notificationViewModel = new ViewModelProvider(Ced_NotificationList.this, viewModelFactory).get(NotificationViewModel.class);
        JsonObject param=new JsonObject();
        param.addProperty("customer_id",session.getCustomerid());
        param.addProperty("page",page);
        notificationViewModel.getnotificationlist(this, param,session.getHahkey()).observe(this, this::Response);


        activityCedNotificationListBinding.notificationList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !loading) {
                    Log.i("onScrollStateChanged", "loadmore");
                    loading = true;
                    //-------------------
                    page = page + 1;
                    param.addProperty("page", page);
                    notificationViewModel.getnotificationlist(Ced_NotificationList.this, param,session.getHahkey()).observe(Ced_NotificationList.this, Ced_NotificationList.this::Response);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void Response(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                try {
                    listnotification(Objects.requireNonNull(apiResponse.data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void listnotification(String output) throws JSONException {
        JSONObject object=new JSONObject(output.toString());
        JSONObject data=object.getJSONObject("data");
        if(data.getString("status").equalsIgnoreCase("true"))
        {
            activityCedNotificationListBinding.notificationList.setAdapter(new NotificationListAdapter(this, data.getJSONArray("list")));
        }
        else
        {
           if(page==1)
            {
                activityCedNotificationListBinding.noNotificationlayout.setVisibility(View.VISIBLE);
                activityCedNotificationListBinding.notificationTitle.setVisibility(View.GONE);
                activityCedNotificationListBinding.conti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("exceptfromhome", "true");
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                });
            }
            else
            {
               // showmsg(data.getString("message"));
            }
        }
    }
}
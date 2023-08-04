package com.akshar.store.Ced_MageNative_Upgrade;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.akshar.store.R;
import com.akshar.store.databinding.MagenativeActivityUpgradeBinding;
import com.akshar.store.databinding.MagenativeAddressListItemShippingBinding;

import okhttp3.internal.Util;

public class Ced_Upgrade extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MagenativeActivityUpgradeBinding binding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.magenative_activity_upgrade, null,false);
        setContentView(binding.getRoot());

        binding.upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

    }
}

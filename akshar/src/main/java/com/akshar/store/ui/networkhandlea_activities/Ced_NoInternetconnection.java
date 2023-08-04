/*
 * Copyright/**
 *          * CedCommerce
 *           *
 *           * NOTICE OF LICENSE
 *           *
 *           * This source file is subject to the End User License Agreement (EULA)
 *           * that is bundled with this package in the file LICENSE.txt.
 *           * It is also available through the world-wide-web at this URL:
 *           * http://cedcommerce.com/license-agreement.txt
 *           *
 *           * @category  Ced
 *           * @package   MageNative
 *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *           * @license      http://cedcommerce.com/license-agreement.txt
 *
 */
package com.akshar.store.ui.networkhandlea_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.MagenativeNointernetconnectionBinding;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;

import dagger.hilt.android.AndroidEntryPoint;

/*@AndroidEntryPoint*/
public class Ced_NoInternetconnection extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        //MagenativeNointernetconnectionBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_nointernetconnection, content, true);

        MagenativeNointernetconnectionBinding binding = DataBindingUtil.setContentView(this,R.layout.magenative_nointernetconnection);

        binding.conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ced_ConnectionDetector cedConnectionDetector = new Ced_ConnectionDetector(Ced_NoInternetconnection.this);
                if (cedConnectionDetector.isConnectingToInternet()){
                    Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }else
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.stillnotconnected),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
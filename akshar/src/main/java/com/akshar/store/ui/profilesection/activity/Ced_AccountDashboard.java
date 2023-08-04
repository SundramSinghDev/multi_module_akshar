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
package com.akshar.store.ui.profilesection.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.akshar.store.base.utilapplication.AnalyticsApplication;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.notificationactivity.activity.Ced_NotificationList;
import com.akshar.store.ui.product_compare_section.activity.Ced_CompareList;
import com.akshar.store.ui.productsection.activity.Ced_MyDownloadableProducts;
import com.akshar.store.databinding.MagenativeAccountPageBinding;
import com.akshar.store.ui.addresssection.activity.Ced_Addressbook;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.ui.orderssection.activity.Ced_ShowOrder;
import com.akshar.store.R;
import com.akshar.store.ui.wishlistsection.activity.Ced_WishListing;

import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_AccountDashboard extends Ced_NavigationActivity {
    MagenativeAccountPageBinding accountPageBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectaccounttab();
        accountPageBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_account_page, content, true);
        showToolBarWithTitle("My Account");
        accountPageBinding.MageNativeEditProfile.setOnClickListener(v -> {
            Intent ProfileIntent = new Intent(getApplicationContext(), Ced_User_Profile.class);
            ProfileIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(ProfileIntent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        final HashMap<String, String> user = session.getUserDetails();
        accountPageBinding.MageNativeProfileMail.setText(user.get(session.Key_Email));
        accountPageBinding.MageNativeUserName.setText(session.getname());

        set_bold_font_fortext(accountPageBinding.MageNativeUserName);
//        set_bold_font_fortext(accountPageBinding.MageNativeHeading);
        set_regular_font_fortext(accountPageBinding.MageNativeProfileMail);
        set_regular_font_fortext(accountPageBinding.MageNativeMyOrders);
        set_regular_font_fortext(accountPageBinding.MageNativeMycomparelist);
        set_regular_font_fortext(accountPageBinding.MageNativeMyAddressBook);
        set_regular_font_fortext(accountPageBinding.MageNativeMyWishlist);
        set_regular_font_fortext(accountPageBinding.MageNativeMyNotifications);
        set_regular_font_fortext(accountPageBinding.MageNativeMyDownloads);
        set_regular_font_fortext(accountPageBinding.MageNativeLogout);

        if (session.getgender().equalsIgnoreCase("male")) {
            accountPageBinding.MageNativeProfileImage.setImageResource(R.drawable.profile_place_holder_male);
        } else if (session.getgender().equalsIgnoreCase("female")) {
            accountPageBinding.MageNativeProfileImage.setImageResource(R.drawable.profile_place_holder_female);
        } else {
            accountPageBinding.MageNativeProfileImage.setImageResource(R.drawable.guest);
        }

        accountPageBinding.MageNativeMyOrders.setOnClickListener(v -> {
            Intent orderIntent = new Intent(getApplicationContext(), Ced_ShowOrder.class);
            orderIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(orderIntent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        accountPageBinding.MageNativeMycomparelist.setOnClickListener(v -> {
            Intent orderIntent = new Intent(getApplicationContext(), Ced_CompareList.class);
            orderIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(orderIntent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        accountPageBinding.MageNativeMyAddressBook.setOnClickListener(v -> {
            Intent orderIntent = new Intent(getApplicationContext(), Ced_Addressbook.class);
            orderIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(orderIntent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        accountPageBinding.MageNativeMyWishlist.setOnClickListener(v -> {
            Intent orderIntent = new Intent(getApplicationContext(), Ced_WishListing.class);
            orderIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(orderIntent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });
        accountPageBinding.MageNativeMyNotifications.setOnClickListener(v -> {
            Intent orderIntent = new Intent(getApplicationContext(), Ced_NotificationList.class);
            orderIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(orderIntent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        accountPageBinding.MageNativeMyDownloads.setOnClickListener(v -> {
            Intent orderIntent = new Intent(getApplicationContext(), Ced_MyDownloadableProducts.class);
            orderIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(orderIntent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        accountPageBinding.MageNativeLogout.setOnClickListener(v -> {
            try {
                cleardataandlogout();
                AnalyticsApplication.productresponse = "";
                AnalyticsApplication.categoryresponse = "";
                AnalyticsApplication.dealResponse = "";
                AnalyticsApplication.loadedproducts = false;
                AnalyticsApplication.loadeddeal = false;

                Intent i = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                startActivity(i);
                finishAffinity();
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        accountPageBinding.MageNativeUserName.setText(session.getname());
    }
}

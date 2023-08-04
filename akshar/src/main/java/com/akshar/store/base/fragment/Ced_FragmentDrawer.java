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
package com.akshar.store.base.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akshar.store.Ced_MageNative_FontSetting.Ced_FontSetting;
import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.base.utilapplication.AnalyticsApplication;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.profilesection.activity.Ced_AccountDashboard;
import com.akshar.store.ui.cartsection.activity.Ced_CartListing;
import com.akshar.store.ui.homesection.activity.Ced_Categories;
import com.akshar.store.ui.loginsection.activity.Ced_Login;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;
import com.akshar.store.databinding.MagenativeFragmentNavigationDrawerBinding;
import com.akshar.store.databinding.MagenativeListItemBinding;
import com.akshar.store.databinding.MagenativeMaincategorysectionBinding;
import com.akshar.store.ui.schoolorder.ui.SchoolDetailsActivity;
import com.akshar.store.ui.schoolorder.ui.SchoolListActivity;
import com.akshar.store.ui.sellersection.activity.Ced_SellerListing;
import com.akshar.store.ui.wishlistsection.activity.Ced_WishListing;
import com.akshar.store.Ced_MageNative_Scanner.Ced_Scanner;
import com.akshar.store.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Ced_FragmentDrawer extends BaseFragment {
    private MagenativeFragmentNavigationDrawerBinding fragmentDrawerBinding;
    public static ActionBarDrawerToggle mDrawerToggle;
    public static DrawerLayout mDrawerLayout;
    private View containerView;
    private FragmentDrawerListener drawerListener;
    View header;
    LinearLayout cmpspages;
    int accessresult;
    public Ced_SessionManagement_login session;
    public Ced_SessionManagement cedSessionManagement;
    private boolean special_category_name_flag, special_category_id_flag;

    public Ced_FragmentDrawer() {
    }

    void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accessresult = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);

        cedSessionManagement = Ced_SessionManagement.getCed_sessionManagement(getActivity());
        session = Ced_SessionManagement_login.getShredPrefs(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentDrawerBinding = DataBindingUtil.inflate(inflater, R.layout.magenative_fragment_navigation_drawer, container, true);
        try {
            PackageInfo pInfo = requireActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            int versioncode = pInfo.versionCode;
            String app_version = getResources().getString(R.string.appversion) + " " + version + "(" + versioncode + ")";
            fragmentDrawerBinding.appversion.setText(app_version);
            fragmentDrawerBinding.copyright.setText(getResources().getString(R.string.copyright) + " " + getResources().getString(R.string.app_name));
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentDrawerBinding.menulinktittle.setOnClickListener(v -> {
            Intent weblink = new Intent(getActivity(), Ced_Categories.class);
            requireActivity().startActivity(weblink);
            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            mDrawerLayout.closeDrawers();
        });

        fragmentDrawerBinding.mySchools.setOnClickListener(v -> {
            if (session.isLoggedIn()) {
                if (session.getSubSellerId().equals("")) {
                    Intent schoolList = new Intent(getActivity(), SchoolListActivity.class);
                    requireActivity().startActivity(schoolList);
                    requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    mDrawerLayout.closeDrawers();
                } else {
                    Intent schoolDetails = new Intent(getActivity(), SchoolDetailsActivity.class);
                    schoolDetails.putExtra("sub_seller_id", session.getSubSellerId());
                    requireActivity().startActivity(schoolDetails);
                    requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    mDrawerLayout.closeDrawers();
                }
            } else {
                Intent account_page = new Intent(getActivity(), Ced_Login.class);
                account_page.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(account_page);
                requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }
        });

        fragmentDrawerBinding.schoolOrders.setOnClickListener(v -> {
//            if (session.isLoggedIn()) {
//                if (session.getSubSellerId().equals("")) {
                Intent schoolList = new Intent(getActivity(), SchoolListActivity.class);
                schoolList.putExtra("from", "school_orders");
                requireActivity().startActivity(schoolList);
                requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                mDrawerLayout.closeDrawers();
//                }
//                else {
//                    Intent schoolDetails = new Intent(getActivity(), SchoolDetailsActivity.class);
//                    schoolDetails.putExtra("seller_id", session.getSubSellerId());
//                    requireActivity().startActivity(schoolDetails);
//                    requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
//                    mDrawerLayout.closeDrawers();
//                }
//            } else {
//                Intent account_page = new Intent(getActivity(), Ced_Login.class);
//                account_page.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(account_page);
//                requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
//            }
        });

        fragmentDrawerBinding.visitsellersection.setOnClickListener(v -> {
            Intent Vistshops = new Intent(getActivity(), Ced_SellerListing.class);
            Vistshops.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(Vistshops);
            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            mDrawerLayout.closeDrawers();
        });

        fragmentDrawerBinding.logoutsection.setOnClickListener(v -> {
            try {
                ((Ced_NavigationActivity) getActivity()).cleardataandlogout();
                AnalyticsApplication.productresponse = "";
                AnalyticsApplication.categoryresponse = "";
                AnalyticsApplication.dealResponse = "";
                AnalyticsApplication.loadedproducts = false;
                AnalyticsApplication.loadeddeal = false;
                Intent i = new Intent(getActivity(), Magenative_HomePageNewTheme.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        fragmentDrawerBinding.homeSection.setOnClickListener(v -> {
            // Intent home = new Intent(getActivity(), Ced_New_home_page.class);
            Intent home = new Intent(getActivity(), Magenative_HomePageNewTheme.class);
            home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            mDrawerLayout.closeDrawers();
        });

        fragmentDrawerBinding.bagSection.setOnClickListener(v -> {
            Intent cart = new Intent(getActivity(), Ced_CartListing.class);
            cart.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(cart);
            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            mDrawerLayout.closeDrawers();
        });

        fragmentDrawerBinding.wishSection.setOnClickListener(v -> {
            if (session.isLoggedIn()) {
                Intent wish = new Intent(getActivity(), Ced_WishListing.class);
                startActivity(wish);
                requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                mDrawerLayout.closeDrawers();
            } else {
                ((Ced_NavigationActivity) getActivity()).showmsg(getResources().getString(R.string.loginfirst));
            }
        });

        fragmentDrawerBinding.myaccountSection.setOnClickListener(v -> {
            if (session.isLoggedIn()) {
                Intent account = new Intent(getActivity(), Ced_AccountDashboard.class);
                startActivity(account);
                requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                mDrawerLayout.closeDrawers();
            } else {
                ((Ced_NavigationActivity) getActivity()).showmsg(getResources().getString(R.string.loginfirst));
            }
        });

        fragmentDrawerBinding.languageSection.setOnClickListener(v -> {
            mDrawerLayout.closeDrawers();
            ((Ced_NavigationActivity) requireActivity()).getStoresRequest();
        });

        fragmentDrawerBinding.invitesection.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareString = Html.fromHtml(getResources().getString(R.string.heycheckout) + "  " + getResources().getString(R.string.app_name) + " \n" + getResources().getString(R.string.googleplayservcices) + getResources().getString(R.string.appstoreurl)).toString();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.invitefriends)));
        });

        fragmentDrawerBinding.scansection.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent searchscanner = new Intent(getActivity(), Ced_Scanner.class);
                searchscanner.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(searchscanner);
                requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                mDrawerLayout.closeDrawers();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{
                                Manifest.permission.CAMERA},
                        Ced_MainActivity.MY_PERMISSIONS_REQUEST_CAMERA);
            }
        });

        if (session.isLoggedIn()) {
            fragmentDrawerBinding.MageNativeSignin.setText(session.getname());
            try {
                fragmentDrawerBinding.headertext.setText(session.getUserDetails().get(Ced_SessionManagement_login.Key_Email));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (session.getgender().equalsIgnoreCase("Male"))
                fragmentDrawerBinding.MageNativeProfileImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.profile_place_holder_male));
            if (session.getgender().equalsIgnoreCase("Female"))
                fragmentDrawerBinding.MageNativeProfileImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.profile_place_holder_female));
            if (session.getgender().equalsIgnoreCase("Guest"))
                fragmentDrawerBinding.MageNativeProfileImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.gust_b));
            fragmentDrawerBinding.logoutsection.setVisibility(View.VISIBLE);
        } else {
            fragmentDrawerBinding.MageNativeSignin.setText(getResources().getString(R.string.signin_drawer));
        }

        fragmentDrawerBinding.section1.setOnClickListener(v -> {
            if (fragmentDrawerBinding.MageNativeSignin.getText().toString().equals(getResources().getString(R.string.signin_drawer))) {
                Intent account_page = new Intent(getActivity(), Ced_Login.class);
                account_page.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(account_page);
                requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                Intent account_page = new Intent(getActivity(), Ced_AccountDashboard.class);
                account_page.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(account_page);
                requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }
        });

        createsidedrawercategories();
//        createsidedrawer__specialcategories();

        return fragmentDrawerBinding.getRoot();
    }


    private void createsidedrawer__specialcategories() {
        //----------------specialcategory
        try {
            JSONArray special_categories_jsonObject = new JSONArray(cedSessionManagement.getspecial_categories());
            for (int i = 0; i < Objects.requireNonNull(special_categories_jsonObject).length(); i++) {
                JSONObject cat = special_categories_jsonObject.getJSONObject(i);
                MagenativeMaincategorysectionBinding categorySectionBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_maincategorysection, fragmentDrawerBinding.sidemenus, false);
                final LinearLayout maincatlayout = categorySectionBinding.MageNativeMaincat;
                final TextView lblListHeader = categorySectionBinding.MageNativeLblListHeader;
                TextView maincatid = categorySectionBinding.MageNativeMaincatid;
                ImageView expandxlose = categorySectionBinding.MageNativeExpandxlose;
                if (cat.has("special_category_name")) {
                    lblListHeader.setText(cat.getString("special_category_name"));
                    lblListHeader.setTextColor(getResources().getColor(R.color.onwhitetextcolor));
                    Ced_FontSetting cedFontSetting = new Ced_FontSetting();
//                    cedFontSetting.setFontforTextviews(lblListHeader, "Celias-Bold.ttf", getActivity());
                    cedFontSetting.setFontforTextviews(lblListHeader, "Celias-Regular.ttf", getActivity());
                    // ((Ced_NavigationActivity)getActivity()).set_regular_font_fortext(lblListHeader);
                }
                if (cat.has("special_category_id")) {
                    maincatid.setText(cat.getString("special_category_id"));
                }
                if (cat.getBoolean("has_child") && cat.has("sub_cats")) {
                    expandxlose.setVisibility(View.VISIBLE);
                }
                maincatlayout.setOnClickListener(v -> {
                    RelativeLayout maincatrelative = (RelativeLayout) maincatlayout.getChildAt(0);
                    final LinearLayout subcategorysection = (LinearLayout) maincatlayout.getChildAt(1);
                    TextView maincatname = (TextView) maincatrelative.getChildAt(0);
                    TextView maincid = (TextView) maincatrelative.getChildAt(4);
                    ImageView rotateimage = (ImageView) maincatrelative.getChildAt(3);
                    try {
                        if (cat.has("sub_cats")) {
                            if (cat.getJSONArray("sub_cats").length() > 0) {
                                if (subcategorysection.getChildCount() == 0) {
                                    JSONArray sub_cats = cat.getJSONArray("sub_cats");
                                    int countersub = 0;
                                    for (int j = 0; j < sub_cats.length(); j++) {
                                        JSONObject subcategory = sub_cats.getJSONObject(j);
                                        countersub = countersub + 1;
                                        MagenativeListItemBinding listItemBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_list_item, null, false);
                                        final RelativeLayout subrelat = listItemBinding.MageNativeSubrelat;
                                        subrelat.setOnClickListener(v1 -> {
                                            TextView subcatid = (TextView) subrelat.getChildAt(2);
                                            Intent intent = new Intent(getActivity(), Ced_New_Product_Listing.class);
                                            intent.putExtra("ID", subcatid.getText().toString());
                                            startActivity(intent);
                                            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                            mDrawerLayout.closeDrawers();

                                        });
                                        TextView upperline = listItemBinding.MageNativeUpperline;
                                        TextView bottomline = listItemBinding.MageNativeBottomline;
                                        TextView lblListItem = listItemBinding.MageNativeLblListItem;
                                        ((Ced_NavigationActivity) getActivity()).set_regular_font_fortext(lblListItem);
                                        TextView lblListItemid = listItemBinding.MageNativeLblListItemid;
                                        lblListItem.setText(subcategory.getString("sub_category_name"));
                                        lblListItemid.setText(subcategory.getString("sub_category_id"));
                                        if (sub_cats.length() == 1) {
                                            upperline.setVisibility(View.INVISIBLE);
                                            bottomline.setVisibility(View.INVISIBLE);
                                        } else {
                                            if (countersub == 1) {
                                                upperline.setVisibility(View.INVISIBLE);
                                            } else {
                                                if (countersub == sub_cats.length()) {
                                                    bottomline.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        }
                                        bottomline.setVisibility(View.VISIBLE);
                                        subcategorysection.addView(listItemBinding.getRoot());

                                    }
                                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.magenative_rotate);
                                    sampleFadeAnimation.setRepeatCount(1);
                                    sampleFadeAnimation.setFillAfter(true);
                                    rotateimage.startAnimation(sampleFadeAnimation);

                                } else {
                                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.magenative_rotatereverse);
                                    sampleFadeAnimation.setRepeatCount(1);
                                    sampleFadeAnimation.setFillAfter(true);
                                    rotateimage.startAnimation(sampleFadeAnimation);
                                    subcategorysection.removeAllViews();
                                }
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), Ced_New_Product_Listing.class);
                            intent.putExtra("ID", maincid.getText().toString());
                            startActivity(intent);
                            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            mDrawerLayout.closeDrawers();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
//                if (special_category_name_flag && special_category_id_flag)
                fragmentDrawerBinding.sidemenus.addView(categorySectionBinding.getRoot());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //----------------specialcategory
    }

    private void createsidedrawercategories() {
        try {
            JSONArray jsonObject = new JSONArray(cedSessionManagement.getCategories());
            for (int i = 0; i < Objects.requireNonNull(jsonObject).length(); i++) {
                JSONObject cat = jsonObject.getJSONObject(i);
                MagenativeMaincategorysectionBinding categorySectionBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_maincategorysection, fragmentDrawerBinding.sidemenus, false);
                final LinearLayout maincatlayout = categorySectionBinding.MageNativeMaincat;
                final TextView lblListHeader = categorySectionBinding.MageNativeLblListHeader;
                TextView maincatid = categorySectionBinding.MageNativeMaincatid;
                ImageView expandxlose = categorySectionBinding.MageNativeExpandxlose;
                if (cat.has("main_category_name")) {
                    lblListHeader.setText(cat.getString("main_category_name"));
                }
                if (cat.has("main_category_id")) {
                    maincatid.setText(cat.getString("main_category_id"));
                }
                if (cat.getBoolean("has_child")) {
                    expandxlose.setVisibility(View.VISIBLE);
                }
                maincatlayout.setOnClickListener(v -> {
                    RelativeLayout maincatrelative = (RelativeLayout) maincatlayout.getChildAt(0);
                    final LinearLayout subcategorysection = (LinearLayout) maincatlayout.getChildAt(1);
                    TextView maincatname = (TextView) maincatrelative.getChildAt(0);
                    TextView maincid = (TextView) maincatrelative.getChildAt(4);
                    ImageView rotateimage = (ImageView) maincatrelative.getChildAt(3);
                    try {
                        if (cat.has("sub_cats")) {
                            if (cat.getJSONArray("sub_cats").length() > 0) {
                                if (subcategorysection.getChildCount() == 0) {
                                    JSONArray sub_cats = cat.getJSONArray("sub_cats");
                                    int countersub = 0;
                                    for (int j = 0; j < sub_cats.length(); j++) {
                                        JSONObject subcategory = sub_cats.getJSONObject(j);
                                        countersub = countersub + 1;
                                        MagenativeListItemBinding listItemBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_list_item, null, false);
                                        final RelativeLayout subrelat = listItemBinding.MageNativeSubrelat;
                                        subrelat.setOnClickListener(v1 -> {
                                            TextView subcatid = (TextView) subrelat.getChildAt(2);
                                            Intent intent = new Intent(getActivity(), Ced_New_Product_Listing.class);
                                            intent.putExtra("ID", subcatid.getText().toString());
                                            startActivity(intent);
                                            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                            mDrawerLayout.closeDrawers();

                                        });
                                        TextView upperline = listItemBinding.MageNativeUpperline;
                                        TextView bottomline = listItemBinding.MageNativeBottomline;
                                        TextView lblListItem = listItemBinding.MageNativeLblListItem;
                                        ((Ced_NavigationActivity) getActivity()).set_regular_font_fortext(lblListItem);
                                        TextView lblListItemid = listItemBinding.MageNativeLblListItemid;
                                        lblListItem.setText(subcategory.getString("sub_category_name"));
                                        lblListItemid.setText(subcategory.getString("sub_category_id"));
                                        if (sub_cats.length() == 1) {
                                            upperline.setVisibility(View.INVISIBLE);
                                            bottomline.setVisibility(View.INVISIBLE);
                                        } else {
                                            if (countersub == 1) {
                                                upperline.setVisibility(View.INVISIBLE);
                                            } else {
                                                if (countersub == sub_cats.length()) {
                                                    bottomline.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        }
                                        bottomline.setVisibility(View.VISIBLE);
                                        subcategorysection.addView(listItemBinding.getRoot());

                                    }
                                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.magenative_rotate);
                                    sampleFadeAnimation.setRepeatCount(1);
                                    sampleFadeAnimation.setFillAfter(true);
                                    rotateimage.startAnimation(sampleFadeAnimation);

                                } else {
                                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.magenative_rotatereverse);
                                    sampleFadeAnimation.setRepeatCount(1);
                                    sampleFadeAnimation.setFillAfter(true);
                                    rotateimage.startAnimation(sampleFadeAnimation);
                                    subcategorysection.removeAllViews();
                                }
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), Ced_New_Product_Listing.class);
                            intent.putExtra("ID", maincid.getText().toString());
                            startActivity(intent);
                            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            mDrawerLayout.closeDrawers();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                fragmentDrawerBinding.sidemenus.addView(categorySectionBinding.getRoot());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = requireActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                requireActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                requireActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(() -> mDrawerToggle.syncState());
    }

    /*public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }*/

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

    public void open() {
        mDrawerLayout.openDrawer(containerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        accessresult = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
    }
}
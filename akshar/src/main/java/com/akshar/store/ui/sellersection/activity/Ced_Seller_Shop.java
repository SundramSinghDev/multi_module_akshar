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
 *           * @license   http://cedcommerce.com/license-agreement.txt
 *
 */
package com.akshar.store.ui.sellersection.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.base.utilapplication.AnalyticsApplication;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.MagenativeListViewBinding;
import com.akshar.store.databinding.MagenativeNoProductInShopBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.productsection.adapter.RecyclerGridViewAdapter;
import com.akshar.store.ui.sellersection.adapter.Ced_SortAdapter;
import com.akshar.store.ui.sellersection.viewmodel.SellerShopViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_Seller_Shop extends Ced_NavigationActivity
{
    MagenativeListViewBinding magenativeListViewBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    SellerShopViewModel sellerShopViewModel;

    HashMap<String,String> subfilters;
    public static JSONObject object;
    boolean firstselected=true;
    HashMap<String,String>filterlabelcode;
    HashMap<String,String>filtercodelabel;
    boolean clearfilternavigation=true;
    JSONArray products = null;
    JSONArray filters = null;
    JSONArray catfilters = null;
    JSONArray filter_label = null;
    JSONArray sorts = null;
    ArrayList<HashMap<String, String>> ProductData;
    int current = 1;
    HashMap<String,ArrayList<String>> catfilterdata;
    ViewStub filterlist,categoryfilterlist;
    LinearLayout sortby;
    AppCompatButton categoryfilter;
    LinearLayout filter;
    private RecyclerView recyclerView;
    boolean load = true;
    char[]id=new char[10];
    String filterid="";
    static String order="";
    String order2="";
    static  String dir="";
    String filterval="";
    private String ID="";
    TextView filterselection;
    ImageView filterselection2;
    HashMap<String,String>keyvalfilter;
    ArrayList<HashMap<String, String>> keyvalsort2;
    HashMap<String,HashMap<String,String>>keyvalfilter2;
    static  String  filter_name="",filter_id="",filter_parent="",filter_parent_id="";
    Dialog dialog;
    Ced_SortAdapter sortAdapter;
    ArrayList<String> IDS;
    static HashMap<String,String> productsids;
    LinearLayout applyandclear;
    LinearLayout catapplyandclear;
    LinearLayout catbelowline;
    LinearLayout catdata;
    JsonObject dataforproductlist;
    TextView seletedfilters;
    TextView sorttoshow;
    LinearLayout filtertags;
    LinearLayout filtervalues;
    boolean tagstoshow=true;
    boolean filtertoshow=true;
    boolean catfiltertoshow=true;
    static String  selectedorder=" ";
    static   String selecteddir=" ";
    ScrollView filterscroll;
    String categoryidstofilter="";
    ArrayList<String> categoryfilterdataarraylist;
    CardView belowheader;
    String phonenumber="",supportemail="",shop_url="";
    public static final String KEY_ITEM = "data";
    public static final String KEY_SUB_ITEM = "products";
    public static  final String KEY_SUB_ITEM2 = "filter";
    public static  final String KEY_SUB_ITEM3 = "sort";
    public static  final String KEY_ID = "product_id";
    public static  final String KEY_NAME = "product_name";
    public static  final String KEY_Image = "product_image";
    public static  final String KEY_Review = "review";
    public static  final String KEY_Price = "regular_price";
    public static  final String KEY_Price_special = "special_price";
    ArrayList<HashMap<String,String>> gridviewdata;
    RecyclerGridViewAdapter mageNative_gridViewAdapter;
    //------------
    NestedScrollView nestedScrollView;
    GridLayoutManager gridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        magenativeListViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_list_view, content, true);
        init();
        sellerShopViewModel = new ViewModelProvider(Ced_Seller_Shop.this, viewModelFactory).get(SellerShopViewModel.class);
        productsids=new HashMap<String,String>();
        dataforproductlist=new JsonObject();
        categoryfilterdataarraylist=new ArrayList<String>();
        Bundle extra = getIntent().getExtras();
        ID=extra.getString("ID");
        try
        {
            dataforproductlist.addProperty("vendor_id", ID);
            dataforproductlist.addProperty("page", String.valueOf(current));
            catfilterdata=new HashMap<String,ArrayList<String>>();
            gridviewdata=new ArrayList<HashMap<String, String>>();
            if(cedSessionManagement.getStoreId()!=null)
            {
                dataforproductlist.addProperty("store_id", cedSessionManagement.getStoreId());
            }
            if(session.isLoggedIn())
            {
                dataforproductlist.addProperty("customer_id", session.getCustomerid());
            }
            filterlabelcode=new HashMap<String,String>();
            filtercodelabel=new HashMap<String,String>();
            if (extra.getString("MULTIURL")!=null)
            {
                dataforproductlist.addProperty("multi_filter", extra.getString("multifilter"));
                if(extra.getString("order")!=null)
                {
                    dataforproductlist.addProperty("order",extra.getString("order"));
                    dataforproductlist.addProperty("dir", extra.getString("dir"));
                }
            }
            if(extra.getString("CATURL")!=null)
            {
                categoryidstofilter=extra.getString("catstring");
                dataforproductlist.addProperty("catfilter", extra.getString("catstring"));
                categoryfilterdataarraylist=extra.getStringArrayList("catstringarraylist");
                if(extra.getString("order")!=null)
                {
                    dataforproductlist.addProperty("order", extra.getString("order"));
                    dataforproductlist.addProperty("dir", extra.getString("dir"));
                }
            }
            if(extra.getString("SORTURL")!=null)
            {

                dataforproductlist.addProperty("dir", extra.getString("dir"));
                if(extra.getString("multifilter")!=null)
                {
                    dataforproductlist.addProperty("multi_filter",extra.getString("multifilter"));
                }
                if(extra.getString("catstring")!=null)
                {
                    dataforproductlist.addProperty("catfilter", extra.getString("catstring"));
                }
            }
            IDS=new ArrayList<String>();
            subfilters=new HashMap<String,String>();
            keyvalfilter2=new HashMap<String,HashMap<String,String>>();
            ProductData = new ArrayList<HashMap<String, String>>();
            keyvalsort2=new ArrayList<HashMap<String, String>>();
            recyclerView = magenativeListViewBinding.MageNativeList;
            gridLayoutManager = new GridLayoutManager(Ced_Seller_Shop.this, 1);
            recyclerView.setLayoutManager(gridLayoutManager);
            //recyclerView.addHeaderView(sellerproductheader);
        }
        catch (Exception e)
        {
            Intent main=new Intent(Ced_Seller_Shop.this,Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
        }


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && load) {
                    Log.i("onScrollStateChanged", "loadmore");
                    Log.i("onScrollStateChanged", "loadmore");
                    //-------------------
                    current = current + 1;
                    dataforproductlist.addProperty("page", String.valueOf(current));
                    load = false;
                    sellerShopViewModel.getSellerShopData(Ced_Seller_Shop.this, dataforproductlist).observe(Ced_Seller_Shop.this, Ced_Seller_Shop.this::consumepaginationResponse);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        sellerShopViewModel.getSellerShopData(Ced_Seller_Shop.this, dataforproductlist).observe(this, this::consumeResponse);
    }


    private void consumepaginationResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                applydata2_(Objects.requireNonNull(apiResponse.data));
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }

    }
    private void init() {
        nestedScrollView= magenativeListViewBinding.nestedscroll;
    }

    private void applydata2_(String Jstring)  {
        if (Jstring.contains("NO_PRODUCTS") || Jstring.equalsIgnoreCase("[]"))
        {
            load = false;
        }
        else
        {
            try {
                JSONObject   jsonObject = new JSONObject(Jstring);
                if(jsonObject.getJSONObject(KEY_ITEM).has("status") && jsonObject.getJSONObject(KEY_ITEM).getString("status").equals("enables"))
                {
                    applydata2(Jstring);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void consumeResponse(ApiResponse apiResponse)  {
        switch (apiResponse.status) {
            case SUCCESS:
                if((apiResponse.status).toString().contains("NO_PRODUCTS")|| (apiResponse.status).toString().equalsIgnoreCase("[]"))
                {
                    MagenativeNoProductInShopBinding magenativeNoProductInShopBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_no_product_in_shop, content, true);
                    magenativeNoProductInShopBinding.MagenativeClear.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            finish();
                        }
                    });
                }
                else
                {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(apiResponse.data));
                        if(jsonObject.has("header") && jsonObject.getString("header").equals("false"))
                        {
                            Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            applydata(Objects.requireNonNull(apiResponse.data));
                            changeview();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }

    }

    public void applydata(String Jstring)
    {
        try
        {
            magenativeListViewBinding.nestedscroll.setVisibility(View.VISIBLE);
            JSONObject jsonObj = new JSONObject(Jstring);
            products = jsonObj.getJSONObject(KEY_ITEM).getJSONArray(KEY_SUB_ITEM);
            Glide.with(Ced_Seller_Shop.this)
                    .load(jsonObj.getJSONObject(KEY_ITEM).getString("banner_url"))
                    .placeholder(R.drawable.bannerplaceholder)
                    .error(R.drawable.bannerplaceholder)
                    .into(magenativeListViewBinding.MagenativeComapnybanner);
            TextView writereview= magenativeListViewBinding.MagenativeWritereview;
            if(session.isLoggedIn())
            {
                writereview.setVisibility(View.VISIBLE);
            }
            TextView vendorname= magenativeListViewBinding.MagenativeVendorname;
            final TextView ShopName= magenativeListViewBinding.MagenativeShopName;
            final TextView averagerating= magenativeListViewBinding.MagenativeAveragerating;
            final TextView address= magenativeListViewBinding.address;
            final TextView reviewcount= magenativeListViewBinding.MagenativeReviewcount;
            TextView email= magenativeListViewBinding.MagenativeEmail;
            TextView comapanytag= magenativeListViewBinding.MagenativeComapanytag;
            TextView contact= magenativeListViewBinding.MagenativeContact;
            TextView vendorsincetag= magenativeListViewBinding.vendorsincetag;
            TextView supporttext=magenativeListViewBinding.MagenativeSupporttext;
            TextView aboutvendor= magenativeListViewBinding.MagenativeAboutvendor;
            TextView company= magenativeListViewBinding.MagenativeCompany;
            TextView vendorsince= magenativeListViewBinding.MagenativeVendorsince;
            RelativeLayout phonenumberasection= magenativeListViewBinding.MagenativePhonenumberasection;
            RelativeLayout supportemailsection= magenativeListViewBinding.MagenativeSupportemailsection;

            set_regular_font_fortext(ShopName);
            set_regular_font_fortext(vendorname);
            set_regular_font_fortext(writereview);
            set_regular_font_fortext(averagerating);
            set_regular_font_fortext(address);
            set_regular_font_fortext(reviewcount);
            set_regular_font_fortext(comapanytag);
            set_regular_font_fortext(email);
            set_regular_font_fortext(aboutvendor);
            set_regular_font_fortext(contact);
            set_regular_font_fortext(supporttext);
            set_regular_font_fortext(vendorsincetag);
            set_regular_font_fortext(company);
            set_regular_font_fortext(vendorsince);
            phonenumberasection.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(phonenumber.isEmpty())
                    {
                        showmsg(getResources().getString(R.string.thisvedorhavenotprovidedanysupportnumber));
                    }
                    else
                    {
                        Uri number = Uri.parse("tel:" + phonenumber);
                        Intent dial = new Intent(Intent.ACTION_DIAL, number);
                        startActivity(dial);
                    }
                }
            });
            supportemailsection.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(supportemail.isEmpty())
                    {
                        showmsg(getResources().getString(R.string.thevendornotprovidedsupportmail));
                    }
                    else
                    {
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, supportemail);
                        email.setType("message/rfc822");
                        startActivity(Intent.createChooser(email, "Choose an Email client :"));
                    }
                }
            });

            writereview.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(session.isLoggedIn())
                    {
                        try
                        {
                            Intent intent=new Intent(getApplicationContext(),Ced_AddVendorReview.class);
                            intent.putExtra("vendor_id",ID);
                            intent.putExtra("ShopName",ShopName.getText().toString());
                            intent.putExtra("reviewcount",reviewcount.getText().toString());
                            intent.putExtra("averagerating",averagerating.getText().toString());
                            intent.putExtra("address",address.getText().toString());
                            intent.putExtra("banner_url",jsonObj.getJSONObject(KEY_ITEM).getString("banner_url"));
                            startActivity(intent);

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        showmsg(getResources().getString(R.string.loginfirstto_postreview));
                    }
                }
            });


            JSONObject vendor_profile=jsonObj.getJSONObject(KEY_ITEM).getJSONObject("vendor_profile");
            if(jsonObj.getJSONObject(KEY_ITEM).has("review_count"))
            {
                reviewcount.setText(jsonObj.getJSONObject(KEY_ITEM).getString("review_count")+" Reviews.");
            }
            else
            {
                reviewcount.setVisibility(View.INVISIBLE);
            }

            if(jsonObj.getJSONObject(KEY_ITEM).has("vendor_review"))
            {
                if(jsonObj.getJSONObject(KEY_ITEM).getString("vendor_review").equals("false"))
                {
                    averagerating.setVisibility(View.INVISIBLE);
                }
                else
                {
                    averagerating.setText(jsonObj.getJSONObject(KEY_ITEM).getString("vendor_review"));
                    Float aFloat=Float.valueOf(averagerating.getText().toString());
                    if(aFloat<2)
                    {
                        averagerating.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                    }
                    if(aFloat>=2&&aFloat<4)
                    {
                        averagerating.setBackground(getResources().getDrawable(R.drawable.round_corner_yellow));
                        averagerating.setTextColor(getResources().getColor(R.color.black));
                    }
                    if(aFloat>=4)
                    {
                        averagerating.setBackground(getResources().getDrawable(R.drawable.round_corner));
                    }
                }
            }
            else
            {
                averagerating.setVisibility(View.INVISIBLE);
            }
            if(vendor_profile.has("public_name"))
            {
                vendorname.setText(vendor_profile.getString("public_name"));
                ShopName.setText(vendor_profile.getString("public_name"));

            }
            if(vendor_profile.has("city")&&vendor_profile.has("country_id"))
            {
                if(vendor_profile.getString("city").isEmpty()||vendor_profile.getString("city").equals("null"))
                {
                    address.setText(getResources().getString(R.string.noinforegardingaddress));
                }
                else
                {
                    if(vendor_profile.getString("country_id").isEmpty()||vendor_profile.getString("country_id").equals("null"))
                    {
                        address.setText(getResources().getString(R.string.noinforegardingaddress));
                    }
                    else
                    {
                        address.setText(vendor_profile.getString("city")+", "+vendor_profile.getString("country_id"));
                    }
                }

            }
            else
            {
                address.setText(getResources().getString(R.string.noinforegardingaddress));
            }
            if (vendor_profile.has("profile_picture"))
            {
                Glide.with(Ced_Seller_Shop.this)
                        .load(vendor_profile.getString("profile_picture"))
                        .placeholder(R.drawable.gust_b)   // optional
                        .error(R.drawable.gust_b) // optional// optional// optional
                        .into(magenativeListViewBinding.MagenativeProfilepic);
            }
            else
            {
                magenativeListViewBinding.MagenativeProfilepic.setImageResource(R.drawable.gust_b);
            }
            if(vendor_profile.has("email"))
            {
                email.setText(vendor_profile.getString("email"));
            }
            if(vendor_profile.has("company_name"))
            {
                company.setText(vendor_profile.getString("company_name"));
            }
            if(vendor_profile.has("created_at"))
            {
                vendorsince.setText(vendor_profile.getString("created_at"));
            }

            if(vendor_profile.has("support_number"))
            {
                phonenumber=vendor_profile.getString("support_number");
            }
            if(vendor_profile.has("support_number"))
            {
                phonenumber=vendor_profile.getString("support_number");
            }
            if(vendor_profile.has("shop_url"))
            {
                shop_url=vendor_profile.getString("shop_url");
            }
            for (int i = 0; i < products.length(); i++)
            {
                JSONObject c = null;
                object = products.getJSONObject(i);
                HashMap<String,String> grid_data=new HashMap<String,String>();
                if(object.has("type"))
                    grid_data.put("type",object.getString("type"));
                grid_data.put("product_image",object.getString(KEY_Image));
                grid_data.put("product_id",object.getString(KEY_ID));
                grid_data.put("product_name",object.getString(KEY_NAME));
                grid_data.put("special_price",object.getString(KEY_Price_special));
                grid_data.put("regular_price",object.getString(KEY_Price));
                grid_data.put("Inwishlist",object.getString("Inwishlist"));
                grid_data.put("wishlist_item_id","1");
                grid_data.put("stock_status", object.getString("stock_status"));
                if (object.has("points")) {
                    grid_data.put("points", String.valueOf(object.getString("points")));
                } else {
                    grid_data.put("points", "");
                }

                if(object.has("offer"))
                {
                    grid_data.put("offer",object.getString("offer"));
                }
                else
                {
                    grid_data.put("offer","0");
                }
                if(object.has(KEY_Review))
                {
                    if(object.getString(KEY_Review).equals("null"))
                    {
                        grid_data.put("review","No_Review");
                    }
                    else
                    {
                        grid_data.put("review",object.getJSONObject(KEY_Review).getString("review-count"));
                    }
                }
                else
                {
                    grid_data.put("review","No_Review");
                }
                gridviewdata.add(grid_data);
            }
            mageNative_gridViewAdapter=new RecyclerGridViewAdapter(this,gridviewdata,false);
           /* recyclerView.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
            recyclerView.setDividerHeight(0);
            recyclerView.setAdapter(mageNative_gridViewAdapter);*/
            recyclerView.setAdapter(mageNative_gridViewAdapter);
            mageNative_gridViewAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition((gridviewdata.size())-(products.length()));
            recyclerView.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.VISIBLE);
            load = true;


        }
        catch (Exception w)
        {
            w.printStackTrace();
            Intent main=new Intent(Ced_Seller_Shop.this,Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
        }
    }
    public void applydata2(String Jstring) {
        try {

            JSONObject jsonObj = new JSONObject(Jstring);
            if(jsonObj.getJSONObject(KEY_ITEM).has(KEY_SUB_ITEM))
            {
                products = jsonObj.getJSONObject(KEY_ITEM).getJSONArray(KEY_SUB_ITEM);
                for (int i = 0; i < products.length(); i++)
                {
                    JSONObject c = null;
                    object = products.getJSONObject(i);
                    HashMap<String,String> grid_data=new HashMap<String,String>();
                    grid_data.put("type",object.getString("type"));
                    grid_data.put("product_image",object.getString(KEY_Image));
                    grid_data.put("product_id",object.getString(KEY_ID));
                    grid_data.put("product_name",object.getString(KEY_NAME));
                    grid_data.put("special_price",object.getString(KEY_Price_special));
                    grid_data.put("regular_price",object.getString(KEY_Price));
                    grid_data.put("Inwishlist",object.getString("Inwishlist"));
                    grid_data.put("wishlist_item_id","1");
                    if(object.has("offer"))
                    {
                        grid_data.put("offer",object.getString("offer"));
                    }
                    else
                    {
                        grid_data.put("offer","0");
                    }
                    if(object.has(KEY_Review))
                    {
                        if(object.getString(KEY_Review).equals("null"))
                        {
                            grid_data.put("review","No_Review");
                        }
                        else
                        {
                            grid_data.put("review",object.getString(KEY_Review));
                        }

                    } else {
                        grid_data.put("review","No_Review");
                    }
                    gridviewdata.add(grid_data);
                }
            }

            mageNative_gridViewAdapter=new RecyclerGridViewAdapter(this,gridviewdata,false);
            /*int cp = recyclerView.getFirstVisiblePosition();
            recyclerView.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
            recyclerView.setDividerHeight(0);
            recyclerView.setAdapter(mageNative_gridViewAdapter);
            recyclerView.setSelectionFromTop(cp + 1, 0);
            mageNative_gridViewAdapter.notifyDataSetChanged();*/


            recyclerView.setAdapter(mageNative_gridViewAdapter);
            mageNative_gridViewAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition((gridviewdata.size())-(products.length()));
            recyclerView.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.VISIBLE);
            load = true;

        }
        catch (Exception w)
        {
            w.printStackTrace();
            Intent main=new Intent(Ced_Seller_Shop.this,Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
        }
    }
    private void showdialog()
    {
        dialog = new Dialog(this,R.style.PauseDialog);
        ((ViewGroup)((ViewGroup)dialog.getWindow().getDecorView()).getChildAt(0)) //ie LinearLayout containing all the dialog (title, titleDivider, content)
                .getChildAt(1) // ie the view titleDivider
                .setBackgroundColor(getResources().getColor(R.color.lighter_gray));
        dialog.setTitle(Html.fromHtml("<center><font color='#444444'>Sort Items by :</font></center>"));
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.magenative_sortitemlist, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        ListView list1 = (ListView) dialog.findViewById(R.id.MageNative_sortlistview);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                dialog.dismiss();
                JSONObject object=new JSONObject();
                TextView direction=(TextView)view.findViewById(R.id.MageNative_sortDirection);
                TextView label=(TextView)view.findViewById(R.id.MageNative_SortLabel);
                selectedorder=label.getText().toString();
                selecteddir=direction.getText().toString();
                char sortparam[]=label.getText().toString().toCharArray();
                for(int c=0;c<sortparam.length;c++)
                {
                    if (sortparam[c] == ':')
                    {
                        for (int C = 0; C < c; C++)
                        {
                            order2 += sortparam[C];
                        }
                    }
                }
                order=order2;
                dir=direction.getText().toString();
                Intent intent = new Intent(Ced_Seller_Shop.this, Ced_Seller_Shop.class);
                if(AnalyticsApplication.seller_main_filters.size()<=0)
                {
                    if(categoryidstofilter.isEmpty())
                    {
                        intent.putExtra("order", order);
                        intent.putExtra("dir", dir);
                        intent.putExtra("ID",ID);
                    }
                    else
                    {
                        intent.putExtra("order", order);
                        intent.putExtra("dir", dir);
                        intent.putExtra("ID",ID);
                        intent.putExtra("catstring",categoryidstofilter);
                    }
                }
                else
                {
                    Iterator iterator1=AnalyticsApplication.seller_main_filters.entrySet().iterator();
                    while (iterator1.hasNext())
                    {
                        Map.Entry pair = (Map.Entry) iterator1.next();
                        String key = String.valueOf(pair.getKey());
                        ArrayList<String> value = (ArrayList<String>) pair.getValue();
                        Iterator innerlist=value.iterator();
                        JSONObject object1=new JSONObject();
                        while(innerlist.hasNext())
                        {
                            String id_name= (String) innerlist.next();
                            String parts[]=id_name.split("#");
                            try
                            {
                                object1.put(parts[0],parts[1]);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();

                                Intent main=new Intent(Ced_Seller_Shop.this,Ced_MainActivity.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(main);
                                e.printStackTrace();
                            }


                        }
                        try
                        {
                            object.put(key,object1);
                        }
                        catch (JSONException e)
                        {

                            e.printStackTrace();
                            Intent main=new Intent(Ced_Seller_Shop.this,Ced_MainActivity.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                            e.printStackTrace();
                        }
                    }
                    intent.putExtra("order", order);
                    intent.putExtra("dir", dir);
                    intent.putExtra("ID",ID);
                    intent.putExtra("multifilter",object.toString());
                }
                intent.putExtra("SORTURL", "SORT");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                clearfilternavigation=false;
                startActivity(intent);
            }
        });
        sortAdapter = new Ced_SortAdapter(this, keyvalsort2,selectedorder,selecteddir);
        list1.setAdapter(sortAdapter);
        dialog.show();

    }
    @Override
    public void onBackPressed()
    {
        AnalyticsApplication.seller_main_filters.clear();
        order="";
        dir="";
        selecteddir="";
        selectedorder="";
        clearfilternavigation=false;
        super.onBackPressed();
    }


    public boolean containskeyalready(String checkbox,String parent,String id)
    {
        if(AnalyticsApplication.seller_main_filters.size()>0)
        {
            if(AnalyticsApplication.seller_main_filters.containsKey(parent))
            {
                ArrayList<String> recyclerView=AnalyticsApplication.seller_main_filters.get(parent);
                if(recyclerView.contains(id+"#"+checkbox))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }

        }
        else
        {
            return false;
        }



    }
    public void filterdatalabel(String Jstring) throws JSONException
    {
        JSONObject  jsonObj = new JSONObject(Jstring);
        filter_label = jsonObj.getJSONObject(KEY_ITEM).getJSONArray("filter_label");
        for(int l=0;l<filter_label.length();l++)
        {
            JSONObject obj2 = (JSONObject) filter_label.getJSONObject(l);
            filterlabelcode.put(obj2.getString("att_code"),obj2.getString("att_label"));
            filtercodelabel.put(obj2.getString("att_label"),obj2.getString("att_code"));
        }
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    public void catlistfilters()
    {
        categoryfilterlist.setVisibility(View.VISIBLE);
        Iterator iterator1=catfilterdata.entrySet().iterator();
        while (iterator1.hasNext())
        {
            Map.Entry pair = (Map.Entry) iterator1.next();
            String key = String.valueOf(pair.getKey());
            String parts[]=key.split("#");
            LinearLayout finallayoutcat=new LinearLayout(Ced_Seller_Shop.this);
            finallayoutcat.setOrientation(LinearLayout.VERTICAL);
            LinearLayout maincat=new LinearLayout(Ced_Seller_Shop.this);
            maincat.setOrientation(LinearLayout.HORIZONTAL);
            final CheckBox maincatname=new CheckBox(Ced_Seller_Shop.this);
            maincatname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        LinearLayout layout = (LinearLayout) maincatname.getParent();
                        TextView view = (TextView) layout.getChildAt(1);
                        if(!categoryfilterdataarraylist.contains(view.getText().toString()))
                        {
                            categoryfilterdataarraylist.add(view.getText().toString());
                        }

                    } else {
                        LinearLayout layout = (LinearLayout) maincatname.getParent();
                        TextView view = (TextView) layout.getChildAt(1);
                        categoryfilterdataarraylist.remove(view.getText().toString());

                    }
                }
            });
            maincatname.setText(parts[1].toUpperCase());
            maincatname.setTypeface(Typeface.DEFAULT_BOLD);
            TextView maincatid=new TextView(Ced_Seller_Shop.this);
            maincatid.setText(parts[0]);
            maincatid.setVisibility(View.INVISIBLE);
            maincat.addView(maincatname, 0);
            maincat.addView(maincatid,1);
            if(categoryfilterdataarraylist.contains(parts[0]))
            {

                maincatname.setChecked(true);
            }
            else
            {
                maincatname.setChecked(false);
            }
            finallayoutcat.addView(maincat,0);
            ArrayList<String> value = (ArrayList<String>) pair.getValue();
            Iterator innerlist = value.iterator();
            JSONObject object1 = new JSONObject();
            LinearLayout subcatlayout=new LinearLayout(Ced_Seller_Shop.this);
            subcatlayout.setOrientation(LinearLayout.VERTICAL);
            subcatlayout.setPadding(5,5,5,5);
            while (innerlist.hasNext())
            {
                String id_name = (String) innerlist.next();
                String subparts[]=id_name.split("#");
                if(subparts[1].equals("subcategories"))
                {

                }
                else
                {
                    LinearLayout layout=new LinearLayout(Ced_Seller_Shop.this);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    final CheckBox subcatname=new CheckBox(Ced_Seller_Shop.this);
                    subcatname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                    {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                        {
                            if(isChecked)
                            {
                                LinearLayout layout= (LinearLayout) subcatname.getParent();
                                TextView view= (TextView) layout.getChildAt(0);
                                if(!categoryfilterdataarraylist.contains(view.getText().toString()))
                                {
                                    categoryfilterdataarraylist.add(view.getText().toString());
                                }

                            }
                            else
                            {
                                LinearLayout layout= (LinearLayout) subcatname.getParent();
                                TextView view= (TextView) layout.getChildAt(0);
                                categoryfilterdataarraylist.remove(view.getText().toString());
                            }
                        }
                    });
                    subcatname.setText(subparts[1]);
                    TextView subcatid=new TextView(Ced_Seller_Shop.this);
                    subcatid.setText(subparts[0]);

                    subcatid.setVisibility(View.INVISIBLE);

                    layout.addView(subcatid, 0);
                    layout.addView(subcatname, 1);

                    subcatlayout.addView(layout);
                    if(categoryfilterdataarraylist.contains(subparts[0]))
                    {

                        subcatname.setChecked(true);
                    }
                    else
                    {
                        subcatname.setChecked(false);
                    }

                }
            }
            finallayoutcat.setPadding(5,5,5,0);
            finallayoutcat.addView(subcatlayout, 1);
            catdata.addView(finallayoutcat);
        }


    }
    public void catfilterdata(String Jstring)
    {
        try
        {
            JSONObject jsonObj = new JSONObject(Jstring);
            catfilters = jsonObj.getJSONObject(KEY_ITEM).getJSONArray("category-filter");

            for (int i = 0; i < catfilters.length(); i++)
            {
                JSONObject obj2 = (JSONObject) catfilters.getJSONObject(i);

                ArrayList<String> subcategories=new ArrayList<String>();
                if(obj2.has("sub_categories"))
                {

                    JSONArray jsonArray=obj2.getJSONArray("sub_categories");
                    for(int j=0;j<jsonArray.length();j++)
                    {
                        JSONObject obj = (JSONObject) jsonArray.getJSONObject(j);
                        subcategories.add(obj.getString("main_category"));

                    }
                }
                else
                {
                    subcategories.add("NO#subcategories");
                }
                if(obj2.has("main_category"))
                {
                    catfilterdata.put(obj2.getString("main_category"),subcategories);
                }

            }

        }
        catch (Exception w)
        {
            w.printStackTrace();
            Intent main=new Intent(Ced_Seller_Shop.this,Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
        }
    }
    private void changeview()
    {
        //filterlist.setVisibility(View.GONE);
        mageNative_gridViewAdapter=new RecyclerGridViewAdapter(this,gridviewdata,false);
        recyclerView.setAdapter(mageNative_gridViewAdapter);
    }
    public void show()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                belowheader.setVisibility(View.INVISIBLE);
            }
        }, 1000);
    }
}
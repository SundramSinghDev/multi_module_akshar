
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
package com.akshar.store.ui.searchsection.activity;
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

import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.base.utilapplication.AnalyticsApplication;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.NewProductListingBinding;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.productsection.adapter.RecyclerGridViewAdapter;
import com.akshar.store.ui.searchsection.viewmodel.SearchViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_Searchview extends Ced_NavigationActivity {
    int lastlength=0;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    NewProductListingBinding listingBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    SearchViewModel searchViewModel;
    String category_id=null;
    JsonObject dataforproductlist;
    String theme = "";
    boolean list = false;
    boolean gridlayout = true;
    ArrayList<HashMap<String, String>> keyvalsort2;
    static String selectedorder = " ";
    static String selecteddir = " ";
    static String order = "";
    static String dir = "";
    GridLayoutManager gridLayoutManager;
    ArrayList<HashMap<String, String>> gridviewdata;
    RecyclerGridViewAdapter mageNative_gridViewAdapter;
    boolean load = true;
    private boolean loading = false;
    int current = 1;
    //------------------
    JSONArray suggestion = null;
    String product_name;
    ArrayList<String> suggestions;
    AutoCompleteTextView search;
    JsonObject searchdata;
    boolean autocomplete = true;
    boolean request = true;
    HashMap<String, String> name_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = new ViewModelProvider(Ced_Searchview.this, viewModelFactory).get(SearchViewModel.class);
        listingBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.new_product_listing, content, true);
        Objects.requireNonNull(getSupportActionBar()).hide();
        dataforproductlist = new JsonObject();
        keyvalsort2 = new ArrayList<>();
        theme = cedSessionManagement.getcategorytheme();
        gridviewdata = new ArrayList<>();
        //category_id = getIntent().getStringExtra("ID");
        listingBinding.newlistlayout.recycler.setHasFixedSize(false);
        gridLayoutManager = new GridLayoutManager(Ced_Searchview.this, 2, RecyclerView.VERTICAL, false);
        listingBinding.newlistlayout.recycler.setLayoutManager(gridLayoutManager);
        try {
            if (Objects.requireNonNull(getIntent().getExtras()).getString("SORTURL") != null) {
                keyvalsort2 = (ArrayList<HashMap<String, String>>) getIntent().getExtras().get("keyvalsort2");
                dataforproductlist.addProperty("order", getIntent().getExtras().getString("order"));
                dataforproductlist.addProperty("dir", getIntent().getExtras().getString("dir"));
            }
            dataforproductlist.addProperty("theme", theme);
            if (cedSessionManagement.getStoreId() != null) {
                dataforproductlist.addProperty("store_id", cedSessionManagement.getStoreId());
            }
            if (session.isLoggedIn()) {
                dataforproductlist.addProperty("customer_id", session.getCustomerid());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        setupsearchlayout();
        listingBinding.newlistlayout.conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        listingBinding.newlistlayout.nestedscroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        int visibleItemCount = gridLayoutManager.getChildCount();
                        int totalItemCount = gridLayoutManager.getItemCount();
                        int pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                        if ( !loading && load) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                Log.i("onScrollStateChanged", "loadmore");
                                loading = true;
                                //-------------------
                                current = current + 1;
                                load = false;
                                dataforproductlist.remove("page");
                                dataforproductlist.addProperty("page", current);

                                searchViewModel.getSearchListData(Ced_Searchview.this, dataforproductlist).observe(Ced_Searchview.this, apiResponse -> {
                                    switch (apiResponse.status){
                                        case SUCCESS:
                                            loading = false;
                                                if (apiResponse.data.contains("NO_PRODUCTS")|| apiResponse.data.equalsIgnoreCase("[]")) {
                                                    load = false;
                                                } else {
                                                    applydata(apiResponse.data);
                                                }

                                            break;

                                        case ERROR:
                                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                                            showmsg(getResources().getString(R.string.errorString));
                                            break;
                                    }
                                });

                            }
                        }
                    }
                }
            }
        });
        listingBinding.newlistlayout.MageNativeChangeview.setOnClickListener(v -> {
            if (list) {

                listingBinding.newlistlayout.MageNativeChangeview.setEnabled(false);
                gridlayout = true;
                listingBinding.newlistlayout.MageNativeChangeview.setImageResource(R.drawable.list_icon);
                list = false;
                gridLayoutManager = new GridLayoutManager(Ced_Searchview.this, 2,RecyclerView.VERTICAL, false);
                listingBinding.newlistlayout.recycler.setLayoutManager(gridLayoutManager);
                mageNative_gridViewAdapter = new RecyclerGridViewAdapter(Ced_Searchview.this, gridviewdata, gridlayout);
                listingBinding.newlistlayout.recycler.setAdapter(mageNative_gridViewAdapter);
                /*mageNative_gridViewAdapter.notifyDataSetChanged();*/
                animateRecyclerLayoutChange(2,gridLayoutManager,listingBinding.newlistlayout.recycler);
                listingBinding.newlistlayout.MageNativeChangeview.setEnabled(true);
                /*ObjectAnimator animation = ObjectAnimator.ofFloat(listingBinding.newlistlayout.MageNativeChangeview, "rotationY", 0.0f, 180f);
                animation.setDuration(100);
                animation.setRepeatCount(0);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.start();*/
            }
            else {
                listingBinding.newlistlayout.MageNativeChangeview.setEnabled(false);
                gridlayout = false;
                listingBinding.newlistlayout.MageNativeChangeview.setImageResource(R.drawable.grid_icon);
                list = true;
                gridLayoutManager = new GridLayoutManager(Ced_Searchview.this, 1, RecyclerView.VERTICAL, false);
                listingBinding.newlistlayout.recycler.setLayoutManager(gridLayoutManager);
                mageNative_gridViewAdapter = new RecyclerGridViewAdapter(Ced_Searchview.this, gridviewdata, gridlayout);
                listingBinding.newlistlayout.recycler.setAdapter(mageNative_gridViewAdapter);
                /*mageNative_gridViewAdapter.notifyDataSetChanged();*/
                animateRecyclerLayoutChange(1,gridLayoutManager,listingBinding.newlistlayout.recycler);
                listingBinding.newlistlayout.MageNativeChangeview.setEnabled(true);
                /*ObjectAnimator animation = ObjectAnimator.ofFloat(listingBinding.newlistlayout.MageNativeChangeview, "rotationY", 0.0f, 180f);
                animation.setDuration(100);
                animation.setRepeatCount(0);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.start();*/
            }
        });
    }

    private void getsearcheddatalist(JsonObject dataforproductlist,String category_id,int page) {
        Ced_Searchview.this.category_id=category_id;
        Ced_Searchview.this.current=page;
        dataforproductlist.addProperty("page",page);
        dataforproductlist.addProperty("q", category_id);
        searchViewModel.getSearchListData(Ced_Searchview.this, dataforproductlist).observe(Ced_Searchview.this, apiResponse -> {
            switch (apiResponse.status)
            {
                case SUCCESS:
                    if (apiResponse.data.contains("NO_PRODUCTS")|| apiResponse.data.equalsIgnoreCase("[]")) {
                       /* MagenativeNoProductListingBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_no_product_listing, content, true);
                        binding1.conti.setOnClickListener(v -> finish());*/
                        listingBinding.newlistlayout.noproductLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        applydata(apiResponse.data);
                    }
                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });

    }

    private void setupsearchlayout() {
        listingBinding.newlistlayout.MageNativeSortingsection.setVisibility(View.GONE);
        listingBinding.newlistlayout.searchviewLayout.setVisibility(View.VISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        searchdata = new JsonObject();
        suggestions = new ArrayList<>();
        name_id = new HashMap<String, String>();
        search = listingBinding.newlistlayout.MageNativeEditText1;
        search.requestFocus();
        listingBinding.newlistlayout.back.setOnClickListener(v -> onBackPressed());
        listingBinding.newlistlayout.voicesearch.setOnClickListener(v -> promptSpeechInput());
        listingBinding.newlistlayout.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!search.getText().toString().isEmpty())
                {
                    current=1;
                    gridviewdata.clear();
                    getsearcheddatalist(dataforproductlist,search.getText().toString(),1);
                }
                else
                {
                    search.setError("Enter some Keyword to proceed");
                }
            }
        });
        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                suggestions.clear();
                gridviewdata.clear();
                getsearcheddatalist(dataforproductlist,search.getText().toString(),1);
                return true;
            }
            return false;
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("currentLenght:", "" + s.length());
                if (s.length() == 0) {
                    lastlength=0;
                    current=1;
                    suggestions.clear();
                    gridviewdata.clear();
                    request = true;
                    Log.i("currentLenght1:", "" + suggestions.size());
                }
                else
                {
                    search.setError(null);
                }
                if (s.length() >= 3 && s.length() >= lastlength) {
                    lastlength=s.length();
                    if (request) {
                        request = false;
                        if (suggestions.size() == 0) {
                            try {
                                searchdata.addProperty("keyword", s.toString());
                                if (cedSessionManagement.getStoreId() != null)
                                {
                                    searchdata.addProperty("store_id", cedSessionManagement.getStoreId());
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            searchViewModel.getAutoCompleteData(Ced_Searchview.this, searchdata).observe(Ced_Searchview.this, apiResponse -> {
                                switch (apiResponse.status) {
                                    case SUCCESS:
                                        request = true;
                                        try {
                                            JSONObject jsonObject = new JSONObject(apiResponse.data);
                                            JSONObject data=jsonObject.getJSONObject("data");
                                            if (data.getString("enabled").equals("true")) {
                                                autocomplete = true;
                                                if (data.getString("status").equals("success"))
                                                {
                                                    suggestion = data.getJSONArray("suggestion");
                                                    for (int i = 0; i < suggestion.length(); i++)
                                                    {
                                                        JSONObject c = suggestion.getJSONObject(i);
                                                        product_name = c.getString("product_name");
                                                        name_id.put(product_name, c.getString("product_id"));
                                                        if (suggestions.size() > 0) {
                                                            if (suggestions.contains(product_name)) {
                                                                continue;
                                                            } else {
                                                                suggestions.add(product_name);
                                                            }
                                                        } else {
                                                            suggestions.add(product_name);
                                                        }
                                                    }
                                                    addautocomplete(s);

                                                }
                                                else {
                                                    if (data.getString("status").equals("empty")) {
                                                      //  showmsg(getResources().getString(R.string.NoProductFound));
                                                    }
                                                }
                                            } else {
                                                autocomplete = false;
                                                // showmsg(getResources().getString(R.string.NoProductFound));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(main);
                                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                        }

                                        break;

                                    case ERROR:
                                        request = true;
                                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                                        showmsg(getResources().getString(R.string.errorString));
                                        break;
                                }
                            });
                            getsearcheddatalist(dataforproductlist,search.getText().toString(),1);
                        }
                    }
                }
            }
        });
    }

    public void sortdata(String Jstring, JSONObject json) {
        try {
            JSONObject jsonObj = new JSONObject(Jstring);
            if( jsonObj.getJSONObject("data").has("sort"))
            {
                JSONObject sorts = jsonObj.getJSONObject("data").getJSONObject("sort");
                if (keyvalsort2.size() > 0) {
                    keyvalsort2.clear();
                }
                Iterator<String> keys= sorts.keys();
                while (keys.hasNext())
                {
                    String name = (String)keys.next();
                    String value = sorts.getString(name);
                    HashMap<String,String>keyvalsort=new HashMap<String, String>();
                    keyvalsort.put(name, value);
                    keyvalsort2.add(keyvalsort);
                }
            }
            createproductgrid(json);
            search.dismissDropDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void applydata(String Jstring) {
        try {

            search.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            JSONObject jsonObj = new JSONObject(Jstring);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                String status = jsonObj.getJSONObject("data").getString("status");
                if (status.equals("success")) {
                    sortdata(Jstring, jsonObj);
                } else {
                   //showmsg(jsonObj.getJSONObject("data").getString("message"));
                    if (jsonObj.getJSONObject("data").has("redirect_url")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(jsonObj.getJSONObject("data").getString("redirect_url")));
                        startActivity(browserIntent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        finish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createproductgrid(JSONObject jsonObj) {
        try {
            listingBinding.newlistlayout.noproductLayout.setVisibility(View.GONE);
            JSONArray productsjsonArray = jsonObj.getJSONObject("data").getJSONArray("products");
            for (int i = 0; i < productsjsonArray.length(); i++) {
                HashMap<String, String> grid_data = new HashMap<>();

                JSONObject object = productsjsonArray.getJSONObject(i);
                grid_data.put("type", object.getString("type"));
                grid_data.put("product_image", object.getString("product_image"));
                grid_data.put("product_id", object.getString("product_id"));
                grid_data.put("product_name", object.getString("product_name"));
                grid_data.put("stock_status",object.getString("stock_status"));
                if(object.has("special_price"))
                {
                    grid_data.put("special_price", object.getString("special_price"));
                }
                else
                {
                    grid_data.put("special_price", "no_special");
                }
                if(object.has("regular_price"))
                {
                    grid_data.put("regular_price",object.getString("regular_price"));
                }
                else
                {
                    if(object.has("final_price"))
                    {
                        grid_data.put("regular_price",object.getString("final_price"));
                    }
                    else
                    {
                        grid_data.put("regular_price","");
                    }
                }
                grid_data.put("Inwishlist", object.getString("Inwishlist"));
                grid_data.put("wishlist_item_id", object.getString("wishlist_item_id"));

                if (object.has("review")) {
                    grid_data.put("review", object.getString("review"));
                } else {
                    grid_data.put("review", "No_Review");
                }

                if (object.has("offer")) {
                    grid_data.put("offer", String.valueOf(object.getInt("offer")));
                } else {
                    grid_data.put("offer", "0");
                }
                 if (object.getString("product_name").toLowerCase().equals(category_id.toLowerCase())) {
                        gridviewdata.add(0, grid_data);
                    } else {
                        /*if (object.getString("product_name").toLowerCase().contains(category_id.toLowerCase())) {
                            gridviewdata.add(0, grid_data);
                        } else {*/
                            gridviewdata.add(grid_data);
                        /*}*/
                    }

            }


            mageNative_gridViewAdapter = new RecyclerGridViewAdapter(Ced_Searchview.this, gridviewdata, gridlayout);
            int cp = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
            listingBinding.newlistlayout.recycler.setAdapter(mageNative_gridViewAdapter);
            gridLayoutManager.scrollToPosition(cp + 1);
            mageNative_gridViewAdapter.notifyDataSetChanged();

            load = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        if(gridviewdata.size()>0)
        {
            mageNative_gridViewAdapter = new RecyclerGridViewAdapter(Ced_Searchview.this, gridviewdata, gridlayout);
            listingBinding.newlistlayout.recycler.setAdapter(mageNative_gridViewAdapter);
            mageNative_gridViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnalyticsApplication.main_filters.clear();
        Ced_Searchview.order = "";
        Ced_Searchview.dir = "";
        Ced_Searchview.selecteddir = "";
        Ced_Searchview.selectedorder = "";
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            showmsg(getString(R.string.speech_not_supported));
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    showmsg( Objects.requireNonNull(result).get(0));
                   /* Intent intent = new Intent(getApplicationContext(), Ced_Searchview.class);*/
                    category_id=( result.get(0));
                    getsearcheddatalist(dataforproductlist,result.get(0),1);
                   /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);*/
                }
                break;
            }
        }
    }

    public void addautocomplete(CharSequence sequence) {
        if (suggestions.size() > 0) {
            String[] suggestionarray = suggestions.toArray(new String[suggestions.size()]);
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.textview, suggestionarray);
            search.setOnItemClickListener((parent, view, position, id) -> {
                if (name_id.containsKey(adapter.getItem(position))) {
                    Intent intent = new Intent(getApplicationContext(), Ced_NewProductView.class);
                    intent.putExtra("product_id", name_id.get(adapter.getItem(position)));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                } else {
                    /*Intent intent = new Intent(getApplicationContext(), Ced_Searchview.class);
                    intent.putExtra("ID", adapter.getItem(position));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);*/
                    /*current=1;
                gridviewdata.clear();
                getsearcheddatalist(dataforproductlist,adapter.getItem(position),1);*/
                }

            });
            search.setThreshold(1);
            search.setAdapter(adapter);
            search.setText(sequence);
            search.showDropDown();
            search.setSelection(search.getText().length());
        }
    }
}
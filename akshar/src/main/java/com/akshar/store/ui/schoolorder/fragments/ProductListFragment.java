package com.akshar.store.ui.schoolorder.fragments;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.base.utilapplication.AnalyticsApplication;
import com.akshar.store.databinding.FragmentProductListBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.productsection.adapter.Ced_SortAdapter;
import com.akshar.store.ui.schoolorder.adapter.SchoolProductListAdapter;
import com.akshar.store.ui.schoolorder.model.ProductDataModel;
import com.akshar.store.ui.schoolorder.model.VendorProductData;
import com.akshar.store.ui.schoolorder.ui.SchoolDetailsActivity;
import com.akshar.store.ui.schoolorder.viewmodel.SchoolViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

public class ProductListFragment extends Fragment implements SchoolProductListAdapter.onItemClickListeners {

    public ProductListFragment() {
        // Required empty public constructor
    }

    FragmentProductListBinding productListBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    SchoolViewModel schoolViewModel;
    SchoolProductListAdapter adapter;
    BottomSheetBehavior<LinearLayout> filterSheetBehavior;
    BottomSheetBehavior<LinearLayout> sortSheetBehavior;
    boolean isFirstTime = true, loading = true;
    Ced_SessionManagement cedSessionManagement;
    Ced_SessionManagement_login session;
    int current = 1;
    Ced_SortAdapter cedSortAdapter;
    private List<String> category_label, category_value;
    private String categoryLabel = "", categoryValue = "";
    private int selectedCategoryPosition = -1;
    private JSONArray filters;
    JSONObject keyvalfilter;
    JSONObject keyvalfilter2;
    HashMap<String, String> filterlabelcode;
    HashMap<String, String> filtercodelabel;
    JSONArray filterlabel = null;
    boolean filterapplied = false;
    boolean firstselected = true;
    boolean tagstoshow = false;
    public int checkbox_visibility;
    public static String selectedorder = " ";
    public static String selecteddir = " ";
    public static String order = "";
    public static String dir = "";
    JsonObject postParams;
    JSONObject filterData;
    String category_id;
    boolean filteravailable = false;
    SchoolDetailsActivity schoolDetailsActivity;
    boolean isCategoryApplied = false, isMultiFilterApplied = false, isClearFilterClicked = false;
    ArrayList<HashMap<String, String>> keyvalsort2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cedSessionManagement = Ced_SessionManagement.getCed_sessionManagement(getContext());
        session = Ced_SessionManagement_login.getShredPrefs(getContext());
        checkbox_visibility = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
        postParams = new JsonObject();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        schoolViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(SchoolViewModel.class);
        schoolDetailsActivity = (SchoolDetailsActivity) getActivity();
        productListBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_product_list, null, false);
        return productListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productListBinding.schoolProductListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        productListBinding.setClickListeners(this);
        filterSheetBehavior = BottomSheetBehavior.from(productListBinding.filterSheet.filterSheetLayout);
        sortSheetBehavior = BottomSheetBehavior.from(productListBinding.sortSheet.sortSheetLayout);

//        ((Ced_NavigationActivity)requireActivity()).setbottomsheet(filterSheetBehavior,productListBinding.view);
        ((Ced_NavigationActivity) requireActivity()).setbottomsheet(sortSheetBehavior, productListBinding.view);
        postParams.addProperty("store_id", cedSessionManagement.getStoreId());
        postParams.addProperty("page_size", "10");
        postParams.addProperty("subseller_id", SchoolDetailsActivity.sud_seller_id);
        keyvalsort2 = new ArrayList<>();

        getVendorProduct();
        if (sortSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sortSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            sortSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        productListBinding.schoolProductListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && loading) {
                    current = current + 1;
                    getVendorProduct();
                }
            }
        });

        //for clear filter
        productListBinding.filterSheet.MageNativeClearfilter.setOnClickListener(v -> {
            if (filterSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                productListBinding.view.setVisibility(View.GONE);
            }
            filterapplied = true;
            AnalyticsApplication.main_filters.clear();
            selecteddir = "";
            selectedorder = "";
            current = 1;
            isClearFilterClicked = true;
            isFirstTime = true;
            if (postParams.has("multi_filter"))
                postParams.remove("multi_filter");
            getVendorProduct();
            isMultiFilterApplied = true;
        });

        //for apply filter
        productListBinding.filterSheet.MageNativeApplyfilter.setOnClickListener(v1 -> {
            filterData = new JSONObject();
            if (filterSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                productListBinding.view.setVisibility(View.GONE);
            }
            isFirstTime = true;
            loading = true;
            filterapplied = true;
            current = 1;
            JSONObject object = new JSONObject();
            for (Map.Entry<String, ArrayList<String>> stringArrayListEntry : AnalyticsApplication.main_filters.entrySet()) {
                String key = String.valueOf(((Map.Entry) stringArrayListEntry).getKey());
                ArrayList<String> value = (ArrayList<String>) ((Map.Entry) stringArrayListEntry).getValue();
                Iterator<String> innerlist = value.iterator();
                JSONObject object1 = new JSONObject();
                while (innerlist.hasNext()) {
                    String id_name = innerlist.next();
                    String[] parts = id_name.split("#");
                    try {
                        object1.put(parts[0], parts[1]);
                    } catch (JSONException e) {
                        jumpToMainActivity();
                        e.printStackTrace();
                    }
                }
                try {
                    object.put(key, object1);
                } catch (JSONException e) {
                    jumpToMainActivity();
                    e.printStackTrace();
                }
            }
            try {
                if (order.isEmpty()) {
                    filterData.put("multifilter", object.toString());
                    filterData.put("ID", category_id);
//                filterData.put("filteravailable", true);
                    filterData.put("filterlabel", filterlabel.toString());
                    filterData.put("filters", filters.toString());
                } else {
                    filterData.put("multifilter", object.toString());
                    filterData.put("ID", category_id);
                    filterData.put("order", order);
                    filterData.put("dir", dir);
//                filterData.put("filteravailable", true);
                    filterData.put("filterlabel", filterlabel.toString());
                    filterData.put("filters", filters.toString());
                }
                filterData.put("MULTIURL", "FILTER");

                if (filterData.has("MULTIURL")) {
                    try {
                        postParams.addProperty("multi_filter", filterData.getString("multifilter"));
                        if (filterData.has("order")) {
                            postParams.addProperty("order", filterData.getString("order"));
                            postParams.addProperty("dir", filterData.getString("dir"));
                        }
                        if (filterData.has("filteravailable"))
                            filteravailable = filterData.getBoolean("filteravailable");
                        JSONArray array;
                        array = new JSONArray(Objects.requireNonNull(filterData.get("filterlabel")).toString());
                        Log.i("filterlabel", "" + array);
                        filterlabel = array;
                        filters = new JSONArray(Objects.requireNonNull(filterData.get("filters")).toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isMultiFilterApplied = true;
            getVendorProduct();
        });
    }

    private void getVendorProduct() {
        Log.e("VENDOR_PRODUCTS", "getVendorProduct: 0");
        postParams.addProperty("category_id", categoryValue);
        postParams.addProperty("current_page", String.valueOf(current));
        postParams.addProperty("customer_id", session.getCustomerid());
        try {
            schoolViewModel.getVendorProducts(getContext(), postParams).observe(getViewLifecycleOwner(), this::onGetResponse);
        } catch (Exception e) {
            e.printStackTrace();
            jumpToMainActivity();
        }
    }

    private void onGetResponse(ApiResponse apiResponse) {
        try {
            switch (apiResponse.status) {
                case SUCCESS:
                    applyData(apiResponse.data);
                    break;
                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    ((Ced_NavigationActivity) requireContext()).showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            jumpToMainActivity();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void applyData(String response) {
        try {
            JSONObject res_json_obj = new JSONObject(response);
            if (res_json_obj.getString("success").equals("true")) {
                if (res_json_obj.getJSONObject("data").has("category")) {
                    category_label = new ArrayList<>();
                    category_value = new ArrayList<>();
                    for (int j = 0; j < res_json_obj.getJSONObject("data").getJSONArray("category").length(); j++) {
                        category_label.add(res_json_obj.getJSONObject("data").getJSONArray("category").getJSONObject(j).getString("category_name"));
                        category_value.add(res_json_obj.getJSONObject("data").getJSONArray("category").getJSONObject(j).getString("category_id"));
                    }
                }
                keyvalfilter2 = new JSONObject();
                filterlabelcode = new HashMap<>();
                filtercodelabel = new HashMap<>();
                if (res_json_obj.getJSONObject("data").has("filter_label")) {
                    filterlabel = res_json_obj.getJSONObject("data").getJSONArray("filter_label");
                }

                if (res_json_obj.getJSONObject("data").has("filter")) {
                    filters = res_json_obj.getJSONObject("data").getJSONArray("filter");
                }
                sortdata(response);

                if (res_json_obj.getJSONObject("data").has("products")) {
                    VendorProductData vendorProductData = new GsonBuilder().create().fromJson(res_json_obj.getJSONObject("data").toString(), VendorProductData.class);
                    if (vendorProductData.getProducts().size() > 0) {
                        if (isFirstTime) {
                            isFirstTime = false;
                            adapter = new SchoolProductListAdapter(getActivity(), vendorProductData.getProducts());
                            productListBinding.schoolProductListRv.setAdapter(adapter);
                            adapter.setOnItemClickListener(this);
                        } else {
                            adapter.data.addAll(vendorProductData.getProducts());
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        if (isFirstTime) {
                            productListBinding.divider2.setVisibility(View.GONE);
                            productListBinding.emptyMsg.setVisibility(View.GONE);
                        }
                        if (isCategoryApplied && current == 1) {
                            if (vendorProductData.getProducts().size()>0) {
                                adapter.data = vendorProductData.getProducts();
                                adapter.notifyDataSetChanged();
                            }
                        }
                        if (isMultiFilterApplied && current == 1) {
                            adapter.data = vendorProductData.getProducts();
                            adapter.notifyDataSetChanged();
                        }
                        ((Ced_NavigationActivity) requireActivity()).showmsg("No More Products");
                        loading = false;
                    }
                } else {
                    loading = false;
                }
            } else {
                loading = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
//            jumpToMainActivity();
            productListBinding.schoolProductListRv.setVisibility(View.GONE);
            productListBinding.categoryDropdownLayout.setVisibility(View.GONE);
            productListBinding.MageNativeSortingsection.setVisibility(View.GONE);
            productListBinding.divider2.setVisibility(View.GONE);
            productListBinding.emptyMsg.setVisibility(View.GONE);
        }
    }

    public void add_remove_to_wishlist(View view, ProductDataModel item, int item_pos) {
        if (session.isLoggedIn()) {
            if (!item.getInwishlist().equals("OUT")) {
                JsonObject removefrom_wishlist = new JsonObject();
                try {
                    removefrom_wishlist.addProperty("item_id", item.getWishlistItemId());
                    removefrom_wishlist.addProperty("customer_id", session.getCustomerid());
                    schoolViewModel.removeFromWishList(getContext(), removefrom_wishlist, session.getHahkey()).observe(this, apiResponse -> {
                        switch (apiResponse.status) {
                            case SUCCESS:
                                try {
                                    JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                                    String Status = object.getString("status");
                                    if (Status.equals("true")) {
                                        ((AppCompatImageView) view).setImageResource(R.drawable.heart_icon);
                                        ((Ced_NavigationActivity) requireContext()).showmsg(object.getString("message"));
//                                        adapter.notifyItemChanged(item_pos);
                                        getVendorProduct();
//                                        inwishlist = false;
                                    }
                                    if (object.has("message"))
                                        ((Ced_NavigationActivity) requireContext()).showmsg(object.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    jumpToMainActivity();
                                }
                                break;

                            case ERROR:
                                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                                ((Ced_NavigationActivity) requireContext()).showmsg(getResources().getString(R.string.errorString));
                                break;
                        }
                    });
                } catch (Exception e) {

                    e.printStackTrace();
                }

            } else {
                JsonObject addtowishlist = new JsonObject();
                addtowishlist.addProperty("prodID", item.getProductId());
                addtowishlist.addProperty("customer_id", session.getCustomerid());
                schoolViewModel.addToWishList(getContext(), addtowishlist, session.getHahkey()).observe(getViewLifecycleOwner(), apiResponse ->
                {
                    switch (apiResponse.status) {
                        case SUCCESS:
                            try {

                                JSONObject objec = new JSONObject(Objects.requireNonNull(apiResponse.data));
                                String Status = objec.getString("status");
                                if (Status.equals("true")) {
                                    ((AppCompatImageView) view).setImageResource(R.drawable.wishred);
//                                    inwishlist = true;
                                    ((Ced_NavigationActivity) requireContext()).showmsg(objec.getString("message"));
//                                    adapter.notifyItemChanged(item_pos);
                                    getVendorProduct();
//                                    item_id = objec.getString("wishlist-item-id");
                                } else {
                                    if (Status.equals("false")) {
//                                        inwishlist = false;
                                        ((Ced_NavigationActivity) requireContext()).showmsg(objec.getString("message"));

                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                        case ERROR:
                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                            ((Ced_NavigationActivity) requireContext()).showmsg(getResources().getString(R.string.errorString));
                            break;
                    }
                });
            }
        } else {
            ((Ced_NavigationActivity) requireContext()).showmsg(getResources().getString(R.string.loginfirsttoaddwish));
        }
    }

    @Override
    public void onAddToWishListClicked(View view, ProductDataModel dataModel, int position) {
        add_remove_to_wishlist(view, dataModel, position);
    }

    //when list item clicked
    @Override
    public void onItemClick(View view, ProductDataModel dataModel) {
        try {
            Intent intent = new Intent(getContext(), Ced_NewProductView.class);
            intent.putExtra("product_id", dataModel.getProductId());
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //when category dropdown selected
    public void onCategoryDropDownClicked(View view) {
        try {
            final CharSequence[] arrayOfInt = category_value.toArray(new CharSequence[category_value.size()]);
            final CharSequence[] arrayOfInt2 = category_label.toArray(new CharSequence[category_label.size()]);
            new MaterialAlertDialogBuilder(requireContext(), R.style.SingleChoiceRadioStyleNew)
                    .setTitle(Html.fromHtml("<font color='#000000'>" + getResources().getString(R.string.select_category) + "</font>"))
                    .setSingleChoiceItems(arrayOfInt2, selectedCategoryPosition, (dialog, postion) -> {
                        selectedCategoryPosition = postion;
                        categoryValue = (String) arrayOfInt[postion];
                        categoryLabel = (String) arrayOfInt2[postion];
                        productListBinding.categoryDropdown.setText(categoryLabel);
                        dialog.dismiss();
                        isFirstTime = true;
                        loading = true;
                        current = 1;
                        if (categoryValue != null) {
                            isCategoryApplied = true;
                            getVendorProduct();
                        }
                    }).setPositiveButton("Clear", (dialog, which) -> {
                categoryValue = "";
                current = 1;
                selectedCategoryPosition = -1;
                productListBinding.categoryDropdown.setText(getResources().getString(R.string.select_category));
                getVendorProduct();
            }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //filter code start

    public void onFilterButtonClicked(View view) {
        try {
            productListBinding.filterSheet.MageNativeFilterscroll.fullScroll(View.FOCUS_UP);
            getFilterData();
            if (filterSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                filterSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            sortSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFilterData() {
        try {
            if (filters != null && filters.length() > 0) {
                for (int i = 0; i < filters.length(); i++) {
                    JSONObject obj2 = filters.getJSONObject(i);
                    JSONArray nameArray = obj2.names();
                    String name = Objects.requireNonNull(nameArray).getString(0);
                    JSONArray valArray = obj2.getJSONArray(name);
                    keyvalfilter = new JSONObject();

                    for (int j = 0; j < valArray.length(); j++) {
                        String value = valArray.getString(j);
                        String[] parts = value.split("#");

                        keyvalfilter.put(parts[0], parts[1]);
                    }
                    keyvalfilter2.put(name, keyvalfilter);
                }
                filterdatalabel();
            } else {
                ((Ced_NavigationActivity) requireContext()).showmsg(getResources().getString(R.string.filternotavailblefornow));
            }
        } catch (Exception w) {
            w.printStackTrace();
            jumpToMainActivity();
            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void filterdatalabel() {
        try {
            for (int l = 0; l < filterlabel.length(); l++) {
                JSONObject obj2 = filterlabel.getJSONObject(l);
                filterlabelcode.put(obj2.getString("att_code"), obj2.getString("att_label"));
                if (filtercodelabel.containsKey(obj2.getString("att_label"))) {
                    JSONArray checkfilters = filters;
                    for (int i = 0; i < checkfilters.length(); i++) {
                        JSONObject obj3 = checkfilters.getJSONObject(i);
                        JSONArray nameArray = obj3.names();
                        if (Objects.requireNonNull(nameArray).get(0).toString().equals(obj2.getString("att_code"))) {
                            int subFiltercount = Objects.requireNonNull(obj3.toJSONArray(nameArray)).length();
                            if (subFiltercount > 0) {
                                filtercodelabel.remove(obj2.getString("att_label"));
                                filtercodelabel.put(obj2.getString("att_label"), obj2.getString("att_code"));
                            }
                        }
                    }
                } else {
                    filtercodelabel.put(obj2.getString("att_label"), obj2.getString("att_code"));
                }
            }
            listfilters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listfilters() {
        Iterator<String> iterator = keyvalfilter2.keys();
        productListBinding.filterSheet.MageNativeFiltertags.removeAllViews();

        while (iterator.hasNext()) {
           /* String
            Map.Entry pair = (Map.Entry) iterator.next();*/
            String key = String.valueOf(iterator.next());
            LinearLayout linebelowbutton = new LinearLayout(getApplicationContext());
            linebelowbutton.setOrientation(LinearLayout.VERTICAL);
            final Button button = new Button(getApplicationContext());
            final TextView label = new TextView(getApplicationContext());
            label.setVisibility(View.GONE);
            if (firstselected || isClearFilterClicked) {
                if (AnalyticsApplication.main_filters.containsKey(key)) {
                    button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                    button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                } else {
                    button.setBackgroundColor(getResources().getColor(R.color.txtapptheme_color));
                    button.setTextColor(getResources().getColor(R.color.AppTheme));
                }
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                linearLayout.requestFocus();
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                JSONObject filtervalueshash = null;
                try {
                    filtervalueshash = keyvalfilter2.getJSONObject(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Iterator<String> filter = Objects.requireNonNull(filtervalueshash).keys();
                while (filter.hasNext()) {
                    String filterid = String.valueOf(filter.next());
                    /* String filterid = String.valueOf(filterpair.getKey());*/
                    String filterval = "";
                    try {
                        filterval = filtervalueshash.getString(filterid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LinearLayout layout = new LinearLayout(getApplicationContext());
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    final CheckBox filtervalue = new CheckBox(getApplicationContext());
                    filtervalue.setButtonDrawable(checkbox_visibility);
                    filtervalue.setText(Html.fromHtml(filterval));
                    filtervalue.setTextColor(getResources().getColor(R.color.black));
//                    if (containskeyalready(filterval, key, filterid)) {
//                        filtervalue.setChecked(true);
//                    } else {
//                        filtervalue.setChecked(false);
//                    }
                    filtervalue.setChecked(containskeyalready(filterval, key, filterid));
                    TextView filId = new TextView(getApplicationContext());
                    filId.setText(filterid);
                    filId.setVisibility(View.GONE);
                    layout.addView(filtervalue, 0);
                    layout.addView(filId, 1);
                    linearLayout.addView(layout);
                    filtervalue.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                            button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                            if (AnalyticsApplication.main_filters.size() <= 0) {
                                ArrayList<String> list = new ArrayList<>();
                                LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);

                            } else {
                                if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                    if (Objects.requireNonNull(list).contains(filter_ID.getText().toString() + "#" + filtervalue.getText().toString())) {
                                    } else {
                                        list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                        AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    }
                                } else {
                                    ArrayList<String> list = new ArrayList<>();
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());

                                    AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                }
                            }
                        } else {
                            boolean ischecked = false;
                            LinearLayout checkboxparent = (LinearLayout) filtervalue.getParent();
                            LinearLayout checkboxparentParent = (LinearLayout) checkboxparent.getParent();
                            int childcount = checkboxparentParent.getChildCount();
                            for (int child = 0; child <= childcount - 1; child++) {
                                LinearLayout layout1 = (LinearLayout) checkboxparentParent.getChildAt(child);
                                CheckBox box = (CheckBox) layout1.getChildAt(0);
                                if (box.isChecked()) {
                                    ischecked = true;
                                }
                            }
                            if (ischecked) {
                                button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                                button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                            } else {
                                button.setBackgroundColor(getResources().getColor(R.color.txtapptheme_color));
                                button.setTextColor(getResources().getColor(R.color.AppTheme));
                            }
                            if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                Objects.requireNonNull(list).remove(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                ArrayList<String> emptylist = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                if (Objects.requireNonNull(emptylist).size() <= 0) {
                                    AnalyticsApplication.main_filters.remove(filtercodelabel.get(button.getText().toString()));
                                }
                            }
                        }
                    });
                }
                productListBinding.filterSheet.MageNativeFiltervalues.addView(linearLayout);
                firstselected = false;
            } else {
                if (AnalyticsApplication.main_filters.containsKey(key)) {
                    button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                    button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                } else {
                    button.setBackgroundColor(getResources().getColor(R.color.darker_gray));
                    button.setTextColor(getResources().getColor(R.color.black));
                }
            }
            if (isClearFilterClicked) {
                isClearFilterClicked = false;
            }
            label.setText(key);
            button.setText(filterlabelcode.get(key));
            LinearLayout border = new LinearLayout(getApplicationContext());
            border.setBackgroundColor(getResources().getColor(R.color.border));
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 2);
            border.setLayoutParams(buttonParams);
            linebelowbutton.addView(button, 0);
            linebelowbutton.addView(border, 1);
            linebelowbutton.addView(label, 2);
            productListBinding.filterSheet.MageNativeFiltertags.addView(linebelowbutton);
            productListBinding.filterSheet.MageNativeFiltertags.setWeightSum(keyvalfilter2.length());
            button.setOnClickListener(v -> {
                productListBinding.filterSheet.MageNativeFilterscroll.scrollTo(0, 0);
                String currenttext = filtercodelabel.get(button.getText().toString());
                LinearLayout buttontags = (LinearLayout) button.getParent();
                LinearLayout buttontagsparent = (LinearLayout) buttontags.getParent();
                int childcountoftags = buttontagsparent.getChildCount();
                for (int child = 0; child <= childcountoftags - 1; child++) {
                    LinearLayout buttonlayout = (LinearLayout) buttontagsparent.getChildAt(child);
                    Button button1 = (Button) buttonlayout.getChildAt(0);
                    if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button1.getText().toString()))) {
                    } else {
                        if (Objects.requireNonNull(currenttext).equals(filtercodelabel.get(button1.getText().toString()))) {
                            button1.setBackgroundColor(getResources().getColor(R.color.txtapptheme_color));
                            button1.setTextColor(getResources().getColor(R.color.AppTheme));
                        } else {
                            button1.setTextColor(getResources().getColor(R.color.black));
                            button1.setBackgroundColor(getResources().getColor(R.color.darker_gray));
                        }
                    }
                }
                if (tagstoshow) {
                    if (productListBinding.filterSheet.MageNativeFiltervalues.getChildCount() > 0) {
                        productListBinding.filterSheet.MageNativeFiltervalues.removeAllViewsInLayout();
                    }
                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.requestFocus();
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    JSONObject filtervalueshash = null;
                    try {
                        filtervalueshash = keyvalfilter2.getJSONObject(Objects.requireNonNull(filtercodelabel.get(button.getText().toString())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Iterator<String> filter = Objects.requireNonNull(filtervalueshash).keys();
                    while (filter.hasNext()) {
                        String filterid = String.valueOf(filter.next());
                        /* String filterid = String.valueOf(filterpair.getKey());*/
                        String filterval = "";
                        try {
                            filterval = filtervalueshash.getString(filterid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LinearLayout layout = new LinearLayout(getApplicationContext());
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        final CheckBox filtervalue = new CheckBox(getApplicationContext());
                        filtervalue.setButtonDrawable(checkbox_visibility);
                        filtervalue.setText(Html.fromHtml(filterval));
//                        if (containskeyalready(filterval, filtercodelabel.get(button.getText().toString()), filterid)) {
//                            filtervalue.setChecked(true);
//                        } else {
//                            filtervalue.setChecked(false);
//                        }
                        filtervalue.setChecked(containskeyalready(filterval, filtercodelabel.get(button.getText().toString()), filterid));
                        TextView filId = new TextView(getApplicationContext());
                        filId.setText(filterid);
                        filId.setVisibility(View.GONE);
                        layout.addView(filtervalue, 0);
                        layout.addView(filId, 1);
                        linearLayout.addView(layout);
                        filtervalue.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            if (isChecked) {

                                button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                                button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                                if (AnalyticsApplication.main_filters.size() <= 0) {
                                    ArrayList<String> list = new ArrayList<>();
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                    AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);

                                } else {
                                    if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                        LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                        TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                        ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                        if (Objects.requireNonNull(list).contains(filter_ID.getText().toString() + "#" + filtervalue.getText().toString())) {
                                        } else {
                                            list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                            AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);

                                        }
                                    } else {
                                        ArrayList<String> list = new ArrayList<>();
                                        LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                        TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                        list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                        AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    }
                                }
                            } else {
                                boolean ischecked = false;
                                LinearLayout checkboxparent = (LinearLayout) filtervalue.getParent();
                                LinearLayout checkboxparentParent = (LinearLayout) checkboxparent.getParent();
                                int childcount = checkboxparentParent.getChildCount();
                                for (int child = 0; child <= childcount - 1; child++) {
                                    LinearLayout layout1 = (LinearLayout) checkboxparentParent.getChildAt(child);
                                    CheckBox box = (CheckBox) layout1.getChildAt(0);
                                    if (box.isChecked()) {
                                        ischecked = true;
                                    }
                                }
                                if (ischecked) {
                                    button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                                    button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                                } else {
                                    button.setBackgroundColor(getResources().getColor(R.color.txtapptheme_color));
                                    button.setTextColor(getResources().getColor(R.color.AppTheme));
                                }
                                if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                    ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    Objects.requireNonNull(list).remove(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                    AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    ArrayList<String> emptylist = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                    if (Objects.requireNonNull(emptylist).size() <= 0) {
                                        AnalyticsApplication.main_filters.remove(filtercodelabel.get(button.getText().toString()));
                                    }
                                }
                            }
                        });
                    }
                    productListBinding.filterSheet.MageNativeFiltervalues.addView(linearLayout);
                    tagstoshow = false;
                } else {
                    productListBinding.filterSheet.MageNativeFiltervalues.removeAllViewsInLayout();
                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    JSONObject filtervalueshash = null;
                    try {
                        filtervalueshash = keyvalfilter2.getJSONObject(Objects.requireNonNull(filtercodelabel.get(button.getText().toString())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Iterator<String> filter = Objects.requireNonNull(filtervalueshash).keys();
                    while (filter.hasNext()) {
                        String filterid = String.valueOf(filter.next());
                        String filterval = "";
                        try {
                            filterval = filtervalueshash.getString(filterid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LinearLayout layout = new LinearLayout(getApplicationContext());
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        final CheckBox filtervalue = new CheckBox(getApplicationContext());
                        filtervalue.setButtonDrawable(checkbox_visibility);
                        filtervalue.setText(filterval);
                        filtervalue.setTextColor(getResources().getColor(R.color.black));
//                        if (containskeyalready(filterval, filtercodelabel.get(button.getText().toString()), filterid)) {
//                            filtervalue.setChecked(true);
//                        } else {
//                            filtervalue.setChecked(false);
//                        }
                        filtervalue.setChecked(containskeyalready(filterval, filtercodelabel.get(button.getText().toString()), filterid));
                        TextView filId = new TextView(getApplicationContext());
                        filId.setText(filterid);
                        filId.setVisibility(View.GONE);
                        layout.addView(filtervalue, 0);
                        layout.addView(filId, 1);
                        layout.requestFocus();
                        linearLayout.addView(layout);
                        filtervalue.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            if (isChecked) {
                                button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                                button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                                if (AnalyticsApplication.main_filters.size() <= 0) {
                                    ArrayList<String> list = new ArrayList<>();
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());

                                    AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);

                                } else {
                                    if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                        ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                        LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                        TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                        Objects.requireNonNull(list).add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                        AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    } else {
                                        ArrayList<String> list = new ArrayList<>();
                                        LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                        TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                        list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                        AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    }
                                }
                            } else {
                                boolean ischecked = false;
                                LinearLayout checkboxparent = (LinearLayout) filtervalue.getParent();
                                LinearLayout checkboxparentParent = (LinearLayout) checkboxparent.getParent();
                                int childcount = checkboxparentParent.getChildCount();
                                for (int child = 0; child <= childcount - 1; child++) {
                                    LinearLayout layout1 = (LinearLayout) checkboxparentParent.getChildAt(child);
                                    CheckBox box = (CheckBox) layout1.getChildAt(0);
                                    if (box.isChecked()) {
                                        ischecked = true;
                                    }
                                }
                                if (ischecked) {
                                    button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                                    button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                                } else {
                                    button.setBackgroundColor(getResources().getColor(R.color.txtapptheme_color));
                                    button.setTextColor(getResources().getColor(R.color.AppTheme));
                                }
                                if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                    ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    Objects.requireNonNull(list).remove(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                    AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    ArrayList<String> emptylist = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                    if (Objects.requireNonNull(emptylist).size() <= 0) {
                                        AnalyticsApplication.main_filters.remove(filtercodelabel.get(button.getText().toString()));
                                    }
                                }
                            }
                        });
                    }
                    productListBinding.filterSheet.MageNativeFiltervalues.addView(linearLayout);
                }
            });
        }
    }

    private boolean containskeyalready(String checkbox, String parent, String id) {
        if (AnalyticsApplication.main_filters.size() > 0) {
            if (AnalyticsApplication.main_filters.containsKey(parent)) {
                ArrayList<String> list = AnalyticsApplication.main_filters.get(parent);
                return Objects.requireNonNull(list).contains(id + "#" + checkbox);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    //filter code end

    //sorting code start
    public void onSortButtonClicked(View view) {
        try {
            if (keyvalsort2.size() > 0) {
                if (sortSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sortSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    sortSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showDialog();
            } else {
                ((Ced_NavigationActivity) requireActivity()).showmsg(getResources().getString(R.string.sortoptionsnotavailable));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sortdata(String Jstring) {
        try {
            JSONObject jsonObj = new JSONObject(Jstring);
            if (jsonObj.getJSONObject("data").has("sort")) {
                JSONObject sorts = jsonObj.getJSONObject("data").getJSONObject("sort");
                if (keyvalsort2.size() > 0) {
                    keyvalsort2.clear();
                }
                Iterator<String> keys = sorts.keys();
                while (keys.hasNext()) {
                    String name = (String) keys.next();
                    String value = sorts.getString(name);
                    HashMap<String, String> keyvalsort = new HashMap<String, String>();
                    keyvalsort.put(name, value);
                    keyvalsort2.add(keyvalsort);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void showDialog() {
        productListBinding.sortSheet.MageNativeSortlistview.setOnItemClickListener((parent, view, position, id) -> {
            sortSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            ProductListFragment.order = "";
            current = 1;
            isFirstTime = true;
            JSONObject object = new JSONObject();
            TextView direction = view.findViewById(R.id.MageNative_sortDirection);
            TextView label = view.findViewById(R.id.MageNative_SortLabel);
            selectedorder = label.getText().toString();
            selecteddir = direction.getText().toString();
            String[] odr = selecteddir.split("#");
            String part1 = odr[0];
            String part2 = odr[1];
            ProductListFragment.order = part1;
            ProductListFragment.dir = part2;
            filterData = new JSONObject();
            try {
                if (AnalyticsApplication.main_filters.size() <= 0) {
                    filterData.put("order", order);
                    filterData.put("dir", dir);
                    filterData.put("ID", category_id);
                    filterData.put("keyvalsort2", keyvalsort2);
                    filterData.put("filterlabel", filterlabel.toString());
                    filterData.put("filters", filters.toString());
                    filterData.put("filteravailable", true);
                } else {
                    for (Map.Entry<String, ArrayList<String>> stringArrayListEntry : AnalyticsApplication.main_filters.entrySet()) {
                        String key = String.valueOf(((Map.Entry) stringArrayListEntry).getKey());
                        ArrayList<String> value = (ArrayList<String>) ((Map.Entry) stringArrayListEntry).getValue();
                        Iterator<String> innerlist = value.iterator();
                        JSONObject object1 = new JSONObject();
                        while (innerlist.hasNext()) {
                            String id_name = innerlist.next();
                            String[] parts = id_name.split("#");
                            try {
                                object1.put(parts[0], parts[1]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(main);
                                requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        }
                        try {
                            object.put(key, object1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                            requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                    }
                    filterData.put("order", order);
                    filterData.put("dir", dir);
                    filterData.put("ID", category_id);
                    filterData.put("keyvalsort2", keyvalsort2);
                    filterData.put("filterlabel", filterlabel.toString());
                    filterData.put("filters", filters.toString());
                    filterData.put("multifilter", object.toString());
                    filterData.put("filteravailable", true);
                }
                filterData.put("SORTURL", "SORT");

                if (Objects.requireNonNull(filterData).has("SORTURL")) {
                    try {
                        keyvalsort2 = (ArrayList<HashMap<String, String>>) filterData.get("keyvalsort2");
                        postParams.addProperty("order", filterData.getString("order"));
                        postParams.addProperty("dir", filterData.getString("dir"));
                        if (filterData.has("multifilter")) {
                            postParams.addProperty("multi_filter", filterData.getString("multifilter"));
                        }
                        filteravailable = filterData.getBoolean("filteravailable");

                        JSONArray array = null;

                        array = new JSONArray(Objects.requireNonNull(filterData.get("filterlabel")).toString());
                        Log.i("filterlabel", "" + array);
                        filterlabel = array;
                        filters = new JSONArray(Objects.requireNonNull(filterData.get("filters")).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            getVendorProduct();
        });
        cedSortAdapter = new Ced_SortAdapter(getActivity(), keyvalsort2, selectedorder, selecteddir);
        productListBinding.sortSheet.MageNativeSortlistview.setAdapter(cedSortAdapter);
        /*listDialog.show();*/
    }
    //sorting code end

    private void jumpToMainActivity() {
        Intent main = new Intent(getContext(), Ced_MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(main);
        requireActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
    }

    @Override
    public void onResume() {
        if (filterSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            productListBinding.view.setVisibility(View.GONE);
        } else if (sortSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sortSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            productListBinding.view.setVisibility(View.GONE);
        }
        super.onResume();
    }
}
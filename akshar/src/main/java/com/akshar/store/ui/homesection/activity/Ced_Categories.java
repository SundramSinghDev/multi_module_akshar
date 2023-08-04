package com.akshar.store.ui.homesection.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.akshar.store.base.activity.Ced_MainActivity;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.CatsBinding;
import com.akshar.store.databinding.ItemCatBinding;
import com.akshar.store.databinding.ItemSubcatBinding;
import com.akshar.store.ui.homesection.viewmodel.CategoriesViewModel;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class Ced_Categories extends Ced_NavigationActivity {
    CatsBinding categoriesBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    CategoriesViewModel categoriesViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoriesViewModel = new ViewModelProvider(Ced_Categories.this, viewModelFactory).get(CategoriesViewModel.class);
        categoriesBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.cats, content, true);
        showbackbutton();
        showtootltext(getResources().getString(R.string.shop_cat));
        selectcategorytab();
        JsonObject object = new JsonObject();
        object.addProperty("store_id", this.cedSessionManagement.getStoreId());
        categoriesViewModel.getCategoriesData(Ced_Categories.this, object).observe(Ced_Categories.this, apiResponse -> {
            switch (apiResponse.status){
                case SUCCESS:
                    createcategories(apiResponse.data); // if showing only 1st index child replace method with createcategories2()--p
                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });
    }

    private void createcategories(String data) {
        try {
            JSONObject jSONObject = new JSONObject(data);
            if (jSONObject.getString("status").equals("success")) {
                JSONObject category_info = jSONObject.getJSONObject("category_info");
                JSONObject child = category_info.getJSONObject(Objects.requireNonNull(category_info.names()).getString(0)).getJSONObject("child");
                JSONArray maincatsarray = child.names();
                Log.i("MainCats:", "" + maincatsarray);
                for (int i = 0; i < Objects.requireNonNull(maincatsarray).length(); i++) {
                    ItemCatBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_cat, null, false);
                    final RelativeLayout maincat_section = binding1.maincatSection;
                    TextView cat_id = binding1.catId;
                    final TextView json = binding1.json;
                    final TextView cat_name = binding1.catName;
                    ImageView cat_icon = binding1.catIcon;
                    ImageView rotateimage = binding1.rotateimage;
                    final LinearLayout subcats_section = binding1.subcatsSection;
                    boolean[] show = new boolean[]{false};
                    cat_name.setOnClickListener(v -> {
                        TextView cat_id1 = (TextView) ((RelativeLayout) cat_name.getParent().getParent()).getChildAt(1);
                        Intent intent = new Intent(Ced_Categories.this, Ced_New_Product_Listing.class);
                        intent.putExtra("ID", cat_id1.getText().toString());
                        Ced_Categories.this.startActivity(intent);
                        Ced_Categories.this.overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    });
                    final boolean[] zArr = show;
                    final LinearLayout linearLayout = subcats_section;
                    final ImageView imageView = rotateimage;
                    maincat_section.setOnClickListener(v -> {
                        if (zArr[0]) {
                            TextView json1 = (TextView) maincat_section.getChildAt(0);
                            if (subcats_section.getChildCount() > 0) {
                                collapse(linearLayout);
                                zArr[0] = false;
                                Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotatereverse);
                                sampleFadeAnimation.setRepeatCount(1);
                                sampleFadeAnimation.setFillAfter(true);
                                imageView.startAnimation(sampleFadeAnimation);
                            } else {
                                try {
                                    JSONObject cat_section = new JSONObject(json1.getText().toString());
                                    if (cat_section.has("child")) {
                                        JSONObject subcatnames = cat_section.getJSONObject("child");
                                        JSONArray subchild = subcatnames.names();
                                        for (int j = 0; j < Objects.requireNonNull(subchild).length(); j++) {
                                            JSONObject jsonObject2 = subcatnames.getJSONObject(subchild.getString(j));
                                            Log.i("MainCats:3", "" + subchild.getString(j));
                                            Log.i("MainCats:4", "" + jsonObject2);
                                            createsubcats(subcats_section, jsonObject2);
                                        }
                                    }
                                    collapse(linearLayout);
                                    zArr[0] = false;
                                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotatereverse);
                                    sampleFadeAnimation.setRepeatCount(1);
                                    sampleFadeAnimation.setFillAfter(true);
                                    imageView.startAnimation(sampleFadeAnimation);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            return;
                        } else {
                            try {
                                if (subcats_section.getChildCount() > 0) {
                                    expand(linearLayout);
                                    zArr[0] = true;
                                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotate);
                                    sampleFadeAnimation.setRepeatCount(1);
                                    sampleFadeAnimation.setFillAfter(true);
                                    imageView.startAnimation(sampleFadeAnimation);
                                } else {
                                    JSONObject cat_section = new JSONObject(json.getText().toString());
                                    if (cat_section.has("child")) {
                                        JSONObject subcatnames = cat_section.getJSONObject("child");
                                        JSONArray subchild = subcatnames.names();
                                        for (int j = 0; j < Objects.requireNonNull(subchild).length(); j++) {
                                            JSONObject jsonObject2 = subcatnames.getJSONObject(subchild.getString(j));
                                            Log.i("MainCats:3", "" + subchild.getString(j));
                                            Log.i("MainCats:4", "" + jsonObject2);
                                            createsubcats(subcats_section, jsonObject2);
                                        }
                                    }
                                    expand(linearLayout);
                                    zArr[0] = true;
                                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotate);
                                    sampleFadeAnimation.setRepeatCount(1);
                                    sampleFadeAnimation.setFillAfter(true);
                                    imageView.startAnimation(sampleFadeAnimation);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    JSONObject cat_section = child.getJSONObject(maincatsarray.getString(i));
                    Log.i("MainCats:1", "" + maincatsarray.getString(i));
                    Log.i("MainCats:2", "" + cat_section.getString("id"));
                    if (cat_section.has("child")) {
                        rotateimage.setVisibility(View.VISIBLE);
                    } else {
                        rotateimage.setVisibility(View.GONE);
                    }
                    cat_name.setText(cat_section.getString("name")/*maincatsarray.getString(i)*/);
                    cat_id.setText(cat_section.getString("id"));
                    json.setText(cat_section.toString());
                    set_regular_font_fortext(cat_name);
                    if (cat_section.has("icon")) {
                        Glide.with(this).load(cat_section.getString("icon")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(cat_icon);
                    } else {
                        cat_icon.setVisibility(View.INVISIBLE);
                    }

                    categoriesBinding.MageNativeCatsection.addView(binding1.getRoot());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createcategories2(String data) {
        try {
            JSONObject jSONObject = new JSONObject(data);
            if (jSONObject.getString("status").equals("success")) {
                //JSONObject category_info = jSONObject.getJSONObject("category_info");
                JSONObject child = jSONObject.getJSONObject("category_info");
                //  JSONObject child = category_info.getJSONObject(Objects.requireNonNull(category_info.names()).getString(0)).getJSONObject("child");
                JSONArray maincatsarray = child.names();
                Log.i("MainCats:", "" + maincatsarray);
                for (int i = 0; i < Objects.requireNonNull(maincatsarray).length(); i++) {
                    ItemCatBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_cat, null, false);
                    final RelativeLayout maincat_section = binding1.maincatSection;
                    TextView cat_id = binding1.catId;
                    final TextView json = binding1.json;
                    final TextView cat_name = binding1.catName;
                    ImageView cat_icon = binding1.catIcon;
                    ImageView rotateimage = binding1.rotateimage;
                    final LinearLayout subcats_section = binding1.subcatsSection;
                    boolean[] show = new boolean[]{false};
                    cat_name.setOnClickListener(v -> {
                        TextView cat_id1 = (TextView) ((RelativeLayout) cat_name.getParent().getParent()).getChildAt(1);
                        Intent intent = new Intent(Ced_Categories.this, Ced_New_Product_Listing.class);
                        intent.putExtra("ID", cat_id1.getText().toString());
                        Ced_Categories.this.startActivity(intent);
                        Ced_Categories.this.overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    });
                    final boolean[] zArr = show;
                    final LinearLayout linearLayout = subcats_section;
                    final ImageView imageView = rotateimage;
                    maincat_section.setOnClickListener(v -> {
                        if (zArr[0]) {
                            TextView json1 = (TextView) maincat_section.getChildAt(0);
                            if (subcats_section.getChildCount() > 0) {
                                collapse(linearLayout);
                                zArr[0] = false;
                                Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotatereverse);
                                sampleFadeAnimation.setRepeatCount(1);
                                sampleFadeAnimation.setFillAfter(true);
                                imageView.startAnimation(sampleFadeAnimation);
                            } else {
                                try {
                                    JSONObject cat_section = new JSONObject(json1.getText().toString());
                                    if (cat_section.has("child")) {
                                        JSONObject subcatnames = cat_section.getJSONObject("child");
                                        JSONArray subchild = subcatnames.names();
                                        for (int j = 0; j < Objects.requireNonNull(subchild).length(); j++) {
                                            JSONObject jsonObject2 = subcatnames.getJSONObject(subchild.getString(j));
                                            Log.i("MainCats:3", "" + subchild.getString(j));
                                            Log.i("MainCats:4", "" + jsonObject2);
                                            createsubcats(subcats_section, jsonObject2);
                                        }
                                    }
                                    collapse(linearLayout);
                                    zArr[0] = false;
                                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotatereverse);
                                    sampleFadeAnimation.setRepeatCount(1);
                                    sampleFadeAnimation.setFillAfter(true);
                                    imageView.startAnimation(sampleFadeAnimation);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            return;
                        } else {
                            try {
                                if (subcats_section.getChildCount() > 0) {
                                    expand(linearLayout);
                                    zArr[0] = true;
                                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotate);
                                    sampleFadeAnimation.setRepeatCount(1);
                                    sampleFadeAnimation.setFillAfter(true);
                                    imageView.startAnimation(sampleFadeAnimation);
                                } else {
                                    JSONObject cat_section = new JSONObject(json.getText().toString());
                                    if (cat_section.has("child")) {
                                        JSONObject subcatnames = cat_section.getJSONObject("child");
                                        JSONArray subchild = subcatnames.names();
                                        for (int j = 0; j < Objects.requireNonNull(subchild).length(); j++) {
                                            JSONObject jsonObject2 = subcatnames.getJSONObject(subchild.getString(j));
                                            Log.i("MainCats:3", "" + subchild.getString(j));
                                            Log.i("MainCats:4", "" + jsonObject2);
                                            createsubcats(subcats_section, jsonObject2);
                                        }
                                    }
                                    expand(linearLayout);
                                    zArr[0] = true;
                                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotate);
                                    sampleFadeAnimation.setRepeatCount(1);
                                    sampleFadeAnimation.setFillAfter(true);
                                    imageView.startAnimation(sampleFadeAnimation);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    JSONObject cat_section = child.getJSONObject(maincatsarray.getString(i));
                    Log.i("MainCats:1", "" + maincatsarray.getString(i));
                    Log.i("MainCats:2", "" + cat_section.getString("id"));
                    if (cat_section.has("child")) {
                        rotateimage.setVisibility(View.VISIBLE);
                    } else {
                        rotateimage.setVisibility(View.GONE);
                    }
                    cat_name.setText(cat_section.getString("name")/*maincatsarray.getString(i)*/);
                    cat_id.setText(cat_section.getString("id"));
                    json.setText(cat_section.toString());
                    set_regular_font_fortext(cat_name);
                    if (cat_section.has("icon")) {
                        Glide.with(this).load(cat_section.getString("icon")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(cat_icon);
                    } else {
                        cat_icon.setVisibility(View.INVISIBLE);
                    }

                    categoriesBinding.MageNativeCatsection.addView(binding1.getRoot());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createsubcats(final LinearLayout subcat_section, final JSONObject jsonObject2) {
        try {
            Log.i("MainCats:5", "" + jsonObject2.getString("name"));
            Log.i("MainCats:6", "" + jsonObject2);
            ItemSubcatBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_subcat, null, false);
            final ImageView rotateimage = binding1.rotateimage;
            final TextView json2 = binding1.json2;
            if (jsonObject2.has("child")) {
                rotateimage.setVisibility(View.VISIBLE);
            } else {
                rotateimage.setVisibility(View.GONE);
            }
            json2.setText(jsonObject2.toString());
            final RelativeLayout maincat_section = binding1.mainsubSection;
            maincat_section.setOnClickListener(v -> {
                TextView cat_id = (TextView) maincat_section.getChildAt(1);
                Intent intent = new Intent(Ced_Categories.this, Ced_New_Product_Listing.class);
                intent.putExtra("ID", cat_id.getText().toString());
                Ced_Categories.this.startActivity(intent);
                Ced_Categories.this.overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            });
            TextView cat_id = binding1.catId;
            final TextView cat_name = binding1.catName;
            final LinearLayout subcats_section = binding1.subcatsSection;
            cat_name.setText(Html.fromHtml("&#8226;  " + jsonObject2.getString("name")));
            set_regular_font_fortext(cat_name);
            final boolean[] show = new boolean[]{false};
            cat_name.setOnClickListener(v -> {
                TextView cat_id1 = (TextView) ((RelativeLayout) cat_name.getParent().getParent()).getChildAt(1);
                Intent intent = new Intent(Ced_Categories.this, Ced_New_Product_Listing.class);
                intent.putExtra("ID", cat_id1.getText().toString());
                Ced_Categories.this.startActivity(intent);
                Ced_Categories.this.overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            });
            maincat_section.setOnClickListener(v -> {
                try {
                    TextView json = (TextView) maincat_section.getChildAt(0);
                    JSONObject jsonObject = new JSONObject(json.getText().toString());
                    if (show[0]) {
                        if (subcats_section.getChildCount() > 0) {
                            collapse(subcats_section);
                            show[0] = false;
                            Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotatereverse);
                            sampleFadeAnimation.setRepeatCount(1);
                            sampleFadeAnimation.setFillAfter(true);
                            rotateimage.startAnimation(sampleFadeAnimation);
                        } else {
                            if (jsonObject.has("child")) {
                                JSONObject subcatnames = jsonObject.getJSONObject("child");
                                JSONArray subchild = subcatnames.names();
                                for (int j = 0; j < Objects.requireNonNull(subchild).length(); j++) {
                                    createsubcats(subcats_section, subcatnames.getJSONObject(subchild.getString(j)));
                                }
                            }

                            collapse(subcats_section);
                            show[0] = false;
                            Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotatereverse);
                            sampleFadeAnimation.setRepeatCount(1);
                            sampleFadeAnimation.setFillAfter(true);
                            rotateimage.startAnimation(sampleFadeAnimation);
                        }
                    } else {
                        if (subcats_section.getChildCount() > 0) {
                            expand(subcats_section);
                            show[0] = true;
                            Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotate);
                            sampleFadeAnimation.setRepeatCount(1);
                            sampleFadeAnimation.setFillAfter(true);
                            rotateimage.startAnimation(sampleFadeAnimation);
                        } else {
                            if (jsonObject.has("child")) {
                                JSONObject subcatnames = jsonObject.getJSONObject("child");
                                JSONArray subchild = subcatnames.names();
                                for (int j = 0; j < Objects.requireNonNull(subchild).length(); j++) {
                                    createsubcats(subcats_section,  subcatnames.getJSONObject(subchild.getString(j)));
                                }
                                expand(subcats_section);
                                show[0] = true;
                                Animation sampleFadeAnimation = AnimationUtils.loadAnimation(Ced_Categories.this, R.anim.magenative_rotate);
                                sampleFadeAnimation.setRepeatCount(1);
                                sampleFadeAnimation.setFillAfter(true);
                                rotateimage.startAnimation(sampleFadeAnimation);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            cat_id.setText(jsonObject2.getString("id"));
            subcat_section.addView(binding1.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

}
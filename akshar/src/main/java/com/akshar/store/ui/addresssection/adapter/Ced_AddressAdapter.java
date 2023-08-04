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

package com.akshar.store.ui.addresssection.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import com.akshar.store.ui.addresssection.activity.Ced_UpdateAddressbook;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.MagenativeAddressListItemBinding;
import com.akshar.store.ui.addresssection.activity.Ced_Addressbook;
import com.akshar.store.R;
import com.akshar.store.ui.addresssection.model.AddressItem;
import com.akshar.store.ui.addresssection.viewmodel.AddressViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by developer on 9/27/2015.
 */
public class Ced_AddressAdapter extends BaseAdapter {

    @Inject
    ViewModelFactory viewModelFactory;
    AddressViewModel addressViewModel;

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private List list;
    private JsonObject hashMap;
    private ArrayList arrayList;
    private ArrayList<String> newupdatelist;
    private HashMap<Integer, ArrayList<String>> addresslistt;
    private static final String KEY_ITEM = "data";
    private static final String KEY_CUSTOMER = "customer";
    private static final String KEY_STATUS = "status";
    private String address_id;
    private String[] array = new String[13];
    private JSONArray userdetail = null;
    private JSONObject jsonObject = null;

    public Ced_AddressAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        hashMap = new JsonObject();
        addresslistt = new HashMap<>();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        addressViewModel = new ViewModelProvider((FragmentActivity)activity, viewModelFactory).get(AddressViewModel.class);
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        MagenativeAddressListItemBinding addressListItemBinding;
        if (convertView == null) {
            AddressViewHolder  holder = new AddressViewHolder();
            addressListItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(activity),
                    R.layout.magenative_address_list_item, parent, false);
            convertView = addressListItemBinding.getRoot();
            addressListItemBinding.setTest(new AddressItem());
          //  addressListItemBinding = DataBindingUtil.inflate(inflater, R.layout.magenative_address_list_item, null, false);

            holder.firstname = addressListItemBinding.MageNativeClientFirstname;
            holder.lastname = addressListItemBinding.MageNativeClientLastname;
            holder.city = addressListItemBinding.MageNativeClientCity;
            holder.state = addressListItemBinding.MageNativeClientState;
            holder.phone = addressListItemBinding.MageNativeClientPhone;
            holder.country = addressListItemBinding.MageNativeClientCountry;
            holder.street = addressListItemBinding.MageNativeClientStreet;
            holder.pincode = addressListItemBinding.MageNativeClientPincode;
            holder. edit = addressListItemBinding.MageNativeEditaddress;
            holder.deleteaddress = addressListItemBinding.MageNativeDeleteaddress;
            holder.id = addressListItemBinding.MageNativeClientId;
            holder.MageNative_prefix = addressListItemBinding.MageNativePrefix;
            holder.MageNativemiddlename = addressListItemBinding.MageNativemiddlename;
            holder.MageNative_suffix = addressListItemBinding.MageNativeSuffix;
            holder.MageNative_taxvat = addressListItemBinding.MageNativeTaxvat;

            String fontPath4 = "fonts/Celias-Regular.ttf";
            Typeface avnblack = Typeface.createFromAsset(activity.getAssets(), fontPath4);
            holder.firstname.setTypeface(avnblack);
            holder.lastname.setTypeface(avnblack);
            holder.city.setTypeface(avnblack);
            holder.state.setTypeface(avnblack);
            holder.phone.setTypeface(avnblack);
            holder.country.setTypeface(avnblack);
            holder.street.setTypeface(avnblack);
            holder.pincode.setTypeface(avnblack);

            final View finalVi = addressListItemBinding.getRoot();
            AddressViewHolder finalHolder1 = holder;
            holder.deleteaddress.setOnClickListener(v -> {
                finalVi.startAnimation(outToLeftAnimation());
                finalVi.setVisibility(View.GONE);
                data.remove(position);
                address_id = finalHolder1.id.getText().toString();
                final HashMap<String, String> user = ((Ced_NavigationActivity)activity).session.getUserDetails();
                try {
                    hashMap.addProperty("customer_id", ((Ced_NavigationActivity)activity).session.getCustomerid());
                    hashMap.addProperty("address_id", address_id);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                addressViewModel.deleteAddressData(activity, hashMap,((Ced_NavigationActivity)activity).session.getHahkey()).observe((FragmentActivity) activity, apiResponse -> {
                    switch (apiResponse.status){
                        case SUCCESS:
                                deladdress(Objects.requireNonNull(apiResponse.data));
                            break;

                        case ERROR:
                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                            ((Ced_NavigationActivity) activity).showmsg(activity.getResources().getString(R.string.errorString));
                            break;
                    }
                });

            });

            list = new ArrayList<String>();
            arrayList = new ArrayList<ArrayList<String>>();
            AddressViewHolder finalHolder = holder;
            holder.edit.setOnClickListener(v -> {
                array[0] = finalHolder.firstname.getText().toString();
                array[1] = finalHolder.lastname.getText().toString();
                array[2] = finalHolder.city.getText().toString();
                array[3] = finalHolder.state.getText().toString();
                array[4] = finalHolder.phone.getText().toString();
                array[5] = finalHolder.street.getText().toString();
                array[6] = finalHolder.pincode.getText().toString();
                array[7] = finalHolder.country.getText().toString();
                array[8] = finalHolder.id.getText().toString();
                array[9] = finalHolder.MageNative_prefix.getText().toString();
                array[10] = finalHolder.MageNative_suffix.getText().toString();
                array[11] = finalHolder.MageNativemiddlename.getText().toString();
                array[12] = finalHolder.MageNative_taxvat.getText().toString();
                Intent updateaddressintent = new Intent(activity, Ced_UpdateAddressbook.class);
                updateaddressintent.putExtra("update", array);
                activity.startActivity(updateaddressintent);
            });

            HashMap<String, String> song;
            song = data.get(position);
            newupdatelist = new ArrayList<>();
            newupdatelist.add(song.get(Ced_Addressbook.KEY_ID) + "_" + song.get(Ced_Addressbook.KEY_FIRSTNAME) + "_" + song.get(Ced_Addressbook.KEY_LASTNAME) + "_" + song.get(Ced_Addressbook.KEY_STREET) + "_" + song.get(Ced_Addressbook.KEY_CITY) + "_" + song.get(Ced_Addressbook.KEY_STATE) + "_" + song.get(Ced_Addressbook.KEY_PINCODE) + "_" + song.get(Ced_Addressbook.KEY_COUNTRY) + "_" + song.get(Ced_Addressbook.KEY_PHONE));
            addresslistt.put(position, newupdatelist);
            holder.firstname.setText(song.get(Ced_Addressbook.KEY_FIRSTNAME));
            holder.lastname.setText(song.get(Ced_Addressbook.KEY_LASTNAME));
            holder.city.setText(song.get(Ced_Addressbook.KEY_CITY));
            holder.state.setText(song.get(Ced_Addressbook.KEY_STATE));
            holder.phone.setText(song.get(Ced_Addressbook.KEY_PHONE));
            holder.country.setText(song.get(Ced_Addressbook.KEY_COUNTRY));
            holder.street.setText(song.get(Ced_Addressbook.KEY_STREET));
            holder.pincode.setText(song.get(Ced_Addressbook.KEY_PINCODE));
            holder.id.setText(song.get(Ced_Addressbook.KEY_ID));
            if (!(Objects.requireNonNull(song.get("prefix")).equals("null"))) {
                holder.MageNative_prefix.setText(song.get("prefix"));
            } else {
                holder.MageNative_prefix.setText(song.get("prefix"));
                holder.MageNative_prefix.setVisibility(View.INVISIBLE);
            }
            if (!(Objects.requireNonNull(song.get("suffix")).equals("null"))) {
                holder.MageNative_suffix.setText(song.get("suffix"));
            } else {
                holder.MageNative_suffix.setText(song.get("suffix"));
                holder.MageNative_suffix.setVisibility(View.INVISIBLE);
            }
            if (!(Objects.requireNonNull(song.get("middlename")).equals("null"))) {
                holder.MageNativemiddlename.setText(song.get("middlename"));
            } else {
                holder.MageNativemiddlename.setText(song.get("middlename"));
                holder.MageNativemiddlename.setVisibility(View.INVISIBLE);
            }
            if (!(Objects.requireNonNull(song.get("taxvat")).equals("null"))) {
                holder.MageNative_taxvat.setText(song.get("taxvat"));
            } else {
                holder.MageNative_taxvat.setText(song.get("taxvat"));
                holder.MageNative_taxvat.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            addressListItemBinding = (MagenativeAddressListItemBinding) convertView.getTag();
        }
        convertView.setTag(addressListItemBinding);
        return convertView;
    }

    private void deladdress(String Output) {
        try {
            jsonObject = new JSONObject(Output);
            userdetail = jsonObject.getJSONObject(KEY_ITEM).getJSONArray(KEY_CUSTOMER);
            for (int i = 0; i < userdetail.length(); i++) {
                JSONObject c = null;
                c = userdetail.getJSONObject(i);
               String status = c.getString(KEY_STATUS);
                if (status.equals("success")) {
                    refreshlistview();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refreshlistview() {
        Handler handle = new Handler();

        handle.postDelayed(() -> {
            Intent intent = new Intent(activity, Ced_Addressbook.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }, 1000);
    }

    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );

        outtoLeft.setDuration(1000);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

}
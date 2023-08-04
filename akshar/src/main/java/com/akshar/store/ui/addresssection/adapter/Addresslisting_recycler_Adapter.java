package com.akshar.store.ui.addresssection.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.MagenativeAddressListItemBinding;
import com.akshar.store.ui.addresssection.activity.Ced_Addressbook;
import com.akshar.store.ui.addresssection.activity.Ced_UpdateAddressbook;
import com.akshar.store.ui.addresssection.model.AddressItem;
import com.akshar.store.ui.addresssection.viewmodel.AddressViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

public class Addresslisting_recycler_Adapter extends RecyclerView.Adapter<Addresslisting_recycler_Adapter.ViewHolder> {
    @Inject
    ViewModelFactory viewModelFactory;
    AddressViewModel addressViewModel;
    LayoutInflater layoutInflater;
    String data;
    private JSONArray data_array;
    private Context context;
    String Output;

    String street = "";
    static final String KEY_ITEM = "data";
    static final String KEY_CUSTOMER = "customer";
    static final String KEY_STATUS = "status";

    ArrayList<String> newupdatelist;
    HashMap<Integer, ArrayList<String>> addresslistt;

    public Addresslisting_recycler_Adapter(Context context, String data) throws JSONException {
        this.context = context;
        this.data = data;
        this.data_array = new JSONArray(data.toString());
        addresslistt = new HashMap<Integer, ArrayList<String>>();
        addressViewModel = new ViewModelProvider((FragmentActivity) context, viewModelFactory).get(AddressViewModel.class);
    }

    @Override
    public int getItemCount() {

        return (null != data_array ? data_array.length() : 0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        MagenativeAddressListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.magenative_address_list_item, viewGroup, false);
        binding.setTest(new AddressItem());
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {

            final String[] array = new String[15];
            JSONObject song = data_array.getJSONObject(position);

            ((Ced_NavigationActivity) context).set_bold_font_fortext(holder.binding.MageNativeClientFirstname);
            ((Ced_NavigationActivity) context).set_bold_font_fortext(holder.binding.MageNativeClientLastname);
            ((Ced_NavigationActivity) context).set_bold_font_fortext(holder.binding.MageNativemiddlename);
            ((Ced_NavigationActivity) context).set_bold_font_fortext(holder.binding.MageNativePrefix);
            ((Ced_NavigationActivity) context).set_bold_font_fortext(holder.binding.MageNativeSuffix);

            ((Ced_NavigationActivity) context).set_regular_font_fortext(holder.binding.MageNativeClientCity);
            ((Ced_NavigationActivity) context).set_regular_font_fortext(holder.binding.MageNativeClientState);
            ((Ced_NavigationActivity) context).set_regular_font_fortext(holder.binding.MageNativeClientPhone);
            ((Ced_NavigationActivity) context).set_regular_font_fortext(holder.binding.MageNativeClientCountry);
            ((Ced_NavigationActivity) context).set_regular_font_fortext(holder.binding.MageNativeClientStreet);
            ((Ced_NavigationActivity) context).set_regular_font_fortext(holder.binding.MageNativeClientPincode);

            newupdatelist = new ArrayList<String>();
            if (song.has(Ced_Addressbook.KEY_STREET)) {
                if (song.getString(Ced_Addressbook.KEY_STREET).contains("{")) {
                    street = String.valueOf(new JSONObject(song.getString(Ced_Addressbook.KEY_STREET)).get("0"));
                } else {
                    street = song.getString(Ced_Addressbook.KEY_STREET);
                }
            }
            newupdatelist.add(song.get(Ced_Addressbook.KEY_ID) + "_" + song.get(Ced_Addressbook.KEY_FIRSTNAME) + "_" + song.get(Ced_Addressbook.KEY_LASTNAME) + "_" + street + "_" + song.get(Ced_Addressbook.KEY_CITY) + "_" + song.get(Ced_Addressbook.KEY_STATE) + "_" + song.get(Ced_Addressbook.KEY_PINCODE) + "_" + song.get(Ced_Addressbook.KEY_COUNTRY) + "_" + song.get(Ced_Addressbook.KEY_PHONE));
            addresslistt.put(position, newupdatelist);

            holder.binding.MageNativeClientFirstname.setText(song.get(Ced_Addressbook.KEY_FIRSTNAME).toString());
            holder.binding.MageNativeClientLastname.setText(song.get(Ced_Addressbook.KEY_LASTNAME).toString());
            holder.binding.MageNativeClientCity.setText(song.get(Ced_Addressbook.KEY_CITY).toString());
            holder.binding.MageNativeClientState.setText(song.get(Ced_Addressbook.KEY_STATE).toString());
            holder.binding.MageNativeClientPhone.setText(song.get(Ced_Addressbook.KEY_PHONE).toString());
            holder.binding.MageNativeClientCountry.setText(song.get(Ced_Addressbook.KEY_COUNTRY).toString());
            holder.binding.MageNativeClientStreet.setText(street);
            holder.binding.MageNativeClientPincode.setText(song.get(Ced_Addressbook.KEY_PINCODE).toString());
            holder.binding.MageNativeClientId.setText(song.get(Ced_Addressbook.KEY_ID).toString());

            if (song.has(Ced_Addressbook.KEY_default_billing)) {
                if (song.getString(Ced_Addressbook.KEY_default_billing).equals("true")) {
                    holder.binding.defaultbillingAddress.setText(context.getResources().getString(R.string.DefaultBilling));
                    holder.binding.defaultbillingAddress.setVisibility(View.VISIBLE);
                    holder.binding.operationsLayout.setVisibility(View.GONE);
                }
            }
            if (song.has(Ced_Addressbook.KEY_default_shipping)) {
                if (song.getString(Ced_Addressbook.KEY_default_shipping).equals("true")) {
                    holder.binding.defaultshippingAddress.setText(context.getResources().getString(R.string.DefaultShipping));
                    holder.binding.defaultshippingAddress.setVisibility(View.VISIBLE);
                    holder.binding.operationsLayout.setVisibility(View.GONE);
                }
            }
            if (song.has("prefix")) {
                if (!(song.get("prefix").equals("null"))) {
                    holder.binding.MageNativePrefix.setText(song.get("prefix").toString());
                } else {
                    holder.binding.MageNativePrefix.setText(song.get("prefix").toString());
                    holder.binding.MageNativePrefix.setVisibility(View.INVISIBLE);
                }
            }
            if (song.has("suffix")) {
                if (!(song.get("suffix").equals("null"))) {
                    holder.binding.MageNativeSuffix.setText(song.get("suffix").toString());
                } else {
                    holder.binding.MageNativeSuffix.setText(song.get("suffix").toString());
                    holder.binding.MageNativeSuffix.setVisibility(View.INVISIBLE);
                }
            }

            if (song.has("middlename")) {
                if (!(song.get("middlename").equals("null"))) {
                    holder.binding.MageNativemiddlename.setText(song.get("middlename").toString());
                } else {
                    holder.binding.MageNativemiddlename.setText(song.get("middlename").toString());
                    holder.binding.MageNativemiddlename.setVisibility(View.INVISIBLE);
                }
            }

            if (song.has("taxvat")) {
                if (!(song.get("taxvat").equals("null"))) {
                    holder.binding.MageNativeTaxvat.setText(song.get("taxvat").toString());
                } else {
                    holder.binding.MageNativeTaxvat.setText(song.get("taxvat").toString());
                    holder.binding.MageNativeTaxvat.setVisibility(View.INVISIBLE);
                }
            }

            holder.binding.MageNativeEditaddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    array[0] = holder.binding.MageNativeClientFirstname.getText().toString();
                    array[1] = holder.binding.MageNativeClientLastname.getText().toString();
                    array[2] = holder.binding.MageNativeClientCity.getText().toString();
                    array[3] = holder.binding.MageNativeClientState.getText().toString();
                    array[4] = holder.binding.MageNativeClientPhone.getText().toString();
                    array[5] = holder.binding.MageNativeClientStreet.getText().toString();
                    array[6] = holder.binding.MageNativeClientPincode.getText().toString();
                    array[7] = holder.binding.MageNativeClientCountry.getText().toString();
                    array[8] = holder.binding.MageNativeClientId.getText().toString();
                    array[9] = holder.binding.MageNativePrefix.getText().toString();
                    array[10] = holder.binding.MageNativeSuffix.getText().toString();
                    array[11] = holder.binding.MageNativemiddlename.getText().toString();
                    array[12] = holder.binding.MageNativeTaxvat.getText().toString();

                    array[13] = holder.binding.defaultshippingAddress.getText().toString();
                    array[14] = holder.binding.defaultbillingAddress.getText().toString();

                    Intent updateaddressintent = new Intent(context, Ced_UpdateAddressbook.class);
                    updateaddressintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    updateaddressintent.putExtra("update", array);
                    context.startActivity(updateaddressintent);
                    ((Ced_Addressbook) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);

                }
            });

            holder.binding.MageNativeDeleteaddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final HashMap<String, String> user = ((Ced_NavigationActivity) context).session.getUserDetails();
                    try {
                        JsonObject hashMap = new JsonObject();
                        hashMap.addProperty("customer_id", ((Ced_NavigationActivity) context).session.getCustomerid());
                        hashMap.addProperty("address_id", holder.binding.MageNativeClientId.getText().toString());
                        addressViewModel.deleteAddressData(context, hashMap, ((Ced_NavigationActivity) context).session.getHahkey()).observe((FragmentActivity) context, apiResponse -> {
                            switch (apiResponse.status) {
                                case SUCCESS:
                                    Output = apiResponse.data;
                                    deladdress(position, holder.binding.MageNativeClientFirstname.getText().toString(), holder.binding.MageNativeClientLastname.getText().toString(), holder.binding.MageNativeClientCity.getText().toString(), holder.binding.MageNativeClientState.getText().toString(), holder.binding.MageNativeClientPhone.getText().toString(), holder.binding.MageNativeClientCountry.getText().toString(), holder.binding.MageNativeClientStreet.getText().toString(), holder.binding.MageNativeClientPincode.getText().toString(), holder.binding.MageNativeClientId.getText().toString());
                                    break;

                                case ERROR:
                                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                                    ((Ced_NavigationActivity) context).showmsg(context.getResources().getString(R.string.errorString));
                                    break;
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void deladdress(int pos, String firstname, String lastname, String city, String state, String phone, String country, String street, String pincode, String address_id) {
        try {
            JSONObject jsonObject = new JSONObject(Output);
            JSONArray userdetail = jsonObject.getJSONObject(KEY_ITEM).getJSONArray(KEY_CUSTOMER);
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

        handle.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(context, Ced_Addressbook.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            }
        }, 1000);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        MagenativeAddressListItemBinding binding;

        public ViewHolder(MagenativeAddressListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}


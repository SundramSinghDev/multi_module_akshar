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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.MagenativeSearchpageBinding;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.searchsection.viewmodel.SearchViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_Search extends Ced_NavigationActivity {
    static final String KEY_STATUS = "status";
    static final String KEY_ITEM = "data";
    static final String KEY_NAME = "suggestion";
    static final String KEY_PRODUCT_NAME = "product_name";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    MagenativeSearchpageBinding searchpageBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    SearchViewModel searchViewModel;
    JSONArray suggestion = null;
    String product_name;
    ArrayList<String> suggestions;
    AutoCompleteTextView search;
    ImageView search_btn;
    JsonObject searchdata;
    boolean autocomplete = true;
    boolean request = true;
    HashMap<String, String> name_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = new ViewModelProvider(Ced_Search.this, viewModelFactory).get(SearchViewModel.class);
        searchpageBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_searchpage, content, true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        showbackbutton();
        searchdata = new JsonObject();
        suggestions = new ArrayList<>();
        name_id = new HashMap<String, String>();
        search = searchpageBinding.MageNativeEditText1;
        search.requestFocus();
        searchpageBinding.voicesearch.setOnClickListener(v -> promptSpeechInput());
        searchpageBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!search.getText().toString().isEmpty())
                {
                    Intent intent = new Intent(getApplicationContext(), Ced_Searchview.class);
                    intent.putExtra("ID", search.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            }
        });
        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Intent intent = new Intent(getApplicationContext(), Ced_Searchview.class);
                intent.putExtra("ID", search.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
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
                Log.i("currentLenght:", "" + s.length());
                if (s.length() == 0) {
                    suggestions.clear();
                    request = true;
                    Log.i("currentLenght1:", "" + suggestions.size());
                }
                if (s.length() >= 3) {
                    if (autocomplete){
                        if (request) {
                            request = false;
                            if (suggestions.size() == 0) {
                                try {
                                    searchdata.addProperty("keyword", s.toString());
                                    if (cedSessionManagement.getStoreId() != null) {
                                        searchdata.addProperty("store_id", cedSessionManagement.getStoreId());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                searchViewModel.getAutoCompleteData(Ced_Search.this, searchdata).observe(Ced_Search.this, apiResponse -> {
                                    switch (apiResponse.status) {
                                        case SUCCESS:
                                            request = true;

                                            try {
                                                JSONObject jsonObject = new JSONObject(apiResponse.data);
                                                if (jsonObject.getJSONObject(KEY_ITEM).getString("enabled").equals("true")) {
                                                    autocomplete = true;
                                                    if (jsonObject.getJSONObject(KEY_ITEM).getString(KEY_STATUS).equals("success")) {

                                                        suggestion = jsonObject.getJSONObject(KEY_ITEM).getJSONArray(KEY_NAME);
                                                        for (int i = 0; i < suggestion.length(); i++) {
                                                            JSONObject c = null;
                                                            c = suggestion.getJSONObject(i);
                                                            product_name = c.getString(KEY_PRODUCT_NAME);
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

                                                    } else {
                                                        if (jsonObject.getJSONObject(KEY_ITEM).getString(KEY_STATUS).equals("empty")) {
                                                            showmsg(getResources().getString(R.string.NoProductFound));
                                                        }
                                                    }
                                                } else {
                                                    autocomplete = false;
                                                    showmsg(getResources().getString(R.string.NoProductFound)); }
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

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
                    Intent intent = new Intent(getApplicationContext(), Ced_Searchview.class);
                    intent.putExtra("ID", result.get(0));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
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
                    Intent intent = new Intent(getApplicationContext(), Ced_Searchview.class);
                    intent.putExtra("ID", adapter.getItem(position));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
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
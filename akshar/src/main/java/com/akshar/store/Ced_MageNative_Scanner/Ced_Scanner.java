package com.akshar.store.Ced_MageNative_Scanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.sellersection.activity.Ced_Seller_Shop;

import org.json.JSONObject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_Scanner extends Ced_NavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.magenative_scanner, content, true);
        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                showmsg(getResources().getString(R.string.noresultfound));
                finish();
            } else {
                if (result.getFormatName().equals("EAN_13")) {
                    try {

                        Intent intent = new Intent(getApplicationContext(), Ced_NewProductView.class);
                        //  intent.putExtra("ean", result.getContents());
                        intent.putExtra("ean", result.getContents()/*"8906040851454"*/);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                }
                if (result.getFormatName().equals("QR_CODE")) {
                    try {
                        if (result.getContents().contains(getResources().getString(R.string.app_name))) {
                            String datavalue[] = result.getContents().split("/");
                            String valueid = datavalue[datavalue.length - 1];
                            if (valueid.contains(getResources().getString(R.string.app_name).replace(" ", "_").trim())) {
                                String id[] = valueid.split("#");
                                String product_id = id[0];
                                Intent intent = new Intent(getApplicationContext(), Ced_NewProductView.class);
                                intent.putExtra("product_id", product_id);
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                finish();

                            }
                        } else if (result.getContents().contains("ID")) {
                            JSONObject json = new JSONObject(result.getContents());
                            Intent sellershop = new Intent(Ced_Scanner.this, Ced_Seller_Shop.class);
                            sellershop.putExtra("ID", json.getString("ID"));
                            startActivity(sellershop);
                        } else {
                            showmsg(getResources().getString(R.string.noresultfound));
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        finish();
                    }

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
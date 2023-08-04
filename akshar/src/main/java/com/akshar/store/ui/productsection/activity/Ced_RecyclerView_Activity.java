package com.akshar.store.ui.productsection.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.akshar.store.ui.productsection.model.Ced_Data_Model;
import com.akshar.store.R;
import com.akshar.store.databinding.MagenativeRecyclerviewBinding;
import com.akshar.store.ui.productsection.adapter.Ced_RecyclerView_Adapter;

import java.util.ArrayList;


public class Ced_RecyclerView_Activity extends AppCompatActivity
{
    private static RecyclerView recyclerView;
    //String and Integer array for Recycler View Items

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        MagenativeRecyclerviewBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_recyclerview, null, false);
        setContentView(binding1.getRoot());
        initViews(binding1);
        populatRecyclerView();
    }

    // Initialize the view
    private void initViews(MagenativeRecyclerviewBinding binding1)
    {
        recyclerView = binding1.MageNativeRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
    }


    // populate the list view by adding data to arraylist
    private void populatRecyclerView()
    {
        Intent intent=getIntent();
        ArrayList<String> Ids=intent.getStringArrayListExtra("Ids");
        String names[]=intent.getStringArrayExtra("data_name");
        String images[]=intent.getStringArrayExtra("data_image");
        String ids[]=intent.getStringArrayExtra("data_ids");
        ArrayList<Ced_Data_Model>arrayList=new ArrayList<Ced_Data_Model>();
        for(int i=0;i<names.length;i++)
        {
            arrayList.add(new Ced_Data_Model(names[i],images[i],ids[i]));
        }

        Ced_RecyclerView_Adapter adapter = new Ced_RecyclerView_Adapter(getApplicationContext(), arrayList,Ids);
        recyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();// Notify the adapter

    }



}
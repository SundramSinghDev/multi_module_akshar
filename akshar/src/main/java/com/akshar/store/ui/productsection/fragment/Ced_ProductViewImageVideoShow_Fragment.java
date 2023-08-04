package com.akshar.store.ui.productsection.fragment;

/*public class Ced_ProductViewImageVideoShow_Fragment {
}*/


import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class Ced_ProductViewImageVideoShow_Fragment extends Fragment
{
    View v;
    String url;
    ImageView image;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
    {
        try
        {
            v = inflater.inflate(R.layout.magenative_image, container,false);
            ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();
            if(!memoryInfo.lowMemory)
            {
                image= (ImageView) v.findViewById(R.id.MageNative_image);
                url = getArguments().getString("url");
                final CharSequence stack[] = getArguments().getCharSequenceArray("stack");
                final int  position = getArguments().getInt("position");
                Log.i("REpo","imgfrg "+getArguments().getString("image"));
                if(getArguments().getString("image").equals("image")) {
                    image.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), Ced_ZoomImagePagerActivity.class);
                        intent.putExtra("IMAGEURL", stack);
                        intent.putExtra("POS", position);
                        startActivity(intent);
                    });
                }
                else if(getArguments().getString("image").equals("external-video")) {
                    image.setOnClickListener(v -> {
                        /*Log.i("REpo","vidpla "+getArguments().getString("image_url"));
                        Intent intent = new Intent(getActivity(), Ced_Product_Videoplayer.class);
                        intent.putExtra("url", getArguments().getString("image_url"));
                        startActivity(intent);*/
                        String youtubelink=  getArguments().getString("image_url");
                        View view=getLayoutInflater().inflate(R.layout.playyoutube_video_layout,null);
                        YouTubePlayerView youTubePlayerView = view.findViewById(R.id.videoview);
                        getLifecycle().addObserver(youTubePlayerView);

                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                //String videoId = "5OZZMweYckg";
                                String videoId = youtubelink.replace("https://www.youtube.com/watch?v=","").trim();
                                videoId = youtubelink.replace("https://youtu.be/","").trim();
                                youTubePlayer.loadVideo(videoId, 0f);
                            }
                        });
                        new MaterialAlertDialogBuilder(getActivity(),R.style.MaterialDialog)
                                .setView(view)
                                .setCancelable(true)
                                .show();
                    });
                }
                try
                {
                    if (!memoryInfo.lowMemory)
                    {

                        Glide.with(getActivity()).asBitmap()
                                .load(url)
                                .placeholder(R.drawable.placeholder)
                                .into(image);
                        //new DownloadTask(image).execute(url);
                    }
                }
                catch (Exception e)
                {

                }
            }
        }
        catch (Exception e)
        {

            Intent main=new Intent(getActivity(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(main);
            getActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
        return  v;
        /* try
        {
            v = inflater.inflate(R.layout.magenative_image, container,false);
            ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();
            if(!memoryInfo.lowMemory)
            {
                ImageView image= (ImageView) v.findViewById(R.id.MageNative_image);
                final String url = getArguments().getString("url");
                final CharSequence stack[] = getArguments().getCharSequenceArray("stack");
                final int  position = getArguments().getInt("position");
                image.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getActivity(), MageNative_ZoomImagePagerActivity.class);
                        intent.putExtra("IMAGEURL", stack);
                        intent.putExtra("POS", position);
                        startActivity(intent);
                    }
                });
                try
                {
                    if (!memoryInfo.lowMemory)
                    {

                        Glide.with(getActivity())
                                .load(url)

                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .into(image);
                        //new DownloadTask(image).execute(url);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {

            Intent main=new Intent(getActivity(),Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(main);
            getActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
        return  v;*/
    }

    @NonNull
    private ActivityManager.MemoryInfo getAvailableMemory()
    {
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager. getMemoryClass();
        return memoryInfo;
    }
}


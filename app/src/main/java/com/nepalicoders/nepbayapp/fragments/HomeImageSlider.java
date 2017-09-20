package com.nepalicoders.nepbayapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeImageSlider extends Fragment {

    String link;
    String image;
    int position;

    public HomeImageSlider() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_templates_image_slider_single_item, container, false);
        ImageView image = (ImageView) view.findViewById(R.id.sliderImage);
        CircularProgressView progressView  = (CircularProgressView) view.findViewById(R.id.loadingImage);

        //Getting the images of respective fragments
        Bundle imageSlider = getArguments();
        this.link = imageSlider.getString("link");
        this.image = imageSlider.getString("image");

        //Picasso.with(getActivity()).load(ApiUrl.HOME_BANNER_IMAGE_BASE_URL + this.image).into(image);

        progressView.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);
        final CircularProgressView progressV = progressView;
        Picasso.with(getActivity())
                .load(ApiUrl.HOME_BANNER_IMAGE_BASE_URL + this.image)
                .fit()
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressV.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub

                    }
                });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
            }
        });
        return view;
    }

}

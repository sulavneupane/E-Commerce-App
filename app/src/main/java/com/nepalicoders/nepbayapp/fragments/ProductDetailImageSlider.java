package com.nepalicoders.nepbayapp.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailImageSlider extends Fragment {


    String img;
    int position;

    public ProductDetailImageSlider() {
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
        this.img = imageSlider.getString("image");

        //Picasso.with(getActivity()).load(ApiUrl.NEPBAY_PRODUCT_IMAGES_URL_TEMPORARY + this.img).into(image);

        progressView.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);
        final CircularProgressView progressV = progressView;
        Picasso.with(getActivity())
                .load(ApiUrl.NEPBAY_PRODUCT_IMAGES_URL_TEMPORARY + this.img)
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

        PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);

        return view;
    }

}

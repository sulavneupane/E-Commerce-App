package com.nepalicoders.nepbayapp.fragments;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.analytics.Tracker;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.activities.MainActivity;
import com.nepalicoders.nepbayapp.adapters.HomeViewContainerAdapter;
import com.nepalicoders.nepbayapp.adapters.HorizontalViewProductsContainerAdapter;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.json.JsonParser;
import com.nepalicoders.nepbayapp.objects.HomeBanner;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View view;
    Fragment fragment;
    RecyclerView recyclerViewOffer;
    ViewPager imageSlider;
    CircularProgressView pd;
    TextView noInternetMessage;
    String[] FeaturedTitle;
    String[] FeaturedId;

    List<HomeBanner> banners;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_drawer_home, container, false);

        pd = (CircularProgressView) view.findViewById(R.id.loading);

        /*//animation for home fragment
        YoYo.with(Techniques.FadeInLeft).duration(600).playOn(view);*/

        //Recyclerview that contains both images slider at top and containers for horizontal view
        recyclerViewOffer = (RecyclerView) view.findViewById(R.id.horizontal_view_offer_container);

        Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API

        BackgroundThread thread = new BackgroundThread(ApiUrl.HOME_BANNER_URL, map, Connection.GET);
        thread.setBackgroundListener(getActivity(), new Callback() {


            @Override
            public void onFailure(Request request, IOException e) {
                pd.setVisibility(View.GONE);
                noInternetMessage = (TextView) view.findViewById(R.id.noInternetMessage);
                noInternetMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(Response response, String result) throws IOException {
                if (response.isSuccessful()) {
                    banners = JsonParser.parseHomeBanners(result);

                    Map<String, String> featuredCategoryMap = new HashMap<>(); //Use HasMap to store parameter required for the API

                    BackgroundThread featuredCategoryThread = new BackgroundThread(ApiUrl.featuredCategoryUrl, featuredCategoryMap, Connection.GET);
                    featuredCategoryThread.setBackgroundListener(getActivity(), new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                        }

                        @Override
                        public void onResponse(Response response, String result) throws IOException {

                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                FeaturedTitle = new String[jsonArray.length()];
                                FeaturedId = new String[jsonArray.length()];
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    FeaturedTitle[i] = jsonObject.getString("featured_name");
                                    FeaturedId[i] = jsonObject.getString("featured_cat_id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //Creating adapter for complete recyclerview of home fragment
                            HomeViewContainerAdapter adapter = new HomeViewContainerAdapter(getActivity(), banners, FeaturedTitle, FeaturedId, HomeFragment.this);
                            recyclerViewOffer.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewOffer.setAdapter(adapter);

                            pd.setVisibility(View.GONE);

                        }

                    });
                    featuredCategoryThread.execute();
                }
            }
        });

        thread.execute();

        return view;
    }

}

package com.nepalicoders.nepbayapp.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.adapters.HorizontalViewProductsContainerAdapter;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.application.App;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.json.JsonParser;
import com.nepalicoders.nepbayapp.objects.Product;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategoryFragment extends Fragment {

    private String cat_ID;
    private String[] sub_sub_category;
    private String[] sub_sub_category_id;
    private String[] product_id;
    private String[] product_name;
    private String[] product_price;
    private String[] product_image;
    View view;

    JSONArray list;

    Tracker mTracker;


    public SubCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_templates_sub_category, container, false);

        //animation for subcategory fragment
        YoYo.with(Techniques.FadeInRight).duration(700).playOn(view);

        cat_ID = getArguments().getString("cat_ID");
        //Snackbar.make(view, cat_ID, Snackbar.LENGTH_SHORT).show();
        mTracker = ((App)getActivity().getApplication()).getDefaultTracker();
       /* mTracker.setScreenName("SubCategoryFragment");*/
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("View")
                .setAction(getArguments().getString("itemname"))
                .build());

        final FragmentActivity c = getActivity();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.horizontal_view_products_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
        map.put("id", cat_ID); //map.put can be use as many time as required, dont change the 'id'

        BackgroundThread thread = new BackgroundThread(ApiUrl.subCategoryListUrl, map, Connection.GET);
        thread.setBackgroundListener(getActivity(), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response, String result) throws IOException {
                if(response.isSuccessful()){
                    try {
                        list = new JSONArray(result);
                        sub_sub_category = new String[list.length()];
                        sub_sub_category_id = new String[list.length()];
                        for (int k = 0; k < list.length(); k++) {
                            sub_sub_category[k] = list.getJSONObject(k).getString("category_name");
                            sub_sub_category_id[k] = list.getJSONObject(k).getString("cat_id");
                        }

                        HorizontalViewProductsContainerAdapter adapter = new HorizontalViewProductsContainerAdapter(c, SubCategoryFragment.this, sub_sub_category, sub_sub_category_id);
                        recyclerView.setAdapter(adapter);
/*
                    //Inside Horizontal scroll view container
                    // there is another recycler view that adjusts the products items in horizontal scrolling form
                    //The adapter is constructed in the same patter as this one is constructed
                    HorizontalScrollViewItemsAdapter productAdapter = new HorizontalScrollViewItemsAdapter(getActivity(), sub_sub_category_name, sub_sub_category_price);
                    productAdapter.horizontalScrollViewItems.setAdapter(productAdapter);
                    productAdapter.horizontalScrollViewItems.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
*/


                    } catch (JSONException e) {
                        //Log.d("CATEGORYERROR", e.getMessage());
                        e.printStackTrace();
                        list = null;
                        Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        thread.execute();
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return view;
    }


    public List<Product> getIndividualProducts(String id) {
        Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
        map.put("id", id); //map.put can be use as many time as required, dont change the 'id'

        BackgroundThread thread = new BackgroundThread(ApiUrl.PRODUCTS_DISPLAY_URL_TEMPORARY, map, Connection.GET);
        thread.setBackgroundListener(getActivity(), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response, String result) throws IOException {
                if(response.isSuccessful()){
                    List<Product> products = JsonParser.parseCategoryProductResult(result);
                }
            }
        });

        thread.execute();

        return null;
    }
}
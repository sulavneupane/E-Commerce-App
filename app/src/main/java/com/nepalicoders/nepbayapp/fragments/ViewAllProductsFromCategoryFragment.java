package com.nepalicoders.nepbayapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.adapters.HorizontalViewProductsContainerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAllProductsFromCategoryFragment extends Fragment {


    public ViewAllProductsFromCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_all_products_from_category, container, false);

        /*RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.horizontal_view_products_container);
        String[] titles = {"Offer", "Mobile Phones", "Men's Cloting", "Women's Clothing", "Computer Accessories"};

        HorizontalViewProductsContainerAdapter adapter = new HorizontalViewProductsContainerAdapter(getActivity(), titles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);*/

        return view;
    }

}

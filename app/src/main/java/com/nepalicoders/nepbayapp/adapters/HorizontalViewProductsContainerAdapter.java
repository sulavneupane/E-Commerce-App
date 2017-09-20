package com.nepalicoders.nepbayapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.fragments.CategoryProductsListingFragment;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.json.JsonParser;
import com.nepalicoders.nepbayapp.objects.Product;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.solovyev.android.views.llm.LinearLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sulav on 12/12/15.
 */
public class HorizontalViewProductsContainerAdapter extends RecyclerView.Adapter<HorizontalViewProductsContainerAdapter.ContainerViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private String containerHeader[];
    private String subCategoriesId[];
    private Fragment fragment;

    Map<Integer, ArrayList<Product>> allProducts = new HashMap<Integer,ArrayList<Product>>();
    List<HorizontalScrollViewCategoryItemsAdapter> subAdapter = new ArrayList<>();

    public HorizontalViewProductsContainerAdapter(Context context, Fragment fragment, String[] subCategories, String[] subCategoriesId){
        this.fragment = fragment;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.containerHeader = subCategories;
        this.subCategoriesId = subCategoriesId;
    }

    @Override
    public ContainerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_templates_horizontal_scroll_view, parent, false);
        ContainerViewHolder holder = new ContainerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ContainerViewHolder holder, final int position) {
        //ContainerViewHolder is for holding the view items of horizontal scroll view container
        holder.title.setText(Html.fromHtml(containerHeader[position]));

        //Setting onclick listener in the MORE link of the Horizontal Scroll View Container
        holder.moreLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle pass = new Bundle();
                pass.putString("sub_cat_ID", subCategoriesId[position]);//passing the clicked value to the next fragment
                pass.putString("sub_cat_title", containerHeader[position]);//passing the clicked value to the next fragment
                Fragment nextFrag = new CategoryProductsListingFragment();
                nextFrag.setArguments(pass);
                fragment.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.drawer_fragment, nextFrag).addToBackStack(null).commit();
            }
        });

        if(!(allProducts.containsKey(position))) {
            Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
            map.put("id", subCategoriesId[position]); //map.put can be use as many time as required, dont change the 'id'
            map.put("limit", "10");
            map.put("random", "true");

            BackgroundThread thread = new BackgroundThread(ApiUrl.PRODUCTS_DISPLAY_URL_TEMPORARY, map, Connection.GET);
            thread.setBackgroundListener(context, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                    holder.progressView.setVisibility(View.GONE);

                }

                @Override
                public void onResponse(Response response, String result) throws IOException {
                    if(response.isSuccessful()){
                        final ArrayList<Product> products = JsonParser.parseCategoryProductResult(result);

                        /*if(products.size() > 0){*/

                            allProducts.put(position, products);
                            holder.productAdapter.setProducts(allProducts.get(position));
                            holder.productAdapter.notifyDataSetChanged();

                        /*} else {

                            containerViewHolder.noProductFound.setVisibility(View.VISIBLE);

                        }*/
                    }

                    holder.progressView.setVisibility(View.GONE);
                }
            });

            thread.execute();
        }else{
            holder.progressView.setVisibility(View.GONE);

            holder.productAdapter.setProducts(allProducts.get(position));
            holder.productAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return containerHeader.length;
    }

    //Class to hold views of Horizontal Scroll View Container
    class ContainerViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView moreLink;
        RecyclerView horizontalScrollViewItems;
        HorizontalScrollViewCategoryItemsAdapter productAdapter;
        CircularProgressView progressView;
        TextView noProductFound;

        public ContainerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.header_offer);
            moreLink = (TextView) itemView.findViewById(R.id.header_more);
            horizontalScrollViewItems = (RecyclerView) itemView.findViewById(R.id.horizontal_scroll_view_items);
            progressView = (CircularProgressView) itemView.findViewById(R.id.loading);
            noProductFound = (TextView) itemView.findViewById(R.id.no_product_found);

            productAdapter = new HorizontalScrollViewCategoryItemsAdapter(context, null);
            horizontalScrollViewItems.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            horizontalScrollViewItems.setAdapter(productAdapter);
        }
    }
}

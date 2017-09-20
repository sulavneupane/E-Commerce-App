package com.nepalicoders.nepbayapp.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.fragments.HomeFragment;
import com.nepalicoders.nepbayapp.fragments.SubCategoryFragment;
import com.nepalicoders.nepbayapp.fragments.ViewAllProductsFromCategoryFragment;
import com.nepalicoders.nepbayapp.objects.HomeBanner;
import com.nepalicoders.nepbayapp.objects.Product;
import com.nepalicoders.nepbayapp.utils.Message;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

/**
 * Created by kshitij on 12/10/15.
 */
public class CategoryProductViewContainerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private SubCategoryFragment subCategoryFragment;
    private Context context;

    List<Product> products;

    //Constructor for receiving values, that are required in the adapter, from the Home Fragment
    public CategoryProductViewContainerAdapter(Context context, List<Product> products, SubCategoryFragment fragment) {
        this.context = context;
        this.subCategoryFragment = fragment;
        inflater = LayoutInflater.from(context);
        this.products = products;
    }
/*
    //Returns the view type i.e. whether the view type is images slider or horizontal scroll view container
    @Override
    public int getItemViewType(int position) {
        //Banners are at the beginning of the home fragment only so if the position is zero then return view type for images slider else for Horizontal scroll view container
        if (position == 0) {
            return VIEW_TYPE_HOME_BANNER;
        } else {
            return VIEW_TYPE_OFFERS_CONTAINER;
        }
    }*/

    //Creates every single new row in the recyclerview
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder holder = null;
        v = inflater.inflate(R.layout.fragment_templates_horizontal_scroll_view, parent, false);
        holder = new ContainerViewHolder(v);
        return holder;
    }

    //Binds data to every single row in the recyclerview
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //ContainerViewHolder is for holding the view items of horizontal scroll view container
        ContainerViewHolder containerViewHolder = (ContainerViewHolder) holder;
        containerViewHolder.title.setText(Html.fromHtml(products.get(position).getName()));
        //Setting onclick listener in the MORE link of the Horizontal Scroll View Container
        containerViewHolder.moreLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCategoryFragment.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_fragment, new ViewAllProductsFromCategoryFragment()).commit();
                Message.snackBarShortPositive(v.getRootView(), " Go to the more items of " + products.get(position).getName());
            }
        });
        //Inside Horizontal scroll view container there is another recycler view that adjusts the products items in horizontal scrolling form
        //The adapter is constructed in the same patter as this one is constructed
       /* HorizontalScrollViewItemsAdapter adapter = new HorizontalScrollViewItemsAdapter(context,products, products.get(position - 1).getPrice());
        containerViewHolder.horizontalScrollViewItems.setAdapter(adapter);
        containerViewHolder.horizontalScrollViewItems.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
*/
    }

    @Override
    public int getItemCount() {
        return (products.size());
    }

    //Class to hold views of Horizontal Scroll View Container
    class ContainerViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView moreLink;
        RecyclerView horizontalScrollViewItems;

        public ContainerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.header_offer);
            moreLink = (TextView) itemView.findViewById(R.id.header_more);
            horizontalScrollViewItems = (RecyclerView) itemView.findViewById(R.id.horizontal_scroll_view_items);
        }
    }

    //Class to hold views of Images Slider
    class SliderViewHolder extends RecyclerView.ViewHolder {

        ViewPager imageSlider;
        CirclePageIndicator indicator;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imageSlider = (ViewPager) itemView.findViewById(R.id.imageSlider);
            indicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
        }
    }
}

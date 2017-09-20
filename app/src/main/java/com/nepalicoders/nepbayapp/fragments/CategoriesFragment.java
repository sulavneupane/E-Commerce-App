package com.nepalicoders.nepbayapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.activities.MainActivity;
import com.nepalicoders.nepbayapp.adapters.AnimatedExpandableListView;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.nepalicoders.nepbayapp.utils.Message;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    List<OrdersFragment.GroupItem> items = new ArrayList<OrdersFragment.GroupItem>();
    JSONArray menu_1;
    JSONArray menu_2;
    JSONArray menu_3;
    String[] categoryNames;
    CircularProgressView pd;
    int i, j;
    OrdersFragment.GroupItem item;
    TextView noInternetMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_categories, container, false);
        /*//animation for categories fragment
        YoYo.with(Techniques.FadeInRight).duration(400).playOn(view);*/
        pd = (CircularProgressView) view.findViewById(R.id.loading);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       //Getting the categories from rest api

        BackgroundThread thread = new BackgroundThread(ApiUrl.CategoryListUrl, null, Connection.GET);
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
                    try {
                        menu_1 = new JSONArray(result);//get all the category list first level
                        for (i = 0; i < menu_1.length(); i++) {
                            if (menu_1.getJSONObject(i).getString("menu_status").equals("first")) {
                                item = new OrdersFragment.GroupItem();
                                item.title = menu_1.getJSONObject(i).getString("category_name");//placing the main category
                                //setting image

                                /*if (i % 2 != 0) {
                                    item.artwork = R.drawable.ic_home;
                                } else {
                                    item.artwork = R.drawable.ic_category;
                                }*/

                                //getting level 2 menus
                                menu_2 = menu_1.getJSONObject(i).getJSONArray("submenu");
                                for (j = 0; j < menu_2.length(); j++) {
                                    //Log.d("Sub Menu", i + " " + j + " " + menu_2.getJSONObject(j).getString("cat_id"));
                                    OrdersFragment.ChildItem child = new OrdersFragment.ChildItem();
                                    child.ItemName = menu_2.getJSONObject(j).getString("category_name");//placing the sub category
                                    child.hint = menu_2.getJSONObject(j).getString("cat_id");//need this id
                                    item.items.add(child);
                                }
                            }
                            items.add(item);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        menu_1 = null;
                        Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
                    }
                    pd.setVisibility(View.GONE);
                }
            }
        });

        thread.execute();
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        adapter = new ExampleAdapter(getActivity());
        adapter.setData(items);

        listView = (AnimatedExpandableListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.

                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                    //lastPosition = -1;
                    //groupPosition = -1;
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }

/*
                if (lastPosition != -1) {
                    listView.collapseGroup(lastPosition);
                }
                lastPosition = groupPosition;
*/

                return true;
            }
        });
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                OrdersFragment.ChildItem item = items.get(groupPosition).items.get(childPosition);
                Bundle pass = new Bundle();
                pass.putString("itemname", item.ItemName);
                pass.putString("cat_ID", item.hint);//passing the clicked value to the next fragment
                Fragment nextFrag = new SubCategoryFragment();
                nextFrag.setArguments(pass);
                ((MainActivity)getActivity()).onRightDrawerNavigationItemSelected(nextFrag, item.ItemName);
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_fragment, nextFrag).commit();
                //Toast.makeText(getActivity(), item.hint, Toast.LENGTH_LONG).show();
                return true;
            }
        });
        return view;
    }


    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<OrdersFragment.GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<OrdersFragment.GroupItem> items) {
            this.items = items;
        }

        @Override
        public OrdersFragment.ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        //Child group view
        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            OrdersFragment.ChildHolder holder;
            OrdersFragment.ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new OrdersFragment.ChildHolder();
                convertView = inflater.inflate(R.layout.fragment_drawer_order_listitem, parent, false);
                holder.item_name = (TextView) convertView.findViewById(R.id.childItem);
                holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (OrdersFragment.ChildHolder) convertView.getTag();
            }

            holder.item_name.setText(Html.fromHtml(item.ItemName));
            holder.hint.setText(Html.fromHtml(item.hint));


            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public OrdersFragment.GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }


        //main Group view
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            OrdersFragment.GroupHolder holder;
            OrdersFragment.GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new OrdersFragment.GroupHolder();
                convertView = inflater.inflate(R.layout.fragment_drawer_order_groupitem, parent, false);

                holder.title = (TextView) convertView.findViewById(R.id.parentTitle);
                holder.artwork = (CircleImageView) convertView.findViewById(R.id.artwork);
                holder.artwork.setVisibility(View.GONE);
                convertView.setTag(holder);
            } else {
                holder = (OrdersFragment.GroupHolder) convertView.getTag();
            }

            holder.title.setText(Html.fromHtml(item.title));
            holder.artwork.setImageResource(item.artwork);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }
}



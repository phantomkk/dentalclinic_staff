package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.ajithvgiri.searchdialog.SearchListAdapter;
import com.ajithvgiri.searchdialog.SearchListItem;
import com.dentalclinic.capstone.admin.R;

import java.util.ArrayList;
import java.util.List;

public class SearchDentistAdapter extends ArrayAdapter {

    public static String TAG = "SearchListAdapter";
    public List<SearchDentisItem> searchListItems;
    List<SearchDentisItem> suggestions = new ArrayList<>();
    SearchDentistAdapter.CustomFilter filter = new SearchDentistAdapter.CustomFilter();
    LayoutInflater inflater;
    private int textviewResourceID;


    public SearchDentistAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
        this.searchListItems = objects;
        this.textviewResourceID = textViewResourceId;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return searchListItems.size();
    }

    @Override
    public Object getItem(int i) {
        return searchListItems.get(i).getTitle();
    }

    @Override
    public long getItemId(int i) {
        return searchListItems.get(i).getId();
    }


    public int getposition(int id) {
        int position = 0;
        for (int i = 0; i < searchListItems.size(); i++) {
            if (id == searchListItems.get(i).getId()) {
                position = i;
            }
        }
        return position;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflateview = view;
        if (inflateview == null) {
            inflateview = inflater
                    .inflate(R.layout.item_search_dentist, null);
        }
        TextView tvDentistName = inflateview.findViewById(R.id.txt_dentist_name);
        TextView tvStatus = inflateview.findViewById(R.id.txt_dentist_status);
        tvDentistName.setText(searchListItems.get(i).getTitle());
        String status = searchListItems.get(i).getStatus();
        if (status.equals("Đang rãnh")) {
            tvStatus.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light));
        } else {
            tvStatus.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        }

        tvStatus.setText(searchListItems.get(i).getStatus());
        return inflateview;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }


    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();
            if (searchListItems != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                for (int i = 0; i < searchListItems.size(); i++) {
                    if (searchListItems.get(i).getTitle().toLowerCase().contains(constraint)) { // Compare item in original searchListItems if it contains constraints.
                        suggestions.add(searchListItems.get(i)); // If TRUE add item in Suggestions.
                    }
                }
            }
            FilterResults results = new FilterResults(); // Create new Filter Results and return this to publishResults;
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public static final class SearchDentisItem {
        int id;
        String title;
        private String status;

        public SearchDentisItem(int id, String title, String status) {
            this.id = id;
            this.title = title;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
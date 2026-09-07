package com.example.findx;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AutoCompleteLocalAdapter extends ArrayAdapter<LocalItem> {
    private List<LocalItem> localListFull;

    public AutoCompleteLocalAdapter(@NonNull Context context, @NonNull List<LocalItem> localList) {
        super(context, 0, localList);
        localListFull = new ArrayList<>(localList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return localFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.local_autocomplete_row, parent, false
            );
        }
        TextView textViewName = convertView.findViewById(R.id.local_view_name);
        ImageView imageViewFlag = convertView.findViewById(R.id.local_view_image);

        LocalItem LocalItem = getItem(position);

        if (LocalItem != null) {
            textViewName.setText(LocalItem.getLocalName());
            imageViewFlag.setImageResource(LocalItem.getImageLocal());
        }

        return convertView;
    }

    private Filter localFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<LocalItem> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(localListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (LocalItem item : localListFull) {
                    if (item.getLocalName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((LocalItem) resultValue).getLocalName();
        }
    };
}
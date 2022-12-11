package JuanjsleepRJ.jsleep_android;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Adapter of Expandable List View
 * @author juanjonathan67
 * @version 1.0.0
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    /**
     * Context of activity
     */
    Context context;
    /**
     * HashMap of filter category and their options
     */
    private Map<String, List<String>> filterCollection;
    /**
     * List of filter options
     */
    private List<String> filterCategory;

    /**
     * Constructor to display Expandable List View
     * @param context Current activity context
     * @param filterCategory List of filter options
     * @param filterCollection HashMap of filter category and their options
     */
    public ExpandableListAdapter(Context context,
                                List<String> filterCategory,
                                Map<String, List<String>> filterCollection){
       this.context = context;
       this.filterCollection = filterCollection;
       this.filterCategory = filterCategory;
    }

    /**
     * Get number of categories
     * @return Number of categories
     */
    @Override
    public int getGroupCount() {
        return filterCollection.size();
    }

    /**
     * Get number of category options
     * @param i Index of filter category
     * @return Number of category options
     */
    @Override
    public int getChildrenCount(int i) {
        return filterCollection.get(filterCategory.get(i)).size();
    }

    /**
     * Get filter category
     * @param i Index of filter category
     * @return Filter category
     */
    @Override
    public Object getGroup(int i) {
        return filterCategory.get(i);
    }

    /**
     * Get filter category options
     * @param i Index of filter category
     * @param i1 Index of filter category options
     * @return Filter category option
     */
    @Override
    public Object getChild(int i, int i1) {
        return filterCollection.get(filterCategory.get(i)).get(i1);
    }

    /**
     * Get Id of filter category
     * @param i index of filter category
     * @return Id of filter category
     */
    @Override
    public long getGroupId(int i) {
        return i;
    }

    /**
     * Get Id of filter category option
     * @param i index of filter category
     * @param i1 index of filter category option
     * @return Id of filter category option
     */
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    /**
     * Check if Id is stable
     * @return true
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * Get view of filter category
     * @param i Index of filter category
     * @param b If id is stable
     * @param view View
     * @param viewGroup View group
     * @return Return inflated view
     */
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String filterName = filterCategory.get(i);
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.category_item, null);
        }
        TextView item = view.findViewById(R.id.category);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(filterName);
        return view;
    }

    /**
     * Get view of filter category option
     * @param i Index of filter category
     * @param i1 Index of filter category option
     * @param b If id is stable
     * @param view View
     * @param viewGroup View group
     * @return Return inflated view
     */
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String optionName = filterCollection.get(filterCategory.get(i)).get(i1);
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.category_option, null);
        }
        TextView item = view.findViewById(R.id.option);
        item.setText(optionName);
        return view;
    }

    /**
     * Check if filter category option is selectable
     * @param i Index of filter category
     * @param i1 Index of filter category option
     * @return true
     */
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

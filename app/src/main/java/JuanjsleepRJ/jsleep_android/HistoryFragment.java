package JuanjsleepRJ.jsleep_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Fragment which lists completed payments
 * @author juanjonathan67
 * @version 1.0.0
 */
public class HistoryFragment extends Fragment {
    /**
     * View to list payment
     */
    ListView listView;
    /**
     * Index of the payment which is clicked
     */
    public static int completedIndex;

    /**
     * Create fragment
     * @param inflater Layout inflater
     * @param container View group container
     * @param savedInstanceState Instance state
     * @return Return inflated layout
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        getFragmentManager().beginTransaction().detach(HistoryFragment.this).attach(HistoryFragment.this).commit();
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    /**
     * Executed when view is created
     * @param view Created view
     * @param savedInstanceState Instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getView().findViewById(R.id.historyPaymentListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                PaymentActivity.completedRoomNames
        ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                completedIndex = i;
                startActivity(new Intent(getActivity(), PaymentConfirmationActivity.class));
                getActivity().getFragmentManager().popBackStack();
            }
        });
    }
}
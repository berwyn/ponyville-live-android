package com.ponyvillelive.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ponyvillelive.app.BusProvider;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.event.StopRequestedEvent;

/**
 * A basic fragment to handle the "now playing" drawer present in the main activity.
 */
public class BottomDrawerFragment extends Fragment {

    private ImageButton cancelButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BottomDrawerFragment.
     */
    public static BottomDrawerFragment newInstance() {
        BottomDrawerFragment fragment = new BottomDrawerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public BottomDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_drawer, container, false);

        cancelButton = (ImageButton) view.findViewById(R.id.btn_drawer_cancel);
        cancelButton.setOnClickListener((clickedView) -> BusProvider.getBus().post(new StopRequestedEvent()));

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

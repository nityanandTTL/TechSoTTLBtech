package com.thyrocare.btechapp.NewScreenDesigns.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thyrocare.btechapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KcfVisflowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KcfVisflowFragment extends Fragment {


    public static KcfVisflowFragment newInstance() {
        KcfVisflowFragment fragment = new KcfVisflowFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kcf_visflow, container, false);
    }
}
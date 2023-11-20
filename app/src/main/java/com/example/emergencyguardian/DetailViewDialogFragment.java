package com.example.emergencyguardian;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.emergencyguardian.databinding.FragmentDetailViewDialogBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class DetailViewDialogFragment extends DialogFragment {


    private FragmentDetailViewDialogBinding binding;



    public DetailViewDialogFragment() {
        // Required empty public constructor
    }

    public static DetailViewDialogFragment newInstance(String name,double lat,double lon,String icon) {
        DetailViewDialogFragment fragment = new DetailViewDialogFragment();
        Bundle args = new Bundle();

        args.putString("ARG_NAME",name);
        args.putDouble("ARG_LAT",lat);
        args.putDouble("ARG_LON",lon);
        args.putString("ARG_ICON",icon);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDetailViewDialogBinding.inflate(inflater, container, false);
        View view=binding.getRoot();

        String name= getArguments().getString("ARG_NAME");
        Double lat= getArguments().getDouble("ARG_LAT");
        Double lon= getArguments().getDouble("ARG_LON");
        String icon= getArguments().getString("ARG_ICON");


        List<Address> address=null;
        Geocoder geocoder= new Geocoder(requireContext(), Locale.getDefault());
        try {
            address = geocoder.getFromLocation(lat,lon,1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        binding.nameTextView.setText(name);
        binding.latTextView.setText(lat.toString());
        binding.lonTextView.setText(lon.toString());
        binding.addressTextView.setText(address.get(0).getAddressLine(0));



        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });




        return view;
    }



}
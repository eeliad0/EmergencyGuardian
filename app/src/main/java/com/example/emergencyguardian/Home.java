package com.example.emergencyguardian;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class Home extends Fragment {
    private CardView cardEmergencyButton;
    private CardView cardEmergencyCall;
    private CardView cardEmergencyText;
    private CardView cardNearMe;
    private CardView cardMyLocation;
    private CardView cardSirenPlay;
    private CardView cardTakeVideo;
    private CardView cardSelfDefence;

    private NavController navController;


    private ActivityResultLauncher<String> requestPermissionLauncherLoc =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission granted, navigate to MyLocationFragment
                    navController.navigate(R.id.action_HomeFragment_to_MyLocationFragment);
                } else {
                    // Permission denied, show a toast or handle it accordingly
                    Toast.makeText(requireContext(), "Location Permissions Required for this function", Toast.LENGTH_SHORT).show();
                }
            });


    private ActivityResultLauncher<String> requestPermissionLauncherPhone =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    dialPhoneNumber();
                } else {
                    // Permission denied, show a toast or handle it accordingly
                    Toast.makeText(requireContext(), "Phone Permissions Required for this function", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cardEmergencyButton = view.findViewById(R.id.card_emergency_button);
        cardEmergencyCall = view.findViewById(R.id.card_emergency_call);
        cardEmergencyText = view.findViewById(R.id.card_emergency_text);
        cardNearMe = view.findViewById(R.id.card_near_me);
        cardMyLocation = view.findViewById(R.id.card_my_location);
        cardTakeVideo = view.findViewById(R.id.card_take_video);
        cardSirenPlay = view.findViewById(R.id.card_siren_play);
        cardSelfDefence = view.findViewById(R.id.card_self_defence);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        cardEmergencyButton.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_EmergencyButtonFragment));
        cardEmergencyCall.setOnClickListener(v ->{

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted. Request it using ActivityResultLauncher.
                requestPermissionLauncherPhone.launch(android.Manifest.permission.CALL_PHONE);
            } else {
                // Permission already granted, navigate directly
                dialPhoneNumber();
            }


        });
        cardEmergencyText.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_EmergencyTextFragment));
        cardNearMe.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_EmergencyContactFragment));
        cardMyLocation.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted. Request it using ActivityResultLauncher.
                requestPermissionLauncherLoc.launch(android.Manifest.permission.ACCESS_FINE_LOCATION);
            } else {
                // Permission already granted, navigate directly
                navController.navigate(R.id.action_HomeFragment_to_MyLocationFragment);
            }
        });
        cardSirenPlay.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_SirenPlayFragment));
        cardTakeVideo.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_TakeVideoFragment));
        cardSelfDefence.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_SelfDefenceFragment));
    }


    private void dialPhoneNumber() {
        // Create an Intent with ACTION_DIAL and the phone number
        Intent dialIntent = new Intent(Intent.ACTION_CALL);
        dialIntent.setData(Uri.parse("tel:" + "112"));

        // Check if there's an activity that can handle the intent
        if (dialIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Start the dialer activity
            startActivity(dialIntent);
        } else {
            // Handle the case where there's no activity to handle the intent
            Toast.makeText(requireContext(), "No phone application found", Toast.LENGTH_SHORT).show();
        }
    }
}

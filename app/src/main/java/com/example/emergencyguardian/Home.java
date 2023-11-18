package com.example.emergencyguardian;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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

        NavController navController = Navigation.findNavController(view);

        cardEmergencyButton.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_EmergencyButtonFragment));
        cardEmergencyCall.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_EmergencyCallFragment));
        cardEmergencyText.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_EmergencyTextFragment));
        cardNearMe.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_NearMeFragment));
        cardMyLocation.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_MyLocationFragment));
        cardSirenPlay.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_SirenPlayFragment));
        cardTakeVideo.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_TakeVideoFragment));
        cardSelfDefence.setOnClickListener(v -> navController.navigate(R.id.action_HomeFragment_to_SelfDefenceFragment));
    }
}

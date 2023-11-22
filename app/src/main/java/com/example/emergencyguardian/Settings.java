package com.example.emergencyguardian;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.navigation.Navigation;

public class Settings extends Fragment {
    private CheckBox notificationCheckBox;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout editEmergencyContacts = view.findViewById(R.id.EditEmergencyContacts);
        editEmergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_SettingsFragment_to_EmergencyContactsFragment);
            }
        });

        RelativeLayout accountEdit = view.findViewById(R.id.AccountEdit);
        accountEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_SettingsFragment_to_EditProfileFragment);
            }
        });
        notificationCheckBox = view.findViewById(R.id.notificationCheckBox);
        Button saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }
    private void saveSettings() {
        boolean notificationsEnabled = notificationCheckBox.isChecked();

        // Save the settings or perform necessary actions here
        // For this example, let's show a Toast indicating the settings have been saved
        String message = notificationsEnabled ? "Notifications enabled" : "Notifications disabled";
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
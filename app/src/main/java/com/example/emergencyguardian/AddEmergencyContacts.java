package com.example.emergencyguardian;
import static android.content.Context.MODE_PRIVATE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEmergencyContacts extends Fragment {
    private FirebaseFirestore db;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText relationshipEditText;
    String userEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_emergency_contacts, container, false);

        db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("userEmail", "");
        nameEditText = view.findViewById(R.id.name_add);
        phoneEditText = view.findViewById(R.id.phone_add);
        relationshipEditText = view.findViewById(R.id.relationship_add);

        Button addContactButton = view.findViewById(R.id.save_add_button);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmergencyContact();
                Navigation.findNavController(view).navigate(R.id.action_AddEmergencyContacts_to_EmergencyContacts);
            }
        });

        return view;
    }

    private void addEmergencyContact() {

        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String relationship = relationshipEditText.getText().toString();

        Map<String, Object> contact = new HashMap<>();
        contact.put("name", name);
        contact.put("phone", phone);
        contact.put("relationship", relationship);

        db.collection("emergency_guardian").document(userEmail).collection("emergencyContacts").add(contact)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Contact added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding contact", e);
                    }
                });
    }
}
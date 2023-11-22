package com.example.emergencyguardian;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EmergencyContacts extends Fragment implements EmergencyContactsAdapter.OnContactListener{
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private EmergencyContactsAdapter adapter;
    private List<EmergencyContactClass> contacts;

    String userEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_contacts, container, false);

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("userEmail", "");
        FloatingActionButton addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_EmergencyContact_to_AddEmergencyContacts);
            }
        });
        loadEmergencyContacts();

        return view;
    }

    private void loadEmergencyContacts() {
        db.collection("emergency_guardian").document(userEmail).collection("emergencyContacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            contacts = new ArrayList<>(); // Use the contacts field, not a local variable
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                EmergencyContactClass contact = document.toObject(EmergencyContactClass.class);
                                contact.setId(document.getId()); // Set the ID
                                contacts.add(contact);
                            }
                            adapter = new EmergencyContactsAdapter(contacts, EmergencyContacts.this);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    @Override
    public void onContactDelete(String id) {
        deleteContact(id);
    }

    private void deleteContact(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("emergency_guardian").document(userEmail).collection("emergencyContacts").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        // Find the position of the contact with the given ID
                        int position = -1;
                        for (int i = 0; i < contacts.size(); i++) {
                            if (contacts.get(i).getId().equals(id)) {
                                position = i;
                                break;
                            }
                        }
                        // If the contact was found, remove it from the list and notify the adapter
                        if (position != -1) {
                            contacts.remove(position);
                            adapter.notifyItemRemoved(position);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
}

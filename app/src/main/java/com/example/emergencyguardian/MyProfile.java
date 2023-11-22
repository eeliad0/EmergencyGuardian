package com.example.emergencyguardian;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencyguardian.databinding.ContentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is responsible for demonstrating how to read firebase data
 */
public class MyProfile extends Fragment {

    String userEmail;
    private ContentProfileBinding binding;
    private TextView textName, textPhone, textEmail, textBirthday;
    private FirebaseFirestore db;
    private SimpleDateFormat dateFormat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("userEmail", "");
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the binding for this fragment
        binding = ContentProfileBinding.inflate(inflater, container, false);

        // Initialize UI elements
        textName = binding.nameText;
        textEmail = binding.emailText;
        textPhone = binding.phoneText;
        textBirthday = binding.dobText;


        // Get a reference to the Firestore document based on the documentReferenceId
        DocumentReference docRef = db.collection("emergency_guardian").document(userEmail);

        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.DEFAULT;

        // Retrieve the document data from Firestore
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Set the retrieved user data in the UI
                        setUserInfo(document);
                    } else {
                        Toast.makeText(requireContext(), "Document does not exist", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Failure: could not retrieve data...", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Set up a click listener for a button that navigates to the user edit info fragment
        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                // Send the document reference ID to the new fragment
                bundle.putString("documentReferenceId", userEmail);
                NavHostFragment.findNavController(MyProfile.this)
                        .navigate(R.id.action_MyProfileFragment_to_EditProfileFragment, bundle);
            }
        });

        RelativeLayout editEmergencyContacts = binding.emergencyContacts;
        editEmergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_MyProfileFragment_to_EmergencyContactsFragment);
            }
        });
        return binding.getRoot();
    }

    // Helper method to set user information in the UI
    private void setUserInfo(DocumentSnapshot document) {
        // Get data from the document
        String name = document.getString("name");
        String phone = document.getString("phone");
        String email = document.getString("email");
        Date birthday = document.getDate("birthday");



        // Set the retrieved data to the respective TextViews
        textName.setText(name);
        textPhone.setText(phone);
        textEmail.setText(email);

        if (birthday != null) {
            textBirthday.setText(dateFormat.format(birthday));
        }
    }
}
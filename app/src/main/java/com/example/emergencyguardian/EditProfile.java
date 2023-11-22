package com.example.emergencyguardian;
import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.example.emergencyguardian.databinding.FragmentEditProfileBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for demonstrating on how to edit and delete firebase data
 */
public class EditProfile extends Fragment {

    private FragmentEditProfileBinding binding;
    String userEmail;
    private EditText editTextName, editTextPhone, editTextBirthday;

    private Button buttonSave;

    private FirebaseFirestore db;
    private DatePickerDialog datePickerDialog;
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
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);

        // Initialize UI elements
        editTextName = binding.nameEdit;
        editTextPhone = binding.phoneEdit;
        editTextBirthday = binding.dobEdit;


        buttonSave = binding.saveButton;

        // Get a reference to the Firestore document based on the documentReferenceId
        DocumentReference docRef = db.collection("emergency_guardian").document(userEmail);
        Source source = Source.DEFAULT;

        // Retrieve document data from Firestore
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

        // Initialize the DatePickerDialog
        datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);

            // Format the date
            String formattedDate = dateFormat.format(calendar.getTime());

            // Display the formatted date in the EditText
            editTextBirthday.setText(formattedDate);
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        // Show date picker dialog when the birthdate EditText is clicked
        editTextBirthday.setOnClickListener(v -> datePickerDialog.show());

        // Define a click listener for the "Save" button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String name = editTextName.getText().toString();
                String phone = editTextPhone.getText().toString();

                Date birthday = null;
                try {
                    birthday = dateFormat.parse(editTextBirthday.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }


                // Create a map to update user data in Firestore
                Map<String, Object> userUpdate = new HashMap<>();
                userUpdate.put("name", name);
                userUpdate.put("phone", phone);
                userUpdate.put("birthday", birthday);

                // Update the document with the new user data
                docRef.update(userUpdate)
                        .addOnSuccessListener(documentReference -> {
                            // Show a success message
                            Toast.makeText(requireContext(), "Info Saved Successfully", Toast.LENGTH_SHORT).show();

                            // Post a delayed action to navigate after 3 seconds
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Create a bundle to send the documentReferenceId to the next fragment
                                    Bundle bundle = new Bundle();
                                    bundle.putString("documentReferenceId", userEmail);

                                    // Navigate to the UserViewInfoFragment with the bundle
                                    NavHostFragment.findNavController(EditProfile.this)
                                            .navigate(R.id.action_EditProfileFragment_to_MyProfileFragment, bundle);
                                }
                            }, 3000); // 3000 milliseconds (3 seconds)
                        })
                        .addOnFailureListener(e -> {
                            // Show a failure message
                            Toast.makeText(requireContext(), "Failure: info were not saved...", Toast.LENGTH_LONG).show();
                        });
            }
        });

        // Define a click listener for the "Delete" button

        return binding.getRoot();
    }

    // Helper method to set user information in the UI
    private void setUserInfo(DocumentSnapshot document) {
        String name = document.getString("name");
        String phone = document.getString("phone");
        Date birthday = document.getDate("birthday");


        // Set the user information in the UI
        editTextName.setText(name);
        editTextPhone.setText(phone);


        if (birthday != null) {
            editTextBirthday.setText(dateFormat.format(birthday));
        }


    }
}

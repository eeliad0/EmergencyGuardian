package com.example.emergencyguardian;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EmergencyText extends Fragment {

    public EmergencyText() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_text, container, false);

        EditText messageEditText = view.findViewById(R.id.messageEditText);
        Button sendButton = view.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the message from the EditText
                String message = messageEditText.getText().toString();

                // Create an intent to send a message
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, message);

                // Verify if there's an app that can handle the intent
                if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    // Start the activity (in this case, the messaging app)
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}

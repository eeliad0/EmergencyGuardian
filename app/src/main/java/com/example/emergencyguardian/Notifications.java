package com.example.emergencyguardian;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Notifications extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Get the saved notifications from shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("notifications", Context.MODE_PRIVATE);
        Map<String, ?> notifications = sharedPreferences.getAll();

        // Convert the saved notifications to a list of Notification objects
        List<NotificationClass> notificationList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : notifications.entrySet()) {
            String title = entry.getKey();
            String body = (String) entry.getValue();
            notificationList.add(new NotificationClass(title, body));
        }

        // Display the notifications in a RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new NotificationsAdapter(notificationList));

        return view;
    }
}
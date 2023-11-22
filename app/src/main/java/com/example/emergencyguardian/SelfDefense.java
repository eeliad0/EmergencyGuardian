package com.example.emergencyguardian;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SelfDefense extends Fragment {
    private static final String DEVELOPER_KEY = "AIzaSyAweV1Dsn4WnG82MFyfhKMH6EcAkNotl3o";
    private static final String APPLICATION_NAME = "Emergency Guardian";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private String apiResponse;
    private TextView responseTextView;

    YouTubePlayerView youTubePlayerView;
    YouTubePlayerView youTubePlayerView2;
    YouTubePlayerView youTubePlayerView3;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_defence, container, false);
        youTubePlayerView = view.findViewById(R.id.youtubePlayerView);
        youTubePlayerView2 = view.findViewById(R.id.youtubePlayerView2);
        youTubePlayerView3 = view.findViewById(R.id.youtubePlayerView3);
        getLifecycle().addObserver(youTubePlayerView);
        new YouTubeTask().execute();



        return view;
    }


    private YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }




    private class YouTubeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                YouTube youtubeService = getService();
                YouTube.Search.List request = youtubeService.search()
                        .list("snippet");
                SearchListResponse response = request.setKey(DEVELOPER_KEY)
                        .setQ("Self Defense Techniques")
                        .execute();

                apiResponse=response.toString();
                return response.toString();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                JSONObject resp= null;
                try {
                    resp = new JSONObject(result);
                    JSONArray jsonArr=resp.getJSONArray("items");

                    String vid1=jsonArr.getJSONObject(0).getJSONObject("id").getString("videoId");
                    String vid2=jsonArr.getJSONObject(1).getJSONObject("id").getString("videoId");
                    String vid3=jsonArr.getJSONObject(2).getJSONObject("id").getString("videoId");


                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(vid1, 0);
                        }
                    });

                    youTubePlayerView2.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(vid2, 0);
                        }
                    });

                    youTubePlayerView3.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(vid3, 0);
                        }
                    });



                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        private void handleJsonResponse(String response) throws JSONException {



//            for (int i=0;i<jsonArr.length();i++){
//
//            }
        }

    }
}
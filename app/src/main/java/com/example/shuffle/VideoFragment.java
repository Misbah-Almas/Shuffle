package com.example.shuffle;

import static com.example.shuffle.MainActivity.items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    RecyclerView recyclerView1;
    MusicAdapter musicAdapter;

    public VideoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView1.setHasFixedSize(true);
        if (!(items.length < 1))
        {
            musicAdapter = new MusicAdapter(getContext(), items);
            recyclerView1.setAdapter(musicAdapter);
            recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                    false));
        }
        return view;
    }
}
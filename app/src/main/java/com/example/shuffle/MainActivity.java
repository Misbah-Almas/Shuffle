package com.example.shuffle;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    static ArrayList<File> mySongs;
    static String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_title_layout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_app_icon_mini);
        setContentView(R.layout.activity_main);
        runtimePermission();


    }

    public void runtimePermission()
    {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        initViewPager();
                        displaySong();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }

    public ArrayList<File> findSong(File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        if (files != null) {
            for(File singleFile : files)
            {
                if (singleFile.isDirectory() && !singleFile.isHidden())
                {
                    arrayList.addAll(findSong(singleFile));
                }
                else
                {
                    if ((singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) && !singleFile.getName().startsWith("."))
                    {
                        arrayList.add(singleFile);
                    }
                }
            }
        }
        return arrayList;

    }

    public void displaySong()
    {
        mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for (int i=0;i<mySongs.size();i++)
        {
            items[i] = mySongs.get(i).getName().replace(".mp3","").replace(".wav","");
        }
    }

    private void initViewPager() {
        ViewPager2 viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragments(new AudioFragment(), "Audios");
        viewPagerAdapter.addFragments(new VideoFragment(), "Albums");
        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getPageTitle(position))
        ).attach();

    }

    public static class ViewPagerAdapter extends FragmentStateAdapter {

        private final ArrayList<Fragment> fragments;
        private final ArrayList<String> titles;


        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        void addFragments(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);

        }

        CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }

    }
}
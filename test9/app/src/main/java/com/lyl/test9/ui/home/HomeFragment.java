package com.lyl.test9.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lyl.test9.MainActivity;
import com.lyl.test9.ui.home.search.SearchActivity;
import com.lyl.test9.R;
import com.lyl.test9.ui.home.Activity.EditActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private Button button;
    private ViewPager viewPager;
    private static List<String> datas = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private PagerAdapter adapter;
    private TextView search_tv;

    public static boolean flag = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initDatas();
        flag = false;
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);
        button = root.findViewById(R.id.add_dle_btn);
        search_tv = root.findViewById(R.id.search_text);
        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                startActivityForResult(intent, 0);
                Toast.makeText(getActivity(),"good", Toast.LENGTH_SHORT).show();

            }
        });
        initViews();
        return root;
    }

    private void initDatas() {
        if (!flag){
            datas.clear();
            SharedPreferences my_tags = getActivity().getSharedPreferences("my_tags",0);
            String tmp = my_tags.getString("tags","A-B-C-D-E-F-G-H-I");
            if (!tmp.equals("")){
                String[] tmpList = tmp.split("-");
                for (int i = 0; i < tmpList.length; i++){
                    datas.add(tmpList[i]);
                }
            }
        }
    }
    private void  initViews() {
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            s.add("ssssss");
        }
        if (datas.size() > 4){
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        if (datas.size() == 0){
            //Toast.makeText(getContext(),"傻逼，删空你看你m呢？？？？？",Toast.LENGTH_SHORT).show();
        }

        //循环注入标签
        fragments.clear();
        for (String tab : datas) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
            HomeSubFragment h = new HomeSubFragment();
            h.getS(s);
            h.initString(tab);
            fragments.add(h);
        }

        //tabLayout.addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) this);
        adapter = new PagerAdapter(getChildFragmentManager(), datas, fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
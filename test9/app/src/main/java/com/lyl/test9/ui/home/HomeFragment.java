package com.lyl.test9.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lyl.test9.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private Button button;
    private ViewPager viewPager;
    private List<String> datas = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private PagerAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initDatas();
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);
        button = root.findViewById(R.id.add_dle_btn);
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
        datas.add("test1");
        datas.add("test2");
        datas.add("test3");
        datas.add("test4");
        datas.add("test5");
        datas.add("test6");
        datas.add("test7");
        datas.add("test8");
        datas.add("test9");
    }
    private void initViews() {
        //循环注入标签
        for (String tab : datas) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            s.add("ssssss");
        }
        //设置TabLayout点击事件
        for (int i = 0; i < 9; i++) {
            HomeSubFragment h = new HomeSubFragment();
            h.getS(s);
            fragments.add(h);
        }
        //tabLayout.addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) this);
        adapter = new PagerAdapter(getChildFragmentManager(), datas, fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
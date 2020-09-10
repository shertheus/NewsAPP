package com.lyl.news.ui.home;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import com.lyl.news.R;
import com.lyl.news.Utils.DatabaseHelper;
import com.lyl.news.Utils.UrlGeter;
import com.lyl.news.ui.home.Activity.EditActivity;
import com.lyl.news.ui.home.search.SearchActivity;

import java.util.ArrayList;
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
    private DatabaseHelper helper;
    private final int listnum = 20;    //初次访问存入数据库的新闻数
    private final int shownum = 10;     //展示的新闻数

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
        try {
            initViews();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return root;
    }

    private void initDatas() {
        if (!flag){
            datas.clear();
            SharedPreferences my_tags = getActivity().getSharedPreferences("my_tags",0);
            String tmp = my_tags.getString("tags","新闻-论文");
            if (!tmp.equals("")){
                String[] tmpList = tmp.split("-");
                for (int i = 0; i < tmpList.length; i++){
                    datas.add(tmpList[i]);
                }
            }
        }
    }
    private void  initViews() throws InterruptedException {
        //获取新闻信息
        UrlGeter geter = new UrlGeter("https://covid-dashboard.aminer.cn/api/events/list?type=news&page=2");
        geter.Gets();
        List<String> n_show_list;
        List<String> n_title_list = new ArrayList<>();
        List<String> n_date_list = new ArrayList<>();
        List<String> n_content_list = new ArrayList<>();
        List<String> n_source_list = new ArrayList<>();
        //获取新闻标题
        for (int i = 0; i < listnum; i++){
            String tem;
            while ((tem = geter.getData("title")) == null) {
                Thread.sleep(100);
            }
            n_title_list.add(tem);
        }
        //获取新闻的时间
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            n_date_list.add(geter.getData("date"));
        }
        //获取新闻的内容
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            n_content_list.add(geter.getData("content"));
        }
        //获取新闻的来源
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            n_source_list.add(geter.getData("source"));
        }
        //将list信息存入数据库
        helper = new DatabaseHelper(getActivity(), "NewsDatabase.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from news");
        db = helper.getWritableDatabase();
        for(int i=listnum-1;i>=0;i--){
            //组装数据
            ContentValues values = new ContentValues();
            values.put("title", n_title_list.get(i));
            values.put("date", n_date_list.get(i));
            values.put("content",n_content_list.get(i));
            values.put("source",n_source_list.get(i));
            //存入数据库
            db.insert("news",null,values);
        }
        db.close();
        n_show_list = n_title_list.subList(0,shownum);

        //获取paper信息
        geter = new UrlGeter("https://covid-dashboard.aminer.cn/api/events/list?type=paper&page=2");
        geter.Gets();
        List<String> p_show_list;
        List<String> p_source_list = new ArrayList<>();
        List<String> p_content_list = new ArrayList<>();
        List<String> p_date_list = new ArrayList<>();
        List<String> p_title_list = new ArrayList<>();
        List<String> p_author_list = new ArrayList<>();
        //获取论文标题
        for (int i = 0; i < listnum; i++){
            String tem;
            while ((tem = geter.getData("title")) == null) {
                Thread.sleep(100);
            }
            p_title_list.add(tem);
        }
        //获取论文的时间
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            p_date_list.add(geter.getData("date"));
        }
        //获取论文的内容
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            p_content_list.add(geter.getData("content"));
        }
        //获取论文的来源
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            p_source_list.add(geter.getData("source"));
        }
        //获取论文的作者
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            p_author_list.add(geter.getData("authors"));
        }
        //将list信息存入数据库
        helper = new DatabaseHelper(getActivity(), "NewsDatabase.db", null, 1);
        db = helper.getWritableDatabase();
        db.execSQL("delete from paper");
        db = helper.getWritableDatabase();
        for(int i=listnum-1;i>=0;i--){
            //组装数据
            ContentValues values = new ContentValues();
            values.put("title", p_title_list.get(i));
            values.put("date", p_date_list.get(i));
            values.put("content",p_content_list.get(i));
            values.put("source",p_source_list.get(i));
            values.put("author",p_author_list.get(i));
            //存入数据库
            db.insert("paper",null,values);
        }
        db.close();
        p_show_list = p_title_list.subList(0,shownum);

//        SQLiteDatabase dbr = helper.getReadableDatabase();
//        Cursor c = dbr.rawQuery("select * from Papers",null);
//        if(c.moveToFirst()){
//            do{
//                String title = c.getString(c.getColumnIndex("papers_title"));
//                String author = c.getString(c.getColumnIndex("papers_author"));
//                String content = c.getString(c.getColumnIndex("papers_content"));
//                String source = c.getString(c.getColumnIndex("papers_source"));
//                System.out.print("source: "+source);
//                System.out.print("   title: "+title);
//                System.out.print("   author: "+author);
//                System.out.println("   content : "+content);
//            }while(c.moveToNext());
//        }
//        dbr.close();


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
            if(tab.equals("新闻"))
                h.setS(n_show_list,"news");
            else
                h.setS(p_show_list,"paper");
            //h.initString(tab);
            fragments.add(h);
        }

        //tabLayout.addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) this);
        adapter = new PagerAdapter(getChildFragmentManager(), datas, fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
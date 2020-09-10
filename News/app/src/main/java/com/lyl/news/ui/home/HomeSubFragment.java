package com.lyl.news.ui.home;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lyl.news.R;
import com.lyl.news.Utils.DatabaseHelper;
import com.lyl.news.Utils.UrlGeter;
import com.lyl.news.ui.home.Activity.NewsActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeSubFragment extends Fragment implements RefreshListView.OnRefreshListener,RefreshListView.OnLoadMoreListener{
    private String label;
    private RefreshListView mListView;
    private List<String> titledata = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private final static int REFRESH_COMPLETE = 0;
    private final static int LOAD_COMPLETE = 1;
    private DatabaseHelper helper;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    mListView.setOnRefreshComplete();
                    mAdapter.notifyDataSetChanged();
                    break;
                case LOAD_COMPLETE:
                    mListView.setOnLoadMoreComplete();
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        };
    };

    public void setS(List<String> s, String label){
        titledata.addAll(s);
        this.label = label;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subhome, container, false);
        final Context context = this.getActivity();
        mListView = root.findViewById(R.id.ListView);

        mAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, titledata);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadMoreListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context, "position=" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                    UrlGeter geter = new UrlGeter("https://covid-dashboard.aminer.cn/api/events/list?type="+label+"&page=1");
                    geter.Gets();
                    String title;
                    while ((title = geter.getData("title")) == null) {
                        Thread.sleep(100);
                    }
                    geter.setZero();
                    String content = geter.getData("content");
                    geter.setZero();
                    String source = geter.getData("source");
                    geter.setZero();
                    String date = geter.getData("date");
                    String author="";
                    if(label.equals("paper")){
                        geter.setZero();
                        author = geter.getData("author");
                    }

                    helper = new DatabaseHelper(getActivity(), "NewsDatabase.db", null, 1);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    Cursor cursor = db.rawQuery("select * from news where title=?", new String[]{title});
                    if(cursor.getCount() == 0){
                        //组装数据
                        ContentValues values = new ContentValues();
                        values.put("title", title);
                        values.put("date", date);
                        values.put("content",content);
                        values.put("source",source);
                        if(label.equals("paper")){
                            values.put("author",author);
                        }
                        //存入数据库
                        db.insert(label,null,values);
                        titledata.add(0, title);
                    }
                    mHandler.sendEmptyMessage(REFRESH_COMPLETE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onLoadMore() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                    String last_title = titledata.get(titledata.size()-1);
                    helper = new DatabaseHelper(getActivity(), "NewsDatabase.db", null, 1);
                    SQLiteDatabase db = helper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("select * from "+label+" where title=?", new String[]{last_title});
                    if(cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        id--;
                        Cursor newcur = db.rawQuery("select * from "+label+" where id=?", new String[]{Integer.toString(id)});
                        if(newcur.moveToNext()) {
                            String title = newcur.getString(cursor.getColumnIndex("title"));
                            titledata.add(title);
                        }
                    }
                    mHandler.sendEmptyMessage(LOAD_COMPLETE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    public void initString(String s){
//        label = s;
//    }
}

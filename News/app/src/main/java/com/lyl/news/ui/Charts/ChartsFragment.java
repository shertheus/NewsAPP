package com.lyl.news.ui.Charts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.lyl.news.R;

import java.util.ArrayList;
import java.util.List;

public class ChartsFragment extends Fragment {
    private TableView mTableView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_charts, container, false);
        mTableView = root.findViewById(R.id.tableView);
        float [] f = {0f, 5f, 10f, 15f, 20f, 25f, 30f};
        float [] f2 = {0f, 10f, 30f, 54f, 30f, 100f, 10f};
        List<Float> f_list = new ArrayList<>();
        for (int i =0; i < 7; i++){
            f_list.add(f2[i]);
        }
        mTableView.setupCoordinator("日", "人", f);
        // 添加曲线, 确保纵坐标的数值位数相等
        mTableView.addWave(ContextCompat.getColor(getContext(), android.R.color.holo_orange_light), true, f_list);
//        mTableView.addWave(ContextCompat.getColor(getContext(), android.R.color.holo_green_light), false,
//                0f, 30f, 20f, 20f, 46f, 25f, 5f);
//        mTableView.addWave(ContextCompat.getColor(getContext(), android.R.color.holo_purple), false,
//                0f, 30f, 20f, 50f);
//        mTableView.addWave(Color.parseColor("#8596dee9"), true,
//                0f, 15f, 10f, 10f, 40f, 20f, 5000f);R
        return root;
    }
}
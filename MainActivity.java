package com.lepus.myapplication;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG = "xyz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwipeRefreshLayout srl = findViewById(R.id.srl);
        GridView gv = findViewById(R.id.gv);
        final List data = new ArrayList<>();
        final BaseAdapter adapter = new SimpleAdapter(this, data, R.layout.item1, new String[]{"id", "val"}, new int[]{R.id.item_index, R.id.item_random});
        srl.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                data.clear();
                data.addAll(getData());
                gv.setAdapter(adapter);
                srl.setRefreshing(false);
            }, 300);
        });
        gv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int startY;
            private int endY;
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount >= totalItemCount){
                    if(totalItemCount < 100){
                        data.addAll(getData());
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(MainActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    protected List<Map<String, Object>> getData(){
        List<Map<String, Object>> list = new ArrayList<>();
        for(int i=0, len=18; i<len; i++){
            Map<String, Object> map = new HashMap<>();
            map.put("id", i);
            map.put("val", Math.random());
            list.add(map);
        }
        return list;
    }

}

package org.tony.svn;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
        gv.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e(TAG, "firstVisibleItem: " + firstVisibleItem);
                Log.e(TAG, "visibleItemCount: " + visibleItemCount);
                Log.e(TAG, "totalItemCount: " + totalItemCount);
            }
        });
        List data = new ArrayList<>();
        srl.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                data.addAll(getData());
                gv.setAdapter(new SimpleAdapter(this, data, R.layout.item1, new String[]{"id", "val"}, new int[]{R.id.item_index, R.id.item_random}));
                gv.setOnItemClickListener((parent, view, position, id) -> {
                    LinearLayout ll = (LinearLayout) view;
                    TextView tv = (TextView) ll.getChildAt(1);
                    Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();
                });
                srl.setRefreshing(false);
            }, 300);
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

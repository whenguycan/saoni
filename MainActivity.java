package com.lepus.myapplication;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG = "xyz";

    private String dbName = "test.db";
    private int dbVersion = 1;
    private SQLiteOpenHelper sol = new SQLiteOpenHelper(this, dbName, null, dbVersion) {
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("create table test (_id integer primary key autoincrement,name varchar(64))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            initData(db);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwipeRefreshLayout srl = findViewById(R.id.srl);
        GridView gv = findViewById(R.id.gv);
        final List data = new ArrayList<>();
        final BaseAdapter adapter = new SimpleAdapter(this, data, R.layout.item1, new String[]{"id", "val"}, new int[]{R.id.item_index, R.id.item_random});
        gv.setAdapter(adapter);
        Info info = new Info();
        srl.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                data.clear();
                info.reset();
                List l = getData(info.pageNo, info.pageSize, sol);
                data.addAll(data.size(), l);
                adapter.notifyDataSetChanged();
                srl.setRefreshing(false);
            }, 300);
        });
        gv.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean isTail = false;
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(isTail && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    if(info.pageNo < info.pageCount){
                        info.pageNo++;
                        List l = getData(info.pageNo, info.pageSize, sol);
                        data.addAll(data.size(), l);
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(MainActivity.this, "no more data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isTail = firstVisibleItem + visibleItemCount - totalItemCount >= 0;
            }
        });
    }

    class Info{
        int pageNo = 1;
        int pageSize = 12;
        int pageCount = 3;
        public void reset(){
            this.pageNo = 1;
            this.pageSize = 12;
            this.pageCount = 3;
        }
    }

    protected void initData(SQLiteDatabase db){
        db.execSQL("delete from test");
        for(int i=0, len=25; i<len; i++){
            String sql = "insert into test (_id,name) values (?,?)";
            db.execSQL(sql, new Object[]{i+1, Math.random()});
        }
    }

    protected List<Map<String, Object>> getData(int pageNo, int pageSize, SQLiteOpenHelper sol){
        SQLiteDatabase db = sol.getReadableDatabase();
        String sql = "select _id,name from test limit ?,?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf((pageNo-1)*pageSize), String.valueOf(pageSize)});
        List<Map<String, Object>> list = new ArrayList<>();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(id));
            map.put("val", name);
            list.add(map);
        }
        return list;
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

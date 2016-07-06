package com.jikexueyuan.joke.jokeapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.jikexueyuan.joke.jokeapp.R;
import com.jikexueyuan.joke.jokeapp.activity.ContentActivity;
import com.jikexueyuan.joke.jokeapp.view.RefreshableView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChickenSoupFragment extends LazyFragment {

    private RefreshableView refreshableView;
    private ArrayList<Map<String, Object>> lists = new ArrayList<>();
    private boolean isprepared;
    private View root;

    public ChickenSoupFragment() {
        // Required empty public constructor
    }

    /**
     * 在onCreate函数中更新数据
     *
     * @param savedInstanceState
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (lists.isEmpty()) {
            new Thread() {
                @Override
                public void run() {
                        loadData();
                }
            }.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_blank, container, false);
        View header = root.findViewById(R.id.my_header);
        header.setVisibility(View.INVISIBLE);
        TextView tv_actionBar = (TextView) root.findViewById(R.id.tv_actionBar);
        tv_actionBar.setText(R.string.jitang);
        isprepared = true;
        lazyLoad();
        return root;
    }

    /**
     * 读取网络数据并缓存到本地文件
     */
    private void loadData() {

        StringBuffer stringBuffer = null;
        InputStream inputStream = null;
        InputStreamReader isr = null;
        //读取网络数据库的数据
        try {
            URL url = new URL("http://1.651809535.applinzi.com/Conn1.php");
            URLConnection urlConnection = url.openConnection();
            inputStream = urlConnection.getInputStream();
            isr = new InputStreamReader(inputStream);
            stringBuffer = new StringBuffer();
            char[] chars = new char[1024];
            int count;
            while ((count = isr.read(chars)) != -1) {
                stringBuffer.append(String.valueOf(chars,0,count));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将读取的数据缓存到本地
        File file = null;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        boolean isdelete = false;
        try {
            file = new File(Environment.getExternalStorageDirectory(), "dataJoke.json");
            if (file.exists() && !TextUtils.isEmpty(stringBuffer)) {
                boolean delete = file.delete();
                isdelete = true;
            }
            if (isdelete) {
                boolean newFile = file.createNewFile();
                fos = new FileOutputStream(file);
                osw = new OutputStreamWriter(fos);
                osw.write(String.valueOf(stringBuffer));
                osw.flush();
                fos.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStreamReader br = null;
        try {
            br = new InputStreamReader(new FileInputStream(file));
            StringBuffer stringBuffer1 = new StringBuffer();
            char[] chars = new char[1024];
            int count;
            while ((count = br.read(chars)) != -1) {
                String str = String.valueOf(chars,0,count);
                stringBuffer1.append(str);
            }

            lists.clear();
            JSONArray array = new JSONArray(stringBuffer1.toString());
            for (int i = array.length() - 1; i >= 0; --i) {
                JSONObject object = array.getJSONObject(i);
                String post_date = object.getString("post_date");
                String post_content = object.getString("post_content");
                String post_title = object.getString("post_title");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("post_date", post_date);
                map.put("post_content", post_content);
                map.put("post_title", post_title);
                lists.add(map);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 在fragment可见时候进行刷新任务
     */
    @Override
    protected void lazyLoad() {
        if (!isprepared || !isVisible) {
            return;
        }
        refreshableView = (RefreshableView) root.findViewById(R.id.refreshable_view);
        ListView listView = (ListView) root.findViewById(R.id.list_view);
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                refreshableView.finishRefreshing();
            }
        }, 1);


        SimpleAdapter adapter = new SimpleAdapter(getActivity(), lists, R.layout.content_cell, new String[]{"post_title", "post_date"}, new int[]{R.id.tv_title, R.id.tv_date});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ContentActivity.class);
                Bundle bundle = new Bundle();
                Map<String, Object> map = lists.get(i);
                bundle.putString("post_title", (String) map.get("post_title"));
                bundle.putString("post_content", (String) map.get("post_content"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}

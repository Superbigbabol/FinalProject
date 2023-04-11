package algonquin.cst2335.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.gyf.immersionbar.ImmersionBar;
import algonquin.cst2335.finalproject.R;


import java.util.List;

import algonquin.cst2335.finalproject.adapter.NewsListAdapter;
import algonquin.cst2335.finalproject.base.BaseActivity;
import algonquin.cst2335.finalproject.bean.NewsBean;
import algonquin.cst2335.finalproject.util.AppUtils;
import algonquin.cst2335.finalproject.util.JsonUtil;
import algonquin.cst2335.finalproject.util.PrefUtils;

public class NewYorkTimeActivity extends BaseActivity implements View.OnClickListener {
    private NewsListAdapter newsListAdapter;
    private TextView tvSearch, tv_title;
    private RecyclerView recyclerView;
    private EditText etTitle;
    private RelativeLayout rl_title;
    private ImageView iv_common_back, cbCollectionBook, ivHelp;


    @Override
    protected int initContentView() {
        return R.layout.activity_newyorktime;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        tvSearch = findViewById(R.id.tvSearch);
        etTitle = findViewById(R.id.etTitle);
        rl_title = findViewById(R.id.rl_title);
        tv_title = findViewById(R.id.tv_title);
        ivHelp = findViewById(R.id.ivHelp);
        iv_common_back = findViewById(R.id.iv_common_back);
        cbCollectionBook = findViewById(R.id.cbCollectionBook);
        iv_common_back.setVisibility(View.INVISIBLE);
        tv_title.setText(getResources().getText(R.string.title));
        ImmersionBar.with(this).titleBar(rl_title).statusBarDarkFont(false).init();
        tvSearch.setOnClickListener(this);
        ivHelp.setOnClickListener(this);
        cbCollectionBook.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(NewYorkTimeActivity.this));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        newsListAdapter = new NewsListAdapter(R.layout.item_news_layout);
        newsListAdapter.setEmptyView(LayoutInflater.from(NewYorkTimeActivity.this).inflate(R.layout.empty_layout, null));
        recyclerView.setAdapter(newsListAdapter);

    }

    @Override
    protected void initListener() {
        newsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsBean.ResponseBean.DocsBean docsBean = (NewsBean.ResponseBean.DocsBean) adapter.getItem(position);
                if (docsBean != null) {
                    String web_url = docsBean.getWeb_url();
                    if (!TextUtils.isEmpty(web_url)) {
                        startActivity(new Intent(NewYorkTimeActivity.this, NewsDetailsActivity.class)
                                .putExtra("news", docsBean));
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        String search_content = PrefUtils.getString(NewYorkTimeActivity.this, "search_content", "");
        if (!TextUtils.isEmpty(search_content)) {
            etTitle.setText(search_content);
            etTitle.setSelection(search_content.length());
            search(search_content);
        }
    }

    /**
     * 搜索功能
     * @param text
     */
    private void search(String text) {
        showProgressDialog(false, getResources().getString(R.string.load));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + text + "&api-key=np7TSY6SzrM1FAcIclTfn1cbCdpRv7Gi"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //json
                if (!TextUtils.isEmpty(response)) {
                    NewsBean newsBean = JsonUtil.parseJsonToBean(response, NewsBean.class);
                    if (newsBean != null) {
                        if (newsBean.getStatus().equals("OK")) {
                            NewsBean.ResponseBean response1 = newsBean.getResponse();
                            if (response1 != null) {
                                List<NewsBean.ResponseBean.DocsBean> docs = response1.getDocs();
                                if (docs != null && docs.size() > 0) {
                                    newsListAdapter.setNewData(docs);
                                }
                                Snackbar.make(NewYorkTimeActivity.this, view, "" + getResources().getText(R.string.search_ok_first) + docs.size() + getResources().getText(R.string.search_ok_second), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                stopProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialog();
            }
        });
        //将请求添加到请求队列中
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSearch:
                String search = etTitle.getText().toString().trim();
                if (TextUtils.isEmpty(search)) {
                    Toast.makeText(NewYorkTimeActivity.this, getResources().getText(R.string.input_search), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hasChinese(search)) {
                    Toast.makeText(NewYorkTimeActivity.this, getResources().getText(R.string.input_english), Toast.LENGTH_SHORT).show();
                    return;
                }
                PrefUtils.putString(NewYorkTimeActivity.this, "search_content", search);
                search(search);
                break;
            case R.id.cbCollectionBook:
                startActivity(new Intent(NewYorkTimeActivity.this, CollectionActivity.class));
                break;
            case R.id.ivHelp:
                AppUtils.showHelp(NewYorkTimeActivity.this,getResources().getString(R.string.news_use));
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出软件的提示
     */
    private void close() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setTitle(getResources().getString(R.string.dia_title));
        builder.setMessage(getResources().getString(R.string.leave_app));

        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean hasChinese(String s) {
        boolean ifHaveChinese = false;
        Character.UnicodeBlock ub;
        for (char c : s.toCharArray()) {
            ub = Character.UnicodeBlock.of(c);
            if ((ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)) {
                ifHaveChinese = true;
                break;
            }
            ifHaveChinese = false;
        }
        return ifHaveChinese;
    }
}
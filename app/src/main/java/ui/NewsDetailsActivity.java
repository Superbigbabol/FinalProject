package ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.gyf.immersionbar.ImmersionBar;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.base.BaseActivity;
import algonquin.cst2335.finalproject.bean.NewsBean;
import db.NewsDao;
import utils.AppUtils;


/**
 * 新闻详情页面
 * This is news details page.
 */
public class NewsDetailsActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout rlTitle;
    WebView web_view;
    private TextView tv_title;
    private ImageView cbCollectionBook,ivHelp;
    private CheckBox cbCollection;
    private ProgressBar progressBar;
    private LinearLayout ll_common_back;
    private NewsDao db;
    private NewsBean.ResponseBean.DocsBean news;

    @Override
    protected int initContentView() {
        return algonquin.cst2335.finalproject.R.layout.activity_content;
    }

    @Override
    protected void initView() {
        rlTitle = findViewById(R.id.rl_title);
        tv_title = findViewById(R.id.tv_title);
        progressBar = findViewById(R.id.progressBar1);
        cbCollectionBook = findViewById(R.id.cbCollectionBook);
        web_view = findViewById(R.id.web_view);
        ivHelp = findViewById(R.id.ivHelp);
        cbCollection = findViewById(R.id.cbCollection);
        ll_common_back = findViewById(R.id.ll_common_back);
        cbCollectionBook.setVisibility(View.GONE);
        cbCollection.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.details));
        ll_common_back.setOnClickListener(this);
        ivHelp.setOnClickListener(this);
        cbCollection.setOnClickListener(this);
        ImmersionBar.with(this).titleBar(rlTitle).statusBarDarkFont(true).init();

        db = new NewsDao(NewsDetailsActivity.this);

    }

    @Override
    protected void initListener() {

    }
    /**
     * used this method which will only return to one lever up
     * 返回上一个页面， 如果开了5个页面，点击返回到第4个页面，以此类推,直到没有网页可以关闭。
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBack() {
        if (web_view.canGoBack()) {
            web_view.goBack();
        } else {
            finish();
        }
    }
    /**
     * Add a click event to the control
     * 给控件添加点击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_common_back:
                goBack();
                break;
            case R.id.cbCollection:
                boolean notes = db.isInShouCang(news.get_id());
                if (!notes) {
                    db.add(news.get_id(), news.getNews_desk(), news.getAbstractX(), news.getWeb_url(), news.getDocument_type(), news.getSource(), news.getPub_date());
                    Snackbar.make(cbCollection, getResources().getString(R.string.collection_bottom), Snackbar.LENGTH_SHORT).show();
//                    Snackbar.make(cbCollection,"aaaaa", BaseTransientBottomBar.LENGTH_LONG).show();
                    } else {
                    db.delete(news.get_id());
                    Snackbar.make(cbCollection, getResources().getString(R.string.cancel_collection_bottom), Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.ivHelp:
                AppUtils.showHelp(NewsDetailsActivity.this,getResources().getString(R.string.content_use));
                break;
        }
    }



    @Override
    protected void initData() {
        Intent intent = getIntent();//获取当前活动的 Intent
        if (intent != null) {
            //关键！！！
            news = (NewsBean.ResponseBean.DocsBean) intent.getSerializableExtra("news");
            if (news != null) {
                String web_url = news.getWeb_url();
                if (!TextUtils.isEmpty(web_url)) {
                    open(web_url);
                }
                boolean notes = db.isInShouCang(news.get_id());
                if (notes) {
                    cbCollection.setChecked(true);
                } else {
                    cbCollection.setChecked(false);
                }
            }
        }


    }

    /**
     * 显示web_url链接
     * @param path
     */
    private void open(String path) {
        web_view.loadUrl(path);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        web_view.getSettings().setDomStorageEnabled(true);
        WebSettings settings = web_view.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);//设置webview支持javascript脚本
        web_view.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }
        });

    }


}

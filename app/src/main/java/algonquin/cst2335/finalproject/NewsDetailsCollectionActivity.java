package algonquin.cst2335.finalproject;

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
import algonquin.cst2335.finalproject.bean.CollectionBean;
import algonquin.cst2335.finalproject.data.NewsDao;
import algonquin.cst2335.finalproject.util.AppUtils;


public class NewsDetailsCollectionActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout rlTitle;
    WebView web_view;
    private TextView tv_title;
    private ImageView cbCollectionBook,ivHelp;
    private CheckBox cbCollection;
    private ProgressBar progressBar;
    private LinearLayout ll_common_back;
    private NewsDao db;
    private CollectionBean news;

    @Override
    protected int initContentView() {
        return R.layout.activity_content;
    }

    @Override
    protected void initView() {
        rlTitle = findViewById(R.id.rl_title);
        tv_title = findViewById(R.id.tv_title);
        progressBar = findViewById(R.id.progressBar1);
        cbCollectionBook = findViewById(R.id.cbCollectionBook);
        web_view = findViewById(R.id.web_view);
        cbCollection = findViewById(R.id.cbCollection);
        ll_common_back = findViewById(R.id.ll_common_back);
        ivHelp = findViewById(R.id.ivHelp);
        cbCollectionBook.setVisibility(View.GONE);
        cbCollection.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.details));
        ll_common_back.setOnClickListener(this);
        ivHelp.setOnClickListener(this);
        cbCollection.setOnClickListener(this);
        ImmersionBar.with(this).titleBar(rlTitle).statusBarDarkFont(true).init();

        db = new NewsDao(NewsDetailsCollectionActivity.this);

    }

    @Override
    protected void initListener() {

    }

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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_common_back:
                goBack();
                break;
            case R.id.cbCollection:
                boolean notes = db.isInShouCang(news.getId());
                if (!notes) {
                    db.add(news.getId(), news.getTitle(), news.getContent(), news.getWeb_url(), news.getType(), news.getCome(), news.getTime());
                    Snackbar.make(cbCollection, getResources().getString(R.string.collection_bottom), Snackbar.LENGTH_SHORT).show();
                } else {
                    db.delete(news.getId());
                    Snackbar.make(cbCollection, getResources().getString(R.string.cancel_collection_bottom), Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.ivHelp:
                AppUtils.showHelp(NewsDetailsCollectionActivity.this,getResources().getString(R.string.content_use));
                break;
        }
    }



    @Override
    protected void initData() {
        Intent intent = getIntent();//获取当前活动的 Intent
        if (intent != null) {
            //关键！！！
            news = (CollectionBean) intent.getSerializableExtra("news");
            if (news != null) {
                String web_url = news.getWeb_url();
                if (!TextUtils.isEmpty(web_url)) {
                    open(web_url);
                }
                boolean notes = db.isInShouCang(news.getId());
                if (notes) {
                    cbCollection.setChecked(true);
                } else {
                    cbCollection.setChecked(false);
                }
            }
        }


    }

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
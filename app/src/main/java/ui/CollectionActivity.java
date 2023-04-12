package ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.gyf.immersionbar.ImmersionBar;

import java.util.List;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.adapter.CollectionAdapter;
import algonquin.cst2335.finalproject.base.BaseActivity;
import algonquin.cst2335.finalproject.bean.CollectionBean;
import db.NewsDao;
import utils.AppUtils;


/**
 * 收藏的页面 favorite pages
 */
public class CollectionActivity extends BaseActivity implements View.OnClickListener {
    private CollectionAdapter collectionAdapter;
    private TextView tv_title;
    private RecyclerView recyclerView;
    private RelativeLayout rl_title;
    private ImageView cbCollectionBook,ivHelp;
    private LinearLayout ll_common_back;
    private NewsDao db;

    @Override
    protected int initContentView() {
        return R.layout.activity_collection;
    }

    /**
     * 控件的初始化  control initial
     */
    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        rl_title = findViewById(R.id.rl_title);
        tv_title = findViewById(R.id.tv_title);
        cbCollectionBook = findViewById(R.id.cbCollectionBook);
        ll_common_back = findViewById(R.id.ll_common_back);
        ivHelp = findViewById(R.id.ivHelp);
        cbCollectionBook.setVisibility(View.GONE);
        tv_title.setText(getResources().getString(R.string.collection));
        ImmersionBar.with(this).titleBar(rl_title).statusBarDarkFont(false).init();
        ll_common_back.setOnClickListener(this);
        cbCollectionBook.setOnClickListener(this);
        ivHelp.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(CollectionActivity.this,LinearLayoutManager.HORIZONTAL,false));

        collectionAdapter = new CollectionAdapter(R.layout.item_news_layout);
        collectionAdapter.setEmptyView(LayoutInflater.from(CollectionActivity.this).inflate(R.layout.empty_layout, null));
        recyclerView.setAdapter(collectionAdapter);

    }

    /**
     * 监听方法 monitoring method
     */
    @Override
    protected void initListener() {
        collectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CollectionBean docsBean = (CollectionBean) adapter.getItem(position);
                if (docsBean != null) {
                    String web_url = docsBean.getWeb_url();
                    if (!TextUtils.isEmpty(web_url)) {
                        startActivity(new Intent(CollectionActivity.this, NewsDetailsCollectionActivity.class)
                                .putExtra("news", docsBean));
                    }
                }
            }
        });
        collectionAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                CollectionBean docsBean = (CollectionBean) adapter.getItem(position);
                if (docsBean != null) {
                    delete(docsBean.getId(), position);
                }
                return false;
            }
        });
    }

    /**
     * 删除收藏/ delete connection
     * @param id
     * @param position
     */
    private void delete(String id, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setTitle(getResources().getString(R.string.dia_title));
        builder.setMessage(getResources().getString(R.string.cancel_collection));

        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.delete(id);
                collectionAdapter.remove(position);
                Snackbar.make(CollectionActivity.this,view, getResources().getString(R.string.cancel_collection_bottom), Snackbar.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        db = new NewsDao(CollectionActivity.this);
        List<CollectionBean> collectionList = db.findCollectionList();
        if (collectionList != null && collectionList.size() > 0) {
            collectionAdapter.setNewData(collectionList);
            Snackbar.make(CollectionActivity.this, view, getResources().getString(R.string.have) + collectionList.size() + getResources().getString(R.string.num_coll), Snackbar.LENGTH_SHORT).show();

        } else {
            collectionAdapter.setNewData(collectionList);
            Toast.makeText(CollectionActivity.this, getResources().getString(R.string.nodata), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_common_back:
                finish();
                break;
            case R.id.ivHelp:
                AppUtils.showHelp(CollectionActivity.this,getResources().getString(R.string.collection_use));
                break;
        }
    }
}
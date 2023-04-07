package algonquin.cst2335.finalproject.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.homework.news_master.R;


import algonquin.cst2335.finalproject.bean.NewsBean;

/**
 * 新闻列表的适配器
 */
public class NewsListAdapter extends BaseQuickAdapter<NewsBean.ResponseBean.DocsBean, BaseViewHolder>{
    public NewsListAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 数据的显示和加载
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, NewsBean.ResponseBean.DocsBean item) {
        helper.setText(R.id.tvName,item.getNews_desk());
        helper.setText(R.id.tvContent,item.getAbstractX());
        helper.setText(R.id.tvCome,"come:"+item.getSource());
        helper.setText(R.id.tvTime,"date:"+item.getPub_date());
        helper.setText(R.id.tvType,"【"+item.getDocument_type()+"】");
    }
}

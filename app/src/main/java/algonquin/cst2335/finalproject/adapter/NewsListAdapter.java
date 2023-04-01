package algonquin.cst2335.finalproject.adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.bean.NewsBean;

import java.util.Base64;

import algonquin.cst2335.finalproject.bean.NewsBean;

public class NewsListAdapter extends BaseQuickAdapter<NewsBean.ResponseBean.DocsBean, BaseViewHolder>{
    public NewsListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsBean.ResponseBean.DocsBean item) {
        helper.setText(R.id.tvName,item.getNews_desk());
        helper.setText(R.id.tvContent,item.getAbstractX());
        helper.setText(R.id.tvCome,"come:"+item.getSource());
        helper.setText(R.id.tvTime,"date:"+item.getPub_date());
        helper.setText(R.id.tvType,"【"+item.getDocument_type()+"】");
    }
}

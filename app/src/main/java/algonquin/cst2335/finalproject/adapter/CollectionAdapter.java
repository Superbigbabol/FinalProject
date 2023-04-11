package algonquin.cst2335.finalproject.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import algonquin.cst2335.finalproject.R;


import algonquin.cst2335.finalproject.bean.CollectionBean;

/**
 * 收藏列表的适配器
 */
public class CollectionAdapter extends BaseQuickAdapter<CollectionBean, BaseViewHolder>{
    public CollectionAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 数据的显示和加载
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, CollectionBean item) {
        helper.setText(R.id.tvName,item.getTitle());
        helper.setText(R.id.tvContent,item.getContent());
        helper.setText(R.id.tvCome,"come:"+item.getCome());
        helper.setText(R.id.tvTime,"date:"+item.getTime());
        helper.setText(R.id.tvType,"【"+item.getType()+"】");
    }
}
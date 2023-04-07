package db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.bean.CollectionBean;

/**
 * 数据库的工具类/ The utility class of the database
 */
public class NewsDao {

    private final MyDataBaseOpenHelper helper;

    public NewsDao(Context context) {
        helper = new MyDataBaseOpenHelper(context);

    }

    /**
     * 向数据库添加数据 add data to database.
     * @param id
     * @param title
     * @param content
     * @param web_url
     * @param type
     * @param come
     * @param time
     */
    public void add(String id, String title, String content, String web_url,String type,String come,String time) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into news (id,title,content,web_url,type,come,time) values (?,?,?,?,?,?,?)",
                new Object[]{id, title, content, web_url,type,come,time});
        db.close();//释放资源/ releases resources
    }

    /**
     * 数据库删除某一条数据/ delete data
     * @param id
     */
    public void delete(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from news where id=?", new Object[]{id});
        db.close();//释放资源 release resources;
    }

    /**
     * 获取数据库所有数据/ received all tata from database.
     * @return
     */
    public List<CollectionBean> findCollectionList() {
        List<CollectionBean> result = new ArrayList<CollectionBean>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from news", null);
        CollectionBean que;
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String web_url = cursor.getString(cursor.getColumnIndex("web_url"));
            @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex("type"));
            @SuppressLint("Range") String come = cursor.getString(cursor.getColumnIndex("come"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
            que = new CollectionBean();
            que.setId(id);
            que.setContent(content);
            que.setTitle(title);
            que.setWeb_url(web_url);
            que.setType(type);
            que.setCome(come);
            que.setTime(time);
            result.add(que);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 判断某一条数据是否存在/ determine whether a piece of data exists
     * @param id
     * @return
     */
    @SuppressLint("Range")
    public boolean isInShouCang(String id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String cuoti_shoucang = "news";
        Cursor cursor = db.rawQuery("select * from news where id=?", new String[]{id});
        if (cursor.moveToNext()) {
            cuoti_shoucang = cursor.getString(cursor.getColumnIndex("id"));
        }
        if (cuoti_shoucang.equals(id)) {
            return true;
        }
            return false;

    }

}

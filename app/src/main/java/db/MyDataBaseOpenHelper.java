package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库的基类 the basic type of database
 */
public class MyDataBaseOpenHelper extends SQLiteOpenHelper{

    public MyDataBaseOpenHelper(Context context) {
        super(context, "news.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table news (_id integer primary key autoincrement, id varchar(50), title varchar(100)" +
                ",content varchar(100),web_url varchar(1000),type varchar(1000),come varchar(1000),time varchar(1000))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

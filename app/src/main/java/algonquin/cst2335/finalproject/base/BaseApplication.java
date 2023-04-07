package algonquin.cst2335.finalproject.base;

import android.app.Application;


public class BaseApplication extends Application {
    private static BaseApplication app;

    public static BaseApplication getInstance() {
        return app;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

    }
}

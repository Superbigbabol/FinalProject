package algonquin.cst2335.finalproject.util;


import com.google.gson.Gson;

public class JsonUtil {
    /**
     * json转实体类的方法
     * @param json
     * @param cls
     * @return
     * @param <T>
     */
    public static <T> T parseJsonToBean(String json, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
        }
        return t;
    }

}
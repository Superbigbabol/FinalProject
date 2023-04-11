package algonquin.cst2335.finalproject.http;

/**
 * response basic situation
 * @param <T>
 */
public class BaseDataResponse<T> extends BaseResponse{

    private T data;             // result of data

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

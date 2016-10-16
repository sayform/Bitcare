package bitcare.com.br.bitcare.models;

/**
 * Created by Rafael on 16/10/2016.
 */
public class CloudantViewRowsDTO<T> {

    private Object key;
    private T value;

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

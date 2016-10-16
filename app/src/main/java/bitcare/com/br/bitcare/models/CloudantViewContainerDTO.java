package bitcare.com.br.bitcare.models;

import java.util.List;

/**
 * Created by Rafael on 16/10/2016.
 */

public class CloudantViewContainerDTO<T> {

    private List<CloudantViewRowsDTO<T>> rows;

    public List<CloudantViewRowsDTO<T>> getRows() {
        return rows;
    }

    public void setRows(List<CloudantViewRowsDTO<T>> rows) {
        this.rows = rows;
    }

}

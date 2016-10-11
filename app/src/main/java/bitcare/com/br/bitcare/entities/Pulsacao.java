package bitcare.com.br.bitcare.entities;

import com.orm.SugarRecord;

/**
 * Created by Rafael on 09/10/2016.
 */

public class Pulsacao extends SugarRecord {
    public String login;
    public Long valor;
    public String hora;

    public Pulsacao() {
    }

    public Pulsacao(String login, Long valor, String hora) {
        this.login = login;
        this.valor = valor;
        this.hora = hora;
    }
}

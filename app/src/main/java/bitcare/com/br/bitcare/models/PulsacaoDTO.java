package bitcare.com.br.bitcare.models;

/**
 * Created by Rafael on 09/10/2016.
 */

public class PulsacaoDTO {
    private final String type = "pulsacao";
    private String login;
    private Long valor;
    private String hora;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long bpm) {
        this.valor = bpm;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getType() {
        return type;
    }
}

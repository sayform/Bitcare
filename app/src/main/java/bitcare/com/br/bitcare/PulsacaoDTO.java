package bitcare.com.br.bitcare;

/**
 * Created by Rafael on 09/10/2016.
 */

public class PulsacaoDTO {
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
}

package bitcare.com.br.bitcare.entities;

/**
 * Created by FBF0113 on 14/10/2016.
 */

public class MediaEstatistica {

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

    @Override
    public String toString() {
        String h = hora.substring( hora.length() - 8, hora.length() - 6   );
        int horaFinal = Integer.parseInt(h) + 1;

        return "Média BPM: " + valor + " | Intervalo: " +  Integer.parseInt(h)+"h - " + horaFinal+"h" ;
    }
}

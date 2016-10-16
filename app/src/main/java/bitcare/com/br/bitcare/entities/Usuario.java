package bitcare.com.br.bitcare.entities;

/**
 * Created by FBF0113 on 13/10/2016.
 */

public class Usuario {

    private String nome;
    private long idade;
    private String dddCelular;
    private String numCelular;
    private String dddTelFixo;
    private String numTelFixo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getIdade() {
        return idade;
    }

    public void setIdade(long idade) {
        this.idade = idade;
    }

    public String getDddCelular() {
        return dddCelular;
    }

    public void setDddCelular(String dddCelular) {
        this.dddCelular = dddCelular;
    }

    public String getNumCelular() {
        return numCelular;
    }

    public void setNumCelular(String numCelular) {
        this.numCelular = numCelular;
    }

    public String getDddTelFixo() {
        return dddTelFixo;
    }

    public void setDddTelFixo(String dddTelFixo) {
        this.dddTelFixo = dddTelFixo;
    }

    public String getNumTelFixo() {
        return numTelFixo;
    }

    public void setNumTelFixo(String numTelFixo) {
        this.numTelFixo = numTelFixo;
    }

}

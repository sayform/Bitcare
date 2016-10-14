package bitcare.com.br.bitcare.interfaces;

import java.util.List;

import bitcare.com.br.bitcare.entities.Usuario;
import bitcare.com.br.bitcare.models.PulsacaoDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by FBF0113 on 13/10/2016.
 */

public interface EstatisticaEndpointService {

    @GET("/pulsacoes?")
    Call<List<PulsacaoDTO>> buscarPulsacoes(@Query("login") String login, @Query("intervalo") int intervalo);

    @GET("/usuario/buscar?")
    Call<Usuario> buscarDadosUsuario(@Query("login") String login);

}

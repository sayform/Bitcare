package bitcare.com.br.bitcare.interfaces;

import java.util.List;

import bitcare.com.br.bitcare.entities.MediaEstatistica;
import bitcare.com.br.bitcare.entities.Usuario;
import bitcare.com.br.bitcare.models.CloudantViewContainerDTO;
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

    @GET("/bitcare_db/_design/pulsacoes/_view/media-por-data?")
    Call<CloudantViewContainerDTO<Long>> buscarPulsacoes(@Query("startkey") String loginDataStartKey,
                                                         @Query("endkey") String loginDataEndKey);

    @GET("/bitcare_db/_design/usuarios/_view/all?")
    Call<CloudantViewContainerDTO<Usuario>> buscarDadosUsuario(@Query("key") String loginKey);

}

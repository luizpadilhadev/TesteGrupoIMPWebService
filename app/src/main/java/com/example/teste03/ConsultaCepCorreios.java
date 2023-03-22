package com.example.teste03;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class ConsultaCepCorreios {

    private static final String TAG = "CorreiosAPI";
    private static final String NAMESPACE = "http://cliente.bean.master.sigep.bsb.correios.com.br/";
    private static final String URL = "https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente";
    private static final String METHOD_NAME = "consultaCEP";

    public static Endereco buscarEndereco(String cep) {
        return null;
    }

    public interface EnderecoCallback {
        void onEnderecoRetornado(Endereco endereco);
        void onErro(String mensagem);
    }

    public static void buscarEndereco(String cep, EnderecoCallback callback) {
        new AsyncTask<String, Void, Endereco>() {

            @Override
            protected Endereco doInBackground(String... params) {
                String cep = params[0];

                try {
                    // Cria o objeto SOAP para fazer a requisição
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                    request.addProperty("cep", cep);

                    // Configura o envelope SOAP e a transport layer HTTP
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE transport = new HttpTransportSE(URL);

                    // Faz a chamada ao método remoto
                    transport.call(NAMESPACE + METHOD_NAME, envelope);

                    // Extrai o objeto de resposta SOAP e o transforma em um objeto Endereco
                    SoapObject response = (SoapObject) envelope.getResponse();
                    Endereco endereco = new Endereco();
                    endereco.setCep(response.getPropertyAsString("cep"));
                    endereco.setLogradouro(response.getPropertyAsString("end"));
                    endereco.setComplemento(response.getPropertyAsString("complemento"));
                    endereco.setBairro(response.getPropertyAsString("bairro"));
                    endereco.setLocalidade(response.getPropertyAsString("cidade"));
                    endereco.setUf(response.getPropertyAsString("uf"));

                    return endereco;

                } catch (Exception e) {
                    Log.e(TAG, "Erro ao buscar endereço: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Endereco endereco) {
                if (endereco != null) {
                    callback.onEnderecoRetornado(endereco);
                } else {
                    callback.onErro("Não foi possível encontrar o endereço correspondente ao CEP informado.");
                }
            }
        }.execute(cep);
    }
}
package com.example.teste03;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCep;
    private Button buttonBuscar;
    private TextView textViewEndereco;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCep = findViewById(R.id.editTextCep);
        buttonBuscar = findViewById(R.id.buttonBuscar);
        textViewEndereco = findViewById(R.id.textViewEndereco);

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cep = editTextCep.getText().toString().trim();

                if (!cep.isEmpty()) {
                    new ConsultaCepAsyncTask().execute(cep);
                } else {
                    textViewEndereco.setText("Digite um CEP válido!");
                }
            }
        });
    }

    private class ConsultaCepAsyncTask extends AsyncTask<String, Void, Endereco> {

        @Override
        protected Endereco doInBackground(String... params) {
            String cep = params[0];
            Endereco endereco = null;
            try {
                endereco = ConsultaCepCorreios.buscarEndereco(cep);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return endereco;
        }

        @Override
        protected void onPostExecute(Endereco endereco) {
            if (endereco != null) {
                String enderecoFormatado = String.format("%s, %s - %s, %s - %s",
                        endereco.getLogradouro(),
                        endereco.getComplemento(),
                        endereco.getBairro(),
                        endereco.getCidade(),
                        endereco.getEstado());
                textViewEndereco.setText(enderecoFormatado);
            } else {
                textViewEndereco.setText("CEP não encontrado!");
            }
        }
    }
}
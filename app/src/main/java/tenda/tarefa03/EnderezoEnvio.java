package tenda.tarefa03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tenda.tarefa_02.R;

public class EnderezoEnvio extends AppCompatActivity {


    private void xestionarEventos(){

        // Botón final
        Button btnFinal = findViewById(R.id.btFinal);
        btnFinal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String enderezo = ((EditText) findViewById(R.id.etEnderezo)).getText().toString().trim();
                String cpostal = ((EditText) findViewById(R.id.etCodPostal)).getText().toString().trim();
                String cidade=((EditText) findViewById(R.id.etCidade)).getText().toString().trim();

                // Só permitimos rematar o pedido cando complete todos os datos do enderezo
                if (enderezo.equals("") || cpostal.equals("") || cidade.equals("")) {

                    Toast.makeText(getApplicationContext(), "Hai que completar todos os datos do enderezo.",Toast.LENGTH_LONG).show();

                }else {

                    // Amosar datos do pedido
                    Intent intent1 = getIntent();
                    String textoPedido = "Datos do pedido:"
                            + "\nCategoría: " + intent1.getExtras().getString(FacerPedido.CATEGORIA)
                            + "\nProduto..: " + intent1.getExtras().getString(FacerPedido.PRODUTO)
                            + "\nCantidade: " + intent1.getExtras().getString(FacerPedido.CANTIDADE)
                            + "\n\nEnderezo de envío:"
                            + "\n" + ((EditText) findViewById(R.id.etEnderezo)).getText().toString()
                            + "\n" + ((EditText) findViewById(R.id.etCodPostal)).getText().toString()
                            + " " + ((EditText) findViewById(R.id.etCidade)).getText().toString();

                    Toast.makeText(getApplicationContext(), textoPedido, Toast.LENGTH_LONG).show();

                    // Esperar unos segundos a pechar a activity para permitir ver a mensaxe Toast
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {

                            // Devolvemos datos e pechamos
                            Intent datosVolta = new Intent();
                            datosVolta.putExtra("Destruir", true);
                            setResult(1, datosVolta);    // Resultado para destruir a activity que chamou a esta.

                            finish();

                        }
                    }, 4000);
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enderezo_envio);

        xestionarEventos();

        // Establecer a caixa enderezo como primeiro elemento en recibir o foco
        EditText et = findViewById(R.id.etEnderezo);
        et.requestFocus();
    }
}

package tenda.tarefa_03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tenda.tarefa_02.R;

public class Cliente extends AppCompatActivity {


    private void xestionarEventos(){

        // Botón para facer un novo pedido
        Button btnFacerPedido = findViewById(R.id.btPedido);
        btnFacerPedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    //crear activity e lanzala
                    Intent intent = new Intent();
                    intent.setClassName(getApplicationContext(), "FacerPedido");
                    startActivity(intent);
            }
        });

        // Botón para ver pedidos en trámite
        Button btnPedidos = findViewById(R.id.btPedidosTramite);
        btnPedidos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //crear activity e lanzala
                Intent intent = new Intent();
                intent.setClassName(getApplicationContext(), "VerPedidos");
                startActivity(intent);
            }
        });

        // Botón para ver pedidos en trámite
        Button btnCompras = findViewById(R.id.btComprasRealizadas);
        btnCompras.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //crear activity e lanzala
                Intent intent = new Intent();
                intent.setClassName(getApplicationContext(), "VerCompras");
                startActivity(intent);
            }
        });


        // Botón Sair
        Button btnSair = findViewById(R.id.btSair);
        btnSair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Destruir a activity;
                finish();
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        xestionarEventos();


    }

}

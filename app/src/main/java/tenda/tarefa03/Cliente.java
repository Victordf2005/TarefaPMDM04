package tenda.tarefa03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import BaseDatos.BDTendaVDF;
import BaseDatos.Usuario;

public class Cliente extends AppCompatActivity {

    private BDTendaVDF baseDatos;
    private Usuario cliente;

    private void xestionarEventos(){

        // Botón para facer un novo pedido
        Button btnFacerPedido = findViewById(R.id.btPedido);
        btnFacerPedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                novoPedido();
            }
        });

        // Botón para ver pedidos en trámite
        Button btnPedidos = findViewById(R.id.btPedidosTramite);
        btnPedidos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                verPendentes();
            }
        });

        // Botón para ver pedidos en trámite
        Button btnCompras = findViewById(R.id.btComprasRealizadas);
        btnCompras.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                verRealizados();
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

    private void novoPedido() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("id_cliente", String.valueOf(cliente.getCodigo()));
        intent.putExtra("nome_cliente", cliente.getNome());
        intent.putExtra("apelidos_cliente", cliente.getApelidos());
        intent.setClassName(getApplicationContext(), "tenda.tarefa03.FacerPedido");
        startActivity(intent);
    }

    private void verPendentes() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("id_cliente", String.valueOf(cliente.getCodigo()));
        intent.putExtra("nome_cliente", cliente.getNome());
        intent.putExtra("apelidos_cliente", cliente.getApelidos());
        intent.setClassName(getApplicationContext(), "tenda.tarefa03.VerPedidos");
        startActivity(intent);
    }

    private void verRealizados() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("id_cliente", String.valueOf(cliente.getCodigo()));
        intent.putExtra("nome_cliente", cliente.getNome());
        intent.putExtra("apelidos_cliente", cliente.getApelidos());
        intent.setClassName(getApplicationContext(), "tenda.tarefa03.VerCompras");
        startActivity(intent);
    }

    private void buscarDatosCliente() {
        Intent intent1 = getIntent();
        cliente = baseDatos.getUsuario(intent1.getExtras().getString(MainActivity.USUARIO),null, true);
        TextView lblCliente = findViewById(R.id.tvNomeCliente);
        lblCliente.setText(cliente.getNome() + "\n" + cliente.getApelidos());
    }


    @Override
    public void onStart(){
        super.onStart();

        if (baseDatos == null) {
            // Abrimos a base de datos para escritura
            try {
                baseDatos = BDTendaVDF.getInstance(getApplicationContext());
                baseDatos.abrirBD();

                buscarDatosCliente();

            }
            catch (Exception erro) {
                // Erro tratando de abrir a BD
                Toast.makeText(getApplicationContext(), "Erro tratando de acceder á base de datos: " + erro.toString(), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onStop(){
        super.onStop();

        if (baseDatos != null){
            // Pechamos a base de datos.
            baseDatos.pecharBD();
            baseDatos=null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        Toolbar barra = findViewById(R.id.toolbarCliente);
        setSupportActionBar(barra);

        xestionarEventos();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cliente, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnuFacerPedido:
                novoPedido();
                return true;

            case R.id.mnuVerPendentes:
                verPendentes();
                return true;

            case R.id.mnuRealizados:
                verRealizados();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

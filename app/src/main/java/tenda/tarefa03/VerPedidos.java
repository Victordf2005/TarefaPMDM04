package tenda.tarefa03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import BaseDatos.BDTendaVDF;
import BaseDatos.Pedido;
import adaptadores.RecyclerViewAdapter_Pedidos;


public class VerPedidos extends AppCompatActivity {

    private BDTendaVDF baseDatos;

    private ArrayList<Pedido> pedidos;

    private void inicializarRecycleView(){

        RecyclerViewAdapter_Pedidos recycleAdapter = new RecyclerViewAdapter_Pedidos(pedidos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rvwPedidosCliente);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycleAdapter);
    }

    private void amosarDatosCliente() {
        Intent intent1 = getIntent();
        TextView lblCliente = findViewById(R.id.tvNomeCliente);
        String texto = intent1.getExtras().getString("nome_cliente") +
                "\n" + intent1.getExtras().getString("apelidos_cliente");
        lblCliente.setText(texto);
    }
/*

    @Override
    public void onStart(){
        super.onStart();

        if (baseDatos == null) {
            // Abrimos a base de datos para escritura
            try {
                baseDatos = BDTendaVDF.getInstance(getApplicationContext());
                baseDatos.abrirBD();

                amosarDatosCliente();
                pedidos = baseDatos.getPedidosCliente("P", getIntent().getExtras().getString("id_cliente"));

                inicializarRecycleView();
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
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedidos);

        Toolbar barra = findViewById(R.id.toolbarPed);
        setSupportActionBar(barra);

        if (baseDatos == null) {
            // Abrimos a base de datos para escritura
            try {
                baseDatos = BDTendaVDF.getInstance(getApplicationContext());
                baseDatos.abrirBD();

                amosarDatosCliente();
                pedidos = baseDatos.getPedidosCliente("P", getIntent().getExtras().getString("id_cliente"));

                inicializarRecycleView();
            }
            catch (Exception erro) {
                // Erro tratando de abrir a BD
                Toast.makeText(getApplicationContext(), "Erro tratando de acceder á base de datos: " + erro.toString(), Toast.LENGTH_LONG).show();
                finish();
            }
        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_atras, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnuAtras:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

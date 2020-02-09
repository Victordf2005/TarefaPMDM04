package tenda.tarefa04;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import BaseDatos.BDTendaVDF;
import BaseDatos.Pedido;
import adaptadores.RecyclerViewAdapter_AdminAR;

public class Administrador_VerPedidosAR extends AppCompatActivity {

    private BDTendaVDF baseDatos;

    private ArrayList<Pedido> pedidos;

    private static RecyclerViewAdapter_AdminAR recyclerAdapter;

    private void inicializarRecycleView(){

        recyclerAdapter = new RecyclerViewAdapter_AdminAR(pedidos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rvPedidosAdministradorAR);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public static void notificarItemRemoved(int pos) {
        recyclerAdapter.notifyItemRemoved(pos);
    }

    @Override
    public void onStart(){
        super.onStart();

        if (baseDatos == null) {
            // Abrimos a base de datos para escritura
            try {
                baseDatos = BDTendaVDF.getInstance(getApplicationContext());
                baseDatos.abrirBD();

                pedidos = baseDatos.getPedidosCliente(getIntent().getExtras().getString("Tipo"), "");

                inicializarRecycleView();
            }
            catch (Exception erro) {
                // Erro tratando de abrir a BD
                Toast.makeText(getApplicationContext(), "Erro tratando de acceder á base de datos: " + erro.toString(), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
/*

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
        setContentView(R.layout.activity_administrador__ver_pedidosar);

        Toolbar barra = findViewById(R.id.toolbarAdminPedAR);
        barra.setTitle("Ped. en trámite");
        setSupportActionBar(barra);
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
            case R.id.mnuAtras:finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
package tenda.tarefa03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
        RecyclerView recyclerView = findViewById(R.id.rvwRecycleView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycleAdapter);
    }

    @Override
    public void onStart(){
        super.onStart();

        if (baseDatos == null) {
            // Abrimos a base de datos para escritura
            try {
                baseDatos = BDTendaVDF.getInstance(getApplicationContext());
                baseDatos.abrirBD();

                pedidos = baseDatos.getPedidosCliente("P", Long.parseLong(getIntent().getExtras().getString("id_cliente")));
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
        setContentView(R.layout.activity_ver_pedidos);

        inicializarRecycleView();


    }
}

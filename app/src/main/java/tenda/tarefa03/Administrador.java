package tenda.tarefa03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import BaseDatos.BDTendaVDF;
import BaseDatos.Usuario;


public class Administrador extends AppCompatActivity {

    private BDTendaVDF baseDatos;
    private Usuario usuario;

    private void buscarDatosAdmin() {
        Intent intent1 = getIntent();
        usuario = baseDatos.getUsuario(intent1.getExtras().getString(MainActivity.USUARIO),null, true);
        TextView lblAdmin = findViewById(R.id.tvNomeAdmin);
        lblAdmin.setText(usuario.getNome() + "\n" + usuario.getApelidos() + "\n");
    }


    private void xestionarEventos(){

        // Botón para facer un novo pedido
        Button btTramite = findViewById(R.id.btEnTramite);
        btTramite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                verEnTramite();
            }
        });

        // Botón para ver pedidos en trámite
        Button btAceptados = findViewById(R.id.btAceptados);
        btAceptados.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                verAceptados();
            }
        });

        // Botón para ver pedidos en trámite
        Button btRexeitados = findViewById(R.id.btRexeitados);
        btRexeitados.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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

    private void verEnTramite() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("Tipo", "P");
        intent.setClassName(getApplicationContext(), "tenda.tarefa03.Administrador_VerPedidos");
        startActivity(intent);
    }

    private void verAceptados() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("Tipo", "A");
        intent.setClassName(getApplicationContext(), "tenda.tarefa03.Administrador_VerPedidos");
        startActivity(intent);
    }

    private void verRexeitados() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("Tipo", "R");
        intent.setClassName(getApplicationContext(), "tenda.tarefa03.Administrador_VerPedidos");
        startActivity(intent);
    }


    @Override
    public void onStart(){
        super.onStart();

        if (baseDatos == null) {
            // Abrimos a base de datos para escritura
            try {
                baseDatos = BDTendaVDF.getInstance(getApplicationContext());
                baseDatos.abrirBD();

                buscarDatosAdmin();

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
        setContentView(R.layout.activity_administrador);

        Toolbar barra = findViewById(R.id.toolbar);
        setSupportActionBar(barra);

        xestionarEventos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnuEnTramite:
                verEnTramite();
                return true;

            case R.id.mnuAceptados:
                verAceptados();
                return true;

            case R.id.mnuRexeitados:
                verRexeitados();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

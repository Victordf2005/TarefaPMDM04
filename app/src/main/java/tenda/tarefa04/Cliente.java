package tenda.tarefa04;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import BaseDatos.BDTendaVDF;
import BaseDatos.Usuario;

public class Cliente extends AppCompatActivity {

    private BDTendaVDF baseDatos;
    private Usuario cliente;

    private Boolean permisoGaleria = false;

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


        // Botón para modificar datos de perfil
        Button btnCambiarDatos = findViewById(R.id.btCambiarDatos);
        btnCambiarDatos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cambiarDatosPerfil();
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

    // Método para lanzar a activity de novo pedido
    private void novoPedido() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("id_cliente", String.valueOf(cliente.getCodigo()));
        intent.putExtra("nome_cliente", cliente.getNome());
        intent.putExtra("apelidos_cliente", cliente.getApelidos());
        intent.putExtra("imaxePerfil", cliente.getImaxePerfil());
        intent.setClassName(getApplicationContext(), "tenda.tarefa04.FacerPedido");
        startActivity(intent);
    }

    // Método para lanzar a activity de ver pedidos pendentes
    private void verPendentes() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("id_cliente", String.valueOf(cliente.getCodigo()));
        intent.putExtra("nome_cliente", cliente.getNome());
        intent.putExtra("apelidos_cliente", cliente.getApelidos());
        intent.putExtra("imaxePerfil", cliente.getImaxePerfil());
        intent.setClassName(getApplicationContext(), "tenda.tarefa04.VerPedidos");
        startActivity(intent);
    }

    // Método para lanzar a activity de ver compras realizadas
    private void verRealizados() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("id_cliente", String.valueOf(cliente.getCodigo()));
        intent.putExtra("nome_cliente", cliente.getNome());
        intent.putExtra("apelidos_cliente", cliente.getApelidos());
        intent.putExtra("imaxePerfil", cliente.getImaxePerfil());
        intent.setClassName(getApplicationContext(), "tenda.tarefa04.VerCompras");
        startActivity(intent);
    }


    // Método para lazar a activity que permite modificar os datos do cliente
    private void cambiarDatosPerfil() {
        // crear activity e lanzala
        Intent intentoPerfil = new Intent();
        intentoPerfil.putExtra("usuario", cliente.getUsuario());
        intentoPerfil.putExtra("nome", cliente.getNome());
        intentoPerfil.putExtra("apelidos", cliente.getApelidos());
        intentoPerfil.putExtra("email", cliente.getEmail());
        intentoPerfil.putExtra("tipo", "cliente");
        intentoPerfil.putExtra("imaxePerfil", cliente.getImaxePerfil());
        intentoPerfil.setClassName(getApplicationContext(), "tenda.tarefa04.CambiarDatosPerfil");
        startActivity(intentoPerfil);
    }

    // Método que busca e amosa os datos do cliente
    private void buscarDatosCliente() {
        Intent intent1 = getIntent();
        cliente = baseDatos.getUsuario(intent1.getExtras().getString(MainActivity.USUARIO),null, true);
        TextView lblCliente = findViewById(R.id.tvNomeCliente);
        lblCliente.setText(cliente.getNome() + "\n" + cliente.getApelidos() + "\n");
        ImageView imaxePerfil = findViewById(R.id.ivCliente);

        // Comprobamos permisos segundo a versión
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!temosPermiso()) {
                ActivityCompat.requestPermissions(Cliente.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                permisoGaleria=true;
            }
        }

        // Se temos acceso á galería, buscamos a imaxe de perfil
        if (permisoGaleria) {

            Bitmap bitmap = BitmapFactory.decodeFile(cliente.getImaxePerfil());

            if (!(bitmap == null)) {
                Bitmap bitmapEscalado = null;

                // Escalamos a imaxe segundo a densidade do dispositivo
                switch (getResources().getDisplayMetrics().densityDpi) {
                    case DisplayMetrics.DENSITY_LOW:
                        bitmapEscalado = Bitmap.createScaledBitmap(bitmap, 320, 320, false);
                        break;
                    case DisplayMetrics.DENSITY_MEDIUM:
                        bitmapEscalado = Bitmap.createScaledBitmap(bitmap, 480, 480, false);
                        break;
                    case DisplayMetrics.DENSITY_HIGH:
                        bitmapEscalado = Bitmap.createScaledBitmap(bitmap, 600, 600, false);
                        break;
                    case DisplayMetrics.DENSITY_XHIGH:
                        bitmapEscalado = Bitmap.createScaledBitmap(bitmap, 960, 960, false);
                        break;
                    default:
                        bitmapEscalado = Bitmap.createScaledBitmap(bitmap, 480, 480, false);
                        break;
                }

                // Asignamos a imaxe escalada
                imaxePerfil.setImageBitmap(bitmapEscalado);
            }
        }
    }


    // Método que devolve se temos permiso de acceso á galería
    private boolean temosPermiso() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (grantResults.length>0) {
            switch (requestCode) {
                case 1: {
                    permisoGaleria = true;
                }
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        if (baseDatos == null) {
            // Abrimos a base de datos para escritura
            try {
                baseDatos = BDTendaVDF.getInstance(getApplicationContext());
                baseDatos.abrirBD();


            }
            catch (Exception erro) {
                // Erro tratando de abrir a BD
                Toast.makeText(getApplicationContext(), "Erro tratando de acceder á base de datos: " + erro.toString(), Toast.LENGTH_LONG).show();
                finish();
            }
        }

        buscarDatosCliente();
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
    }*/


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

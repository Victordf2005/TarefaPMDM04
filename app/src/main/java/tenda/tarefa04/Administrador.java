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


public class Administrador extends AppCompatActivity {

    private BDTendaVDF baseDatos;
    private Usuario usuario;

    private Boolean permisoGaleria = false;

    // Método para obter e amosar os datos do usuario administrador
    private void buscarDatosAdmin() {

        Intent intent1 = getIntent();
        usuario = baseDatos.getUsuario(intent1.getExtras().getString(MainActivity.USUARIO),null, true);

        TextView lblAdmin = findViewById(R.id.tvNomeAdmin);
        lblAdmin.setText(usuario.getNome() + "\n" + usuario.getApelidos() + "\n");
        ImageView imaxePerfil = findViewById(R.id.ivAdmin);

        // comprobamos permisos dependendo da versión
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!temosPermiso()) {
                ActivityCompat.requestPermissions(Administrador.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                permisoGaleria=true;
            }
        }

        // Se podemos acceder á galería, buscamos a foto de perfil
        // a través da ruta almacenada na BD.
        if (permisoGaleria) {

            Bitmap bitmap = BitmapFactory.decodeFile(usuario.getImaxePerfil());
            if (!(bitmap == null)) {

                // Escalamos a imaxe segundo a densidade do dispositivo
                Bitmap bitmapEscalado = null;
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

    // Comprobamos permiso de acdeso á galería.
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

    private void xestionarEventos(){

        // Botón para ver pedidos en trámite
        Button btnEnTramite = findViewById(R.id.btEnTramite);
        btnEnTramite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                verEnTramite();
            }
        });

        // Botón para ver pedidos aceptados
        Button btnAceptados = findViewById(R.id.btAceptados);
        btnAceptados.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                verAceptados();
            }
        });

        // Botón para ver pedidos rexeitados
        Button btnRexeitados = findViewById(R.id.btRexeitados);
        btnRexeitados.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                verRexeitados();
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

        // Botón para modificar datos de perfil
        Button btnCambiarDatos = findViewById(R.id.btCambiarDatos);
        btnCambiarDatos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cambiarDatosPerfil();
            }
        });
    }

    // Método que lanza a activity de ver pedidos en trámite
    private void verEnTramite() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("Tipo", "P");
        intent.setClassName(getApplicationContext(), "tenda.tarefa04.Administrador_VerPedidosAR");
        startActivity(intent);
    }

    // Método que lanza a activity de ver pedidos aceptados
    private void verAceptados() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("Tipo", "A");
        intent.setClassName(getApplicationContext(), "tenda.tarefa04.Administrador_VerPedidosVer");
        startActivity(intent);
    }

    // Método que lanza a activity de ver pedidos rexeitados
    private void verRexeitados() {

        //crear activity e lanzala
        Intent intent = new Intent();
        intent.putExtra("Tipo", "R");
        intent.setClassName(getApplicationContext(), "tenda.tarefa04.Administrador_VerPedidosVer");
        startActivity(intent);
    }

    // Métooo que lanza a activity para poder modificar os datos de perfil
    private void cambiarDatosPerfil() {
        // crear activity e lanzala
        Intent intentoPerfil = new Intent();
        intentoPerfil.putExtra("usuario", usuario.getUsuario());
        intentoPerfil.putExtra("nome", usuario.getNome());
        intentoPerfil.putExtra("apelidos", usuario.getApelidos());
        intentoPerfil.putExtra("email", usuario.getEmail());
        intentoPerfil.putExtra("tipo", "administrador");
        intentoPerfil.putExtra("imaxePerfil", usuario.getImaxePerfil());
        intentoPerfil.setClassName(getApplicationContext(), "tenda.tarefa04.CambiarDatosPerfil");
        startActivity(intentoPerfil);
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

        buscarDatosAdmin();
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
        setContentView(R.layout.activity_administrador);

        Toolbar barra = findViewById(R.id.toolbarAdmin);
        setSupportActionBar(barra);

        xestionarEventos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }


    // Menú para AppBar
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

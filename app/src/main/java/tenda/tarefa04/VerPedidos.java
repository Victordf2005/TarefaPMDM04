package tenda.tarefa04;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import BaseDatos.BDTendaVDF;
import BaseDatos.Pedido;
import adaptadores.RecyclerViewAdapter_Pedidos;


public class VerPedidos extends AppCompatActivity {

    private BDTendaVDF baseDatos;

    private ArrayList<Pedido> pedidos;
    private boolean permisoGaleria = false;

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
        ImageView imaxePerfil = findViewById(R.id.ivCliente);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!temosPermiso()) {
                ActivityCompat.requestPermissions(VerPedidos.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                permisoGaleria=true;
            }
        }
        Log.i("imaxe: ", intent1.getExtras().getString("imaxePerfil"));

        if (permisoGaleria) {
            Log.i("Permiso: ", "Si");
            Bitmap bitmap = BitmapFactory.decodeFile( intent1.getExtras().getString("imaxePerfil"));
            if (!(bitmap == null)) {
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
                imaxePerfil.setImageBitmap(bitmapEscalado);
            }
        }
    }


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

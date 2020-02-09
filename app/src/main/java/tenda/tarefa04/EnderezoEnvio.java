package tenda.tarefa04;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import BaseDatos.BDTendaVDF;


public class EnderezoEnvio extends AppCompatActivity {

    private BDTendaVDF baseDatos;

    private boolean permisoGaleria = false;

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

                } else {

                    AlertDialog.Builder dialogo = new AlertDialog.Builder(EnderezoEnvio.this);

                    dialogo.setTitle("Información sobre o pedido");
                    dialogo.setIcon(android.R.drawable.ic_dialog_alert);

                    Intent intent1 = getIntent();

                    String textoPedido = "Datos do pedido:"
                            + "\nCategoría: " + intent1.getExtras().getString(FacerPedido.CATEGORIA)
                            + "\nProduto..: " +  intent1.getExtras().getString(FacerPedido.PRODUTO)
                            + "\nCantidade: " + intent1.getExtras().getString(FacerPedido.CANTIDADE)
                            + "\n\nEnderezo de envío:"
                            + "\n" + ((EditText) findViewById(R.id.etEnderezo)).getText().toString()
                            + "\n" + ((EditText) findViewById(R.id.etCodPostal)).getText().toString()
                            + " " + ((EditText) findViewById(R.id.etCidade)).getText().toString()
                            + "\n\n Pulse 'OK' para aceptar e gardar o pedido";

                    dialogo.setMessage(textoPedido);

                    dialogo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gravarPedido();
                        }
                    });
                    dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(getApplicationContext(),"Pedido cancelado", Toast.LENGTH_LONG).show();
                        }
                    });
                    dialogo.create();   // Devolve un Dialog, pero non o necesitamos polo de agora.
                    dialogo.show();


                }
            };
        });
    };

    private void gravarPedido() {

        //gravar pedido
        Intent intent1 = getIntent();

        long resultado = baseDatos.gravarPedido("P",
                Integer.parseInt(intent1.getExtras().getString("id_cliente")),
                Integer.parseInt(intent1.getExtras().getString(FacerPedido.IDCATEGORIA)),
                Integer.parseInt(intent1.getExtras().getString(FacerPedido.IDPRODUTO) ),
                Integer.parseInt(intent1.getExtras().getString(FacerPedido.CANTIDADE)),
                ((EditText) findViewById(R.id.etEnderezo)).getText().toString(),
                ((EditText) findViewById(R.id.etCidade)).getText().toString(),
                ((EditText) findViewById(R.id.etCodPostal)).getText().toString());

        if (resultado > 0) {
            Toast.makeText(getApplicationContext(), "Pedido gravado", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Produciuse un erro. O pedido NON FOI GRAVADO", Toast.LENGTH_LONG).show();
        }

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

    private void buscarDatosCliente() {

        Intent intent1 = getIntent();
        TextView lblCliente = findViewById(R.id.tvNomeCliente);
        lblCliente.setText(intent1.getExtras().getString("nome_cliente") + "\n" + intent1.getExtras().get("apelidos_cliente"));
        ImageView imaxePerfil = findViewById(R.id.ivCliente);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!temosPermiso()) {
                ActivityCompat.requestPermissions(EnderezoEnvio.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
                }
                imaxePerfil.setImageBitmap(bitmap);
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
    }
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enderezo_envio);

        Toolbar barra = findViewById(R.id.toolbarEnvio);
        setSupportActionBar(barra);

        xestionarEventos();

        // Establecer a caixa enderezo como primeiro elemento en recibir o foco
        EditText et = findViewById(R.id.etEnderezo);
        et.requestFocus();
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
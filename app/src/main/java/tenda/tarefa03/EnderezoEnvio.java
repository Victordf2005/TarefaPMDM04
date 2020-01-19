package tenda.tarefa03;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import BaseDatos.BDTendaVDF;


public class EnderezoEnvio extends AppCompatActivity {

    private BDTendaVDF baseDatos;

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
                            + "\nProduto..: " + intent1.getExtras().getString(FacerPedido.PRODUTO)
                            + "\nCantidade: " + intent1.getExtras().getString(FacerPedido.CANTIDADE)
                            + "\n\nEnderezo de envío:"
                            + "\n" + ((EditText) findViewById(R.id.etEnderezo)).getText().toString()
                            + "\n" + ((EditText) findViewById(R.id.etCodPostal)).getText().toString()
                            + " " + ((EditText) findViewById(R.id.etCidade)).getText().toString()
                            + "\n\n Pulse 'OK' para aceptar e gardar o pedido";

                    dialogo.setMessage("INFO PEDIDO");

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

        long resultado = baseDatos.gravarPedido("P",Integer.parseInt(intent1.getExtras().getString("id_cliente")),
                Integer.parseInt(intent1.getExtras().getString(FacerPedido.CATEGORIA)),
                Integer.parseInt(intent1.getExtras().getString(FacerPedido.PRODUTO)),
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
package tenda.tarefa03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import BaseDatos.BDTendaVDF;

public class Rexistro extends AppCompatActivity {

    private BDTendaVDF baseDatos;

    private void xestionarEventos(){
        // VDF
        // Botón para iniciar sesión
        Button btRexistrarse = findViewById(R.id.btRexistrar);
        btRexistrarse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rexistrarUsuario();
            }

        });

    }

    private void rexistrarUsuario() {

        EditText etUsuario = findViewById(R.id.etUsuarioRexistro);
        String usuario = etUsuario.getText().toString();

        // Comprobamos se xa existe un usuario rexistrado con ese mesmo usuario
        Boolean xaExiste = baseDatos.existeUsuario(etUsuario.getText().toString());

        if (xaExiste) {

            // Xa hai un rexistro na BD con ese usuario
            Toast.makeText(getApplicationContext(), "Xa hai un rexistro con usuario '" + usuario + "'; non se pode rexistrar este novo usuario.", Toast.LENGTH_LONG).show();

        } else {
            // Podemos rexistrar o novo usuario, recollemos o resto de datos do layout
            Spinner spnTipoUsuario = findViewById(R.id.spnTipo);
            String tipo = spnTipoUsuario.getSelectedItem().toString().substring(0, 1);   // O código de tipo é a primeira letra

            Long resultado = baseDatos.engadirUsuario(((EditText) findViewById(R.id.etNome)).getText().toString(),
                    ((EditText) findViewById(R.id.etApelidos)).getText().toString(),
                    ((EditText) findViewById(R.id.etEmail)).getText().toString(),
                    ((EditText) findViewById(R.id.etUsuarioRexistro)).getText().toString(),
                    ((EditText) findViewById(R.id.etContrasinalRexistro)).getText().toString(),
                    tipo);

            if (resultado > 0) {

                // Datos gravados correctamente
                Toast.makeText(getApplicationContext(), "Datos gravados correctamewnte; código: " + resultado, Toast.LENGTH_LONG).show();

                // Esperar unos segundos a pechar a activity para permitir ver a mensaxe Toast
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 4000);

            } else {

                // Houbo algún erro
                Toast.makeText(getApplicationContext(), "Produciuse algún erro. Os datos NON FORON GRAVADOS.", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void abrirBaseDatos() {

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
    public void onStart(){
        super.onStart();
        abrirBaseDatos();
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
        setContentView(R.layout.activity_rexistro);

        xestionarEventos();
    }
}

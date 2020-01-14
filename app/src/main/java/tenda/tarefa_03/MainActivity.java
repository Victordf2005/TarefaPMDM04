package tenda.tarefa_03;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import BaseDatos.BDTendaVDF;

import BaseDatos.Usuario;
import tenda.tarefa_02.R;

public class MainActivity extends AppCompatActivity {

    private BDTendaVDF baseDatos;
    protected Usuario usuarioLoggeado = null;

    private void xestionarEventos(){

        // Botón para iniciar sesión
        Button btnLogIn = findViewById(R.id.btLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText etUsuario = findViewById(R.id.etUsuario);
                EditText etContrasinal = findViewById(R.id.etContrasinal);

                // Obtemos os datos do usuario loggeado nun obxecto Usuario
                usuarioLoggeado = baseDatos.getUsuario(etUsuario.getText().toString(), etContrasinal.getText().toString());

                // Comprobamos se o usuario existe e o tipo
                if (usuarioLoggeado != null) {

                    switch (usuarioLoggeado.getTipo()) {
                        case "A": {
                            // Usuario de tipo administrador
                            // crear activity e lanzala
                            Intent intent = new Intent();
                            intent.setClassName(getApplicationContext(), "Administrador");
                            startActivityForResult(intent,1);
                            break;
                        }
                        case "C": {
                            // Usuario de tipo cliente
                            // crear activity e lanzala
                            Intent intent = new Intent();
                            intent.setClassName(getApplicationContext(), "Cliente");
                            startActivityForResult(intent,2);
                            break;
                        }
                        default: {
                            // Tipo de usuario descoñecido. Avisar
                            Toast.makeText(getApplicationContext(), "Tipo de usuario descoñecido.", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    //usuario incorrecto. Avisar
                    Toast.makeText(getApplicationContext(), "Usuario e/ou contrasinal incorrectos.", Toast.LENGTH_LONG).show();
                }
            }
        });


        // Botón para rexistrarse
        Button btnRexistrase = findViewById(R.id.btRexistrar);
        btnRexistrase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // crear activity de solicitude de datos para rexistrarse e lanzala
                Intent intent = new Intent();
                intent.setClassName(getApplicationContext(), "Rexistro");
                startActivityForResult(intent, 3);
            }
        });
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
                Toast.makeText(getApplicationContext(), "Erro tratando de acceder á base de datos.", Toast.LENGTH_LONG).show();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Independentemente de donde veña, borramos datos de login
        EditText etUsuario = findViewById(R.id.etUsuario);
        EditText etContrasinal = findViewById(R.id.etContrasinal);

        etUsuario.setText("");
        etContrasinal.setText("");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xestionarEventos();
    }
}

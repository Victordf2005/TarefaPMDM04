package tenda.tarefa03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import BaseDatos.BDTendaVDF;

import BaseDatos.Usuario;

public class MainActivity extends AppCompatActivity {

    public final static String USUARIO = "usuario";

    private BDTendaVDF baseDatos;
    private Usuario usuarioLoggeado = null;

    private void xestionarEventos(){

        // Botón para iniciar sesión
        Button btnLogIn = findViewById(R.id.btLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText etUsuario = findViewById(R.id.etUsuario);
                EditText etContrasinal = findViewById(R.id.etContrasinal);

                    usuarioLoggeado = baseDatos.getUsuario(etUsuario.getText().toString(), etContrasinal.getText().toString(), false);

                    // Comprobamos se o usuario existe e o tipo
                    if (usuarioLoggeado != null) {

                        if (usuarioLoggeado.getTipo() == null) {
                            // Tipo de usuario null. Avisar
                            Toast.makeText(getApplicationContext(), "Tipo de usuario null.", Toast.LENGTH_LONG).show();
                        } else {
                            switch (usuarioLoggeado.getTipo()) {
                                case "A": {
                                    // Usuario de tipo administrador
                                    // crear activity e lanzala
                                    Intent intent = new Intent();
                                    intent.setClassName(getApplicationContext(), "tenda.tarefa03.Administrador");
                                    // Pasámoslle á activity os datos do usuario
                                    intent.putExtra(USUARIO, usuarioLoggeado.getUsuario());
                                    startActivityForResult(intent, 1);
                                    break;
                                }
                                case "C": {
                                    // Usuario de tipo cliente
                                    // crear activity e lanzala
                                    Intent intent = new Intent();
                                    intent.setClassName(getApplicationContext(), "tenda.tarefa03.Cliente");
                                    // Pasámoslle á activity os datos do usuario
                                    intent.putExtra(USUARIO, usuarioLoggeado.getUsuario());
                                    startActivityForResult(intent, 2);
                                    break;
                                }
                                default: {
                                    // Tipo de usuario descoñecido. Avisar
                                    Toast.makeText(getApplicationContext(), "Tipo de usuario descoñecido.", Toast.LENGTH_LONG).show();
                                }
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

                //crear activity e lanzala
                Intent intent = new Intent();
                intent.setClassName(getApplicationContext(), "tenda.tarefa03.Rexistro");
                startActivityForResult(intent,2);
            }
        });


    }

    private void copiarBD() {
        String bddestino = "/data/data/" + getPackageName() + "/databases/"
                + BDTendaVDF.NOME_BD;
        File file = new File(bddestino);

        //if (file.exists()) {file.delete();}

        if (!file.exists()) {

            // A BD non existe no cartafol de destino, podemos copiala
            String pathbd = "/data/data/" + getPackageName() + "/databases";
            File filepathdb = new File(pathbd);
            if (!filepathdb.exists()) {
                filepathdb.mkdirs();
            }

            InputStream inputstream;
            try {
                inputstream = getAssets().open(BDTendaVDF.NOME_BD);
                OutputStream outputstream = new FileOutputStream(bddestino);

                int tamread;
                byte[] buffer = new byte[2048];

                while ((tamread = inputstream.read(buffer)) > 0) {
                    outputstream.write(buffer, 0, tamread);
                }

                inputstream.close();
                outputstream.flush();
                outputstream.close();

            } catch (Exception erro) {
                Toast.makeText(getApplicationContext(), "Erro inicializando Base de Datos", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "Pedidos: " + baseDatos.numPedidos(),Toast.LENGTH_LONG).show();
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
        copiarBD();
    }

}

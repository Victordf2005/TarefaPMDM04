package tenda.tarefa_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private void xestionarEventos(){

        // Botón para iniciar sesión
        Button btnLogIn = findViewById(R.id.btLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText etUsuario = findViewById(R.id.etUsuario);
                EditText etContrasinal = findViewById(R.id.etContrasinal);

                String usuario = etUsuario.getText().toString();
                String contrasinal = etContrasinal.getText().toString();

                if (usuario.equals("admin") && contrasinal.equals("abc123.")) {

                    //crear activity e lanzala
                    Intent intent = new Intent();
                    intent.setClassName(getApplicationContext(), "tenda.tarefa_02.Administrador");
                    startActivityForResult(intent,1);

                }else if (usuario.equals("cliente1") && contrasinal.equals("abc123.")) {

                    //crear activity e lanzala
                    Intent intent = new Intent();
                    intent.setClassName(getApplicationContext(), "tenda.tarefa_02.Cliente");
                    startActivityForResult(intent,2);

                } else {
                    //usuario incorrecto. Avisar
                    Toast.makeText(getApplicationContext(), "Usuario e/ou contrasinal incorrectos.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //borrar datos de login
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

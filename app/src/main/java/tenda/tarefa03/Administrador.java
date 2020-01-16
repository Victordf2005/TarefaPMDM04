package tenda.tarefa03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    }
}

package tenda.tarefa04;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import BaseDatos.BDTendaVDF;

import static androidx.core.content.FileProvider.getUriForFile;

public class CambiarDatosPerfil extends AppCompatActivity {

    private BDTendaVDF baseDatos;
    private String rutaImaxePerfil;
    private String nomeFoto;

    private final int FOTO_GALERIA = 1;
    private final int FOTO_NOVA = 2;

    private boolean permisoCamara = false;
    private boolean permisoGaleria = false;
    private boolean permisoWrite = false;

    private String usuario;

    private void xestionarEventos(){

        // Botón para rexistrarse
        Button btGravar = findViewById(R.id.btRexistrar);
        btGravar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gravarDatos();
            }

        });

    }

    private void amosarDatosUsuario() {

        Intent pantallaAnterior = getIntent();
        usuario = pantallaAnterior.getExtras().getString("usuario");

        String datosUsuario = usuario + " (" + pantallaAnterior.getExtras().getString("tipo") + ")";
        TextView infoUsuario = findViewById(R.id.tvInfoCambioPerfil);
        infoUsuario.setText(datosUsuario);

        EditText nome = findViewById(R.id.etNomeCambioPerfil);
        nome.setText(pantallaAnterior.getExtras().getString("nome"));

        EditText apelidos = findViewById(R.id.etApelidosCambioPerfil);
        apelidos.setText(pantallaAnterior.getExtras().getString("apelidos"));

        EditText email = findViewById(R.id.etEmailCambioPerfil);
        email.setText(pantallaAnterior.getExtras().getString("email"));
        ImageView imaxePerfil = findViewById(R.id.ivRexistroCambioPerfil);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!temosPermiso(1)) {
                ActivityCompat.requestPermissions(CambiarDatosPerfil.this, new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                permisoCamara = true;
            }
            if (!temosPermiso(2)) {
                ActivityCompat.requestPermissions(CambiarDatosPerfil.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            } else {
                permisoGaleria = true;
            }
            if (!temosPermiso(3)) {
                ActivityCompat.requestPermissions(CambiarDatosPerfil.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
            } else {
                permisoWrite = true;
            }
        }

        if (permisoGaleria) {
            Log.i("Permiso: ", "Si");
            Bitmap bitmap = BitmapFactory.decodeFile(pantallaAnterior.getExtras().getString("imaxePerfil"));
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

    private boolean temosPermiso(int codigo) {

        int result = PackageManager.PERMISSION_DENIED;

        switch (codigo) {
            case 1:
                result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                break;
            case 2:
                result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case 3:
                result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            break;
        }
        return result == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (grantResults.length>0) {
            switch (requestCode) {
                case 1: {
                    // Se o usuario premeou o boton de cancelar o array volve cun null
                    permisoCamara = true;
                    TextView infoPerfil = findViewById(R.id.tvInfoImaxe);
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        xestionarCamara();
                    } else {
                        infoPerfil.setText("Non se pode cargar ningunha imaxe\nmentras non permita acceso á cámara");
                    }
                }
                case 2: {
                    permisoGaleria = true;
                }
                case 3: {
                    permisoWrite = true;
                }

            }
        }
    }

    private void xestionarCamara() {

        ImageView fotoPerfil = findViewById(R.id.ivRexistro);
        fotoPerfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Preguntamos se o usuario quere facer unha foto ou coller unha da galería
                fonteFoto();
            }
        });
    }

    private void fonteFoto() {

        final CharSequence[] opcions = {"Cámara","Galería","Cancelar"};
        final AlertDialog.Builder dialogo = new AlertDialog.Builder(CambiarDatosPerfil.this);

        dialogo.setTitle("Escolle a orixe da foto");
        dialogo.setIcon(android.R.drawable.ic_dialog_alert);
        dialogo.setItems(opcions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int opcionEscollida) {
                switch (opcionEscollida){
                    case 0:
                        if (permisoCamara && permisoWrite) {
                            novaFoto();
                        } else {
                            Toast.makeText(getApplicationContext(), "A aplicación non ten permiso para empregar a cámara.", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        if (permisoGaleria) {
                            escollerFoto();
                        } else {
                            Toast.makeText(getApplicationContext(), "A aplicación non ten permiso para acceder á galería.", Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        dialog.dismiss();
                }
            }
        });
        dialogo.show();
    }

    private void novaFoto() {

        File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        nomeFoto = "Img_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        File arquivo = new File(ruta, nomeFoto);
        Uri contentUri=null;
        if (Build.VERSION.SDK_INT >= 24) {
            contentUri = getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", arquivo);
        }
        else {
            contentUri = Uri.fromFile(arquivo);
        }

        Intent intento = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intento.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        startActivityForResult(intento, FOTO_NOVA);

    }

    private void escollerFoto() {
        Intent intento = new Intent(Intent.ACTION_PICK);
        intento.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intento.createChooser(intento,"Selecciona app de imaxes"), FOTO_GALERIA);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            File ruta, arquivo;
            ImageView fotoPerfil = findViewById(R.id.ivRexistro);

            switch (requestCode) {
                case FOTO_NOVA:
                    ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    arquivo = new File(ruta, nomeFoto);
                    if (arquivo.exists()) {
                        rutaImaxePerfil = arquivo.getAbsolutePath();
                        Bitmap imaxe = BitmapFactory.decodeFile(arquivo.getAbsolutePath());
                        fotoPerfil.setImageBitmap(imaxe);
                    } else {
                        Toast.makeText(getApplicationContext(),"Erro accedendo á foto recén feita.",Toast.LENGTH_LONG).show();
                    }
                    break;
                case FOTO_GALERIA:
                    Uri ruta2 = data.getData();
                    fotoPerfil.setImageURI(ruta2);
                    rutaImaxePerfil = rutaDeUri(ruta2,getContentResolver());
                    break;
            }
        }

    }

    private String rutaDeUri(Uri selectedAudioUri,
                             ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedAudioUri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    private void gravarDatos() {

        if (datosCorrectos()){
            // Podemos rexistrar o novo usuario, recollemos o resto de datos do layout
            Spinner spnTipoUsuario = findViewById(R.id.spnTipo);
            String tipo = spnTipoUsuario.getSelectedItem().toString().substring(0, 1);   // O código de tipo é a primeira letra

            Long resultado = baseDatos.actualizarUsuario(((EditText) findViewById(R.id.etNome)).getText().toString(),
                    ((EditText) findViewById(R.id.etApelidos)).getText().toString(),
                    ((EditText) findViewById(R.id.etEmail)).getText().toString(),
                    usuario,
                    ((EditText) findViewById(R.id.etContrasinalRexistro)).getText().toString(),
                    rutaImaxePerfil);

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

    private boolean datosCorrectos() {

        String erros = "";
        EditText etUsuario = findViewById(R.id.etUsuarioRexistro);
        String usuario = etUsuario.getText().toString();

        if (((EditText) findViewById(R.id.etNome)).getText().toString().trim().equals("")) {
            erros += "\nDebe indicar o nome.";
        }
        if (((EditText) findViewById(R.id.etApelidos)).getText().toString().trim().equals("")) {
            erros += "\nDebe indicar os apelidos.";
        }
        if (((EditText) findViewById(R.id.etEmail)).getText().toString().trim().equals("")) {
            erros += "\nDebe indicar o email.";
        }
        if (((EditText) findViewById(R.id.etContrasinal1RexistroCambioPerfil)).getText().toString().trim().equals("")) {
            erros += "\nDebe indicar un contrasinal.";
        }
        if (!((EditText) findViewById(R.id.etContrasinal2RexistroCambioPerfil)).getText().toString().trim()
                .equals(((EditText) findViewById(R.id.etContrasinal1RexistroCambioPerfil)).getText().toString().trim())) {
            erros += "\nOs contrasinais non coinciden.";
        }

        if (erros.equals("")) {
            return true;
        } else {

            AlertDialog.Builder dialogo = new AlertDialog.Builder(CambiarDatosPerfil.this);

            dialogo.setTitle("Erros atopados:\n");
            dialogo.setIcon(android.R.drawable.ic_dialog_alert);

            Intent intent1 = getIntent();

            dialogo.setMessage(erros);

            dialogo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialogo.create();
            dialogo.show();
            return false;
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
        amosarDatosUsuario();
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
        setContentView(R.layout.activity_cambiar_datos_perfil);

        xestionarEventos();
    }
}

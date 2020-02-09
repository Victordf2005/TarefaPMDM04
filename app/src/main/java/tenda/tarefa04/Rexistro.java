package tenda.tarefa04;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class Rexistro extends AppCompatActivity {

    private BDTendaVDF baseDatos;
    private String rutaImaxePerfil;
    private String nomeFoto;

    private final int FOTO_GALERIA = 1;
    private final int FOTO_NOVA = 2;

    private boolean permisoCamara = false;
    private boolean permisoGaleria = false;
    private boolean permisoTarxeta = false;


    // Gardamos a ruta da imaxe escollida, por se foi modificada, xa que se perde cando cambia de orientación
    @Override
    protected void onSaveInstanceState(Bundle estado){
        super.onSaveInstanceState(estado);
        estado.putString("IMAXE", rutaImaxePerfil);
    }

    // Recuperar a ruta da imaxe seleccionada antes do cambio de orientación
    @Override
    protected void onRestoreInstanceState(Bundle estado) {
        super.onRestoreInstanceState(estado);

        // Volvemos a gardar a ruta da imaxe seleccionada
        rutaImaxePerfil = estado.getString("IMAXE");

        // Volvemos a recuperar a imaxe seleccionada por se fora modificada antes do cambio de orientación
        ImageView imaxePerfil = findViewById(R.id.ivRexistro);

        Bitmap bitmap = BitmapFactory.decodeFile(estado.getString("IMAXE"));

        if (!(bitmap == null)) {
            Bitmap bitmapEscalado = null;

            // Escalamos a imaxe segundo a densidade do dispositivo
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


    private void xestionarEventos(){

        // Botón para rexistrarse
        Button btRexistrarse = findViewById(R.id.btRexistrar);
        btRexistrarse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rexistrarUsuario();
            }

        });

        // Evento click na imaxe
        ImageView fotoPerfil = findViewById(R.id.ivRexistro);
        fotoPerfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Preguntamos se o usuario quere facer unha foto ou coller unha da galería
                fonteFoto();
            }
        });

    }


    // Pedimos permisos
    public void pedirPermiso(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions( new String[]{Manifest.permission.CAMERA},1);
            requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (grantResults.length>0) {
            switch (requestCode) {
                case 1: {
                    // Se o usuario premeou o boton de cancelar o array volve cun null
                    permisoCamara = true;
                }
                case 2: {
                    permisoGaleria = true;
                }
                case 3: {
                    permisoTarxeta = true;
                }

            }
        }
    }

    // Preguntamos se obtemos a imaxe da cámara ou da galería
    private void fonteFoto() {

        final CharSequence[] opcions = {"Cámara","Galería","Cancelar"};
        final AlertDialog.Builder dialogo = new AlertDialog.Builder(Rexistro.this);

        dialogo.setTitle("Escolle a orixe da foto");
        dialogo.setIcon(android.R.drawable.ic_dialog_alert);
        dialogo.setItems(opcions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int opcionEscollida) {
                switch (opcionEscollida){
                    case 0:
                        if (permisoCamara && permisoTarxeta) {
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

    // Obteremos a imaxe facendo unha novo foto
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

    // Obteremos a imaxe seleccionándoa da galería
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

                    // Comprobamos se existe a foto recén feita
                    if (arquivo.exists()) {
                        rutaImaxePerfil = arquivo.getAbsolutePath();
                        Bitmap imaxe = BitmapFactory.decodeFile(arquivo.getAbsolutePath());

                        if (!(imaxe == null)) {
                            Bitmap bitmapEscalado = null;

                            // Escalamos a imaxe segundo a densidade do dispositivo
                            switch (getResources().getDisplayMetrics().densityDpi) {
                                case DisplayMetrics.DENSITY_LOW:
                                    bitmapEscalado = Bitmap.createScaledBitmap(imaxe, 320, 320, false);
                                    break;
                                case DisplayMetrics.DENSITY_MEDIUM:
                                    bitmapEscalado = Bitmap.createScaledBitmap(imaxe, 480, 480, false);
                                    break;
                                case DisplayMetrics.DENSITY_HIGH:
                                    bitmapEscalado = Bitmap.createScaledBitmap(imaxe, 600, 600, false);
                                    break;
                                case DisplayMetrics.DENSITY_XHIGH:
                                    bitmapEscalado = Bitmap.createScaledBitmap(imaxe, 960, 960, false);
                                    break;
                                default:
                                    bitmapEscalado = Bitmap.createScaledBitmap(imaxe, 480, 480, false);
                                    break;
                            }
                            // Asignamos a imaxe escalada
                            fotoPerfil.setImageBitmap(bitmapEscalado);
                        }
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

    // Método que devolve a ruta absoluta á imaxe
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

    // Método para gardar os datos do novo usuario
    private void rexistrarUsuario() {

        EditText etUsuario = findViewById(R.id.etUsuarioRexistro);
        String usuario = etUsuario.getText().toString();

        // Comprobamos se xa existe un usuario rexistrado con ese mesmo usuario
        Boolean xaExiste = baseDatos.existeUsuario(etUsuario.getText().toString());

        if (datosCorrectos()){
            // Podemos rexistrar o novo usuario, recollemos o resto de datos do layout
            Spinner spnTipoUsuario = findViewById(R.id.spnTipo);
            String tipo = spnTipoUsuario.getSelectedItem().toString().substring(0, 1);   // O código de tipo é a primeira letra

            Long resultado = baseDatos.engadirUsuario(((EditText) findViewById(R.id.etNome)).getText().toString(),
                    ((EditText) findViewById(R.id.etApelidos)).getText().toString(),
                    ((EditText) findViewById(R.id.etEmail)).getText().toString(),
                    ((EditText) findViewById(R.id.etUsuarioRexistro)).getText().toString(),
                    ((EditText) findViewById(R.id.etContrasinalRexistro)).getText().toString(),
                    tipo, rutaImaxePerfil);

            if (resultado > 0) {

                // Datos gravados correctamente
                Toast.makeText(getApplicationContext(), "Datos gravados correctamewnte; código: " + resultado, Toast.LENGTH_LONG).show();

                // Esperar uns segundos a pechar a activity para permitir ver a mensaxe Toast
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

    // Método que comproba se os datos introducidos son correctos
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
        if (((EditText) findViewById(R.id.etUsuarioRexistro)).getText().toString().trim().equals("")) {
            erros += "\nDebe indicar o usuario co que desexa rexistrarse.";
        } else {
            // Comprobamos se xa existe un usuario rexistrado con ese mesmo usuario
            Boolean xaExiste = baseDatos.existeUsuario(etUsuario.getText().toString());
            if (xaExiste) {
                erros += "\nXa existe un rexistro co usuario '" + usuario + "'.";
            }
        }
        if (((EditText) findViewById(R.id.etContrasinalRexistro)).getText().toString().trim().equals("")) {
            erros += "\nDebe indicar un contrasinal.";
        }

        if (erros.equals("")) {
            return true;
        } else {

            AlertDialog.Builder dialogo = new AlertDialog.Builder(Rexistro.this);

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
        setContentView(R.layout.activity_rexistro);

        pedirPermiso();
        xestionarEventos();
    }
}

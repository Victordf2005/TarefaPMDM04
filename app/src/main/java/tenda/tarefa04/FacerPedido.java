package tenda.tarefa04;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

//TODO: Facer que se habilite e inhabilite o boton seguinte de xeito correcto
public class FacerPedido extends AppCompatActivity {

    //Nomes das variables en SaveInstanceState
    public final static String IDCATEGORIA = "idcategoria";
    public final static String CATEGORIA = "categoria";
    public final static String IDPRODUTO = "idproduto";
    public final static String PRODUTO = "produto";
    public final static String CANTIDADE = "cantidade";

    private boolean permisoGaleria = false;

    // Gardar elementos seleccionados, xa que se perden cando cambia de orientación
    @Override
    protected void onSaveInstanceState(Bundle estado){
        super.onSaveInstanceState(estado);
        estado.putInt("CAT", ((Spinner) findViewById(R.id.spnCategoria)).getSelectedItemPosition());
        estado.putInt("PROD",  ((Spinner) findViewById(R.id.spnProduto)).getSelectedItemPosition());
        estado.putInt("CANT",  ((Spinner) findViewById(R.id.spnCantidade)).getSelectedItemPosition());
    }

    // Recuperar elementos seleccionados cando cambie a orientación
    @Override
    protected void onRestoreInstanceState(Bundle estado) {
        super.onRestoreInstanceState(estado);

        //Collemos o spinner e seleccionamos a categoría que estaba escollida e deshabilitamos os listeners
        Spinner spCat = findViewById(R.id.spnCategoria);
        int estadoCategoria = estado.getInt("CAT");
        spCat.setOnItemSelectedListener(null);

        //Collemos  spiner do prodcuto e o seu estado
        int estadoProducto = estado.getInt("PROD");
        Spinner spProd = findViewById(R.id.spnProduto);
        spProd.setOnItemSelectedListener(null);

        //Collemos o spinner de cantidade e o seu estado
        int estadoCantidade = estado.getInt("CANT");
        Spinner spCant = findViewById(R.id.spnCantidade);
        spCant.setOnItemSelectedListener(null);

        //Seleccionamos a categoría que tiñamos antes
        spCat.setSelection(estadoCategoria,false);

        if(estadoCategoria > 0) {

            //Collemos o array cos datos que meteremos no spinner do producto segundo a seleccion de categoria
            ArrayAdapter<CharSequence> adapter;
            switch (estadoCategoria){
                case 1:
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.arrInformatica, android.R.layout.simple_spinner_item);
                    break;
                case 2:
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.arrElectronica, android.R.layout.simple_spinner_item);
                    break;
                default:
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.arrMobiles, android.R.layout.simple_spinner_item);
                    break;
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Aplicar o adapter ao spinner
            spProd.setAdapter(adapter);
            // Habilitar o spinner
            spProd.setEnabled(true);

            //selecionamos o producto que estaba
            if(estadoProducto > 0){
                spCant.setEnabled(true);
                spProd.setSelection(estadoProducto,false);

                //Agora seleccionamos a cantidade de productos
                spCant.setSelection(estadoCantidade,false);

            }
            else{
                //Non deixamos escoller a cantidade
                spCant.setEnabled(false);
            }
        }
        else{
            spProd.setEnabled(false);
            spCant.setEnabled(false);
        }

        //Habilitamos os liesteners
        this.xestionarEventos();
        habilitarBotonSeguinte(spCant.isEnabled());
    }

    private void xestionarEventos(){

        //Spinner de categorías
        final Spinner spCat =  findViewById(R.id.spnCategoria);
        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int posicion, long id) {


                switch (posicion) {
                    case 0: {
                        // Ningunha categoría selecionada
                        Spinner sp = findViewById(R.id.spnProduto);
                        habilitarSpinnerProductos(false );
                        habilitarBotonSeguinte(false);
                        break;
                    }
                    case 1: {
                        Spinner spP = findViewById(R.id.spnProduto);
                        // Crear un ArrayAdapter usando un string array e un spinner layout por defecto
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                R.array.arrInformatica, android.R.layout.simple_spinner_item);
                        // Indicar o layout a usar cando se cargue a lista
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Aplicar o adapter ao spinner
                        spP.setAdapter(adapter);
                        // Habilitar o spinner
                        spP.setEnabled(true);
                        break;
                    }
                    case 2: {
                        Spinner spP = findViewById(R.id.spnProduto);
                        // Crear un ArrayAdapter usando un string array e un spinner layout por defecto
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                R.array.arrElectronica, android.R.layout.simple_spinner_item);
                        // Indicar o layout a usar cando se cargue a lista
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Aplicar o adapter ao spinner
                        spP.setAdapter(adapter);
                        // habilitar spinner
                        spP.setEnabled(true);
                        break;
                    }
                    case 3: {
                        Spinner spP = findViewById(R.id.spnProduto);
                        // Crear un ArrayAdapter usando un string array e un spinner layout por defecto
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                R.array.arrMobiles, android.R.layout.simple_spinner_item);
                        // Indicar o layout a usar cando se cargue a lista
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Aplicar o adapter ao spinner
                        spP.setAdapter(adapter);
                        // Habilitar o spinner
                        spP.setEnabled(true);
                        break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Spinner de produtos
        final Spinner spProd =  findViewById(R.id.spnProduto);
        spProd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int posicion, long id) {
                if(view == null) return;
                Log.i("onItemSelectedP","producto: " + posicion);

                Spinner spCant = findViewById(R.id.spnCantidade);
                // Só habilitamos o botón "seguinte" cando teña selecionado algún produto
                switch (posicion) {
                    case 0: {

                        habilitarBotonSeguinte(false);
                        spCant.setEnabled(false);
                        break;
                    }
                    default: {
                        spCant.setEnabled(true);
                        habilitarBotonSeguinte(true );
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Botón seguinte
        Button btnSeguinte = findViewById(R.id.btSeguinte);
        btnSeguinte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //crear activity
                Intent intent = new Intent();
                intent.setClassName(getApplicationContext(), "tenda.tarefa04.EnderezoEnvio");

                // Pasámoslle á activity os datos selecionados
                intent.putExtras(getIntent().getExtras());
                intent.putExtra(IDCATEGORIA, String.valueOf(((Spinner) findViewById(R.id.spnCategoria)).getSelectedItemId()));
                intent.putExtra(CATEGORIA, ((Spinner) findViewById(R.id.spnCategoria)).getSelectedItem().toString());
                intent.putExtra(IDPRODUTO, String.valueOf(((Spinner) findViewById(R.id.spnProduto)).getSelectedItemId()));
                intent.putExtra(PRODUTO, ((Spinner) findViewById(R.id.spnProduto)).getSelectedItem().toString());
                intent.putExtra(CANTIDADE, ((Spinner) findViewById(R.id.spnCantidade)).getSelectedItem().toString());
                Intent intentAnterior = getIntent();
                intent.putExtra("imaxePerfil,", intentAnterior.getExtras().getString("imaxeperfil"));

                //Lanzamos a activity
                startActivityForResult(intent, 211);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 211) {
            // Volve da pantalla de facer un pedido
            if (resultCode == 1) {
                // Hai que destruir a activity para amosar a pantalla de cliente
                this.finish();
            }
        }

    }

    private void habilitarSpinnerProductos(Boolean siNon) {
        //Deshabilitar spinner produtos mentres no escolla categoría
        Spinner spP = findViewById(R.id.spnProduto);
        spP.setEnabled(siNon);
    }

    private void habilitarBotonSeguinte(Boolean siNon) {
        //Deshabilitar botón seguinte mentres no escolla categoría
        Button btSeguinte = findViewById(R.id.btSeguinte);
        btSeguinte.setEnabled(siNon);
    }

    private void buscarDatosCliente() {

        Intent intent1 = getIntent();
        TextView lblCliente = findViewById(R.id.tvNomeCliente);
        lblCliente.setText(intent1.getExtras().getString("nome_cliente") + "\n" + intent1.getExtras().get("apelidos_cliente"));
        ImageView imaxePerfil = findViewById(R.id.ivCliente);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!temosPermiso()) {
                ActivityCompat.requestPermissions(FacerPedido.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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

        buscarDatosCliente();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facer_pedido);

        Toolbar barra = findViewById(R.id.toolbarNovoPedido);
        setSupportActionBar(barra);

        xestionarEventos();

        Spinner spCant = findViewById(R.id.spnCantidade);
        spCant.setEnabled(false);
        habilitarSpinnerProductos(false );
        habilitarBotonSeguinte(false);

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
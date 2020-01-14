// VERSION DESENVOLVIDA POLO PROFESOR

package tenda.tarefa03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import tenda.tarefa_02.R;

//TODO: Facer que se habilite e inhabilite o boton seguinte de xeito correcto
public class FacerPedido extends AppCompatActivity {

    //Nomes das variables en SaveInstanceState
    public final static String CATEGORIA = "categoria";
    public final static String PRODUTO = "produto";
    public final static String CANTIDADE = "cantidade";


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
                intent.setClassName(getApplicationContext(), "EnderezoEnvio");

                // Pasámoslle á activity os datos selecionados
                intent.putExtra(CATEGORIA, ((Spinner) findViewById(R.id.spnCategoria)).getSelectedItem().toString());
                intent.putExtra(PRODUTO, ((Spinner) findViewById(R.id.spnProduto)).getSelectedItem().toString());
                intent.putExtra(CANTIDADE, ((Spinner) findViewById(R.id.spnCantidade)).getSelectedItem().toString());

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facer_pedido);

        xestionarEventos();

        Spinner spCant = findViewById(R.id.spnCantidade);
        spCant.setEnabled(false);
        habilitarSpinnerProductos(false );
        habilitarBotonSeguinte(false);

    }


}
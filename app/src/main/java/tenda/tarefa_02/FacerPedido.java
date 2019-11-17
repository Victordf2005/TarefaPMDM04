package tenda.tarefa_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class FacerPedido extends AppCompatActivity {

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
        Spinner spCat = findViewById(R.id.spnCategoria);
        spCat.setSelection((estado.getInt("CAT")));
        Spinner spProd = findViewById(R.id.spnProduto);
        spProd.setSelection(estado.getInt("PROD"));
        Spinner spCant = findViewById(R.id.spnCantidade);
        spCant.setSelection(estado.getInt("CANT"));

    }

    private void xestionarEventos(){

        //Spinner de categorías
        final Spinner spCat =  findViewById(R.id.spnCategoria);
        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int posicion, long id) {

                //Cando seleccione unha categoría, cargamos os produtos que inclúe
                switch (posicion) {
                    case 0: {
                        // Ningunha categoría selecionada
                        Spinner sp = findViewById(R.id.spnProduto);
                        sp.setSelection(0);
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

                // Só habilitamos o botón "seguinte" cando teña selecionado algún produto
                switch (posicion) {
                    case 0: {
                        habilitarBotonSeguinte(false);
                        break;
                    }
                    default: {
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
                intent.setClassName(getApplicationContext(), "tenda.tarefa_02.EnderezoEnvio");

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

        habilitarSpinnerProductos(false );
        habilitarBotonSeguinte(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Spinner spCat = findViewById(R.id.spnCategoria);
        spCat.setSelection(0);
    }

}

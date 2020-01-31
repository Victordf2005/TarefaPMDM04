package BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BDTendaVDF  extends SQLiteOpenHelper {

    public final static String NOME_BD = "bdTarefa03.db";
    public final static int VERSION_BD = 1;
    public final static String TABOA_USUARIOS = "usuarios";
    public final static String TABOA_PEDIDOS = "pedidos";
    public final static String TABOA_PRODUTOS = "produtos";

    private static BDTendaVDF sInstance;
    private SQLiteDatabase sqlLiteDB;

    // Construtor por defecto
    public BDTendaVDF(Context contexto) {
        super(contexto, NOME_BD,null,VERSION_BD);
    }

    // Instanciamos BD empregando singleton
    public static synchronized BDTendaVDF getInstance(Context contexto) {
        if (sInstance == null) {
            sInstance = new BDTendaVDF(contexto.getApplicationContext());
        }
        return sInstance;
    }

    // Método para averiguar se xa hai un usuario rexistrado co mesmo id de usuario
    public Boolean existeUsuario(String usuario) {

        // Buscamos rexistros con ese id de usuario
        Cursor consulta = sqlLiteDB.rawQuery("select * from " + TABOA_USUARIOS + " where us_usuario=?", new String[] {usuario});

        // Devolvemos se ten rexistros
        return (consulta.getCount() > 0);
    }

    public int numUsuariosRexistrados () {

        // Comprobamos as credenciais
        Cursor consulta = sqlLiteDB.rawQuery("select * from " + TABOA_USUARIOS , new String[] {});

        return consulta.getCount();
    }

    public int numPedidos() {
        Cursor consulta = sqlLiteDB.rawQuery("select * from " + TABOA_PEDIDOS, new String[] {});
        return consulta.getCount();
    }


    // Método para engadir un novo usuario
    public long engadirUsuario(String nome, String apelidos, String email, String usuario, String contrasinal, String tipo) {

        // creamos o rexistro
        ContentValues rexistro = new ContentValues();

        // engadimos valores ás columnas
        rexistro.put("us_nome", nome);
        rexistro.put("us_apelidos", apelidos);
        rexistro.put("us_email", email);
        rexistro.put("us_usuario", usuario);
        rexistro.put("us_contrasinal", contrasinal);
        rexistro.put("us_tipo", tipo);

        // gravamos na BD, que nos devolve o _id do rexistro ou -1 se hai erro
        long codigo = sqlLiteDB.insert(TABOA_USUARIOS, null, rexistro);

        return codigo;
    }

    // Método para devolver un obxecto usuario se existe.
    // Se o usuario non existe, devolve null
    public Usuario getUsuario (String usuario, String contrasinal, Boolean soUsuario) {

        // Por defecto asumimos que as credenciais non son correctas
        Usuario retorno = null;
        Cursor consulta = null;

        // Comprobamos as credenciais
        if (soUsuario) {
            consulta = sqlLiteDB.rawQuery("select * from " + TABOA_USUARIOS + " where us_usuario= ?", new String[]{usuario});
        } else {
            consulta = sqlLiteDB.rawQuery("select * from " + TABOA_USUARIOS + " where us_usuario= ? and us_contrasinal=?", new String[]{usuario, contrasinal});
        }

        if (consulta.moveToFirst()) {
            retorno = new Usuario(
                    consulta.getInt(0),
                    consulta.getString(1),
                    consulta.getString(2),
                    consulta.getString(3),
                    consulta.getString(4),
                    consulta.getString(5),
                    consulta.getString(6));
        }

        return retorno;
    }

    public long gravarPedido (String estado, int idCliente, int categoria, int produto, int cantidade, String enderezo, String cidade, String codpostal) {

        ContentValues rexistro = new ContentValues();

        rexistro.put("pe_estado", estado);
        rexistro.put("pe_idcliente", idCliente);
        rexistro.put("pe_idcategoria", categoria);
        rexistro.put("pe_idproduto", produto);
        rexistro.put("pe_cantidade", cantidade);
        rexistro.put("pe_enderezoenvio", enderezo);
        rexistro.put("pe_cidadeenvio", cidade);
        rexistro.put("pe_codpostalenvio", codpostal);

        long codigo = sqlLiteDB.insert(TABOA_PEDIDOS, null, rexistro);

        return codigo;

    }

    public ArrayList<Pedido> getPedidosCliente(String tipo, String idCliente) {

        ArrayList<Pedido> retorno = new ArrayList<Pedido>();

        Cursor cursor = null;
        if (idCliente.equals("")) {
            // Buscamos pedidos de todos os clientes
            cursor = sqlLiteDB.rawQuery("select pe._id, pe_estado, pe_cantidade, pr._id, pr_produto, pe_enderezoenvio, pe_cidadeenvio, pe_codpostalenvio, pe_idcliente" +
                    " from " + TABOA_PEDIDOS + " pe left join " + TABOA_PRODUTOS + " pr on pe_idcategoria=pr_idcategoria and pe_idproduto=pr_idproduto" +
                    " where pe_estado=?", new String[]{tipo});
        } else{
            // Buscamos pedidos dun cliente determinado
            cursor = sqlLiteDB.rawQuery("select pe._id, pe_estado, pe_cantidade, pr._id, pr_produto, pe_enderezoenvio, pe_cidadeenvio, pe_codpostalenvio, pe_idcliente" +
                    " from " + TABOA_PEDIDOS + " pe left join " + TABOA_PRODUTOS + " pr on pe_idcategoria=pr_idcategoria and pe_idproduto=pr_idproduto" +
                    " where pe_idcliente=? and pe_estado=?", new String[]{idCliente, tipo});
        }

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Pedido aux = new Pedido(cursor.getInt(0), cursor.getString(1), cursor.getLong(8), cursor.getInt(2),
                        cursor.getLong(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),cursor.getString(7));

                retorno.add(aux);
                cursor.moveToNext();
            }
        }

        return retorno;
    }

    public int actualizarEstadoPedido(long numPedido, String estado) {

        ContentValues rexistro = new ContentValues();
        rexistro.put("pe_estado",estado);
        String condicionWhere = "_id=?";
        String[] parametros = new String[]{String.valueOf(numPedido)};
        int rexistrosafectados = sqlLiteDB.update(TABOA_PEDIDOS,rexistro,condicionWhere,parametros);

        return rexistrosafectados;

    }


    public void abrirBD(){
        if (sqlLiteDB==null || !sqlLiteDB.isOpen()){
            sqlLiteDB = sInstance.getWritableDatabase();
        }
    }

    public void pecharBD(){
        if (sqlLiteDB!=null && sqlLiteDB.isOpen()){
            sqlLiteDB.close();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

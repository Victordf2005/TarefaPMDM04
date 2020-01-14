package BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDTendaVDF extends SQLiteOpenHelper {

    public final static String NOME_BD = "BDTendaVDF.db";
    public final static int VERSION_BD = 1;
    public final static String TABOA_USUARIOS = "usuarios";

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

    // Método para averiguar se xa hai un usuario rexistrado
    public boolean existe(String usuario) {

        // Por defecto asumimos que non existe o usuario
        Boolean retorno = false;

        // Comprobamos se o usuario xa existe na base de datos
        Cursor consulta = sqlLiteDB.rawQuery("select _id from " + TABOA_USUARIOS +" where us_usuario = ?", new String[] { usuario });

        if (consulta.moveToFirst()) {
            // Informamos que o usuario xa existe
            retorno = true;
        }

        return retorno;
    }

    // Método para devolver un obxecto usuario se existe.
    // Se o usuario non existe, devolve null
    public Usuario getUsuario (String usuario, String contrasinal) {

        // Por defecto asumimos que as credenciais non son correctas
        Usuario retorno = null;

        // Comprobamos as credenciais
        Cursor consulta = sqlLiteDB.rawQuery("select * from " + TABOA_USUARIOS +" where us_usuario = ? and us_constrasinal = ?", new String[] { usuario, contrasinal });

        if (consulta.moveToFirst()) {
            retorno = new Usuario(
                    consulta.getLong(0),
                    consulta.getString(1),
                    consulta.getString(2),
                    consulta.getString(3),
                    consulta.getString(4),
                    consulta.getString(5),
                    consulta.getString(6));
        }

        return retorno;
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

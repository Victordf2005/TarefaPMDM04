package BaseDatos;

public class Usuario {

    private long codigo = 0;
    private String nome = "";
    private String apelidos = "";
    private String email = "";
    private String usuario = "";
    private String contrasinal = "";
    private String tipo = "";
    private String imaxePerfil = "";

    // construtor por defecto
    public Usuario(){}

    // construtor con parámetros
    public Usuario(long codigo, String nome, String apelidos, String email, String usuario, String contrasinal, String tipo, String imaxePerfil) {
        this.codigo = codigo;
        this.nome = nome;
        this.apelidos = apelidos;
        this.email = email;
        this.usuario = usuario;
        this.contrasinal = contrasinal;
        this.tipo = tipo;
        this.imaxePerfil = imaxePerfil;
    }

    // getters
    public long getCodigo() {return this.codigo;}
    public String getNome() {return this.nome;}
    public String getApelidos() {return this.apelidos;}
    public String getEmail() {return this.email;}
    public String getUsuario() {return this.usuario;}
    public String getContrasinal() {return this.contrasinal;}
    public String getTipo() {return this.tipo;}
    public String getImaxePerfil() {return this.imaxePerfil;}

    // setters
    public void setCodigo(int codigo) {this.codigo = codigo;}
    public void setNome(String nome) {this.nome = nome;}
    public void setApelidos(String apelidos) {this.apelidos = apelidos;}
    public void setEmail(String email) {this.email = email;}
    public void setUsuario(String usuario){this.usuario = usuario;}
    public void setContrasinal(String contrasinal) {this.contrasinal = contrasinal;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setImaxePerfil(String imaxePerfil) {this.imaxePerfil = imaxePerfil;}

}

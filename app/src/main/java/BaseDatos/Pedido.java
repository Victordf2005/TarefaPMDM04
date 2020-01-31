package BaseDatos;

public class Pedido {

    private int codigo = 0;
    private String estado = "";
    private long idCliente = 0;
    private int cantidade = 0;
    private long idProduto = 0;
    private String produto = "";
    private String enderezo = "";
    private String cidade = "";
    private String codpostal = "";

    // construtor por defecto
    public Pedido(){}

    // construtor con parámetros
    public Pedido(int codigo, String estado, long idCliente, int cantidade, long idProduto, String produto, String enderezo, String cidade, String codpostal) {
        this.codigo = codigo;
        this.estado = estado;
        this.idCliente = idCliente;
        this.cantidade = cantidade;
        this.idProduto = idProduto;
        this.produto = produto;
        this.enderezo = enderezo;
        this.cidade = cidade;
        this.codpostal = codpostal;
    }

    // getters
    public int getCodigo() {return this.codigo;}
    public String getEstado() {return this.estado;}
    public long getIdCliente() {return this.idCliente;}
    public int getCantidade() {return this.cantidade;}
    public long getIdProduto() {return this.idProduto;}
    public String getProduto() {return this.produto;}
    public String getEnderezo() {return this.enderezo;}
    public String getCidade() {return this.cidade;}
    public String getCodpostal() {return this.codpostal;}

}

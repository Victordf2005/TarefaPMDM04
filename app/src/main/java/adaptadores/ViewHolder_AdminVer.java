package adaptadores;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tenda.tarefa03.R;

public class ViewHolder_AdminVer extends RecyclerView.ViewHolder {

    public TextView tvCliente;
    public TextView tvInfoProduto;
    public TextView tvInfoEnderezo;
    public Button btAceptar;
    public Button btRexeitar;

    public ViewHolder_AdminVer(@NonNull View itemView) {
        super(itemView);

        tvCliente = itemView.findViewById(R.id.tvAdmCliente);
        tvInfoProduto = itemView.findViewById(R.id.tvInfoAdmProduto);
        tvInfoEnderezo = itemView.findViewById(R.id.tvInfoAdmEnderezo);
        btAceptar = itemView.findViewById(R.id.btAceptarPedido);
        btRexeitar = itemView.findViewById(R.id.btRexeitarPedido);
    }
}

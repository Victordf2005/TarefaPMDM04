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

    public ViewHolder_AdminVer(@NonNull View itemView) {
        super(itemView);

        tvCliente = itemView.findViewById(R.id.tvAdmClienteVer);
        tvInfoProduto = itemView.findViewById(R.id.tvInfoAdmProdutoVer);
        tvInfoEnderezo = itemView.findViewById(R.id.tvInfoAdmEnderezoVer);
    }
}

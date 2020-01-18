package adaptadores;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tenda.tarefa03.R;

public class ViewHolder_Admin extends RecyclerView.ViewHolder {

    public TextView tvCliente;
    public TextView tvInfoProduto;
    public TextView tvInfoEnderezo;

    public ViewHolder_Admin(@NonNull View itemView) {
        super(itemView);

        tvCliente = itemView.findViewById(R.id.tvCliente);
        tvInfoProduto = itemView.findViewById(R.id.tvInfoProduto);
        tvInfoEnderezo = itemView.findViewById(R.id.tvInfoEnderezo);
    }
}

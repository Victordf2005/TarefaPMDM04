package adaptadores;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tenda.tarefa04.R;

public class ViewHolder_Compras extends RecyclerView.ViewHolder {

    public TextView tvInfoProduto;
    public TextView tvInfoEnderezo;

    public ViewHolder_Compras(@NonNull View itemView) {
        super(itemView);

        tvInfoProduto = itemView.findViewById(R.id.tvInfoPedProduto);
        tvInfoEnderezo = itemView.findViewById(R.id.tvInfoPedEnderezo);
    }
}

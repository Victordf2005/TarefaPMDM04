package adaptadores;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tenda.tarefa03.R;

public class ViewHolder_Pedidos extends RecyclerView.ViewHolder {

    public TextView tvInfoProduto;
    public TextView tvInfoEnderezo;

    public ViewHolder_Pedidos(@NonNull View itemView) {
        super(itemView);

        tvInfoProduto = itemView.findViewById(R.id.tvInfoProduto);
        tvInfoEnderezo = itemView.findViewById(R.id.tvInfoEnderezo);
    }
}

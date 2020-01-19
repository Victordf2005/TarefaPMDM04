package adaptadores;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import BaseDatos.Pedido;
import tenda.tarefa03.R;

public class RecyclerViewAdapter_Pedidos extends RecyclerView.Adapter {

    private ArrayList<Pedido> pedidos;

    public RecyclerViewAdapter_Pedidos(ArrayList<Pedido> pedidos){
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater mInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = mInflater.inflate(R.layout.activity_ver_pedidos_card,viewGroup,false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder_Pedidos(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder_Pedidos vhPedidos = (ViewHolder_Pedidos) holder;
        vhPedidos.tvInfoProduto.setText(pedidos.get(position).getCantidade() + "\n" + pedidos.get(position).getProduto());
        vhPedidos.tvInfoEnderezo.setText(pedidos.get(position).getEnderezo() + "\n" + pedidos.get(position).getCodpostal() + " " + pedidos.get(position).getCidade());
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

}

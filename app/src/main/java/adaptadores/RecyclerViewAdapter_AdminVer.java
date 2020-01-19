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

public class RecyclerViewAdapter_AdminVer extends RecyclerView.Adapter {

    private ArrayList<Pedido> pedidos = new ArrayList<Pedido>();

    public RecyclerViewAdapter_AdminVer(ArrayList<Pedido> pedidos){
        this.pedidos = pedidos;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater mInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = mInflater.inflate(R.layout.activity_administrador_cardver,viewGroup,false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder_AdminVer(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder_AdminVer vhPedidos = (ViewHolder_AdminVer) holder;
        vhPedidos.tvCliente.setText("Cliente " + String.valueOf(pedidos.get(position).getIdCliente()));
        vhPedidos.tvInfoProduto.setText("Cant. " + pedidos.get(position).getCantidade() + "\n" + "Prod. :" + pedidos.get(position).getProduto());
        vhPedidos.tvInfoEnderezo.setText("Envíar a: \n" + pedidos.get(position).getEnderezo() + "\n" + pedidos.get(position).getCodpostal() + " " + pedidos.get(position).getCidade());
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

}

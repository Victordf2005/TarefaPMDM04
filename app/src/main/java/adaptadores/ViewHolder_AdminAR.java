package adaptadores;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import BaseDatos.BDTendaVDF;
import BaseDatos.Pedido;
import tenda.tarefa03.R;

public class ViewHolder_AdminAR extends RecyclerView.ViewHolder {

    public BDTendaVDF baseDatos;
    Pedido pedido;

    public TextView tvCliente;
    public TextView tvInfoProduto;
    public TextView tvInfoEnderezo;
    public Button btAceptar;
    public Button btRexeitar;

    public ViewHolder_AdminAR(@NonNull View itemView) {
        super(itemView);

        tvCliente = itemView.findViewById(R.id.tvAdmCliente);
        tvInfoProduto = itemView.findViewById(R.id.tvInfoAdmProduto);
        tvInfoEnderezo = itemView.findViewById(R.id.tvInfoAdmEnderezo);
        btAceptar = itemView.findViewById(R.id.btAceptarPedido);
        btRexeitar = itemView.findViewById(R.id.btRexeitarPedido);

        btAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedido = (Pedido) v.getTag();
                Toast.makeText(v.getContext(),"Aceptar elemento " + getAdapterPosition() + " da lista.\n Obxecto pedido: " + pedido,Toast.LENGTH_SHORT).show();
            }
        });


        btRexeitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedido = (Pedido) v.getTag();
                Toast.makeText(v.getContext(),"Rexeitar elemento " + getAdapterPosition() + " da lista.\n Obxecto pedido: " + pedido,Toast.LENGTH_SHORT).show();
            }
        });
    }


}

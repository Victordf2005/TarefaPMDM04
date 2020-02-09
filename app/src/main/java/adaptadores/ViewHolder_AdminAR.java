package adaptadores;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import BaseDatos.BDTendaVDF;
import BaseDatos.Pedido;
import tenda.tarefa04.Administrador_VerPedidosAR;
import tenda.tarefa04.R;


public class ViewHolder_AdminAR extends RecyclerView.ViewHolder {

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

                BDTendaVDF baseDatos = new BDTendaVDF(v.getContext());
                baseDatos.abrirBD();

                pedido = (Pedido) ViewHolder_AdminAR.super.itemView.getTag();
                int rexistrosAfectados = baseDatos.actualizarEstadoPedido(pedido.getCodigo(), "A");     // Aceptar pedido

                if (rexistrosAfectados == 1) {
                    Toast.makeText(v.getContext(), "Pedido aceptado.", Toast.LENGTH_LONG).show();
                    Administrador_VerPedidosAR.notificarItemRemoved(getAdapterPosition());
                } else {
                    Toast.makeText(v.getContext(), "Erro tratando de aceptar o pedido.", Toast.LENGTH_LONG).show();
                }
                baseDatos.pecharBD();
            }
        });


        btRexeitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BDTendaVDF baseDatos = new BDTendaVDF(v.getContext());
                baseDatos.abrirBD();

                pedido = (Pedido) ViewHolder_AdminAR.super.itemView.getTag();
                int rexistrosAfectados = baseDatos.actualizarEstadoPedido(pedido.getCodigo(), "R");     // Aceptar pedido

                if (rexistrosAfectados == 1) {
                    Toast.makeText(v.getContext(), "Pedido rexeitado.", Toast.LENGTH_LONG).show();
                    Administrador_VerPedidosAR.notificarItemRemoved(getAdapterPosition());
                } else {
                    Toast.makeText(v.getContext(), "Erro tratando de rexeitar o pedido.", Toast.LENGTH_LONG).show();
                }
                baseDatos.pecharBD();
            }
        });
    }

}

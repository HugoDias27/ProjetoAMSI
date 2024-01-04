package pt.ipleiria.estg.dei.carolo_farmaceutica.adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pt.ipleiria.estg.dei.carolo_farmaceutica.R;
import pt.ipleiria.estg.dei.carolo_farmaceutica.listeners.QuantidadeAlteradaListener;
import pt.ipleiria.estg.dei.carolo_farmaceutica.listeners.SubtotalListener;
import pt.ipleiria.estg.dei.carolo_farmaceutica.modelo.LinhaCarrinhoCompra;
import pt.ipleiria.estg.dei.carolo_farmaceutica.modelo.Medicamento;
import pt.ipleiria.estg.dei.carolo_farmaceutica.modelo.SingletonGestorFarmacia;

public class ListaCarrinhoAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<LinhaCarrinhoCompra> linhacarrinhoCompras;

    private ImageButton btnAdicionar;
    private ImageButton btnReduzir;
    private ImageButton btnRemover;
    private EditText etQuantidade;
    private TextView tvPreco;
    private ArrayList<EditText> listaEditTextQuantidade = new ArrayList<>();
    private QuantidadeAlteradaListener quantidadeAlteradaListener;

    public void setQuantidadeAlteradaListener(QuantidadeAlteradaListener quantidadeAlteradaListener) {
        this.quantidadeAlteradaListener = quantidadeAlteradaListener;
    }

    public ListaCarrinhoAdaptador(Context context, ArrayList<LinhaCarrinhoCompra> linhacarrinhoCompras) {
        this.context = context;
        this.linhacarrinhoCompras = linhacarrinhoCompras;
    }

    @Override
    public int getCount() {
        return linhacarrinhoCompras.size();
    }

    @Override
    public Object getItem(int i) {
        return linhacarrinhoCompras.get(i);
    }

    @Override
    public long getItemId(int i) {
        return linhacarrinhoCompras.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.item_lista_carrinho, null);

        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(linhacarrinhoCompras.get(position));

        final ViewHolderLista finalViewHolder = viewHolder;

        btnAdicionar = view.findViewById(R.id.btAdicionar);
        tvPreco = view.findViewById(R.id.tvPreco);
        etQuantidade = view.findViewById(R.id.etQuantidade);

        if (listaEditTextQuantidade.size() <= position) {
            listaEditTextQuantidade.add(position, etQuantidade);
        } else {
            listaEditTextQuantidade.set(position, etQuantidade);
        }


        // Configurar o OnClickListener do botão "Adicionar"
        viewHolder.btAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinhaCarrinhoCompra linhaAtual = linhacarrinhoCompras.get(position);
                int quantidadeAtual = linhaAtual.getQuantidade();
                int novaQuantidade = quantidadeAtual + 1;

                linhaAtual.setQuantidade(novaQuantidade);

                finalViewHolder.tvQuantidade.setText(String.valueOf(novaQuantidade));

                SingletonGestorFarmacia.getInstance(context.getApplicationContext()).QuantidadeProduto(linhacarrinhoCompras.get(position).getId(), context);

                SharedPreferences preferences = context.getSharedPreferences("QUANTIDADE_PRODUTO", Context.MODE_PRIVATE);
                int produtoQuantidade = preferences.getInt("quantidade", 0);
                int QuantidadeLinhaProduto = preferences.getInt("quantidadelinha", 0);

                int quantidadeFinal = QuantidadeLinhaProduto + produtoQuantidade;

                if (novaQuantidade <= quantidadeFinal) {
                    SingletonGestorFarmacia.getInstance(context.getApplicationContext()).updateQuantidadeProdutoCarrinho(novaQuantidade, linhacarrinhoCompras.get(position).getId(), context);

                    double novoPreco = linhacarrinhoCompras.get(position).getValorcomiva() * novaQuantidade;
                    String novoPrecoFormatado = String.format("%.2f€", novoPreco);

                    linhaAtual.setSubtotal(novoPreco);

                    finalViewHolder.tvPreco.setText(novoPrecoFormatado);

                    if (quantidadeAlteradaListener != null) {
                        quantidadeAlteradaListener.onQuantidadeAlterada();
                    }

                } else {
                    Toast.makeText(context, "Não é possível adicionar mais quantidade!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReduzir = view.findViewById(R.id.btReduzir);

        viewHolder.btReduzir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinhaCarrinhoCompra linhaAtual = linhacarrinhoCompras.get(position);
                int quantidadeAtual = linhaAtual.getQuantidade();
                int novaQuantidade = quantidadeAtual - 1;

                linhaAtual.setQuantidade(novaQuantidade);

                finalViewHolder.tvQuantidade.setText(String.valueOf(novaQuantidade));

                if (quantidadeAtual != 0) {
                    SingletonGestorFarmacia.getInstance(context.getApplicationContext()).updateQuantidadeProdutoCarrinho(novaQuantidade, linhacarrinhoCompras.get(position).getId(), context);

                    double novoPreco = linhacarrinhoCompras.get(position).getValorcomiva() * novaQuantidade;
                    String novoPrecoFormatado = String.format("%.2f€", novoPreco);

                    linhaAtual.setSubtotal(novoPreco);

                    finalViewHolder.tvPreco.setText(novoPrecoFormatado);

                    if (quantidadeAlteradaListener != null) {
                        quantidadeAlteradaListener.onQuantidadeAlterada();
                    }

                } else {
                    Toast.makeText(context, "Não é possível reduzir mais a quantidade!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.tvPreco.setText(String.format("%.2f€", linhacarrinhoCompras.get(position).getSubtotal()) + "€");

        btnRemover = view.findViewById(R.id.btRemover);
        viewHolder.btRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionToRemove = position; // Captura a posição atual

                if (positionToRemove >= 0 && positionToRemove < linhacarrinhoCompras.size()) {
                    int linhaIdToRemove = linhacarrinhoCompras.get(positionToRemove).getId();

                    linhacarrinhoCompras.remove(positionToRemove);

                    notifyDataSetChanged();

                    SingletonGestorFarmacia.getInstance(context.getApplicationContext()).deleleLinhaCarrinho(linhaIdToRemove, context);
                    Toast.makeText(context, "Item removido do carrinho com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private class ViewHolderLista {

        private TextView tvProduto, tvPreco;
        private EditText tvQuantidade;
        private ImageButton btReduzir, btAdicionar, btRemover;
        private ImageView imgProduto;

        public ViewHolderLista(View view) {
            tvProduto = view.findViewById(R.id.tvProduto);
            tvPreco = view.findViewById(R.id.tvPreco);
            tvQuantidade = view.findViewById(R.id.etQuantidade);
            btReduzir = view.findViewById(R.id.btReduzir);
            btAdicionar = view.findViewById(R.id.btAdicionar);
            btRemover = view.findViewById(R.id.btRemover);
            imgProduto = view.findViewById(R.id.imgProduto);
        }

        public void update(LinhaCarrinhoCompra linhaCarrinhoCompra) {
            tvProduto.setText(linhaCarrinhoCompra.getProduto_id());
            tvPreco.setText(linhaCarrinhoCompra.getSubtotal() + "");
            tvQuantidade.setText(linhaCarrinhoCompra.getQuantidade() + "");
            Glide.with(context)
                    .load(linhaCarrinhoCompra.getImagem())
                    .placeholder(R.drawable.medicamento_stock)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProduto);
        }
    }
}
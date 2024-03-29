package pt.ipleiria.estg.dei.carolo_farmaceutica;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.carolo_farmaceutica.adaptadores.ListaFaturaAdaptador;
import pt.ipleiria.estg.dei.carolo_farmaceutica.adaptadores.ListaLinhaFaturaAdaptador;
import pt.ipleiria.estg.dei.carolo_farmaceutica.listeners.FaturaCarrinhoListener;
import pt.ipleiria.estg.dei.carolo_farmaceutica.modelo.LinhaCarrinhoCompra;
import pt.ipleiria.estg.dei.carolo_farmaceutica.modelo.LinhaCarrinhoCompraFatura;
import pt.ipleiria.estg.dei.carolo_farmaceutica.modelo.SingletonGestorFarmacia;

public class FaturaLinhasFragment extends Fragment implements FaturaCarrinhoListener {

    // Declaração de variáveis
    private ListView lvLinhasFatura;
    private int idFatura;

    // Construtor
    public FaturaLinhasFragment() {
    }

    // Método que recebe o id da fatura da selecionada
    public void setIdFatura(int idFatura) {
        this.idFatura = idFatura;
    }

    // Método para carregar o fragmento das linhas da fatura
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_fatura_linhas, container, false);
        setHasOptionsMenu(true);
        lvLinhasFatura = view.findViewById(R.id.lvLinhasFatura);

        if (idFatura != 0) {
            SingletonGestorFarmacia.getInstance(getContext()).setFaturaCarrinhoListener(this);
            SingletonGestorFarmacia.getInstance(getContext()).getLinhasCarrinhoCompraFatura(idFatura, getContext());
        }

        return view;
    }

    // Método para carregar as linhas da fatura
    @Override
    public void onLinhasCarrinhoCarregadas(ArrayList<LinhaCarrinhoCompraFatura> linhas) {
        if (linhas != null) {
            lvLinhasFatura.setAdapter(new ListaLinhaFaturaAdaptador(getContext(), linhas));
        }
    }
}


package pt.ipleiria.estg.dei.carolo_farmaceutica.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.carolo_farmaceutica.modelo.LinhaCarrinhoCompra;
import pt.ipleiria.estg.dei.carolo_farmaceutica.modelo.LinhaCarrinhoCompraFatura;

public interface FaturaCarrinhoListener {

    // Método que carrega as linhas do carrinho nos detalhes da fatura
    void onLinhasCarrinhoCarregadas(ArrayList<LinhaCarrinhoCompraFatura> linhas);
}

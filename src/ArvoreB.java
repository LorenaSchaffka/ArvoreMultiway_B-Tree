// a fazer na ArvoreB:
// [x] acharPosicao()   Chaves ordenadas;               Seção 3, p. 175
// [x] inserirNoNo()    Inserção ordenada;	            Seção 4, p. 177
// [x] removerDoNo()
// [x] buscar()         Busca por comparações e filhos;	Figura 3, p. 177
// [x]] inserir()         Falta Dividir nós após estouro
// [x] dividirNo()
// [] remover()


public class ArvoreB {
    Node raiz = null;
    final int k = 2;

    // acharPosicao()
    int acharPosicao(Node no, int valor) {
        int i = 0;

        while (i < no.quantidade && no.chaves[i] < valor) {
            i++;
        }
        return i;
    }

    // inserirNoNo()
    void inserirNoNo(Node no, int valor) {
        int posicao = acharPosicao(no, valor);

        for (int i = no.quantidade - 1; i >= posicao; i--) {
            no.chaves[i + 1] = no.chaves[i];
        }
        no.chaves[posicao] = valor;
        no.quantidade++;
    }

    // removerDoNo()
    void removerDoNo(Node no, int valor) {
        int posicao = acharPosicao(no, valor);

        if (posicao >= no.quantidade || no.chaves[posicao] != valor) {
            return;
        }
        for (int i =posicao; i < no.quantidade - 1; i++) {
            no.chaves[i] = no.chaves[i + 1];
        }
        no.quantidade--;
    }

    // buscar()
    boolean buscar(int valor) {
        Node atual = raiz;

        while (atual != null) {
            int posicao = acharPosicao(atual, valor);

            if (posicao < atual.quantidade && atual.chaves[posicao] == valor) {
                return true;
            }
            atual = atual.filhos[posicao];
        }
        return false;

    }

    // [] inserir()  incompleto falta o dividirNo pra fazer Dividir nós após estouro
    void inserir(int valor) {
        // verifica se ta vazio
        if (raiz == null) {
            raiz = new Node();
            inserirNoNo(raiz, valor);
            return;
        }
        //percurso
        Node atual = raiz;
        Node[] caminho = new Node[50];
        int[] posicoes = new int[50];
        int nivel = 0;
        int posicao;

        while (!atual.folha) {
            posicao = acharPosicao(atual, valor);
            if (posicao < atual.quantidade && atual.chaves[posicao] == valor) {
                return;
            }
            // Proteção contra estouro do vetor caminho ( caso limite de níveis seja excedido)
            if (nivel >= caminho.length) {
                return;
            }
            caminho[nivel] = atual;
            posicoes[nivel] = posicao;
            atual = atual.filhos[posicao];
            nivel++;
        }
            // Encontra a posição correta na folha
            posicao = acharPosicao(atual, valor);

            // precaução que impede a inserção de chaves duplicadas
            if (posicao < atual.quantidade && atual.chaves[posicao] == valor) {
                return;
        }
            inserirNoNo(atual, valor);

            while (atual.quantidade > 4) {
                if (nivel == 0) {
                    dividirNo(atual, null, 0);
                    return;
                }
                dividirNo(atual, caminho[nivel - 1], posicoes[nivel - 1]);
                atual = caminho[nivel - 1];
                nivel--;
            }
    }

    void dividirNo(Node no, Node pai, int posicaoNoPai) {
        Node novo = new Node();
        novo.folha = no.folha;
        int meio = no.chaves[2];

        novo.chaves[0] = no.chaves[3];
        novo.chaves[1] = no.chaves[4];
        novo.quantidade = 2;
        no.quantidade = 2;

        if (!no.folha) {
            novo.filhos[0] = no.filhos[3];
            novo.filhos[1] = no.filhos[4];
            novo.filhos[2] = no.filhos[5];
        }
        if (pai == null) {
            Node novaRaiz = new Node();
            novaRaiz.chaves[0] = meio;
            novaRaiz.filhos[0] = no;
            novaRaiz.filhos[1] = novo;
            novaRaiz.quantidade = 1;
            novaRaiz.folha = false;
            raiz = novaRaiz;
            return;
        }

        for (int i = pai.quantidade - 1; i >= posicaoNoPai; i--) {
            pai.chaves[i + 1] = pai.chaves[i];
            pai.filhos[i + 2] = pai.filhos[i + 1];
        }
        pai.chaves[posicaoNoPai] = meio;
        pai.filhos[posicaoNoPai + 1] = novo;
        pai.quantidade++;
    }
}
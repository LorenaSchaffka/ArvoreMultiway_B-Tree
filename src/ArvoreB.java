// Implementação baseada em:
// BAYER, R.; McCREIGHT, E. Organization and maintenance of large ordered indexes.
// Acta Informatica, v. 1, n. 3, p. 173-189, 1972.
// [x] acharPosicao()   Chaves ordenadas;               Seção 3, p. 175
// [x] inserirNoNo()    Inserção ordenada;	            Seção 4, p. 177
// [x] removerDoNo()    Remoção ordenada;	            Seção 6, Fig. 6, p. 181
// [x] buscar()         Busca por comparações e filhos;	Figura 3, p. 177
// [x] inserir()        Inserção com divisão;           Seção 4, Fig. 4, p. 177-178
// [x] dividirNo()      Splitting a page;               Seção 4, p. 177-178
// [x] remover()        Catenation e underflow;         Seção 6, Fig. 6, p. 180-182; raiz: Seção 1, p. 174public class ArvoreB {
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
        for (int i = posicao; i < no.quantidade - 1; i++) {
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

    void inserir(int valor) {
        // verifica se ta vazio
        if (raiz == null) {
            raiz = new Node();
            inserirNoNo(raiz, valor);
            return;
        }
        // percurso
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
            // Proteção contra estouro do vetor caminho ( caso limite de níveis seja
            // excedido)
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

    void remover(int valor) {
        if (raiz == null) {
            return;
        }
        Node atual = raiz;
        Node[] caminho = new Node[50];
        int[] posicoes = new int[50];
        int nivel = 0;
        int posicao = 0;

        boolean achou = false;

        while (!achou) {
            posicao = acharPosicao(atual, valor);
            if (posicao < atual.quantidade && atual.chaves[posicao] == valor) {
                achou = true;
            } else if (atual.folha) {
                return;
            } else {
                caminho[nivel] = atual;
                posicoes[nivel] = posicao;
                atual = atual.filhos[posicao];
                nivel++;
            }
        }

        if (atual.folha) {
            removerDoNo(atual, valor);
        }

        else {
            Node noDaChave = atual;
            caminho[nivel] = atual;
            posicoes[nivel] = posicao + 1;
            atual = atual.filhos[posicao + 1];
            nivel++;

            while (!atual.folha) {
                caminho[nivel] = atual;
                posicoes[nivel] = 0;
                atual = atual.filhos[0];
                nivel++;
            }
            noDaChave.chaves[posicao] = atual.chaves[0];
            removerDoNo(atual, atual.chaves[0]);
        }

        while (nivel > 0 && atual.quantidade < k) {
            Node pai = caminho[nivel - 1];
            int posicaoNoPai = posicoes[nivel - 1];
            Node esquerdo;
            Node direito;
            int separador;

            if (posicaoNoPai > 0) {
                esquerdo = pai.filhos[posicaoNoPai - 1];
                direito = atual;
                separador = posicaoNoPai - 1;
            } else {
                esquerdo = atual;
                direito = pai.filhos[posicaoNoPai + 1];
                separador = 0;
            }

            if (esquerdo.quantidade + direito.quantidade >= 2 * k) {
                int[] chavesTemp = new int[6];
                Node[] filhosTemp = new Node[7];
                int total = 0;

                for (int i = 0; i < esquerdo.quantidade; i++) {
                    chavesTemp[total] = esquerdo.chaves[i];
                    total++;
                }

                chavesTemp[total] = pai.chaves[separador];
                total++;

                for (int i = 0; i < direito.quantidade; i++) {
                    chavesTemp[total] = direito.chaves[i];
                    total++;
                }

                int totalFilhos = 0;

                for (int i = 0; i <= esquerdo.quantidade; i++) {
                    filhosTemp[totalFilhos] = esquerdo.filhos[i];
                    totalFilhos++;
                }

                for (int i = 0; i <= direito.quantidade; i++) {
                    filhosTemp[totalFilhos] = direito.filhos[i];
                    totalFilhos++;
                }

                int meio = total / 2;

                for (int i = 0; i < meio; i++) {
                    esquerdo.chaves[i] = chavesTemp[i];
                }

                for (int i = 0; i <= meio; i++) {
                    esquerdo.filhos[i] = filhosTemp[i];
                }

                esquerdo.quantidade = meio;
                pai.chaves[separador] = chavesTemp[meio];

                for (int i = 0; i < total - meio - 1; i++) {
                    direito.chaves[i] = chavesTemp[meio + 1 + i];
                }

                for (int i = 0; i <= total - meio - 1; i++) {
                    direito.filhos[i] = filhosTemp[meio + 1 + i];
                }

                direito.quantidade = total - meio - 1;
            }
            else {
                esquerdo.chaves[esquerdo.quantidade] = pai.chaves[separador];
                for (int i = 0; i < direito.quantidade; i++) {
                    esquerdo.chaves[esquerdo.quantidade + 1 + i] = direito.chaves[i];
                }

                for (int i = 0; i <= direito.quantidade; i++) {
                    esquerdo.filhos[esquerdo.quantidade + 1 + i] = direito.filhos[i];
                }

                esquerdo.quantidade = esquerdo.quantidade + 1 + direito.quantidade;

                for (int i = separador; i < pai.quantidade - 1; i++) {
                    pai.chaves[i] = pai.chaves[i + 1];
                    pai.filhos[i + 1] = pai.filhos[i + 2];
                }
                pai.quantidade--;
            }
            atual = pai;
            nivel--;
        }
        if (raiz.quantidade == 0) {
            if (raiz.folha) {
                raiz = null;
            } else {
                raiz = raiz.filhos[0];
            }
        }
    }

    void imprimir() {
        if (raiz == null) {
            System.out.println("Árvore vazia.");
            return;
        }
        Node[] nivelAtual = new Node[100];
        nivelAtual[0] = raiz;
        int quantidadeAtual = 1;
        int numeroNivel = 0;

        while (quantidadeAtual > 0) {
            Node[] proximoNivel = new Node[100];
            int quantidadeProximoNivel = 0;
            System.out.print("Nivel " + numeroNivel + ": ");

            for (int i = 0; i < quantidadeAtual; i++) {
                Node no = nivelAtual[i];
                System.out.print("[");
                for (int j = 0; j < no.quantidade; j++) {
                    System.out.print(no.chaves[j] + " ");
                }
                System.out.print("] ");
                if (!no.folha) {
                    for (int j = 0; j <= no.quantidade; j++) {
                        proximoNivel[quantidadeProximoNivel] = no.filhos[j];
                        quantidadeProximoNivel++;
                    }
                }
            }
            System.out.println();
            nivelAtual = proximoNivel;
            quantidadeAtual = quantidadeProximoNivel;
            numeroNivel++;
        }
    }
}
public class Node {
    int[] chaves;
    Node[] filhos;
    int quantidade;
    boolean folha;

Node() {
    chaves = new int[5];
    filhos = new Node[6];
    quantidade = 0;
    folha = true;
    }
}
// regras da arvore, usando o artigo como parametro:
// k                                    = 2
// Mínimo de chaves por nó não raiz     = 2
// Máximo de chaves por nó              = 4 ; Seção 3
// durante o estouro	                = 5
// Máximo de filhos normalmente 	    = 5
// Filhos durante o estouro 	        = 6
// Chave promovida na divisão         	= Índice 2
// Crescimento da árvore    	        = Nova raiz após divisão da raiz
// Testes e demonstração
public class Main {
    public static void main(String[] args) {
        ArvoreB arvore = new ArvoreB();

        arvore.inserir(10);
        arvore.inserir(20);
        arvore.inserir(30);
        arvore.inserir(40);
        arvore.inserir(50);

        arvore.imprimir();

        arvore.inserir(60);
        arvore.inserir(70);
        arvore.inserir(80);

        arvore.imprimir();

        System.out.println("Buscar 40: " + arvore.buscar(40));
        System.out.println("Buscar 99: " + arvore.buscar(99));

        arvore.remover(50);
        arvore.imprimir();

        arvore.remover(80);
        arvore.imprimir();

        arvore.remover(40);
        arvore.imprimir();

        arvore.remover(70);
        arvore.imprimir();
    }
}
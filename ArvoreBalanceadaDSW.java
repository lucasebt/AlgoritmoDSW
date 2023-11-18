
import java.util.Random;


public class ArvoreBalanceadaDSW {
  private static ArvoreNo raiz;

    public static void main(String[] args) {
        // 1) Criar uma árvore com 100 números aleatórios de 0 a 100
        for (int i = 0; i < 100; i++) {
            int numeroAleatorio = new Random().nextInt(101);
            raiz = inserir(raiz, numeroAleatorio);
        }

        // 2) Adicionar 20 números à árvore
        for (int i = 0; i < 20; i++) {
            int numeroAdicional = new Random().nextInt(101);
            raiz = inserir(raiz, numeroAdicional);
        }

        // 3) Balancear a árvore usando o algoritmo DSW
        raiz = balancearDSW(raiz);

        // Agora a árvore está balanceada
    }

    // Função para inserir um novo nó na árvore
    private static ArvoreNo inserir(ArvoreNo no, int chave) {
        if (no == null) {
            return new ArvoreNo(chave);
        }

        if (chave < no.chave) {
            no.esquerda = inserir(no.esquerda, chave);
        } else if (chave > no.chave) {
            no.direita = inserir(no.direita, chave);
        }

        return no;
    }

    // Função para realizar a rotação direita
    private static ArvoreNo rotacionarDireita(ArvoreNo raiz) {
        ArvoreNo novaRaiz = raiz.esquerda;
        raiz.esquerda = novaRaiz.direita;
        novaRaiz.direita = raiz;
        return novaRaiz;
    }

    // Função para realizar a compressão para a esquerda
    private static ArvoreNo comprimir(ArvoreNo raiz, int count) {
    ArvoreNo scanner = raiz;
    for (int i = 0; i < count && scanner != null; i++) {
        ArvoreNo filho = scanner.direita;
        if (filho != null) {
            scanner.direita = filho.direita;
            scanner = scanner.direita;
            if (scanner != null) {
                filho.direita = scanner.esquerda;
                scanner.esquerda = filho;
            }
        } else {
            break; // Se filho for null, interrompe o loop
        }
    }
    return raiz;
}

    // Função para balancear a árvore usando o algoritmo DSW
    private static ArvoreNo balancearDSW(ArvoreNo raiz) {
        int tamanho = contarNos(raiz);

        // Passo 1: Transformar a árvore em uma "vine"
        raiz = criarVine(raiz);

        // Passo 2: Balancear a "vine"
        raiz = balancearVine(raiz, tamanho);

        return raiz;
    }

    // Função para contar o número de nós na árvore
    private static int contarNos(ArvoreNo raiz) {
        if (raiz == null) {
            return 0;
        }
        return 1 + contarNos(raiz.esquerda) + contarNos(raiz.direita);
    }

    // Função para transformar a árvore em uma "vine"
    private static ArvoreNo criarVine(ArvoreNo raiz) {
        ArvoreNo pontaVine = raiz;
        ArvoreNo resto = pontaVine.direita;

        while (resto != null) {
            if (resto.esquerda == null) {
                pontaVine = resto;
                resto = resto.direita;
            } else {
                ArvoreNo temp = resto.esquerda;
                resto.esquerda = temp.direita;
                temp.direita = resto;
                resto = temp;
                pontaVine.direita = temp;
            }
        }
        return raiz;
    }

    // Função para balancear a "vine"
    private static ArvoreNo balancearVine(ArvoreNo raiz, int tamanho) {
        int numFolhas = tamanho + 1 - Integer.highestOneBit(tamanho);
        comprimir(raiz, numFolhas);

        while (tamanho > 1) {
            tamanho /= 2;
            comprimir(raiz, tamanho);
        }

        return raiz;
    }
}
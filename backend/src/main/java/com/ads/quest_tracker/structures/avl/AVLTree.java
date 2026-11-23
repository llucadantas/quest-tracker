package com.ads.quest_tracker.structures.avl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * Implementação genérica de uma Árvore AVL auto-balanceada e independente de domínio.
 *
 * <p>A Árvore AVL (Adelson-Velsky e Landis) é uma árvore binária de busca auto-balanceada
 * onde a diferença de altura entre as subárvores esquerda e direita de qualquer nó
 * (o fator de balanceamento) nunca excede 1 em valor absoluto.</p>
 *
 * <p><strong>Principais Características:</strong></p>
 * <ul>
 *   <li>Operações de busca, inserção e remoção em tempo garantido O(log n).</li>
 *   <li>Suporte a tipos {@link Comparable} e comparadores customizados {@link Comparator}.</li>
 *   <li>Política estrita de chaves únicas (rejeição de duplicados).</li>
 *   <li>Mecanismos de rotações manuais: LL, RR, LR e RL.</li>
 *   <li>Percursos tradicionais: In-Order, Pre-Order, Post-Order e Level-Order.</li>
 *   <li>Operações de consulta de extremos (mínimo, máximo, raiz e elementos ordenados).</li>
 * </ul>
 *
 * @param <T> o tipo dos elementos armazenados na árvore.
 */
public class AVLTree<T> {

    private AVLNode<T> root;
    private int size;
    private final Comparator<? super T> comparator;

    /**
     * Flag interna para rastrear se a operação recursiva mais recente alterou a árvore.
     */
    private boolean lastOperationSuccess;

    /**
     * Construtor padrão que utiliza a ordem natural dos elementos.
     * Os elementos inseridos devem implementar {@link Comparable}.
     */
    @SuppressWarnings("unchecked")
    public AVLTree() {
        this((a, b) -> {
            if (!(a instanceof Comparable)) {
                throw new ClassCastException("O elemento fornecido não implementa Comparable e nenhum Comparator foi definido.");
            }
            return ((Comparable<? super T>) a).compareTo(b);
        });
    }

    /**
     * Construtor que utiliza um {@link Comparator} customizado para ordenação dos elementos.
     *
     * @param comparator comparador a ser utilizado para ordenação e busca dos nós.
     * @throws NullPointerException se o comparador for nulo.
     */
    public AVLTree(Comparator<? super T> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "O comparador não pode ser nulo.");
        this.root = null;
        this.size = 0;
    }

    /**
     * Insere um novo elemento na árvore mantendo as propriedades de ordenação.
     *
     * @param element o elemento a ser adicionado.
     * @return {@code true} se o elemento foi inserido com sucesso; {@code false} se já existir na árvore.
     * @throws NullPointerException se o elemento for nulo.
     */
    public boolean insert(T element) {
        Objects.requireNonNull(element, "O elemento a ser inserido não pode ser nulo.");
        if (root == null) {
            // Valida compatibilidade do tipo com o critério de comparação mesmo no primeiro elemento
            comparator.compare(element, element);
        }
        lastOperationSuccess = false;
        root = insertRecursive(root, element);
        if (lastOperationSuccess) {
            size++;
        }
        return lastOperationSuccess;
    }

    private AVLNode<T> insertRecursive(AVLNode<T> node, T element) {
        if (node == null) {
            lastOperationSuccess = true;
            return new AVLNode<>(element);
        }

        int cmp = comparator.compare(element, node.getValue());
        if (cmp < 0) {
            node.setLeft(insertRecursive(node.getLeft(), element));
        } else if (cmp > 0) {
            node.setRight(insertRecursive(node.getRight(), element));
        } else {
            // Política de duplicados: rejeição
            lastOperationSuccess = false;
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

    /**
     * Busca um elemento na árvore que coincida com o critério de comparação do valor fornecido.
     *
     * @param element o elemento ou chave a ser buscado.
     * @return o elemento armazenado correspondente, ou {@code null} se não for localizado.
     * @throws NullPointerException se o elemento for nulo.
     */
    public T search(T element) {
        Objects.requireNonNull(element, "O elemento de busca não pode ser nulo.");
        AVLNode<T> foundNode = searchNode(root, element);
        return foundNode != null ? foundNode.getValue() : null;
    }

    private AVLNode<T> searchNode(AVLNode<T> current, T element) {
        while (current != null) {
            int cmp = comparator.compare(element, current.getValue());
            if (cmp < 0) {
                current = current.getLeft();
            } else if (cmp > 0) {
                current = current.getRight();
            } else {
                return current;
            }
        }
        return null;
    }

    /**
     * Verifica se um elemento existe na árvore.
     *
     * @param element o elemento a ser verificado.
     * @return {@code true} se o elemento estiver presente; {@code false} caso contrário.
     * @throws NullPointerException se o elemento for nulo.
     */
    public boolean contains(T element) {
        return search(element) != null;
    }

    /**
     * Remove um elemento da árvore.
     *
     * @param element o elemento a ser removido.
     * @return {@code true} se o elemento foi encontrado e removido; {@code false} caso contrário.
     * @throws NullPointerException se o elemento for nulo.
     */
    public boolean delete(T element) {
        Objects.requireNonNull(element, "O elemento a ser removido não pode ser nulo.");
        lastOperationSuccess = false;
        root = deleteRecursive(root, element);
        if (lastOperationSuccess) {
            size--;
        }
        return lastOperationSuccess;
    }

    /**
     * Alias para o método {@link #delete(Object)}.
     *
     * @param element o elemento a ser removido.
     * @return {@code true} se o elemento foi removido com sucesso; {@code false} caso contrário.
     */
    public boolean remove(T element) {
        return delete(element);
    }

    private AVLNode<T> deleteRecursive(AVLNode<T> node, T element) {
        if (node == null) {
            lastOperationSuccess = false;
            return null;
        }

        int cmp = comparator.compare(element, node.getValue());
        if (cmp < 0) {
            node.setLeft(deleteRecursive(node.getLeft(), element));
        } else if (cmp > 0) {
            node.setRight(deleteRecursive(node.getRight(), element));
        } else {
            lastOperationSuccess = true;

            // Caso 1: Nó folha ou Caso 2: Nó com apenas 1 filho
            if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            }

            // Caso 3: Nó com 2 filhos
            // Localiza o sucessor in-order (menor nó da subárvore direita)
            AVLNode<T> successor = findMinNode(node.getRight());
            node.setValue(successor.getValue());
            node.setRight(deleteRecursive(node.getRight(), successor.getValue()));
        }

        updateHeight(node);
        return balance(node);
    }

    /**
     * Encontra o nó com menor valor a partir de determinado nó.
     */
    private AVLNode<T> findMinNode(AVLNode<T> node) {
        AVLNode<T> current = node;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    /**
     * Retorna a altura da árvore.
     * Uma árvore vazia possui altura 0.
     *
     * @return a altura da árvore.
     */
    public int height() {
        return getNodeHeight(root);
    }

    /**
     * Retorna a quantidade total de elementos presentes na árvore.
     *
     * @return o número de elementos armazenados em tempo O(1).
     */
    public int size() {
        return size;
    }

    /**
     * Verifica se a árvore está vazia.
     *
     * @return {@code true} se não contiver elementos; {@code false} caso contrário.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Remove todos os nós da árvore, resetando a raiz e a contagem de elementos.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Retorna a altura de um nó específico, considerando nós nulos como altura 0.
     *
     * @param node nó a ser avaliado.
     * @return a altura do nó ou 0 se for nulo.
     */
    int getNodeHeight(AVLNode<T> node) {
        return node == null ? 0 : node.getHeight();
    }

    /**
     * Recalcula a altura de um nó em O(1) com base nas alturas dos seus filhos.
     *
     * @param node nó cuja altura deve ser atualizada.
     */
    void updateHeight(AVLNode<T> node) {
        if (node != null) {
            node.setHeight(1 + Math.max(getNodeHeight(node.getLeft()), getNodeHeight(node.getRight())));
        }
    }

    /**
     * Calcula o fator de balanceamento (FB) de um nó.
     * FB = height(left) - height(right).
     *
     * @param node nó cujo fator de balanceamento será calculado.
     * @return o fator de balanceamento do nó (0 se o nó for nulo).
     */
    int getBalanceFactor(AVLNode<T> node) {
        return node == null ? 0 : getNodeHeight(node.getLeft()) - getNodeHeight(node.getRight());
    }

    /**
     * Executa uma Rotação Simples à Direita (Caso LL - Left-Left).
     *
     * <p>Utilizada quando a subárvore esquerda está desbalanceada positivamente (FB > 1)
     * e a inserção/peso ocorreu na subárvore esquerda do filho esquerdo (FB(left) >= 0).</p>
     *
     * <pre>
     *        y                               x
     *       / \                             / \
     *      x   T3   -- rotateRight(y) -->  T1   y
     *     / \                                 / \
     *    T1  T2                              T2  T3
     * </pre>
     *
     * @param y nó desbalanceado raiz da subárvore.
     * @return a nova raiz da subárvore (nó x).
     */
    AVLNode<T> rotateRight(AVLNode<T> y) {
        AVLNode<T> x = y.getLeft();
        AVLNode<T> t2 = x.getRight();

        // Realiza rotação
        x.setRight(y);
        y.setLeft(t2);

        // Atualiza alturas (primeiro o nó rebaixado y, depois a nova raiz x)
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    /**
     * Executa uma Rotação Simples à Esquerda (Caso RR - Right-Right).
     *
     * <p>Utilizada quando a subárvore direita está desbalanceada negativamente (FB < -1)
     * e a inserção/peso ocorreu na subárvore direita do filho direito (FB(right) <= 0).</p>
     *
     * <pre>
     *      y                                x
     *     / \                              / \
     *    T1  x    -- rotateLeft(y) -->    y   T3
     *       / \                          / \
     *      T2  T3                       T1  T2
     * </pre>
     *
     * @param y nó desbalanceado raiz da subárvore.
     * @return a nova raiz da subárvore (nó x).
     */
    AVLNode<T> rotateLeft(AVLNode<T> y) {
        AVLNode<T> x = y.getRight();
        AVLNode<T> t2 = x.getLeft();

        // Realiza rotação
        x.setLeft(y);
        y.setRight(t2);

        // Atualiza alturas (primeiro o nó rebaixado y, depois a nova raiz x)
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    /**
     * Executa uma Rotação Dupla Esquerda-Direita (Caso LR - Left-Right).
     *
     * <p>Utilizada quando o nó está desbalanceado à esquerda (FB > 1), mas o filho esquerdo
     * pende para a direita (FB(left) < 0), formando um padrão zigue-zague.</p>
     *
     * <p>Consiste em uma rotação simples à esquerda no filho esquerdo, seguida por uma rotação
     * simples à direita no nó desbalanceado.</p>
     *
     * @param node nó desbalanceado.
     * @return a nova raiz da subárvore após o rebalanceamento duplo.
     */
    AVLNode<T> rotateLeftRight(AVLNode<T> node) {
        node.setLeft(rotateLeft(node.getLeft()));
        return rotateRight(node);
    }

    /**
     * Executa uma Rotação Dupla Direita-Esquerda (Caso RL - Right-Left).
     *
     * <p>Utilizada quando o nó está desbalanceado à direita (FB < -1), mas o filho direito
     * pende para a esquerda (FB(right) > 0), formando um padrão zigue-zague.</p>
     *
     * <p>Consiste em uma rotação simples à direita no filho direito, seguida por uma rotação
     * simples à esquerda no nó desbalanceado.</p>
     *
     * @param node nó desbalanceado.
     * @return a nova raiz da subárvore após o rebalanceamento duplo.
     */
    AVLNode<T> rotateRightLeft(AVLNode<T> node) {
        node.setRight(rotateRight(node.getRight()));
        return rotateLeft(node);
    }

    /**
     * Avalia e restaura o balanceamento AVL de determinado nó, se necessário.
     * Garante a invariante estrita da Árvore AVL: para todo nó u, -1 <= FB(u) <= 1.
     *
     * @param node nó a ser balanceado.
     * @return a raiz da subárvore balanceada.
     */
    protected AVLNode<T> balance(AVLNode<T> node) {
        if (node == null) {
            return null;
        }

        updateHeight(node);
        int balanceFactor = getBalanceFactor(node);

        // Caso LL (Left-Left): desbalanceamento à esquerda com subárvore esquerda dominante ou neutra
        if (balanceFactor > 1 && getBalanceFactor(node.getLeft()) >= 0) {
            return rotateRight(node);
        }

        // Caso LR (Left-Right): desbalanceamento à esquerda com subárvore direita dominante
        if (balanceFactor > 1 && getBalanceFactor(node.getLeft()) < 0) {
            return rotateLeftRight(node);
        }

        // Caso RR (Right-Right): desbalanceamento à direita com subárvore direita dominante ou neutra
        if (balanceFactor < -1 && getBalanceFactor(node.getRight()) <= 0) {
            return rotateLeft(node);
        }

        // Caso RL (Right-Left): desbalanceamento à direita com subárvore esquerda dominante
        if (balanceFactor < -1 && getBalanceFactor(node.getRight()) > 0) {
            return rotateRightLeft(node);
        }

        return node;
    }

    // =========================================================================
    // ETAPA 5 — Percursos e Operações Auxiliares
    // =========================================================================

    /**
     * Realiza o percurso Em-Ordem (In-Order: Esquerda, Raiz, Direita).
     * Em uma Árvore AVL, esse percurso visita todos os elementos em ordem crescente.
     *
     * @return lista imutável contendo os elementos na ordem do percurso.
     */
    public List<T> inOrder() {
        List<T> result = new ArrayList<>(size);
        inOrderRecursive(root, result);
        return Collections.unmodifiableList(result);
    }

    private void inOrderRecursive(AVLNode<T> node, List<T> result) {
        if (node != null) {
            inOrderRecursive(node.getLeft(), result);
            result.add(node.getValue());
            inOrderRecursive(node.getRight(), result);
        }
    }

    /**
     * Realiza o percurso Pré-Ordem (Pre-Order: Raiz, Esquerda, Direita).
     * Útil para serialização e reconstrução estrutural da árvore.
     *
     * @return lista imutável contendo os elementos na ordem do percurso.
     */
    public List<T> preOrder() {
        List<T> result = new ArrayList<>(size);
        preOrderRecursive(root, result);
        return Collections.unmodifiableList(result);
    }

    private void preOrderRecursive(AVLNode<T> node, List<T> result) {
        if (node != null) {
            result.add(node.getValue());
            preOrderRecursive(node.getLeft(), result);
            preOrderRecursive(node.getRight(), result);
        }
    }

    /**
     * Realiza o percurso Pós-Ordem (Post-Order: Esquerda, Direita, Raiz).
     * Útil para deleção bottom-up ou avaliação de nós filhos antes do pai.
     *
     * @return lista imutável contendo os elementos na ordem do percurso.
     */
    public List<T> postOrder() {
        List<T> result = new ArrayList<>(size);
        postOrderRecursive(root, result);
        return Collections.unmodifiableList(result);
    }

    private void postOrderRecursive(AVLNode<T> node, List<T> result) {
        if (node != null) {
            postOrderRecursive(node.getLeft(), result);
            postOrderRecursive(node.getRight(), result);
            result.add(node.getValue());
        }
    }

    /**
     * Realiza o percurso em Nível / Largura (Level-Order / BFS).
     * Visita os nós nível por nível, da esquerda para a direita.
     *
     * @return lista imutável contendo os elementos na ordem do percurso.
     */
    public List<T> levelOrder() {
        List<T> result = new ArrayList<>(size);
        if (root == null) {
            return Collections.unmodifiableList(result);
        }

        Queue<AVLNode<T>> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            AVLNode<T> current = queue.poll();
            result.add(current.getValue());

            if (current.getLeft() != null) {
                queue.offer(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.offer(current.getRight());
            }
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Retorna o menor elemento presente na árvore.
     *
     * @return o elemento mínimo, ou {@code null} se a árvore estiver vazia.
     */
    public T min() {
        if (root == null) {
            return null;
        }
        return findMinNode(root).getValue();
    }

    /**
     * Retorna o maior elemento presente na árvore.
     *
     * @return o elemento máximo, ou {@code null} se a árvore estiver vazia.
     */
    public T max() {
        if (root == null) {
            return null;
        }
        return findMaxNode(root).getValue();
    }

    private AVLNode<T> findMaxNode(AVLNode<T> node) {
        AVLNode<T> current = node;
        while (current.getRight() != null) {
            current = current.getRight();
        }
        return current;
    }

    /**
     * Obtém o valor armazenado no nó raiz sem expor referências internas de nós.
     *
     * @return o valor do elemento na raiz, ou {@code null} se a árvore estiver vazia.
     */
    public T getRootValue() {
        return root != null ? root.getValue() : null;
    }

    /**
     * Retorna todos os elementos da árvore ordenados de acordo com o critério de comparação.
     *
     * <p>Equivale semanticamente ao percurso {@link #inOrder()}, retornando uma visualização
     * imutável e ordenada dos dados em tempo O(n).</p>
     *
     * @return lista imutável contendo todos os elementos em ordem crescente.
     */
    public List<T> getSortedElements() {
        return inOrder();
    }

    /**
     * Retorna a referência do nó raiz (visibilidade restrita ao pacote para testes estruturais).
     *
     * @return o nó raiz da árvore.
     */
    AVLNode<T> getRoot() {
        return root;
    }
}

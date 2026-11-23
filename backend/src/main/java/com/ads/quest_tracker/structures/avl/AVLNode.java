package com.ads.quest_tracker.structures.avl;

import java.util.Objects;

/**
 * Representa a unidade fundamental (nó) de uma Árvore AVL.
 *
 * Cada nó encapsula o valor armazenado, referências aos filhos esquerdo e direito,
 * e a sua altura na árvore para cálculo do fator de balanceamento.
 *
 * @param <T> o tipo de dado armazenado no nó.
 */
public class AVLNode<T> {

    private T value;
    private AVLNode<T> left;
    private AVLNode<T> right;
    private int height;

    /**
     * Cria um novo nó folha contendo o valor especificado.
     * Na convenção canônica da Árvore AVL, um nó folha recém-criado possui altura 1.
     *
     * @param value o elemento a ser armazenado no nó.
     * @throws NullPointerException se o valor for nulo.
     */
    public AVLNode(T value) {
        this.value = Objects.requireNonNull(value, "O valor do nó não pode ser nulo.");
        this.left = null;
        this.right = null;
        this.height = 1;
    }

    /**
     * Cria um nó com valor e referências filhas especificadas.
     *
     * @param value o elemento a ser armazenado no nó.
     * @param left  referência para o filho esquerdo.
     * @param right referência para o filho direito.
     * @throws NullPointerException se o valor for nulo.
     */
    public AVLNode(T value, AVLNode<T> left, AVLNode<T> right) {
        this(value);
        this.left = left;
        this.right = right;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = Objects.requireNonNull(value, "O valor do nó não pode ser nulo.");
    }

    public AVLNode<T> getLeft() {
        return left;
    }

    public void setLeft(AVLNode<T> left) {
        this.left = left;
    }

    public AVLNode<T> getRight() {
        return right;
    }

    public void setRight(AVLNode<T> right) {
        this.right = right;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height < 1) {
            throw new IllegalArgumentException("A altura do nó deve ser pelo menos 1.");
        }
        this.height = height;
    }

    /**
     * Verifica se o nó atual é uma folha (não possui filhos).
     *
     * @return {@code true} se ambos os filhos forem nulos; {@code false} caso contrário.
     */
    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    /**
     * Verifica se o nó possui filho esquerdo.
     *
     * @return {@code true} se o filho esquerdo não for nulo; {@code false} caso contrário.
     */
    public boolean hasLeft() {
        return this.left != null;
    }

    /**
     * Verifica se o nó possui filho direito.
     *
     * @return {@code true} se o filho direito não for nulo; {@code false} caso contrário.
     */
    public boolean hasRight() {
        return this.right != null;
    }

    @Override
    public String toString() {
        return "AVLNode{" +
                "value=" + value +
                ", height=" + height +
                ", hasLeft=" + hasLeft() +
                ", hasRight=" + hasRight() +
                '}';
    }
}

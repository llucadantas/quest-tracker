package com.ads.quest_tracker.structures.linkedlist;

/**
 * Representa um nó atômico de uma Lista Simplesmente Encadeada.
 *
 * <p>Cada nó encapsula o valor armazenado do tipo genérico {@code T} e a referência
 * para o próximo nó da cadeia.</p>
 *
 * @param <T> o tipo de dado armazenado no nó.
 */
public class Node<T> {

    private T value;
    private Node<T> next;

    /**
     * Cria um novo nó contendo o valor especificado e sem sucessor.
     *
     * @param value o elemento a ser armazenado no nó.
     */
    public Node(T value) {
        this(value, null);
    }

    /**
     * Cria um novo nó contendo o valor e a referência para o próximo nó.
     *
     * @param value o elemento a ser armazenado no nó.
     * @param next  o próximo nó na lista encadeada.
     */
    public Node(T value, Node<T> next) {
        this.value = value;
        this.next = next;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    /**
     * Verifica se o nó possui um sucessor.
     *
     * @return {@code true} se houver um próximo nó; {@code false} caso contrário.
     */
    public boolean hasNext() {
        return this.next != null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", hasNext=" + hasNext() +
                '}';
    }
}

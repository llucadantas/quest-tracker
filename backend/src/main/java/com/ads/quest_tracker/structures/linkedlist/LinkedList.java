package com.ads.quest_tracker.structures.linkedlist;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementação de uma Lista Simplesmente Encadeada (Singly Linked List) genérica e independente de domínio.
 *
 * <p>A estrutura mantém ponteiros para a cabeça ({@code head}) e para a cauda ({@code tail}),
 * permitindo operações de inserção em ambas as extremidades em tempo constante O(1).</p>
 *
 * @param <T> o tipo dos elementos armazenados na lista.
 */
public class LinkedList<T> implements Iterable<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Cria uma lista encadeada inicialmente vazia.
     */
    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // =========================================================================
    // Operações de Inserção
    // =========================================================================

    /**
     * Adiciona um elemento ao final da lista encadeada (tempo O(1)).
     *
     * @param element o elemento a ser adicionado.
     */
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    /**
     * Adiciona um elemento no início da lista encadeada (tempo O(1)).
     *
     * @param element o elemento a ser adicionado.
     */
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setNext(head);
            head = newNode;
        }
        size++;
    }

    /**
     * Insere um elemento em uma posição específica da lista encadeada (tempo O(n)).
     *
     * @param index   a posição na qual o elemento deve ser inserido (0 <= index <= size).
     * @param element o elemento a ser inserido.
     * @throws IndexOutOfBoundsException se o índice for inválido.
     */
    public void add(int index, T element) {
        checkPositionIndex(index);

        if (index == 0) {
            addFirst(element);
            return;
        }

        if (index == size) {
            add(element);
            return;
        }

        Node<T> prev = getNode(index - 1);
        Node<T> newNode = new Node<>(element, prev.getNext());
        prev.setNext(newNode);
        size++;
    }

    // =========================================================================
    // Operações de Remoção
    // =========================================================================

    /**
     * Remove o primeiro elemento da lista encadeada (tempo O(1)).
     *
     * @return o elemento que foi removido.
     * @throws NoSuchElementException se a lista estiver vazia.
     */
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Não é possível remover de uma lista vazia.");
        }

        T removedValue = head.getValue();
        Node<T> nextNode = head.getNext();

        head.setValue(null); // auxílio ao Garbage Collector
        head.setNext(null);
        head = nextNode;

        size--;
        if (size == 0) {
            tail = null;
        }

        return removedValue;
    }

    /**
     * Remove o último elemento da lista encadeada (tempo O(n)).
     *
     * @return o elemento que foi removido.
     * @throws NoSuchElementException se a lista estiver vazia.
     */
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Não é possível remover de uma lista vazia.");
        }

        if (size == 1) {
            return removeFirst();
        }

        Node<T> prev = getNode(size - 2);
        T removedValue = tail.getValue();

        tail.setValue(null); // auxílio ao Garbage Collector
        tail.setNext(null);

        prev.setNext(null);
        tail = prev;
        size--;

        return removedValue;
    }

    /**
     * Remove o elemento localizado em determinado índice (tempo O(n)).
     *
     * @param index a posição do elemento a ser removido (0 <= index < size).
     * @return o elemento que foi removido.
     * @throws IndexOutOfBoundsException se o índice for inválido.
     */
    public T remove(int index) {
        checkElementIndex(index);

        if (index == 0) {
            return removeFirst();
        }

        if (index == size - 1) {
            return removeLast();
        }

        Node<T> prev = getNode(index - 1);
        Node<T> target = prev.getNext();
        T removedValue = target.getValue();

        prev.setNext(target.getNext());

        target.setValue(null); // auxílio ao Garbage Collector
        target.setNext(null);
        size--;

        return removedValue;
    }

    /**
     * Remove a primeira ocorrência do elemento especificado na lista (tempo O(n)).
     *
     * @param element o elemento a ser removido (pode ser nulo).
     * @return {@code true} se o elemento foi encontrado e removido; {@code false} caso contrário.
     */
    public boolean remove(T element) {
        if (isEmpty()) {
            return false;
        }

        if (Objects.equals(element, head.getValue())) {
            removeFirst();
            return true;
        }

        Node<T> current = head;
        while (current.getNext() != null) {
            if (Objects.equals(element, current.getNext().getValue())) {
                Node<T> target = current.getNext();
                current.setNext(target.getNext());

                if (target == tail) {
                    tail = current;
                }

                target.setValue(null); // auxílio ao Garbage Collector
                target.setNext(null);
                size--;
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    // =========================================================================
    // Operações de Consulta
    // =========================================================================

    /**
     * Retorna o elemento localizado em determinado índice da lista (tempo O(n)).
     *
     * @param index a posição do elemento (0 <= index < size).
     * @return o elemento no índice especificado.
     * @throws IndexOutOfBoundsException se o índice for inválido.
     */
    public T get(int index) {
        checkElementIndex(index);
        return getNode(index).getValue();
    }

    /**
     * Retorna o primeiro elemento da lista (tempo O(1)).
     *
     * @return o primeiro elemento.
     * @throws NoSuchElementException se a lista estiver vazia.
     */
    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("A lista está vazia.");
        }
        return head.getValue();
    }

    /**
     * Retorna o último elemento da lista (tempo O(1)).
     *
     * @return o último elemento.
     * @throws NoSuchElementException se a lista estiver vazia.
     */
    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("A lista está vazia.");
        }
        return tail.getValue();
    }

    /**
     * Verifica se determinado elemento está presente na lista (tempo O(n)).
     *
     * @param element o elemento a ser verificado.
     * @return {@code true} se o elemento estiver na lista; {@code false} caso contrário.
     */
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    /**
     * Retorna o índice da primeira ocorrência do elemento especificado (tempo O(n)).
     *
     * @param element o elemento a ser pesquisado.
     * @return o índice do elemento, ou {@code -1} se não for localizado.
     */
    public int indexOf(T element) {
        int index = 0;
        for (Node<T> curr = head; curr != null; curr = curr.getNext()) {
            if (Objects.equals(element, curr.getValue())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    // =========================================================================
    // Operações de Controle
    // =========================================================================

    /**
     * Retorna o número de elementos contidos na lista (tempo O(1)).
     *
     * @return a quantidade de elementos.
     */
    public int size() {
        return size;
    }

    /**
     * Verifica se a lista não contém elementos (tempo O(1)).
     *
     * @return {@code true} se a lista estiver vazia; {@code false} caso contrário.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Remove todos os elementos da lista, desvinculando os nós para liberar memória.
     */
    public void clear() {
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.getNext();
            current.setValue(null);
            current.setNext(null);
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    // =========================================================================
    // Métodos Auxiliares e Iterador
    // =========================================================================

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice inválido: " + index + ", Tamanho atual: " + size);
        }
    }

    private void checkPositionIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Posição inválida para inserção: " + index + ", Tamanho atual: " + size);
        }
    }

    private Node<T> getNode(int index) {
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Fim da iteração da lista encadeada.");
                }
                T value = current.getValue();
                current = current.getNext();
                return value;
            }
        };
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.getValue());
            if (current.getNext() != null) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}

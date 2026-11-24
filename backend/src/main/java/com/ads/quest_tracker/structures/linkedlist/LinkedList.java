package com.ads.quest_tracker.structures.linkedlist;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

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

    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Não é possível remover de uma lista vazia.");
        }

        T removedValue = head.getValue();
        Node<T> nextNode = head.getNext();

        head.setValue(null);
        head.setNext(null);
        head = nextNode;

        size--;
        if (size == 0) {
            tail = null;
        }

        return removedValue;
    }

    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Não é possível remover de uma lista vazia.");
        }

        if (size == 1) {
            return removeFirst();
        }

        Node<T> prev = getNode(size - 2);
        T removedValue = tail.getValue();

        tail.setValue(null);
        tail.setNext(null);

        prev.setNext(null);
        tail = prev;
        size--;

        return removedValue;
    }

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

        target.setValue(null);
        target.setNext(null);
        size--;

        return removedValue;
    }

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

                target.setValue(null);
                target.setNext(null);
                size--;
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    public T get(int index) {
        checkElementIndex(index);
        return getNode(index).getValue();
    }

    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("A lista está vazia.");
        }
        return head.getValue();
    }

    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("A lista está vazia.");
        }
        return tail.getValue();
    }

    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

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

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

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

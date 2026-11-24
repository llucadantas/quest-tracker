package com.ads.quest_tracker.structures.priorityqueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class PriorityQueue<T> {
    private static final int DEFAULT_CAPACITY = 16;

    private Object[] heap;
    private int size;
    private long nextSequence;
    private final Comparator<? super T> comparator;

    private static final class HeapEntry<T> {
        private final T element;
        private final long sequence;

        private HeapEntry(T element, long sequence) {
            this.element = element;
            this.sequence = sequence;
        }
    }

    @SuppressWarnings("unchecked")
    public PriorityQueue() {
        this((a, b) -> {
            if (!(a instanceof Comparable)) {
                throw new ClassCastException(
                        "O elemento fornecido não implementa Comparable e nenhum Comparator foi definido."
                );
            }
            return ((Comparable<? super T>) a).compareTo(b);
        });
    }

    public PriorityQueue(Comparator<? super T> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "O comparador não pode ser nulo.");
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.nextSequence = 0L;
    }

    private PriorityQueue(PriorityQueue<T> source) {
        this.comparator = source.comparator;
        this.size = source.size;
        this.nextSequence = source.nextSequence;
        this.heap = Arrays.copyOf(source.heap, Math.max(source.size, DEFAULT_CAPACITY));
    }

    public void insert(T element) {
        Objects.requireNonNull(element, "O elemento a ser inserido não pode ser nulo.");
        if (size == 0) {
            comparator.compare(element, element);
        }

        ensureCapacity();
        heap[size] = new HeapEntry<>(element, nextSequence++);
        heapifyUp(size);
        size++;
    }

    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Não é possível remover de uma fila de prioridades vazia.");
        }

        T rootElement = getEntry(0).element;
        size--;

        if (size > 0) {
            heap[0] = heap[size];
            heap[size] = null;
            heapifyDown(0);
        } else {
            heap[0] = null;
        }

        return rootElement;
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("A fila de prioridades está vazia.");
        }
        return getEntry(0).element;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(heap, 0, size, null);
        size = 0;
        nextSequence = 0L;
    }

    public List<T> list() {
        if (isEmpty()) {
            return Collections.emptyList();
        }

        List<T> ordered = new ArrayList<>(size);
        PriorityQueue<T> snapshot = new PriorityQueue<>(this);
        while (!snapshot.isEmpty()) {
            ordered.add(snapshot.remove());
        }
        return Collections.unmodifiableList(ordered);
    }

    private void heapifyUp(int index) {
        HeapEntry<T> target = getEntry(index);
        int current = index;

        while (current > 0) {
            int parentIndex = (current - 1) / 2;
            HeapEntry<T> parent = getEntry(parentIndex);

            if (compareEntries(target, parent) <= 0) {
                break;
            }

            heap[current] = parent;
            current = parentIndex;
        }

        heap[current] = target;
    }

    private void heapifyDown(int index) {
        HeapEntry<T> target = getEntry(index);
        int current = index;
        int half = size / 2;

        while (current < half) {
            int leftIndex = (2 * current) + 1;
            int rightIndex = leftIndex + 1;

            int bestChildIndex = leftIndex;
            HeapEntry<T> bestChild = getEntry(leftIndex);

            if (rightIndex < size) {
                HeapEntry<T> rightChild = getEntry(rightIndex);
                if (compareEntries(rightChild, bestChild) > 0) {
                    bestChildIndex = rightIndex;
                    bestChild = rightChild;
                }
            }

            if (compareEntries(target, bestChild) >= 0) {
                break;
            }

            heap[current] = bestChild;
            current = bestChildIndex;
        }

        heap[current] = target;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length * 2;
            heap = Arrays.copyOf(heap, newCapacity);
        }
    }

    private int compareEntries(HeapEntry<T> a, HeapEntry<T> b) {
        int cmp = comparator.compare(a.element, b.element);
        if (cmp != 0) {
            return cmp;
        }
        return Long.compare(b.sequence, a.sequence);
    }

    @SuppressWarnings("unchecked")
    private HeapEntry<T> getEntry(int index) {
        return (HeapEntry<T>) heap[index];
    }

    int capacity() {
        return heap.length;
    }

    boolean isValidMaxHeap() {
        for (int i = 0; i < size / 2; i++) {
            HeapEntry<T> parent = getEntry(i);
            int leftIndex = (2 * i) + 1;
            int rightIndex = leftIndex + 1;

            if (leftIndex < size && compareEntries(parent, getEntry(leftIndex)) < 0) {
                return false;
            }
            if (rightIndex < size && compareEntries(parent, getEntry(rightIndex)) < 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        return list().toString();
    }
}

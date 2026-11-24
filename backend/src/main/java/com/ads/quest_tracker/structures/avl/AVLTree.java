package com.ads.quest_tracker.structures.avl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class AVLTree<T> {
    private AVLNode<T> root;
    private int size;
    private final Comparator<? super T> comparator;

    private boolean lastOperationSuccess;

    @SuppressWarnings("unchecked")
    public AVLTree() {
        this((a, b) -> {
            if (!(a instanceof Comparable)) {
                throw new ClassCastException("O elemento fornecido não implementa Comparable e nenhum Comparator foi definido.");
            }
            return ((Comparable<? super T>) a).compareTo(b);
        });
    }

    public AVLTree(Comparator<? super T> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "O comparador não pode ser nulo.");
        this.root = null;
        this.size = 0;
    }

    public boolean insert(T element) {
        Objects.requireNonNull(element, "O elemento a ser inserido não pode ser nulo.");
        if (root == null) {
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
            lastOperationSuccess = false;
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

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

    public boolean contains(T element) {
        return search(element) != null;
    }

    public boolean delete(T element) {
        Objects.requireNonNull(element, "O elemento a ser removido não pode ser nulo.");
        lastOperationSuccess = false;
        root = deleteRecursive(root, element);
        if (lastOperationSuccess) {
            size--;
        }
        return lastOperationSuccess;
    }

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

            if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            }

            AVLNode<T> successor = findMinNode(node.getRight());
            node.setValue(successor.getValue());
            node.setRight(deleteRecursive(node.getRight(), successor.getValue()));
        }

        updateHeight(node);
        return balance(node);
    }

    private AVLNode<T> findMinNode(AVLNode<T> node) {
        AVLNode<T> current = node;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    public int height() {
        return getNodeHeight(root);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    int getNodeHeight(AVLNode<T> node) {
        return node == null ? 0 : node.getHeight();
    }

    void updateHeight(AVLNode<T> node) {
        if (node != null) {
            node.setHeight(1 + Math.max(getNodeHeight(node.getLeft()), getNodeHeight(node.getRight())));
        }
    }

    int getBalanceFactor(AVLNode<T> node) {
        return node == null ? 0 : getNodeHeight(node.getLeft()) - getNodeHeight(node.getRight());
    }

    AVLNode<T> rotateRight(AVLNode<T> y) {
        AVLNode<T> x = y.getLeft();
        AVLNode<T> t2 = x.getRight();

        x.setRight(y);
        y.setLeft(t2);

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    AVLNode<T> rotateLeft(AVLNode<T> y) {
        AVLNode<T> x = y.getRight();
        AVLNode<T> t2 = x.getLeft();

        x.setLeft(y);
        y.setRight(t2);

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    AVLNode<T> rotateLeftRight(AVLNode<T> node) {
        node.setLeft(rotateLeft(node.getLeft()));
        return rotateRight(node);
    }

    AVLNode<T> rotateRightLeft(AVLNode<T> node) {
        node.setRight(rotateRight(node.getRight()));
        return rotateLeft(node);
    }

    protected AVLNode<T> balance(AVLNode<T> node) {
        if (node == null) {
            return null;
        }

        updateHeight(node);
        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1 && getBalanceFactor(node.getLeft()) >= 0) {
            return rotateRight(node);
        }

        if (balanceFactor > 1 && getBalanceFactor(node.getLeft()) < 0) {
            return rotateLeftRight(node);
        }

        if (balanceFactor < -1 && getBalanceFactor(node.getRight()) <= 0) {
            return rotateLeft(node);
        }

        if (balanceFactor < -1 && getBalanceFactor(node.getRight()) > 0) {
            return rotateRightLeft(node);
        }

        return node;
    }

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

    public T min() {
        if (root == null) {
            return null;
        }
        return findMinNode(root).getValue();
    }

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

    public T getRootValue() {
        return root != null ? root.getValue() : null;
    }

    public List<T> getSortedElements() {
        return inOrder();
    }

    AVLNode<T> getRoot() {
        return root;
    }
}

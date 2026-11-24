package com.ads.quest_tracker.structures.avl;

import java.util.Objects;

public class AVLNode<T> {
    private T value;
    private AVLNode<T> left;
    private AVLNode<T> right;
    private int height;

    public AVLNode(T value) {
        this.value = Objects.requireNonNull(value, "O valor do nó não pode ser nulo.");
        this.left = null;
        this.right = null;
        this.height = 1;
    }

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

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    public boolean hasLeft() {
        return this.left != null;
    }

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

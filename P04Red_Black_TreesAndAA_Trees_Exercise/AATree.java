package P04Red_Black_TreesAndAA_Trees_Exercise;

import java.util.function.Consumer;

class AATree<T extends Comparable<T>> {
    private Node<T> root;
    private int size;

    public static class Node<T> {
        public T value;
        public Node<T> left, right;
        public int level;

        public Node(T value) {
            this.value = value;
            this.level = 1;
        }
    }

    public AATree() {
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void clear() {
        this.root = null;
        this.size = 0;
    }

    public void insert(T value) {
        this.root = this.insert(this.root, value);
        this.size++;
    }

    public Node<T> insert(Node<T> node, T value) {
        if (node == null) {
            return new Node<>(value);
        }

        int cmp = value.compareTo(node.value);

        if (cmp < 0) {
            node.left = this.insert(node.left, value);
        } else if (cmp > 0) {
            node.right = this.insert(node.right, value);
        } else {
            // Duplicate values are not allowed in an AA tree
            return node;
        }

        // Perform skew and split after recursive insert
        node = skew(node);
        node = split(node);

        return node;
    }

    private Node<T> split(Node<T> node) {
        if (node.right == null || node.right.right == null || node.level != node.right.right.level) {
            return node;
        }

        Node<T> k2 = node.right;
        node.right = k2.left;
        k2.left = node;
        k2.level++;
        return k2;
    }

    private Node<T> skew(Node<T> node) {
        if (node.left == null || node.left.level != node.level) {
            return node;
        }

        Node<T> k1 = node.left;
        node.left = k1.right;
        k1.right = node;
        return k1;
    }

    public int countNodes() {
        return this.size;
    }

    public boolean search(T element) {
        Node<T> current = this.root;
        while (current != null) {
            int cmp = element.compareTo(current.value);

            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                if (element.compareTo(current.value) == 0) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public void inOrder(Consumer<T> consumer) {
        this.eachInOrder(this.root, consumer);
    }

    public void preOrder(Consumer<T> consumer) {
        this.eachPreOrder(this.root, consumer);
    }

    public void postOrder(Consumer<T> consumer) {
        this.eachPostOrder(this.root, consumer);
    }

    public void eachPostOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        this.eachPostOrder(node.left, consumer);
        this.eachPostOrder(node.right, consumer);
        consumer.accept(node.value);
    }

    public void eachInOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        this.eachInOrder(node.left, consumer);
        consumer.accept(node.value);
        this.eachInOrder(node.right, consumer);
    }

    public void eachPreOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        consumer.accept(node.value);
        this.eachPreOrder(node.left, consumer);
        this.eachPreOrder(node.right, consumer);
    }
}
package P01DataStructuresAdvanced_B_2_3_AVL_Trees;

public class Node<T extends Comparable<T>> {

    public T value;
    public Node<T> left;
    public Node<T> right;

    public int height;

    public Node(T value) {
        this.value = value;
        this.height = 1;
    }

}

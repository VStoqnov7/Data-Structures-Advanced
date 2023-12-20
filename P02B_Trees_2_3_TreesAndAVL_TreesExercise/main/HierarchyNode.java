package P02B_Trees_2_3_TreesAndAVL_TreesExercise.main;

import java.util.ArrayList;
import java.util.List;

public class HierarchyNode<T> {
    private T value;
    private HierarchyNode<T> parent;
    private List<HierarchyNode<T>> children;

    public HierarchyNode(T element) {
        this.value = element;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public HierarchyNode<T> getParent() {
        return this.parent;
    }

    public void setParent(HierarchyNode<T> parent) {
        this.parent = parent;
    }

    public List<HierarchyNode<T>> getChildren() {
        return this.children;
    }

    public void setChildren(List<HierarchyNode<T>> children) {
        this.children = children;
    }
}

package treedatastructures;

import documentsdatastructures.DocumentVector;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> {
    T value;
    List<TreeNode<T>> children;

    public TreeNode(T value) {
        this.value = value;
        children = new LinkedList<>();
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void add(TreeNode<T> child) {
        children.add(child);
    }

    public void print(int i) {
        if(children.isEmpty())
            for(DocumentVector doc : (Collection<DocumentVector>)value) {
                for(int j = 0; j < i; j++)
                    System.out.print("   ");
                System.out.println(doc.getDocument().getTitle());
            }
        else {
            for(int j = 0; j < i; j++)
                System.out.print("   ");
            System.out.println(i);
        }
        for(TreeNode<T> child : children) {
            child.print(i+1);
        }
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }
}

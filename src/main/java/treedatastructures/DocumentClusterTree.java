package treedatastructures;

import documentsdatastructures.NewsDocument;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public class DocumentClusterTree {
    PriorityQueue<String> tags;
    List<NewsDocument> documents;
    int size;
    boolean tagsAdded = false;
    private static Set<String> usedTags = new HashSet<>();

    List<DocumentClusterTree> children;

    public DocumentClusterTree(int size) {
        this.size = size;
        children = new ArrayList<>(size);
    }

    // leaf
    public DocumentClusterTree(List<NewsDocument> documents) {
        this.documents = documents;
        children = null;
    }

    public boolean isLeaf() {
        return children == null;
    }

    public void setTags(PriorityQueue<String> tags) {
        tagsAdded = true;
        this.tags = tags;
    }

    public PriorityQueue<String> getTags() {
        return tags;
    }

    public List<DocumentClusterTree> getChildren() {
        return children;
    }

    public List<NewsDocument> getDocuments() {
        return documents;
    }

    public void add(DocumentClusterTree child) {
        children.add(child);
    }

    public void print(int level) {
        if (level == 0) {
            for (DocumentClusterTree child : children) {
                child.print(level + 1);
            }
            return;
        }
        if (isLeaf()) {
            printTab(level);
            for (NewsDocument doc : documents) {
                printTab(level + 1);
                System.out.println(doc.getTitle());
            }
        } else {
            printTab(level);
            System.out.println(tags.peek());
            for (DocumentClusterTree child : children) {
                child.print(level + 1);
            }
        }
    }

    private static void printTab(int len) {
        for (int j = 0; j < len; j++)
            System.out.print("   ");
    }

    public DefaultMutableTreeNode draw() {
        if (tags == null) {
            DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode();
            for (DocumentClusterTree child : children) {
                treeNode.add(child.draw());
            }
            return treeNode;
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; (i < 10) && (tags.size() > 0); i++)
            sb.append(tags.poll()).append(" ");
        String tag = sb.toString();
        usedTags.add(tag);

        DefaultMutableTreeNode treeNode;
        if (isLeaf()) {
            treeNode = new DefaultMutableTreeNode(tag);
            for (NewsDocument doc : documents) {
                treeNode.add(new DefaultMutableTreeNode(doc.getTitle()));
            }
        } else {
            treeNode = new DefaultMutableTreeNode(tag);
            for (DocumentClusterTree child : children) {
                child.getTags().removeAll(usedTags);
                treeNode.add(child.draw());
            }
        }
        return treeNode;
    }
}

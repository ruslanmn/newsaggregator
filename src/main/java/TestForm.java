import treedatastructures.DocumentClusterTree;

import javax.swing.*;

public class TestForm extends JFrame {
    public TestForm(DocumentClusterTree rootTree) {
        super("News Aggreagator: clustering test");
        JTree tree = new JTree(rootTree.draw());
        JScrollPane treeView = new JScrollPane(tree);

        add(treeView);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}

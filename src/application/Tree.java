package application;

public class Tree{
    public Node root;

    public Tree()
    {
    	root = null;
    }

    public Tree(Cell contents)
    {
    	root = new Node(contents);
    }

    public void add(Cell contents)
    {
    	root.children.add(new Node(contents));
    }

}
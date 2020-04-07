/**
 * AVLTree
 * <p>
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 */

public class AVLTree {

    private IAVLNode root;
    private int size;

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return root == null;
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) {
        IAVLNode node = root;

        while (node != null) {
            if (k == node.getKey())
                return node.getValue();
            else if (k < node.getKey())
                node = node.getLeft();
            else
                node = node.getRight();
        }
        return null;
    }

    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        if (root == null)
            return null;
        IAVLNode node = root;

        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node.getValue();
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        if (root == null)
            return null;
        IAVLNode node = root;

        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[size]; // to be replaced by student code
        InOrderToArrayRec(root, arr, 0);
        return arr;
    }

    private int InOrderToArrayRec(IAVLNode node, int[] arr, int i) {
        if (node != null) {
            int j = InOrderToArrayRec(node.getLeft(), arr, i);
            arr[i + j] = node.getKey();
            int k = InOrderToArrayRec(node.getRight(), arr, i + j + 1);
            return j + k + 1;
        }
        return 0;
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        String[] arr = new String[42]; // to be replaced by student code
        return arr;                    // to be replaced by student code
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     * <p>
     * precondition: none
     * postcondition: none
     */
    public int size() {
        return size();
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     * <p>
     * precondition: none
     * postcondition: none
     */
    public IAVLNode getRoot() {
        return root;
    }


    /**
     * public int insert(int k, String i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        IAVLNode nodeToAdd = new AVLNode(k, i);
        int rotations = 0;
        boolean inserted = baseInsert(nodeToAdd);
        if (!inserted)
            return -1;
        IAVLNode parent = nodeToAdd.getParent();
        IAVLNode heightRemained = updateHeightsOnPath(parent); // the node which is the stopping point of all the nodes which changed their height
        while (parent != null) {
            int BF = computeBF(parent);
            if (Math.abs(BF) < 2 && parent != heightRemained)
                break;
            else if (Math.abs(BF) < 2 && parent == heightRemained)
                continue;
            else {
                rotations = doRotation(parent, BF);
                break;
            }
        }
        size++;
		return rotations;

    }

    /**
     * The function inserts the nodeToAdd to the tree, using the algorithm learnt in class.
     *
     * @param nodeToAdd the node to be added to the tree.
     * @return true if the node was added to the tree, false if the node already existed in the tree.
     */
    private boolean baseInsert(IAVLNode nodeToAdd) { // Support function
        IAVLNode backNode = null, frontNode = root;

        // Inserting the node
        while (frontNode != null) {
            if (frontNode.getKey() == nodeToAdd.getKey())
                return false;
            backNode = frontNode;
            if (nodeToAdd.getKey() < frontNode.getKey())
                frontNode = frontNode.getLeft();
            else
                frontNode = frontNode.getRight();
        }
        nodeToAdd.setParent(backNode);
        if (backNode == null)
            root = nodeToAdd;
        else if (nodeToAdd.getKey() < backNode.getKey())
            backNode.setLeft(nodeToAdd);
        else
            backNode.setRight(nodeToAdd);
        return true;
    }

    /**
     * updates all the heights of the parents of node. {@link #updateHeight(IAVLNode) updateHeight}
     *
     * @param node the starting point of the path.
     * @return the last node which's height was updated.
     */
    private IAVLNode updateHeightsOnPath(IAVLNode node) { // support function
        while (node != null) {
            boolean changed = updateHeight(node);
            if (!changed)
                return node.getParent();
            node = node.getParent();
        }
        return null;
    }

    /**
     * Updates the height of a AVLNode.
     * If node is a leaf, will set its height to 0. otherwise:
     * Height = max{leftHeight,rightHeight} where leftHeight is the height of the left child, and right is the height of the right child
     *
     * @param node the node which's height is being updated
     * @return true if height changed, false otherwise.
     */
    public boolean updateHeight(IAVLNode node) { //support function
        int leftHeight = -1, rightHeight = -1;
        if (node.getLeft() != null)
            leftHeight = node.getLeft().getHeight();
        if (node.getRight() != null)
            rightHeight = node.getRight().getHeight();
        int oldHeight = node.getHeight();
        node.setHeight(Math.max(leftHeight, rightHeight) + 1);
        if (oldHeight == node.getHeight())
            return false;
        return true;

    }

    /**
     * computes the BF value of the {@link AVLNode} using the following formula:
     * BF = |leftHeight - rightHeight| where leftHeight is the height of the left child, and right is the height of the right child
     *
     * @param node
     * @return
     */
    private int computeBF(IAVLNode node) {
        int leftHeight = -1, rightHeight = -1;
        if (node.getLeft() != null)
            leftHeight = node.getLeft().getHeight();
        if (node.getRight() != null)
            rightHeight = node.getRight().getHeight();

        return leftHeight - rightHeight;

    }

    private int doRotation(IAVLNode node, int BF) {
        if (BF == 2)
            if (computeBF(node.getLeft()) == -1)
                return rotateLR(node);
            else
                return rotateRR(node);
        else {
            if (computeBF(node.getRight()) == 1)
                return rotateRL(node);
            else
                return rotateLL(node);
        }

    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        return 42;    // to be replaced by student code
    }

    private int rotateLL(IAVLNode node) {
        IAVLNode y = node.getRight();
        node.setRight(y.getLeft());
        if (y.getLeft() != null)
            y.getLeft().setParent(node);
        y.setParent(node.getParent());
        if (node.getParent() == null)
            root = y;
        else {
            if (node == node.getParent().getLeft())
                node.getParent().setLeft(node);
            else
                node.getParent().setRight(node);
        }
        y.setLeft(node);
        node.setParent(y);
        return 1;
    }

    private int rotateLR(IAVLNode node) {
        rotateLL(node.getLeft());
        rotateRR(node);
        return 2;
    }

    private int rotateRR(IAVLNode node) {
        IAVLNode y = node.getLeft();
        node.setRight(y.getRight());
        if (y.getRight() != null)
            y.getRight().setParent(node);
        y.setParent(node.getParent());
        if (node.getParent() == null)
            root = y;
        else {
            if (node == node.getParent().getLeft())
                node.getParent().setLeft(node);
            else
                node.getParent().setRight(node);
        }
        y.setRight(node);
        node.setParent(y);
        return 1;
    }

    private int rotateRL(IAVLNode node) {
        rotateRR(node.getRight());
        rotateLL(node);
        return 2;
    }


    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode {
        public int getKey(); //returns node's key

        public String getValue(); //returns node's value [info]

        public void setLeft(IAVLNode node); //sets left child

        public IAVLNode getLeft(); //returns left child (if there is no left child return null)

        public void setRight(IAVLNode node); //sets right child

        public IAVLNode getRight(); //returns right child (if there is no right child return null)

        public void setParent(IAVLNode node); //sets parent

        public IAVLNode getParent(); //returns the parent (if there is no parent return null)

        public void setHeight(int height); // sets the height of the node

        public int getHeight(); // Returns the height of the node
    }

    /**
     * public class AVLNode
     * <p>
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in
     * another file.
     * This class can and must be modified.
     * (It must implement IAVLNode)
     */
    public class AVLNode implements IAVLNode {

        private int key;
        private String value;
        private IAVLNode left, right, parent;
        private int height;

        public AVLNode(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setLeft(IAVLNode node) {
            left = node;
        }

        public IAVLNode getLeft() {
            return left;
        }

        public void setRight(IAVLNode node) {
            right = node;
        }

        public IAVLNode getRight() {
            return right;
        }

        public void setParent(IAVLNode node) {
            parent = node;
        }

        public IAVLNode getParent() {
            return parent;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

    }

}





import org.jetbrains.annotations.NotNull;

/**
 * AVLTree
 * <p>
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 */

public class AVLTree {

    protected IAVLNode root;
    protected int size;

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
        IAVLNode node = searchNodeByKey(k);
        if (node == null)
            return null;
        return node.getValue();
    }

    /**
     * public String search(int k)
     * <p>
     * returns the node with key k if it exists in the tree
     * otherwise, returns null
     */
    public IAVLNode searchNodeByKey(int k) {
        IAVLNode node = root;

        while (node != null) {
            if (k == node.getKey())
                return node;
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
        int[] arr = new int[size];
        InOrderKeyToArrayRec(root, arr, 0);
        return arr;
    }

    protected int InOrderKeyToArrayRec(IAVLNode node, int[] arr, int i) {
        if (node != null) {
            int j = InOrderKeyToArrayRec(node.getLeft(), arr, i);
            arr[i + j] = node.getKey();
            int k = InOrderKeyToArrayRec(node.getRight(), arr, i + j + 1);
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
        String[] arr = new String[size];
        InOrderInfoToArrayRec(root, arr, 0);
        return arr;
    }

    private int InOrderInfoToArrayRec(IAVLNode node, String[] arr, int i) {
        if (node != null) {
            int j = InOrderInfoToArrayRec(node.getLeft(), arr, i);
            arr[i + j] = node.getValue();
            int k = InOrderInfoToArrayRec(node.getRight(), arr, i + j + 1);
            return j + k + 1;
        }
        return 0;
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
        boolean inserted = baseInsert(nodeToAdd);
        if (!inserted)
            return -1;
        size++;
        return fixTreeInsert(nodeToAdd, this::updateHeight);

    }

    protected int fixTreeInsert(IAVLNode node, updateNodeInt updater) {
        IAVLNode parent = node.getParent();
        int rotations = 0;
        while (parent != null) {
            int BF = computeBF(parent);
            boolean changed = updater.update(parent);
            if (Math.abs(BF) < 2 && !changed)
                break;
            else if (Math.abs(BF) < 2) {
                parent = parent.getParent();
                continue;
            } else {
                rotations = doRotation(parent, BF, this::updateHeight);
                break;
            }
        }
        while (parent != null) {
            updater.update(parent);
            parent = parent.getParent();
        }
        return rotations;
    }

    /**
     * The function inserts the nodeToAdd to the tree, using the algorithm learnt in class.
     *
     * @param nodeToAdd the node to be added to the tree.
     *
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
     *
     * @return the last node which's height was updated.
     */
    private IAVLNode updateHeightsOnPath(IAVLNode node) { // support function
        while (node != null) {
            boolean changed = updateHeight(node);
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
     *
     * @return true if height changed, false otherwise.
     */
    protected boolean updateHeight(IAVLNode node) { //support function
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
     *
     * @return
     */
    protected int computeBF(IAVLNode node) {
        int leftHeight = -1, rightHeight = -1;
        if (node.getLeft() != null)
            leftHeight = node.getLeft().getHeight();
        if (node.getRight() != null)
            rightHeight = node.getRight().getHeight();

        return leftHeight - rightHeight;

    }

    protected int doRotation(IAVLNode node, int BF, updateNodeInt updater) {
        if (BF == 2)
            if (computeBF(node.getLeft()) == -1)
                return rotateLR(node, updater);
            else
                return rotateRR(node, updater);
        else {
            if (computeBF(node.getRight()) == 1)
                return rotateRL(node, updater);
            else
                return rotateLL(node, updater);
        }

    }

    private int rotateLL(IAVLNode node, updateNodeInt updater) {
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
        updater.update(node);
        updater.update(y);
        return 1;
    }

    private int rotateLR(IAVLNode node, updateNodeInt updater) {
        rotateLL(node.getLeft(), updater);
        rotateRR(node, updater);
        return 2;
    }

    private int rotateRR(IAVLNode node, updateNodeInt updater) {
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
        updater.update(node);
        updater.update(y);
        return 1;
    }

    private int rotateRL(IAVLNode node, updateNodeInt updater) {
        rotateRR(node.getRight(), updater);
        rotateLL(node, updater);
        return 2;
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
        IAVLNode node = searchNodeByKey(k);
        if (node == null)
            return -1;
        return deleteNode(node, this::updateHeight);


    }

    protected int deleteNode(IAVLNode node, updateNodeInt updater) {
        IAVLNode parent = node.getParent();
        IAVLNode rotateFrom = null;
        if (node.getLeft() == null && node.getRight() == null) {
            if (parent.getRight() == node)
                parent.setRight(null);
            else
                parent.setLeft(null);
            int counter = 0;
            rotateFrom = parent;
            return counter;
        } else if (node.getRight() != null ^ node.getLeft() != null) {
            if (node.getRight() != null) {
                if (parent.getRight() == node)
                    parent.setRight(node.getRight());
                else
                    parent.setLeft(node.getRight());
                node.getRight().setParent(parent);
            } else {
                if (parent.getRight() == node)
                    parent.setRight(node.getLeft());
                else
                    parent.setLeft(node.getLeft());
                node.getLeft().setParent(parent);
            }
            rotateFrom = parent;
        } else {
            IAVLNode runningNode = node.getRight();
            while (runningNode.getLeft() != null)
                runningNode = runningNode.getLeft();
            IAVLNode temp = runningNode.getParent();
            temp.setLeft(runningNode.getRight());
            runningNode.setRight(node.getRight());
            runningNode.setLeft(node.getLeft());
            runningNode.setParent(node.getParent());
            node.getRight().setParent(runningNode);
            node.getLeft().setParent(runningNode);
            if (node.getParent() == null)
                root = runningNode;
            else {
                if (node.getParent().getRight() == node)
                    node.getParent().setRight(runningNode);
                else
                    node.getParent().setLeft(runningNode);
            }
            rotateFrom = temp;
        }
        int counter = 0;
        while (rotateFrom != null) {
            boolean changed = updater.update(rotateFrom);
            int BF = computeBF(rotateFrom);
            parent = rotateFrom.getParent();
            if (Math.abs(BF) < 2 && !changed)
                return counter;

            if (Math.abs(BF) >= 2) {
                counter += doRotation(rotateFrom, BF, updater); // rotate
            }
            rotateFrom = parent;
        }
        size--;
        return counter;
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

        protected int key;
        protected String value;
        protected IAVLNode left, right, parent;
        protected int height;

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

    public class RankTreeNode extends AVLNode {
        private int size;

        public RankTreeNode(int key, String value) {
            super(key, value);
            size = 1;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void addNode() {
            size++;
        }
    }


}

interface updateNodeInt {
    public boolean update(AVLTree.IAVLNode node);
}

class RankTreeList extends AVLTree {

    public int insert(int i, int k, String s) {
        IAVLNode nodeToAdd = new RankTreeNode(k, s);
        boolean inserted = baseInsert(nodeToAdd, i);
        if (!inserted)
            return -1;
        size++;
//       return fixTreeInsert(nodeToAdd, this::updateSizeAndHeight);
        fixTreeInsert(nodeToAdd, this::updateSizeAndHeight);
        return 0;
    }

    protected boolean baseInsert(IAVLNode nodeToAdd, int i) {
        if (i < 0 || i > size)
            return false;
        if (size == 0) {
            this.root = nodeToAdd;
            return true;
        }
        if (i == size) {
            IAVLNode runningNode = root;
            while (runningNode.getRight() != null)
                runningNode = runningNode.getRight();
            runningNode.setRight(nodeToAdd);
            nodeToAdd.setParent(runningNode);
            return true;
        } else {
            IAVLNode successor = select(i + 1);
            if (successor.getLeft() != null) {
                successor.setLeft(nodeToAdd);
                nodeToAdd.setParent(successor);
            } else {
                IAVLNode runningNode = successor.getLeft();
                while (runningNode.getRight() != null)
                    runningNode = runningNode.getRight();
                runningNode.setRight(nodeToAdd);
                nodeToAdd.setParent(runningNode);
            }
        }
        return true;
    }

    private IAVLNode select(int k) {
        if (k > size || k < 1)
            return null;
        return selectRec(root, k);
    }

    private IAVLNode selectRec(IAVLNode node, int k) {
        int rank = 1;
        if (node.getLeft() != null)
            rank = ((RankTreeNode) node.getLeft()).getSize() + 1;
        if (k == rank)
            return node;
        else if (k < rank)
            return selectRec(node.getLeft(), k);
        else
            return selectRec(node.getLeft(), k);
    }

    private int getRank(IAVLNode node, int k) {
        int rank = 1;
        if (node.getLeft() != null)
            rank = ((RankTreeNode) node.getLeft()).getSize() + 1;
        IAVLNode runningNode = node;
        while (runningNode != null) {
            if (runningNode.getParent() != null)
                if (runningNode == runningNode.getParent().getRight()) {
                    if (runningNode.getParent().getLeft() != null)
                        rank += ((RankTreeNode) runningNode.getParent().getLeft()).getSize() + 1;
                    else
                        rank++;
                }
            runningNode = runningNode.getParent();
        }
        return rank;
    }

    public Item retrieve(int i) {
        IAVLNode node = select(i + 1);
        return new Item(node.getKey(), node.getValue());
    }

    @Override
    public int delete(int i) {
        IAVLNode nodeToDelete = select(i + 1);
        if (nodeToDelete == null)
            return -1;
        size--;
//        return deleteNode(nodeToDelete, this::updateSizeAndHeight);
        deleteNode(nodeToDelete, this::updateSizeAndHeight);
        return 0;
    }

    private boolean updateSizeAndHeight(IAVLNode node) {
        int leftSize = 0, rightSize = 0;
        RankTreeNode leftNode = (RankTreeNode) node.getLeft();
        RankTreeNode rightNode = (RankTreeNode) node.getRight();
        if (node.getLeft() != null)
            leftSize = leftNode.getSize();
        if (node.getRight() != null)
            rightSize = rightNode.getSize();
        ((RankTreeNode) node).setSize(leftSize + rightSize + 1);

        return updateHeight(node);
    }
}







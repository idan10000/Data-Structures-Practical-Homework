
/**
 * @author Idan Pinto, Yarden Rashti
 * AVLTree
 * <p>
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 */

public class AVLTree {

    protected IAVLNode root, min, max;
    protected int size;

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     * <p>
     * Time Complexity: O(1)
     */
    public boolean empty() {
        return root == null;
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     * <p>
     * Time Complexity: O(log(n))
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
     * <p>
     * Time Complexity: O(log(n))
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
     * <p>
     * Time Complexity: O(1)
     */
    public String min() {
        if (min == null)
            return null;
        else
            return min.getValue();
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     * <p>
     * Time Complexity: O(1)
     */
    public String max() {
        if (max == null)
            return null;
        else
            return max.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     * <p>
     * Time Complexity: O(log(n))
     */
    public int[] keysToArray() {
        int[] arr = new int[size];
        InOrderKeyToArrayRec(root, arr, 0);
        return arr;
    }

    /**
     * <p>
     * A recursive method that does an in-order tree run and adds the key of the current node into arr[i]
     * Used in {@link #keysToArray()}
     * </p>
     * Time Complexity: O(n)
     *
     * @param node the current node of the recursive function
     * @param arr  the array of keys
     * @param i    the current index in the array
     *
     * @return the amount progressed
     */
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
     * <p>
     * Time Complexity: O(n)
     */
    public String[] infoToArray() {
        String[] arr = new String[size];
        InOrderInfoToArrayRec(root, arr, 0);
        return arr;
    }

    /**
     * <p>
     * Simmilarly to {@link #InOrderKeyToArrayRec(IAVLNode, int[], int)}it is a recursive method that does an in-order
     * tree run and adds the value of the current node into arr[i]
     * Used in {@link #infoToArray()} ()}
     * </p>
     * Time Complexity: O(n)
     *
     * @param node the current node of the recursive function
     * @param arr  the array of values
     * @param i    the current index in the array
     *
     * @return the amount progressed
     */
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
     * <p>
     * Time Complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     * <p>
     * precondition: none
     * postcondition: none
     * <p>
     * Time Complexity: O(1)
     */
    public IAVLNode getRoot() {
        return root;
    }


    /**
     * public int insert(int k, String i)
     * <p>
     * inserts an item with key k and info i to the AVL tree using the method {@link #baseInsert(IAVLNode)}
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * returns -1 if an item with key k already exists in the tree.
     * <p>
     * Time Complexity: O(log(n))
     */
    public int insert(int k, String i) {
        IAVLNode nodeToAdd = new AVLNode(k, i);
        boolean inserted = baseInsert(nodeToAdd);
        if (!inserted)
            return -1;
        size++;
        return fixTreeInsert(nodeToAdd, this::updateHeight);

    }

    /**
     * <p>
     * A method used to re-balance the tree after a node has been inserted into the tree.
     * The algorithm is the standard one taught in class, run from the parent of the node added to the tree.
     * </p>
     * Time Complexity: O(log(n))
     *
     * @param node    the node added to the tree
     * @param updater the functional interface used to update the parameters of a node.
     *
     * @return the amount of rotations done while fixing the tree
     */
    protected int fixTreeInsert(IAVLNode node, updateNodeInterface updater) {
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
     * <p>
     * The function inserts the nodeToAdd to the tree, using the algorithm learnt in class.
     * </p>
     * Time Complexity: O(log(n))
     *
     * @param nodeToAdd the node to be added to the tree.
     *
     * @return true if the node was added to the tree, false if the node already existed in the tree.
     */
    private boolean baseInsert(IAVLNode nodeToAdd) { // Support function

        //updating max and min
        if (max == null || max.getKey() < nodeToAdd.getKey())
            max = nodeToAdd;
        if (min == null || min.getKey() > nodeToAdd.getKey())
            min = nodeToAdd;

        // Inserting the node
        IAVLNode backNode = null, frontNode = root;
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
     * <p>
     * Updates the height of a AVLNode.
     * If node is a leaf, will set its height to 0. otherwise:
     * Height = max{leftHeight,rightHeight} where leftHeight is the height of the left child, and right is the height of the right child
     * This function is mainly used as the {@link updateNodeInterface#update(IAVLNode)} function in the functional interface.
     * </p>
     * Time Complexity: O(1)
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
     * <p>
     * computes the BF value of the {@link AVLNode} using the following formula:
     * BF = |leftHeight - rightHeight| where leftHeight is the height of the left child, and right is the height of the right child
     * </p>
     * Time Complexity: O(1)
     *
     * @param node the node which BF is being calculated
     *
     * @return the BF
     */
    protected int computeBF(IAVLNode node) {
        int leftHeight = -1, rightHeight = -1;
        if (node.getLeft() != null)
            leftHeight = node.getLeft().getHeight();
        if (node.getRight() != null)
            rightHeight = node.getRight().getHeight();

        return leftHeight - rightHeight;

    }

    /**
     * <p>
     * A method which controls which rotation is done to the node according to it's BF, and its corresponding son's BF. {@link #computeBF(IAVLNode)}
     * The algorithm works as learnt in class:
     * if node.BF = 2 -> if(node.left.BF = -1) do {@link #rotateLR(IAVLNode, updateNodeInterface)} else do {@link #rotateRR(IAVLNode, updateNodeInterface)}
     * if node.BF = -2 -> if(node.right.BF = 1) do {@link #rotateRL(IAVLNode, updateNodeInterface)} else do {@link #rotateLL(IAVLNode, updateNodeInterface)}
     * </p>
     * Time complexity: O(1)
     *
     * @param node    the BF criminal
     * @param BF      the Balance factor of the node (node.left.height - node.right.height)
     * @param updater a functional interface which updates the parameters of the node after the rotation
     *
     * @return the number of rotations done
     */
    protected int doRotation(IAVLNode node, int BF, updateNodeInterface updater) {
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

    /**
     * <p>
     * The algorithm for a left rotation.
     * At the end of the method updates the corresponding nodes parameters using the {@link updateNodeInterface#update(IAVLNode)}
     * </p>
     * Time complexity: O(1)
     *
     * @param node    The BF criminal which is being fixed
     * @param updater the functional interface which updates the node parameters
     *
     * @return 1, the amount of rotations done
     */
    private int rotateLL(IAVLNode node, updateNodeInterface updater) {
        IAVLNode y = node.getRight();
        node.setRight(y.getLeft());
        if (y.getLeft() != null)
            y.getLeft().setParent(node);
        y.setParent(node.getParent());
        if (node.getParent() == null)
            root = y;
        else {
            if (node == node.getParent().getLeft())
                node.getParent().setLeft(y);
            else
                node.getParent().setRight(y);
        }
        y.setLeft(node);
        node.setParent(y);
        updater.update(node);
        updater.update(y);
        return -1;
    }

    /**
     * <p>
     * Calls {@link #rotateLL(IAVLNode, updateNodeInterface)} on node.left
     * Then calls {@link #rotateRR(IAVLNode, updateNodeInterface)} on node
     * </p>
     * Time Complexity: O(1)
     *
     * @param node    the BF criminal
     * @param updater the functional interface which updates the node parameters
     *
     * @return 2, the amount of rotations done.
     */
    private int rotateLR(IAVLNode node, updateNodeInterface updater) {
        rotateLL(node.getLeft(), updater);
        rotateRR(node, updater);
        return 3;
    }

    /**
     * <p>
     * The algorithm for a right rotation as tought in class.
     * At the end of the method updates the corresponding nodes parameters using the {@link updateNodeInterface#update(IAVLNode)}
     * </p>
     * Time complexity: O(1)
     *
     * @param node    The BF criminal which is being fixed
     * @param updater the functional interface which updates the node parameters
     *
     * @return 1, the amount of rotations done
     */
    private int rotateRR(IAVLNode node, updateNodeInterface updater) {
        IAVLNode y = node.getLeft();
        node.setLeft(y.getRight());
        if (y.getRight() != null)
            y.getRight().setParent(node);
        y.setParent(node.getParent());
        if (node.getParent() == null)
            root = y;
        else {
            if (node == node.getParent().getLeft())
                node.getParent().setLeft(y);
            else
                node.getParent().setRight(y);
        }
        y.setRight(node);
        node.setParent(y);
        updater.update(node);
        updater.update(y);
        return 1;
    }

    /**
     * <p>
     * Calls {@link #rotateRR(IAVLNode, updateNodeInterface)} on node.right
     * Then calls {@link #rotateLL(IAVLNode, updateNodeInterface)} on node
     * </p>
     * Time Complexity: O(1)
     *
     * @param node    the BF criminal
     * @param updater the functional interface which updates the node parameters
     *
     * @return 2, the amount of rotations done.
     */
    private int rotateRL(IAVLNode node, updateNodeInterface updater) {
        rotateRR(node.getRight(), updater);
        rotateLL(node, updater);
        return 3;
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * returns -1 if an item with key k was not found in the tree.
     * <p>
     * Time Complexity: O(log(n))
     */
    public int delete(int k) {
        IAVLNode node = searchNodeByKey(k);
        if (node == null)
            return -1;

        //updating max and min
        if (node == max)
            if (node.getLeft() == null)
                max = node.getParent();
            else {
                IAVLNode runningNode = max.getLeft();
                while (runningNode.getRight() != null)
                    runningNode = runningNode.getRight();
                max = runningNode;
            }
        if (node == min)
            if (node.getRight() == null)
                min = node.getParent();
            else {
                IAVLNode runningNode = min.getRight();
                while (runningNode.getLeft() != null)
                    runningNode = runningNode.getLeft();
                min = runningNode;
            }


        size--;
        return deleteNode(node, this::updateHeight);


    }


    /**
     * <p>
     * A method which deletes a node from the tree, and re-balances it using the standard algorithm learnt in class.
     * The method starts re-balancing the tree from a different node considering if the the successor
     * successor of the node is it's right child then we fix the tree from it. otherwise we fix it from
     * the parent of the physically deleted node (the parent of the successor).
     * <p>
     * This method is used in {@link AVLTree#delete(int)} and {@link RankTreeList#delete(int)} to delete a node from the tree.
     * </p>
     * Time Complexity: O(log(n))
     *
     * @param node    the node to be deleted
     * @param updater the functional interface used to update a node's parameters
     *
     * @return the amount of rotations done during the process of fixing the tree
     */
    protected int deleteNode(IAVLNode node, updateNodeInterface updater) {
        IAVLNode parent = node.getParent();
        IAVLNode rotateFrom = null; // the node from which the tree is starting to be fixed
        boolean movedLeft = false;
        if (node.getLeft() == null && node.getRight() == null) { // node has no children
            if (parent != null) {
                if (parent.getRight() == node)
                    parent.setRight(null);
                else
                    parent.setLeft(null);
                rotateFrom = parent;
            } else {
                root = null;
                return 0;
            }
        } else if (node.getRight() != null ^ node.getLeft() != null) { //node has one child
            if (parent != null) { // if not root
                if (node.getRight() != null) { //do overpass
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
            } else { // if root
                if (node.getRight() != null) //replaces root
                    root = node.getRight();
                else
                    root = node.getLeft();
                root.setParent(null);
                return 0;
            }
        } else { //node has 2 children
            IAVLNode runningNode = node.getRight();
            if (runningNode.getLeft() != null)
                movedLeft = true;
            while (runningNode.getLeft() != null) // finding successor when node has 2 children
                runningNode = runningNode.getLeft();
            if (movedLeft) { // if successor is not the node's right child
                IAVLNode temp = runningNode.getParent();
                temp.setLeft(runningNode.getRight());
                if (runningNode.getRight() != null)
                    runningNode.getRight().setParent(temp);
                rotateFrom = temp;

            } else { // if the successor is the node's right child
                node.setRight(runningNode.getRight());
                if (runningNode.getRight() != null)
                    runningNode.getRight().setParent(node);
                rotateFrom = runningNode;
            }
            // replace node with it's successor
            runningNode.setRight(node.getRight());
            runningNode.setLeft(node.getLeft());
            runningNode.setParent(node.getParent());
            if (node.getRight() != null)
                node.getRight().setParent(runningNode);
            if (node.getLeft() != null)
                node.getLeft().setParent(runningNode);
            if (node.getParent() == null)
                root = runningNode;
            else {
                if (node.getParent().getRight() == node)
                    node.getParent().setRight(runningNode);
                else
                    node.getParent().setLeft(runningNode);
            }
            //replaces the height and size (if it's a RankTreeNode) of runningNode to be those of node
            runningNode.setHeight(node.getHeight());
            if (runningNode instanceof RankTreeNode)
                ((RankTreeNode) runningNode).setSize(((RankTreeNode) node).getSize());

        }
        //Re-balances the tree from rotateFrom
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

        /*
        All getters and setters run at O(1) time and are implemented defaultly (return value for getter, change it for setter)
         */

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

    /**
     * a class which inherits from {@link AVLNode} which implements {@link IAVLNode}.
     * This class contains an extra parameter, size. this class is used in {@link RankTreeList}
     */
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
    }


}

/**
 * A Functional interface used to update parameters of an {@link AVLTree.IAVLNode}
 */
interface updateNodeInterface {
    public boolean update(AVLTree.IAVLNode node);
}

/**
 * A class which is used in the implementation of {@link TreeList}.
 * The class extends {@link AVLTree}, and implements all of the nodes as {@link RankTreeNode}.
 */
class RankTreeList extends AVLTree {

    /**
     * <p>
     * Inserts an item into the tree using {@link #baseInsert(IAVLNode, int)}
     * Then updates the size of the tree and rebalances it using {@link #fixTreeInsert(IAVLNode, updateNodeInterface)}
     * The function it uses to update the parameters of the nodes is {@link #updateSizeAndHeight(IAVLNode)}.
     * </p>
     * Time Complexity: O(log(n))
     *
     * @param i the position of the inserted item in the list
     * @param k the key of the item
     * @param s the value of the item (info)
     *
     * @return -1 if i<0 or i>n otherwise return 0.
     */
    public int insert(int i, int k, String s) {
        IAVLNode nodeToAdd = new RankTreeNode(k, s);
        boolean inserted = baseInsert(nodeToAdd, i);
        if (!inserted)
            return -1;
        size++;
        return fixTreeInsert(nodeToAdd, this::updateSizeAndHeight);
//        fixTreeInsert(nodeToAdd, this::updateSizeAndHeight);
//        return 0;
    }

    /**
     * <p>
     * Inserts the nodeToAdd into the tree according to the parameter i:
     * if(i = sizeOfTree) -> max.right = node
     * else we find node at rank(i+1) using {@link #select(int)}. if it doesn't have a left child, than make it node.
     * otherwise make its successors right child node.
     * </p>
     * Time Complexity: O(log(n))
     *
     * @param nodeToAdd the node that is being added to the tree
     * @param i         the index of the node in the list the tree is representing
     *
     * @return true if node inserted, false otherwise
     */
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
            IAVLNode predecessor = select(i + 1);
            if (predecessor.getLeft() == null) {
                predecessor.setLeft(nodeToAdd);
                nodeToAdd.setParent(predecessor);
            } else {
                IAVLNode runningNode = predecessor.getLeft();
                while (runningNode.getRight() != null)
                    runningNode = runningNode.getRight();
                runningNode.setRight(nodeToAdd);
                nodeToAdd.setParent(runningNode);
            }
        }
        return true;
    }

    /**
     * <p>
     * A shell function used to call {@link #selectRec(IAVLNode, int)} if 1 <= k <= size
     * </p>
     * Time Complexity: O(log(n))
     *
     * @param k the rank that is being searched
     *
     * @return the node which's rank is k.
     */
    private IAVLNode select(int k) {
        if (k > size || k < 1)
            return null;
        return selectRec(root, k);
    }

    /**
     * <p>
     * A recursive function which finds the node at rank k.
     * </p>
     * Time Complexity: O(log(n))
     *
     * @param node the current node on the path to the correct one
     * @param k    the desired rank
     *
     * @return the node which's rank is k
     */
    private IAVLNode selectRec(IAVLNode node, int k) {
        int rank = 1;
        if (node.getLeft() != null)
            rank = ((RankTreeNode) node.getLeft()).getSize() + 1;
        if (k == rank)
            return node;
        else if (k < rank)
            return selectRec(node.getLeft(), k);
        else
            return selectRec(node.getRight(), k - rank);
    }

    /**
     * <p>
     * A method which returns an {@link Item}  who's rank in the tree is {@code i + 1} using the method {@link #select(int)}.
     * </p>
     * Time complexity: O(log(n))
     *
     * @param i the desired item in the {@link TreeList}
     *
     * @return a new {@link Item} which's key is the key of the node at rank i+1, and it's info is node's value.
     */
    public Item retrieve(int i) {
        IAVLNode node = select(i + 1);
        if (node == null)
            return null;
        return new Item(node.getKey(), node.getValue());
    }

    /**
     * <p>
     * A method which deletes the node at rank i+1 using the method {@link #deleteNode(IAVLNode, updateNodeInterface)}.
     * if finds the node using the method {@link #select(int)}.
     * This method is used to implement the method {@link TreeList#delete(int)}
     * </p>
     * Time Complexity: O(log(n))
     *
     * @param i the index of desired item to delete in {@link TreeList}.
     *
     * @return 0 if deleted the node, -1 otherwise (node not found)
     */
    @Override
    public int delete(int i) {
        IAVLNode nodeToDelete = select(i + 1);
        if (nodeToDelete == null)
            return -1;
        size--;
        return deleteNode(nodeToDelete, this::updateSizeAndHeight);
//        deleteNode(nodeToDelete, this::updateSizeAndHeight);
//        return 0;
    }

    /**
     * <p>
     * A method that updates the size and height of a node:
     * Size = left.size + right.size + 1 (if a child is null, it's size is 0)
     * Height = {@link #updateHeight(IAVLNode)}
     * <p>
     * This function is used as the {@link updateNodeInterface#update(IAVLNode)} function in the functional interface for {@link RankTreeList}
     * </p>
     * Time Complexity: O(1)
     *
     * @param node the node who's height and size needs to be updated. is an instance of {@link RankTreeNode}
     *
     * @return true if height changed, false otherwise
     */
    private boolean updateSizeAndHeight(IAVLNode node) {
        // update size
        int leftSize = 0, rightSize = 0;
        RankTreeNode leftNode = (RankTreeNode) node.getLeft();
        RankTreeNode rightNode = (RankTreeNode) node.getRight();
        if (node.getLeft() != null)
            leftSize = leftNode.getSize();
        if (node.getRight() != null)
            rightSize = rightNode.getSize();
        ((RankTreeNode) node).setSize(leftSize + rightSize + 1);
        // update height and return if changed the height
        return updateHeight(node);
    }
}







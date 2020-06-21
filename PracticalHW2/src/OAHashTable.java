public abstract class OAHashTable implements IHashTable {

    private HashTableElement[] table; // The array that holds the table elements
    private int curElements; // The current amount of items in the table
    private final HashTableElement deleted = new HashTableElement(-1, -1); // An item that marks an index which an item was deleted from

    public OAHashTable(int m) {
        // Init empty table
        this.table = new HashTableElement[m];
        curElements = 0;
    }


    @Override
    public HashTableElement Find(long key) {
        // Attempt m (table size) probes of hash function to find the element with the corresponding key
        for (int i = 0; i < table.length - 1; i++) {
            HashTableElement element = table[Hash(key, i)];
            if (element == null)
                break;
            if (element != deleted && element.GetKey() == key) // ignores deleted node by comparing memory addresses
                return element;
        }
        return null;
    }

    @Override
    public void Insert(HashTableElement hte) throws TableIsFullException, KeyAlreadyExistsException {
        if (curElements == table.length) // If table is full
            throw new TableIsFullException(hte);
        int indexToInsert = -1;
        for (int i = 0; i < table.length; i++) { // Attempt m (table size) probes to insert the item
            int hashedIndex = Hash(hte.GetKey(), i);
            HashTableElement element = table[hashedIndex];
            if (element == null) { // If found an empty slot, save index and end probes
                if (indexToInsert == -1)
                    indexToInsert = hashedIndex;
                break;
            }
            if ((element == deleted) && indexToInsert == -1) // If found the first deleted node, save index and continue probing to find if it already exists in table
                indexToInsert = hashedIndex;
            else if (element.GetKey() == hte.GetKey()) { // If found key, save index to throw key exists exception
                indexToInsert = -2;
                break;
            }
        }
        if (indexToInsert == -1) // If could not find an empty or deleted slot to insert the new element
            throw new TableIsFullException(hte);
        if (indexToInsert == -2) // If the key already exists in the table
            throw new KeyAlreadyExistsException(hte);
        // Update the current amount of items and insert the new element to the table
        curElements++;
        table[indexToInsert] = hte;
    }

    @Override
    public void Delete(long key) throws KeyDoesntExistException {
        for (int i = 0; i < table.length; i++) { // Attempt m (table size) probes to find and delete the element
            int hashedIndex = Hash(key, i);
            HashTableElement element = table[hashedIndex];
            if (element == null) // If found a null slot, the key searched will not be in any of the future probes, thus it is not in the table
                throw new KeyDoesntExistException(key);
            else if (element.GetKey() == key && element != deleted) { // If found key, and it is not the deleted marker
                // Put the deleted marker in the index and update the current amount of items in the table, end method.
                table[hashedIndex] = deleted;
                curElements--;
                break;
            }
        }
    }

    /**
     * @param x - the key to hash
     * @param i - the index in the probing sequence
     *
     * @return the index into the hash table to place the key x
     */
    public abstract int Hash(long x, int i);
}

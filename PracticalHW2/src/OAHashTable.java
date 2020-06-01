
public abstract class OAHashTable implements IHashTable {

    private HashTableElement[] table;
    private int curElements;
    private final HashTableElement deleted = new HashTableElement(0, 0);

    public OAHashTable(int m) {
        this.table = new HashTableElement[m];
        curElements = 0;
    }


    @Override
    public HashTableElement Find(long key) {
        for (int i = 0; i < table.length - 1; i++) {
            HashTableElement element = table[Hash(key, i)];
            if (element == null)
                break;
            if (element != deleted && element.GetKey() == key)
                return element;
        }
        return null;
    }

    @Override
    public void Insert(HashTableElement hte) throws TableIsFullException, KeyAlreadyExistsException {
        if (curElements == table.length)
            throw new TableIsFullException(hte);
        int indexToInsert = -1;
        for (int i = 0; i < table.length; i++) {
            HashTableElement element = table[Hash(hte.GetKey(), i)];
            if ((element == deleted || element == null) && indexToInsert == -1)
                indexToInsert = i;
            else if (element != null && element.GetKey() == hte.GetKey()) {
                indexToInsert = -2;
                break;
            }
        }

        if (indexToInsert == -2)
            throw new KeyAlreadyExistsException(hte);
        curElements++;
        table[indexToInsert] = hte;
    }

    @Override
    public void Delete(long key) throws KeyDoesntExistException {
        for (int i = 0; i < table.length; i++) {
            HashTableElement element = table[Hash(key, i)];
            if(element == null)
                throw new KeyDoesntExistException(key);
            else if(element.GetKey() == key) {
                table[i] = deleted;
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

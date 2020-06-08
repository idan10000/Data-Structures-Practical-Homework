import java.util.Arrays;

public abstract class OAHashTable implements IHashTable {

    private HashTableElement[] table;
    private int curElements;
    private final HashTableElement deleted = new HashTableElement(-1, -1);

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
            int hashedIndex = Hash(hte.GetKey(), i);
            HashTableElement element = table[hashedIndex];
            if (element == null) {
                if (indexToInsert == -1)
                    indexToInsert = hashedIndex;
                break;
            }
            if ((element == deleted) && indexToInsert == -1)
                indexToInsert = hashedIndex;
            else if (element.GetKey() == hte.GetKey()) {
                indexToInsert = -2;
                break;
            }
        }
        if (indexToInsert == -1)
            throw new TableIsFullException(hte);
        if (indexToInsert == -2)
            throw new KeyAlreadyExistsException(hte);
        curElements++;
        table[indexToInsert] = hte;
    }

    @Override
    public void Delete(long key) throws KeyDoesntExistException {
        for (int i = 0; i < table.length; i++) {
            int hashedIndex = Hash(key, i);
            HashTableElement element = table[hashedIndex];
            if (element == null)
                throw new KeyDoesntExistException(key);
            else if (element.GetKey() == key && element != deleted) {
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

    @Override
    public String toString() {
        return "OAHashTable{" +
                "table=" + Arrays.toString(table) +
                "\ncurElements=" + curElements +
                '}';
    }
}

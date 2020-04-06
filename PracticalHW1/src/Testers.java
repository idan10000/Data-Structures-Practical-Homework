import com.sun.org.apache.bcel.internal.generic.RET;

public class Testers {
    public static void main(String[] args) {
        circularListTester();
    }

    private static void circularListTester()
    {
        CircularList lst1 = new CircularList(10);
        for (int i = 0; i < 5; i++) {
            lst1.insert(i, i, "" + i);
        }
        System.out.println(lst1);
        System.out.println(lst1.insert(5,11,"5"));
        System.out.println(lst1.insert(8,7,""));
        System.out.println(lst1);
        System.out.println(lst1.retrieve(-1));
        System.out.println(lst1.retrieve(2));
        System.out.println(lst1.retrieve(9));
//        lst1.delete(2);
//        lst1.delete(2);
//        lst1.delete(2);
//        System.out.println(lst1);
//        lst1.insert(7,11,"6");
//        System.out.println(lst1);
//        System.out.println();
//
//        lst1.insert(8,12,"");
//        System.out.println(lst1);
//        lst1.delete(7);
//        System.out.println(lst1);

    }
}

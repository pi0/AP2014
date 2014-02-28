package AP2014.set;

public class SetTest {

    public static void main(String[] args) {
        Set<String> a, b;
        a = new Set<String>();
        b = new Set<String>();

        a.add("1");
        a.add("2");

        b.cloneFrom(a);
        b.add("boo");
        b.add("4");

        a.add("3");
        a.add("2");
        a.add("3");
        a.add("4");

        b.intersectWith(a);
       // a.setMinus(b);
        System.out.println(b);
        System.out.println(a);
    }

}

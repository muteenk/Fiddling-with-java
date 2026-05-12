public class stringsInJava {
    static void main() {
        String email = "testmuteen@gmail.com";

        // basic methods
        System.out.println(email.charAt(5));
        System.out.println(email.length());
        System.out.println(email.isEmpty());
        System.out.println(email.contains("gmail"));
        // System.out.println(email.contains('g'));  ERROR

        // Index Operations
        System.out.println(email.indexOf("gmail"));
        System.out.println(email.indexOf('g'));
        System.out.println(email.lastIndexOf("gmail"));
        System.out.println(email.lastIndexOf('a'));

        // String Comparison
        System.out.println(" \n\n String Comparisons \n\n");
        System.out.println(email.equals("gmail")); // false
        System.out.println(email == "testmuteen@gmail.com"); // true
        String mail = "testmuteen@gmail.com";
        System.out.println(email == mail); // true
        String mail2 = new String("testmuteen@gmail.com");
        System.out.println(email == mail2); // false

        // String Casing
        System.out.println(email.toUpperCase());
        System.out.println(email.toLowerCase());

        // Sub Strings
        System.out.println(email.substring(0, email.indexOf("@")));



    }
}

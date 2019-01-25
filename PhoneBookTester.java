/*
 *  Książka telefoniczna realizowana przy pomocy komunikacji sieciowej
 *   - tester
 *
 *  Autor: Adrian Mrówka
 *  Nr indeksu: 234939
 *  Data: 25 stycznia 2019 r.
 */

package phoneBook;

public class PhoneBookTester {
    public static void main(String [] args) {
        new PhoneBookServer();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new PhoneBookClient("Adrian", "");
        new PhoneBookClient("Ewelina", "");
    }
}

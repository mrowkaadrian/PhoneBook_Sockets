/*
 *  Książka telefoniczna realizowana przy pomocy komunikacji sieciowej
 *   - Klasa zawierająca łańcuchy znaków
 *
 *  Autor: Adrian Mrówka
 *  Nr indeksu: 234939
 *  Data: 25 stycznia 2019 r.
 */

package phoneBook;

public class MyStrings {
    public static String UNKNOWN_COMMAND_INFO = "Nieznana komenda. Wpisz HELP\n" +
            "aby uzyskac liste dostepnych komend";

    public static String HELP_INFO = "Mozliwe operacje:\n" +
            "LOAD [nazwa pliku]\n" +
            "SAVE [nazwa pliku]\n" +
            "GET [imie]\n" +
            "PUT [imie  numer]\n" +
            "REPLACE [imie  nowy_numer]\n" +
            "DELETE [imie]\n" +
            "LIST\n" +
            "CLOSE\n" +
            "BYE\n";
}

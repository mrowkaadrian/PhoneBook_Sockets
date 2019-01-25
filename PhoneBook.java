/*
 *  Książka telefoniczna realizowana przy pomocy komunikacji sieciowej
 *   - baza danych
 *
 *  Autor: Adrian Mrówka
 *  Nr indeksu: 234939
 *   Data: 25 stycznia 2019 r.
 */

package phoneBook;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PhoneBook implements Serializable {

    private static final long serialVersionUID = 1L;

    ConcurrentHashMap<String, Integer> database = new ConcurrentHashMap<>();

    String load(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            this.database = (ConcurrentHashMap<String, Integer>)ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: Sprawdz stacktrace.\n";
        }
        return "OK.\n";
    }

    String save(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(database);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: Sprawdz stacktrace.\n";
        }
        return "OK.\n";
    }

    String get(String name) {
        if (database.containsKey(name))
            return "OK: " + database.get(name) + "\n";
        else
            return "ERROR: Nie ma takiej osoby w bazie!\n";
    }

    String put(String name, String number) {
        if (database.containsKey(name)) {
            return "ERROR: Taki klucz juz istnieje!\n";
        }
        else {
            database.put(name, Integer.valueOf(number));
            return "OK.\n";
        }
    }

    String replace (String name, String newNumber) {
        if (database.containsKey(name)) {
            database.replace(name, Integer.valueOf(newNumber));
            return "OK.\n";
        }
        else {
            return "Nie ma takiej osoby w bazie.\n";
        }
    }

    String delete (String name) {
        if (database.containsKey(name)) {
            database.remove(name);
            return "OK.\n";
        }
        else {
            return "ERROR: Nie ma takiej osoby w bazie.\n";
        }
    }

    String list() {
        StringBuilder lista = new StringBuilder();
        Iterator it = database.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            lista.append(pair.getKey());
            lista.append("\n");
        }
        return "Aktualna baza:\n" + lista;
    }

}

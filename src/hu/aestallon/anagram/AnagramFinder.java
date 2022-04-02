package hu.aestallon.anagram;

import java.io.*;
import java.util.*;

/**
 * <b>Fájlkezelés - feladat 6.</b>
 * <p>
 * Készítsünk programot, amely a magyar szavak listájából (input.txt)
 * kiválogatja az egymással anagrammát alkotó szavakat. Ezeket
 * csoportosítja és visszaadja egy {@code Map&lt;String, List &lt;String&gt;&gt;}
 * adatstruktúrában.
 * </p><p>
 * Csak azon csoportokat tároljuk, ahol legalább 2 szó van:
 * </p><blockquote>
 * Pl.: [lándzsa, nászdal, szandál]
 * </blockquote><p>
 * <b>Comments:</b>
 * </p><p>
 * Based on my initial testing, the {@link #getAnagrams(File)} method
 * needs <b>324 ms</b> on average to process the provided
 * {@code input.txt}. The testing method used was {@link #main(String[])}
 * as is.
 * </p><p>
 * <b>IMPORTANT:</b> Before running the program, change the
 * <b>project encoding</b> to {@code windows-1258}, as this was the encoding
 * used for the {@code input.txt}!
 * </p>
 *
 * @author Szabolcs Bazil Papp <papp.szabolcs.bazil@gmail.com>
 * @version 1.0
 * @since 17.0.2
 */
public class AnagramFinder {

    /**
     * Provides a Map of the anagrams found in the specified
     * file.
     * <p>
     * <i>The format of the file should comply with the one
     * provided for the exercise.</i>
     * </p>
     *
     * @param wordlist a {@code File} to be treated as a
     *                 dictionary
     * @return a {@code Map&lt;String, List &lt;String&gt;&gt;}
     *         containing the anagrams found in the wordlist.
     */
    public Map<String, List<String>> getAnagrams(File wordlist) {
        List<String> words = getWords(wordlist);
        List<String> tokens = new ArrayList<>(words.size());

        for (String word : words) {
            tokens.add(orderLetters(word));
        }

        Map<String, List<String>> candidates = new HashMap<>();
        for (int i = 0; i < tokens.size(); i++) {
            if (candidates.containsKey(tokens.get(i))) {
                addItemToListMap(candidates, tokens.get(i), words.get(i));
            } else {
                ArrayList<String> al = new ArrayList<>();
                al.add(words.get(i));
                candidates.put(tokens.get(i), al);
            }
        }

        Map<String, List<String>> result = new HashMap<>();
        for (String key : candidates.keySet()) {
            if (candidates.get(key).size() > 1) {
                result.put(key, candidates.get(key));
            }
        }
        return result;
    }

    /**
     * Reads the specified file to construct an {@code ArrayList}
     * of valid words.
     *
     * @param wordlist a {@code File} containing valid words.
     *                 <p><i>
     *                 Every Line should contain one word only.
     *                 </i></p>
     * @return a {@code List<String>} of the words the parameter
     *         contains
     */
    private ArrayList<String> getWords(File wordlist) {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(wordlist))) {
            String word;
            while ((word = br.readLine()) != null) {
                words.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return words;
    }

    /**
     * Creates a copy of a {@code String} with its
     * characters sorted.
     *
     * @param str a {@code String}
     * @return sorted copy of {@code str}
     */
    private String orderLetters(String str) {
        char[] arr = str.toCharArray();
        Arrays.sort(arr);
        return new String(arr);
    }

    /**
     * Given a {@link Map} with keys of type {@code K} and
     * values of {@code Lists of type V}, a new element is
     * appended to the {@code List} found at the specified
     * key.
     *
     * @param map      a {@code Map&lt;K, List &lt;V&gt;&gt;}
     * @param key      a key whose associated value list is
     *                 appended
     * @param newValue a new {@code V} element.
     * @param <K>      type of the map's key-set.
     * @param <V>      type of the map's
     *                 {@code List&lt;V&gt;} value-set.
     */
    private <K, V> void addItemToListMap(Map<K, List<V>> map, K key, V newValue) {
        List<V> tempList = map.get(key);
        tempList.add(newValue);
        map.replace(key, tempList);
    }

    // Driver method for testing purposes only.
    public static void main(String[] args) {
        AnagramFinder af = new AnagramFinder();
        long start = Calendar.getInstance().getTimeInMillis();
        Map<String, List<String>> anagrams = af.getAnagrams(new File("resources\\input.txt"));
        long elapsed = Calendar.getInstance().getTimeInMillis() - start;
        System.out.println("Completed in " + elapsed + "ms");

        int i = 1;
        for (Map.Entry<String, List<String>> entry : anagrams.entrySet()) {
            System.out.println(i + ":\t" + entry.getValue().toString());
            i++;
        }
    }
}

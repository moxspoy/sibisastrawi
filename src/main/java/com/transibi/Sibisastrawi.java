package com.transibi;

import jsastrawi.morphology.DefaultLemmatizer;
import jsastrawi.morphology.Lemmatizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Sibisastrawi {
    private final static String ROOT_WORDS_URL = "/root-words.txt";
    //private final static String SIMPLE_SOURCE_SAMPLE_TEXT = "/source1.txt";
    private static Set<String> dictionary;
    private static Lemmatizer lemmatizer;
    private static Sibisastrawi sibisastrawi = new Sibisastrawi();

    public Sibisastrawi() {
        dictionary = new HashSet<>();

        try {
            InputStream in = Sibisastrawi.class.getResourceAsStream(ROOT_WORDS_URL);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = br.readLine()) != null) {
                dictionary.add(line);
            }
            lemmatizer = new DefaultLemmatizer(this.dictionary);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Sibisastrawi(String[] rootWords) {
        dictionary = new LinkedHashSet<>(Arrays.asList(rootWords));
        lemmatizer = new DefaultLemmatizer(dictionary);
    }


    private static void stemSingleFile(String PATH) {

        List<String> wordList = generateSourceFromFile(PATH);

        System.out.println("\nTotal jumlah kata: " + wordList.size() + " kata" +
                "\n\nKATA ASLI,KATA HASIL STEM");

        long startTime = System.currentTimeMillis();

        int counter = 1;
        for (String s : wordList) {
            s = removeUnecessaryCharacter(s);
            String stemResult = sibisastrawi.stem(s.toLowerCase());
            String existInDictionary = dictionary.contains(stemResult) ? "Ya" : "Tidak";
            System.out.println(counter + "," + s.toLowerCase() + "," + stemResult + "," + existInDictionary);
            counter++;
        }

        long endTime = System.currentTimeMillis();
        long executedTime = endTime - startTime;

        System.out.println("\nTotal Waktu Yang Dihabiskan: " + executedTime + " ms");
    }

    public static String stem(String s) {
        String stemResult = lemmatizer.lemmatize(s);
        if (!dictionary.contains(s)) {
            stemResult = generatePrefixBack(s, stemResult);
            stemResult = generateSuffixBack(s, stemResult);
        } else {
            stemResult = s;
        }
        return stemResult;
    }

    private static String removeUnecessaryCharacter(String s) {
        s = s.replace("\"", "").replace("[", "").replace("]", "");
        s = s.replaceAll("[():|/,.!]", "");
        return s;
    }

    private static List<String> generateSource() {
        List<String> wordList = new ArrayList<>();

        String text = "menerawang menerangkanlah menalangi menerangi";
        String[] splittedText = text.split(" ");
        wordList = Arrays.asList(splittedText);

        return wordList;
    }

    private static List<String> generateSourceFromFile(String PATH) {
        List<String> wordList = new ArrayList<>();

        try {
            InputStream in = Sibisastrawi.class.getResourceAsStream(PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                String[] wordsInLine = line.split(" ");
                for (String s : wordsInLine) {
                    //prevent empty word and duplicate word
                    if (s.isEmpty() || wordList.contains(s)) {
                        continue;
                    }
                    wordList.add(s);
                }
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return wordList;
    }

    private static void printAuthor() {
        Date date = new Date();
        System.out.println("HASIL ALGORITMA STEMMING SIBISASTRAWI");
        System.out.println("Generated at " + date.toString());
        System.out.println("@Author: M Nurilman Baehaqi");
        System.out.println("@Institution: Universitas Negeri Jakarta");
    }


    private static String generatePrefixBack(String s, String stemResult) {

        //first check if s is contain in dictionary
        if (!dictionary.contains(s)) {
            StringBuilder prefixResult = new StringBuilder();
            if (s.length() > 2) {
                String twoFirstCharSequence = s.substring(0, 2);
                //System.out.println("twoFirstCharSequence in prefix: " + twoFirstCharSequence);
                if (s.startsWith("di")) {
                    prefixResult.append("di ");
                    generateDerivationPrefixBack(s.replace(twoFirstCharSequence, ""), prefixResult);
                } else if (s.startsWith("ke")) {
                    prefixResult.append("ke ");
                    generateDerivationPrefixBack(s.replace(twoFirstCharSequence, ""), prefixResult);
                } else if (s.startsWith("se")) {
                    prefixResult.append("se ");
                    generateDerivationPrefixBack(s.replace(twoFirstCharSequence, ""), prefixResult);
                }

                generateDerivationPrefixBack(s, prefixResult);

                stemResult = prefixResult.toString() + stemResult;
            }

        }

        //System.out.println("result from generatePrefixBack: " + stemResult);
        return stemResult;
    }

    private static String generateDerivationPrefixBack(String s, StringBuilder prefixResult) {

        //Prevent output like = "me be beri kan" from input "memberikan"
        //should check if word before inflection and derivation suffix is exist in dictionary
        boolean checkWordAfterDelSuffix = isWordExistInDictionaryAfterDeleteSuffix(s);
        if (!checkWordAfterDelSuffix) {
            if (s.startsWith("me")) {
                prefixResult.append("me ");
            } else if (s.startsWith("te")) {
                prefixResult.append("te ");
            } else if (s.startsWith("be")) {
                prefixResult.append("be ");
            } else if (s.startsWith("pe")) {
                prefixResult.append("pe ");
            }
        }


        return prefixResult.toString();
    }

    private static boolean isWordExistInDictionaryAfterDeleteSuffix(String s) {
        int len = s.length() - 1;
        if (len <= 3) {
            if (dictionary.contains(s)) {
                return true;
            } else {
                return false;
            }
        }
        String threeCharSequence = s.substring(len - 2);
        String twoCharSequence = s.substring(len - 1);
        String tempString = "";

        if (s.endsWith("lah")) {
            tempString = removeDerivationSuffix(s.replace(threeCharSequence, ""));
        } else if (s.endsWith("kah")) {
            tempString = removeDerivationSuffix(s.replace(threeCharSequence, ""));
        } else if (s.endsWith("ku")) {
            tempString = removeDerivationSuffix(s.replace(twoCharSequence, ""));
        } else if (s.endsWith("mu")) {

            tempString = removeDerivationSuffix(s.replace(twoCharSequence, ""));
        } else if (s.endsWith("nya")) {
            tempString = removeDerivationSuffix(s.replace(threeCharSequence, ""));
        }

        if (dictionary.contains(tempString)) {
            return true;
        } else {
            return false;
        }
    }

    private static String removeDerivationSuffix(String s) {
        int len = s.length() - 1;
        if (len <= 3) {
            return s;
        }
        String threeCharSequence = s.substring(len - 2);
        String twoCharSequence = s.substring(len - 1);
        String oneCharSequence = s.substring(len);

        if (s.endsWith("i")) {
            s = s.replace(oneCharSequence, "");
        } else if (s.endsWith("kan")) {
            s = s.replace(threeCharSequence, "");
        } else if (s.endsWith("an")) {
            s = s.replace(twoCharSequence, "");
        }
        return s;
    }

    private static String generateSuffixBack(String s, String stemResult) {

        //first check whether current word in current process is available in dictionary
        //Example input: ke be manfaat
        //Output: manfaat
        String[] wordsOfStemmResult = stemResult.split(" ");
        String lastPartOfStemmResult = wordsOfStemmResult[wordsOfStemmResult.length - 1];
        if (dictionary.contains(lastPartOfStemmResult) && !s.endsWith(lastPartOfStemmResult)) {
            StringBuilder tempSuffix = new StringBuilder();
            String tempInflectionResult = "";

            int len = s.length() - 1;
            String threeCharSequence = s.substring(len - 2);
            String twoCharSequence = s.substring(len - 1);

            if (s.endsWith("lah")) {
                tempSuffix.append("lah");
                tempInflectionResult = generateInflectionSuffixBack(s.replace(threeCharSequence, ""));
                tempSuffix.insert(0, tempInflectionResult);
            } else if (s.endsWith("kah")) {
                tempSuffix.append("kah");
                tempInflectionResult = generateInflectionSuffixBack(s.replace(threeCharSequence, ""));
                tempSuffix.insert(0, tempInflectionResult);
            } else if (s.endsWith("ku")) {
                tempSuffix.append("aku");
                tempInflectionResult = generateInflectionSuffixBack(s.replace(twoCharSequence, ""));
                tempSuffix.insert(0, tempInflectionResult);
            } else if (s.endsWith("mu")) {
                tempSuffix.append("kamu");
                tempInflectionResult = generateInflectionSuffixBack(s.replace(twoCharSequence, ""));
                tempSuffix.insert(0, tempInflectionResult);
            } else if (s.endsWith("nya")) {
                tempSuffix.append("nya");
                tempInflectionResult = generateInflectionSuffixBack(s.replace(threeCharSequence, ""));
                tempSuffix.insert(0, tempInflectionResult);
            }
            tempInflectionResult = generateInflectionSuffixBack(s);
            tempSuffix.insert(0, tempInflectionResult);
            //System.out.println("tempSuffix: " + tempSuffix.toString());
            stemResult = stemResult + " " + tempSuffix.toString();
        }

        return stemResult;
    }

    private static String generateInflectionSuffixBack(String s) {
        if (s.endsWith("i")) {
            s = "i ";
        } else if (s.endsWith("kan")) {
            s = "kan ";
        } else if (s.endsWith("an")) {
            s = "an ";
        } else {
            s = "";
        }
        return s;
    }

    public static void main(String[] args) {
        final int MAX_SAMPLE_TEXT = 10;
        String[] PATHS = new String[11K];

        //Print meta data
        printAuthor();

        //Setting source file name
        for (int i = 1; i <= MAX_SAMPLE_TEXT; i++) {
            PATHS[i] = "/source" + i + ".txt";
            System.out.println("\n\nPERCOBAAN KE " + i);
            stemSingleFile(PATHS[i]);
        }

    }
}

/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Trieda pre načítanie .json súboru pre získanie riadkov
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public abstract class JSONLoader {
    /**
     * Načíta zadaný súbor do JSON objektu
     * @param filePath cesta
     * @return JSON Objekt
     * @throws Exception e
     */
    public static JSONObject load(String filePath) throws Exception {
        return parseFile(openFile(filePath));
    }

    /**
     * Načíta všetko z čítača do JSON objektu
     * @param fileReader čítač pre načítanie
     * @return JSON Objekt
     * @throws Exception
     */
    private static JSONObject parseFile(FileReader fileReader) throws Exception {
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(fileReader);
    }

    /**
     * Otvorí súbory a vytvorí nový objekt pre čítanie
     * @param filePath cesta
     * @return čítač
     * @throws Exception
     */
    private static FileReader openFile(String filePath) throws Exception {
        File file1 = new File(filePath);
        FileReader reader = null;
        try {
            reader = new FileReader(file1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Súbor neexistuje...");
        }
        return reader;
    }

}
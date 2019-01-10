package com.example.hvalfisk.notsosuckyhangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class HangManLogic {

  ArrayList<String> possibleWords = new ArrayList<String>();
  private String word;
  private ArrayList<String> usedLetters = new ArrayList<String>();
  private String visibleWord;
  private int amountWrongLetters;
  private boolean letterContained;
  private boolean gameWon;
  private boolean gameLost;


  public ArrayList<String> getUsedLetters() {
    return usedLetters;
  }

  public String getVisibleWord() {
    return visibleWord;
  }

  public String getWord() {
    return word;
  }

  public int getAmountWrongLetters() {
    return amountWrongLetters;
  }

  public boolean isLetterContained() {
    return letterContained;
  }

  public boolean isGameWon() {
    return gameWon;
  }

  public boolean isGameLost() {
    return gameLost;
  }

  public boolean isGameFinished() {
    return gameLost || gameWon;
  }


  public HangManLogic() {
    possibleWords.add("bil");
    possibleWords.add("computer");
    possibleWords.add("programmering");
    possibleWords.add("motorvej");
    possibleWords.add("busrute");
    possibleWords.add("gangsti");
    possibleWords.add("skovsnegl");
    possibleWords.add("solsort");
    possibleWords.add("seksten");
    possibleWords.add("sytten");
    possibleWords.add("atten");
    possibleWords.add("møøse");
    reset();
  }

  public void reset() {
    usedLetters.clear();
    amountWrongLetters = 0;
    gameWon = false;
    gameLost = false;
    word = possibleWords.get(new Random().nextInt(possibleWords.size()));
    updateVisibleWord();
  }


  private void updateVisibleWord() {
    visibleWord = "";
    gameWon = true;
    for (int n = 0; n < word.length(); n++) {
      String letter = word.substring(n, n + 1);
      if (usedLetters.contains(letter)) {
        visibleWord = visibleWord + letter;
      } else {
        visibleWord = visibleWord + "*";
        gameWon = false;
      }
    }
  }

  public boolean guessLetter(String letter) {
    if (letter.length() != 1) return false;
    System.out.println("Der gættes på bogstavet: " + letter);
    if (usedLetters.contains(letter)) return false;
    if (gameWon || gameLost) return false;

    usedLetters.add(letter);

    if (word.contains(letter)) {
      letterContained = true;
      System.out.println("Bogstavet var korrekt: " + letter);
      updateVisibleWord();
      return false;
    } else {
      // Vi gættede på et bogstav der ikke var i word.
      letterContained = false;
      System.out.println("Bogstavet var IKKE korrekt: " + letter);
      amountWrongLetters = amountWrongLetters + 1;
      if (amountWrongLetters > 5) {
        gameLost = true;
      }
      updateVisibleWord();
      return true;
    }

  }

  public void logStatus() {
    System.out.println("---------- ");
    System.out.println("- word (skult) = " + word);
    System.out.println("- visibleWord = " + visibleWord);
    System.out.println("- forkerteBogstaver = " + amountWrongLetters);
    System.out.println("- brugeBogstaver = " + usedLetters);
    if (gameLost) System.out.println("- SPILLET ER TABT");
    if (gameWon) System.out.println("- SPILLET ER VUNDET");
    System.out.println("---------- ");
  }


  public static String getUrl(String url) throws IOException {
    System.out.println("Henter data fra " + url);
    BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
    StringBuilder sb = new StringBuilder();
    String line = br.readLine();
    while (line != null) {
      sb.append(line + "\n");
      line = br.readLine();
    }
    return sb.toString();
  }


  public void getWordsFromDR() throws Exception {
    String data = getUrl("https://dr.dk");
    //System.out.println("data = " + data);

    data = data.substring(data.indexOf("<body")). // fjern headere
            replaceAll("<.+?>", " ").toLowerCase(). // fjern tags
            replaceAll("&#198;", "æ"). // erstat HTML-tegn
            replaceAll("&#230;", "æ"). // erstat HTML-tegn
            replaceAll("&#216;", "ø"). // erstat HTML-tegn
            replaceAll("&#248;", "ø"). // erstat HTML-tegn
            replaceAll("&oslash;", "ø"). // erstat HTML-tegn
            replaceAll("&#229;", "å"). // erstat HTML-tegn
            replaceAll("[^a-zæøå]", " "). // fjern tegn der ikke er bogstaver
            replaceAll(" [bcdfghjklmnpqrstvxz]+ "," "). //remove all words without vowels
            replaceAll(" [a-zæøå] "," "). // fjern 1-bogstavsord
            replaceAll(" [a-zæøå][a-zæøå] "," "); // fjern 2-bogstavsord

    System.out.println("data = " + data);
    System.out.println("data = " + Arrays.asList(data.split("\\s+")));
    possibleWords.clear();
    possibleWords.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));

    System.out.println("possibleWords = " + possibleWords);
    reset();
  }

    public Boolean getLetterContained() {
      return letterContained;
    }

    public int getRemainingGuesses() {
      return 6-amountWrongLetters;
    }

    public String getUsedLettersString() {
      String string = "";
      for (String s: usedLetters) {
        string = string + s;
      }

      return string;
    }
}

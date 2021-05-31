
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class GuessPokemon {
    public static String guessing;
    public static String chosenOne = "";
    public static int points = 0;
    public static String[] hangman = {"","O\n","O\n|","O\n-|","O\n-|-","O\n-|-\n/","O\n-|-\n/\\","GAME OVER"};
    public String setup() throws FileNotFoundException {
        guessing = "";
        int idx = 0;
        String noNumName = "";
        Random rand = new Random();
        int pokedexNum = rand.nextInt(898);
        String[] pokemon_array = new String[898];
        File myFile = new File("/Users/pengmong/csci1933/GuessPokemon/src/pokedex.txt");
        Scanner myReader = new Scanner(myFile);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            data = data.replace("1","");data = data.replace("2","");data = data.replace("3","");
            data = data.replace("4","");data = data.replace("5","");data = data.replace("6","");
            data = data.replace("7","");data = data.replace("8","");data = data.replace("9","");
            data = data.replace("0","");data = data.replace("-","");
            for(int j = 0; j < data.length();j++){
                if(data.charAt(j) != ' '){
                    noNumName += String.valueOf(data.charAt(j));
                }
            }
            pokemon_array[idx] = noNumName;
            noNumName = "";
            idx++;
        }
        myReader.close();
        chosenOne = pokemon_array[pokedexNum];
        for(int i = 0; i < chosenOne.length();i++){
            guessing += " _ ";
        }
        return guessing;
    }
    public static void main(String args[]) throws FileNotFoundException {
        GuessPokemon newOne = new GuessPokemon();
        newOne.setup();
    }
}

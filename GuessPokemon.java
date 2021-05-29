
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class GuessPokemon {
    public static String guessing = "";
    public static String chosenOne = "";
    public static int points = 0;
    public static String[] hangman = {"","O\n","O\n|","O\n-|","O\n-|-","O\n-|-\n/","O\n-|-\n/\\","GAME OVER"};
    public String setup() throws FileNotFoundException {
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
        /*
        System.out.println("Guess the Pokemon! Try to guess what Pokemon I'm thinking of!\n\n" +
                "How to Play:\nType in the corresponding number of whatever information you would like to recieve about the Pokemon."
                + "\n\nRules: All information that you recieve will cost you points.\nYou will start out with a total of 10 points and " +
                "if you ever go below 0 points, you lose. If you guess correctly you will earn 10 points but if you guess\nincorrectly you will lose 5 points. Good luck!\n");
        int pointCount = 10;
        while (true) {
            Random rand = new Random();
            String nameTracker = "";
            String typeTracker = "";
            String categoryTracker = "";
            String heightTracker = "";
            String weightTracker = "";
            boolean firstLoop = true;
            boolean secondLoop = true;
            int whichPokemon = rand.nextInt(10);
            Pocket chosenOne = list.pokedex[whichPokemon];
            int letterPoint = 1;
            for (int a = 0; a < (chosenOne.PokName).length(); a++) {
                nameTracker += " _ ";
            }
            while (firstLoop == true) {
                System.out.print("Information you want about the Pokemon:\n" +
                        "1. Give me a random letter in the Pokemon's name (Cost: " + letterPoint + " points)         // Note: Cost of this will increase with each use\n");
                System.out.print("2. Type (Cost: 2 points)\n");
                System.out.print("3. Height in ft (Cost: 1 point)\n");
                System.out.print("4. Weight in pounds (Cost: 1 point)\n");
                System.out.print("5. Category (Cost: 4 points)\n");
                System.out.println("\nYour Points: " + pointCount + "\n");

                String sf1 = String.format("Pokemon: %s", nameTracker);
                System.out.println(sf1);
                String sf2 = String.format("Type: %s", typeTracker);
                System.out.println(sf2);
                String sf3 = String.format("Height: %s ft", heightTracker);
                System.out.println(sf3);
                String sf4 = String.format("Weight: %s lbs", weightTracker);
                System.out.println(sf4);
                String sf5 = String.format("Category: %s", categoryTracker);
                System.out.println(sf5);
                while (secondLoop == true) {
                Scanner myObj = new Scanner(System.in);  // Create a Scanner object
                System.out.println("Do you want to guess (Y/N): ");
                String guessYN = myObj.nextLine();
                    if (guessYN.equalsIgnoreCase("Y")) {
                        System.out.println("Who's that Pokemon!?\n--------------------");
                        String userGuess = myObj.nextLine();
                        if (userGuess.equalsIgnoreCase(chosenOne.PokName)) {
                            System.out.println("You are Correct!!! +10");
                            pointCount += 10;
                            while(true) {
                                System.out.println("Continue the Game? (Y/N)");
                                String continueGame = myObj.nextLine();
                                if (continueGame.equalsIgnoreCase("N")) {
                                    System.out.println("Your Final Score is " + pointCount + "!");
                                    System.exit(0);
                                } else if (continueGame.equalsIgnoreCase("Y")) {
                                    secondLoop = false;
                                    firstLoop = false;
                                    break;
                                } else {
                                    System.out.println("Invalid Option. Please try again.");
                                }
                            }
                        } else {
                            System.out.println("WRONG! Minus 5 points!");
                            pointCount -= 5;
                        }
                        break;
                    } else if (guessYN.equalsIgnoreCase("N")) {
                        while (true) {
                            System.out.println("Please enter an integer:");
                            String infoNum = myObj.nextLine();  // Read user input
                            if (infoNum.equals("3") == true) {
                                System.out.println("The pokemon's height is about " + chosenOne.height + "feet\n");
                                pointCount -= 1;
                                heightTracker = String.valueOf(chosenOne.height);
                                System.out.println("-------------------------------------------------------------------");
                                break;
                            } else if (infoNum.equals("4") == true) {
                                System.out.println("The pokemon's weight is about" + chosenOne.weight + "pounds\n");
                                pointCount -= 1;
                                weightTracker = String.valueOf(chosenOne.weight);
                                System.out.println("-------------------------------------------------------------------");
                                break;
                            } else if (infoNum.equals("2") == true) {
                                System.out.println("The pokemon's type is " + chosenOne.type + "\n");
                                pointCount -= 2;
                                typeTracker = chosenOne.type;
                                System.out.println("-------------------------------------------------------------------");
                                break;
                            } else if (infoNum.equals("5") == true) {
                                System.out.println("The pokemon's category is " + chosenOne.category + "\n");
                                pointCount -= 4;
                                categoryTracker = chosenOne.category;
                                System.out.println("-------------------------------------------------------------------");
                                break;
                            } else if (infoNum.equals("1") == true) {
                                pointCount -= letterPoint;
                                letterPoint += 2;
                                while (true) {
                                    Random rand2 = new Random();
                                    int randIndex = rand2.nextInt(chosenOne.PokName.length());
                                    String compString = " " + nameTracker.charAt(randIndex) + " ";
                                    if (compString.equals(" _ ")) {
                                        if (randIndex == 0) {
                                            nameTracker = chosenOne.PokName.charAt(0) + nameTracker.substring(1, chosenOne.PokName.length());
                                        } else {
                                            nameTracker = nameTracker.substring(0, randIndex) + chosenOne.PokName.charAt(randIndex) + nameTracker.substring(randIndex + 1, chosenOne.PokName.length());
                                        }
                                        break;
                                    }
                                }
                                System.out.println("-------------------------------------------------------------------");
                                break;
                            } else {
                                System.out.println("Invalid Option. Please try again.");
                            }
                        }
                        break;
                    } else {
                        System.out.println("Invalid Option. Please try again.");
                    }
                }
            }
        }
    }
    public void getLetter(String pokemon){
        Random rand = new Random();
        int randIndex = rand.nextInt(pokemon.length());
        String compString = " " + curLetters.charAt(randIndex) + " ";
        if (compString.equals(" _ ")) {
            if (randIndex == 0) {
                nameTracker = chosenOne.PokName.charAt(0) + nameTracker.substring(1, chosenOne.PokName.length());
            } else {
                nameTracker = nameTracker.substring(0, randIndex) + chosenOne.PokName.charAt(randIndex) + nameTracker.substring(randIndex + 1, chosenOne.PokName.length());
            }
            */
    }
}

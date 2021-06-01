import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class gui extends JFrame implements ActionListener {
    public static GuessPokemon start = new GuessPokemon();      // get an instance of GuessPokemon class
    public static JFrame frame = new gui();                     // main frame that holds all the components
    public static JPanel topPanel = new JPanel();               // panel that holds the points and quit button
    public static JPanel namePanel = new JPanel();              // panel that holds the underscores and letters that have been guessed so far
    public static JPanel letters_panel = new JPanel();          // panel to hold all the letter buttons
    public static JButton[] letterButtons = new JButton[26];    // actual letter buttons
    public static JTextField write_guess = new JTextField( 20);      // TextField for guessing
    public static int wrong = 0;                                // an array index that we'll use to help display hangman
    public static int letters_left = 0;                         // the amount of letters that the user hasn't guessed yet

    public gui(){
        this.getContentPane().setLayout(new BorderLayout());
    }

    public static void layLetters(){                                // layout the letter buttons for user to click on to guess the name of pokemon
        int idx = 0;
        letters_panel.setLayout(new GridLayout (7,4));
        for(int i = 0;i<26;i++){
            if(letterButtons[i] != null) {
                letters_panel.remove(letterButtons[i]);
            }
        }
        for(char ch = 'A'; ch <= 'Z';ch++){
            JButton letter = new JButton(""+ ch);
            letter.addActionListener((ActionListener) frame);
            letter.setActionCommand(""+ch);
            letter.setPreferredSize(new Dimension(40,20));
            letters_panel.add(letter);
            letterButtons[idx] = letter;
            idx++;
        }
        frame.add(letters_panel,BorderLayout.SOUTH);
    }
    public static void showGUI() throws FileNotFoundException {               // start the game off by laying out the majority of the GUI
        String setup = start.setup();                                         // function in GuessPokemon.java that chooses our pokemon
        letters_left = GuessPokemon.chosenOne.length();
        System.out.println(GuessPokemon.chosenOne);

        topPanel.setLayout(new FlowLayout ());
        TextField points = new TextField("Pointos: " + GuessPokemon.points, 10);      // construct the TextField component with initial text
        points.setEditable(false);                                            // set to read-only
        topPanel.add(points);
        JButton quit = new JButton("QUIT");
        quit.addActionListener((ActionListener) frame);
        quit.setActionCommand("quit");
        topPanel.add(quit);

                                                                            // layout the panel containing the pokemon name/letters
        namePanel.setLayout(new BoxLayout(namePanel,BoxLayout.PAGE_AXIS));
        JTextPane name = new JTextPane();
        name.setText(setup);
        StyledDocument doc1 = name.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc1.setParagraphAttributes(0, doc1.getLength(), center, false);
        name.setEditable(false);
        name.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        namePanel.add(name);

        JTextPane stickman = new JTextPane();                              // layout the hangman figure
        stickman.setText(GuessPokemon.hangman[0]);
        StyledDocument doc2 = stickman.getStyledDocument();
        doc2.setParagraphAttributes(0, doc2.getLength(), center, false);
        stickman.setFont(new Font("TimesRoman", Font.PLAIN, 60));
        stickman.setEditable(false);
        namePanel.add(stickman);

        JPanel write_guess_panel = new JPanel();                            // set up the text field where the user can type in a guess
        write_guess_panel.setLayout(new FlowLayout());
        JTextField guess_prompt = new JTextField("Guess (First letter is capital and rest aren't): ", 24);
        guess_prompt.setEditable(false);
        write_guess_panel.add(guess_prompt);
        write_guess_panel.add(write_guess);
        write_guess.addActionListener((ActionListener) frame);
        write_guess.setActionCommand("text");
        namePanel.add(write_guess_panel);

        layLetters();                               // function that lays out the buttons (containing the letters) onto the GUI

        frame.add(topPanel,BorderLayout.PAGE_START);                // add the different components to the main frame
        frame.add(namePanel,BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,600);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String pressed = e.getActionCommand();
        boolean found = false;
        if(pressed.equals("text")){
            if(write_guess.getText().equals(GuessPokemon.chosenOne)){       // if user correctly guesses name
                GuessPokemon.guessing = GuessPokemon.chosenOne;             // this lets our program know further down into the code that user has found the right answer
                GuessPokemon.points += letters_left;                        // add the appropriate amount of points that the user gets by guessing correctly
            }else{
                GuessPokemon.points -= letters_left;                        // if user guessed wrong then minus points and add to the hangman
                wrong++;                                                    //   figure (wrong indexes into an array containing the hangman pieces)
            }
        }else {
            if (pressed.equals("cont") || pressed.equals("again")) {        // this happens when a new game must be started (user either won or lost)
                wrong = 0;                                                  // set the hangman back to its original state
                start = new GuessPokemon();                                 // get a new instance of GuessPokemon class
                try {
                    start.setup();                                          // same thing as showGUI() but we're not creating a new frame, just updating components oof the old frame
                    write_guess.setText("");
                    letters_left = GuessPokemon.chosenOne.length();
                    System.out.println(GuessPokemon.chosenOne);
                    layLetters();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                if (pressed.equals("again")) {                //reset points only if game over and player wants to play again
                    GuessPokemon.points = 0;
                }
            } else {
                if (pressed.equals("quit")) {                 // if the user clicks the "QUIT" button
                    System.exit(0);
                } else {
                    if (wrong != 7) {                                                    // if wrong != 7 then that means the hangman is not complete yet (still have turns)
                        for (int i = 0; i < GuessPokemon.chosenOne.length(); i++) {      // this loop is essentially checking if the letter is in the name or not
                            if (pressed.charAt(0) == GuessPokemon.chosenOne.charAt(i)) {                                       // check for uppercase character in pokemon name
                                GuessPokemon.guessing = newGuess(GuessPokemon.guessing, pressed.charAt(0), i * 3 + 1);
                                GuessPokemon.points += 1;
                                letters_left -= 1;
                                found = true;
                            } else {
                                if (pressed.toLowerCase().charAt(0) == GuessPokemon.chosenOne.charAt(i)) {                         //check for lowercase character in pokemon name
                                    GuessPokemon.guessing = newGuess(GuessPokemon.guessing, pressed.charAt(0), i * 3 + 1);
                                    GuessPokemon.points += 1;
                                    letters_left -= 1;
                                    found = true;
                                } else {                                                                                        // if the letter is not in the name
                                    if (i == GuessPokemon.chosenOne.length() - 1 && found == false) {
                                        wrong++;
                                        GuessPokemon.points -= 1;
                                    }
                                }
                            }
                        }
                        letters_panel.remove(letterButtons[pressed.charAt(0) - 65]);                 //remove the button so it can't be clicked again
                    }
                }
            }
        }
        update_namePanel();
        updatePoints();
        checkGuess();
    }

    public String newGuess(String fullName,char letter,int index){      // function to create and find new output every time user guesses a letter
        String first_half = "";                                         // output in this case are the underscores and letters (ex: P _ _ A C H _)
        String second_half = "";
        for(int i = 0; i < index;i++){                                  // get all the characters up to the point in the name where we found the given letter
            first_half += fullName.charAt(i);
        }
        for(int j = index + 1; j < fullName.length();j++){              // get all the characters after the point in the name where we found the given letter
            second_half += fullName.charAt(j);
        }
        String newString = first_half + letter + second_half;           // combine the first half with the second half except we add the given letter in between them
        return newString;
    }

    public void update_namePanel(){                                          // function to actually output the new guess/state of the game
        frame.remove(namePanel);                                             // very similar to showGUI() except just updating certain components
        JPanel namePanel2 = new JPanel();                                    // layout the panel containing the pokemon name/letters/underscores
        namePanel2.setLayout(new BoxLayout(namePanel2,BoxLayout.PAGE_AXIS));
        JTextPane name = new JTextPane();
        name.setText(GuessPokemon.guessing);
        StyledDocument doc1 = name.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc1.setParagraphAttributes(0, doc1.getLength(), center, false);
        name.setEditable(false);
        name.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        namePanel2.add(name);

        JTextPane stickman = new JTextPane();                                // layout the hangman figure
        String current_man = GuessPokemon.hangman[wrong];
        stickman.setText(current_man);
        StyledDocument doc2 = stickman.getStyledDocument();
        doc2.setParagraphAttributes(0, doc2.getLength(), center, false);
        stickman.setFont(new Font("TimesRoman", Font.PLAIN, 60));
        stickman.setEditable(false);
        namePanel2.add(stickman);

        JPanel write_guess_panel2 = new JPanel();                           // layout the text field for guessing
        write_guess_panel2.setLayout(new FlowLayout());
        JTextField guess_prompt2 = new JTextField("Guess (First letter is capital and rest aren't): ", 24);
        guess_prompt2.setEditable(false);
        write_guess_panel2.add(guess_prompt2);
        write_guess_panel2.add(write_guess);
        namePanel2.add(write_guess_panel2);

        if(wrong == 7){
            JButton play_again = new JButton("Play Again?");          // if the user has lost, give them the option to continue start a new game
            play_again.addActionListener(this);
            play_again.setActionCommand("again");
            namePanel2.add(play_again);
        }
        namePanel = namePanel2;
        frame.add(namePanel,BorderLayout.CENTER);                          // update the panel onto the frame
    }

    public void updatePoints(){                 // function to update the points accordingly
        frame.remove(topPanel);
        JPanel topPanel2 = new JPanel();
        topPanel2.setLayout(new FlowLayout ());
        TextField points = new TextField("Points: " + GuessPokemon.points, 10);
        points.setEditable(false);
        topPanel2.add(points);
        JButton quit = new JButton("QUIT");
        quit.addActionListener(this);
        quit.setActionCommand("quit");
        topPanel2.add(quit);
        topPanel = topPanel2;
        frame.add(topPanel,BorderLayout.PAGE_START);
    }

    public void checkGuess(){                 // checks to see if the player has guessed all the letters in the Pokemon name
        String guess_no_blanks = "";
        if(!GuessPokemon.guessing.equals(GuessPokemon.chosenOne)) {              // if GuessPokemon.guessing == GuessPokemon.chosenOne then the user has guessed the name correctly already (refer above comments)
            for (int i = 0; i < GuessPokemon.guessing.length(); i++) {                                    //get only the letters in the guess we have so far
                if (GuessPokemon.guessing.charAt(i) != ' ' && GuessPokemon.guessing.charAt(i) != '_') {   // so instead of "P _ K _ _ H U ",we get "P"
                    if (guess_no_blanks.equals("")) {
                        guess_no_blanks += GuessPokemon.guessing.charAt(i);
                    } else {
                        guess_no_blanks += GuessPokemon.guessing.toLowerCase().charAt(i); // Makes all the non-first letters lowercase instead of all caps
                    }
                }
            }
        }else{
            guess_no_blanks = GuessPokemon.chosenOne;                   // this happens if the user correctly typed in the pokemon's name in the guess text field
        }
        if(guess_no_blanks.equals(GuessPokemon.chosenOne)){             // cannot directly put this in the else statement above because user can win in multiple ways
            frame.remove(namePanel);                                    //  1. Guessing all the letters or 2. Typing in their guess correctly (both resulting in guess_no_blanks == GuessPokemon.chosenOne)
            JPanel namePanel2 = new JPanel();                           // layout the panel containing "You Win!" message
            namePanel2.setLayout(new BoxLayout(namePanel2,BoxLayout.PAGE_AXIS));
            JTextPane name = new JTextPane();
            name.setText("\n\n\nYOU WIN!");
            StyledDocument doc1 = name.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc1.setParagraphAttributes(0, doc1.getLength(), center, false);
            name.setEditable(false);
            name.setFont(new Font("TimesRoman", Font.PLAIN, 40));
            namePanel2.add(name);

            JButton continuation = new JButton("Continue>>>");                       // button to continue on to the next round if the user has won
            continuation.addActionListener(this);
            continuation.setActionCommand("cont");
            namePanel2.add(continuation);
            namePanel = namePanel2;
            frame.add(namePanel,BorderLayout.CENTER);
        }
    }
    public static void main(String args[]) throws FileNotFoundException {
        showGUI();
    }
}


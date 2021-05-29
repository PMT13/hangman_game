
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class gui extends JFrame implements ActionListener {
    public static JPanel topPanel = new JPanel();
    public static JPanel namePanel = new JPanel();
    public static JPanel letters_panel = new JPanel();
    public static JButton[] letterButtons = new JButton[26];
    public static JFrame frame = new gui();
    public static int wrong = 0;

    public gui(){
        this.getContentPane().setLayout(new BorderLayout());
    }

    public static void layLetters(){                                // layout the letters for user to click on to guess the name of pokemon
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
    public static void showGUI() throws FileNotFoundException {                         // layout the majority of the GUI
        GuessPokemon.guessing = "";
        GuessPokemon start = new GuessPokemon();
        String setup = start.setup();

        topPanel.setLayout(new FlowLayout ());
        TextField points = new TextField("Points: " + GuessPokemon.points, 10);      // construct the TextField component with initial text
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

        JTextField write_guess = new JTextField("Guess (First letter is capital and rest aren't): ", 70);      // construct the TextField component with initial text
        write_guess.addActionListener((ActionListener) frame);
        write_guess.setActionCommand("text");
        namePanel.add(write_guess);

        layLetters();

        frame.add(topPanel,BorderLayout.PAGE_START);
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
           System.out.println("Enter");
        }
        if(pressed.equals("cont") || pressed.equals("again")){
            wrong = 0;
            new gui();
            try {
                showGUI();                         // starts a new round by calling showGUI()
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            if(pressed.equals("again")){                //reset points only if game over and player wants to play again
                GuessPokemon.points = 0;
            }
            this.remove(frame);
        }else {
            if (pressed.equals("quit")) {
                System.exit(0);
            } else {
                if (wrong != 7) {
                    for (int i = 0; i < GuessPokemon.chosenOne.length(); i++) {
                        if (pressed.charAt(0) == GuessPokemon.chosenOne.charAt(i)) {                                       // check for uppercase character in pokemon name
                            GuessPokemon.guessing = newGuess(GuessPokemon.guessing, pressed.charAt(0), i * 3 + 1);
                            GuessPokemon.points += 1;
                            found = true;
                        } else {
                            if (pressed.toLowerCase().charAt(0) == GuessPokemon.chosenOne.charAt(i)) {                         //check for lowercase character in pokemon name
                                GuessPokemon.guessing = newGuess(GuessPokemon.guessing, pressed.charAt(0), i * 3 + 1);
                                GuessPokemon.points += 1;
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
        this.updateGuess();
        this.updatePoints();
        this.checkGuess();
    }

    public String newGuess(String fullName,char letter,int index){      // function to create new output every time user guesses a letter
        String first_half = "";
        String second_half = "";
        for(int i = 0; i < index;i++){
            first_half += fullName.charAt(i);
        }
        for(int j = index + 1; j < fullName.length();j++){
            second_half += fullName.charAt(j);
        }
        String newString = first_half + letter + second_half;
        return newString;
    }

    public void updateGuess(){                                          // function to actually output the new guess/state of the game
        this.remove(namePanel);
        JPanel namePanel2 = new JPanel();                                       // layout the panel containing the pokemon name/letters
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

        JTextPane stickman = new JTextPane();                                               // layout the hangman figure
        String current_man = GuessPokemon.hangman[wrong];
        stickman.setText(current_man);
        StyledDocument doc2 = stickman.getStyledDocument();
        doc2.setParagraphAttributes(0, doc2.getLength(), center, false);
        stickman.setFont(new Font("TimesRoman", Font.PLAIN, 60));
        stickman.setEditable(false);
        namePanel2.add(stickman);

        JTextField write_guess = new JTextField("Guess (First letter is capital and rest aren't): ", 70);      // construct the TextField component with initial text
        write_guess.addActionListener(this);
        write_guess.setActionCommand("text");
        namePanel2.add(write_guess);

        if(wrong == 7){
            JButton play_again = new JButton("Play Again?");                       // button to continue on with the game
            play_again.addActionListener(this);
            play_again.setActionCommand("again");
            namePanel2.add(play_again);
        }
        namePanel = namePanel2;
        this.add(namePanel,BorderLayout.CENTER);
    }

    public void updatePoints(){                 // function to update the points accordingly
        this.remove(topPanel);
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
        this.add(topPanel,BorderLayout.PAGE_START);
    }

    public String checkGuess(){                 // check to see if the player has guessed all the letters before losing
        String guess_no_blanks = "";
        if(!GuessPokemon.guessing.equals(GuessPokemon.chosenOne)) {
            for (int i = 0; i < GuessPokemon.guessing.length(); i++) {                                     //get only the letters in the guess we have so far
                if (GuessPokemon.guessing.charAt(i) != ' ' && GuessPokemon.guessing.charAt(i) != '_') {   // so instead of "P _ K _ _ H U ",we get "P"
                    if (guess_no_blanks.equals("")) {
                        guess_no_blanks += GuessPokemon.guessing.charAt(i);
                    } else {
                        guess_no_blanks += GuessPokemon.guessing.toLowerCase().charAt(i); // Makes all the non-first letters lowercase instead of all caps
                    }
                }
            }
        }else{
            guess_no_blanks = GuessPokemon.chosenOne;                   // this only happens if the user correctly typed in the pokemon's name in the guess textfield
        }
        if(guess_no_blanks.equals(GuessPokemon.chosenOne)){
            this.remove(namePanel);
            JPanel namePanel2 = new JPanel();                                       // layout the panel containing "You Win!" message
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

            JButton continuation = new JButton("Continue>>>");                       // button to continue on with the game
            continuation.addActionListener(this);
            continuation.setActionCommand("cont");
            namePanel2.add(continuation);
            namePanel = namePanel2;
            this.add(namePanel,BorderLayout.CENTER);
        }
        return guess_no_blanks;
    }
    public static void main(String args[]) throws FileNotFoundException {
        showGUI();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

public class PasswordGeneratorSwing extends JFrame {
    private JCheckBox lowercaseCheckBox;
    private JCheckBox uppercaseCheckBox;
    private JCheckBox digitsCheckBox;
    private JCheckBox specialCheckBox;
    private JTextField lengthField;
    private JTextArea passwordArea;

    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+";

    public PasswordGeneratorSwing() {
        setTitle("Password Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.LIGHT_GRAY);

        JPanel optionsPanel = new JPanel(new GridLayout(5, 1));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        optionsPanel.setBackground(Color.LIGHT_GRAY);

        lowercaseCheckBox = new JCheckBox("Include Lowercase");
        uppercaseCheckBox = new JCheckBox("Include Uppercase");
        digitsCheckBox = new JCheckBox("Include Digits");
        specialCheckBox = new JCheckBox("Include Special Characters");
        lengthField = new JTextField(10);

        Font largerFont = new Font(lowercaseCheckBox.getFont().getName(), Font.PLAIN, 14); 

        lowercaseCheckBox.setFont(largerFont);
        uppercaseCheckBox.setFont(largerFont);
        digitsCheckBox.setFont(largerFont);
        specialCheckBox.setFont(largerFont);

        optionsPanel.add(lowercaseCheckBox);
        optionsPanel.add(uppercaseCheckBox);
        optionsPanel.add(digitsCheckBox);
        optionsPanel.add(specialCheckBox);
        optionsPanel.add(new JLabel("Password Length:"));
        optionsPanel.add(lengthField);

        JButton generateButton = new JButton("Generate Password");
        JButton clearButton = new JButton("Clear");

        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generatePassword();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(generateButton);
        buttonPanel.add(clearButton);

        passwordArea = new JTextArea(5, 20);
        passwordArea.setEditable(false);
        passwordArea.setBackground(Color.LIGHT_GRAY);

        add(optionsPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(passwordArea), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void generatePassword() {
        int passwordLength = Integer.parseInt(lengthField.getText());
        boolean useLowercase = lowercaseCheckBox.isSelected();
        boolean useUppercase = uppercaseCheckBox.isSelected();
        boolean useDigits = digitsCheckBox.isSelected();
        boolean useSpecial = specialCheckBox.isSelected();

        String generatedPassword = generatePassword(passwordLength, useLowercase, useUppercase, useDigits, useSpecial);
        passwordArea.setText(generatedPassword);
    }

    private String generatePassword(int length, boolean useLowercase, boolean useUppercase, boolean useDigits, boolean useSpecial) {
        SecureRandom random = new SecureRandom();
        StringBuilder characters = new StringBuilder();

        if (useLowercase) {
            characters.append(LOWERCASE_CHARACTERS);
        }
        if (useUppercase) {
            characters.append(UPPERCASE_CHARACTERS);
        }
        if (useDigits) {
            characters.append(DIGITS);
        }
        if (useSpecial) {
            characters.append(SPECIAL_CHARACTERS);
        }

        if (characters.length() == 0) {
            throw new IllegalArgumentException("At least one character set must be selected.");
        }

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }

    private void clearFields() {
        lowercaseCheckBox.setSelected(false);
        uppercaseCheckBox.setSelected(false);
        digitsCheckBox.setSelected(false);
        specialCheckBox.setSelected(false);
        lengthField.setText("");
        passwordArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PasswordGeneratorSwing().setVisible(true);
            }
        });
    }
}

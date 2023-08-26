
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class Calculator extends JFrame implements ActionListener {
    private JTextField textField;
    private String input = "";
    private boolean newCalculation = true;

    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400); // Smaller window size
        setLocationRelativeTo(null);

        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 24)); // Smaller font size
        textField.setEditable(false);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setBorder(new EmptyBorder(0, 10, 10, 10)); // Add padding to the text field (bottom padding)

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5)); // Default gaps between buttons
        String[] buttons = {
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "*",
            "0", ".", "=", "/",
            "C", "<-"
        };

        for (String button : buttons) {
            JButton btn = new JButton(button);
            btn.addActionListener(this);
            btn.setFont(new Font("Arial", Font.PLAIN, 18)); // Smaller font size
            btn.setFocusPainted(false); // Remove button focus border
            buttonPanel.add(btn);
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(textField, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (newCalculation) {
            if (Character.isDigit(command.charAt(0))) {
                input = command;
            } else if (command.equals("C")) {
                input = "";
            }
            newCalculation = false;
        } else {
            if (command.equals("=")) {
                try {
                    input = String.valueOf(eval(input));
                } catch (ArithmeticException ex) {
                    input = "Error";
                }
                newCalculation = true;
            } else if (command.equals("C")) {
                input = "";
                newCalculation = true;
            } else if (command.equals("<-")) {
                if (input.length() > 0) {
                    input = input.substring(0, input.length() - 1);
                }
            } else {
                input += command;
            }
        }

        textField.setText(input);
    }

    private double eval(String expression) {
        try {
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
                }

                boolean isDigit(int c) {
                    return Character.isDigit(c);
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < expression.length()) {
                        throw new RuntimeException("Unexpected: " + (char) ch);
                    }
                    return x;
                }

                double parseExpression() {
                    double x = parseTerm();
                    for (;;) {
                        if (eat('+')) {
                            x += parseTerm();
                        } else if (eat('-')) {
                            x -= parseTerm();
                        } else {
                            return x;
                        }
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (;;) {
                        if (eat('*')) {
                            x *= parseFactor();
                        } else if (eat('/')) {
                            double divisor = parseFactor();
                            if (divisor == 0.0) {
                                throw new ArithmeticException("Division by zero");
                            }
                            x /= divisor;
                        } else {
                            return x;
                        }
                    }
                }

                double parseFactor() {
                    if (eat('+')) {
                        return parseFactor();
                    }
                    if (eat('-')) {
                        return -parseFactor();
                    }
                    double x;
                    int startPos = this.pos;
                    if (eat('(')) {
                        x = parseExpression();
                        eat(')');
                    } else if (isDigit(ch) || ch == '.') {
                        while (isDigit(ch) || ch == '.') {
                            nextChar();
                        }
                        x = Double.parseDouble(expression.substring(startPos, this.pos));
                    } else {
                        throw new RuntimeException("Unexpected: " + (char) ch);
                    }
                    return x;
                }

                boolean eat(int charToEat) {
                    while (ch == ' ') {
                        nextChar();
                    }
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }
            }.parse();
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}
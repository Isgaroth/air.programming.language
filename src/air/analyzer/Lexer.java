package air.analyzer;

import air.util.Token;
import air.util.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    private static final String OPERATOR_CHAR = "+-*/%(){}[]=<>!&|;,:?";
    private static final String WARNING_MESSAGE_1 = "Invalid float number";
    private static final String WARNING_MESSAGE_2 = "Missing close comment tag";
    private static final Map<String, TokenType> OPERATOR_TOKEN;
    private static final String[] CHAR = {
            "+", "-", "*", "/", "%", "(", ")", "{", "}", "[", "]", "=", "<", ">", "!",
            "&", "|", "==", "!=", "<=", ">=", "&&", "||", ";", ",", ":", "?"
    };
    private static final TokenType[] TYPE = {
            TokenType.ADD, TokenType.SUB, TokenType.MUL, TokenType.DIV, TokenType.MOD, TokenType.LEFT_BRACKET,
            TokenType.RIGHT_BRACKET, TokenType.BLOCK_BEGIN, TokenType.BLOCK_END, TokenType.L_ARRAY_BRACKET,
            TokenType.R_ARRAY_BRACKET, TokenType.EQUAL, TokenType.LT, TokenType.GT, TokenType.LNOT,
            TokenType.LAND, TokenType.LOR, TokenType.D_EQUAL, TokenType.LNOT_EQUAL, TokenType.LT_EQUAL,
            TokenType.GT_EQUAL, TokenType.D_LAND, TokenType.D_LOR, TokenType.SEMICOLON, TokenType.COMMA,
            TokenType.COLON, TokenType.QUESTION_MARK
    };

    static {
        OPERATOR_TOKEN = new HashMap<>();
        for (int i = 0; i < CHAR.length; i++) {
            OPERATOR_TOKEN.put(CHAR[i], TYPE[i]);
        }
    }

    private final String input;
    private final List<Token> list;
    private final int length;

    private int currentPosition;

    public Lexer(String input) {
        this.input = input;
        length = input.length();
        list = new ArrayList<>();
    }

    public List<Token> createToken() {
        while (currentPosition < length) {
            char currentSymbol = getCurrentSymbol(0);
            if (Character.isDigit(currentSymbol)) createDecNumberToken();
            else if (Character.isLetter(currentSymbol)) createWordToken();
            else if (currentSymbol == '#') {
                getNextSymbol();
                createHexNumberToken();
            } else if (currentSymbol == '"') createTextToken();
            else if (OPERATOR_CHAR.indexOf(currentSymbol) != -1) createOperatorToken();
            else getNextSymbol();
        }
        return list;
    }

    private void createWordToken() {
        StringBuilder buffer = new StringBuilder();
        char currentSymbol = getCurrentSymbol(0);
        while (true) {
            if (!Character.isLetterOrDigit(currentSymbol) && (currentSymbol != '_') && (currentSymbol != '$')) break;
            buffer.append(currentSymbol);
            currentSymbol = getNextSymbol();
        }
        String stringBuffer = buffer.toString();
        switch (stringBuffer) {
            case "print": addToken(TokenType.PRINT); break;
            case "println": addToken(TokenType.PRINTLN); break;
            case "if": addToken(TokenType.IF); break;
            case "else": addToken(TokenType.ELSE); break;
            case "exploit": addToken(TokenType.EXPLOIT); break;
            case "while": addToken(TokenType.WHILE); break;
            case "for": addToken(TokenType.FOR); break;
            case "repeat": addToken(TokenType.REPEAT); break;
            case "until": addToken(TokenType.UNTIL); break;
            case "break": addToken(TokenType.BREAK); break;
            case "continue": addToken(TokenType.CONTINUE); break;
            case "def": addToken(TokenType.DEF); break;
            case "return": addToken(TokenType.RETURN); break;
            default: addToken(TokenType.WORD, stringBuffer); break;
        }
    }

    private void createTextToken() {
        getNextSymbol();
        StringBuilder buffer = new StringBuilder();
        char currentSymbol = getCurrentSymbol(0);
        while (true) {
            if (currentSymbol == '\\') {
                currentSymbol = getNextSymbol();
                switch (currentSymbol) {
                    case 'n':
                        currentSymbol = getNextSymbol();
                        buffer.append('\n');
                        continue;
                    case 'r':
                        currentSymbol = getNextSymbol();
                        buffer.append('\r');
                        continue;
                    case 't':
                        currentSymbol = getNextSymbol();
                        buffer.append('\t');
                        continue;
                    case '"':
                        currentSymbol = getNextSymbol();
                        buffer.append('"');
                        continue;
                }
                buffer.append('\\');
                continue;
            }
            if (currentSymbol == '"') break;
            buffer.append(currentSymbol);
            currentSymbol = getNextSymbol();
        }
        getNextSymbol();
        addToken(TokenType.TEXT, buffer.toString());
    }

    private void createHexNumberToken() {
        StringBuilder buffer = new StringBuilder();
        char currentSymbol = getCurrentSymbol(0);
        while (Character.isDigit(currentSymbol) || isHexNumber(currentSymbol)) {
            buffer.append(currentSymbol);
            currentSymbol = getNextSymbol();
        }
        addToken(TokenType.HEX_NUMBER, buffer.toString());
    }

    private void createDecNumberToken() {
        StringBuilder buffer = new StringBuilder();
        char currentSymbol = getCurrentSymbol(0);
        while (true) {
            if (currentSymbol == '.') {
                if (buffer.indexOf(".") != -1) throw new RuntimeException(WARNING_MESSAGE_1);
            } else if (!Character.isDigit(currentSymbol)) break;
            buffer.append(currentSymbol);
            currentSymbol = getNextSymbol();
        }
        addToken(TokenType.DEC_NUMBER, buffer.toString());
    }

    private void createOperatorToken() {
        char currentSymbol = getCurrentSymbol(0);
        if (currentSymbol == '/') {
            if (getCurrentSymbol(1) == '/') {
                for (int i = 0; i < 2; i++) getNextSymbol();
                createCommentToken();
                return;
            } else if (getCurrentSymbol(1) == '*') {
                for (int i = 0; i < 2; i++) getNextSymbol();
                createMultilineCommentToken();
                return;
            }
        }
        StringBuilder buffer = new StringBuilder();
        while (true) {
            String text = buffer.toString();
            if (!OPERATOR_TOKEN.containsKey(text + currentSymbol) && !text.isEmpty()) {
                addToken(OPERATOR_TOKEN.get(text));
                return;
            }
            buffer.append(currentSymbol);
            currentSymbol = getNextSymbol();
        }
    }

    private void createCommentToken() {
        char currentSymbol = getCurrentSymbol(0);
        while ("\r\n\0".indexOf(currentSymbol) == -1) {
            currentSymbol = getNextSymbol();
        }
    }

    private void createMultilineCommentToken() {
        char currentSymbol = getCurrentSymbol(0);
        while (true) {
            if (currentSymbol == '\0') throw new RuntimeException(WARNING_MESSAGE_2);
            if (currentSymbol == '*' && getCurrentSymbol(1) == '/') break;
            currentSymbol = getNextSymbol();
        }
        for (int i = 0; i < 2; i++) getNextSymbol();
    }

    private boolean isHexNumber(char currentSymbol) {
        return "abcdef".indexOf(Character.toLowerCase(currentSymbol)) != -1;
    }

    private void addToken(TokenType type) {
        addToken(type, "");
    }

    private void addToken(TokenType type, String text) {
        list.add(new Token(type, text));
    }

    private char getCurrentSymbol(int nextPosition) {
        int position = currentPosition + nextPosition;
        if (position >= length) return '\0';
        return input.charAt(position);
    }

    private char getNextSymbol() {
        currentPosition++;
        return getCurrentSymbol(0);
    }
}
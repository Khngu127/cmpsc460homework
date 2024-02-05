public class Lexer {
    private static final char EOF = 0;

    private Parser yyparser; // parent parser object
    private char[] inputBuffer; // array to store the entire program source
    private int currentPos; // current position in the inputBuffer
    public int lineno; // line number
    public int column; // column

    public Lexer(java.io.Reader reader, Parser yyparser) throws Exception {
        this.yyparser = yyparser;
        lineno = 1;
        column = 1;

        // Read the entire program source into inputBuffer
        StringBuilder inputStringBuilder = new StringBuilder();
        int data;
        while ((data = reader.read()) != -1) {
            inputStringBuilder.append((char) data);
        }
        inputBuffer = inputStringBuilder.toString().toCharArray();
        currentPos = 0;
    }

    public char NextChar() {
        if (currentPos < inputBuffer.length) {
            char c = inputBuffer[currentPos++];
            if (c == '\n') {
                lineno++;
                column = 1;
            } else {
                column++;
            }
            return c;
        } else {
            return EOF;
        }
    }

    public int Fail() {
        return -1;
    }

    public int yylex() throws Exception {
        int state = 0;
        StringBuilder lexemeBuffer = new StringBuilder(); // Added lexemeBuffer

        while (true) {
            char c;
            switch (state) {
                case 0:
                    c = NextChar();
                    if (c == EOF) {
                        state = 9999;
                        continue;
                    } else if (isSymbol(c)) {
                        int symbolToken = checkSymbol(c);
                        if (symbolToken != -1) {
                            lexemeBuffer.append(c);
                            yyparser.yylval = new ParserVal(lexemeBuffer.toString());
                            return symbolToken;
                        } else {
                            return Fail();
                        }
                    } else if (Character.isLetter(c)) {
                        lexemeBuffer.append(c);
                        state = 20;
                        continue;
                    } else if (Character.isWhitespace(c)) {
                        continue;
                    } else {
                        return Fail();
                    }
                case 20: // State for reading identifiers
                    c = NextChar();
                    while (Character.isLetterOrDigit(c) || c == '_') {
                        lexemeBuffer.append(c);
                        // Continue reading identifiers
                        System.out.println("20Char: " + c);
                        c = NextChar();
                        checkKeyword(String.valueOf(c));
                        checkSymbol(c);  // Pass the char directly
                        System.out.println("20NextChar: " + c);
                    }
                    if (Character.isLetterOrDigit(c) || c == '_') {
                        lexemeBuffer.append(c);
                        System.out.println("2OisDigit: " + c);
                        // Continue reading identifiers
                    } else {
                        // Finish reading identifiers
                        String identifier = lexemeBuffer.toString();
                        lexemeBuffer.setLength(0); // Clear the buffer
                        int keywordToken = checkKeyword(identifier);
                        if (keywordToken != -1) {
                            yyparser.yylval = new ParserVal();
                            yyparser.yylval.obj = identifier;
                            return keywordToken;
                        } else {
                            yyparser.yylval = new ParserVal();
                            yyparser.yylval.obj = identifier;
                            return Parser.ID;
                        }
                    }

                case 9999:
                    return 0; // EOF
            }
        }
    }

    private boolean isSymbol(char symbol) {
        // Add more symbols as needed
        return symbol == '(' || symbol == ')' || symbol == ':' ||
                symbol == ';' || symbol == ',' || symbol == '<' ||
                symbol == '>' || symbol == '=' || symbol == '+' ||
                symbol == '-' || symbol == '*' || symbol == '/';
    }

    private int checkSymbol(char symbol) throws Exception {
        // Determine the token for a symbol
        switch (symbol) {
            case '(':
                return Parser.LPAREN;
            case ')':
                return Parser.RPAREN;
            case ':':
                return Parser.COLON;
            case ';':
                return Parser.SEMI;
            case ',':
                return Parser.COMMA;
            case '+':
                return Parser.OP;
            case '-':
                return Parser.OP;
            case '*':
                return Parser.OP;
            case '/':
                return Parser.OP;
            case '<':
                return Parser.RELOP;
            case '>':
                return Parser.RELOP;
            case '=':
                return Parser.RELOP;
            // Add cases for other symbols
            default:
                return -1; // Not a symbol
        }
    }

    private int checkKeyword(String keyword) {
        // Determine the token for a keyword
        switch (keyword) {
            case "int":
                return Parser.INT;
            case "print":
                return Parser.PRINT;
            case "var":
                return Parser.VAR;
            case "func":
                return Parser.FUNC;
            case "if":
                return Parser.IF;
            case "then":
                return Parser.THEN;
            case "else":
                return Parser.ELSE;
            case "while":
                return Parser.WHILE;
            case "void":
                return Parser.VOID;
            case "begin":
                return Parser.BEGIN;
            case "end":
                return Parser.END;
            // Add cases for other keywords
            default:
                return -1; // Not a keyword
        }
    }
}

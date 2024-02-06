public class Lexer {
    private static final char EOF = 0;

    private Parser yyparser; // parent parser object
    private char[] inputBuffer; // array to store the entire program source
    private int currentPos; // current position in the inputBuffer
    public int lineno; // line number
    public int column; // column
    private int lexemeStartColumn; // Variable to store the start position of the lexeme
    private StringBuilder lexemeBuffer = new StringBuilder();

    private static final int BUFFER_SIZE = 10;

    private char[] inputBuffer1;
    private char[] inputBuffer2;
    private char[] currentBuffer;
    private char[] nextBuffer;

    public Lexer(java.io.Reader reader, Parser yyparser) throws Exception {
        this.yyparser = yyparser;
        lineno = 1;
        column = 1;
        lexemeStartColumn = 1;

        StringBuilder inputStringBuilder = new StringBuilder();
        int data;
        while ((data = reader.read()) != -1) {
            inputStringBuilder.append((char) data);
        }
        inputBuffer1 = inputStringBuilder.toString().toCharArray();
        inputBuffer2 = new char[BUFFER_SIZE];
        currentBuffer = inputBuffer1;
        nextBuffer = inputBuffer2;
        currentPos = 0;
    }

    private void switchBuffers() {
        char[] temp = currentBuffer;
        currentBuffer = nextBuffer;
        nextBuffer = temp;
    }

    public int getLexemeStartColumn() {
        return lexemeStartColumn;
    }

    public char NextChar() {
        while (true) {
            if (currentPos < currentBuffer.length) {
                char c = currentBuffer[currentPos++];
                if (c == '\n') {
                    lineno++;
                    column = 1; // Reset column to 1 when a newline is encountered
                } else {
                    if (lexemeBuffer.length() == 0) {
                        lexemeStartColumn = column;
                    }
                    column++;
                }
                return c;
            } else {
                // Switch to the next buffer when the current buffer is exhausted
                switchBuffers();
                currentPos = 0;
            }
        }
    }

    public int Fail() {
        return -1;
    }

    public int yylex() throws Exception {
        int state = 0;
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
                            yyparser.yylval = new ParserVal();
                            yyparser.yylval.obj = String.valueOf(c);
                            return symbolToken;
                        } else {
                            return Fail();
                        }
                    } else if (Character.isLetter(c)) {
                        lexemeBuffer.setLength(0); // Clear the buffer
                        lexemeBuffer.append(c);
                        state = 20;
                        continue;
                    } else if (Character.isWhitespace(c)) {
                        continue;
                    } else {
                        return Fail();
                    }
                case 10: // State for reading ":"
                    c = NextChar();
                    if (c == ':') {
                        // Handle "::" as a single token
                        lexemeBuffer.append("::");
                        return Parser.TYPEOF;
                    } else {
                        currentPos--;
                        lexemeBuffer.append(":");
                        return Parser.COLON;
                    }
                case 20: // State for reading ":="
                    c = NextChar();
                    while (Character.isLetterOrDigit(c) || c == '_') {
                        lexemeBuffer.append(c);
                        c = NextChar();
                        if (!(Character.isLetterOrDigit(c) || c == '_')) {
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
                    }
                    currentPos--;
                    return Parser.COLON;
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
            case ';':
                return Parser.SEMI;
            case ',':
                return Parser.COMMA;
            case '+':
            case '-':
            case '*':
            case '/':
                return Parser.OP;
            case '<':
            case '>':
                char nextChar = NextChar();
                if (nextChar == '=') {
                    lexemeBuffer.append(symbol);
                    lexemeBuffer.append('=');
                    yyparser.yylval = new ParserVal(lexemeBuffer.toString());
                    return Parser.RELOP;
                } else {
                    currentPos--; // Rewind the position if it's not "<=" or ">="
                    return Parser.RELOP;
                }
            case '=':
                nextChar = NextChar();
                if (nextChar == '=') {
                    lexemeBuffer.append(symbol);
                    lexemeBuffer.append('=');
                    yyparser.yylval = new ParserVal(lexemeBuffer.toString());
                    return Parser.RELOP;
                } else {
                    currentPos--; // Rewind the position if it's not "=="
                    return Parser.ASSIGN;
                }
            case ':':
                nextChar = NextChar();
                if (nextChar == ':') {
                    // Handle "::" as a single token
                    lexemeBuffer.append("::");
                    return Parser.TYPEOF;
                } else {
                    // Handle ":" as a separate token
                    currentPos--;
                    lexemeBuffer.append(":");
                    return Parser.COLON;
                }
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
            default:
                return Parser.ID;
        }
    }
}

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
        char c;

        while (true) {
            switch (state) {
                case 0:
                    c = NextChar();
                    if (c == EOF) {
                        state = 9999;
                    } else if (isSymbol(c)) {
                        int symbolToken = checkSymbol(c);
                        if (symbolToken != -1) {
                            System.out.println("symboltokenChar: " + c + ", Line: " + lineno + ", Column: " + column);
                            yyparser.yylval = new ParserVal();
                            yyparser.yylval.obj = String.valueOf(c);
                            return symbolToken;
                        } else {
                            return Fail();
                        }
                    } else if (Character.isLetter(c)) {
                        System.out.println("isletterChar: " + c + ", Line: " + lineno + ", Column: " + column);
                        lexemeBuffer.setLength(0); // Clear the buffer
                        lexemeBuffer.append(c);
                        state = 20;
                    } else if (Character.isWhitespace(c)) {
                        System.out.println("iswhitespaceChar: " + c + ", Line: " + lineno + ", Column: " + column);
                    } else {
                        return Fail();
                    }
                    break;
                case 20: // State for reading ":="
                    c = NextChar();
                    System.out.println("case20Char: " + c + ", Line: " + lineno + ", Column: " + column);
                    while (Character.isLetterOrDigit(c) || c == '_') {
                        System.out.println("c20isLODChar: " + c + ", Line: " + lineno + ", Column: " + column);
                        lexemeBuffer.append(c);
                        c = NextChar();
                        if (c == '(') {
                            System.out.println("LPARENChar: " + c + ", Line: " + lineno + ", Column: " + column);
                            yyparser.yylval = new ParserVal(String.valueOf(c));
                            return Parser.LPAREN;
                        } else if (c == ')') {
                            System.out.println("RPARENChar: " + c + ", Line: " + lineno + ", Column: " + column);
                            yyparser.yylval = new ParserVal(String.valueOf(c));
                            return Parser.RPAREN;
                        }
                        System.out.println("c20isLODafterChar: " + c + ", Line: " + lineno + ", Column: " + column);
                        if (!(Character.isLetterOrDigit(c) || c == '_')) {
                            System.out.println("c20isnotLODChar: " + c + ", Line: " + lineno + ", Column: " + column);
                            String identifier = lexemeBuffer.toString();
                            lexemeBuffer.setLength(0); // Clear the buffer

                            int keywordToken = checkKeyword(identifier);
                            if (keywordToken != -1) {
                                System.out.println("c20KeywordTokenChar: " + c + ", Line: " + lineno + ", Column: " + column);
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = identifier;
                                return keywordToken;
                            } else {
                                System.out.println("C20ELSEChar: " + c + ", Line: " + lineno + ", Column: " + column);
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = identifier;
                                return Parser.ID;
                            }
                        } else if (isSymbol(c)) {
                            checkSymbol(c);
                        }
                    }
                    currentPos--;
                    yyparser.yylval = new ParserVal();
                    yyparser.yylval.obj = lexemeBuffer.toString();
                    return Parser.ID;

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
                System.out.println("ArrowRightChar: " + symbol + ", Line: " + lineno + ", Column: " + column);
                char nextChar = NextChar();
                if (nextChar == '=') {
                    System.out.println("RELOPChar: " + symbol + ", Line: " + lineno + ", Column: " + column);
                    lexemeBuffer.append(symbol);
                    lexemeBuffer.append('=');
                    yyparser.yylval = new ParserVal(lexemeBuffer.toString());
                    return Parser.RELOP;
                } else {
                    currentPos--; // Rewind the position if it's not "<=" or ">="
                    return Parser.RELOP;
                }
            case '=':
                System.out.println("EQChar: " + symbol + ", Line: " + lineno + ", Column: " + column);
                nextChar = NextChar();
                if (nextChar == '=') {
                    System.out.println("EQUALChar: " + symbol + ", Line: " + lineno + ", Column: " + column);
                    lexemeBuffer.append(symbol);
                    lexemeBuffer.append('=');
                    yyparser.yylval = new ParserVal(lexemeBuffer.toString());
                    return Parser.RELOP;
                } else {
                    currentPos--; // Rewind the position if it's not "=="
                    return Parser.ASSIGN;
                }
            case ':':
                currentPos--;
                System.out.println("COLONChar: " + symbol + ", Line: " + lineno + ", Column: " + column);
                nextChar = NextChar();
                if (nextChar == ':') {
                    // Handle "::" as a single token
                    System.out.println("TYPEOFChar: " + symbol + ", Line: " + lineno + ", Column: " + column);
                    lexemeBuffer.append("::");
                    return Parser.TYPEOF;
                } else if (nextChar == '=') {
                    System.out.println("AssignChar: " + symbol + ", Line: " +lineno + ", Column: " + column);
                    lexemeBuffer.append(":=");
                    return Parser.ASSIGN;
                }
                else {
                    // Handle ":" as a separate token
                    System.out.println("finalcolChar: " + symbol + ", Line: " + lineno + ", Column: " + column);
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
            case "id":
                return Parser.ID;
            default:
                return -1;
        }
    }
}

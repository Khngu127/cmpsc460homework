import java.io.Reader;

/*
 * Khoa Nguyen
 * Affin Malik
 *
 *
 *
 */
public class Lexer {
    private static final char EOF = 0;
    private Parser yyparser;
    private int CLocation; // current position in the inputBuffer
    public int lineNum; // line number
    public int column; // column
    private int startColumn;
    private int currColumn;
    private StringBuilder LexerBuffer = new StringBuilder();

    private static final int BUFFER_SIZE = 10;

    private char[] inputBuffer1;
    private char[] inputBuffer2;
    private char[] CurrBuffer;
    private char[] nextBuffer;

    public Lexer(Reader reader, Parser yyparser) throws Exception {
        this.yyparser = yyparser;
        lineNum = 1;
        column = 1;

        StringBuilder inputStringBuilder = new StringBuilder();
        int data;
        while ((data = reader.read()) != -1) {
            inputStringBuilder.append((char) data);
        }
        inputBuffer1 = inputStringBuilder.toString().toCharArray();
        inputBuffer2 = new char[BUFFER_SIZE];
        CurrBuffer = inputBuffer1;
        nextBuffer = inputBuffer2;
        CLocation = 0;
    }
    public int getColumn(){
        return column;
    }
    public char NextChar() {
        while (true) {
            if (CLocation < CurrBuffer.length) {
                char c = CurrBuffer[CLocation++];
                if (c == '\n') {
                    //System.out.println("NEWLINE");
                    lineNum++;
                    column = 1; // Reset column to 1 when a newline is encountered
                    continue;
                } else if (Character.isWhitespace(c)) {
                    // Skip whitespace characters and continue with the next character
                    //System.out.println("Whitespace");
                    column++;
                    continue;
                } else if (c == ':') {
                    //System.out.println("Character: " + c);
                    yyparser.yylval.obj = String.valueOf(c); // Set yylval.obj to a new instance
                    return c;
                } else if (c == '=') {
                    //System.out.println("Chara: " + c);
                    yyparser.yylval.obj = String.valueOf(c); // Set yylval.obj to a new instance
                    return c;
                } else {
                    if (LexerBuffer.length() == 0) {
                        startColumn = column;
                    }
                    column++;
                    yyparser.yylval.obj = String.valueOf(c); // Set yylval.obj to a new instance
                }
                //System.out.println("LASTPRINT");
                return c;
            } else {
                // Switch to the next buffer when the current buffer is exhausted
                switchBuffers();
                if (CLocation < CurrBuffer.length) {
                    char c = CurrBuffer[CLocation++];
                    yyparser.yylval.obj = String.valueOf(c); // Set yylval.obj to a new instance
                    return c;
                } else {
                    if (CurrBuffer == inputBuffer1 && CLocation == inputBuffer1.length) {
                        return Parser.SUCCESS;
                    } else {
                        return Parser.END;
                    }
                }
            }
        }
    }


    private void switchBuffers() {
        char[] temp = CurrBuffer;
        CurrBuffer = nextBuffer;
        nextBuffer = temp;
    }
    public char PeekChar() {
        int peekPos = CLocation; // Use the current position
        char peekChar = 0;

        while (true) {
            if (peekPos < CurrBuffer.length) {
                peekChar = CurrBuffer[peekPos];
                if (peekChar == '\n') {
                    // If a newline is encountered while peeking, move to the next buffer
                    return '\n';
                } else if (Character.isWhitespace(peekChar)) {
                    // Skip whitespace characters and continue with the next character
                    peekPos++;
                    continue;
                }
                return peekChar; // Return the character one position ahead
            } else {
                // Switch to the next buffer when the current buffer is exhausted
                char[] temp = CurrBuffer;
                CurrBuffer = nextBuffer;
                nextBuffer = temp;
                peekPos = 0;
            }
        }
    }

    public int yylex() throws Exception {
        char ch = NextChar();

        while (true) {
            System.out.println("Char: " + ch + ", Lineno: " + lineNum + ", Column: " + column);
            System.out.println("Peekaboo: " + PeekChar() + ", Lineno: " + lineNum + "Column: " + column);
            switch (ch) {
                case EOF:
                    return 0;
                case ';':
                    LexerBuffer.append(ch);
                    return Parser.SEMI;

                case '+':
                    LexerBuffer.append(ch);
                    return Parser.OP;

                case '-':
                    LexerBuffer.append(ch);
                    return Parser.OP;

                case '*':
                    LexerBuffer.append(ch);
                    return Parser.OP;

                case '/':
                    if (PeekChar() == '/') {
                        do {
                            ch = NextChar();
                        } while (ch != '\n' && ch != EOF); // Skip until end of line or file
                        continue; // Continue with the next iteration of the loop to process the next line or token
                    }
                    LexerBuffer.append(ch);
                    return Parser.OP;

                case '<':
                    if (PeekChar() == '=') {
                        NextChar();
                        LexerBuffer.append("<=");
                    } else if (PeekChar() == '>') {
                        NextChar();
                        LexerBuffer.append("<>");
                    } else {
                        LexerBuffer.append("<");
                    }
                    return Parser.RELOP;

                case '>':
                    if (PeekChar() == '=') {
                        NextChar();
                        LexerBuffer.append(">=");
                    } else {
                        LexerBuffer.append(">");
                    }
                    return Parser.RELOP;

                case '(':
                    LexerBuffer.append(ch);
                    return Parser.LPAREN;

                case ')':
                    LexerBuffer.append(ch);
                    return Parser.RPAREN;

                case ',':
                    LexerBuffer.append(ch);
                    return Parser.COMMA;

                case '=':
                    LexerBuffer.append(ch);
                    return Parser.RELOP;

                case ':':
                    if (PeekChar() == '=') {
                        NextChar();
                        LexerBuffer.append(":=");
                        return Parser.ASSIGN;
                    } else if (PeekChar() == ':') {
                        NextChar();
                        LexerBuffer.append("::");
                        return Parser.TYPEOF;
                    } else {
                        return -1;
                    }

                default:
                    if (ch == '_') {
                        return -1;
                    } else if (Character.isLetter(ch)) {
                        LexerBuffer.setLength(0);
                        LexerBuffer.append(ch);
                        while (Character.isLetterOrDigit(PeekChar()) || PeekChar() == '_') {
                            LexerBuffer.append(NextChar());
                        }

                        String identifier = LexerBuffer.toString();
                        switch (identifier) {
                            case "int":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.INT;
                            case "print":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.PRINT;
                            case "var":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.VAR;
                            case "func":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.FUNC;
                            case "if":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.IF;
                            case "then":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.THEN;
                            case "else":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.ELSE;
                            case "while":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.WHILE;
                            case "void":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.VOID;
                            case "begin":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.BEGIN;
                            case "end":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.END;
                            default:
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString(); // Set yylval.obj to a new instance
                                return Parser.ID;
                        }

                    } else if (Character.isDigit(ch)) {
                        // Read a number
                        LexerBuffer.append(ch);
                        startColumn = currColumn;
                        boolean isFloat = false;
                        while (Character.isDigit(PeekChar()) || PeekChar() == '.') {
                            char nextChar = NextChar();
                            LexerBuffer.append(nextChar);
                            currColumn++;
                            if (nextChar == '.') {
                                if (isFloat) {
                                    return -1;
                                }
                                isFloat = true;
                            }
                        }
                        if (isFloat && LexerBuffer.charAt(LexerBuffer.length() - 1) == '.') {
                            return -1;
                        }
                        return Parser.NUM;

                    } else {
                        if (CurrBuffer == inputBuffer1 && CLocation == inputBuffer1.length) {
                            return Parser.SUCCESS;
                        } else {
                            return -1;
                        }
                    }
            }
        }
    }
}

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
    private int CLocation;
    public int lineNum;
    public int column;
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
                System.out.println("BufferChar: " + c + ", Lineno: " + lineNum + " Column: " + column);
                if (c == '\n') {
                    lineNum++;
                    column = 1;
                    continue;
                } else if (Character.isWhitespace(c)) {
                    column++;
                    continue;
                } else if (c == ':') {
                    yyparser.yylval.obj = String.valueOf(c);
                    return c;
                } else if (c == '=') {
                    yyparser.yylval.obj = String.valueOf(c);
                    return c;
                } else {
                    if (LexerBuffer.length() == 0) {
                        startColumn = column;
                    }
                    column++;
                    yyparser.yylval.obj = String.valueOf(c);
                }
                return c;
            } else {
                switchBuffers();
                if (CLocation < CurrBuffer.length) {
                    char c = CurrBuffer[CLocation++];
                    yyparser.yylval.obj = String.valueOf(c);
                    return c;
                } else {
                    if (CurrBuffer == inputBuffer1 && CLocation == inputBuffer1.length) {
                        System.out.println("Success!");
                        return Parser.SUCCESS;
                    } else {
                        System.out.println("Reached the end");
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
        int peekPos = CLocation;
        char peekChar = 0;

        while (true) {
            if (peekPos < CurrBuffer.length) {
                peekChar = CurrBuffer[peekPos];
                if (peekChar == '\n') {
                    return '\n';
                } else if (Character.isWhitespace(peekChar)) {
                    peekPos++;
                    continue;
                }
                return peekChar;
            } else {
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
                        } while (ch != '\n' && ch != EOF);
                        continue;
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
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.INT;
                            case "print":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.PRINT;
                            case "var":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.VAR;
                            case "func":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.FUNC;
                            case "if":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.IF;
                            case "then":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.THEN;
                            case "else":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.ELSE;
                            case "while":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.WHILE;
                            case "void":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.VOID;
                            case "begin":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.BEGIN;
                            case "end":
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.END;
                            default:
                                yyparser.yylval = new ParserVal();
                                yyparser.yylval.obj = LexerBuffer.toString();
                                return Parser.ID;
                        }

                    } else if (Character.isDigit(ch)) {
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

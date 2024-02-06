public class Parser {
    public static final int OP = 10;      // +  -  *  /
    public static final int RELOP = 11;   // <  >  <=  >=  ...
    public static final int LPAREN = 12;  // (
    public static final int RPAREN = 13;  // )
    public static final int SEMI = 14;    // ;
    public static final int COMMA = 15;   // ,
    public static final int INT = 16;     // int
    public static final int NUM = 17;     // number
    public static final int ID = 18;      // identifier
    public static final int PRINT = 19;   // print
    public static final int VOID = 20;    // void
    public static final int FUNC = 21;    // func
    public static final int IF = 22;      // if
    public static final int THEN = 23;    // then
    public static final int ELSE = 24;    // else
    public static final int WHILE = 25;   // while
    public static final int BEGIN = 26;   // begin
    public static final int END = 27;     // end
    public static final int COLON = 28;   // :
    public static final int VAR = 29;
    public static final int TYPEOF = 30;
    public static final int ASSIGN = 31;

    private Compiler compiler;
    private Lexer lexer;       // lexer.yylex() returns token-name
    public ParserVal yylval;   // yylval contains token-attribute

    public Parser(java.io.Reader r, Compiler compiler) throws Exception {
        this.compiler = compiler;
        this.lexer = new Lexer(r, this);
    }

    public int yyparse() throws Exception {
        while (true) {
            int token = lexer.yylex();  // get next token-name
            Object attr = yylval.obj;   // get token-attribute
            String tokenname;

            switch (token) {
                case 0:
                    // EOF is reached
                    System.out.println("Success!");
                    return 0;
                case -1:
                    // lexical error is found
                    System.out.println("Error! There is a lexical error at " + lexer.lineno + ":" + lexer.getLexemeStartColumn() + ".");
                    return -1;
                case Parser.TYPEOF:
                    // Handle TYPEOF token
                    tokenname = getTokenName(token);
                    System.out.println("<" + tokenname + ", token-attr:\"" + attr + "\", " + lexer.lineno + ":" + lexer.getLexemeStartColumn() + ">");
                    break;

                case Parser.LPAREN:
                    // Handle TYPEOF token
                    tokenname = getTokenName(token);
                    System.out.println("<" + tokenname + ", token-attr:\"" + attr + "\", " + lexer.lineno + ":" + lexer.getLexemeStartColumn() + ">");
                    break;
                default:
                    // Print token information
                    tokenname = getTokenName(token);
                    System.out.println("<" + tokenname + ", token-attr:\"" + attr + "\", " + lexer.lineno + ":" + lexer.getLexemeStartColumn() + ">");
            }
        }
    }

    private String getTokenName(int token) {
        // Return the string representation of a token
        switch (token) {
            case OP:
                return "OP";
            case RELOP:
                return "RELOP";
            case LPAREN:
                return "LPAREN";
            case RPAREN:
                return "RPAREN";
            case SEMI:
                return "SEMI";
            case COMMA:
                return "COMMA";
            case INT:
                return "INT";
            case NUM:
                return "NUM";
            case ID:
                return "ID";
            case PRINT:
                return "PRINT";
            case VOID:
                return "VOID";
            case FUNC:
                return "FUNC";
            case IF:
                return "IF";
            case THEN:
                return "THEN";
            case ELSE:
                return "ELSE";
            case WHILE:
                return "WHILE";
            case BEGIN:
                return "BEGIN";
            case END:
                return "END";
            case COLON:
                return "COLON";
            case VAR:
                return "VAR";
            case TYPEOF:
                return "TYPEOF";
            case ASSIGN:
                return "ASSIGN";
            default:
                return "UNKNOWN";
        }
    }
}

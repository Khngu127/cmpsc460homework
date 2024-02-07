/*
 * Khoa Nguyen
 * Affin Malik
 *
 *
 *
 */
public class Parser {
    public static final int OP = 10;
    public static final int RELOP = 11;
    public static final int LPAREN = 12;
    public static final int RPAREN = 13;
    public static final int SEMI = 14;
    public static final int COMMA = 15;
    public static final int INT = 16;
    public static final int NUM = 17;
    public static final int ID = 18;
    public static final int PRINT = 19;
    public static final int VOID = 20;
    public static final int FUNC = 21;
    public static final int IF = 22;
    public static final int THEN = 23;
    public static final int ELSE = 24;
    public static final int WHILE = 25;
    public static final int BEGIN = 26;
    public static final int END = 27;
    public static final int VAR = 29;
    public static final int TYPEOF = 30;
    public static final int ASSIGN = 31;
    public static final int RELOPGE = 32;
    public static final int RELOPLE = 33;
    public static final int RELOPLT = 34;
    public static final int RELOPEE = 35;
    public static final int SUCCESS = 36;

    private Compiler compiler;
    private Lexer lexer;
    public ParserVal yylval = new ParserVal();

    public Parser(java.io.Reader r, Compiler compiler) throws Exception {
        this.compiler = compiler;
        this.lexer = new Lexer(r, this);
    }

    public int yyparse() throws Exception {
        while (true) {
            int token = lexer.yylex();
            Object attr = yylval.obj;
            int tokenStartColumn = lexer.getColumn();

            switch (token) {
                case OP:
                case RELOP:
                case LPAREN:
                case RPAREN:
                case SEMI:
                case COMMA:
                case INT:
                case NUM:
                case PRINT:
                case VAR:
                case FUNC:
                case IF:
                case THEN:
                case ELSE:
                case WHILE:
                case VOID:
                case BEGIN:
                case END:
                    System.out.println("<" + getTokenName(token) + ", token-attr:" + attr + ", " + lexer.lineNum + ":" + (tokenStartColumn) + ">");
                    break;
                case ASSIGN:
                    System.out.println("<" + getTokenName(token) + ", token-attr:" + attr + ", " + lexer.lineNum + ":" + (tokenStartColumn) + ">");
                    break;
                case ID:
                    System.out.println("<" + getTokenName(token) + ", token-attr:" + attr + ", " + lexer.lineNum + ":" + (tokenStartColumn + 1) + ">");
                    break;
                case TYPEOF:
                    System.out.println("<TYPEOF, token-attr:" + attr + ", " + lexer.lineNum + ":" + tokenStartColumn + ">");
                    break;
                case SUCCESS:
                    System.out.println("Success!");
                    return SUCCESS;
                case -1:
                    System.out.println("Error! There is a lexical error at " + lexer.lineNum + ":" + tokenStartColumn + ".");
                    return -1;
            }
        }
    }

        public String getTokenName ( int token){
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
                case VAR:
                    return "VAR";
                case TYPEOF:
                    return "TYPEOF";
                case ASSIGN:
                    return "ASSIGN";
                case RELOPGE:
                    return "RELOP";
                case RELOPLE:
                    return "RELOP";
                case RELOPLT:
                    return "RELOP";
                case RELOPEE:
                    return "RELOP";
                default:
                    return "UNKNOWN";
            }
        }
    }

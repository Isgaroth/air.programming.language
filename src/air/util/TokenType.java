package air.util;

public enum TokenType {
    // 10 #10
    DEC_NUMBER, HEX_NUMBER,

    // word text function
    WORD, TEXT, DEF,

    // if else < > while for repeat until break continue
    IF, ELSE, LT, GT, WHILE, FOR, REPEAT, UNTIL,
    BREAK, CONTINUE, RETURN, EXPLOIT,

    // == <= >= ! !=
    D_EQUAL, LT_EQUAL, GT_EQUAL,
    LNOT, LNOT_EQUAL,

    PRINT, PRINTLN,

    // + - * / = %
    ADD, SUB, MUL, DIV, EQUAL, MOD,

    // ( ) { } [ ]
    LEFT_BRACKET, RIGHT_BRACKET,
    BLOCK_BEGIN, BLOCK_END,
    L_ARRAY_BRACKET, R_ARRAY_BRACKET,

    // ; , : ?
    SEMICOLON, COMMA, COLON, QUESTION_MARK,

    // & && | ||
    LAND, D_LAND, LOR, D_LOR,

    END
}

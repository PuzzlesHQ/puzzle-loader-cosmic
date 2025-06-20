package dev.puzzleshq.pql;

import java.util.regex.Pattern;

public class Lex {

    String content;
    int idx = 0;

    public Lex(String content) {
        this.content = content;
    }

    public char at() {
        return this.content.charAt(this.idx);
    }

    public char s_at() {
        return this.idx >= this.content.length() ? '\0' : this.content.charAt(this.idx);
    }

    public char s_peek() {
        return this.idx + 1 >= this.content.length() ? '\0' : this.content.charAt(this.idx + 1);
    }

    public char peek() {
        return this.content.charAt(this.idx + 1);
    }

    static final Pattern NUM = Pattern.compile("[0-9]");
    static final Pattern ALP = Pattern.compile("[a-zA-Z_$]");

    public Tok nextToken() {
        char chr = this.at();

        while (this.at() == '\n' || this.at() == '\r' || this.at() == ' ') {
            if (this.idx >= this.content.length()) return null;
            chr = this.at();
            this.idx++;
        }

        return switch (chr) {
            case '!' -> new Tok(this.idx++, String.valueOf(chr), TokType.NOT);
            case '(' -> new Tok(this.idx++, String.valueOf(chr), TokType.L_PAREN);
            case ')' -> new Tok(this.idx++, String.valueOf(chr), TokType.R_PAREN);
            case '&' -> new Tok(this.idx++, String.valueOf(chr), TokType.AND);
            case '|' -> new Tok(this.idx++, String.valueOf(chr), TokType.OR);
            case '^' -> new Tok(this.idx++, String.valueOf(chr), TokType.XOR);
            case '=' -> new Tok(this.idx++, String.valueOf(chr), TokType.EQU);
            case '>' -> {
                if (this.s_peek() == '=') {
                    this.idx += 2;
                    yield new Tok(this.idx - 2, ">=", TokType.GTEQ);
                }
                yield new Tok(this.idx++, String.valueOf(chr), TokType.GT);
            }
            case '<' -> {
                if (this.s_peek() == '=') {
                    this.idx += 2;
                    yield new Tok(this.idx - 2, "<=", TokType.LTEQ);
                }
                yield new Tok(this.idx++, String.valueOf(chr), TokType.LT);
            }
            default -> {
                if (ALP.matcher(String.valueOf(this.at())).matches()) {
                    String alp = "";
                    int start = this.idx;

                    while (this.idx < this.content.length() && ALP.matcher(String.valueOf(this.at())).matches()) {
                        alp += String.valueOf(this.at());
                        this.idx++;
                    }

                    yield new Tok(start, alp, switch (alp) {
                        case "if" -> TokType.IF;
                        case "else" -> TokType.ELSE;
                        case "yield" -> TokType.YIELD;
                        default -> TokType.IDT;
                    });
                }
                if (NUM.matcher(String.valueOf(this.at())).matches()) {
                    String num = "";
                    int start = this.idx;

                    while (this.idx < this.content.length() && NUM.matcher(String.valueOf(this.at())).matches()) {
                        num += String.valueOf(this.at());
                        this.idx++;

                        if (this.s_at() == '.') {
                            num += '.';
                            this.idx++;
                            while (this.idx < this.content.length() && NUM.matcher(String.valueOf(this.at())).matches()) {
                                num += String.valueOf(this.at());
                                this.idx++;
                            }
                        }
                    }

                    yield new Tok(start, num, TokType.NUM);
                }

                throw new RuntimeException("Invalid Char \"" + chr + "\"");
            }
        };
    }

    public record Tok(
            int startIdx,
            String content,
            TokType type
    ) {}

    public enum TokType {
        NOT,
        OR,
        XOR,
        AND,

        EQU,
        GT,
        GTEQ,
        LT,
        LTEQ,

        NUM,
        IDT,

        IF,
        ELSE,

        L_PAREN,
        R_PAREN,

        YIELD
    }

}

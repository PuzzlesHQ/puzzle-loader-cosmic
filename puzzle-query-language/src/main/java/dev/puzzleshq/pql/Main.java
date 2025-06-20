package dev.puzzleshq.pql;

public class Main {
    public static void main(String[] args) {
        Lex l = new Lex("23123123 423521.453245");
        System.out.println(l.nextToken());
        System.out.println(l.nextToken());
    }
}
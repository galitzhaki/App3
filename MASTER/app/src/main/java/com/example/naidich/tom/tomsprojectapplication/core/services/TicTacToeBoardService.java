package com.example.naidich.tom.tomsprojectapplication.core.services;

import java.util.Random;
import static com.example.naidich.tom.tomsprojectapplication.core.services.TicTacToeBoardService.TurnType.O_ComputerTurn;
import static com.example.naidich.tom.tomsprojectapplication.core.services.TicTacToeBoardService.TurnType.X_PlayerTurn;

public class TicTacToeBoardService {
        public enum EndGameStatus{
            X_PlayerWins,
            O_ComputerWins,
            Tie
        }

        public enum TurnType{
            X_PlayerTurn,
            O_ComputerTurn
        }

        public static final char X_PLAYER_MARK = 'X';
        public static final char O_COMPUTER_MARK = 'O';
        public static final char T_TIE_MARK = 'T';
        public static final char EMPTY_MARK = ' ';

        private static final Random RANDOM = new Random();
        private char[] elts;
        private TurnType currentPlayer;
        private boolean ended;

        public TicTacToeBoardService() {
            elts = new char[9];
            newGame();
        }

        public boolean isEnded() {
            return ended;
        }

        public char play(int x, int y) {
            if (!ended  &&  elts[3 * y + x] == EMPTY_MARK) {
                elts[3 * y + x] = getCurrentPlayerTurnMark();
                changePlayer();
            }

            return checkEnd();
        }

        public void changePlayer() {
            currentPlayer = currentPlayer == X_PlayerTurn ?
                    O_ComputerTurn : X_PlayerTurn;
        }

        public boolean isValidMove(int x, int y) {
            return getElt(x, y) == EMPTY_MARK;
        }

        public char getElt(int x, int y) {
            return elts[3 * y + x];
        }

        public void newGame() {
            for (int i = 0; i  < elts.length; i++)
                elts[i] = EMPTY_MARK;

            currentPlayer = X_PlayerTurn;
            ended = false;
        }

        public char checkEnd() {
            for (int i = 0; i < 3; i++) {
                if (getElt(i, 0) != ' ' &&
                        getElt(i, 0) == getElt(i, 1)  &&
                        getElt(i, 1) == getElt(i, 2)) {
                    ended = true;
                    return getElt(i, 0);
                }

                if (getElt(0, i) != ' ' &&
                        getElt(0, i) == getElt(1, i)  &&
                        getElt(1, i) == getElt(2, i)) {
                    ended = true;
                    return getElt(0, i);
                }
            }

            if (getElt(0, 0) != ' '  &&
                    getElt(0, 0) == getElt(1, 1)  &&
                    getElt(1, 1) == getElt(2, 2)) {
                ended = true;
                return getElt(0, 0);
            }

            if (getElt(2, 0) != ' '  &&
                    getElt(2, 0) == getElt(1, 1)  &&
                    getElt(1, 1) == getElt(0, 2)) {
                ended = true;
                return getElt(2, 0);
            }

            for (int i = 0; i < 9; i++) {
                if (elts[i] == ' ')
                    return EMPTY_MARK;
            }

            return T_TIE_MARK;
        }

        public char handleComputerTurn() {
            if (!ended) {
                int position;

                do {
                    position = RANDOM.nextInt(9);
                } while (elts[position] != EMPTY_MARK);

                elts[position] = getCurrentPlayerTurnMark();
                changePlayer();
            }

            return checkEnd();
        }

        private char getCurrentPlayerTurnMark(){
            return currentPlayer == X_PlayerTurn ?
                    X_PLAYER_MARK: O_COMPUTER_MARK;
        }
    }

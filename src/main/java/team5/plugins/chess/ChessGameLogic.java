package team5.plugins.chess;

// Internal
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.PieceLogicFactory;
import team5.game.TurnType;
import team5.game.state.MovementDirection;
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.game.state.PieceLogic;

import java.util.*;

import team5.util.Pair;

/*
 * @brief   Game logic for chess
 * @author  Team5
 * @date    2017/03/08
 */
public class ChessGameLogic extends GameLogic {

    private static final int ROWS = 8;
    private static final int COLS = 8;

    private static Map<String, Pair<String, String>> images = new HashMap<String, Pair<String, String>>() {{
        put("Rook",   new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_rlt.png", "http://jamamp.ddns.net/inf122icons/Chess_rdt.png"));
        put("Knight", new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_klt.png", "http://jamamp.ddns.net/inf122icons/Chess_kdt.png"));
        put("Bishop", new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_blt.png", "http://jamamp.ddns.net/inf122icons/Chess_bdt.png"));
        put("Queen",  new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_qlt.png", "http://jamamp.ddns.net/inf122icons/Chess_qdt.png"));
        put("King",   new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_klt.png", "http://jamamp.ddns.net/inf122icons/Chess_kdt.png"));
        put("Pawn",   new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_plt.png", "http://jamamp.ddns.net/inf122icons/Chess_pdt.png"));
    }};

    private String whitePlayer;
    private String blackPlayer;

    public ChessGameLogic(GameSession session) {
        super(session);
        System.out.println("Created ChessGameLogic");

        gameName = "Chess";
    }

    public Pair<Integer, Integer> getBoardSize() {
        return new Pair<Integer, Integer>(ROWS, COLS);
    }

    public boolean needsCheckered() { return true; }
    public boolean needsFlip() { return true; }

    public void initializePieces() {
        List<String> pieceNames = Arrays.asList("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook");

        whitePlayer = session.getUsernames().get(0);
        blackPlayer = session.getUsernames().get(1);

        session.nextTurnType(TurnType.Move);

        // Main pieces
        for (int row : Arrays.asList(0,7)) {
            for (int col = 0; col < COLS; col++) {
                Piece p = new Piece();
                p.setPieceLogic(PieceLogicFactory.createPieceLogic(pieceNames.get(col)));
                p.getPieceLogic().setPieceRef(p);
                String u = row == 0 ? whitePlayer : blackPlayer;
                p.setImage(imageFor(pieceNames.get(col), u));
                p.setDirection(row == 0 ? MovementDirection.Up : MovementDirection.Down);
                p.setUsername(u);
                state().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }

        // Pawns
        for (int row : Arrays.asList(1,6)) {
            for (int col = 0; col < COLS; col++) {
                Piece p = new Piece();
                p.setPieceLogic(PieceLogicFactory.createPieceLogic("Pawn"));
                p.getPieceLogic().setPieceRef(p);
                String u = row == 1 ? whitePlayer : blackPlayer;
                p.setImage(imageFor("Pawn", u));
                p.setDirection(row == 1 ? MovementDirection.Up : MovementDirection.Down);
                p.setUsername(u);
                state().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }


    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {

    }

    public  String gameFinishedWinner() {

        return null;
    }

    private String imageFor(String pieceName, String username) {
        Pair<String, String> pair = images.get(pieceName);
        if (username.equals(whitePlayer)) {
            return pair.getFirst();
        } else {
            return pair.getSecond();
        }
    }

}

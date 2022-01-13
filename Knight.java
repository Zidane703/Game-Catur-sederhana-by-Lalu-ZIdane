import java.util.ArrayList;

/**
 * This class implements methods from the ChessPiece interface.
 * Instantiations of this class are stored in ChessBoard objects.
 * 
 * @author Devan
 */
public class Knight implements ChessPiece{
	
	private String color;
	private char name;
	
	private int startRow;
	private int startColumn;
	
	private ArrayList<MoveCommand> goodMovesList = new ArrayList<MoveCommand>();
	private ArrayList<MoveCommand> noLossMovesList = new ArrayList<MoveCommand>();
	private ArrayList<MoveCommand> takeOpponentPieceMovesList = new ArrayList<MoveCommand>();
	private ArrayList<MoveCommand> opponentKingInCheckMovesList = new ArrayList<MoveCommand>();
	private ArrayList<MoveCommand> opponentInCheckMateMovesList = new ArrayList<MoveCommand>();
	private ArrayList<MoveCommand> canTakePieceAfterwardsList = new ArrayList<MoveCommand>();
	//private ArrayList<MoveCommand> minimumLossMovesList = new ArrayList<MoveCommand>();
	
	private int pieceValue = 3;
	
	/**
	 * This is the constructor for Knight objects.
	 * @param color [of the knight]
	 * @param row
	 * @param col
	 */
	Knight (String color, int row, int col) {
		this.color = color; 
		startRow = row;
		startColumn = col;
		name = color.equals("white") ? 'N' : 'n';
	}
	
	@Override
	public ChessPiece copyOf() {
		Knight copy = new Knight(color, startRow, startColumn);
		return copy;
	}
	
	@Override
	public void generateGoodMoves(ChessBoard board) {

		ArrayList<MoveCommand> maybeGoodMoves = new ArrayList<MoveCommand>();
		ArrayList<MoveCommand> goodMoves = new ArrayList<MoveCommand>();
		
		if (startRow - 2 > -1 && startColumn - 1 > -1) {
			if (!(board.get(startRow-2, startColumn-1).getColor().equals(color)))
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow - 2, startColumn - 1));
		}

		if (startRow - 2 > -1 && startColumn + 1 < 8) {
			if (!(board.get(startRow-2, startColumn+1).getColor().equals(color))) 
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow - 2, startColumn + 1));
		}
		
		if (startRow - 1 > -1 && startRow - 1 < 8 && startColumn + 2 > -1 && startColumn + 2 < 8) {
			if (!(board.get(startRow-1, startColumn+2).getColor().equals(color)))
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow - 1, startColumn + 2));
		}
		
		if (startRow + 1 > -1 && startRow + 1 < 8 && startColumn + 2 > -1 && startColumn + 2 < 8) {
			if (!(board.get(startRow+1, startColumn+2).getColor().equals(color)))
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow + 1, startColumn +2));
		}

		if ((startRow + 2 > -1 && startRow + 2 < 8) && (startColumn + 1 > -1 && startColumn + 1 < 8)) {
			if (!(board.get(startRow+2, startColumn+1).getColor().equals(color))) 
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow + 2, startColumn + 1));
		}
		
		if (startRow + 2 > -1 && startRow + 2 < 8 && startColumn - 1 > -1 && startColumn - 1 < 8) {
			if (!(board.get(startRow+2, startColumn-1).getColor().equals(color)))
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow + 2, startColumn - 1));
		}
		
		if (startRow + 1 > -1 && startRow + 1 < 8 && startColumn - 2 > -1 && startColumn - 2 < 8) {
			if (!(board.get(startRow+1, startColumn-2).getColor().equals(color))) 
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow + 1, startColumn - 2));
		}
		
		if (startRow - 1 > -1 && startRow - 1 < 8 && startColumn - 2 > -1 && startColumn - 2 < 8) {
			if (!(board.get(startRow-1, startColumn-2).getColor().equals(color))) 
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow - 1, startColumn - 2));
		}
		
		int tempKingRow = 0;
		int tempKingColumn = 0;
		
		goodMovesList = new ArrayList<MoveCommand>();
		for (int i = 0; i < maybeGoodMoves.size(); i++) {
			MoveCommand tempMove = maybeGoodMoves.get(i);
			int startRow = tempMove.getStartRow();
			int startColumn = tempMove.getStartColumn();
			int endRow = tempMove.getEndRow();
			int endColumn = tempMove.getEndColumn();
			ChessBoard temp = board.copyOf();
			generateNoLossMoves(temp);
			int before = noLossMovesList.size();
			if (temp.executeMove(tempMove) != false) {
			   generateNoLossMoves(temp);
			   int after = noLossMovesList.size();
			   if (color.equals("white")) {
				   tempKingColumn = board.getPlayerWhite().getKingColumn();
				   tempKingRow = board.getPlayerWhite().getKingRow();
			   }
			   else if (color.equals("black")) {
				   tempKingRow = board.getPlayerBlack().getKingRow();
				   tempKingColumn = board.getPlayerBlack().getKingColumn();
			   }  
			   if (!(temp.isInCheckState(tempKingRow, tempKingColumn, color))) {
				   maybeGoodMoves.get(i).setDiffNumSafeMoves(after - before);
				   int val = (int)(Math.abs(3.5 - endRow) + Math.abs(3.5 - endColumn) / 2.0);
				   maybeGoodMoves.get(i).setAvgDistToCenter(val);
				   goodMoves.add(maybeGoodMoves.get(i));
			   }
			}
		}
		goodMovesList = goodMoves;
	}

	@Override
	public void generateNoLossMoves(ChessBoard board) {
		noLossMovesList = new ArrayList<MoveCommand>();
		ArrayList<MoveCommand> maybeNoLossMoves = new ArrayList<MoveCommand>();
		ArrayList<ArrayList<Integer>> playerPieceLocations = new ArrayList<ArrayList<Integer>>();
		playerPieceLocations = board.getPlayerPieces(color);
		
		if (startRow - 2 > -1 && startColumn - 1 > -1) {
			if (!(board.get(startRow-2, startColumn-1).getColor().equals(color)))
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow - 2, startColumn - 1));
		}

		if (startRow - 2 > -1 && startColumn + 1 < 8) {
			if (!(board.get(startRow-2, startColumn+1).getColor().equals(color))) 
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow - 2, startColumn + 1));
		}
		
		if (startRow - 1 > -1 && startRow - 1 < 8 && startColumn + 2 > -1 && startColumn + 2 < 8) {
			if (!(board.get(startRow-1, startColumn+2).getColor().equals(color)))
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow - 1, startColumn + 2));
		}
		
		if (startRow + 1 > -1 && startRow + 1 < 8 && startColumn + 2 > -1 && startColumn + 2 < 8) {
			if (!(board.get(startRow+1, startColumn+2).getColor().equals(color)))
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow + 1, startColumn +2));
		}

		if ((startRow + 2 > -1 && startRow + 2 < 8) && (startColumn + 1 > -1 && startColumn + 1 < 8)) {
			if (!(board.get(startRow+2, startColumn+1).getColor().equals(color))) 
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow + 2, startColumn + 1));
		}
		
		if (startRow + 2 > -1 && startRow + 2 < 8 && startColumn - 1 > -1 && startColumn - 1 < 8) {
			if (!(board.get(startRow+2, startColumn-1).getColor().equals(color)))
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow + 2, startColumn - 1));
		}
		
		if (startRow + 1 > -1 && startRow + 1 < 8 && startColumn - 2 > -1 && startColumn - 2 < 8) {
			if (!(board.get(startRow+1, startColumn-2).getColor().equals(color))) 
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow + 1, startColumn - 2));
		}
		
		if (startRow - 1 > -1 && startRow - 1 < 8 && startColumn - 2 > -1 && startColumn - 2 < 8) {
			if (!(board.get(startRow-1, startColumn-2).getColor().equals(color))) 
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow - 1, startColumn - 2));
		}
		noLossMovesList = filteredMaybeNoLossMoves(board, playerPieceLocations, maybeNoLossMoves);
	}

	@Override
	public ArrayList<MoveCommand> filteredMaybeNoLossMoves(ChessBoard board, ArrayList<ArrayList<Integer>> playerPieceLocations, ArrayList<MoveCommand> maybeNoLossMoves) {
		ArrayList<MoveCommand> noLossMoves= new ArrayList<MoveCommand>();	
		//minimumLossMovesList = new ArrayList<MoveCommand>();

		int tempPieceRow = 0;
		int tempPieceColumn = 0;
		
		for (int i = 0; i < maybeNoLossMoves.size(); i++) {
			MoveCommand tempMove = maybeNoLossMoves.get(i);
			int startRow = tempMove.getStartRow();
			int startColumn = tempMove.getStartColumn();
			int endRow = tempMove.getEndRow();
			int endColumn = tempMove.getEndColumn();
			boolean addToNoLossMovesFlag = true;
			int numSafePoints = 0;
			for (int j = 0; j < playerPieceLocations.size(); j++) {
				ChessBoard temp = board.copyOf();
				if (temp.get(playerPieceLocations.get(j).get(0), playerPieceLocations.get(j).get(1)).getChar() != 'P' && temp.get(playerPieceLocations.get(j).get(0), playerPieceLocations.get(j).get(1)).getChar() != 'p')
					if (temp.executeMove(tempMove) != false) {
						if (startRow == playerPieceLocations.get(j).get(0) && startColumn == playerPieceLocations.get(j).get(1)) {
							tempPieceRow = endRow;
							tempPieceColumn = endColumn;
						}
						else {
							tempPieceRow = playerPieceLocations.get(j).get(0);
							tempPieceColumn = playerPieceLocations.get(j).get(1);
						}
						if (temp.isInCheckState(tempPieceRow, tempPieceColumn, color)){
							addToNoLossMovesFlag = false;
						}
						else {
							numSafePoints += board.get(tempPieceRow, tempPieceColumn).getPieceValue();
						}
					}
			}
			maybeNoLossMoves.get(i).setNumSafePoints(numSafePoints);
			if (addToNoLossMovesFlag)
				maybeNoLossMoves.get(i).setActuallySafe();
			noLossMoves.add(maybeNoLossMoves.get(i));
			//minimumLossMovesList.add(maybeNoLossMoves.get(i));
		}
		return noLossMoves;
	}

	@Override
	public void generateTakeOpponentPieceMoves(ChessBoard board) {
		takeOpponentPieceMovesList = new ArrayList<MoveCommand>();
		ArrayList<MoveCommand> takeOpponentPieceMoves = new ArrayList<MoveCommand>();
		
		if (startRow - 2 > -1 && startColumn - 1 > -1) {
			if (!(board.get(startRow-2, startColumn-1).getColor().equals(color)))
				if (board.get(startRow-2, startColumn-1).getChar() != '-'){
					MoveCommand temp = new MoveCommand(startRow, startColumn, startRow - 2, startColumn - 1);
					temp.setTakePieceDiff(board.get(startRow-2, startColumn-1).getPieceValue() - pieceValue);
					takeOpponentPieceMoves.add(temp);
				}
		}

		if (startRow - 2 > -1 && startColumn + 1 < 8) {
			if (!(board.get(startRow-2, startColumn+1).getColor().equals(color))) 
				if (board.get(startRow-2, startColumn+1).getChar() != '-') {
					MoveCommand temp = new MoveCommand(startRow, startColumn, startRow - 2, startColumn + 1);
					temp.setTakePieceDiff(board.get(startRow-2, startColumn+1).getPieceValue() - pieceValue);
					takeOpponentPieceMoves.add(temp);
				}
		}
		
		if (startRow - 1 > -1 && startRow - 1 < 8 && startColumn + 2 > -1 && startColumn + 2 < 8) {
			if (!(board.get(startRow-1, startColumn+2).getColor().equals(color)))
				if (board.get(startRow-1, startColumn+2).getChar() != '-'){
					MoveCommand temp = new MoveCommand(startRow, startColumn, startRow - 1, startColumn + 2);
					temp.setTakePieceDiff(board.get(startRow-1, startColumn+2).getPieceValue() - pieceValue);
					takeOpponentPieceMoves.add(temp);
				}
		}
		
		if (startRow + 1 > -1 && startRow + 1 < 8 && startColumn + 2 > -1 && startColumn + 2 < 8) {
			if (!(board.get(startRow+1, startColumn+2).getColor().equals(color)))
				if (board.get(startRow+1, startColumn+2).getChar() != '-'){
					MoveCommand temp = new MoveCommand(startRow, startColumn, startRow + 1, startColumn +2);
					temp.setTakePieceDiff(board.get(startRow+1, startColumn+2).getPieceValue() - pieceValue);
					takeOpponentPieceMoves.add(temp);
				}
		}

		if ((startRow + 2 > -1 && startRow + 2 < 8) && (startColumn + 1 > -1 && startColumn + 1 < 8)) {
			if (!(board.get(startRow+2, startColumn+1).getColor().equals(color))) 
				if (board.get(startRow+2, startColumn+1).getChar() != '-') {
					MoveCommand temp = new MoveCommand(startRow, startColumn, startRow + 2, startColumn + 1);
					temp.setTakePieceDiff(board.get(startRow+2, startColumn+1).getPieceValue() - pieceValue);
					takeOpponentPieceMoves.add(temp);
				}
		}
		
		if (startRow + 2 > -1 && startRow + 2 < 8 && startColumn - 1 > -1 && startColumn - 1 < 8) {
			if (!(board.get(startRow+2, startColumn-1).getColor().equals(color)))
				if (board.get(startRow+2, startColumn-1).getChar() != '-'){
					MoveCommand temp = new MoveCommand(startRow, startColumn, startRow + 2, startColumn - 1);
					temp.setTakePieceDiff(board.get(startRow+2, startColumn-1).getPieceValue() - pieceValue);
					takeOpponentPieceMoves.add(temp);
				}
		}
		
		if (startRow + 1 > -1 && startRow + 1 < 8 && startColumn - 2 > -1 && startColumn - 2 < 8) {
			if (!(board.get(startRow+1, startColumn-2).getColor().equals(color))) 
				if (board.get(startRow+1, startColumn-2).getChar() != '-') {
					MoveCommand temp = new MoveCommand(startRow, startColumn, startRow + 1, startColumn - 2);
					temp.setTakePieceDiff(board.get(startRow+1, startColumn-2).getPieceValue() - pieceValue);
					takeOpponentPieceMoves.add(temp);
				}
		}
		
		if (startRow - 1 > -1 && startRow - 1 < 8 && startColumn - 2 > -1 && startColumn - 2 < 8) {
			if (!(board.get(startRow-1, startColumn-2).getColor().equals(color))) 
				if (board.get(startRow-1, startColumn-2).getChar() != '-') {
					MoveCommand temp = new MoveCommand(startRow, startColumn, startRow - 1, startColumn - 2);
					temp.setTakePieceDiff(board.get(startRow-1, startColumn-2).getPieceValue() - pieceValue);
					takeOpponentPieceMoves.add(temp);
				}
		}
		takeOpponentPieceMovesList = takeOpponentPieceMoves;
		
	}

	@Override
	public void generateOpponentKingInCheckMoves(ChessBoard board) {
		
		generateGoodMoves(board);	
		ArrayList<MoveCommand> maybeOpponentKingInCheckMovesList = goodMovesList;
		opponentKingInCheckMovesList = new ArrayList<MoveCommand>();
		
		int tempPieceRow = -1;
		int tempPieceColumn = -1;
		String tempColor = "";
		
		if (color.equals("white")) {
			tempPieceRow = board.getPlayerBlack().getKingRow();
			tempPieceColumn = board.getPlayerBlack().getKingColumn();
			tempColor = "black";
		}
		else if (color.equals("black")) {
			tempPieceRow = board.getPlayerWhite().getKingRow();
			tempPieceColumn = board.getPlayerWhite().getKingColumn();
			tempColor = "white";
		}
		
		for (int i = 0; i < maybeOpponentKingInCheckMovesList.size(); i++) {
			MoveCommand tempMove = maybeOpponentKingInCheckMovesList.get(i);
			int startRow = tempMove.getStartRow();
			int startColumn = tempMove.getStartColumn();
			int endRow = tempMove.getEndRow();
			int endColumn = tempMove.getEndColumn();
			ChessBoard temp = board.copyOf();
			if (temp.executeMove(tempMove) != false) {
				if (temp.isInCheckState(tempPieceRow, tempPieceColumn, tempColor)) {
					opponentKingInCheckMovesList.add(tempMove);
				}
			}
		}
	}
	
	@Override
	public void generateOpponentInCheckMateMoves(ChessBoard board) {

		generateGoodMoves(board);	
		if (goodMovesList.size() > 0) {
			ArrayList<MoveCommand> maybeOpponentInCheckMateMovesList = goodMovesList;
			opponentInCheckMateMovesList = new ArrayList<MoveCommand>();

			int tempPieceRow = -1;
			int tempPieceColumn = -1;
			String tempColor = "";

			if (color.equals("white")) {
				tempPieceRow = board.getPlayerBlack().getKingRow();
				tempPieceColumn = board.getPlayerBlack().getKingColumn();
				tempColor = "black";
			}
			else if (color.equals("black")) {
				tempPieceRow = board.getPlayerWhite().getKingRow();
				tempPieceColumn = board.getPlayerWhite().getKingColumn();
				tempColor = "white";
			}

			for (int i = 0; i < maybeOpponentInCheckMateMovesList.size(); i++) {
				MoveCommand tempMove = maybeOpponentInCheckMateMovesList.get(i);
				int startRow = tempMove.getStartRow();
				int startColumn = tempMove.getStartColumn();
				int endRow = tempMove.getEndRow();
				int endColumn = tempMove.getEndColumn();
				ChessBoard temp = board.copyOf();
				if (temp.executeMove(tempMove) != false) {
					ArrayList<MoveCommand> opponentGoodMoves = new ArrayList<MoveCommand>();
					if (color.equals("white"))
						opponentGoodMoves = board.getPlayerBlack().getGoodMoves(temp, tempColor);
					else if (color.equals("black"))
						opponentGoodMoves = board.getPlayerWhite().getGoodMoves(temp, tempColor);
					if (opponentGoodMoves.size() == 0)
						opponentInCheckMateMovesList.add(maybeOpponentInCheckMateMovesList.get(i));
				}
			}
		}
	}
	
	@Override
	public void generateCanTakePieceAfterwardsMoves(ChessBoard board) {
		ArrayList<MoveCommand> maybeCanTakePieceAfterwardsList = new ArrayList<MoveCommand>();
		generateGoodMoves(board);
		maybeCanTakePieceAfterwardsList = getGoodMoves();
		for (int i = 0; i < maybeCanTakePieceAfterwardsList.size(); i++){
			ChessBoard boardCopy = board.copyOf();
			boardCopy.executeMove(maybeCanTakePieceAfterwardsList.get(i));
			generateTakeOpponentPieceMoves(boardCopy);
			ArrayList<MoveCommand> metaTakeOpponentPieceMoves = getTakeOpponentPieceMoves();
			for (int j = 0; j < metaTakeOpponentPieceMoves.size(); j++){
				canTakePieceAfterwardsList.add(metaTakeOpponentPieceMoves.get(j));
			}
		}
	}

	@Override
	public ArrayList getGoodMoves() { return goodMovesList; }
	
	@Override
	public int getStartRow() { return startRow;	}
	
	@Override
	public ArrayList<MoveCommand> getOpponentKingInCheckMoves() { return opponentKingInCheckMovesList; }

	@Override
	public ArrayList<MoveCommand> getOpponentInCheckMateMoves() { return opponentInCheckMateMovesList; }
	
	@Override
	public void setStartRow(int startRow) {	this.startRow = startRow; }
	
	@Override
	public int getStartColumn() { return startColumn; }
	
	@Override
	public void setStartColumn(int startColumn) { this.startColumn = startColumn; }
	
	@Override
	public ArrayList<MoveCommand> getTakeOpponentPieceMoves() {	return takeOpponentPieceMovesList; }
	
	@Override
	public char getChar() { return name; }
	
	@Override 
	public int getPieceValue() { return pieceValue; }
	
	@Override
	public String getColor() { return color; }
	
	@Override
	public ArrayList<MoveCommand> getNoLossMoves() { return noLossMovesList; }

	@Override
	public ArrayList<MoveCommand> getCanTakePieceAfterwardsMoves() { return canTakePieceAfterwardsList; }

}

package com.redvex.byteme.gamelogic;

import java.io.Serializable;

public class Helper implements Serializable {
	public enum GameField {
		Bin, Dec, Hex
	}

	public enum GameType {
		BinDec, BinHex, HexDec, BinHexDec
	}

	private static final long serialVersionUID = 1L;
}

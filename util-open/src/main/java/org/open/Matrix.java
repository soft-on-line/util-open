package org.open;

import java.util.ArrayList;

public class Matrix<T>
{
	private ArrayList<ArrayList<T>> table;
	private int columns;
	private int rows;

	//
	
	public Matrix(ArrayList<ArrayList<T>> table) throws Exception {
		this.table = table;
		this.columns = this.table.get(0).size();
		this.rows = this.table.size();
	}

	public void printOut() {
		for (int row = 0; row < rows; row++) {
			StringBuilder sb = new StringBuilder();
			for (int column = 0; column < columns; column++) {
				sb.append(leftAdjusting(this.table.get(row).get(column)
						.toString(), 8));
			}
			System.out.println(sb.toString());
		}
	}

	private String leftAdjusting(String input, int length) {
		try {
			return input.substring(0, length);
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < length - input.length(); i++) {
				sb.append(" ");
			}
			return input + sb.toString();
		}
	}

	public void reduceTheMatrix() throws Exception {
//		GaussianElimination gaussian = new GaussianElimination(this.table);
//		gaussian.reduceTheMatrix();
//		Gauss_JordanElimination gauss_Jordan = new Gauss_JordanElimination(
//				this.table);
//		gauss_Jordan.reduceTheMatrix(gaussian.getDealingColumn(), gaussian
//				.getDealingRow());
	}
}
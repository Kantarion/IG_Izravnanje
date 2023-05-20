package application;

public class Matrix {

	private double matrix[][];

	public Matrix(double matrix[][]) {
		this.matrix = matrix;
	}

	public int getNumberOfRows() {
		return matrix.length;
	}

	public int getNumberOfColumns() {
		return matrix[0].length;
	}

	public double[][] getMatrix() {
		return matrix;
	}

	public Matrix add(Matrix matrix) {
		double m[][] = new double[matrix.getNumberOfRows()][matrix.getNumberOfColumns()];
		for (int i = 0; i < matrix.getNumberOfRows(); i++) {
			for (int j = 0; j < matrix.getNumberOfColumns(); j++) {
				m[i][j] = matrix.getMatrix()[i][j] + this.matrix[i][j];
			}
		}

		return new Matrix(m);
	}
	
	public Matrix subtract(Matrix matrix) {
		double m[][] = new double[matrix.getNumberOfRows()][matrix.getNumberOfColumns()];
		for (int i = 0; i < matrix.getNumberOfRows(); i++) {
			for (int j = 0; j < matrix.getNumberOfColumns(); j++) {
				m[i][j] = matrix.getMatrix()[i][j] - this.matrix[i][j];
			}
		}

		return new Matrix(m);
	}

	public Matrix multiply(Matrix matrix) {
		double m[][] = new double[this.getNumberOfRows()][matrix.getNumberOfColumns()];
		for (int i = 0; i < this.getNumberOfRows(); i++) {
			for (int j = 0; j < matrix.getNumberOfColumns(); j++) {
				for (int k = 0; k < this.getNumberOfColumns(); k++) {
					m[i][j] += this.matrix[i][k] * matrix.getMatrix()[k][j];
				}
			}

		}
		
		return new Matrix(m);

	}
	
	public Matrix transpose() {
		double m[][] = new double[this.getNumberOfColumns()][this.getNumberOfRows()];
		for(int i = 0; i < this.getNumberOfRows(); i++) {
			for(int j = 0; j < this.getNumberOfColumns(); j++) {
				m[j][i] = matrix[i][j];
			}
		}
		
		return new Matrix(m);
	}
	
	public Matrix inverse() {
		double inverse[][] = new double[this.getNumberOfRows()][this.getNumberOfColumns()];
		// minors and cofactors
				for (int i = 0; i < matrix.length; i++)
					for (int j = 0; j < matrix[i].length; j++)
						inverse[i][j] = Math.pow(-1, i + j)
								* determinant(minor(matrix, i, j));

				// adjugate and determinant
				double det = 1.0 / determinant(matrix);
				for (int i = 0; i < inverse.length; i++) {
					for (int j = 0; j <= i; j++) {
						double temp = inverse[i][j];
						inverse[i][j] = inverse[j][i] * det;
						inverse[j][i] = temp * det;
					}
				}

				return new Matrix(inverse);
			}
	
	private double[][] minor(double[][] matrix, int row, int column) {
		double[][] minor = new double[matrix.length - 1][matrix.length - 1];

		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; i != row && j < matrix[i].length; j++)
				if (j != column)
					minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
		return minor;
	}
	
	public double determinant(double[][] matrix) {
		if (matrix.length != matrix[0].length)
			throw new IllegalStateException("invalid dimensions");

		if (matrix.length == 2)
			return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

		double det = 0;
		for (int i = 0; i < matrix[0].length; i++)
			det += Math.pow(-1, i) * matrix[0][i]
					* determinant(minor(matrix, 0, i));
		return det;
	}
	
	
	public Matrix multConst(double c) {
		double m[][] = new double[this.getNumberOfRows()][this.getNumberOfColumns()];
		for(int i = 0; i < this.getNumberOfRows(); i++) {
			for(int j = 0; j < this.getNumberOfColumns(); j++) {
				m[i][j] = c*matrix[i][j];
			}
		}
		
		return new Matrix(m);
	}
	
	public Matrix getDiagonal() {
		double m[][] = new double[this.getNumberOfRows()][1];
		for(int i = 0; i < this.getNumberOfColumns(); i++) {
			m[i][0] = matrix[i][i];
		}
		
		return new Matrix(m);
	}

	@Override
	public String toString() {
		String text = "";
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				text += (matrix[i][j]) + "   ";
			}
			text += "\n";
		}

		return text;
	}

}

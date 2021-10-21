package org.knime.rankaggregation.matrix;

import java.util.NoSuchElementException;

/**
 * 
 * @author Randy Reyna Hernández
 * 
 */

public class ProbabilityMatrix {
	
	private ProbabilityNode[][] matrix;

	public ProbabilityMatrix(Integer cantidadElementos) {
		this.matrix = new ProbabilityNode[cantidadElementos][cantidadElementos];
	}

	// i precede a j
	public void add(int i, int j, int count) {
		if (this.matrix.length < i || this.matrix.length < j) {
			throw new NoSuchElementException("Indice fuera de los limites de la matriz");
		}
		if (this.matrix[i - 1][j - 1] == null) {
			ProbabilityNode elemento1 = new ProbabilityNode();
			elemento1.setDividendo(1 * count);
			elemento1.setDivisor(1 * count);
			this.matrix[i - 1][j - 1] = elemento1;
			ProbabilityNode elemento2 = new ProbabilityNode();
			elemento2.setDividendo(0);
			elemento2.setDivisor(1 * count);
			this.matrix[j - 1][i - 1] = elemento2;
		} else {
			ProbabilityNode elemento1 = this.matrix[i - 1][j - 1];
			ProbabilityNode elemento2 = this.matrix[j - 1][i - 1];
			elemento1.setDividendo(elemento1.getDividendo() + (1 * count));
			elemento1.setDivisor(elemento1.getDivisor() + (1 * count));
			elemento2.setDivisor(elemento2.getDivisor() + (1 * count));
		}
	}

	public ProbabilityNode getElement(int i, int j) {
		if (this.matrix.length < i || this.matrix.length < j) {
			throw new NoSuchElementException("Indice fuera de los limites de la matriz");
		}
		return this.matrix[i][j];
	}
}

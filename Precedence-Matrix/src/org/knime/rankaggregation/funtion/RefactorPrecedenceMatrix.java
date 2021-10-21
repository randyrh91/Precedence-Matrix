package org.knime.rankaggregation.funtion;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.IntValue;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.rankaggregation.matrix.ProbabilityMatrix;
import org.knime.rankaggregation.matrix.ProbabilityNode;

/**
 * 
 * @author Randy Reyna Hernández
 * 
 */

public class RefactorPrecedenceMatrix extends AbstractPrecedenceMatrix {


	private int numberOfVoters;

	public RefactorPrecedenceMatrix(BufferedDataTable tableSpecInput, String diagonal, int numberOfVoters) {
		super(tableSpecInput, diagonal, numberOfVoters);
	}

	@Override
	protected void create() {
		// TODO Auto-generated method stub
		tableSpecOutput = new ProbabilityMatrix(noOfCols);
		for (DataRow r : tableSpecInput) {
			int count = (((IntValue) r.getCell(0)).getIntValue());
			for (int indexStartCell = 1; indexStartCell < r.getNumCells(); indexStartCell++) {
				int selectedElement = (((IntValue) r.getCell(indexStartCell)).getIntValue());
				if (selectedElement != 0) {
					for (int indexCell = indexStartCell + 1; indexCell < r.getNumCells(); indexCell++) {
						int compareElement = (((IntValue) r.getCell(indexCell)).getIntValue());
						if (selectedElement != compareElement && compareElement != 0) {
							if (selectedElement < compareElement) {
								tableSpecOutput.add(indexStartCell, indexCell, count);
							} else {
								tableSpecOutput.add(indexCell, indexStartCell, count);
							}
						}
					}
				}
			}
		}
	}
	@Override
	protected DataCell[] readDataRow() throws Exception {

		DataCell[] rowCells = new DataCell[noOfCols];
		for (int i = 0; i < noOfCols; i++) {
			double resultado = 0;
			boolean esDiagonal = false;
			if (i == rowNo - 1) {
				esDiagonal = true;
				if (diagonal.compareTo("0") == 0) {
					rowCells[i] = new DoubleCell(0.0);
				} else {
					resultado = 0.5;
					rowCells[i] = new DoubleCell(0.5);
				}
			} else {
				ProbabilityNode node = tableSpecOutput.getElement(rowNo - 1, i);
				if (node != null) {
					double empNoOrd = numberOfVoters - node.getDivisor();
					double dividendo = node.getDividendo() + (empNoOrd / 2);
					double divisor = empNoOrd + node.getDivisor();
					resultado = dividendo / divisor;
					rowCells[i] = new DoubleCell(resultado);
				} else {
					rowCells[i] = new DoubleCell(0.5);
				}
			}
			CalcularUtopicoOBOP(resultado);
			CalcularAntiUtopicoOBOP(resultado, esDiagonal);
			CalcularUtopicoRAP(resultado, esDiagonal);
			CalcularAntiUtopicoRAP(resultado, esDiagonal);
		}

		return rowCells;
	}
}

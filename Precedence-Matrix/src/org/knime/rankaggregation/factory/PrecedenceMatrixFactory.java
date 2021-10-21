package org.knime.rankaggregation.factory;

import org.knime.core.node.BufferedDataTable;
import org.knime.rankaggregation.funtion.AbstractPrecedenceMatrix;
import org.knime.rankaggregation.funtion.GeneralPrecedenceMatrix;
import org.knime.rankaggregation.funtion.RefactorPrecedenceMatrix;

/**
 * 
 * @author Randy Reyna Hernández
 * 
 */

public class PrecedenceMatrixFactory extends AbstractPrecedenceMatrixFactory{

	@Override
	public AbstractPrecedenceMatrix createPrecedenceMatrix(String tipo, BufferedDataTable tableSpecInput, String diagonal,
			int numberOfVoters) {
		switch (tipo) {
		case "General Precedence Matrix":
			return new GeneralPrecedenceMatrix(tableSpecInput, diagonal);
		case "Refactor Precedence Matrix":
			return new RefactorPrecedenceMatrix(tableSpecInput, diagonal, numberOfVoters);
		default:
			return new GeneralPrecedenceMatrix(tableSpecInput, diagonal);
		}
	}
}

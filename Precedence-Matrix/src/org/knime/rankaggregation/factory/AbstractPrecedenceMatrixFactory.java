package org.knime.rankaggregation.factory;

import org.knime.core.node.BufferedDataTable;
import org.knime.rankaggregation.funtion.AbstractPrecedenceMatrix;

public abstract class AbstractPrecedenceMatrixFactory {
	public abstract AbstractPrecedenceMatrix createPrecedenceMatrix(String tipo, BufferedDataTable tableSpecInput, String diagonal,
			int numberOfVoters);
}

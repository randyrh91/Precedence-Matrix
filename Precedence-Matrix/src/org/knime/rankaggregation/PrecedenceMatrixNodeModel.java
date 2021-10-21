package org.knime.rankaggregation;

import java.io.File;
import java.io.IOException;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataCellTypeConverter;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.IntValue;
import org.knime.core.data.container.ColumnRearranger;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.rankaggregation.factory.AbstractPrecedenceMatrixFactory;
import org.knime.rankaggregation.factory.PrecedenceMatrixFactory;
import org.knime.rankaggregation.funtion.AbstractPrecedenceMatrix;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * 
 * @author Randy Reyna Hernández
 * 
 *         This is the model implementation of PrecedenceMatrix.
 */
public class PrecedenceMatrixNodeModel extends NodeModel {

	/** Constant for the inport index. */
	public static final int IN_PORT = 0;

	/** initial default count value. */
	static final String CFGKEY_FUNCTION = "funcion";
	static final String CFGKEY_DIAGONAL = "diagonal";

	private AbstractPrecedenceMatrixFactory matrixFactory;
	private AbstractPrecedenceMatrix matrix;

	private final SettingsModelString m_function = new SettingsModelString(PrecedenceMatrixNodeModel.CFGKEY_FUNCTION,
			"Classic Precedence Matrix");

	private final SettingsModelString m_diagonal = new SettingsModelString(PrecedenceMatrixNodeModel.CFGKEY_DIAGONAL,
			"0");

	protected PrecedenceMatrixNodeModel() {
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		String diagonalValue = m_diagonal.getStringValue();
		String functionValue = m_function.getStringValue();
		Integer numberOfVoters = peekFlowVariableInt("reader.voters");
		matrixFactory = new PrecedenceMatrixFactory();
		matrix = matrixFactory.createPrecedenceMatrix(functionValue, inData[0], diagonalValue, numberOfVoters);
		BufferedDataTable out = exec.createBufferedDataTable(matrix, exec);
		pushFlowVariableDouble("precedenceMatrix.utopicOBOP", matrix.getUtopicoOBOP());
		pushFlowVariableDouble("precedenceMatrix.utopicRAP", matrix.getUtopicoRAP());
		pushFlowVariableDouble("precedenceMatrix.anti-utopicOBOP", matrix.getAntiUtopicoOBOP());
		pushFlowVariableDouble("precedenceMatrix.anti-utopicRAP", matrix.getAntiUtopicoRAP());
		return new BufferedDataTable[] { out };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		for (int c = 0; c < inSpecs[IN_PORT].getNumColumns(); c++) {
			DataType colType = inSpecs[IN_PORT].getColumnSpec(c).getType();
			if (!colType.isCompatible(IntValue.class)) {
				throw new InvalidSettingsException("Class " + colType + " not supported.");
			}
		}
		if (!inSpecs[IN_PORT].containsName("Count")) {
			throw new InvalidSettingsException("Column Count not exist.");
		}
		ColumnRearranger c = removeColumnRearranger(inSpecs[IN_PORT]);
		DataTableSpec dts = c.createSpec();
		return new DataTableSpec[] { dts };
	}

	private ColumnRearranger removeColumnRearranger(DataTableSpec dts) {
		ColumnRearranger result = new ColumnRearranger(dts);
		DataCellTypeConverter dctc = new DataCellTypeConverter() {

			@Override
			public DataType getOutputType() {
				return DoubleCell.TYPE;
			}

			@Override
			public DataCell convert(DataCell source) throws Exception {
				return null;
			}
		};

		result.remove(0);
		for (int i = 0; i < result.getColumnCount(); i++) {
			result.ensureColumnIsConverted(dctc, i);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_diagonal.saveSettingsTo(settings);
		m_function.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_diagonal.loadSettingsFrom(settings);
		m_function.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_diagonal.validateSettings(settings);
		m_function.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

}

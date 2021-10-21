package org.knime.rankaggregation.funtion;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.rankaggregation.matrix.ProbabilityMatrix;

/**
 * 
 * @author Randy Reyna Hernández
 * 
 */

public abstract class AbstractPrecedenceMatrix extends RowIterator implements DataTable {
	
	protected BufferedDataTable tableSpecInput;
	protected ProbabilityMatrix tableSpecOutput;
	protected int rowNo;
	protected int noOfCols;
	protected String diagonal;
	protected double utopicoOBOP;
	protected double antiUtopicoOBOP;
	protected double utopicoRAP;
	protected double antiUtopicoRAP;
	protected int numberOfVoters;

	public AbstractPrecedenceMatrix(BufferedDataTable tableSpecInput, String diagonal) {
		super();
		
		if (tableSpecInput == null) {
			throw new NullPointerException("No se pudo obtener el DataTableSpec de entrada");
		}
		
		this.tableSpecInput = tableSpecInput;
		this.diagonal = diagonal;
		this.rowNo = 1;
		this.utopicoOBOP = 0;
		this.antiUtopicoOBOP = 0;
		this.utopicoRAP = 0;
		this.antiUtopicoRAP = 0;
		this.noOfCols = this.tableSpecInput.getDataTableSpec().getNumColumns() - 1;
		this.create();
	}

	public AbstractPrecedenceMatrix(BufferedDataTable tableSpecInput, String diagonal, int numberOfVoters) {
		super();
		
		if (tableSpecInput == null) {
			throw new NullPointerException("No se pudo obtener el DataTableSpec de entrada");
		}
		this.tableSpecInput = tableSpecInput;
		this.diagonal = diagonal;
		this.numberOfVoters = numberOfVoters;
	}

	public int getNumberOfVoters() {
		return numberOfVoters;
	}

	public void setNumberOfVoters(int numberOfVoters) {
		this.numberOfVoters = numberOfVoters;
	}

	public BufferedDataTable getTableSpecInput() {
		return tableSpecInput;
	}


	public void setTableSpecInput(BufferedDataTable tableSpecInput) {
		this.tableSpecInput = tableSpecInput;
	}


	public ProbabilityMatrix getTableSpecOutput() {
		return tableSpecOutput;
	}


	public void setTableSpecOutput(ProbabilityMatrix tableSpecOutput) {
		this.tableSpecOutput = tableSpecOutput;
	}


	public int getRowNo() {
		return rowNo;
	}


	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}


	public int getNoOfCols() {
		return noOfCols;
	}


	public void setNoOfCols(int noOfCols) {
		this.noOfCols = noOfCols;
	}


	public String getDiagonal() {
		return diagonal;
	}


	public void setDiagonal(String diagonal) {
		this.diagonal = diagonal;
	}


	public double getUtopicoOBOP() {
		return utopicoOBOP;
	}


	public void setUtopicoOBOP(double utopicoOBOP) {
		this.utopicoOBOP = utopicoOBOP;
	}


	public double getAntiUtopicoOBOP() {
		return antiUtopicoOBOP;
	}


	public void setAntiUtopicoOBOP(double antiUtopicoOBOP) {
		this.antiUtopicoOBOP = antiUtopicoOBOP;
	}


	public double getUtopicoRAP() {
		return utopicoRAP;
	}


	public void setUtopicoRAP(double utopicoRAP) {
		this.utopicoRAP = utopicoRAP;
	}


	public double getAntiUtopicoRAP() {
		return antiUtopicoRAP;
	}


	public void setAntiUtopicoRAP(double antiUtopicoRAP) {
		this.antiUtopicoRAP = antiUtopicoRAP;
	}


	protected abstract void create();
	
	protected abstract DataCell[] readDataRow() throws Exception;

	
	@Override
	public DataTableSpec getDataTableSpec() {
		String tableName = getMatrixName();
		List<DataColumnSpec> colSpecs = new ArrayList<DataColumnSpec>();
		int createdCols = 1;
		while (createdCols <= noOfCols) {
			DataColumnSpec colSpec = tableSpecInput.getDataTableSpec().getColumnSpec(createdCols);
			colSpecs.add(new DataColumnSpecCreator(colSpec.getName(), DoubleCell.TYPE).createSpec());
			createdCols++;
		}
		return new DataTableSpec(tableName, colSpecs.toArray(new DataColumnSpec[colSpecs.size()]));
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		boolean hasNext = false;
		if (rowNo <= noOfCols) {
			hasNext = true;
		}
		return hasNext;
	}

	@Override
	public DataRow next() {
		if (!hasNext()) {
			throw new NoSuchElementException("El procedimiento de iterar filas fue mas alla de la ultima linea");
		}
		DataCell[] rowCells;
		try {
			rowCells = readDataRow();
			String rowID = tableSpecInput.getDataTableSpec().getColumnSpec(rowNo).getName();
			rowNo++;
			DefaultRow df = new DefaultRow(rowID, rowCells);
			return df;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public RowIterator iterator() {
		return this;
	}

	
	protected void CalcularUtopicoOBOP(double resultado) {
		double utopico = 0;
		if (resultado < 0.25) {
			utopico = resultado;
		} else {
			if (resultado > 0.75) {
				utopico = 1 - resultado;
			} else {
				utopico = Math.abs(0.5 - resultado);
			}
		}
		utopicoOBOP += utopico;
	}

	protected void CalcularAntiUtopicoOBOP(double resultado, boolean esDiagonal) {
		if (!esDiagonal) {
			double antiutopico = 0;
			if (resultado < 0.25) {
				antiutopico = 1 - resultado;
			} else {
				if (resultado > 0.75) {
					antiutopico = resultado;
				} else {
					antiutopico = Math.abs(0.5 - resultado);
				}
			}
			antiUtopicoOBOP += antiutopico;
		}
	}

	protected void CalcularUtopicoRAP(double resultado, boolean esDiagonal) {
		if (!esDiagonal) {
			double utopico = 0;
			if (resultado < 0.5) {
				utopico = resultado;
			} else {
				utopico = 1 - resultado;
			}
			utopicoRAP += utopico;
		}
	}

	protected void CalcularAntiUtopicoRAP(double resultado, boolean esDiagonal) {
		if (!esDiagonal) {
			double antiutopico = 0;
			if (resultado < 0.5) {
				antiutopico = 1 - resultado;
			} else {
				antiutopico = resultado;
			}
			antiUtopicoRAP += antiutopico;
		}
	}

	protected String getMatrixName() {
		return "Precedence Matrix";
	}

}

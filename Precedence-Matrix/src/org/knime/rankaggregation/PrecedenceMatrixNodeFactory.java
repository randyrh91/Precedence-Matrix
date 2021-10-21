package org.knime.rankaggregation;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * 
 * @author Randy Reyna Hernández
 * 
 *         <code>NodeFactory</code> for the "PrecedenceMatrix" Node.
 */
public class PrecedenceMatrixNodeFactory extends NodeFactory<PrecedenceMatrixNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrecedenceMatrixNodeModel createNodeModel() {
		return new PrecedenceMatrixNodeModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<PrecedenceMatrixNodeModel> createNodeView(final int viewIndex,
			final PrecedenceMatrixNodeModel nodeModel) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasDialog() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeDialogPane createNodeDialogPane() {
		return new PrecedenceMatrixNodeDialog();
	}

}

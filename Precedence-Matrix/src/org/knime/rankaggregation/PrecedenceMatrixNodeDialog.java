package org.knime.rankaggregation;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author Randy Reyna Hernández
 * 
 *         <code>NodeDialog</code> for the "PrecedenceMatrix" Node.
 * 
 *         This node dialog derives from {@link DefaultNodeSettingsPane} which
 *         allows creation of a simple dialog with standard components. If you
 *         need a more complex dialog please derive directly from
 *         {@link org.knime.core.node.NodeDialogPane}.
 * 
 */
public class PrecedenceMatrixNodeDialog extends DefaultNodeSettingsPane {

	protected PrecedenceMatrixNodeDialog() {
		createNewGroup("Function:");
		addDialogComponent(new DialogComponentStringSelection(
				new SettingsModelString(PrecedenceMatrixNodeModel.CFGKEY_FUNCTION, "General Precedence Matrix"),
				"Your choice", "General Precedence Matrix", "Refactor Precedence Matrix"));
		createNewGroup("Diagonal:");
		addDialogComponent(
				new DialogComponentButtonGroup(new SettingsModelString(PrecedenceMatrixNodeModel.CFGKEY_DIAGONAL, "0"),
						false, "Fill diagonal with ...", "0", "1/2"));
	}
}

/*******************************************************************************
 * Copyright (c) 2005, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM - Initial API and implementation
 * Sebastian Davids - bug 128529
 * Semion Chichelnitsky (semion@il.ibm.com) - bug 278064
 *******************************************************************************/
package org.eclipse.e4.ui.workbench.swt.internal.copy;

import org.eclipse.osgi.util.NLS;

/**
 * Based on org.eclipse.ui.internal.WorkbenchMessages
 */
public class WorkbenchSWTMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.e4.ui.workbench.swt.internal.copy.messages";//$NON-NLS-1$

	public static String FilteredTree_AccessibleListenerClearButton;
	public static String FilteredTree_ClearToolTip;
	public static String FilteredTree_FilterMessage;
	public static String FilteredTree_AccessibleListenerFiltered;
	public static String ViewLabel_unknown;
	public static String ShowView_noDesc;
	public static String ShowView_selectViewHelp;
	public static String ICategory_general;
	public static String ShowView_shellTitle;

	// IDEApplication messages
	public static String IDEApplication_workspaceMandatoryTitle;
	public static String IDEApplication_workspaceMandatoryMessage;
	public static String IDEApplication_workspaceCannotLockTitle;
	public static String IDEApplication_workspaceCannotLockMessage;
	public static String IDEApplication_workspaceCannotBeSetTitle;
	public static String IDEApplication_workspaceCannotBeSetMessage;
	public static String InternalError;
	public static String IDEApplication_versionTitle;
	public static String IDEApplication_versionMessage;

	static {
		// load message values from bundle file
		reloadMessages();
	}

	public static void reloadMessages() {
		NLS.initializeMessages(BUNDLE_NAME, WorkbenchSWTMessages.class);
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.dialogs;

/**
 * Minimal interface to a page change provider. Used for dialogs which can
 * switch between multiple pages.
 *
 * @since 3.1
 */
public interface IPageChangeProvider {
	/**
	 * Returns the currently selected page in the dialog.
	 *
	 * @return the selected page in the dialog or <code>null</code> if none is
	 *         selected. The type may be domain specific. In
	 *         the JFace provided dialogs this will be an instance of
	 *         <code>IDialogPage</code>.
	 */
	Object getSelectedPage();

	/**
	 * Adds a listener for page changes in this page change provider. Has no
	 * effect if an identical listener is already registered.
	 *
	 * @param listener
	 *            a page changed listener
	 */
	void addPageChangedListener(IPageChangedListener listener);

	/**
	 * Removes the given page change listener from this page change provider.
	 * Has no effect if an identical listener is not registered.
	 *
	 * @param listener
	 *            a page changed listener
	 */
	void removePageChangedListener(IPageChangedListener listener);

}

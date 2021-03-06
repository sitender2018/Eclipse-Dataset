/*******************************************************************************
 * Copyright (c) 2009 Oakland Software Incorporated and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Oakland Software Incorporated - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.tests.navigator.cdt;

import org.eclipse.core.resources.IResource;

// Corresponds to ICModel (workspace root)
public class CRoot extends CElement {

	public CRoot(CNavigatorContentProvider cp, IResource resource) {
		super(cp, resource, null);
	}


}

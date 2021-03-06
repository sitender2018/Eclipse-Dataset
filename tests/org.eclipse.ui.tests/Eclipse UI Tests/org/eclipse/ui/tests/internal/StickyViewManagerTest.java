/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
 ******************************************************************************/

package org.eclipse.ui.tests.internal;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.tests.harness.util.UITestCase;

/**
 * @since 3.6
 */
public class StickyViewManagerTest extends UITestCase {

	/**
	 * The original behaviour of sticky views.
	 */
	private boolean originalPreference;

	public StickyViewManagerTest(String testName) {
		super(testName);
	}

	@Override
	protected void doSetUp() throws Exception {
		// preserve the original behaviour
		originalPreference = PlatformUI.getPreferenceStore().getBoolean(
				IWorkbenchPreferenceConstants.ENABLE_32_STICKY_CLOSE_BEHAVIOR);
		// this test tests the new behaviour
		PlatformUI.getPreferenceStore().setValue(
				IWorkbenchPreferenceConstants.ENABLE_32_STICKY_CLOSE_BEHAVIOR,
				false);
		super.doSetUp();
	}

	@Override
	protected void doTearDown() throws Exception {
		super.doTearDown();
		// revert to the original behaviour to ensure future tests are not
		// indirectly tampered by our settings
		PlatformUI.getPreferenceStore().setValue(
				IWorkbenchPreferenceConstants.ENABLE_32_STICKY_CLOSE_BEHAVIOR,
				originalPreference);
	}

	/**
	 * Tests that multi-instance views that are defined to be sticky have the
	 * sticky behaviour across all their instances (regardless of whether the
	 * instance has a secondary id or not).
	 */
	public void testMultipleStickyViewAcrossPerspectivesBug280656()
			throws Exception {
		IWorkbenchPage page = fWorkbench.getActiveWorkbenchWindow()
				.getActivePage();
		// show a multi-instance view that has no secondary id
		page.showView("org.eclipse.ui.tests.api.MockViewPartMultSticky", null,
				IWorkbenchPage.VIEW_ACTIVATE);
		// show a multi-instance view that has a secondary id
		page.showView("org.eclipse.ui.tests.api.MockViewPartMultSticky",
				"secondary", IWorkbenchPage.VIEW_ACTIVATE);

		IPerspectiveRegistry registry = fWorkbench.getPerspectiveRegistry();
		IPerspectiveDescriptor[] descriptors = registry.getPerspectives();

		for (IPerspectiveDescriptor descriptor : descriptors) {
			// switch to every perspective we know of
			page.setPerspective(descriptor);
			// check that the sticky view is in all of these perspectives
			assertNotNull(page.findViewReference(
					"org.eclipse.ui.tests.api.MockViewPartMultSticky", null));
			assertNotNull(page.findViewReference(
					"org.eclipse.ui.tests.api.MockViewPartMultSticky",
					"secondary"));
		}
	}

	/**
	 * Tests that hiding multi-instance sticky views from one perspective
	 * subsequently causes them to be hidden in all other perspectives.
	 */
	public void testRemovedMultipleStickyViewAcrossPerspectives()
			throws Exception {
		// first we show the special test views
		testMultipleStickyViewAcrossPerspectivesBug280656();

		IWorkbenchPage page = fWorkbench.getActiveWorkbenchWindow()
				.getActivePage();
		IViewReference primaryViewReference = page.findViewReference(
				"org.eclipse.ui.tests.api.MockViewPartMultSticky", null);
		IViewReference secondaryViewReference = page.findViewReference(
				"org.eclipse.ui.tests.api.MockViewPartMultSticky", "secondary");

		// now hide the two views
		page.hideView(primaryViewReference);
		page.hideView(secondaryViewReference);

		IPerspectiveRegistry registry = fWorkbench.getPerspectiveRegistry();
		IPerspectiveDescriptor[] descriptors = registry.getPerspectives();

		for (IPerspectiveDescriptor descriptor : descriptors) {
			// check that every single perspective now has the special views
			// hidden
			page.setPerspective(descriptor);
			assertNull(page.findViewReference(
					"org.eclipse.ui.tests.api.MockViewPartMultSticky", null));
			assertNull(page.findViewReference(
					"org.eclipse.ui.tests.api.MockViewPartMultSticky",
					"secondary"));
		}
	}

	/**
	 * Ensures that views that are not defined to be sticky are not indirectly
	 * affected as a side-effect of the sticky view management code.
	 */
	public void testRemovedMultipleStickyViewAcrossPerspectives2()
			throws Exception {
		IPerspectiveRegistry registry = fWorkbench.getPerspectiveRegistry();
		// retrieve two different perspectives
		IPerspectiveDescriptor resourcePerspectiveDescriptor = registry
				.findPerspectiveWithId("org.eclipse.ui.resourcePerspective");
		IPerspectiveDescriptor viewPerspectiveDescriptor = registry
				.findPerspectiveWithId("org.eclipse.ui.tests.api.ViewPerspective");

		IWorkbenchPage page = fWorkbench.getActiveWorkbenchWindow()
				.getActivePage();
		page.setPerspective(resourcePerspectiveDescriptor);

		// show some multi-instance sticky view instances
		IViewPart primaryViewPart = page.showView(
				"org.eclipse.ui.tests.api.MockViewPartMultSticky", null,
				IWorkbenchPage.VIEW_ACTIVATE);
		page.showView("org.eclipse.ui.tests.api.MockViewPartMultSticky",
				"secondary", IWorkbenchPage.VIEW_ACTIVATE);
		// show the 'Outline' view, a non-sticky view
		IViewPart outlineViewPart = page.showView(IPageLayout.ID_OUTLINE, null,
				IWorkbenchPage.VIEW_ACTIVATE);

		page.setPerspective(viewPerspectiveDescriptor);

		// sticky views should be up
		assertNotNull(page.findViewReference(
				"org.eclipse.ui.tests.api.MockViewPartMultSticky", null));
		assertNotNull(page.findViewReference(
				"org.eclipse.ui.tests.api.MockViewPartMultSticky", "secondary"));

		// open the 'Outline' view in the other perspective also
		page.showView(IPageLayout.ID_OUTLINE, null,
				IWorkbenchPage.VIEW_ACTIVATE);

		// switch back to the perspective we were originally in
		page.setPerspective(resourcePerspectiveDescriptor);

		// no change
		assertNotNull(page.findViewReference(
				"org.eclipse.ui.tests.api.MockViewPartMultSticky", null));
		assertNotNull(page.findViewReference(
				"org.eclipse.ui.tests.api.MockViewPartMultSticky", "secondary"));
		assertNotNull(page.findViewReference(IPageLayout.ID_OUTLINE, null));

		// now hide one of the sticky views and the 'Outline' view
		page.hideView(primaryViewPart);
		page.hideView(outlineViewPart);

		// switch to the other perspective
		page.setPerspective(viewPerspectiveDescriptor);

		// check that the primary sticky view instance can now no longer be
		// found
		assertNull(page.findViewReference(
				"org.eclipse.ui.tests.api.MockViewPartMultSticky", null));
		// the secondary instance was untouched, it should be non-null
		assertNotNull(page.findViewReference(
				"org.eclipse.ui.tests.api.MockViewPartMultSticky", "secondary"));
		// this should be non-null, the 'Outline' view being closed in the other
		// perspective should not affect the current perspective
		assertNotNull(page.findViewReference(IPageLayout.ID_OUTLINE, null));
	}
}

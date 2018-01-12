/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2016 Serge Rieder (serge@jkiss.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (version 2)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.kingbase.db.replication.application.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

public class AboutBoxAction extends Action {

	private IWorkbenchWindow window;

	public AboutBoxAction(IWorkbenchWindow window) {
		this.window = window;
		setText("关于");
		// setImageDescriptor(ImageURL.createImageDescriptor("icons/dbeaver16.png"));
	}

	@Override
	public void run() {
		AboutDialog dialog = new AboutDialog(window.getShell());
		dialog.open();
	}

}
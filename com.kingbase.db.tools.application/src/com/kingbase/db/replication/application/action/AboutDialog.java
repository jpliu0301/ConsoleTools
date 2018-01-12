package com.kingbase.db.replication.application.action;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.ProductProperties;
import org.eclipse.ui.internal.about.AboutItem;
import org.eclipse.ui.internal.about.AboutTextManager;

@SuppressWarnings("restriction")
public class AboutDialog extends Dialog{

	private IProduct product;
	private final static int MAX_IMAGE_WIDTH_FOR_TEXT = 250;
	private ArrayList<Image> images = new ArrayList<Image>();


	public AboutDialog(Shell parentShell) {
		super(parentShell);
		product = Platform.getProduct();
	}
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("about");
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		// brand the about box if there is product info
		Image aboutImage = null;
		AboutItem item = null;
		if (product != null) {
			ImageDescriptor imageDescriptor = ProductProperties
					.getAboutImage(product);
			if (imageDescriptor != null) {
				aboutImage = imageDescriptor.createImage();
			}

			// if the about image is small enough, then show the text
			if (aboutImage == null
					|| aboutImage.getBounds().width <= MAX_IMAGE_WIDTH_FOR_TEXT) {
				String aboutText = ProductProperties.getAboutText(product);
				if (aboutText != null) {
					item = AboutTextManager.scan(aboutText);
				}
			}

			if (aboutImage != null) {
				images.add(aboutImage);
			}
		}

		// create a composite which is the parent of the top area and the bottom
		// button bar, this allows there to be a second child of this composite with
		// a banner background on top but not have on the bottom
		Composite workArea = new Composite(parent, SWT.NONE);
		GridLayout workLayout = new GridLayout();
		workLayout.marginHeight = 0;
		workLayout.marginWidth = 0;
		workLayout.verticalSpacing = 0;
		workLayout.horizontalSpacing = 0;
		workArea.setLayout(workLayout);
		workArea.setLayoutData(new GridData(GridData.FILL_BOTH));

		// page group
		Color background = JFaceColors.getBannerBackground(parent.getDisplay());
		Color foreground = JFaceColors.getBannerForeground(parent.getDisplay());
		Composite top = (Composite) super.createDialogArea(workArea);

		// override any layout inherited from createDialogArea
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		top.setLayout(layout);
		top.setLayoutData(new GridData(GridData.FILL_BOTH));
		top.setBackground(background);
		top.setForeground(foreground);

		// the image & text
		final Composite topContainer = new Composite(top, SWT.NONE);
		topContainer.setBackground(background);
		topContainer.setForeground(foreground);

		layout = new GridLayout();
		layout.numColumns = (aboutImage == null || item == null ? 1 : 2);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		topContainer.setLayout(layout);


		GC gc = new GC(parent);
		// arbitrary default
		int topContainerHeightHint = 100;
		try {
			// default height enough for 6 lines of text
			topContainerHeightHint = Math.max(topContainerHeightHint, gc
					.getFontMetrics().getHeight() * 6);
		}
		finally {
			gc.dispose();
		}

		//image on left side of dialog
		if (aboutImage != null) {
			Label imageLabel = new Label(topContainer, SWT.NONE);
			imageLabel.setBackground(background);
			imageLabel.setForeground(foreground);

			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.verticalAlignment = GridData.BEGINNING;
			data.grabExcessHorizontalSpace = false;
			imageLabel.setLayoutData(data);
			imageLabel.setImage(aboutImage);
			topContainerHeightHint = Math.max(topContainerHeightHint, aboutImage.getBounds().height);
		}

		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		data.heightHint = topContainerHeightHint;
		topContainer.setLayoutData(data);

		if (item != null) {
			final int minWidth = 400; // This value should really be calculated
			// from the computeSize(SWT.DEFAULT,
			// SWT.DEFAULT) of all the
			// children in infoArea excluding the
			// wrapped styled text
			// There is no easy way to do this.
			final ScrolledComposite scroller = new ScrolledComposite(topContainer,
					SWT.V_SCROLL | SWT.H_SCROLL);
			data = new GridData(GridData.FILL_BOTH);
			data.widthHint = minWidth;
			scroller.setLayoutData(data);

			final Composite textComposite = new Composite(scroller, SWT.NONE);
			textComposite.setBackground(background);

			layout = new GridLayout();
			textComposite.setLayout(layout);

			StyledText text = new StyledText(textComposite, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);

			// Don't set caret to 'null' as this causes https://bugs.eclipse.org/293263.
			//	    		text.setCaret(null);

			text.setFont(parent.getFont());
			text.setText(item.getText());
			text.setCursor(null);
			text.setBackground(background);
			text.setForeground(foreground);

			AboutTextManager aboutTextManager = new AboutTextManager(text);
			aboutTextManager.setItem(item);

			//	            createTextMenu();

			GridData gd = new GridData();
			gd.verticalAlignment = GridData.BEGINNING;
			gd.horizontalAlignment = GridData.FILL;
			gd.grabExcessHorizontalSpace = true;
			text.setLayoutData(gd);

			// Adjust the scrollbar increments
			scroller.getHorizontalBar().setIncrement(20);
			scroller.getVerticalBar().setIncrement(20);

			final boolean[] inresize = new boolean[1]; // flag to stop unneccesary
			// recursion
			textComposite.addControlListener(new ControlAdapter() {
				@Override
				public void controlResized(ControlEvent e) {
					if (inresize[0]) {
						return;
					}
					inresize[0] = true;
					// required because of bugzilla report 4579
					textComposite.layout(true);
					// required because you want to change the height that the
					// scrollbar will scroll over when the width changes.
					int width = textComposite.getClientArea().width;
					Point p = textComposite.computeSize(width, SWT.DEFAULT);
					scroller.setMinSize(minWidth, p.y);
					inresize[0] = false;
				}
			});

			scroller.setExpandHorizontal(true);
			scroller.setExpandVertical(true);
			Point p = textComposite.computeSize(minWidth, SWT.DEFAULT);
			textComposite.setSize(p.x, p.y);
			scroller.setMinWidth(minWidth);
			scroller.setMinHeight(p.y);

			scroller.setContent(textComposite);
		}

		// horizontal bar
		Label bar = new Label(workArea, SWT.HORIZONTAL | SWT.SEPARATOR);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		bar.setLayoutData(data);

		// add image buttons for bundle groups that have them
		Composite bottom = (Composite) super.createDialogArea(workArea);
		// override any layout inherited from createDialogArea
		layout = new GridLayout();
		bottom.setLayout(layout);
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.verticalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;

		bottom.setLayoutData(data);


		// spacer
		bar = new Label(bottom, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		bar.setLayoutData(data);

		return workArea;
	}
}

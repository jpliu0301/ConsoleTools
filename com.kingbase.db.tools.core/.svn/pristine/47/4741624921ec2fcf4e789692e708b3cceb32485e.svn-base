package com.kingbase.db.core.util;

import java.text.Collator;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class PickerShellTableSort {
	private Table table;

	private int[] sortFlags;
	private int[] sortColIndexes;

	public PickerShellTableSort(Table table) {
		int[] sortCols = new int[table.getColumnCount()];
		for (int i = 1; i < sortCols.length; i++) {
			sortCols[i] = i;
		}

		this.table = table;
		this.sortColIndexes = sortCols; // 需要排序的索引
		this.sortFlags = new int[table.getColumnCount()];

		init();
	}

	private void init() {
		for (int i = 0; i < sortColIndexes.length; i++) {
			final int sortColIndex = this.sortColIndexes[i];
			TableColumn col = table.getColumn(sortColIndex);

			col.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					columnHandleEvent(event, sortColIndex);
				}
			});
		}
	}

	private void columnHandleEvent(Event event, int sortColIndex) {
		try {
			for (int i = 0; i < sortColIndexes.length; i++) {
				org.eclipse.swt.widgets.TableColumn tabCol = table.getColumn(i);
				tabCol.setImage(null);
			}

			boolean selectColumnType = this.isStringOrNumberType(sortColIndex);

			if (this.sortFlags[sortColIndex] == 1) {
				clearSortFlags();
				this.sortFlags[sortColIndex] = -1;

				if (selectColumnType) {
					this.addNumberSorter(table.getColumn(sortColIndex), true);
				} else {
					this.addStringSorter(table.getColumn(sortColIndex), true);
				}

			} else {
				this.sortFlags[sortColIndex] = 1;

				if (selectColumnType) {
					this.addNumberSorter(table.getColumn(sortColIndex), false);
				} else {
					this.addStringSorter(table.getColumn(sortColIndex), false);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param table
	 * @param column
	 * @param isAscend
	 */
	private void addStringSorter(org.eclipse.swt.widgets.TableColumn column, boolean isAscend) {

		Collator comparator = Collator.getInstance(Locale.getDefault());
		int columnIndex = getColumnIndex(table, column);
		TableItem[] items = table.getItems();
		// 使用冒泡法进行排序
		for (int i = 1; i < items.length; i++) {
			String str2value = items[i].getText(columnIndex);
			if (str2value.equalsIgnoreCase("")) {
				// 当遇到表格中的空项目时，就停止往下检索排序项目
				break;
			}
			for (int j = 0; j < i; j++) {
				String str1value = items[j].getText(columnIndex);
				boolean isLessThan = comparator.compare(str2value, str1value) < 0;
				if ((isAscend && isLessThan) || (!isAscend && !isLessThan)) {
					String[] values = getTableItemText(table, items[i]);
					Object obj = items[i].getData();
					items[i].dispose();
					TableItem item = new TableItem(table, SWT.NONE, j);
					item.setText(values);
					item.setData(obj);
					items = table.getItems();
					break;
				}
			}
		}
		table.setSortColumn(column);
		table.setSortDirection((isAscend ? SWT.UP : SWT.DOWN));
		isAscend = !isAscend;
	}

	private void addNumberSorter(TableColumn column, boolean isAscend) {

		int columnIndex = getColumnIndex(table, column);
		TableItem[] items = table.getItems();
		// 使用冒泡法进行排序
		for (int i = 1; i < items.length; i++) {
			String strvalue2 = items[i].getText(columnIndex);
			if (strvalue2.equalsIgnoreCase("")) {
				// 当遇到表格中的空项目时，就停止往下检索排序项目
				break;
			}

			for (int j = 0; j < i; j++) {
				String strvalue1 = items[j].getText(columnIndex);

				// 将字符串类型数据转化为float类型
				float numbervalue1 = Float.valueOf(strvalue1);
				float numbervalue2 = Float.valueOf(strvalue2);

				boolean isLessThan = false;
				if (numbervalue2 < numbervalue1) {
					isLessThan = true;
				}

				if ((isAscend && isLessThan) || (!isAscend && !isLessThan)) {
					String[] values = getTableItemText(table, items[i]);
					Object obj = items[i].getData();
					items[i].dispose();
					TableItem item = new TableItem(table, SWT.NONE, j);
					item.setText(values);
					item.setData(obj);
					items = table.getItems();
					break;
				}
			}
		}

		table.setSortColumn(column);
		table.setSortDirection((isAscend ? SWT.UP : SWT.DOWN));
		isAscend = !isAscend;
	}

	private int getColumnIndex(Table table, TableColumn column) {
		TableColumn[] columns = table.getColumns();
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].equals(column))
				return i;
		}
		return -1;
	}

	private String[] getTableItemText(Table table, TableItem item) {
		int count = table.getColumnCount();
		String[] strs = new String[count];
		for (int i = 0; i < count; i++) {
			strs[i] = item.getText(i);
		}
		return strs;
	}

	private void clearSortFlags() {
		for (int i = 0; i < table.getColumnCount(); i++) {
			this.sortFlags[i] = 0;
		}
	}

	/**
	 * 判断当前选中列的数据类型
	 * 
	 * @return
	 */
	private boolean isStringOrNumberType(int selectColumnIndex) {
		boolean isok = false;

		TableItem[] items = table.getItems();
		String[] str = new String[items.length];

		for (int i = 0; i < items.length; i++) {
			str[i] = items[i].getText(selectColumnIndex);
		}

		for (int i = 0; i < str.length; i++) {
			String string = str[i];
			isok = string.matches("^(-|\\+)?\\d+\\.?\\d*$");
			// 如果这一列中有一个是字符串，也按字符串排序
			if (!isok) {
				return isok;
			}
		}

		return isok;
	}
}

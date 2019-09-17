package com.android.blupark.adapter;

public class VeiculoRow {
    private String rowText;
    private int icon;

    public VeiculoRow(String rowText, int icon) {
        this.rowText = rowText;
        this.icon = icon;
    }

    public String getRowText() {
        return rowText;
    }

    public void setRowText(String rowText) {
        this.rowText = rowText;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}

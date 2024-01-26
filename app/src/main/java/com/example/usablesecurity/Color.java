package com.example.usablesecurity;

public class Color {
    private String selectedColor;

    public Color() {
        // Default constructor required for Firebase
    }

    public Color(String selectedColor) {
        this.selectedColor = selectedColor;
    }

    public String getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }
}


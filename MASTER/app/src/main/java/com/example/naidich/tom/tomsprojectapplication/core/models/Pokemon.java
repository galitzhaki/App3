package com.example.naidich.tom.tomsprojectapplication.core.models;

public class Pokemon {
    private int imageResource;
    private int nameResource;

    public Pokemon(int imageResource, int nameResource) {
        this.imageResource = imageResource;
        this.nameResource = nameResource;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getNameResource() {
        return nameResource;
    }

    public void setNameResource(int nameResource) {
        this.nameResource = nameResource;
    }
}

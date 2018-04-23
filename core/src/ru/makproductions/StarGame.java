package ru.makproductions;

import com.badlogic.gdx.Game;

import ru.makproductions.screens.menu.MenuScreen;

public class StarGame extends Game {
    @Override
    public void create(){
        setScreen(new MenuScreen(this));
    }
}

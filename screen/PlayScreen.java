/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package screen;

import world.*;
import asciiPanel.AsciiPanel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;
    private Creature player;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;
    private List<int[]> route;
    private boolean cheatMode;

    public PlayScreen() {
        this.screenWidth = 80;
        this.screenHeight = 24;
        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);
    }

    private void createCreatures(CreatureFactory creatureFactory) {
        this.player = creatureFactory.newPlayer(this.messages, 0, 1);

        /*
         * for (int i = 0; i < 8; i++) { creatureFactory.newFungus(); }
         */
    }

    private void createWorld() {
        world = new WorldBuilder(90, 31).makeMaze().build();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;

                if (player.canSee(wx, wy)) {
                    terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
                } else {
                    terminal.write(world.glyph(wx, wy), x, y, Color.DARK_GRAY);
                }
            }
        }
        // Show creatures
        for (Creature creature : world.getCreatures()) {
            if (creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                if (player.canSee(creature.x(), creature.y())) {
                    terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
                }
            }
        }

        // Show Route
        if (cheatMode)
            for (int i = 0; i < route.size(); ++i) {
                int xx = route.get(i)[0];
                int yy = route.get(i)[1];
                int dx = (i == 0) ? xx - 0 : xx - route.get(i - 1)[0];
                int dy = (i == 0) ? yy - 0 : yy - route.get(i - 1)[1];
                char arrow = 0;
                if (dx == 1 && dy == 0)
                    arrow = 26;
                else if (dx == -1 && dy == 0)
                    arrow = 27;
                else if (dx == 0 && dy == 1)
                    arrow = 25;
                else
                    arrow = 24;
                if (xx >= left && xx < left + screenWidth && yy >= top && yy < top + screenHeight)
                    terminal.write(arrow, xx - left, yy - top, Color.PINK);

            }

        // Creatures can choose their next action now
        world.update();

    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = this.screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            terminal.write(messages.get(i), 1, top + i + 1);
        }
        this.oldMessages.addAll(messages);
        messages.clear();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // Terrain and creatures
        displayTiles(terminal, getScrollX(), getScrollY());
        // Player
        terminal.write(player.glyph(), player.x() - getScrollX(), player.y() - getScrollY(), player.color());
        String hint = "Press 'C' to cheat.";
        terminal.write(hint, 1, 27, Color.DARK_GRAY);
        if (cheatMode)
            terminal.write("CHEAT MODE ON!!!!", 23, 27, Color.RED);
        // Messages
        displayMessages(terminal, this.messages);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.moveBy(-1, 0);
                if (cheatMode)
                    route = new MazeRouter(world, player.x(), player.y(), 89, 29).getRoute();
                break;
            case KeyEvent.VK_RIGHT:
                if (player.x() == this.world.width() - 1 && player.y() == this.world.height() - 2)
                    return new WinScreen();
                player.moveBy(1, 0);
                if (cheatMode)
                    route = new MazeRouter(world, player.x(), player.y(), 89, 29).getRoute();
                break;
            case KeyEvent.VK_UP:
                player.moveBy(0, -1);
                if (cheatMode)
                    route = new MazeRouter(world, player.x(), player.y(), 89, 29).getRoute();
                break;
            case KeyEvent.VK_DOWN:
                player.moveBy(0, 1);
                if (cheatMode)
                    route = new MazeRouter(world, player.x(), player.y(), 89, 29).getRoute();
                break;
            case KeyEvent.VK_C:
                this.cheatMode = !this.cheatMode;
                if (cheatMode)
                    route = new MazeRouter(world, player.x(), player.y(), 89, 29).getRoute();
                break;
        }
        return this;
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y() - screenHeight / 2, world.height() - screenHeight));
    }

}

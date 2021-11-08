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
import java.util.*;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;
    private Player player;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;
    private PlayerAI[] myAIs;
    private boolean[] validAIs;
    private int iCurAI;

    public PlayScreen() {
        this.screenWidth = 80;
        this.screenHeight = 40;
        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        createCreatures();
        createBonusus();
    }

    private void createCreatures() {
        player = new Player(this.world, (char) 2, AsciiPanel.brightWhite, 100, 20, 5, 9);
        world.addAtEmptyLocation(player);
        myAIs = new PlayerAI[7];
        myAIs[0] = new OldManAI(player, messages);
        myAIs[1] = new PowerBrotherAI(player, messages);
        myAIs[2] = new ViewBrotherAI(player, messages);
        myAIs[3] = new FireBrotherAI(player, messages);
        myAIs[4] = new WaterBrotherAI(player, messages);
        myAIs[5] = new SteelBrotherAI(player, messages);
        myAIs[6] = new HideBrotherAI(player, messages);
        iCurAI = 0;
        player.setAI(myAIs[iCurAI]);
        validAIs = new boolean[7];
        Arrays.fill(validAIs, true);
    }

    private void createBonusus() {
        for (int i = 0; i < 10; ++i) {
            world.addBonusAtEmptyLocation(new world.Bonus(world, 0));
            world.addBonusAtEmptyLocation(new world.Bonus(world, 1));
            world.addBonusAtEmptyLocation(new world.Bonus(world, 2));
        }
    }

    private void createWorld() {
        world = new WorldBuilder(90, 60).makeCaves().build();
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

        // Show bonuses
        for (Bonus bonus : world.getBonuses()) {
            if (bonus.x() >= left && bonus.x() < left + screenWidth && bonus.y() >= top
                    && bonus.y() < top + screenHeight) {
                if (player.canSee(bonus.x(), bonus.y())) {
                    terminal.write(bonus.glyph(), bonus.x() - left, bonus.y() - top, Color.BLUE);
                }
            }
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
        // Stats
        String stats = String.format("%3d/%3d hp   %2d digs", player.hp(), player.maxHP(), player.digCount);
        terminal.write(stats, 1, 42);
        // Messages
        displayMessages(terminal, this.messages);

        // Show characters
        terminal.write("OldMan", 2, 44, iCurAI == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY);
        terminal.write("PowerBro", 10, 44, iCurAI == 1 ? Color.ORANGE : Color.DARK_GRAY);
        terminal.write("ViewBro", 20, 44, iCurAI == 2 ? Color.YELLOW : Color.DARK_GRAY);
        terminal.write("FireBro", 29, 44, iCurAI == 3 ? Color.RED : Color.DARK_GRAY);
        terminal.write("WaterBro", 38, 44, iCurAI == 4 ? Color.BLUE : Color.DARK_GRAY);
        terminal.write("SteelBro", 48, 44, iCurAI == 5 ? Color.GREEN : Color.DARK_GRAY);
        terminal.write("HideBro", 58, 44, iCurAI == 6 ? Color.CYAN : Color.DARK_GRAY);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            player.moveBy(-1, 0);
            break;
        case KeyEvent.VK_RIGHT:
            player.moveBy(1, 0);
            break;
        case KeyEvent.VK_UP:
            player.moveBy(0, -1);
            break;
        case KeyEvent.VK_DOWN:
            player.moveBy(0, 1);
            break;
        case KeyEvent.VK_V:
            iCurAI = (iCurAI + 1) % 7;
            while (!validAIs[iCurAI])
                iCurAI = (iCurAI + 1) % 7;
            player.setAI(myAIs[iCurAI]);
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

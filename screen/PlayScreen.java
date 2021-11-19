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
    private int[] curCoolTime;
    private final int[] maxCoolTime;
    private boolean[] validAIs;
    private int iCurAI;
    private int preDirect;
    private Date preMessageClearTime;

    public PlayScreen() {
        this.screenWidth = 80;
        this.screenHeight = 40;
        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        maxCoolTime = new int[] { 0, 10, 10, 10, 10, 10, 10 };
        curCoolTime = new int[7];

        createCreatures();
        createBonusus();

        new Thread(() -> {
            while (true) {
                for (int i = 1; i < 7; ++i)
                    if (curCoolTime[i] < maxCoolTime[i])
                        ++curCoolTime[i];
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }).start();
    }

    private void createCreatures() {
        player = new Player(this.world, (char) 2, AsciiPanel.brightWhite, 100, 20, 5, 9);
        world.addAtEmptyLocation(player);
        myAIs = new PlayerAI[7];
        myAIs[0] = new OldManAI(player, world, messages);
        myAIs[1] = new PowerBrotherAI(player, world, messages);
        myAIs[2] = new ViewBrotherAI(player, world, messages);
        myAIs[3] = new FireBrotherAI(player, world, messages);
        myAIs[4] = new WaterBrotherAI(player, world, messages);
        myAIs[5] = new SteelBrotherAI(player, world, messages);
        myAIs[6] = new HideBrotherAI(player, world, messages);
        iCurAI = 0;
        player.setAI(myAIs[iCurAI]);
        validAIs = new boolean[7];
        Arrays.fill(validAIs, true);

        for (int i = 0; i < 20; ++i) {
            Creature enemy = new Creature(this.world, (char) 15, Color.PINK, 100, 20, 5, 9);
            new Thread(new EnemyAI(enemy, world, player)).start();
            world.addAtEmptyLocation(enemy);
        }
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

    private void displayCoolTime(AsciiPanel terminal) {
        // Show characters
        for (int i = 1; i < 7; ++i) {
            int leftStart = 10 + 11 * (i - 1);
            for (int j = 0; j < maxCoolTime[i]; ++j)
                if (j < curCoolTime[i])
                    terminal.write("*", leftStart + j, 45, Color.GREEN);
                else
                    terminal.write("*", leftStart + j, 45, Color.DARK_GRAY);
        }
    }

    private void displaySkill(AsciiPanel terminal) {
        switch (iCurAI) {
        case 1:
            switch (preDirect) {
            case KeyEvent.VK_LEFT:
                terminal.write("*", player.x() - getScrollX() - 1, player.y() - getScrollY(), Color.RED);
                break;
            case KeyEvent.VK_RIGHT:
                terminal.write("*", player.x() - getScrollX() + 1, player.y() - getScrollY(), Color.RED);
                break;
            case KeyEvent.VK_UP:
                terminal.write("*", player.x() - getScrollX(), player.y() - getScrollY() - 1, Color.RED);
                break;
            case KeyEvent.VK_DOWN:
                terminal.write("*", player.x() - getScrollX(), player.y() - getScrollY() + 1, Color.RED);
                break;
            }
            break;
        case 2:
            break;
        case 3:
            switch (preDirect) {
            case KeyEvent.VK_LEFT:
                for (int i = 1; i < 6 && player.x() - getScrollX() - i >= 0
                        && world.tile(player.x() - i, player.y()) != Tile.WALL; ++i)
                    terminal.write("*", player.x() - getScrollX() - i, player.y() - getScrollY(), Color.RED);
                break;
            case KeyEvent.VK_RIGHT:
                for (int i = 1; i < 6 && player.x() - getScrollX() + i < screenWidth
                        && world.tile(player.x() + i, player.y()) != Tile.WALL; ++i)
                    terminal.write("*", player.x() - getScrollX() + i, player.y() - getScrollY(), Color.RED);
                break;
            case KeyEvent.VK_UP:
                for (int i = 1; i < 6 && player.y() - getScrollY() - i >= 0
                        && world.tile(player.x(), player.y() - i) != Tile.WALL; ++i)
                    terminal.write("*", player.x() - getScrollX(), player.y() - getScrollY() - i, Color.RED);
                break;
            case KeyEvent.VK_DOWN:
                for (int i = 1; i < 6 && player.y() - getScrollY() + i < screenHeight
                        && world.tile(player.x(), player.y() + i) != Tile.WALL; ++i)
                    terminal.write("*", player.x() - getScrollX(), player.y() - getScrollY() + i, Color.RED);
                break;
            }
            break;
        case 4:
            int r = 5;
            int xx = player.x() - getScrollX();
            int yy = player.y() - getScrollY();
            int wxx = player.x();
            int wyy = player.y();
            for (int i = 0; i < r; ++i) {
                int rr = r - i;
                for (int j = 0; j < rr; ++j) {
                    if (i == 0 && j == 0)
                        continue;
                    try {
                        if (xx + j < screenWidth && xx + j >= 0 && yy + i < screenHeight && yy + i >= 0
                                && world.tile(wxx + j, wyy + i) != Tile.WALL)
                            terminal.write('*', xx + j, yy + i, Color.BLUE);
                    } catch (Exception e) {
                    }
                    try {
                        if (xx + j < screenWidth && xx + j >= 0 && yy - i < screenHeight && yy - i >= 0
                                && world.tile(wxx + j, wyy - i) != Tile.WALL)
                            terminal.write('*', xx + j, yy - i, Color.BLUE);
                    } catch (Exception e) {
                    }
                    try {
                        if (xx - j < screenWidth && xx - j >= 0 && yy + i < screenHeight && yy + i >= 0
                                && world.tile(wxx - j, wyy + i) != Tile.WALL)
                            terminal.write('*', xx - j, yy + i, Color.BLUE);
                    } catch (Exception e) {
                    }
                    try {
                        if (xx - j < screenWidth && xx - j >= 0 && yy - i < screenHeight && yy - i >= 0
                                && world.tile(wxx - j, wyy - i) != Tile.WALL)
                            terminal.write('*', xx - j, yy - i, Color.BLUE);
                    } catch (Exception e) {
                    }

                }
            }
            break;
        case 5:
            break;
        case 6:
            break;
        default:
            break;
        }
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        if (preMessageClearTime == null)
            preMessageClearTime = new Date();
        int top = this.screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            terminal.write(messages.get(i), 1, top - i + 1);
        }
        this.oldMessages.addAll(messages);
        if (new Date().getTime() - preMessageClearTime.getTime() > 2000) {
            messages.clear();
            preMessageClearTime = null;
        }
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
        // Cool Times
        displayCoolTime(terminal);

        if (player.onSkill())
            displaySkill(terminal);

        // Show characters
        terminal.write("OldMan", 2, 44, iCurAI == 0 ? Player.id2Color(iCurAI) : Color.DARK_GRAY);
        terminal.write("PowerBro", 10, 44, iCurAI == 1 ? Player.id2Color(iCurAI) : Color.DARK_GRAY);
        terminal.write("ViewBro", 21, 44, iCurAI == 2 ? Player.id2Color(iCurAI) : Color.DARK_GRAY);
        terminal.write("FireBro", 32, 44, iCurAI == 3 ? Player.id2Color(iCurAI) : Color.DARK_GRAY);
        terminal.write("WaterBro", 43, 44, iCurAI == 4 ? Player.id2Color(iCurAI) : Color.DARK_GRAY);
        terminal.write("SteelBro", 54, 44, iCurAI == 5 ? Player.id2Color(iCurAI) : Color.DARK_GRAY);
        terminal.write("HideBro", 65, 44, iCurAI == 6 ? Player.id2Color(iCurAI) : Color.DARK_GRAY);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (player.onSkill())
            return this;
        switch (key.getKeyCode()) {
        case KeyEvent.VK_A:
            player.moveBy(-1, 0);
            preDirect = KeyEvent.VK_LEFT;
            break;
        case KeyEvent.VK_D:
            player.moveBy(1, 0);
            preDirect = KeyEvent.VK_RIGHT;
            break;
        case KeyEvent.VK_W:
            player.moveBy(0, -1);
            preDirect = KeyEvent.VK_UP;
            break;
        case KeyEvent.VK_S:
            player.moveBy(0, 1);
            preDirect = KeyEvent.VK_DOWN;
            break;
        case KeyEvent.VK_Q:
            iCurAI = iCurAI == 0 ? 6 : iCurAI - 1;
            while (!validAIs[iCurAI])
                iCurAI = iCurAI == 0 ? 6 : iCurAI - 1;
            player.setAI(myAIs[iCurAI]);
            player.setColor(Player.id2Color(iCurAI));
            break;
        case KeyEvent.VK_E:
            iCurAI = (iCurAI + 1) % 7;
            while (!validAIs[iCurAI])
                iCurAI = (iCurAI + 1) % 7;
            player.setAI(myAIs[iCurAI]);
            player.setColor(Player.id2Color(iCurAI));
            break;
        case KeyEvent.VK_J:
            if (curCoolTime[iCurAI] > 3) {
                player.skill();
                curCoolTime[iCurAI] -= 3;
            }

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

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

import asciiPanel.AsciiPanel;

/**
 *
 * @author Aeranythe Echosong
 */
public class WinScreen extends RestartScreen {
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("                                                              dP ", 8, 3);
        terminal.write("                                                              88 ", 8, 4);
        terminal.write("dP    dP .d8888b. dP    dP    dP  dP  dP .d8888b. 88d888b.    88 ", 8, 5);
        terminal.write("88    88 88'  `88 88    88    88  88  88 88'  `88 88'  `88    dP ", 8, 6);
        terminal.write("88.  .88 88.  .88 88.  .88    88.88b.88' 88.  .88 88    88       ", 8, 7);
        terminal.write("`8888P88 `88888P' `88888P'    8888P Y8P  `88888P' dP    dP    oo ", 8, 8);
        terminal.write("     .88                                                         ", 8, 9);
        terminal.write(" d8888P                                                          ", 8, 10);
        terminal.write("Press Enter to restart...", 0, 15);
    }
}

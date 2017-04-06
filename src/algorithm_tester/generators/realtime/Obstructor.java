/*
 * Copyright (C) 2017 stefko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package algorithm_tester.generators.realtime;


/**
 * This object is a constant obstruction of the field of view (for example gold
 * bead, foreign object in field of view, dirt, etc.)
 * @author Marcel Stefko
 */
public interface Obstructor {

    /**
     * Draws the obstruction onto the given float array representing an image.
     * @param pixels image to be drawn on
     */
    public void applyTo(float[][] pixels);
}

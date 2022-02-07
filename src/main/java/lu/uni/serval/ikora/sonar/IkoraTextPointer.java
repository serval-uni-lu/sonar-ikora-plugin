package lu.uni.serval.ikora.sonar;

/*-
 * #%L
 * sonar-ikora-plugin
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.sonar.api.batch.fs.TextPointer;

public class IkoraTextPointer implements TextPointer {
    private final int line;
    private final int lineOffset;

    public IkoraTextPointer(int line, int lineOffset){
        this.line = line;
        this.lineOffset = lineOffset;
    }

    @Override
    public int line() {
        return this.line;
    }

    @Override
    public int lineOffset() {
        return this.lineOffset;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.line;
        hash = 31 * hash + this.lineOffset;
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(other == this) return true;
        if(other.getClass() != this.getClass()) return false;

        final IkoraTextPointer that = (IkoraTextPointer) other;

        return this.line == that.line && this.lineOffset == that.lineOffset;
    }

    @Override
    public int compareTo(TextPointer other) {
        if(this.line == other.line()){
            return Integer.compare(this.line, other.line());
        }

        return Integer.compare(this.lineOffset, other.lineOffset());
    }

    @Override
    public String toString() {
        return this.line + ":" + this.lineOffset;
    }
}

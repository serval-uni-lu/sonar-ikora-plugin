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
import org.sonar.api.batch.fs.TextRange;

public class IkoraTextRange implements TextRange {
    private final TextPointer start;
    private final TextPointer end;

    public IkoraTextRange(TextPointer start, TextPointer end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public TextPointer start() {
        return this.start;
    }

    @Override
    public TextPointer end() {
        return this.end;
    }

    @Override
    public boolean overlap(TextRange other) {
        if(this.start() == other.start() || this.end() == other.end()){
            return true;
        }

        if(this.start().compareTo(other.start()) < 0){
            return this.end().compareTo(other.start()) <= 0;
        }

        return other.end().compareTo(this.start()) <= 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.start == null ? 0 : this.start.hashCode());
        hash = 31 * hash + (this.end == null ? 0 : this.end.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(other == this) return true;
        if(other.getClass() != this.getClass()) return false;

        final IkoraTextRange that = (IkoraTextRange)other;

        return this.start == that.start && this.end == that.end;
    }

    @Override
    public String toString() {
        return "[" + this.start + "," + this.end + "]";
    }
}

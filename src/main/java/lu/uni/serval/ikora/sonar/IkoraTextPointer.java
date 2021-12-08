package lu.uni.serval.ikora.sonar;

/*-
 * #%L
 * sonar-ikora-plugin
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

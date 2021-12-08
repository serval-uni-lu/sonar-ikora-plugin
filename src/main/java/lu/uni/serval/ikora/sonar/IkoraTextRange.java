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

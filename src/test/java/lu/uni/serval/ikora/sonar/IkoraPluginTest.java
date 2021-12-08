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

import org.junit.jupiter.api.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.junit.jupiter.api.Assertions.*;

class IkoraPluginTest {
    @Test
    void sonarqube_9_1_extensions() {
        Plugin.Context context = new Plugin.Context(SonarRuntimeImpl.forSonarQube(Version.create(9, 1), SonarQubeSide.SERVER, SonarEdition.COMMUNITY));

        new IkoraPlugin().define(context);
        assertEquals(10, context.getExtensions().size());
    }
}

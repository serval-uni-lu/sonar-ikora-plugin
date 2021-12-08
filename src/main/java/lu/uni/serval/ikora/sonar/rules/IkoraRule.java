package lu.uni.serval.ikora.sonar.rules;

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

import lu.uni.serval.ikora.sonar.IkoraSourceCode;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;

public abstract class IkoraRule {
    protected RuleKey ruleKey = null;
    protected IkoraSourceCode ikoraSourceCode = null;
    private SensorContext context = null;

    public final void setRuleKey(RuleKey ruleKey){
        this.ruleKey = ruleKey;
    }

    public RuleKey getRuleKey() {
        return ruleKey;
    }

    public IkoraSourceCode getIkoraSourceCode() {
        return ikoraSourceCode;
    }

    public void setIkoraSourceCode(IkoraSourceCode ikoraSourceCode) {
        this.ikoraSourceCode = ikoraSourceCode;
    }

    public void setContext(SensorContext context){
        this.context = context;
    }

    public int getInt(String key, int defaultValue){
        return this.context.config().getInt(key).orElse(defaultValue);
    }

    public abstract void validate();
}

package lu.uni.serval.ikora.sonar.rules;

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

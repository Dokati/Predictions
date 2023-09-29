package Property.definition;

import PRD.PRDProperty;
import Property.*;

public class NumericPropertyDefinition extends PropertyDefinition {

    private Range range;

    public NumericPropertyDefinition(PRDProperty propertyDef) {
        super(propertyDef);

        if(propertyDef.getPRDRange() != null) {
            this.range = new Range(propertyDef.getPRDRange());
        }
    }

    public Boolean hasRange()  {
        return range != null;
    }

    public Range getRange(){
        return range;
    }

    @Override
    public String toString() {
        if (hasRange()) {
            return super.toString() + ", " + range.toString();
        } else {
            return super.toString();
        }
    }
}


package Expression;
import Context.Context;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Entity.definition.EntityDefinition;
import Exceptions.NotNumericValueException;
import Property.PropertyType;
import Property.definition.EnvPropertyDefinition;
import Property.definition.PropertyDefinition;
import Property.instance.PropertyInstance;

public class Expression {
    final private String expression;
    final private List<String> supportMethods;

    public Expression(String expression) {
        this.expression = expression;
        this.supportMethods = new ArrayList<>();
        initialSupportMethods();
    }

    public String getExpression() {
        return expression;
    }

    private void initialSupportMethods() {
        supportMethods.add("environment");
        supportMethods.add("random");
        supportMethods.add("evaluate");
        supportMethods.add("percent");
        supportMethods.add("ticks");
    }

    public Object getTranslatedValue(Context context){
        return evaluateExpression(context);
    }

    public PropertyType GetTranslatedValueType(EntityDefinition entityDefinition, HashMap<String, EnvPropertyDefinition> environmentProperties){
        if(expressionIsSupportMethod()){
            if(this.expression.contains("environment")){
                List<String> arguments = extractArguments(this.expression);
                String envVarName = arguments.get(0);
                return environmentProperties.get(envVarName).getType();
            }
            return PropertyType.DECIMAL;
        }

        else if (expressionIsPropertyDef(entityDefinition.getProperties())) {
            return entityDefinition.getProperties().get(this.expression).getType();
        }

        else if(isConvertibleToInteger(this.expression)) {
            return PropertyType.DECIMAL;
        }
        else if(isConvertibleToFloat(this.expression)) {
            return PropertyType.FLOAT;
        }
        else if (isConvertibleToBoolean((this.expression))) {
            return PropertyType.BOOLEAN;
        }
        else {
            return PropertyType.STRING;
        }
    }
    private Object evaluateExpression(Context context){

        if(expressionIsSupportMethod()){
            return returnValueOfSupportMethod(context);
        }
        else if (expressionIsProperty(context.getActiveEntityInstance().getProperties())) {
            return propertyValue(context);
        }
        else if(isConvertibleToInteger(this.expression)) {
            return Integer.parseInt(this.expression);
        }
        else if(isConvertibleToFloat(this.expression)) {
            return Float.parseFloat(this.expression);
        }
        else if (isConvertibleToBoolean((this.expression))) {
            return Boolean.parseBoolean(this.expression);
        }

        return expression;
    }

    private Object propertyValue(Context context) {
        return context.getActiveEntityInstance().getProperties().get(this.expression);
    }

    private Boolean expressionIsProperty(Map<String, PropertyInstance> Properties) {
        return Properties.containsKey(this.expression);
    }

    private Boolean expressionIsPropertyDef(Map<String, PropertyDefinition> Properties) {
        return Properties.containsKey(this.expression);
    }

    private Object returnValueOfSupportMethod(Context context) {
        String methodName = null;
        List<String> arguments = extractArguments(this.expression);
        Object result = null;

        for(String method : supportMethods){
            if(this.expression.contains(method)){
                methodName = method;
                break;
            }
        }

        switch (Objects.requireNonNull(methodName)){
            case "environment":
                result = context.environment(arguments.get(0));
                break;
            case  "random":
                if(!isConvertibleToNumber(arguments.get(0))){
                    throw new NotNumericValueException();
                }
                result = context.random(Integer.parseInt(arguments.get(0)));
                break;
            case "evaluate":
                result = context.evaluate(arguments.get(0));
                break;
            case "percent":
                result = context.percent(new Expression(arguments.get(0)),new Expression(arguments.get(1)));
                break;
            case "ticks":
                result = context.ticks(arguments.get(0));
                break;

        }
        return result;
    }

    private Boolean expressionIsSupportMethod() {
        for(String method : supportMethods){
            if(this.expression.contains(method))
                return true;
        }

        return false;
    }

    public List<String> extractArguments(String input) {
        List<String> arguments = new ArrayList<>();

        // Define a regular expression pattern to match arguments inside parentheses
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(input);

        // Find the first match
        if (matcher.find()) {
            String argsGroup = matcher.group(1); // Get the content inside the parentheses
            String[] argsArray = argsGroup.split("\\s*,\\s*"); // Split by comma and trim whitespace

            for (String arg : argsArray) {
                arguments.add(arg);
            }
        }

        return arguments;
    }

    public Boolean isConvertibleToNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Boolean isConvertibleToInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Boolean isConvertibleToFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public Boolean isConvertibleToBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }
}


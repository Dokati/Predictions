package Expression;
import Context.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entity.SecondaryEntity;
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

    public PropertyType GetTranslatedValueType(EntityDefinition entityDefinition , HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties){
        if(expressionIsSupportMethod()){
            if(this.expression.startsWith("environment")){
                List<String> arguments = extractArguments(this.expression);
                if(!environmentProperties.containsKey(arguments.get(0))) {
                    throw new IllegalArgumentException("The environment operation cannot be performed on environment property " + arguments.get(0) + " that does not exist");
                }
                return environmentProperties.get(arguments.get(0)).getType();
            }

            else if(this.expression.startsWith("evaluate")){
                List<String> arguments = extractArguments(this.expression);
                String[] parts = arguments.get(0).split("\\.");

                if (parts.length < 2){
                    throw new IllegalArgumentException("The evaluate function accepts one and only argument of the following format <entity>.<property name>");
                }
                if(!entities.containsKey(parts[0])){
                    throw new IllegalArgumentException("The entity passed in the format to the evaluate function does not exist");
                }
                if(!entities.get(parts[0]).getProperties().containsKey(parts[1])){
                    throw new IllegalArgumentException("The property passed in the format to the evaluate function does not exist in the context of the " + parts[0] + " entity");
                }

                return entities.get(parts[0]).getProperties().get(parts[1]).getType();
            }

            else if(this.expression.startsWith("ticks")){
                List<String> arguments = extractArguments(this.expression);
                String[] parts = arguments.get(0).split("\\.");

                if (parts.length < 2){
                    throw new IllegalArgumentException("The ticks function accepts one and only argument of the following format <entity>.<property name>");
                }
                if(!entities.containsKey(parts[0])){
                    throw new IllegalArgumentException("The entity passed in the format to the ticks function does not exist");
                }
                if(!entities.get(parts[0]).getProperties().containsKey(parts[1])){
                    throw new IllegalArgumentException("The property passed in the format to the ticks function does not exist in the context of the " + parts[0] + " entity");
                }

                return PropertyType.FLOAT;
            }

            else if(this.expression.startsWith("percent")) {
                List<String> arguments = extractArgumentsPercent(this.expression);

                if(arguments.size() < 2){
                    throw new IllegalArgumentException("The percent function should accept 2 arguments");
                }

                if(!new Expression(arguments.get(0)).GetTranslatedValueType(entityDefinition,entities,environmentProperties).equals(PropertyType.FLOAT) ||
                        !new Expression(arguments.get(1)).GetTranslatedValueType(entityDefinition,entities,environmentProperties).equals(PropertyType.FLOAT)){
                    throw new IllegalArgumentException("The percent function accepts only numeric arguments");
                }

                return PropertyType.FLOAT;
            }

            else if(this.expression.startsWith("random")) {
                List<String> arguments = extractArguments(this.expression);
                try{
                    PropertyType.DECIMAL.convert(arguments.get(0));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("The random function accepts only positive integers");
                }

                return PropertyType.FLOAT;
            }

            return PropertyType.FLOAT;
        }
        else if (expressionIsProperty(entityDefinition.getProperties())) {
            return entityDefinition.getProperties().get(this.expression).getType();
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
        else if (expressionIsProperty(context.getActiveEntityInstance().getEntityDef().getProperties())) {
            return propertyValue(context.getActiveEntityInstance().getProperties());
        }
        else if(isConvertibleToFloat(this.expression)) {
            return Float.parseFloat(this.expression);
        }
        else if (isConvertibleToBoolean((this.expression))) {
            return Boolean.parseBoolean(this.expression);
        }

        return expression;
    }

    private Object propertyValue(Map<String, PropertyInstance> Properties) {
        return Properties.get(this.expression).getValue();
    }

    private Boolean expressionIsProperty(Map<String, PropertyDefinition> Properties) {
        return Properties.containsKey(this.expression);
    }

    private Object returnValueOfSupportMethod(Context context) {
        String methodName = null;
        Object result = null;
        List<String> arguments = null;

        for(String method : supportMethods){
            if(this.expression.startsWith(method)){
                methodName = method;
                break;
            }
        }

        switch (Objects.requireNonNull(methodName)){
            case "environment":
                arguments = extractArguments(this.expression);
                result = context.environment(arguments.get(0));
                break;
            case  "random":
                arguments = extractArguments(this.expression);
                if(!isConvertibleToNumber(arguments.get(0))){
                    throw new NotNumericValueException();
                }
                result = context.random(Integer.parseInt(arguments.get(0)));
                break;
            case "evaluate":
                arguments = extractArguments(this.expression);
                result = context.evaluate(arguments.get(0));
                break;
            case "percent":
                arguments = extractArgumentsPercent(this.expression);
                result = context.percent(new Expression(arguments.get(0)),new Expression(arguments.get(1)));
                break;
            case "ticks":
                arguments = extractArguments(this.expression);
                result = context.ticks(arguments.get(0));
                break;

        }
        return result;
    }

    private Boolean expressionIsSupportMethod() {
        for(String method : supportMethods){
            if(this.expression.startsWith(method))
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

    public List<String> extractArgumentsPercent(String input) {
        List<String> arguments = new ArrayList<>();
        String regex = "\\b(evaluate|environment|percent|ticks|random)\\([^()]*\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String argument = matcher.group();
            arguments.add(argument);
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


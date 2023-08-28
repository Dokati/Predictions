package Property;

import java.util.Random;

public enum PropertyType
{
    BOOLEAN {
        @Override
        public Object randomInitialization(Range range) {
            Random random = new Random();
            return random.nextBoolean();
        }

        @Override
        public Boolean convert(Object value) {
            if(value instanceof String) {
                if(((String) value).equalsIgnoreCase("false") || ((String) value).equalsIgnoreCase("true")){
                    return Boolean.parseBoolean((String) value);
                }
                else {
                    throw new RuntimeException("Value of type " + value.getClass().getSimpleName() +
                            "cannot be converted to Boolean");
                }
            }
            else if(value instanceof Boolean) {
                return (Boolean)value;
            }
            else {
                throw new RuntimeException("Value of type " + value.getClass().getSimpleName() +
                        "cannot be converted to Boolean");
            }
        }
    },
    STRING {
        @Override
        public Object randomInitialization(Range range) {
            String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !?,_-().";
            int maxLength = 50;
            Random random = new Random();
            int length = random.nextInt(maxLength) + 1;
            StringBuilder value = new StringBuilder();

            for (int i = 0; i < length; i++) {
                int index = random.nextInt(characters.length());
                char randomChar = characters.charAt(index);
                value.append(randomChar);
            }

            return value.toString();
        }

        @Override
        public String convert(Object value) {
            if(value instanceof Integer) {
                return value.toString();
            }
            else if(value instanceof Float) {
                return value.toString();
            }
            else if(value instanceof String) {
                return (String) value;
            }
            else  {
                throw new RuntimeException("Value of type " + value.getClass().getSimpleName() +
                        "cannot be converted to String");
            }
        }

    },
    DECIMAL {
        @Override
        public Object randomInitialization(Range range) {
            Random random = new Random();

            if (range != null){
                int minValue = range.getFrom().intValue();
                int maxValue = range.getTo().intValue();
                return random.nextInt(maxValue - minValue + 1) + minValue;
            }
            else {
                return random.nextInt();
            }
        }

        @Override
        public Integer convert(Object value) {
            if(value instanceof String) {
                return Integer.parseInt((String) value);
            }
            else if(value instanceof Float) {
                return ((Float)value).intValue();
            }
            else if(value instanceof Integer) {
                return (Integer) value;
            }
            else {
                throw new RuntimeException("Value of type " + value.getClass().getSimpleName() +
                        "cannot be converted to Integer");
            }
        }
    },
    FLOAT {
        @Override
        public Float randomInitialization(Range range) {
            Random random = new Random();

            if (range != null){
                float minValue = range.getFrom();
                float maxValue = range.getTo();
                return minValue + random.nextFloat() * (maxValue - minValue);
            }
            else {
                return random.nextFloat();
            }
        }

        @Override
        public Float convert(Object value) {
            if(value instanceof String) {
                return Float.parseFloat((String) value);
            }
            else if(value instanceof Integer) {
                return ((Integer)value).floatValue();
            }
            else if(value instanceof Float) {
                return (Float) value;
            }
            else {
                throw new RuntimeException("Value of type " + value.getClass().getSimpleName() +
                        "cannot be converted to Float");
            }
        }
    };

    public abstract Object randomInitialization(Range range);
    public abstract <T> T convert(Object value);
}

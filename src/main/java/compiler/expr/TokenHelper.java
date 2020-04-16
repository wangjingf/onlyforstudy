package compiler.expr;

import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.LinkedHashMap;
import java.util.Map;

public class TokenHelper {
    private static Map<String,TokenType> symbolTokens = new LinkedCaseInsensitiveMap<>();

    private static Map<String,TokenType> keywordTokens = new LinkedCaseInsensitiveMap<>();
    static {
        for (TokenType tokenType : TokenType.values()) {
            if(tokenType.getName() != null){
                if(tokenType.getName().length() == 1){
                    symbolTokens.put(tokenType.getName(),tokenType);
                }
                char c = tokenType.getName().charAt(0);
                if(Character.isLetter(c)){
                    keywordTokens.put(tokenType.getName(),tokenType);
                }
            }

        }
    }
    public static TokenType getSymbolToken(String name){
        return symbolTokens.get(name);
    }
    public static TokenType getSymbolToken(char name){
        return symbolTokens.get(Character.valueOf(name).toString());
    }
    public static boolean isSymbolToken(char name){
        return getSymbolToken(name) != null;
    }
    public static boolean isSymbolToken(String name){
        return getSymbolToken(name) != null;
    }
    public static TokenType getKeywordToken(String name){
        return keywordTokens.get(name);
    }
    public static boolean isKeywordToken(String name){
        return getKeywordToken(name) != null;
    }
}

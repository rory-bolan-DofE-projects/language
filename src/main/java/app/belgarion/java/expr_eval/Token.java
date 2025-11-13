package app.belgarion.java.expr_eval;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Token {
    public Tokens TokenType;
    @Nullable public Integer value;

    public Token(Tokens type, @Nullable Integer value) {
        this.TokenType = type;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }


        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }


        Token otherToken = (Token) obj;


        return this.TokenType == otherToken.TokenType &&
                Objects.equals(this.value, otherToken.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TokenType, value);
    }
}

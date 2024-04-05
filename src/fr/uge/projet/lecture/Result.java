package fr.uge.projet.lecture;
import java.util.Objects;


public record Result(Token token, String content) {
    public Result {
      Objects.requireNonNull(token);
      Objects.requireNonNull(content);
    }
  }
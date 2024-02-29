package teamb.w4e.interfaces;

import java.util.Optional;

public interface SkiPass {
    Optional<String> reserve(String name, String activity);
}

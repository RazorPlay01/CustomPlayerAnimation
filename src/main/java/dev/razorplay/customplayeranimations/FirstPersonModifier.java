package dev.razorplay.customplayeranimations;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public class FirstPersonModifier extends AbstractModifier {
    private final FirstPersonConfiguration currentConfig = new FirstPersonConfiguration(true, false, true, false);
    @Setter
    private boolean enabled = false;

    @Override
    public @NotNull FirstPersonConfiguration getFirstPersonConfiguration(float tickDelta) {
        return enabled ? new FirstPersonConfiguration(true, true, true, true) : currentConfig;
    }
}
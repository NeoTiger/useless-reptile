package nordmods.uselessreptile.common.dragon_variant;

import net.minecraft.entity.attribute.EntityAttributeModifier;

public record VariantAttributeModifier(String attributeId, float value, EntityAttributeModifier.Operation operation) {
}

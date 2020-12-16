package com.alexlabbane.christmasmanhunt.hostilesnowman;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;

import net.minecraft.server.v1_16_R2.AttributeModifiable;
import net.minecraft.server.v1_16_R2.ChatComponentText;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.EntitySnowman;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.GenericAttributes;
import net.minecraft.server.v1_16_R2.PathfinderGoalNearestAttackableTarget;

public class EntityHostileSnowman extends EntitySnowman {

	private static Field attributeField;
	
	public EntityHostileSnowman(Location loc) {
		super(EntityTypes.SNOW_GOLEM, ((CraftWorld) loc.getWorld()).getHandle());
		this.setPosition(loc.getX(), loc.getY(), loc.getZ());
		
		this.setCustomName(new ChatComponentText("Snow Minion"));
		this.setCustomNameVisible(true);
		
//		try {
//            registerGenericAttribute(this.getBukkitEntity(), Attribute.GENERIC_MOVEMENT_SPEED);
//        } catch (IllegalAccessException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
	}
	
	@Override
	public void initPathfinder() {
		super.initPathfinder();
        this.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
        
        //this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.MOVEMENT_SPEED, (a) -> {a.setValue(0.2);}));
	}
	
    static {
        try {
            attributeField = net.minecraft.server.v1_16_R2.AttributeMapBase.class.getDeclaredField("b");
            attributeField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void registerGenericAttribute(org.bukkit.entity.Entity entity, Attribute attribute) throws IllegalAccessException {
        net.minecraft.server.v1_16_R2.AttributeMapBase attributeMapBase = ((org.bukkit.craftbukkit.v1_16_R2.entity.CraftLivingEntity)entity).getHandle().getAttributeMap();
        Map<net.minecraft.server.v1_16_R2.AttributeBase, net.minecraft.server.v1_16_R2.AttributeModifiable> map = (Map<net.minecraft.server.v1_16_R2.AttributeBase, net.minecraft.server.v1_16_R2.AttributeModifiable>) attributeField.get(attributeMapBase);
        net.minecraft.server.v1_16_R2.AttributeBase attributeBase = org.bukkit.craftbukkit.v1_16_R2.attribute.CraftAttributeMap.toMinecraft(attribute);
        net.minecraft.server.v1_16_R2.AttributeModifiable attributeModifiable = new net.minecraft.server.v1_16_R2.AttributeModifiable(attributeBase, net.minecraft.server.v1_16_R2.AttributeModifiable::getAttribute);
        map.put(attributeBase, attributeModifiable);
    }
}
